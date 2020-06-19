package gd.rf.acro.blockwake.blocks;

import gd.rf.acro.blockwake.Blockwake;
import gd.rf.acro.blockwake.entities.PirateEntity;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.passive.VillagerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;

import java.util.Random;

public class StaticMobSpawner extends Block {
    private EntityType spawnType;
    public StaticMobSpawner(Settings settings,EntityType type) {
        super(settings);
        this.spawnType=type;
    }



    @Override
    public void scheduledTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
        super.scheduledTick(state, world, pos, random);
        if(spawnType==EntityType.VILLAGER)
        {
            VillagerEntity entity = new VillagerEntity(EntityType.VILLAGER,world);
            entity.teleport(pos.getX(),pos.getY(),pos.getZ());
            world.spawnEntity(entity);
        }
        if(spawnType== Blockwake.PIRATE_ENTITY_ENTITY_TYPE)
        {
            PirateEntity entity = new PirateEntity(Blockwake.PIRATE_ENTITY_ENTITY_TYPE,world);
            entity.teleport(pos.getX(),pos.getY(),pos.getZ());
            world.spawnEntity(entity);
        }
        world.setBlockState(pos, Blocks.AIR.getDefaultState());
    }
}
