package gd.rf.acro.pap.world;

import com.mojang.serialization.Codec;
import net.minecraft.block.Blocks;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ServerWorldAccess;
import net.minecraft.world.gen.StructureAccessor;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.gen.feature.DefaultFeatureConfig;
import net.minecraft.world.gen.feature.Feature;
import org.apache.commons.lang3.RandomUtils;

import java.util.Random;

public class PortTownFeature extends Feature<DefaultFeatureConfig> {
    private final String[] buildings = {"port_town_tannery","port_town_archer","port_town_farmer","port_town_library"};
    public PortTownFeature(Codec<DefaultFeatureConfig> codec) {
        super(codec);
    }

    @Override
    public boolean generate(ServerWorldAccess serverWorldAccess, StructureAccessor accessor, ChunkGenerator generator, Random random, BlockPos pos, DefaultFeatureConfig config) {
        serverWorldAccess.setBlockState(pos, Blocks.DIAMOND_BLOCK.getDefaultState(),3);
        serverWorldAccess.setBlockState(pos.up(1), Blocks.DIAMOND_BLOCK.getDefaultState(),3);
        serverWorldAccess.setBlockState(pos.up(2), Blocks.DIAMOND_BLOCK.getDefaultState(),3);
        serverWorldAccess.setBlockState(pos.up(3), Blocks.DIAMOND_BLOCK.getDefaultState(),3);
        System.out.println("generating");

        return true;
    }
}
