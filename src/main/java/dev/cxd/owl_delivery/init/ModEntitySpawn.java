package dev.cxd.owl_delivery.init;

import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectors;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.entity.SpawnRestriction;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.registry.tag.BiomeTags;
import net.minecraft.world.Heightmap;
import net.minecraft.world.biome.BiomeKeys;

public class ModEntitySpawn {

    public static void init() {

        SpawnRestriction.register(
                ModEntities.OWL,
                SpawnRestriction.Location.ON_GROUND,
                Heightmap.Type.MOTION_BLOCKING_NO_LEAVES,
                (entityType, world, spawnReason, pos, random) ->
                        AnimalEntity.isValidNaturalSpawn(entityType, world, spawnReason, pos, random)
                                && !world.toServerWorld().isDay()
        );

        BiomeModifications.addSpawn(
                BiomeSelectors.tag(BiomeTags.IS_FOREST),
                SpawnGroup.CREATURE,
                ModEntities.OWL,
                6,
                1,
                3
        );

        BiomeModifications.addSpawn(
                BiomeSelectors.includeByKey(BiomeKeys.TAIGA),
                SpawnGroup.CREATURE,
                ModEntities.OWL,
                5,
                1,
                2
        );

        BiomeModifications.addSpawn(
                BiomeSelectors.includeByKey(BiomeKeys.SNOWY_TAIGA),
                SpawnGroup.CREATURE,
                ModEntities.OWL,
                5,
                1,
                2
        );
    }
}