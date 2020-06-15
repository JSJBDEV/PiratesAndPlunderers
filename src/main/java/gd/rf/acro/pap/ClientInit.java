package gd.rf.acro.pap;

import gd.rf.acro.pap.entities.PirateEntityRenderer;
import gd.rf.acro.pap.entities.SailingShipEntityRenderer;
import gd.rf.acro.pap.entities.SailingShipMobEntityRenderer;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendereregistry.v1.EntityRendererRegistry;

public class ClientInit implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        EntityRendererRegistry.INSTANCE.register(PiratesAndPlunderers.SAILING_BOAT_ENTITY_ENTITY_TYPE, (entityRenderDispatcher, context) -> new SailingShipMobEntityRenderer(entityRenderDispatcher));
        EntityRendererRegistry.INSTANCE.register(PiratesAndPlunderers.PIRATE_ENTITY_ENTITY_TYPE, (entityRenderDispatcher, context) -> new PirateEntityRenderer(entityRenderDispatcher));

    }
}
