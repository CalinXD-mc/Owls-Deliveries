package dev.cxd.owl_delivery;

import dev.cxd.owl_delivery.client.OwlModel;
import dev.cxd.owl_delivery.client.OwlRenderer;
import dev.cxd.owl_delivery.init.ModEntities;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;

public class OwlsDeliveriesClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        EntityModelLayerRegistry.registerModelLayer(OwlModel.OWL, OwlModel::getTexturedModelData);
        EntityRendererRegistry.register(ModEntities.OWL, OwlRenderer::new);
    }
}