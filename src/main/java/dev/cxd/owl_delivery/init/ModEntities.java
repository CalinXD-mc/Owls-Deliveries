package dev.cxd.owl_delivery.init;

import dev.cxd.owl_delivery.OwlsDeliveries;
import dev.cxd.owl_delivery.entity.OwlEntity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;

public class ModEntities {

    private static final RegistryKey<EntityType<?>> OWL_KEY =
            RegistryKey.of(RegistryKeys.ENTITY_TYPE, Identifier.of(OwlsDeliveries.MOD_ID, "owl"));

    public static final EntityType<OwlEntity> OWL = Registry.register(Registries.ENTITY_TYPE,
            Identifier.of(OwlsDeliveries.MOD_ID, "owl"),
            EntityType.Builder.create(OwlEntity::new, SpawnGroup.CREATURE)
                    .setDimensions(0.6f, 0.6f).build(String.valueOf(OWL_KEY)));


    public static void init() {
        OwlsDeliveries.LOGGER.info("Registering Mobs for " + OwlsDeliveries.MOD_ID);
    }
}
