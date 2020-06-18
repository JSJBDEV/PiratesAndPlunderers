package gd.rf.acro.pap.items;

import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ServerWorldAccess;
import net.minecraft.world.World;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class BlocksLoaderItem extends Item {

    public BlocksLoaderItem(Settings settings) {
        super(settings);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        if(user.isSneaking())
        {
            loadStructure(world,user.getBlockPos(),user.getStackInHand(hand).getName().asString(),"buildings");
        }
        else
        {
            loadStructure(world,user.getBlockPos(),user.getStackInHand(hand).getName().asString(),"ships");
        }
        return super.use(world, user, hand);
    }

    private void loadStructure(World world, BlockPos origin, String name, String folder)
    {

        try {
            List<String> entries = FileUtils.readLines(new File("./config/PiratesAndPlunderers/"+folder+"/"+name+".blocks"),"utf-8");
            entries.forEach(entry->
            {
                String[] compound = entry.split(" ");
                //the -3 means it will spawn at water level

                BlockPos loc = origin.add(Integer.parseInt(compound[1]),Integer.parseInt(compound[2])-3,Integer.parseInt(compound[3]));
                world.setBlockState(loc, Block.getStateFromRawId(Integer.parseInt(compound[0])));
            });
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("no .blocks file found: "+folder+"/"+name);
        }
    }
}
