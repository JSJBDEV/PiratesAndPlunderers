package gd.rf.acro.blockwake.items;

import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.world.World;

import java.util.List;

public class AstrolabeItem extends Item {
    public AstrolabeItem(Settings settings) {
        super(settings);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        HitResult result = user.rayTrace(100,1,true);
        if(result!=null && user.isSneaking())
        {
            CompoundTag tag = new CompoundTag();
            tag.putDouble("x",result.getPos().x);
            tag.putDouble("y",result.getPos().y);
            tag.putDouble("z",result.getPos().z);
            user.getStackInHand(hand).setTag(tag);
        }
        return super.use(world, user, hand);
    }

    @Override
    public void appendTooltip(ItemStack stack, World world, List<Text> tooltip, TooltipContext context) {
        super.appendTooltip(stack, world, tooltip, context);
        if(stack.hasTag())
        {
            CompoundTag tag = stack.getTag();
            tooltip.add(new LiteralText("X: "+tag.getDouble("x")));
            tooltip.add(new LiteralText("Y: "+tag.getDouble("y")));
            tooltip.add(new LiteralText("Z: "+tag.getDouble("z")));
        }
    }
}
