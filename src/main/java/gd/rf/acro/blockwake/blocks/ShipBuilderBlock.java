package gd.rf.acro.blockwake.blocks;

import gd.rf.acro.blockwake.Blockwake;
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

public class ShipBuilderBlock extends Block {
    public ShipBuilderBlock(Settings settings) {
        super(settings);
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        if(!world.isClient && hand==Hand.MAIN_HAND)
        {
            //should be placed 2 blocks below where you want the water level
            List<String> complete = new ArrayList<>();
            int vv = RandomUtils.nextInt(0,9999);
            for (int i = -32; i < 32; i++) {
                for (int j = 0; j < 64; j++) {
                    for (int k = -32; k < 32; k++) {
                        if(Blockwake.BOAT_MATERIAL.contains(world.getBlockState(pos.add(i,j,k)).getBlock()))
                        {
                            //complete.add(Registry.BLOCK.getId(world.getBlockState(pos.add(i,j,k)).getBlock()).toString()+" "+i+" "+j+" "+k);
                            complete.add(Block.getRawIdFromState(world.getBlockState(pos.add(i,j,k)))+" "+i+" "+(j-3)+" "+k);
                        }
                    }
                }
            }
            try {

                FileUtils.writeLines(new File("./config/Blockwake/ships/"+ vv+".blocks"),complete);
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
        if(world.getBlockState(pos.add(-32,0,-32)).getBlock()== Blocks.AIR){
            world.setBlockState(pos.add(-32,0,-32), Blockwake.SHIP_BUILDER_MARKER.getDefaultState());
        }
        if(world.getBlockState(pos.add(32,0,-32)).getBlock()== Blocks.AIR){
            world.setBlockState(pos.add(32,0,-32), Blockwake.SHIP_BUILDER_MARKER.getDefaultState());
        }
        if(world.getBlockState(pos.add(32,64,-32)).getBlock()== Blocks.AIR){
            world.setBlockState(pos.add(32,64,-32), Blockwake.SHIP_BUILDER_MARKER.getDefaultState());
        }
        if(world.getBlockState(pos.add(32,64,32)).getBlock()== Blocks.AIR){
            world.setBlockState(pos.add(32,64,32), Blockwake.SHIP_BUILDER_MARKER.getDefaultState());
        }
        if(world.getBlockState(pos.add(-32,0,32)).getBlock()== Blocks.AIR){
            world.setBlockState(pos.add(-32,0,32), Blockwake.SHIP_BUILDER_MARKER.getDefaultState());
        }
        if(world.getBlockState(pos.add(-32,64,32)).getBlock()== Blocks.AIR){
            world.setBlockState(pos.add(-32,64,32), Blockwake.SHIP_BUILDER_MARKER.getDefaultState());
        }
        if(world.getBlockState(pos.add(-32,64,-32)).getBlock()== Blocks.AIR){
            world.setBlockState(pos.add(-32,64,-32), Blockwake.SHIP_BUILDER_MARKER.getDefaultState());
        }
        if(world.getBlockState(pos.add(32,0,32)).getBlock()== Blocks.AIR){
            world.setBlockState(pos.add(32,0,32), Blockwake.SHIP_BUILDER_MARKER.getDefaultState());}

    }
}
