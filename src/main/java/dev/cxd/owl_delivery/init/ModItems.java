package dev.cxd.owl_delivery.init;

import dev.cxd.owl_delivery.OwlsDeliveries;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroups;
import net.minecraft.item.Items;
import net.minecraft.item.SpawnEggItem;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class ModItems {
    public static final Item OWL_SPAWN_EGG = registerItem("owl_spawn_egg",
            new SpawnEggItem(ModEntities.OWL, 0xFFFFFF, 0xFFFFFF, new Item.Settings()));

    private static Item registerItem(String name, Item item) {
        return Registry.register(Registries.ITEM, Identifier.of(OwlsDeliveries.MOD_ID, name), item);
    }

    public static void init() {
        OwlsDeliveries.LOGGER.info("Registering Mod Items for " + OwlsDeliveries.MOD_ID);

        ItemGroupEvents.modifyEntriesEvent(ItemGroups.SPAWN_EGGS).register(entries -> {
            entries.addAfter(Items.OCELOT_SPAWN_EGG, OWL_SPAWN_EGG);
            entries.addAfter(OWL_SPAWN_EGG, Items.PANDA_SPAWN_EGG);
        });
    }
}
