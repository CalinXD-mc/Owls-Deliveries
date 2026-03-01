package dev.cxd.owl_delivery.client;

import com.google.common.collect.Maps;
import dev.cxd.owl_delivery.OwlsDeliveries;
import dev.cxd.owl_delivery.entity.OwlEntity;
import dev.cxd.owl_delivery.entity.OwlVariant;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.Util;

import java.util.Map;

public class OwlRenderer extends MobEntityRenderer<OwlEntity, OwlModel<OwlEntity>> {
    private static final Map<OwlVariant, Identifier> LOCATION_BY_VARIANT =
            Util.make(Maps.newEnumMap(OwlVariant.class), map -> {
                // Wild
                map.put(OwlVariant.WILD,
                        new Identifier(OwlsDeliveries.MOD_ID, "textures/entity/owl/owl.png"));
                map.put(OwlVariant.WILD_GREEN_EYED,
                        new Identifier(OwlsDeliveries.MOD_ID, "textures/entity/owl/owl_green_eyed.png"));
                // Gray
                map.put(OwlVariant.GRAY_OWL,
                        new Identifier(OwlsDeliveries.MOD_ID, "textures/entity/owl/gray_owl.png"));
                map.put(OwlVariant.GRAY_OWL_GREEN_EYED,
                        new Identifier(OwlsDeliveries.MOD_ID, "textures/entity/owl/gray_owl_green_eyed.png"));
                // Black
                map.put(OwlVariant.BLACK_OWL,
                        new Identifier(OwlsDeliveries.MOD_ID, "textures/entity/owl/black_owl.png"));
                map.put(OwlVariant.BLACK_OWL_GREEN_EYED,
                        new Identifier(OwlsDeliveries.MOD_ID, "textures/entity/owl/black_owl_green_eyed.png"));
                // Snow
                map.put(OwlVariant.SNOW_OWL,
                        new Identifier(OwlsDeliveries.MOD_ID, "textures/entity/owl/snow_owl.png"));
                map.put(OwlVariant.SNOW_OWL_BLUE_EYED,
                        new Identifier(OwlsDeliveries.MOD_ID, "textures/entity/owl/snow_owl_blue_eyed.png"));
                map.put(OwlVariant.SNOW_OWL_GREEN_EYED,
                        new Identifier(OwlsDeliveries.MOD_ID, "textures/entity/owl/snow_owl_green_eyed.png"));
                // Special
                map.put(OwlVariant.SHINY_OWL,
                        new Identifier(OwlsDeliveries.MOD_ID, "textures/entity/owl/shiny_owl.png"));
                // Name tag
                map.put(OwlVariant.FOLLY_OWL,
                        new Identifier(OwlsDeliveries.MOD_ID, "textures/entity/owl/folly_owl.png"));
                map.put(OwlVariant.LUX_OWL,
                        new Identifier(OwlsDeliveries.MOD_ID, "textures/entity/owl/lux_owl.png"));
                map.put(OwlVariant.VAMPIRE_OWL,
                        new Identifier(OwlsDeliveries.MOD_ID, "textures/entity/owl/vampire_owl.png"));
            });

    public OwlRenderer(EntityRendererFactory.Context context) {
        super(context, new OwlModel<>(context.getPart(OwlModel.OWL)), 0.35f);
    }

    @Override
    public Identifier getTexture(OwlEntity entity) {
        return LOCATION_BY_VARIANT.get(entity.getVariant());
    }

    @Override
    public void render(OwlEntity entity, float yaw, float tickDelta, MatrixStack matrices,
                       VertexConsumerProvider vertexConsumers, int light) {
        if (entity.isBaby()) {
            matrices.scale(0.5f, 0.5f, 0.5f);
        }
        super.render(entity, yaw, tickDelta, matrices, vertexConsumers, light);
    }
}