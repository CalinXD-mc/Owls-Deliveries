package dev.cxd.owl_delivery.entity;

import java.util.Arrays;
import java.util.Comparator;

public enum OwlVariant {
    WILD(0),
    WILD_GREEN_EYED(1),

    GRAY_OWL(2),
    GRAY_OWL_GREEN_EYED(3),

    BLACK_OWL(4),
    BLACK_OWL_GREEN_EYED(5),

    SNOW_OWL(6),
    SNOW_OWL_BLUE_EYED(7),
    SNOW_OWL_GREEN_EYED(8),

    SHINY_OWL(9),

    FOLLY_OWL(10),
    LUX_OWL(11),
    VAMPIRE_OWL(12);

    private static final OwlVariant[] BY_ID = Arrays.stream(values())
            .sorted(Comparator.comparingInt(OwlVariant::getId))
            .toArray(OwlVariant[]::new);

    private final int id;

    OwlVariant(int id) {
        this.id = id;
    }

    public int getId() {
        return this.id;
    }

    public static OwlVariant byId(int id) {
        return BY_ID[id % BY_ID.length];
    }
}