package gd.rf.acro.pap.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.passive.VillagerEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.Random;

public class StaticMobSpawner extends Block {
    public StaticMobSpawner(Settings settings) {
        super(settings);
    }



    @Override
    public void scheduledTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
        super.scheduledTick(state, world, pos, random);
        VillagerEntity entity = new VillagerEntity(EntityType.VILLAGER,world);
        entity.teleport(pos.getX(),pos.getY(),pos.getZ());
        world.spawnEntity(entity);
        world.setBlockState(pos, Blocks.AIR.getDefaultState());
    }
}
