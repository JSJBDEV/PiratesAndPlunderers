package gd.rf.acro.blockwake.blocks;

import gd.rf.acro.blockwake.Blockwake;
import net.fabricmc.fabric.api.dimension.v1.FabricDimensions;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ShipWheelBlock extends Block {
    public ShipWheelBlock(Settings settings) {
        super(settings);
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        if(world==world.getServer().getWorld(Blockwake.PIRATE_OCEAN_WORLD))
        {
            ShipWheelBlockEntity shipWheelBlockEntity = (ShipWheelBlockEntity) world.getBlockEntity(pos);
            //call method
            shipWheelBlockEntity.interact();
        }
        return super.onUse(state, world, pos, player, hand, hit);
    }
}
