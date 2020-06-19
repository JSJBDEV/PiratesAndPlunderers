package gd.rf.acro.blockwake.entities;

import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.client.render.entity.model.PlayerEntityModel;
import net.minecraft.util.Identifier;

public class PirateEntityRenderer extends LivingEntityRenderer<PirateEntity, PlayerEntityModel<PirateEntity>>
{


    public PirateEntityRenderer(EntityRenderDispatcher dispatcher) {
        super(dispatcher,new PlayerEntityModel<>(1,true), 1);
    }

    @Override
    public Identifier getTexture(PirateEntity entity) {
        return new Identifier("blockwake","textures/entity/test.png");
    }
}
