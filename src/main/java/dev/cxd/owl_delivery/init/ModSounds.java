package dev.cxd.owl_delivery.init;

import dev.cxd.owl_delivery.OwlsDeliveries;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;

public class ModSounds {
    public static final SoundEvent OWL_SOUNDS = registerSoundEvent("entity.owl.hoot");

    private static SoundEvent registerSoundEvent(String name) {
        Identifier id = Identifier.of(OwlsDeliveries.MOD_ID, name);
        return Registry.register(Registries.SOUND_EVENT, id, SoundEvent.of(id));
    }

    public static void init() {
        OwlsDeliveries.LOGGER.info("Registering Sounds for " + OwlsDeliveries.MOD_ID);
    }
}
