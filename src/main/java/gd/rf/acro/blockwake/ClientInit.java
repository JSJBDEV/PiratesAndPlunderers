package gd.rf.acro.blockwake;

import gd.rf.acro.blockwake.engagement.EngagementManager;
import gd.rf.acro.blockwake.entities.PirateEntityRenderer;
import gd.rf.acro.blockwake.entities.SailingShipEntity;
import gd.rf.acro.blockwake.entities.SailingShipMobEntityRenderer;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.rendereregistry.v1.EntityRendererRegistry;
import net.minecraft.entity.Entity;

import java.util.ArrayList;

public class ClientInit implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        EntityRendererRegistry.INSTANCE.register(Blockwake.SAILING_BOAT_ENTITY_ENTITY_TYPE, (entityRenderDispatcher, context) -> new SailingShipMobEntityRenderer(entityRenderDispatcher));
        EntityRendererRegistry.INSTANCE.register(Blockwake.PIRATE_ENTITY_ENTITY_TYPE, (entityRenderDispatcher, context) -> new PirateEntityRenderer(entityRenderDispatcher));
    }
}
