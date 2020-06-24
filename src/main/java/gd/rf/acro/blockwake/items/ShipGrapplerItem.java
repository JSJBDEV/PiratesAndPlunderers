package gd.rf.acro.blockwake.items;

import gd.rf.acro.blockwake.Blockwake;
import gd.rf.acro.blockwake.engagement.EngagementManager;
import gd.rf.acro.blockwake.entities.SailingShipEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.TargetPredicate;
import net.minecraft.entity.damage.EntityDamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.particle.DustParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.Box;
import net.minecraft.world.World;
import org.apache.commons.lang3.RandomUtils;

import java.util.Random;

public class ShipGrapplerItem extends Item {
    public ShipGrapplerItem(Settings settings) {
        super(settings);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        if(hand== Hand.MAIN_HAND)
        {
            user.playSound(SoundEvents.ENTITY_ENDER_PEARL_THROW,1,1);
            HitResult result = user.rayTrace(100,1,true);
            Random random = new Random();
            LivingEntity livingEntity = world.getClosestEntity(LivingEntity.class, TargetPredicate.DEFAULT,user,result.getPos().getX(),result.getPos().getY(),result.getPos().getZ(),new Box(result.getPos().getX()-10,result.getPos().getY()-10,result.getPos().getZ()-10,result.getPos().getX()+10,result.getPos().getY()+10,result.getPos().getZ()+10));
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

            while(j < h) {
                j += 1.8D - d + random.nextDouble() * (1.7D - d);
                world.addParticle(new DustParticleEffect(1/223,1/195,1/86,3), user.getBlockPos().getX() + e * j, user.getEyeY() + f * j, user.getBlockPos().getZ() + g * j, 0.0D, 0.0D, 0.0D);
            }
            if(livingEntity!=null && livingEntity.getType()== Blockwake.SAILING_BOAT_ENTITY_ENTITY_TYPE && !world.isClient)
            {
                if(user.hasVehicle() && user.getVehicle().getType()== Blockwake.SAILING_BOAT_ENTITY_ENTITY_TYPE)
                {
                    EngagementManager.INSTANCE.startEngagementWithShipEntities((SailingShipEntity) user.getVehicle(),(SailingShipEntity)livingEntity);
                }
            }
        }
        return super.use(world, user, hand);
    }
}
