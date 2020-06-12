package gd.rf.acro.pap.entities;

import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class SailingShipBlockModel extends EntityModel<SailingShipEntity> {
    private String mymodel = "";
    @Override
    public void setAngles(SailingShipEntity entity, float limbAngle, float limbDistance, float animationProgress, float headYaw, float headPitch) {

    }
    public SailingShipBlockModel(String model)
    {
        this.mymodel=model;

    }

    @Override
    public void render(MatrixStack matrices, VertexConsumer vertices, int light, int overlay, float red, float green, float blue, float alpha) {
        try {
            VertexConsumerProvider vertexConsumerProvider = MinecraftClient.getInstance().getBufferBuilders().getEntityVertexConsumers();
            List<String> blocks = FileUtils.readLines(new File(FabricLoader.getInstance().getConfigDirectory().getPath() + "/PiratesAndPluderers/ships/"+mymodel),"utf-8");
            blocks.forEach(line->
            {
                String[] compound = line.split(" ");
                Block block = Registry.BLOCK.get(new Identifier(compound[0]));
                matrices.push();
                matrices.translate(Double.parseDouble(compound[1]),Double.parseDouble(compound[2]),Double.parseDouble(compound[3]));
                MinecraftClient.getInstance().getBlockRenderManager().renderBlockAsEntity(block.getDefaultState(),matrices, vertexConsumerProvider,light,overlay);
                matrices.pop();
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
