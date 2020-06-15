package gd.rf.acro.pap.entities;

import gd.rf.acro.pap.PiratesAndPlunderers;
import net.minecraft.block.Blocks;
import net.minecraft.client.util.math.Vector3f;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.passive.PigEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.Fluid;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.tag.FluidTags;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3i;
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
        return this.getPrimaryPassenger() instanceof PlayerEntity;
    }

    @Override
    public boolean canMoveVoluntarily() {
        return false;
    }

    @Override
    protected void initGoals() {

    }
    public void setModel(String name)
    {
        ItemStack itemStack = new ItemStack(Items.OAK_PLANKS);
        CompoundTag tag = new CompoundTag();
        tag.putString("model",name);
        itemStack.setTag(tag);
        this.equipStack(EquipmentSlot.CHEST,itemStack);
    }

    @Override
    public float getSaddledSpeed() {
        if(this.world.getBlockState(this.getBlockPos().down()).getBlock()== Blocks.WATER)
        {
            return 1;
        }
        return 0.1f;
    }

    @Override
    public boolean cannotDespawn() {
        return true;
    }

    @Override
    public ActionResult interactAt(PlayerEntity player, Vec3d hitPos, Hand hand) {
        if(!player.getEntityWorld().isClient && player.getStackInHand(hand)==ItemStack.EMPTY)
        {
            player.startRiding(this,true);
        }
        if(!player.getEntityWorld().isClient && player.getStackInHand(hand).getItem()==Items.DIAMOND)
        {
            PirateEntity entity = new PirateEntity(PiratesAndPlunderers.PIRATE_ENTITY_ENTITY_TYPE,player.getEntityWorld());
            entity.teleport(this.getX(),this.getY(),this.getZ());
            player.getEntityWorld().spawnEntity(entity);
            entity.startRiding(this,true);
        }
        return super.interactAt(player, hitPos, hand);
    }

    @Override
    public void updatePassengerPosition(Entity passenger) {
        Vec3d v = Vec3d.of(this.getMovementDirection().getOpposite().getVector()).multiply(this.getPassengerList().indexOf(passenger)).add(this.getPos());
        passenger.updatePosition(v.x,v.y+5,v.z);

    }
}
