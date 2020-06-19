package gd.rf.acro.blockwake.entities;

import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.util.math.Vector3f;
import net.minecraft.util.math.Quaternion;

public class SailingShipModel extends EntityModel<SailingShipEntity> {
    private final ModelPart bone;

    public SailingShipModel() {
        textureWidth = 100;
        textureHeight = 100;

        bone = new ModelPart(this);
        bone.setPivot(0.0F, 24.0F, 0.0F);
        bone.addCuboid("bone", 32.0F, -18.0F, -13.0F, 9, 6, 26, 0.0F, 0, 0);
        bone.addCuboid("bone", 32.0F, -12.0F, -8.0F, 5, 4, 16, 0.0F, 0, 0);
        bone.addCuboid("bone", 32.0F, -8.0F, -4.0F, 2, 4, 8, 0.0F, 0, 0);
        bone.addCuboid("bone", -2.0F, -50.0F, -1.0F, 2, 32, 2, 0.0F, 0, 66);
        bone.addCuboid("bone", 24.0F, -50.0F, -1.0F, 2, 32, 2, 0.0F, 0, 66);
        bone.addCuboid("bone", -4.0F, -49.0F, -9.0F, 2, 20, 20, 0.0F, 56, 60);
        bone.addCuboid("bone", 22.0F, -49.0F, -9.0F, 2, 20, 20, 0.0F, 56, 60);
        bone.addCuboid("bone", -32.0F, -22.0F, -11.0F, 2, 4, 22, 0.0F, 0, 0);
        bone.addCuboid("bone", 39.0F, -22.0F, -11.0F, 2, 4, 22, 0.0F, 0, 0);
        bone.addCuboid("bone", -32.0F, -22.0F, 11.0F, 73, 4, 2, 0.0F, 14, 0);
        bone.addCuboid("bone", -32.0F, -22.0F, -13.0F, 73, 4, 2, 0.0F, 0, 0);
        bone.addCuboid("bone", -32.0F, -18.0F, -13.0F, 64, 4, 26, 0.0F, 0, 25);
        bone.addCuboid("bone", -32.0F, -14.0F, -13.0F, 64, 2, 26, 0.0F, 0, 0);
        bone.addCuboid("bone", -32.0F, -12.0F, -11.0F, 64, 2, 22, 0.0F, 0, 0);
        bone.addCuboid("bone", -32.0F, -10.0F, -9.0F, 64, 2, 18, 0.0F, 0, 0);
        bone.addCuboid("bone", -32.0F, -8.0F, -7.0F, 64, 2, 14, 0.0F, 0, 0);
        bone.addCuboid("bone", -32.0F, -6.0F, -5.0F, 64, 2, 10, 0.0F, 0, 0);
        bone.addCuboid("bone", -32.0F, -4.0F, -3.0F, 64, 2, 6, 0.0F, 0, 0);
        bone.addCuboid("bone", -32.0F, -2.0F, -1.0F, 64, 2, 2, 0.0F, 0, 0);
    }

    @Override
    public void setAngles(SailingShipEntity entity, float limbAngle, float limbDistance, float animationProgress, float headYaw, float headPitch) {

    }





    @Override
    public void render(MatrixStack matrices, VertexConsumer vertices, int light, int overlay, float red, float green, float blue, float alpha) {
        matrices.push();
        matrices.scale(3,3,3);
        matrices.translate(0,-0.5,1);
        matrices.multiply(Vector3f.NEGATIVE_Y.getDegreesQuaternion(-90));
        this.bone.render(matrices, vertices, light, overlay);
        matrices.pop();
    }
}