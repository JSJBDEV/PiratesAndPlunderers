package gd.rf.acro.pap.items;

import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.passive.VillagerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.Box;
import net.minecraft.world.World;
import org.apache.commons.lang3.RandomUtils;

import java.util.List;

public class RecruitmentBookItem extends Item {
    public RecruitmentBookItem(Settings settings) {
        super(settings);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        if(!user.getItemCooldownManager().isCoolingDown(this) && hand==Hand.MAIN_HAND && !world.isClient)
        {
            int searchrange = 20;
            List<VillagerEntity> villagers = user.getEntityWorld().getEntities(
                VillagerEntity.class,
                new Box(user.getBlockPos().add(0-searchrange,0-searchrange,0-searchrange),user.getBlockPos().add(searchrange,searchrange,searchrange)),VillagerEntity::isAlive);
            CompoundTag tag = new CompoundTag();
            if(user.getStackInHand(hand).hasTag())
            {
                tag=user.getStackInHand(hand).getTag();
            }
            for (VillagerEntity villager : villagers) {
                if (RandomUtils.nextInt(0, 10) == 0) {
                    user.sendMessage(new LiteralText("A villager has joined your crew"), false);
                    if (tag.contains("crew")) {
                        tag.putInt("crew", tag.getInt("crew") + 1);
                    } else {
                        tag.putInt("crew", 1);
                    }
                    villager.teleport(0, -50, 0);
                }
            }
            user.getStackInHand(hand).setTag(tag);
            user.getItemCooldownManager().set(this,100);
        }
        return super.use(world, user, hand);
    }

    @Override
    public void appendTooltip(ItemStack stack, World world, List<Text> tooltip, TooltipContext context) {
        super.appendTooltip(stack, world, tooltip, context);
        if(stack.hasTag())
        {
            tooltip.add(new LiteralText("Crew: "+stack.getTag().getInt("crew")));
        }
    }
}
