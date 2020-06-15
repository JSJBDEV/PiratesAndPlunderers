package gd.rf.acro.pap.mixin;

import gd.rf.acro.pap.PiratesAndPlunderers;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.passive.VillagerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.village.TradeOffer;
import net.minecraft.village.TraderOfferList;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(VillagerEntity.class)
public class VillagerTradesMixin {
    @Inject(method = "fillRecipes", at = @At("RETURN"))
    protected void fillRecipes(CallbackInfo ci)
    {
        System.out.println("doing the thing");
        VillagerEntity villager = ((VillagerEntity)(Object)this);
        if(villager.getVillagerData().getProfession()== PiratesAndPlunderers.SHIPWRIGHT)
        {
            TraderOfferList list = new TraderOfferList();
            list.add(new TradeOffer(new ItemStack(Items.EMERALD),new ItemStack(Items.STONE),99,1,1));
            list.add(new TradeOffer(new ItemStack(Items.EMERALD),new ItemStack(Items.STONE),99,1,1));
            if(villager.getVillagerData().getLevel()>=2)
            {
                list.add(new TradeOffer(new ItemStack(Items.EMERALD),new ItemStack(Items.STONE),99,1,1));
                list.add(new TradeOffer(new ItemStack(Items.EMERALD),new ItemStack(Items.STONE),99,1,1));
            }
            if(villager.getVillagerData().getLevel()>=3)
            {
                list.add(new TradeOffer(new ItemStack(Items.EMERALD),new ItemStack(Items.STONE),99,1,1));
                list.add(new TradeOffer(new ItemStack(Items.EMERALD),new ItemStack(Items.STONE),99,1,1));
            }
            if(villager.getVillagerData().getLevel()>=4)
            {
                list.add(new TradeOffer(new ItemStack(Items.EMERALD),new ItemStack(Items.STONE),99,1,1));
                list.add(new TradeOffer(new ItemStack(Items.EMERALD),new ItemStack(Items.STONE),99,1,1));
            }
            if(villager.getVillagerData().getLevel()==5)
            {
                list.add(new TradeOffer(new ItemStack(Items.EMERALD),new ItemStack(Items.STONE),99,1,1));
                list.add(new TradeOffer(new ItemStack(Items.EMERALD),new ItemStack(Items.STONE),99,1,1));
            }
            villager.setOffers(list);
        }
    }
}
