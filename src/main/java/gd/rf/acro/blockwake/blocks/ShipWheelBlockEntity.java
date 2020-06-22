package gd.rf.acro.blockwake.blocks;

import gd.rf.acro.blockwake.Blockwake;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.nbt.CompoundTag;

public class ShipWheelBlockEntity extends BlockEntity {
    long nextTime = 0;

    public ShipWheelBlockEntity() {
        super(Blockwake.SHIPWHEEL_BLOCK_ENTITY);
    }

    @Override
    public void fromTag(BlockState state, CompoundTag tag) {
        super.fromTag(state, tag);
        nextTime=tag.getLong("nextTime");
    }

    @Override
    public CompoundTag toTag(CompoundTag tag) {
        super.toTag(tag);
        tag.putLong("nextTime",nextTime);
        return tag;
    }

    public long getNextTime() {
        return nextTime;
    }

    public void interact()
    {
        this.nextTime=this.world.getTime()+1200;
        markDirty();
    }
}
