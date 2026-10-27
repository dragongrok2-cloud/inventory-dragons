package com.dragongrok.inventorydragons.entity;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.mob.PathAwareEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

/**
 * Inventory Dragon entity.
 * - Summon / recall with whistle
 * - Follows owner
 * - Rideable & flyable
 * - Element-specific breath attacks
 */
public class InventoryDragonEntity extends PathAwareEntity {

    private int level = 1;
    private String dragonType = "Fire";
    private PlayerEntity owner;
    private int abilityCooldown = 0;

    public InventoryDragonEntity(EntityType<? extends PathAwareEntity> entityType, World world) {
        super(entityType, world);
        this.setHealth(20.0f + level * 2);
    }

    public static DefaultAttributeContainer.Builder createDragonAttributes() {
        return PathAwareEntity.createMobAttributes()
                .add(EntityAttributes.GENERIC_MAX_HEALTH, 30.0)
                .add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.35)
                .add(EntityAttributes.GENERIC_FLYING_SPEED, 0.6)
                .add(EntityAttributes.GENERIC_FOLLOW_RANGE, 32.0)
                .add(EntityAttributes.GENERIC_ATTACK_DAMAGE, 4.0);
    }

    @Override
    protected void initGoals() {
        this.goalSelector.add(0, new SwimGoal(this));
        this.goalSelector.add(1, new EscapeDangerGoal(this, 1.4));
        this.goalSelector.add(2, new MeleeAttackGoal(this, 1.2, true));
        this.goalSelector.add(3, new FollowOwnerGoal(this, 1.2, 6.0f, 2.0f));
        this.goalSelector.add(4, new LookAtEntityGoal(this, PlayerEntity.class, 10.0f));
        this.goalSelector.add(5, new WanderAroundFarGoal(this, 0.8));
        this.goalSelector.add(6, new LookAroundGoal(this));

        this.targetSelector.add(1, new RevengeGoal(this));
        this.targetSelector.add(2, new ActiveTargetGoal<>(this, net.minecraft.entity.mob.HostileEntity.class, true));
    }

    @Override
    public void tick() {
        super.tick();

        if (abilityCooldown > 0) {
            abilityCooldown--;
        }

        // While being ridden, player can activate breath by "attacking" (left click)
        if (!this.getWorld().isClient && this.hasPassengers() && this.getFirstPassenger() instanceof PlayerEntity player) {
            if (player.handSwinging && abilityCooldown <= 0) {
                DragonAbilities.performAreaAttack(this);
                abilityCooldown = 40; // 2 seconds cooldown
            }
        }
    }

    @Override
    public boolean tryAttack(Entity target) {
        if (super.tryAttack(target)) {
            if (target instanceof LivingEntity living) {
                DragonAbilities.performBreathAttack(this, living);
            }
            return true;
        }
        return false;
    }

    // ===== Level & Type =====

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = Math.max(1, Math.min(level, 10));
        this.setHealth(20.0f + this.level * 2);
    }

    public String getDragonType() {
        return dragonType;
    }

    public void setDragonType(String type) {
        this.dragonType = type != null ? type : "Fire";
    }

    public void setOwner(PlayerEntity owner) {
        this.owner = owner;
    }

    public PlayerEntity getOwner() {
        return owner;
    }

    // ===== Riding & Flying =====

    @Override
    public ActionResult interactMob(PlayerEntity player, Hand hand) {
        if (!this.getWorld().isClient && !this.hasPassengers()) {
            player.startRiding(this);
            this.setOwner(player);
            return ActionResult.SUCCESS;
        }
        return super.interactMob(player, hand);
    }

    @Override
    public boolean canBeLeashedBy(PlayerEntity player) {
        return true;
    }

    @Override
    protected void updatePassengerPosition(Entity passenger, PositionUpdater positionUpdater) {
        if (this.hasPassenger(passenger)) {
            double yOffset = this.getHeight() * 0.75;
            positionUpdater.accept(passenger, this.getX(), this.getY() + yOffset, this.getZ());
        }
    }

    @Override
    public Vec3d updatePassengerForDismount(LivingEntity passenger) {
        return new Vec3d(this.getX(), this.getY() + 0.5, this.getZ());
    }

    @Override
    public void travel(Vec3d movementInput) {
        if (this.hasPassengers() && this.getFirstPassenger() instanceof PlayerEntity player) {
            this.setYaw(player.getYaw());
            this.prevYaw = this.getYaw();
            this.setPitch(player.getPitch() * 0.5f);
            this.setRotation(this.getYaw(), this.getPitch());
            this.headYaw = this.getYaw();
            this.bodyYaw = this.getYaw();

            float side = player.sidewaysSpeed * 0.5f;
            float forward = player.forwardSpeed;

            if (forward <= 0.0f) {
                forward *= 0.25f;
            }

            this.setMovementSpeed((float) this.getAttributeValue(EntityAttributes.GENERIC_FLYING_SPEED));

            double vertical = 0.0;
            if (player.jumping) {
                vertical = 0.4;
            } else if (player.isSneaking()) {
                vertical = -0.4;
            }

            super.travel(new Vec3d(side, vertical, forward));
        } else {
            super.travel(movementInput);
        }
    }

    @Override
    public boolean isLogicalSideForUpdatingMovement() {
        if (this.hasPassengers() && this.getFirstPassenger() instanceof PlayerEntity) {
            return true;
        }
        return super.isLogicalSideForUpdatingMovement();
    }

    // Follow owner goal
    static class FollowOwnerGoal extends Goal {
        private final InventoryDragonEntity dragon;
        private final double speed;
        private final float maxDistance;
        private final float minDistance;

        public FollowOwnerGoal(InventoryDragonEntity dragon, double speed, float maxDistance, float minDistance) {
            this.dragon = dragon;
            this.speed = speed;
            this.maxDistance = maxDistance;
            this.minDistance = minDistance;
        }

        @Override
        public boolean canStart() {
            PlayerEntity owner = dragon.getOwner();
            if (owner == null || !owner.isAlive() || dragon.hasPassengers()) return false;
            return dragon.squaredDistanceTo(owner) > (minDistance * minDistance);
        }

        @Override
        public void tick() {
            PlayerEntity owner = dragon.getOwner();
            if (owner != null) {
                dragon.getNavigation().startMovingTo(owner, speed);
            }
        }

        @Override
        public boolean shouldContinue() {
            PlayerEntity owner = dragon.getOwner();
            if (owner == null || !owner.isAlive() || dragon.hasPassengers()) return false;
            return dragon.squaredDistanceTo(owner) > (maxDistance * maxDistance * 0.25);
        }
    }
}
