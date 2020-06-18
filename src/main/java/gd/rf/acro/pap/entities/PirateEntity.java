package gd.rf.acro.pap.entities;

import net.minecraft.block.Blocks;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.mob.SkeletonEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.LiteralText;
import net.minecraft.world.World;
import org.apache.commons.lang3.RandomUtils;

public class PirateEntity extends SkeletonEntity {
    private static final String[] names = {"Achilles",
            "Adonis",
            "Adrian",
            "Aegeus",
            "Alec",
            "Alesandro",
            "Basil",
            "Barak",
            "Belen",
            "Bemus",
            "Caesar",
            "Calix",
            "Christophe",
            "Cicero",
            "Claus",
            "Cole",
            "Constantine",
            "Corban",
            "Cy",
            "Damen",
            "Darius",
            "Deacon",
            "Demitrius",
            "Dennis",
            "Deo",
            "Dru",
            "Egan",
            "Eros",
            "Estevan",
            "Eugene",
            "Evan",
            "Ezio",
            "Faustus",
            "Felipe",
            "Flavian",
            "George",
            "Giles",
            "Gregory",
            "Griffin",
            "Hercules",
            "Homer",
            "Icarus",
            "Isidore",
            "Jace",
            "Jerry",
            "Jorges",
            "Julian",
            "Kal",
            "Karan",
            "Keelan",
            "Kosmos",
            "Kristo",
            "Kyril",
            "Lander",
            "Layland",
            "Leo",
            "Magus",
            "Mateo",
            "Maximus",
            "Miles",
            "Moe",
            "Neo",
            "Nicholas",
            "Nicos",
            "Niles",
            "Nyke",
            "Obelius",
            "Odell",
            "Odysseus",
            "Orien",
            "Orrin",
            "Othello",
            "Otis",
            "Owen",
            "Pancras",
            "Pearce",
            "Philip",
            "Phoenix",
            "Proteus",
            "Quinn",
            "Rastus",
            "Sander",
            "Santos",
            "Sirius",
            "Spiro",
            "Stavros",
            "Tadd",
            "Tassos",
            "Theo",
            "Timon",
            "Titan",
            "Tomaso",
            "Tyrone",
            "Ulysses",
            "Urion",
            "Vasilios",
            "Vitalis",
            "Xander"};


    public PirateEntity(EntityType<? extends SkeletonEntity> entityType, World world) {
        super(entityType, world);
        this.equipStack(EquipmentSlot.HEAD,new ItemStack(Items.DIAMOND_HELMET));
        this.setCustomName(new LiteralText(names[RandomUtils.nextInt(0,names.length)]+" "+names[RandomUtils.nextInt(0,names.length)]));
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return SoundEvents.ENTITY_PILLAGER_AMBIENT;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource source) {
        return SoundEvents.ENTITY_PILLAGER_HURT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.ENTITY_PILLAGER_DEATH;
    }

    @Override
    public boolean isUndead() {
        return false;
    }

    @Override
    protected void initGoals() {
        this.goalSelector.add(5, new WanderAroundFarGoal(this, 1.0D));
        this.goalSelector.add(6, new LookAtEntityGoal(this, PlayerEntity.class, 8.0F));
        this.goalSelector.add(6, new LookAroundGoal(this));
        this.targetSelector.add(1, new RevengeGoal(this, new Class[0]));
        this.targetSelector.add(2, new FollowTargetGoal<>(this, PlayerEntity.class,10, true,false,
                v-> this.getScoreboardTeam()!=v.getScoreboardTeam()||this.getScoreboardTeam()==null));
        this.targetSelector.add(2, new FollowTargetGoal<>(this, PirateEntity.class,10, true,false,
                v-> this.getScoreboardTeam()!=v.getScoreboardTeam()||this.getScoreboardTeam()==null));

    }

    @Override
    public void tickMovement() {
        super.tickMovement();

        if(this.getTarget()!=null)
        {
            if(world.getBlockState(this.getBlockPos().down()).getBlock()==Blocks.AIR && this.getTarget().getY()>=this.getY()+2)
            {
                world.setBlockState(this.getBlockPos().down(), Blocks.OAK_PLANKS.getDefaultState());
                if(this.getTarget() instanceof  PlayerEntity)
                {
                    this.getTarget().playSound(SoundEvents.BLOCK_WOOD_PLACE,1,1);

                }
            }
            if(this.getTarget().getY()>this.getY())
            {
                world.setBlockState(this.getBlockPos(), Blocks.OAK_PLANKS.getDefaultState());
                if(this.getTarget() instanceof  PlayerEntity)
                {
                    this.getTarget().playSound(SoundEvents.BLOCK_WOOD_PLACE,1,1);
                }
                this.jump();
            }

        }
    }

}
