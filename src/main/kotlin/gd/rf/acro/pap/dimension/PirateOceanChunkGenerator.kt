package gd.rf.acro.pap.dimension

import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import net.minecraft.block.BlockState
import net.minecraft.block.Blocks
import net.minecraft.util.math.BlockPos
import net.minecraft.world.BlockView
import net.minecraft.world.ChunkRegion
import net.minecraft.world.Heightmap
import net.minecraft.world.WorldAccess
import net.minecraft.world.biome.source.BiomeSource
import net.minecraft.world.chunk.Chunk
import net.minecraft.world.gen.StructureAccessor
import net.minecraft.world.gen.chunk.ChunkGenerator
import net.minecraft.world.gen.chunk.StructuresConfig
import net.minecraft.world.gen.chunk.VerticalBlockSample
import java.util.*
import java.util.function.Function


class PirateOceanChunkGenerator(biomeSource: BiomeSource?) : ChunkGenerator(biomeSource, StructuresConfig(Optional.empty(), emptyMap())) {
    override fun getColumnSample(x: Int, z: Int): BlockView? {
        return VerticalBlockSample(arrayOfNulls(0))
    }

    override fun method_28506(): Codec<out ChunkGenerator> {
        return CODEC
    }

    override fun getHeight(x: Int, z: Int, heightmapType: Heightmap.Type?): Int {
        return 256
    }

    override fun buildSurface(region: ChunkRegion?, chunk: Chunk?) {

    }

    override fun populateNoise(world: WorldAccess?, accessor: StructureAccessor?, chunk: Chunk?) {
        val blockStates: Array<BlockState> = layerBlocks;
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

    companion object {
        @JvmField
        val CODEC: Codec<PirateOceanChunkGenerator> = RecordCodecBuilder.create { instance: RecordCodecBuilder.Instance<PirateOceanChunkGenerator> ->
            instance.group(
                    BiomeSource.field_24713.fieldOf("biome_source")
                            .forGetter { generator: PirateOceanChunkGenerator -> generator.biomeSource }
            ).apply(instance, instance.stable(Function { biomeSource: BiomeSource? -> PirateOceanChunkGenerator(biomeSource) }))
        }

        //private val layerBlocks = arrayOfNulls<BlockState>(256);
        private val layerBlocks = {
            val blk = arrayOfNulls<BlockState>(256)
            for(i in 0..3) {
                blk[i] = Blocks.BEDROCK.defaultState!!
            }
            for (i in 4..16) {
                blk[i] = Blocks.SAND.defaultState!!
            }
            for (i in 17..64) {
                blk[i] = Blocks.WATER.defaultState!!
            }
            for (i in 65..255) {
                blk[i] = Blocks.AIR.defaultState!!
            }
            blk
        }() as Array<BlockState>

    }
}
