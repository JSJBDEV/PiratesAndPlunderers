package gd.rf.acro.pap.entities;

import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.client.render.entity.model.PigEntityModel;
import net.minecraft.util.Identifier;

public class SailingShipEntityRenderer extends MobEntityRenderer<SailingShipEntity, SailingShipBlockModel> {
    public SailingShipEntityRenderer(EntityRenderDispatcher entityRenderDispatcher) {
        super(entityRenderDispatcher, new SailingShipBlockModel("test.blocks"),1);
    }

    @Override
    public Identifier getTexture(SailingShipEntity entity) {
        return new Identifier("pap:textures/entity/all.png");
    }
}
