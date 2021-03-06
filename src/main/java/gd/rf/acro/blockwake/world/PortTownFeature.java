package gd.rf.acro.blockwake.world;

import com.mojang.serialization.Codec;
import gd.rf.acro.blockwake.Blockwake;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.BarrelBlockEntity;
import net.minecraft.loot.LootTable;
import net.minecraft.loot.LootTables;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ServerWorldAccess;
import net.minecraft.world.gen.StructureAccessor;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.gen.feature.DefaultFeatureConfig;
import net.minecraft.world.gen.feature.Feature;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.RandomUtils;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Random;

public class PortTownFeature extends Feature<DefaultFeatureConfig> {
    private final String[] buildings = {"port_town_tannery","port_town_archery","port_town_farm","port_town_library","port_town_shipwright","port_town_storage"};
    public PortTownFeature(Codec<DefaultFeatureConfig> codec) {
        super(codec);
    }

    @Override
    public boolean generate(ServerWorldAccess serverWorldAccess, StructureAccessor accessor, ChunkGenerator generator, Random random, BlockPos pos, DefaultFeatureConfig config) {
        int state = RandomUtils.nextInt(1,4);
        //1 & 2 happy villager town
        //3 ruins
        //4 pirates, probably
        boolean isBroken = false;
        if(state==3){isBroken=true;}

        BlockPos seaOrigin = new BlockPos(pos.getX(),63,pos.getZ());
        loadStructure(serverWorldAccess,seaOrigin,"port_town_bottom_floor",isBroken);
        serverWorldAccess.setBlockState(seaOrigin.add(5,0,5),Blocks.BELL.getDefaultState(),3);

        int floors = RandomUtils.nextInt(2,10);
        for (int i = 0; i < floors; i++) {
            loadStructure(serverWorldAccess,seaOrigin.up(5+i*4),buildings[RandomUtils.nextInt(0,buildings.length)],isBroken);

            if(state<3)
            {
                serverWorldAccess.setBlockState(seaOrigin.up(5+i*4), Blockwake.STATIC_MOB_SPAWNER.getDefaultState(),3);
                serverWorldAccess.getBlockTickScheduler().schedule(seaOrigin.up(5+i*4), Blockwake.STATIC_MOB_SPAWNER,1);
            }
            if(state==4)
            {
                serverWorldAccess.setBlockState(seaOrigin.up(5+i*4), Blockwake.STATIC_PIRATE_SPAWNER.getDefaultState(),3);
                serverWorldAccess.getBlockTickScheduler().schedule(seaOrigin.up(5+i*4), Blockwake.STATIC_PIRATE_SPAWNER,1);
            }
        }

        System.out.println(pos.toString());

        return true;
    }

    private void loadStructure(ServerWorldAccess world, BlockPos origin,String name)
    {
        for (int i = -16; i < 16; i++) {
            for (int j = 0; j < 8; j++) {
                for (int k = -16; k < 16; k++) {
                    world.setBlockState(origin.add(i,j,k),Blocks.AIR.getDefaultState(),3);
                }
            }
        }
        try {
            List<String> entries = FileUtils.readLines(new File("./config/Blockwake/buildings/"+name+".blocks"),"utf-8");
            entries.forEach(entry->
            {
                String[] compound = entry.split(" ");
                //the -3 means it will spawn at water level

                BlockPos loc = origin.add(Integer.parseInt(compound[1]),Integer.parseInt(compound[2])-3,Integer.parseInt(compound[3]));
                world.setBlockState(loc, Block.getStateFromRawId(Integer.parseInt(compound[0])),3);
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void loadStructure(ServerWorldAccess world, BlockPos origin,String name,boolean isBroken)
    {
        for (int i = -16; i < 16; i++) {
            for (int j = 0; j < 8; j++) {
                for (int k = -16; k < 16; k++) {
                    world.setBlockState(origin.add(i,j,k),Blocks.AIR.getDefaultState(),3);
                }
            }
        }
        try {
            List<String> entries = FileUtils.readLines(new File("./config/Blockwake/buildings/"+name+".blocks"),"utf-8");
            entries.forEach(entry->
            {
                String[] compound = entry.split(" ");
                //the -3 means it will spawn at water level
                BlockPos loc = origin.add(Integer.parseInt(compound[1]),Integer.parseInt(compound[2])-3,Integer.parseInt(compound[3]));
                if(!isBroken ||  RandomUtils.nextInt(0,8)>1)
                {

                    world.setBlockState(loc, Block.getStateFromRawId(Integer.parseInt(compound[0])),3);
                    if(Block.getStateFromRawId(Integer.parseInt(compound[0])).getBlock()==Blocks.BARREL && RandomUtils.nextInt(0,3)==0)
                    {
                        BarrelBlockEntity entity = (BarrelBlockEntity) world.getBlockEntity(loc);
                        Identifier[] vc =  LootTables.getAll().toArray(new Identifier[]{});
                        Identifier table = vc[RandomUtils.nextInt(0,vc.length)];
                        while(table.getPath().equals("empty"))
                        {
                            table = vc[RandomUtils.nextInt(0,vc.length)];
                        }
                        entity.setLootTable(table,RandomUtils.nextLong());
                    }
                }
                else
                {
                    if(RandomUtils.nextBoolean())
                    {
                        world.setBlockState(loc, Blocks.COBWEB.getDefaultState(),3);
                    }
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
