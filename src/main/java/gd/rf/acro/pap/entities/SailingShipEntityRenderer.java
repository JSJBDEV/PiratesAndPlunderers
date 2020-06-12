package gd.rf.acro.pap.entities;

import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.client.render.entity.model.PigEntityModel;
import net.minecraft.util.Identifier;

public class SailingShipEntityRenderer extends MobEntityRenderer<SailingShipEntity, PigEntityModel<SailingShipEntity>> {
    public SailingShipEntityRenderer(EntityRenderDispatcher entityRenderDispatcher, PigEntityModel<SailingShipEntity> entityModel, float f) {
        super(entityRenderDispatcher, new PigEntityModel<>(),1);
    }

    @Override
    public Identifier getTexture(SailingShipEntity entity) {
        return new Identifier("minecraft:textures/entity/pig/pig.png");
    }
}
