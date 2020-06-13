package gd.rf.acro.pap.world;

import net.minecraft.server.world.ServerWorld;
import net.minecraft.structure.Structure;
import net.minecraft.structure.StructureManager;
import net.minecraft.structure.StructurePlacementData;
import net.minecraft.util.BlockMirror;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;

import java.util.Random;

public class PortTownHandler {
    private static void spawnStruct(ServerWorld world, BlockPos pos, String name)
    {
        StructureManager manager = world.getStructureManager();


        Identifier load = new Identifier("pap",name);
        Structure structure = manager.getStructure(load);
        if(structure!=null)
        {
            StructurePlacementData data = new StructurePlacementData()
                    .setMirror(BlockMirror.NONE)
                    .setIgnoreEntities(true)
                    .setRotation(BlockRotation.NONE);
            structure.place(world,pos,data,new Random());
        }
        else
        {
            System.out.println("this structure is null!");
        }



    }
}
