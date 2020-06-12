package gd.rf.acro.pap.entities;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.passive.PigEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.Fluid;
import net.minecraft.tag.FluidTags;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class SailingShipEntity extends PigEntity {
    public SailingShipEntity(EntityType<? extends PigEntity> entityType, World world) {
        super(entityType, world);
    }
    @Override
    public boolean canWalkOnFluid(Fluid fluid) {
        return fluid.isIn(FluidTags.WATER);
    }

    @Override
    protected boolean canClimb() {
        return false;
    }

    @Override
    public boolean canBeControlledByRider() {
        return true;
    }

    @Override
    protected void initGoals() {

    }

    @Override
    public boolean cannotDespawn() {
        return true;
    }

    @Override
    public ActionResult interactAt(PlayerEntity player, Vec3d hitPos, Hand hand) {
        player.startRiding(this);
        return super.interactAt(player, hitPos, hand);
    }
}
