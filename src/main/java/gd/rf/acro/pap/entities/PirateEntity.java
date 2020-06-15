package gd.rf.acro.pap.entities;

import net.minecraft.block.Blocks;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.mob.SkeletonEntity;
import net.minecraft.entity.passive.IronGolemEntity;
import net.minecraft.entity.passive.TurtleEntity;
import net.minecraft.entity.passive.VillagerEntity;
import net.minecraft.entity.passive.WolfEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.Box;
import net.minecraft.world.LocalDifficulty;
import net.minecraft.world.World;

import java.util.Collections;
import java.util.List;

public class PirateEntity extends SkeletonEntity {
    public PirateEntity(EntityType<? extends SkeletonEntity> entityType, World world) {
        super(entityType, world);
        this.equipStack(EquipmentSlot.HEAD,new ItemStack(Items.DIAMOND_HELMET));
    }

    @Override
    public boolean isUndead() {
        return false;
    }

    @Override
    protected void initGoals() {
        this.goalSelector.add(5, new WanderAroundFarGoal(this, 1.0D));
        this.goalSelector.add(6, new LookAtEntityGoal(this, PlayerEntity.class, 8.0F));
        this.goalSelector.add(6, new LookAroundGoal(this));
        this.targetSelector.add(1, new RevengeGoal(this, new Class[0]));
        this.targetSelector.add(2, new FollowTargetGoal(this, PlayerEntity.class, true));

    }

    @Override
    public void tickMovement() {
        super.tickMovement();

        if(this.getTarget()!=null)
        {
            if(world.getBlockState(this.getBlockPos().down()).getBlock()==Blocks.AIR && this.getTarget().getY()>=this.getY())
            {
                world.setBlockState(this.getBlockPos().down(), Blocks.OAK_PLANKS.getDefaultState());
                if(this.getTarget() instanceof  PlayerEntity)
                {
                    this.getTarget().playSound(SoundEvents.BLOCK_WOOD_PLACE,1,1);

                }
            }
            if(this.getTarget().getY()>this.getY())
            {
                world.setBlockState(this.getBlockPos(), Blocks.OAK_PLANKS.getDefaultState());
                if(this.getTarget() instanceof  PlayerEntity)
                {
                    this.getTarget().playSound(SoundEvents.BLOCK_WOOD_PLACE,1,1);
                }
                this.jump();
            }

        }
    }

}
