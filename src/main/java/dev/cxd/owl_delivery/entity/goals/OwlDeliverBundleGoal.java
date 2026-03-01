package dev.cxd.owl_delivery.entity.goals;

import dev.cxd.owl_delivery.entity.OwlEntity;
import net.minecraft.block.BlockState;
import net.minecraft.block.LeavesBlock;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.ai.pathing.BirdNavigation;
import net.minecraft.entity.ai.pathing.EntityNavigation;
import net.minecraft.entity.ai.pathing.PathNodeType;
import net.minecraft.entity.passive.TameableEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.EnumSet;
import java.util.UUID;

public class OwlDeliverBundleGoal<T extends TameableEntity> extends Goal {
    private final T tameable;
    private final World world;
    private final double speed;
    private final EntityNavigation navigation;
    private int updateCountdownTicks;
    private final float maxDistance;
    private final float minDistance;
    private float oldWaterPathfindingPenalty;
    private final boolean leavesAllowed;
    private LivingEntity receiver;

    private boolean inDeliveryRange = false;

    public OwlDeliverBundleGoal(T tameable, double speed, float minDistance, float maxDistance, boolean leavesAllowed) {
        this.tameable = tameable;
        this.world = tameable.getWorld();
        this.speed = speed;
        this.navigation = tameable.getNavigation();
        this.minDistance = minDistance;
        this.maxDistance = maxDistance;
        this.leavesAllowed = leavesAllowed;

        this.setControls(EnumSet.of(Control.MOVE, Control.LOOK));

        if (!(tameable.getNavigation() instanceof BirdNavigation)
                && !(tameable.getNavigation() instanceof EntityNavigation)
                || !(tameable instanceof OwlEntity)) {
            throw new IllegalArgumentException("Unsupported mob type for DeliverBundleGoal");
        }
    }

    @Override
    public boolean canStart() {
        OwlEntity owl = (OwlEntity) this.tameable;

        UUID receiverUuid = owl.getReceiverUuid();
        if (receiverUuid == null) return false;

        if (!owl.hasCarriedBundle()) return false;

        Entity potentialReceiver = ((ServerWorld) this.world).getEntity(receiverUuid);
        if (!(potentialReceiver instanceof LivingEntity livingReceiver)) return false;
        this.receiver = livingReceiver;

        LivingEntity owner = this.tameable.getOwner();
        if (owner == null || !owner.isAlive()) return false;

        return true;
    }

    @Override
    public boolean shouldContinue() {
        OwlEntity owl = (OwlEntity) this.tameable;

        if (!owl.hasCarriedBundle()) return false;

        if (this.receiver == null || !this.receiver.isAlive()) return false;

        return true;
    }

    @Override
    public void start() {
        this.updateCountdownTicks = 0;
        this.inDeliveryRange = false;
        this.oldWaterPathfindingPenalty = this.tameable.getPathfindingPenalty(PathNodeType.WATER);
        this.tameable.setPathfindingPenalty(PathNodeType.WATER, 0.0F);
        this.tameable.getDataTracker().set(OwlEntity.GOING_TO_RECEIVER, true);
    }

    @Override
    public void stop() {
        this.receiver = null;
        this.inDeliveryRange = false;
        this.navigation.stop();
        this.tameable.setPathfindingPenalty(PathNodeType.WATER, this.oldWaterPathfindingPenalty);
    }

    @Override
    public void tick() {
        if (this.receiver == null) return;

        OwlEntity owl = (OwlEntity) this.tameable;

        this.tameable.getLookControl().lookAt(this.receiver, 10.0F, this.tameable.getMaxLookPitchChange());

        if (this.tameable.squaredDistanceTo(this.receiver) < 1.0D) {            deliverBundle(owl);
            return;
        }

        if (--this.updateCountdownTicks > 0) return;
        this.updateCountdownTicks = 10;

        double distanceSq = this.tameable.squaredDistanceTo(this.receiver);

        if (!this.inDeliveryRange) {
            teleportNearReceiver();
            this.inDeliveryRange = true;
        } else {
            this.navigation.startMovingTo(this.receiver, this.speed);
        }
    }

    private void deliverBundle(OwlEntity owl) {
        ItemStack bundle = owl.getCarriedBundle().copy();
        ItemEntity drop = new ItemEntity(
                this.world,
                this.receiver.getX(),
                this.receiver.getY() + 0.5D,
                this.receiver.getZ(),
                bundle
        );
        drop.setVelocity(0, 0.2D, 0);
        this.world.spawnEntity(drop);

        owl.setCarriedBundle(ItemStack.EMPTY);
        owl.setReceiverUuid(null);
        owl.getDataTracker().set(OwlEntity.GOING_TO_RECEIVER, false);

        teleportToOwner(owl);

        this.navigation.stop();
        this.stop();
    }

    private void teleportToOwner(OwlEntity owl) {
        LivingEntity owner = owl.getOwner();
        if (owner == null) return;

        BlockPos ownerPos = owner.getBlockPos();

        for (int i = 0; i < 20; i++) {
            int dx = getRandomInt(-3, 3);
            int dy = getRandomInt(0, 2);
            int dz = getRandomInt(-3, 3);

            BlockPos target = ownerPos.add(dx, dy, dz);
            if (canTeleportTo(target)) {
                owl.refreshPositionAndAngles(
                        target.getX() + 0.5D,
                        target.getY(),
                        target.getZ() + 0.5D,
                        owl.getYaw(),
                        owl.getPitch()
                );
                return;
            }
        }

        owl.refreshPositionAndAngles(
                ownerPos.getX() + 0.5D,
                ownerPos.getY() + 1.0D,
                ownerPos.getZ() + 0.5D,
                owl.getYaw(),
                owl.getPitch()
        );
    }

    private void teleportNearReceiver() {
        BlockPos receiverPos = this.receiver.getBlockPos();

        for (int i = 0; i < 30; i++) {
            int dx = getRandomInt(-8, 8);
            int dy = getRandomInt(-2, 4);
            int dz = getRandomInt(-8, 8);

            if (Math.abs(dx) < 3 && Math.abs(dz) < 3) continue;

            BlockPos target = receiverPos.add(dx, dy, dz);
            if (canTeleportTo(target)) {
                this.tameable.refreshPositionAndAngles(
                        target.getX() + 0.5D,
                        target.getY(),
                        target.getZ() + 0.5D,
                        this.tameable.getYaw(),
                        this.tameable.getPitch()
                );
                this.navigation.stop();
                return;
            }
        }

        this.inDeliveryRange = false;
    }

    private boolean canTeleportTo(BlockPos pos) {
        BlockState blockState = this.world.getBlockState(pos.down());
        if (!this.leavesAllowed && blockState.getBlock() instanceof LeavesBlock) {
            return false;
        }

        BlockPos offset = pos.subtract(this.tameable.getBlockPos());
        return this.world.isSpaceEmpty(this.tameable, this.tameable.getBoundingBox().offset(offset));
    }

    private int getRandomInt(int min, int max) {
        return this.tameable.getRandom().nextInt(max - min + 1) + min;
    }
}