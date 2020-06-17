package gd.rf.acro.pap.entities;

import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.util.math.Vector3f;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Quaternion;
import net.minecraft.util.registry.Registry;
import org.apache.commons.io.FileUtils;
import static gd.rf.acro.pap.PiratesAndPlunderers.logger;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class SailingShipBlockModel extends EntityModel<SailingShipEntity> {
    private List<String> blocks;
    private String ship;
    @Override
    public void setAngles(SailingShipEntity entity, float limbAngle, float limbDistance, float animationProgress, float headYaw, float headPitch) {

    }
    public SailingShipBlockModel(String model)
    {
        this.ship=model;

    }

    @Override
    public void render(MatrixStack matrices, VertexConsumer vertices, int light, int overlay, float red, float green, float blue, float alpha) {

        //on first launch, the config wont exist by the time this is initiated, to solve this problems instead it loads the file on the first in game render frame
        if(blocks==null)
        {
            try {
                blocks = FileUtils.readLines(new File("./config/PiratesAndPlunderers/ships/"+this.ship),"utf-8");
            } catch (IOException e) {
                logger.error("Error reading blocks file.", e);
            }
        }

        VertexConsumerProvider vertexConsumerProvider = MinecraftClient.getInstance().getBufferBuilders().getEntityVertexConsumers();
        matrices.push();
        matrices.scale(-1.0F, -1.0F, 1.0F);
        matrices.multiply(Vector3f.POSITIVE_Y.getDegreesQuaternion(90.0F));
        blocks.forEach(line->
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
