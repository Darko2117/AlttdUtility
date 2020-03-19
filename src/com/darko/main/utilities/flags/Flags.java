package com.darko.main.utilities.flags;

import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.protection.flags.Flag;
import com.sk89q.worldguard.protection.flags.StateFlag;
import com.sk89q.worldguard.protection.flags.registry.FlagConflictException;
import com.sk89q.worldguard.protection.flags.registry.FlagRegistry;

public class Flags {

    public static StateFlag SIT, ANVIL_REPAIR, ANVIL_USE, ENCHANTING_TABLE_USE, NAME_TAG_USE, BONE_MEAL_USE;

    public static void FlagsEnable() {
        SitFlag();
        AnvilRepairFlag();
        AnvilUseFlag();
        EnchantingTableUseFlag();
        NameTagFlag();
        BoneMealFlag();
    }

    public static void SitFlag() {
        FlagRegistry registry = WorldGuard.getInstance().getFlagRegistry();
        try {
            StateFlag flag = new StateFlag("chair-sit", true);
            registry.register(flag);
            SIT = flag;
        } catch (FlagConflictException e) {
            Flag<?> existing = registry.get("chair-sit");
            if (existing instanceof StateFlag) {
                SIT = (StateFlag) existing;
            } else {
            }
        }
    }

    public static void AnvilRepairFlag() {
        FlagRegistry registry = WorldGuard.getInstance().getFlagRegistry();
        try {
            StateFlag flag = new StateFlag("anvil-repair", true);
            registry.register(flag);
            ANVIL_REPAIR = flag;
        } catch (FlagConflictException e) {
            Flag<?> existing = registry.get("anvil-repair");
            if (existing instanceof StateFlag) {
                ANVIL_REPAIR = (StateFlag) existing;
            } else {
            }
        }
    }

    public static void AnvilUseFlag() {
        FlagRegistry registry = WorldGuard.getInstance().getFlagRegistry();
        try {
            StateFlag flag = new StateFlag("anvil-use", true);
            registry.register(flag);
            ANVIL_USE = flag;
        } catch (FlagConflictException e) {
            Flag<?> existing = registry.get("anvil-use");
            if (existing instanceof StateFlag) {
                ANVIL_USE = (StateFlag) existing;
            } else {
            }
        }
    }

    public static void EnchantingTableUseFlag() {
        FlagRegistry registry = WorldGuard.getInstance().getFlagRegistry();
        try {
            StateFlag flag = new StateFlag("enchanting-table-use", true);
            registry.register(flag);
            ENCHANTING_TABLE_USE = flag;
        } catch (FlagConflictException e) {
            Flag<?> existing = registry.get("enchanting-table-use");
            if (existing instanceof StateFlag) {
                ENCHANTING_TABLE_USE = (StateFlag) existing;
            } else {
            }
        }
    }

    public static void NameTagFlag() {
        FlagRegistry registry = WorldGuard.getInstance().getFlagRegistry();
        try {
            StateFlag flag = new StateFlag("name-tag-use", true);
            registry.register(flag);
            NAME_TAG_USE = flag;
        } catch (FlagConflictException e) {
            Flag<?> existing = registry.get("name-tag-use");
            if (existing instanceof StateFlag) {
                NAME_TAG_USE = (StateFlag) existing;
            } else {
            }
        }
    }

    public static void BoneMealFlag() {
        FlagRegistry registry = WorldGuard.getInstance().getFlagRegistry();
        try {
            StateFlag flag = new StateFlag("bone-meal-use", true);
            registry.register(flag);
            BONE_MEAL_USE = flag;
        } catch (FlagConflictException e) {
            Flag<?> existing = registry.get("bone-meal-use");
            if (existing instanceof StateFlag) {
                BONE_MEAL_USE = (StateFlag) existing;
            } else {
            }
        }
    }
}
