package dev.cxd.owl_delivery.client;

import dev.cxd.owl_delivery.OwlsDeliveries;
import dev.cxd.owl_delivery.entity.OwlEntity;
import net.minecraft.client.model.*;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.entity.model.EntityModelLayer;
import net.minecraft.client.render.entity.model.SinglePartEntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;

public class OwlModel<T extends OwlEntity> extends SinglePartEntityModel<T> {
    public static final EntityModelLayer OWL = new EntityModelLayer(
            new Identifier(OwlsDeliveries.MOD_ID, "owl"), "main");

    private final ModelPart root;
    private final ModelPart head;
    private final ModelPart torso;
    private final ModelPart lFoot;
    private final ModelPart rFoot;
    private final ModelPart lWing;
    private final ModelPart rWing;

    public OwlModel(ModelPart root) {
        this.root     = root.getChild("root");
        this.head     = this.root.getChild("head");
        this.torso    = this.root.getChild("torso");
        this.lFoot    = this.root.getChild("l_foot");
        this.rFoot    = this.root.getChild("r_foot");
        this.lWing    = this.root.getChild("l_wing");
        this.rWing    = this.root.getChild("r_wing");
    }

    public static TexturedModelData getTexturedModelData() {
        ModelData modelData = new ModelData();
        ModelPartData modelPartData = modelData.getRoot();

        ModelPartData root = modelPartData.addChild("root",
                ModelPartBuilder.create(),
                ModelTransform.pivot(0.0F, 0.0F, 0.0F));

        ModelPartData head = root.addChild("head",
                ModelPartBuilder.create()
                        .uv(0, 0).cuboid(-5.0F, -7.0F, -4.0F, 10.0F, 7.0F, 8.0F, new Dilation(0.0F))
                        .uv(36, 4).cuboid(-1.0F, -3.0F, -5.0F, 2.0F, 3.0F, 1.0F, new Dilation(0.0F)),
                ModelTransform.pivot(0.0F, 17.9F, 0.0F));

        head.addChild("eye_brows",
                ModelPartBuilder.create()
                        .uv(0, 42).cuboid(-7.0F, -9.0F, 0.0F, 14.0F, 9.0F, 0.0F, new Dilation(0.0F)),
                ModelTransform.pivot(0.0F, -0.25F, -4.1F));

        root.addChild("torso",
                ModelPartBuilder.create()
                        .uv(0, 15).cuboid(-4.0F, -3.0F, -4.0F, 8.0F, 6.0F, 8.0F, new Dilation(0.0F)),
                ModelTransform.pivot(0.0F, 20.9F, 0.0F));

        root.addChild("l_foot",
                ModelPartBuilder.create()
                        .uv(36, 0).cuboid(-1.0F, 0.0F, -2.0F, 2.0F, 0.0F, 2.0F, new Dilation(0.0F)),
                ModelTransform.pivot(2.0F, 24.0F, 1.0F));

        root.addChild("r_foot",
                ModelPartBuilder.create()
                        .uv(36, 2).cuboid(-1.0F, 0.0F, -2.0F, 2.0F, 0.0F, 2.0F, new Dilation(0.0F)),
                ModelTransform.pivot(-2.0F, 24.0F, 1.0F));

        root.addChild("l_wing",
                ModelPartBuilder.create()
                        .uv(24, 29).cuboid(0.0F, 0.0F, -3.5F, 1.0F, 6.0F, 7.0F, new Dilation(0.0F)),
                ModelTransform.pivot(4.0F, 17.9F, 0.0F));

        root.addChild("r_wing",
                ModelPartBuilder.create()
                        .uv(32, 15).cuboid(-1.0F, 0.0F, -3.5F, 1.0F, 6.0F, 7.0F, new Dilation(0.0F)),
                ModelTransform.pivot(-4.0F, 17.9F, 0.0F));

        return TexturedModelData.of(modelData, 64, 64);
    }

    @Override
    public ModelPart getPart() {
        return root;
    }

    @Override
    public void setAngles(T entity, float limbSwing, float limbSwingAmount,
                          float ageInTicks, float netHeadYaw, float headPitch) {
        this.getPart().traverse().forEach(ModelPart::resetTransform);

        this.head.yaw   = netHeadYaw  * MathHelper.RADIANS_PER_DEGREE;
        this.head.pitch = MathHelper.clamp(headPitch, -15.0F, 5.0F) * MathHelper.RADIANS_PER_DEGREE;

        if (entity.isSitting()) {
            this.updateAnimation(entity.sittingAnimationState, OwlAnimations.SIT_IDLE, ageInTicks, 1.0F);
        } else if (entity.isGoingToReceiver()) {
            this.animateMovement(OwlAnimations.DIVE, limbSwing, limbSwingAmount, 2.0F, 2.5F);
        } else if (!entity.isOnGround()) {
            this.animateMovement(OwlAnimations.FLY, limbSwing, limbSwingAmount, 2.0F, 2.5F);
        } else {
            this.updateAnimation(entity.idleAnimationState, OwlAnimations.IDLE, ageInTicks, 1.0F);
        }
    }

    @Override
    public void render(MatrixStack matrices, VertexConsumer vertices,
                       int light, int overlay, float red, float green, float blue, float alpha) {
        root.render(matrices, vertices, light, overlay, red, green, blue, alpha);
    }
}