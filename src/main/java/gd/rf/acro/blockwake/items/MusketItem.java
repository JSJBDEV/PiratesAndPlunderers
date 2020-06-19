package gd.rf.acro.blockwake.items;

import gd.rf.acro.blockwake.Blockwake;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.TargetPredicate;
import net.minecraft.entity.damage.EntityDamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.Box;
import net.minecraft.world.World;
import org.apache.commons.lang3.RandomUtils;

import java.util.List;
import java.util.Random;

public class MusketItem extends Item {
    int minDamage;
    int maxDamage;
    public MusketItem(Settings settings,int min, int max) {
        super(settings);
        this.minDamage=min;
        this.maxDamage=max;
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {

        if(user.inventory.count(Blockwake.SHOT_ITEM)>0)
        {
            //mostly modified from GuardianEntity
            user.playSound(SoundEvents.ENTITY_FIREWORK_ROCKET_BLAST,1,1);
            HitResult result = user.rayTrace(100,1,true);
            Random random = new Random();
            LivingEntity livingEntity = world.getClosestEntity(LivingEntity.class, TargetPredicate.DEFAULT,user,result.getPos().getX(),result.getPos().getY(),result.getPos().getZ(),new Box(result.getPos().getX()-2,result.getPos().getY()-2,result.getPos().getZ()-2,result.getPos().getX()+2,result.getPos().getY()+2,result.getPos().getZ()+2));
            double d = 1D;
            double e = result.getPos().getX() - user.getBlockPos().getX();
            double f = result.getPos().y+0.5 - user.getEyeY();
            double g = result.getPos().getZ() - user.getZ();
            double h = Math.sqrt(e * e + f * f + g * g);
            e /= h;
            f /= h;
            g /= h;
            double j = random.nextDouble();
            user.getStackInHand(hand).damage(1,user,(dobreak)-> dobreak.sendToolBreakStatus(hand));
            if(livingEntity!=null)
            {
                livingEntity.damage(new EntityDamageSource("blockwake_shot",user), RandomUtils.nextFloat(this.minDamage,this.maxDamage));
            }
            while(j < h) {
                j += 1.8D - d + random.nextDouble() * (1.7D - d);
                world.addParticle(ParticleTypes.SMOKE, user.getBlockPos().getX() + e * j, user.getEyeY() + f * j, user.getBlockPos().getZ() + g * j, 0.0D, 0.0D, 0.0D);
             }
            for (ItemStack item : user.inventory.main) {
                if (item.getItem() == Blockwake.SHOT_ITEM) {
                    item.decrement(1);
                    break;
                }
            }

        }
        return super.use(world, user, hand);
    }

    @Override
    public void appendTooltip(ItemStack stack, World world, List<Text> tooltip, TooltipContext context) {
        super.appendTooltip(stack, world, tooltip, context);
        tooltip.add(new LiteralText(this.minDamage+"-"+maxDamage+" damage"));
    }
}
