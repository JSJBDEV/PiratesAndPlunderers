package gd.rf.acro.pap.entities;

import gd.rf.acro.pap.engagement.NoCurrentEngagementException;
import gd.rf.acro.pap.PiratesAndPlunderers;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.util.math.Vector3f;
import net.minecraft.entity.Entity;
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
import net.minecraft.util.Pair;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.World;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SailingShipEntity extends PigEntity {
    public SailingShipEntity(EntityType<? extends PigEntity> entityType, World world) {
        super(entityType, world);
    }

    protected SailingShipEntity engagedWithEntity;

    @Override
    public boolean canWalkOnFluid(Fluid fluid) {
        return fluid.isIn(FluidTags.WATER);
    }

    public static List<Pair<BlockState, BlockPos>> readModelBlocks(String dataString) {
        String[] data = dataString.split(System.lineSeparator());

        List<Pair<BlockState, BlockPos>> result = new ArrayList<>();

        for (String line : data) {
            String[] compound = line.split(" ");

            BlockPos pos = new BlockPos(Integer.parseInt(compound[1]), Integer.parseInt(compound[1])-4, Integer.parseInt(compound[1]));
            BlockState block = Block.getStateFromRawId(Integer.parseInt(compound[0]));

            result.add(new Pair<>(block, pos));
        }
        return result;
    }

    public List<Pair<BlockState, BlockPos>> getModelBlocksForEntity() throws IOException {
        return readModelBlocks(FileUtils.readFileToString(new File("./config/PiratesAndPlunderers/ships/ship1.blocks"),"utf-8"));
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
    public boolean isEngaged() {
        return engagedWithEntity != null;
    }

    public void setEngagedWithShipEntity(SailingShipEntity other) {
        engagedWithEntity = other;
    }

    public void setDisengaged() throws NoCurrentEngagementException {
        if (engagedWithEntity == null)
            throw new NoCurrentEngagementException();
        engagedWithEntity = null;
    }

    @Override
    public List<Entity> getPassengerList() {
        // Does this even work?
        // Pirate entities can be 'Created' here
        return super.getPassengerList();
    }

    @Override
    public boolean isInvulnerable() {
        return super.isInvulnerable() || this.isEngaged();
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
