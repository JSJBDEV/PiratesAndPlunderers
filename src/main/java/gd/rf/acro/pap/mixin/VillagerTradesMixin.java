package gd.rf.acro.pap.mixin;

import gd.rf.acro.pap.PiratesAndPlunderers;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.passive.VillagerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundTag;
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


            ItemStack LCC = new ItemStack(PiratesAndPlunderers.COMMISSION_ITEM);
            CompoundTag tag = new CompoundTag();
            tag.putString("storedModel","lord_crawlmasks_clipper");
            LCC.setTag(tag);

            ItemStack PA = new ItemStack(PiratesAndPlunderers.COMMISSION_ITEM);
            CompoundTag tag2 = new CompoundTag();
            tag2.putString("storedModel","piratv_arcadia");
            PA.setTag(tag2);


            list.add(new TradeOffer(new ItemStack(Items.EMERALD,64),LCC,2,20,1));
            list.add(new TradeOffer(new ItemStack(Items.EMERALD,32),PA,2,20,1));
            villager.setOffers(list);
        }
    }
}
