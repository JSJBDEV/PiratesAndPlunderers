package gd.rf.acro.pap.blocks;

import gd.rf.acro.pap.PiratesAndPlunderers;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.client.util.math.Vector3f;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.FireballEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.*;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;

public class StaticCannonBlock extends Block {
    public StaticCannonBlock(Settings settings) {
        super(settings);
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        if(hand==Hand.MAIN_HAND && player.getStackInHand(hand).getItem()==PiratesAndPlunderers.ASTROLABE_ITEM)
        {
            if(player.getStackInHand(hand).hasTag())
            {
                Vec3d dir = Vec3d.of(hit.getSide().getOpposite().getVector());
                FireballEntity entity = new FireballEntity(world,pos.getX()+0.5+dir.x*2,pos.getY()+0.5+dir.y*2,pos.getZ()+0.5+dir.z*2,0,-0.01,0);
                entity.explosionPower=1;
                entity.setItem(new ItemStack(Items.STONE));
                CompoundTag tag = player.getStackInHand(hand).getTag();
                BlockPos target = new BlockPos(tag.getDouble("x"),tag.getDouble("y"),tag.getDouble("z"));
                entity.setVelocity(Vec3d.ofCenter(target.add(-pos.getX(),-pos.getY(),-pos.getZ())).multiply(3/Math.sqrt(pos.getSquaredDistance(tag.getDouble("x"),tag.getDouble("y"),tag.getDouble("z"),false))));
                world.spawnEntity(entity);
            }
        }
        return super.onUse(state, world, pos, player, hand, hit);
    }
}
