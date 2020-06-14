package gd.rf.acro.pap.blocks;

import gd.rf.acro.pap.PiratesAndPlunderers;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.LiteralText;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.RandomUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class StructureBuilderBlock extends Block {
    public StructureBuilderBlock(Settings settings) {
        super(settings);
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        if(!world.isClient)
        {
            //should be placed 2 blocks below where you want the water level
            List<String> complete = new ArrayList<>();
            int vv = RandomUtils.nextInt(0,9999);
            for (int i = -16; i < 16; i++) {
                for (int j = 0; j < 32; j++) {
                    for (int k = -16; k < 16; k++) {
                        if(world.getBlockState(pos.add(i,j,k)).getBlock()!=PiratesAndPlunderers.SHIP_BUILDER_MARKER
                                && world.getBlockState(pos.add(i,j,k)).getBlock()!=PiratesAndPlunderers.STRUCTURE_BUILDER_BLOCK)
                        {
                            complete.add(Block.getRawIdFromState(world.getBlockState(pos.add(i,j,k)))+" "+i+" "+j+" "+k);
                        }
                    }
                }
            }
            try {

                FileUtils.writeLines(new File("./config/PiratesAndPlunderers/buildings/"+ vv+".blocks"),complete);
                player.sendMessage(new LiteralText("saved as "+vv+".blocks"),true);
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        return super.onUse(state, world, pos, player, hand, hit);
    }


    @Override
    public void onSteppedOn(World world, BlockPos pos, Entity entity) {
        super.onSteppedOn(world, pos, entity);
        if(world.getBlockState(pos.add(-16,0,-16)).getBlock()== Blocks.AIR){
            world.setBlockState(pos.add(-16,0,-16),PiratesAndPlunderers.SHIP_BUILDER_MARKER.getDefaultState());
        }
        if(world.getBlockState(pos.add(16,0,-16)).getBlock()== Blocks.AIR){
            world.setBlockState(pos.add(16,0,-16),PiratesAndPlunderers.SHIP_BUILDER_MARKER.getDefaultState());
        }
        if(world.getBlockState(pos.add(16,32,-16)).getBlock()== Blocks.AIR){
            world.setBlockState(pos.add(16,32,-16),PiratesAndPlunderers.SHIP_BUILDER_MARKER.getDefaultState());
        }
        if(world.getBlockState(pos.add(16,32,16)).getBlock()== Blocks.AIR){
            world.setBlockState(pos.add(16,32,16),PiratesAndPlunderers.SHIP_BUILDER_MARKER.getDefaultState());
        }
        if(world.getBlockState(pos.add(-16,0,16)).getBlock()== Blocks.AIR){
            world.setBlockState(pos.add(-16,0,16),PiratesAndPlunderers.SHIP_BUILDER_MARKER.getDefaultState());
        }
        if(world.getBlockState(pos.add(-16,32,16)).getBlock()== Blocks.AIR){
            world.setBlockState(pos.add(-16,32,16),PiratesAndPlunderers.SHIP_BUILDER_MARKER.getDefaultState());
        }
        if(world.getBlockState(pos.add(-16,32,-16)).getBlock()== Blocks.AIR){
            world.setBlockState(pos.add(-16,32,-16),PiratesAndPlunderers.SHIP_BUILDER_MARKER.getDefaultState());
        }
        if(world.getBlockState(pos.add(16,0,16)).getBlock()== Blocks.AIR){
            world.setBlockState(pos.add(16,0,16),PiratesAndPlunderers.SHIP_BUILDER_MARKER.getDefaultState());}

    }
}
