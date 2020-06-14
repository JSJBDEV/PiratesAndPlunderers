package gd.rf.acro.pap.entities;

import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.util.Identifier;

public class SailingShipMobEntityRenderer extends MobEntityRenderer<SailingShipEntity,SailingShipBlockModel> {
    public SailingShipMobEntityRenderer(EntityRenderDispatcher entityRenderDispatcher) {
        super(entityRenderDispatcher, new SailingShipBlockModel("ship1.blocks"), 1);
    }

    @Override
    public Identifier getTexture(SailingShipEntity entity) {
        return new Identifier("pap:textures/entity/all.png");
    }
}
