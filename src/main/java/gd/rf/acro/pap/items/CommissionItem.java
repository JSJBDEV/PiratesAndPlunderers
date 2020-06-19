package gd.rf.acro.pap.items;

import gd.rf.acro.pap.PiratesAndPlunderers;
import gd.rf.acro.pap.entities.SailingShipEntity;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;
import org.apache.commons.lang3.StringUtils;

import java.util.List;

public class CommissionItem extends Item {

    public CommissionItem(Settings settings) {
        super(settings);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        if(hand==Hand.MAIN_HAND && user.getStackInHand(hand).hasTag())
        {
            String model = user.getStackInHand(hand).getTag().getString("storedModel");
            SailingShipEntity sailingShipEntity = new SailingShipEntity(PiratesAndPlunderers.SAILING_BOAT_ENTITY_ENTITY_TYPE,world);
            sailingShipEntity.setModel(model);
            sailingShipEntity.teleport(user.getX(),user.getY(),user.getZ());
            world.spawnEntity(sailingShipEntity);
            user.getStackInHand(hand).decrement(1);
        }
        return super.use(world, user, hand);
    }

    @Override
    public void appendTooltip(ItemStack stack, World world, List<Text> tooltip, TooltipContext context) {
        super.appendTooltip(stack, world, tooltip, context);
        if(stack.hasTag())
        {
            String format = stack.getTag().getString("storedModel").replace("_"," ");
            format= StringUtils.capitalize(format);
            tooltip.add(new LiteralText("Use this item to spawn a ship!"));
            tooltip.add(new LiteralText("Commission: "+format));
        }
    }
}
