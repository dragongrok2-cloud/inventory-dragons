package com.dragongrok.inventorydragons.item;

import com.dragongrok.inventorydragons.system.DragonLeveling;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.NbtComponent;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.util.List;

/**
 * Base class for all Inventory Dragon items.
 * Stores level and XP in NBT.
 */
public class InventoryDragonItem extends Item {

    public static final String NBT_LEVEL = "DragonLevel";
    public static final String NBT_XP = "DragonXp";
    public static final String NBT_TYPE = "DragonType";

    private final String dragonType;

    public InventoryDragonItem(Settings settings, String dragonType) {
        super(settings);
        this.dragonType = dragonType;
    }

    public String getDragonType() {
        return dragonType;
    }

    public static int getLevel(ItemStack stack) {
        NbtCompound nbt = getNbt(stack);
        return nbt.contains(NBT_LEVEL) ? nbt.getInt(NBT_LEVEL) : 1;
    }

    public static int getXp(ItemStack stack) {
        NbtCompound nbt = getNbt(stack);
        return nbt.contains(NBT_XP) ? nbt.getInt(NBT_XP) : 0;
    }

    public static void setLevelAndXp(ItemStack stack, int level, int xp) {
        NbtCompound nbt = getNbt(stack);
        nbt.putInt(NBT_LEVEL, Math.max(1, Math.min(level, DragonLeveling.MAX_LEVEL)));
        nbt.putInt(NBT_XP, Math.max(0, xp));
        setNbt(stack, nbt);
    }

    public static void addXp(ItemStack stack, int amount) {
        int xp = getXp(stack) + amount;
        int level = getLevel(stack);

        while (level < DragonLeveling.MAX_LEVEL && xp >= DragonLeveling.getXpForLevel(level + 1)) {
            level++;
        }

        setLevelAndXp(stack, level, xp);
    }

    private static NbtCompound getNbt(ItemStack stack) {
        NbtComponent component = stack.get(DataComponentTypes.CUSTOM_DATA);
        if (component != null) {
            return component.copyNbt();
        }
        return new NbtCompound();
    }

    private static void setNbt(ItemStack stack, NbtCompound nbt) {
        stack.set(DataComponentTypes.CUSTOM_DATA, NbtComponent.of(nbt));
    }

    @Override
    public void appendTooltip(ItemStack stack, TooltipContext context, List<Text> tooltip, TooltipType type) {
        int level = getLevel(stack);
        int xp = getXp(stack);
        String title = DragonLeveling.getTitleForLevel(level);

        tooltip.add(Text.literal("Type: " + dragonType).formatted(Formatting.GRAY));
        tooltip.add(Text.literal("Level: " + level + " (" + title + ")").formatted(Formatting.GOLD));
        tooltip.add(Text.literal("XP: " + xp).formatted(Formatting.AQUA));

        if (level < DragonLeveling.MAX_LEVEL) {
            int needed = DragonLeveling.getXpForLevel(level + 1);
            tooltip.add(Text.literal("Next level: " + needed + " XP").formatted(Formatting.DARK_GRAY));
        } else {
            tooltip.add(Text.literal("Max level reached!").formatted(Formatting.LIGHT_PURPLE));
        }
    }
}
