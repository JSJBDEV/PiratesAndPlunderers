package gd.rf.acro.blockwake.blocks;

import gd.rf.acro.blockwake.Blockwake;
import gd.rf.acro.blockwake.engagement.EngagementManager;
import gd.rf.acro.blockwake.engagement.EngagementManagerKt;
import gd.rf.acro.blockwake.entities.SailingShipEntity;
import net.fabricmc.fabric.api.dimension.v1.FabricDimensions;
import net.minecraft.block.Block;
import net.minecraft.block.BlockEntityProvider;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.LiteralText;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.dimension.DimensionType;
import org.apache.commons.lang3.RandomUtils;

import java.util.ArrayList;
import java.util.stream.Collectors;

public class ShipWheelBlock extends Block implements BlockEntityProvider {
    public ShipWheelBlock(Settings settings) {
        super(settings);
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        if(player.getEntityWorld().getRegistryKey()==Blockwake.PIRATE_OCEAN_WORLD && !world.isClient)
        {
            ShipWheelBlockEntity shipWheelBlockEntity = (ShipWheelBlockEntity) world.getBlockEntity(pos);
            shipWheelBlockEntity.interact();
            SailingShipEntity vv = EngagementManager.INSTANCE.getEntityToShipEntityMap().get(player);
            SailingShipEntity other = EngagementManager.INSTANCE.getShipToShipEngagementsMap().get(vv);
            int vvv = EngagementManager.INSTANCE.getEntityToShipEntityMap().keySet().stream().filter(e -> EngagementManager.INSTANCE.getEntityToShipEntityMap().get(e) == other).toArray().length;
            if(RandomUtils.nextInt(0,vvv+1)==0)
            {
                EngagementManager.INSTANCE.finishEngagement(player);
            }
            else
            {
                player.sendMessage(new LiteralText("You didn't manage to escape"),false);
            }
        }
        return super.onUse(state, world, pos, player, hand, hit);
    }

    @Override
    public BlockEntity createBlockEntity(BlockView world) {
        return new ShipWheelBlockEntity();
    }
}
