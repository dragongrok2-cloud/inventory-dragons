package com.dragongrok.inventorydragons.entity;

import com.dragongrok.inventorydragons.InventoryDragons;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class ModEntities {

    public static final EntityType<InventoryDragonEntity> INVENTORY_DRAGON = Registry.register(
            Registries.ENTITY_TYPE,
            Identifier.of(InventoryDragons.MOD_ID, "inventory_dragon"),
            EntityType.Builder.create(InventoryDragonEntity::new, SpawnGroup.CREATURE)
                    .dimensions(0.6f, 0.6f)
                    .build()
    );

    public static void registerModEntities() {
        InventoryDragons.LOGGER.info("Registering Inventory Dragon entities...");
    }
}
