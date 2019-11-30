package com.darko.main.utilities.other;

import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.protection.flags.Flag;
import com.sk89q.worldguard.protection.flags.StateFlag;
import com.sk89q.worldguard.protection.flags.registry.FlagConflictException;
import com.sk89q.worldguard.protection.flags.registry.FlagRegistry;

public class Flags {

	public static StateFlag SIT, ANVIL_REPAIR, ANVIL_USE;
	
	public static void FlagsEnable(){
	SitFlag();
	AnvilRepairFlag();
	AnvilUseFlag();
	}
	
	
	public static void SitFlag(){
		FlagRegistry registry = WorldGuard.getInstance().getFlagRegistry();
	    try {
	     StateFlag flag = new StateFlag("sit", true);
	     registry.register(flag);
	     SIT = flag;
	     }catch (FlagConflictException e) {
	     Flag<?> existing = registry.get("sit");
	     if (existing instanceof StateFlag) {
	     SIT = (StateFlag) existing;
	     }else{}}}
	
	public static void AnvilRepairFlag(){
		FlagRegistry registry = WorldGuard.getInstance().getFlagRegistry();
	    try {
	     StateFlag flag = new StateFlag("anvilrepair", true);
	     registry.register(flag);
	     ANVIL_REPAIR = flag;
	     }catch (FlagConflictException e) {
	     Flag<?> existing = registry.get("anvilrepair");
	     if (existing instanceof StateFlag) {
	     ANVIL_REPAIR = (StateFlag) existing;
	     }else{}}}
	
	public static void AnvilUseFlag(){
		FlagRegistry registry = WorldGuard.getInstance().getFlagRegistry();
	    try {
	     StateFlag flag = new StateFlag("anviluse", true);
	     registry.register(flag);
	     ANVIL_USE = flag;
	     }catch (FlagConflictException e) {
	     Flag<?> existing = registry.get("anviluse");
	     if (existing instanceof StateFlag) {
	     ANVIL_USE = (StateFlag) existing;
	     }else{}}}
	
}
