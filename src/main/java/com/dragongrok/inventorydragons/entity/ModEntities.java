package com.dragongrok.inventorydragons.entity;

import com.dragongrok.inventorydragons.InventoryDragons;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
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
                    .dimensions(1.2f, 1.0f) // larger so it's rideable
                    .build()
    );

    public static void registerModEntities() {
        FabricDefaultAttributeRegistry.register(INVENTORY_DRAGON, InventoryDragonEntity.createDragonAttributes());
        InventoryDragons.LOGGER.info("Registering Inventory Dragon entities (rideable & AI)...");
    }
}
