package com.dragongrok.inventorydragons.system;

/**
 * Simple leveling system for Inventory Dragons.
 * Dragons gain experience by:
 * - Being fed treats
 * - Protecting the player
 * - Exploring the world while summoned
 */
public class DragonLeveling {

    public static final int MAX_LEVEL = 10;

    public static int getXpForLevel(int level) {
        // Simple formula: level^2 * 50
        return level * level * 50;
    }

    public static int getLevelFromXp(int xp) {
        int level = 1;
        while (level < MAX_LEVEL && xp >= getXpForLevel(level + 1)) {
            level++;
        }
        return level;
    }

    public static String getTitleForLevel(int level) {
        return switch (level) {
            case 1, 2 -> "Hatchling";
            case 3, 4 -> "Young Dragon";
            case 5, 6 -> "Adult Dragon";
            case 7, 8 -> "Elder Dragon";
            case 9 -> "Ancient Dragon";
            case 10 -> "Legendary Dragon";
            default -> "Dragon";
        };
    }

    // Future: apply buffs based on level and type
    public static void applyLevelBuffs(int level, String type) {
        // TODO: Implement actual status effects / attributes
    }
}
