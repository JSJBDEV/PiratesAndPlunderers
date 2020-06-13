package gd.rf.acro.pap.entities;

import net.minecraft.block.Block;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.Frustum;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.client.render.entity.model.PigEntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.util.math.Vector3f;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Quaternion;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class SailingShipEntityRenderer extends EntityRenderer<SailingShipEntity> {
    private List<String> blocks;
    public SailingShipEntityRenderer(EntityRenderDispatcher entityRenderDispatcher) {
        super(entityRenderDispatcher);
        try {
            blocks = FileUtils.readLines(new File("./config/PiratesAndPlunderers/ships/"+"3209.blocks"),"utf-8");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Identifier getTexture(SailingShipEntity entity) {
        return new Identifier("pap:textures/entity/all.png");
    }

    @Override
    public void render(SailingShipEntity entity, float yaw, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light) {
        super.render(entity, yaw, tickDelta, matrices, vertexConsumers, light);
        matrices.push();
        matrices.multiply(Vector3f.POSITIVE_Y.getDegreesQuaternion(180.0F - yaw));
        blocks.forEach(line->
        {
            String[] compound = line.split(" ");
            //Block block = Registry.BLOCK.get(new Identifier(compound[0]));

            matrices.push();
            matrices.translate(Double.parseDouble(compound[1]),Double.parseDouble(compound[2]),Double.parseDouble(compound[3]));
            MinecraftClient.getInstance().getBlockRenderManager().renderBlockAsEntity(Block.getStateFromRawId(Integer.parseInt(compound[0])),matrices, vertexConsumers,light, OverlayTexture.DEFAULT_UV);
            matrices.pop();
        });
        matrices.pop();
    }
}
