package gd.rf.acro.pap.entities;

import net.minecraft.block.Block;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.util.math.Vector3f;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.Items;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import static gd.rf.acro.pap.PiratesAndPlunderers.logger;

public class SailingShipBlockModel extends EntityModel<SailingShipEntity> {
    private HashMap<String,List<String>> entities;
    private String ship;
    @Override
    public void setAngles(SailingShipEntity entity, float limbAngle, float limbDistance, float animationProgress, float headYaw, float headPitch) {
        if(entity.getEquippedStack(EquipmentSlot.CHEST).getItem()== Items.OAK_PLANKS)
        {
            this.ship =entity.getEquippedStack(EquipmentSlot.CHEST).getTag().getString("model");
        }
    }
    public SailingShipBlockModel()
    {
        entities=new HashMap<>();
    }

    @Override
    public void render(MatrixStack matrices, VertexConsumer vertices, int light, int overlay, float red, float green, float blue, float alpha) {

        //on first launch, the config wont exist by the time this is initiated, to solve this problems instead it loads the file on the first in game render frame

        if(!entities.containsKey(this.ship)){
            try {
                entities.put(this.ship,FileUtils.readLines(new File("./config/PiratesAndPlunderers/ships/"+this.ship+".blocks"),"utf-8"));
                System.out.println("loaded "+this.ship+" model into the world");
            } catch (IOException e) {
                logger.error("Error reading blocks file.", e);
                entities.put(this.ship,Collections.singletonList("15 0 0 0"));
            }
        }


        VertexConsumerProvider vertexConsumerProvider = MinecraftClient.getInstance().getBufferBuilders().getEntityVertexConsumers();
        matrices.push();
        matrices.scale(-1.0F, -1.0F, 1.0F);
        matrices.multiply(Vector3f.POSITIVE_Y.getDegreesQuaternion(90.0F));
        entities.get(this.ship).forEach(line->
        {
            String[] compound = line.split(" ");
            //Block block = Registry.BLOCK.get(new Identifier(compound[0]));
            matrices.push();

            matrices.translate(Double.parseDouble(compound[1]),Double.parseDouble(compound[2])-4,Double.parseDouble(compound[3]));
            MinecraftClient.getInstance().getBlockRenderManager().renderBlockAsEntity(Block.getStateFromRawId(Integer.parseInt(compound[0])),matrices, vertexConsumerProvider,light,overlay);
            matrices.pop();
        });
        matrices.pop();
    }
}
