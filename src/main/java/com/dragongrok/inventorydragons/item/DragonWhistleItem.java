package com.dragongrok.inventorydragons.item;

import com.dragongrok.inventorydragons.entity.InventoryDragonEntity;
import com.dragongrok.inventorydragons.entity.ModEntities;
import com.dragongrok.inventorydragons.sound.ModSounds;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundCategory;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.Box;
import net.minecraft.world.World;

import java.util.List;

/**
 * Dragon Whistle - summon or recall Inventory Dragons.
 * Right-click: if a dragon item is in offhand/mainhand, try to summon it as entity.
 * If already summoned nearby, recall it back into item form.
 */
public class DragonWhistleItem extends Item {

    public DragonWhistleItem(Settings settings) {
        super(settings);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack whistle = user.getStackInHand(hand);

        if (world.isClient) {
            return TypedActionResult.success(whistle);
        }

        // Play whistle sound
        world.playSound(null, user.getX(), user.getY(), user.getZ(),
                ModSounds.DRAGON_WHISTLE, SoundCategory.PLAYERS, 1.0f, 1.0f);

        // Check for nearby summoned dragons to recall
        Box searchBox = user.getBoundingBox().expand(16.0);
        List<InventoryDragonEntity> nearby = world.getEntitiesByClass(
                InventoryDragonEntity.class, searchBox, e -> e.isAlive());

        if (!nearby.isEmpty()) {
            // Recall the closest one
            InventoryDragonEntity dragon = nearby.get(0);
            String type = dragon.getDragonType();
            int level = dragon.getLevel();

            // Create item version
            ItemStack dragonItem = createDragonItem(type, level);
            if (!user.getInventory().insertStack(dragonItem)) {
                user.dropItem(dragonItem, false);
            }

            dragon.discard();
            user.sendMessage(net.minecraft.text.Text.literal("Dragon recalled into your inventory!"), true);
            return TypedActionResult.success(whistle);
        }

        // Try to summon from hand or inventory
        ItemStack dragonStack = findDragonInHands(user);
        if (dragonStack.isEmpty()) {
            dragonStack = findFirstDragonInInventory(user);
        }

        if (!dragonStack.isEmpty() && dragonStack.getItem() instanceof InventoryDragonItem invDragon) {
            // Summon entity
            InventoryDragonEntity entity = ModEntities.INVENTORY_DRAGON.create(world);
            if (entity != null) {
                entity.setPosition(user.getX(), user.getY() + 1.0, user.getZ());
                entity.setDragonType(invDragon.getDragonType());
                entity.setLevel(InventoryDragonItem.getLevel(dragonStack));

                world.spawnEntity(entity);

                // Consume the item version
                dragonStack.decrement(1);

                user.sendMessage(net.minecraft.text.Text.literal(
                        "You summoned your " + invDragon.getDragonType() + " Dragon!"), true);

                world.playSound(null, user.getX(), user.getY(), user.getZ(),
                        ModSounds.DRAGON_ROAR, SoundCategory.PLAYERS, 0.8f, 1.2f);
            }
            return TypedActionResult.success(whistle);
        }

        user.sendMessage(net.minecraft.text.Text.literal("No dragon to summon or recall nearby."), true);
        return TypedActionResult.pass(whistle);
    }

    private ItemStack findDragonInHands(PlayerEntity player) {
        ItemStack main = player.getMainHandStack();
        ItemStack off = player.getOffHandStack();
        if (main.getItem() instanceof InventoryDragonItem) return main;
        if (off.getItem() instanceof InventoryDragonItem) return off;
        return ItemStack.EMPTY;
    }

    private ItemStack findFirstDragonInInventory(PlayerEntity player) {
        for (int i = 0; i < player.getInventory().size(); i++) {
            ItemStack stack = player.getInventory().getStack(i);
            if (stack.getItem() instanceof InventoryDragonItem) {
                return stack;
            }
        }
        return ItemStack.EMPTY;
    }

    private ItemStack createDragonItem(String type, int level) {
        Item item = switch (type.toLowerCase()) {
            case "fire" -> ModItems.FIRE_INVENTORY_DRAGON;
            case "ice" -> ModItems.ICE_INVENTORY_DRAGON;
            case "nature" -> ModItems.NATURE_INVENTORY_DRAGON;
            case "shadow" -> ModItems.SHADOW_INVENTORY_DRAGON;
            case "light" -> ModItems.LIGHT_INVENTORY_DRAGON;
            case "wind" -> ModItems.WIND_INVENTORY_DRAGON;
            default -> ModItems.BABY_INVENTORY_DRAGON;
        };
        ItemStack stack = new ItemStack(item);
        InventoryDragonItem.setLevelAndXp(stack, level, 0);
        return stack;
    }
}
