package gd.rf.acro.blockwake.dimension

import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import net.minecraft.block.BlockState
import net.minecraft.block.Blocks
import net.minecraft.structure.StructureManager
import net.minecraft.util.math.BlockPos
import net.minecraft.world.BlockView
import net.minecraft.world.ChunkRegion
import net.minecraft.world.Heightmap
import net.minecraft.world.WorldAccess
import net.minecraft.world.biome.source.BiomeSource
import net.minecraft.world.chunk.Chunk
import net.minecraft.world.gen.StructureAccessor
import net.minecraft.world.gen.chunk.*
import java.util.*
import java.util.function.Function
import java.util.function.IntFunction


class PirateOceanChunkGenerator(biomeSource: BiomeSource?) : ChunkGenerator(biomeSource, StructuresConfig(Optional.empty(), emptyMap())) {

    override fun getColumnSample(x: Int, z: Int): BlockView? {
        return VerticalBlockSample(Arrays.stream(layerBlocks).map(Function { state: BlockState? ->
            state ?: Blocks.AIR.defaultState
        }).toArray(IntFunction { i: Int -> arrayOfNulls<BlockState>(i) }) as Array<BlockState?>)
    }

    override fun setStructureStarts(structureAccessor: StructureAccessor?, chunk: Chunk?, structureManager: StructureManager?, l: Long) {
        // Do nothing lol
    }

    override fun addStructureReferences(world: WorldAccess?, accessor: StructureAccessor?, chunk: Chunk?) {
        // Do nothing lol
    }

    override fun method_28506(): Codec<out ChunkGenerator> {
        return CODEC
    }

    override fun getSpawnHeight(): Int {
        val blockStates: Array<BlockState?> = layerBlocks
        for (i in blockStates.indices) {
            val blockState = (if (blockStates[i] == null) Blocks.AIR.defaultState else blockStates[i])!!
            if (!Heightmap.Type.MOTION_BLOCKING.blockPredicate.test(blockState)) {
                return i - 1
            }
        }
        return blockStates.size
    }

    override fun buildSurface(region: ChunkRegion?, chunk: Chunk?) {

    }

    override fun populateNoise(world: WorldAccess?, accessor: StructureAccessor?, chunk: Chunk?) {
        val blockStates: Array<BlockState?> = layerBlocks;
        val mutable = BlockPos.Mutable()
        val heightmap = chunk!!.getHeightmap(Heightmap.Type.OCEAN_FLOOR_WG)
        val heightmap2 = chunk.getHeightmap(Heightmap.Type.WORLD_SURFACE_WG)

        for (i in blockStates.indices) {
            val blockState = blockStates[i]
            if (blockState != null) {
                for (j in 0..15) {
                    for (k in 0..15) {
                        chunk.setBlockState(mutable.set(j, i, k), blockState, false)
                        heightmap.trackUpdate(j, i, k, blockState)
                        heightmap2.trackUpdate(j, i, k, blockState)
                    }
                }
            }
        }
    }


    override fun withSeed(seed: Long): ChunkGenerator {
        return this
    }

    override fun getHeight(x: Int, z: Int, heightmapType: Heightmap.Type): Int {
        val blockStates: Array<BlockState?> = layerBlocks;
        for (i in blockStates.indices.reversed()) {
            val blockState = blockStates[i]
            if (blockState != null && heightmapType.blockPredicate.test(blockState)) {
                return i + 1
            }
        }
        return 0
    }

    companion object {
        @JvmField
        val CODEC: Codec<PirateOceanChunkGenerator> = RecordCodecBuilder.create { instance: RecordCodecBuilder.Instance<PirateOceanChunkGenerator> ->
            instance.group(
                    BiomeSource.field_24713.fieldOf("biome_source")
                            .forGetter { generator: PirateOceanChunkGenerator -> generator.biomeSource }
            ).apply(instance, instance.stable(Function { biomeSource: BiomeSource? -> PirateOceanChunkGenerator(biomeSource) }))
        }

        //private val layerBlocks = arrayOfNulls<BlockState>(256);
        val layerBlocks = {
            val blk = arrayOfNulls<BlockState>(256)
            for(i in 0..3) {
                blk[i] = Blocks.BEDROCK.defaultState!!
            }
            for (i in 4..16) {
                blk[i] = Blocks.SANDSTONE.defaultState!!
            }
            for (i in 17..64) {
                blk[i] = Blocks.WATER.defaultState!!
            }
            blk
        }()

        fun getFilledBlockStates(): Array<BlockState> {
            val new = arrayOfNulls<BlockState>(256);
            for(i in 0..256) {
                new[i] = layerBlocks[i] ?: Blocks.AIR.defaultState
            }
            return new as Array<BlockState>
        }

    }
}
