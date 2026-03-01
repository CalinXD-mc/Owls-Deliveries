package dev.cxd.owl_delivery;

import dev.cxd.owl_delivery.entity.OwlEntity;
import dev.cxd.owl_delivery.init.ModEntities;
import dev.cxd.owl_delivery.init.ModEntitySpawn;
import dev.cxd.owl_delivery.init.ModItems;
import dev.cxd.owl_delivery.init.ModSounds;
import net.fabricmc.api.ModInitializer;

import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class OwlsDeliveries implements ModInitializer {
	public static final String MOD_ID = "owl_delivery";


	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		ModEntities.init();
		ModEntitySpawn.init();
		ModItems.init();
		ModSounds.init();

		FabricDefaultAttributeRegistry.register(ModEntities.OWL, OwlEntity.createAttributes());

		LOGGER.info("Hello Fabric world!");
	}
}