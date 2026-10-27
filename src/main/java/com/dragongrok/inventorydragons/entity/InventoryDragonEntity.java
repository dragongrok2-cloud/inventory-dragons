package com.dragongrok.inventorydragons.entity;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.mob.PathAwareEntity;
import net.minecraft.world.World;

/**
 * Placeholder entity for Inventory Dragons.
 * In the future this will be a small flying companion that can:
 * - Be stored in inventory as an item
 * - Be summoned with the Dragon Whistle
 * - Level up and gain abilities
 * - Protect the player
 */
public class InventoryDragonEntity extends PathAwareEntity {

    private int level = 1;
    private String dragonType = "fire"; // fire, ice, nature, shadow, light

    public InventoryDragonEntity(EntityType<? extends PathAwareEntity> entityType, World world) {
        super(entityType, world);
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = Math.max(1, Math.min(level, 10)); // Cap at level 10 for now
    }

    public String getDragonType() {
        return dragonType;
    }

    public void setDragonType(String type) {
        this.dragonType = type;
    }

    // TODO: Add AI goals, flying, inventory interaction, buffs, etc.
}
