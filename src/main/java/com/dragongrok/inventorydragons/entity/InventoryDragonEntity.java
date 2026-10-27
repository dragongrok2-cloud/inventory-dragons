package com.dragongrok.inventorydragons.entity;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.mob.PathAwareEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;

/**
 * Inventory Dragon entity - can be summoned with the Dragon Whistle.
 * Stores type and level so it can be recalled back into item form.
 */
public class InventoryDragonEntity extends PathAwareEntity {

    private int level = 1;
    private String dragonType = "Fire";

    public InventoryDragonEntity(EntityType<? extends PathAwareEntity> entityType, World world) {
        super(entityType, world);
        this.setHealth(20.0f + level * 2);
    }

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

    @Override
    public boolean canBeLeashedBy(PlayerEntity player) {
        return true;
    }

    // TODO: Add proper AI (follow owner, fly, attack nearby hostiles based on type)
}
