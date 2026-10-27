package com.dragongrok.inventorydragons.entity;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.util.List;

/**
 * Type-specific abilities for Inventory Dragons.
 * Called when the dragon attacks or when the rider activates special ability.
 */
public class DragonAbilities {

    public static void performBreathAttack(InventoryDragonEntity dragon, LivingEntity target) {
        if (dragon.getWorld().isClient) return;

        String type = dragon.getDragonType().toLowerCase();
        World world = dragon.getWorld();
        Vec3d pos = target.getPos();

        switch (type) {
            case "fire" -> {
                target.setOnFireFor(4);
                target.damage(world.getDamageSources().onFire(), 4.0f + dragon.getLevel());
                spawnParticles(world, pos, ParticleTypes.FLAME, 20);
                world.playSound(null, pos.x, pos.y, pos.z, SoundEvents.ITEM_FIRECHARGE_USE, dragon.getSoundCategory(), 1.0f, 1.0f);
            }
            case "ice" -> {
                target.addStatusEffect(new StatusEffectInstance(StatusEffects.SLOWNESS, 60, 1));
                target.damage(world.getDamageSources().magic(), 3.0f + dragon.getLevel() * 0.5f);
                spawnParticles(world, pos, ParticleTypes.SNOWFLAKE, 25);
                world.playSound(null, pos.x, pos.y, pos.z, SoundEvents.BLOCK_GLASS_BREAK, dragon.getSoundCategory(), 0.8f, 1.2f);
            }
            case "nature" -> {
                target.addStatusEffect(new StatusEffectInstance(StatusEffects.POISON, 80, 0));
                target.damage(world.getDamageSources().magic(), 3.0f + dragon.getLevel() * 0.4f);
                spawnParticles(world, pos, ParticleTypes.HAPPY_VILLAGER, 15);
            }
            case "shadow" -> {
                target.addStatusEffect(new StatusEffectInstance(StatusEffects.BLINDNESS, 40, 0));
                target.addStatusEffect(new StatusEffectInstance(StatusEffects.WEAKNESS, 60, 0));
                target.damage(world.getDamageSources().magic(), 3.5f + dragon.getLevel() * 0.5f);
                spawnParticles(world, pos, ParticleTypes.SMOKE, 20);
            }
            case "light" -> {
                target.addStatusEffect(new StatusEffectInstance(StatusEffects.GLOWING, 100, 0));
                target.damage(world.getDamageSources().magic(), 4.0f + dragon.getLevel() * 0.6f);
                spawnParticles(world, pos, ParticleTypes.END_ROD, 20);
            }
            case "wind" -> {
                // Knockback + slight damage
                Vec3d knock = target.getPos().subtract(dragon.getPos()).normalize().multiply(1.5);
                target.addVelocity(knock.x, 0.4, knock.z);
                target.damage(world.getDamageSources().magic(), 2.5f + dragon.getLevel() * 0.4f);
                spawnParticles(world, pos, ParticleTypes.CLOUD, 25);
                world.playSound(null, pos.x, pos.y, pos.z, SoundEvents.ENTITY_PHANTOM_FLAP, dragon.getSoundCategory(), 1.0f, 1.0f);
            }
            default -> {
                target.damage(world.getDamageSources().mobAttack(dragon), 3.0f + dragon.getLevel() * 0.3f);
            }
        }
    }

    /** Area attack around the dragon (useful while flying) */
    public static void performAreaAttack(InventoryDragonEntity dragon) {
        if (dragon.getWorld().isClient) return;

        World world = dragon.getWorld();
        Box box = dragon.getBoundingBox().expand(4.0 + dragon.getLevel() * 0.3);
        List<LivingEntity> targets = world.getEntitiesByClass(LivingEntity.class, box,
                e -> e != dragon && e.isAlive() && !(e instanceof PlayerEntity player && player == dragon.getOwner()));

        for (LivingEntity target : targets) {
            performBreathAttack(dragon, target);
        }

        // Visual feedback
        spawnParticles(world, dragon.getPos(), ParticleTypes.CRIT, 30);
    }

    private static void spawnParticles(World world, Vec3d pos, net.minecraft.particle.ParticleEffect particle, int count) {
        if (world instanceof ServerWorld serverWorld) {
            serverWorld.spawnParticles(particle, pos.x, pos.y + 0.5, pos.z, count, 0.5, 0.5, 0.5, 0.05);
        }
    }
}
