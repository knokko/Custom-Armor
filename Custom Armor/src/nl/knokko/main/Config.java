package nl.knokko.main;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.logging.Level;

import nl.knokko.armor.ArmorPiece;
import nl.knokko.armor.ArmorPlace;
import nl.knokko.armor.ArmorType;
import nl.knokko.armor.enchantment.ArmorEnchantmentCourse;
import nl.knokko.armor.enchantment.SingleArmorEnchantment;
import nl.knokko.armor.upgrade.ArmorUpgradeCourse;
import nl.knokko.armor.upgrade.SingleArmorUpgrade;
import nl.knokko.armor.upgrade.SingleArmorUpgrade.Stack;
import nl.knokko.recipes.ArmorRecipes;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.MemorySection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.enchantments.Enchantment;

import com.google.common.collect.Lists;

import static org.bukkit.Material.*;
import static org.bukkit.enchantments.Enchantment.*;

public class Config {
	
	private static final String ARMOR_TYPES = "armor types";
	private static final String UPGRADES = "upgrades";
	private static final String ENCHANTMENTS = "custom enchantments";
	private static final String HAS_BEEN_CREATED = "has been created";
	
	private static final String USE_CUSTOM_ANVIL = "use custom anvil";
	private static final String DISABLE_VANILLA_ANVIL = "disable vanilla anvil";
	
	private static final String USE_CUSTOM_ENCHANTING = "use custom enchanting";
	private static final String DISABLE_VANILLA_ENCHANTING = "disable vanilla enchanting";
	
	private static final String AVAILABLE_ARMOR = "available on the following armor";
	private static final String UPGRADE_ICON = "icon";
	private static final String PRICE = "price";
	private static final String REQUIRED_LEVEL = "required level";
	private static final String LEVEL_COST = "level cost";
	
	private static final String RECIPE = "recipe";
	private static final String ARMOR_TYPE = "armor type";
	private static final String ARMOR_SLOT = "armor slot";
	
	private static final String UNBREAKABLE = "unbreakable";
	private static final String ARMOR = "armor";
	private static final String TOUGHNESS = "armor toughness";
	private static final String ATTACK_DAMAGE = "attack damage";
	private static final String ATTACK_SPEED = "attack speed";
	private static final String MAX_HEALTH = "extra health";
	private static final String MOVEMENT_SPEED = "extra speed";
	private static final String KNOCKBACK_RESISTANCE = "knockback resistance";
	private static final String LUCK = "extra luck";
	
	private static final String LEATHER_RED = "leather red component";
	private static final String LEATHER_GREEN = "leather green component";
	private static final String LEATHER_BLUE = "leather blue component";
	
	private FileConfiguration config;
	
	private boolean useCustomAnvil;
	private boolean disableVanillaAnvil;

	private boolean useCustomEnchanting;
	private boolean disableVanillaEnchanting;
	
	private final List<ArmorPiece> armorList;
	private final List<ArmorUpgradeCourse> upgrades;
	private final List<ArmorEnchantmentCourse> enchantments;
	
	private final Map<ArmorPiece,List<ArmorUpgradeCourse>> upgradeMap;
	private final Map<ArmorPiece,List<ArmorEnchantmentCourse>> enchantMap;

	public Config(CustomArmor plugin) {
		config = plugin.getConfig();
		armorList = new ArrayList<ArmorPiece>();
		upgrades = new ArrayList<ArmorUpgradeCourse>();
		enchantments = new ArrayList<ArmorEnchantmentCourse>();
		upgradeMap = new HashMap<ArmorPiece,List<ArmorUpgradeCourse>>();
		enchantMap = new HashMap<ArmorPiece,List<ArmorEnchantmentCourse>>();
	}
	
	private void clearDoubles(MemorySection ms, String... paths){
		for(String path : paths)
			clearDouble(ms, path);
	}
	
	private void clearDouble(MemorySection ms, String path){
		if(ms.getDouble(path) == 0)
			config.set(ms.getCurrentPath() + "." + path, null);
	}

	private void clearBooleans(MemorySection ms, String... paths){
		for(String path : paths)
			clearBoolean(ms, path);
	}
	
	private void clearBoolean(MemorySection ms, String path){
		if(!ms.getBoolean(path))
			config.set(ms.getCurrentPath() + "." + path, null);
	}
	
	private void clearInts(MemorySection ms, String... paths){
		for(String path : paths)
			clearInt(ms, path);
	}
	
	private void clearInt(MemorySection ms, String path){
		if(ms.getInt(path) == 0)
			config.set(ms.getCurrentPath() + "." + path, null);
	}
	
	public void compress(){
		ConfigurationSection section = getSection(ARMOR_TYPES);
		Map<String,Object> map = section.getValues(false);
		Set<Entry<String,Object>> set = map.entrySet();
		for(Entry<String,Object> entry : set){
			if(entry.getValue() instanceof MemorySection){
				MemorySection ms = (MemorySection) entry.getValue();
				clearDoubles(ms, ARMOR, TOUGHNESS, ATTACK_DAMAGE, ATTACK_SPEED, MAX_HEALTH, MOVEMENT_SPEED, KNOCKBACK_RESISTANCE, LUCK);
				clearBoolean(ms, UNBREAKABLE);
				clearInts(ms, e(PROTECTION_ENVIRONMENTAL), e(PROTECTION_FIRE), e(PROTECTION_FALL), e(PROTECTION_EXPLOSIONS), e(PROTECTION_PROJECTILE), e(OXYGEN),e(WATER_WORKER), 
						e(THORNS), e(DEPTH_STRIDER), e(FROST_WALKER), e(DURABILITY), e(MENDING), e(VANISHING_CURSE), e(BINDING_CURSE), LEATHER_RED, LEATHER_GREEN, LEATHER_BLUE);
				ArmorType type = ArmorType.fromString(ms.getString(ARMOR_TYPE));
				if(type != ArmorType.LEATHER){
					config.set(ms.getCurrentPath() + "." + LEATHER_RED, null);
					config.set(ms.getCurrentPath() + "." + LEATHER_GREEN, null);
					config.set(ms.getCurrentPath() + "." + LEATHER_BLUE, null);
				}
			}
		}
		section = getSection(UPGRADES);
		for(Entry<String,Object> entry : section.getValues(false).entrySet()){
			if(entry.getValue() instanceof MemorySection){
				MemorySection ms = (MemorySection) entry.getValue();
				for(Entry<String,Object> entry2 : ms.getValues(false).entrySet()){
					if(entry2.getValue() instanceof MemorySection){
						MemorySection single = (MemorySection) entry2.getValue();
						clearBooleans(single, UNBREAKABLE, e(WATER_WORKER), e(MENDING), e(VANISHING_CURSE), e(BINDING_CURSE));
						clearDoubles(single, ARMOR, TOUGHNESS, ATTACK_DAMAGE, ATTACK_SPEED, MAX_HEALTH, MOVEMENT_SPEED, KNOCKBACK_RESISTANCE, LUCK);
						clearInts(single, e(PROTECTION_ENVIRONMENTAL), e(PROTECTION_FIRE), e(PROTECTION_FALL), e(PROTECTION_EXPLOSIONS), e(PROTECTION_PROJECTILE),
							e(OXYGEN), e(THORNS), e(DEPTH_STRIDER), e(FROST_WALKER), e(DURABILITY));
					}
				}
			}
		}
		section = getSection(ENCHANTMENTS);
		for(Entry<String,Object> entry : section.getValues(false).entrySet()){
			if(entry.getValue() instanceof MemorySection){
				MemorySection ms = (MemorySection) entry.getValue();
				for(Entry<String,Object> entry2 : ms.getValues(false).entrySet()){
					if(entry2.getValue() instanceof MemorySection){
						MemorySection single = (MemorySection) entry2.getValue();
						clearBooleans(single, UNBREAKABLE, e(WATER_WORKER), e(MENDING), e(VANISHING_CURSE), e(BINDING_CURSE));
						clearDoubles(single, ARMOR, TOUGHNESS, ATTACK_DAMAGE, ATTACK_SPEED, MAX_HEALTH, MOVEMENT_SPEED, KNOCKBACK_RESISTANCE, LUCK);
						clearInts(single, e(PROTECTION_ENVIRONMENTAL), e(PROTECTION_FIRE), e(PROTECTION_FALL), e(PROTECTION_EXPLOSIONS), e(PROTECTION_PROJECTILE),
							e(OXYGEN), e(THORNS), e(DEPTH_STRIDER), e(FROST_WALKER), e(DURABILITY), REQUIRED_LEVEL);
					}
				}
			}
		}
		CustomArmor.getInstance().saveConfig();
	}
	
	private void clearConfig(){
		config.set(ARMOR_TYPES, null);
		config.set(UPGRADES, null);
		config.set(ENCHANTMENTS, null);
	}
	
	void initDefaults(){
		if(config.getBoolean(HAS_BEEN_CREATED))
			return;
		config.set(HAS_BEEN_CREATED, true);
		config.set(USE_CUSTOM_ANVIL, false);
		config.set(DISABLE_VANILLA_ANVIL, false);
		config.set(USE_CUSTOM_ENCHANTING, false);
		config.set(DISABLE_VANILLA_ENCHANTING, false);
		addVanillaArmor();
		config.options().copyDefaults(true);
		CustomArmor.getInstance().saveConfig();
	}
	
	public void exampleNoArmor(){
		clearConfig();
		config.set(HAS_BEEN_CREATED, true);
		config.set(USE_CUSTOM_ANVIL, false);
		config.set(DISABLE_VANILLA_ANVIL, false);
		config.set(USE_CUSTOM_ENCHANTING, false);
		config.set(DISABLE_VANILLA_ENCHANTING, false);
		config.options().copyDefaults(true);
		CustomArmor.getInstance().saveConfig();
	}
	
	private ConfigurationSection addVanillaArmor(){
		ConfigurationSection armor = getSection(ARMOR_TYPES);
		addHelmet(armor, "Diamond Helmet", ArmorType.DIAMOND, 3, 2, DIAMOND);
		addChestplate(armor, "Diamond Chestplate", ArmorType.DIAMOND, 8, 2, DIAMOND);
		addLeggings(armor, "Diamond Leggings", ArmorType.DIAMOND, 6, 2, DIAMOND);
		addBoots(armor, "Diamond Boots", ArmorType.DIAMOND, 3, 2, DIAMOND);
		addHelmet(armor, "Iron Helmet", ArmorType.IRON, 2, 0, IRON_INGOT);
		addChestplate(armor, "Iron Chestplate", ArmorType.IRON, 6, 0, IRON_INGOT);
		addLeggings(armor, "Iron Leggings", ArmorType.IRON, 5, 0, IRON_INGOT);
		addBoots(armor, "Iron Boots", ArmorType.IRON, 2, 0, IRON_INGOT);
		addHelmet(armor, "Golden Helmet", ArmorType.GOLD, 2, 0, GOLD_INGOT);
		addChestplate(armor, "Golden Chestplate", ArmorType.GOLD, 5, 0, GOLD_INGOT);
		addLeggings(armor, "Golden Leggings", ArmorType.GOLD, 3, 0, GOLD_INGOT);
		addBoots(armor, "Golden Boots", ArmorType.GOLD, 1, 0, GOLD_INGOT);
		addHelmet(armor, "Leather Cap", ArmorType.LEATHER, 1, 0, LEATHER);
		addChestplate(armor, "Leather Tunic", ArmorType.LEATHER, 3, 0, LEATHER);
		addLeggings(armor, "Leather Pants", ArmorType.LEATHER, 2, 0, LEATHER);
		addBoots(armor, "Leather Boots", ArmorType.LEATHER, 1, 0, LEATHER);
		return armor;
	}
	
	public void exampleChainmail(){
		clearConfig();
		config.set(HAS_BEEN_CREATED, true);
		config.set(USE_CUSTOM_ANVIL, false);
		config.set(DISABLE_VANILLA_ANVIL, false);
		config.set(USE_CUSTOM_ENCHANTING, false);
		config.set(DISABLE_VANILLA_ENCHANTING, false);
		ConfigurationSection armor = addVanillaArmor();
		addHelmet(armor, "Chainmail Helmet", ArmorType.CHAIN, 2, 0, FLINT);
		addChestplate(armor, "Chainmail Chestplate", ArmorType.CHAIN, 5, 0, FLINT);
		addLeggings(armor, "Chainmail Leggings", ArmorType.CHAIN, 4, 0, FLINT);
		addBoots(armor, "Chainmail Boots", ArmorType.CHAIN, 1, 0, FLINT);
		config.options().copyDefaults(true);
		CustomArmor.getInstance().saveConfig();
	}
	
	public void exampleCactusBlaze(){
		clearConfig();
		config.set(HAS_BEEN_CREATED, true);
		config.set(USE_CUSTOM_ANVIL, true);
		config.set(DISABLE_VANILLA_ANVIL, false);
		config.set(USE_CUSTOM_ENCHANTING, false);
		config.set(DISABLE_VANILLA_ENCHANTING, false);
		ConfigurationSection armor = addVanillaArmor();
		addHelmet(armor, "Chainmail Helmet", ArmorType.CHAIN, 2, 0, FLINT);
		addChestplate(armor, "Chainmail Chestplate", ArmorType.CHAIN, 5, 0, FLINT);
		addLeggings(armor, "Chainmail Leggings", ArmorType.CHAIN, 4, 0, FLINT);
		addBoots(armor, "Chainmail Boots", ArmorType.CHAIN, 1, 0, FLINT);
		addArmor(armor, "Cactus Hat", ArmorType.LEATHER, ArmorPlace.HELMET, 0, 200, 0, 1, 0, 0, 0, 1, 0, 0, 0, CACTUS, CACTUS, CACTUS, CACTUS, null, CACTUS, null, null, null, false, 0, 0, 0, 0, 0, 0, 0, 5, 0, 0, 10, 0, 0, 0);
		addArmor(armor, "Cactus Shirt", ArmorType.LEATHER, ArmorPlace.CHESTPLATE, 0, 200, 0, 3, 0, 0, 0, 3, 0, 0, 0, CACTUS, null, CACTUS, CACTUS, CACTUS, CACTUS, CACTUS, CACTUS, CACTUS, false, 0, 0, 0, 0, 0, 0, 0, 5, 0, 0, 10, 0, 0, 0);
		addArmor(armor, "Cactus Pants", ArmorType.LEATHER, ArmorPlace.LEGGINGS, 0, 200, 0, 2, 0, 0, 0, 2, 0, 0, 0, CACTUS, CACTUS, CACTUS, CACTUS, null, CACTUS, CACTUS, null, CACTUS, false, 0, 0, 0, 0, 0, 0, 0, 5, 0, 0, 10, 0, 0, 0);
		addArmor(armor, "Cactus Boots", ArmorType.LEATHER, ArmorPlace.BOOTS, 0, 200, 0, 1, 0, 0, 0, 1, 0, 0, 0, null, null, null, CACTUS, null, CACTUS, CACTUS, null, CACTUS, false, 0, 0, 0, 0, 0, 0, 0, 5, 0, 0, 10, 0, 0, 0);
		addArmor(armor, "Blaze Hat", ArmorType.LEATHER, ArmorPlace.HELMET, 200, 150, 0, 1, 0, 1, 0, 0, 0, 0, 0, BLAZE_POWDER, BLAZE_POWDER, BLAZE_POWDER, BLAZE_POWDER, null, BLAZE_POWDER, null, null, null, false, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0);
		addArmor(armor, "Blaze Shirt", ArmorType.LEATHER, ArmorPlace.CHESTPLATE, 200, 150, 0, 3, 0, 3, 0, 0, 0, 0, 0, BLAZE_POWDER, null, BLAZE_POWDER, BLAZE_POWDER, BLAZE_POWDER, BLAZE_POWDER, BLAZE_POWDER, BLAZE_POWDER, BLAZE_POWDER, false, 0, 2, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0);
		addArmor(armor, "Blaze Pants", ArmorType.LEATHER, ArmorPlace.LEGGINGS, 200, 150, 0, 2, 0, 2, 0, 0, 0, 0, 0, BLAZE_POWDER, BLAZE_POWDER, BLAZE_POWDER, BLAZE_POWDER, null, BLAZE_POWDER, BLAZE_POWDER, null, BLAZE_POWDER, false, 0, 2, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0);
		addArmor(armor, "Blaze Boots", ArmorType.LEATHER, ArmorPlace.BOOTS, 200, 150, 0, 1, 0, 1, 0, 0, 0, 0, 0, null, null, null, BLAZE_POWDER, null, BLAZE_POWDER, BLAZE_POWDER, null, BLAZE_POWDER, false, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0);
		config.options().copyDefaults(true);
		CustomArmor.getInstance().saveConfig();
	}
	
	public void exampleDurability(){
		clearConfig();
		config.set(HAS_BEEN_CREATED, true);
		config.set(USE_CUSTOM_ANVIL, true);
		config.set(DISABLE_VANILLA_ANVIL, false);
		config.set(USE_CUSTOM_ENCHANTING, false);
		config.set(DISABLE_VANILLA_ENCHANTING, false);
		addVanillaArmor();
		addUpgrade(getSection(UPGRADES), new ArmorUpgradeCourse(new String[]{"Diamond Helmet", "Diamond Chestplate", "Diamond Leggings", "Diamond Boots",
				"Iron Helmet", "Iron Chestplate", "Iron Leggings", "Iron Boots", "Golden Helmet", "Golden Chestplate", "Golden Leggings",
				"Golden Boots", "Leather Cap", "Leather Tunic", "Leather Pants", "Leather Boots"}, "Durability", OBSIDIAN,
				new SingleArmorUpgrade(false, false, false, false, false, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, new Stack(OBSIDIAN, 1)),
				new SingleArmorUpgrade(false, false, false, false, false, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 2, new Stack(OBSIDIAN, 10)),
				new SingleArmorUpgrade(false, false, false, false, false, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 3, new Stack(OBSIDIAN, 50)),
				new SingleArmorUpgrade(false, false, false, false, false, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 4, new Stack(OBSIDIAN, 250)),
				new SingleArmorUpgrade(true, false, false, false, false, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, -10, new Stack(OBSIDIAN, 1000))));
		config.options().copyDefaults(true);
		CustomArmor.getInstance().saveConfig();
	}
	
	public void exampleBlazeUpgrades(){
		clearConfig();
		config.set(HAS_BEEN_CREATED, true);
		config.set(USE_CUSTOM_ANVIL, true);
		config.set(DISABLE_VANILLA_ANVIL, false);
		config.set(USE_CUSTOM_ENCHANTING, true);
		config.set(DISABLE_VANILLA_ENCHANTING, false);
		ConfigurationSection armor = addVanillaArmor();
		addArmor(armor, "Blaze Hat", ArmorType.LEATHER, ArmorPlace.HELMET, 200, 150, 0, 1, 0, 0, 0, 0, 0, 0, 0, BLAZE_POWDER, BLAZE_POWDER, BLAZE_POWDER, BLAZE_POWDER, null, BLAZE_POWDER, null, null, null, false, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0);
		addArmor(armor, "Blaze Shirt", ArmorType.LEATHER, ArmorPlace.CHESTPLATE, 200, 150, 0, 3, 0, 0, 0, 0, 0, 0, 0, BLAZE_POWDER, null, BLAZE_POWDER, BLAZE_POWDER, BLAZE_POWDER, BLAZE_POWDER, BLAZE_POWDER, BLAZE_POWDER, BLAZE_POWDER, false, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0);
		addArmor(armor, "Blaze Pants", ArmorType.LEATHER, ArmorPlace.LEGGINGS, 200, 150, 0, 2, 0, 0, 0, 0, 0, 0, 0, BLAZE_POWDER, BLAZE_POWDER, BLAZE_POWDER, BLAZE_POWDER, null, BLAZE_POWDER, BLAZE_POWDER, null, BLAZE_POWDER, false, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0);
		addArmor(armor, "Blaze Boots", ArmorType.LEATHER, ArmorPlace.BOOTS, 200, 150, 0, 1, 0, 0, 0, 0, 0, 0, 0, null, null, null, BLAZE_POWDER, null, BLAZE_POWDER, BLAZE_POWDER, null, BLAZE_POWDER, false, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0);
		addUpgrade(getSection(UPGRADES), new ArmorUpgradeCourse(new String[]{"Blaze Hat", "Blaze Shirt", "Blaze Pants", "Blaze Boots"}, "Fire Protection", MAGMA_CREAM,
				new SingleArmorUpgrade(false, false, false, false, false, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, new Stack(MAGMA_CREAM, 1)),
				new SingleArmorUpgrade(false, false, false, false, false, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, new Stack(MAGMA_CREAM, 1)),
				new SingleArmorUpgrade(false, false, false, false, false, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, new Stack(MAGMA_CREAM, 1)),
				new SingleArmorUpgrade(false, false, false, false, false, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, new Stack(MAGMA_CREAM, 1)),
				new SingleArmorUpgrade(false, false, false, false, false, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, new Stack(MAGMA_CREAM, 1))));
		addEnchantment(getSection(ENCHANTMENTS), new ArmorEnchantmentCourse(new String[]{"Blaze Hat", "Blaze Shirt", "Blaze Pants", "Blaze Boots"}, "Strength", BLAZE_POWDER,
				new SingleArmorEnchantment(false, false, false, false, false, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, new Stack[]{new Stack(BLAZE_POWDER, 1)}, 1, 1),
				new SingleArmorEnchantment(false, false, false, false, false, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, new Stack[]{new Stack(BLAZE_POWDER, 2)}, 15, 2),
				new SingleArmorEnchantment(false, false, false, false, false, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, new Stack[]{new Stack(BLAZE_POWDER, 5)}, 30, 3)));
		config.options().copyDefaults(true);
		CustomArmor.getInstance().saveConfig();
	}
	
	void addUpgrade(ConfigurationSection section, ArmorUpgradeCourse course){
		ConfigurationSection upgradeSection = getSection(section, course.getName());
		upgradeSection.addDefault(UPGRADE_ICON, m(course.getIcon()));
		upgradeSection.addDefault(AVAILABLE_ARMOR, Lists.newArrayList(course.getAllowedArmor()));
		for(int level = 1; level <= course.getMaxLevel(); level++){
			SingleArmorUpgrade single = course.getUpgrade(level);
			ConfigurationSection singleSection = getSection(upgradeSection, "level " + level);
			singleSection.addDefault(UNBREAKABLE, single.addUnbreakable());
			singleSection.addDefault(e(WATER_WORKER), single.addAquaAffinity());
			singleSection.addDefault(e(MENDING), single.addMending());
			singleSection.addDefault(e(VANISHING_CURSE), single.addVanishCurse());
			singleSection.addDefault(e(BINDING_CURSE), single.addBindingCurse());
			singleSection.addDefault(ARMOR, single.getExtraArmor());
			singleSection.addDefault(TOUGHNESS, single.getExtraToughness());
			singleSection.addDefault(ATTACK_DAMAGE, single.getExtraAttackDamage());
			singleSection.addDefault(ATTACK_SPEED, single.getExtraAttackSpeed());
			singleSection.addDefault(MAX_HEALTH, single.getExtraHealth());
			singleSection.addDefault(MOVEMENT_SPEED, single.getExtraSpeed());
			singleSection.addDefault(KNOCKBACK_RESISTANCE, single.getExtraKnockbackResistance());
			singleSection.addDefault(LUCK, single.getExtraLuck());
			singleSection.addDefault(e(PROTECTION_ENVIRONMENTAL), single.getExtraProtection());
			singleSection.addDefault(e(PROTECTION_FIRE), single.getExtraFireProtection());
			singleSection.addDefault(e(PROTECTION_FALL), single.getExtraFeatherFalling());
			singleSection.addDefault(e(PROTECTION_EXPLOSIONS), single.getExtraBlastProtection());
			singleSection.addDefault(e(PROTECTION_PROJECTILE), single.getExtraProjectileProtection());
			singleSection.addDefault(e(OXYGEN), single.getExtraRespiration());
			singleSection.addDefault(e(THORNS), single.getExtraThorns());
			singleSection.addDefault(e(DEPTH_STRIDER), single.getExtraDepthsStrider());
			singleSection.addDefault(e(FROST_WALKER), single.getExtraFrostWalker());
			singleSection.addDefault(e(DURABILITY), single.getExtraUnbreaking());
			ConfigurationSection priceSection = getSection(singleSection, PRICE);
			Stack[] price = single.getPrice();
			for(Stack stack : price)
				priceSection.addDefault(m(stack.getItem()), stack.getAmount());
		}
	}
	
	void addEnchantment(ConfigurationSection section, ArmorEnchantmentCourse course){
		ConfigurationSection upgradeSection = getSection(section, course.getName());
		upgradeSection.addDefault(UPGRADE_ICON, m(course.getIcon()));
		upgradeSection.addDefault(AVAILABLE_ARMOR, Lists.newArrayList(course.getAllowedArmor()));
		for(int level = 1; level <= course.getMaxLevel(); level++){
			SingleArmorEnchantment single = course.getEnchantment(level);
			ConfigurationSection singleSection = getSection(upgradeSection, "level " + level);
			singleSection.addDefault(UNBREAKABLE, single.addUnbreakable());
			singleSection.addDefault(e(WATER_WORKER), single.addAquaAffinity());
			singleSection.addDefault(e(MENDING), single.addMending());
			singleSection.addDefault(e(VANISHING_CURSE), single.addVanishCurse());
			singleSection.addDefault(e(BINDING_CURSE), single.addBindingCurse());
			singleSection.addDefault(ARMOR, single.getExtraArmor());
			singleSection.addDefault(TOUGHNESS, single.getExtraToughness());
			singleSection.addDefault(ATTACK_DAMAGE, single.getExtraAttackDamage());
			singleSection.addDefault(ATTACK_SPEED, single.getExtraAttackSpeed());
			singleSection.addDefault(MAX_HEALTH, single.getExtraHealth());
			singleSection.addDefault(MOVEMENT_SPEED, single.getExtraSpeed());
			singleSection.addDefault(KNOCKBACK_RESISTANCE, single.getExtraKnockbackResistance());
			singleSection.addDefault(LUCK, single.getExtraLuck());
			singleSection.addDefault(e(PROTECTION_ENVIRONMENTAL), single.getExtraProtection());
			singleSection.addDefault(e(PROTECTION_FIRE), single.getExtraFireProtection());
			singleSection.addDefault(e(PROTECTION_FALL), single.getExtraFeatherFalling());
			singleSection.addDefault(e(PROTECTION_EXPLOSIONS), single.getExtraBlastProtection());
			singleSection.addDefault(e(PROTECTION_PROJECTILE), single.getExtraProjectileProtection());
			singleSection.addDefault(e(OXYGEN), single.getExtraRespiration());
			singleSection.addDefault(e(THORNS), single.getExtraThorns());
			singleSection.addDefault(e(DEPTH_STRIDER), single.getExtraDepthsStrider());
			singleSection.addDefault(e(FROST_WALKER), single.getExtraFrostWalker());
			singleSection.addDefault(e(DURABILITY), single.getExtraUnbreaking());
			ConfigurationSection priceSection = getSection(singleSection, PRICE);
			Stack[] price = single.getPrice();
			for(Stack stack : price)
				priceSection.addDefault(m(stack.getItem()), stack.getAmount());
			singleSection.addDefault(REQUIRED_LEVEL, single.getRequiredLevel());
			singleSection.addDefault(LEVEL_COST, single.getLevelCost());
		}
	}
	
	private void addHelmet(ConfigurationSection section, String name, ArmorType type, double armor, double toughness, Material m){
		addArmor(section, name, type, ArmorPlace.HELMET, 160, 101, 64, armor, toughness, m, m, m, m, null, m, null, null, null);
	}
	
	private void addChestplate(ConfigurationSection section, String name, ArmorType type, double armor, double toughness, Material m){
		addArmor(section, name, type, ArmorPlace.CHESTPLATE, 160, 101, 64, armor, toughness, m, null, m, m, m, m, m, m, m);
	}
	
	private void addLeggings(ConfigurationSection section, String name, ArmorType type, double armor, double toughness, Material m){
		addArmor(section, name, type, ArmorPlace.LEGGINGS, 160, 101, 64, armor, toughness, m, m, m, m, null, m, m, null, m);
	}
	
	private void addBoots(ConfigurationSection section, String name, ArmorType type, double armor, double toughness, Material m){
		addArmor(section, name, type, ArmorPlace.BOOTS, 160, 101, 64, armor, toughness, null, null, null, m, null, m, m, null, m);
	}
	
	private void addArmor(ConfigurationSection section, String name, ArmorType type, ArmorPlace place, int leatherRed, int leatherGreen, int leatherBlue, double armorValue, double armorToughness, Material m1, Material m2, Material m3, Material m4, Material m5, Material m6, Material m7, Material m8, Material m9){
		addArmor(section, name, type, place, leatherRed, leatherGreen, leatherBlue, armorValue, armorToughness, 0, 0, 0, 0, 0, 0, m1, m2, m3, m4, m5, m6, m7, m8, m9, false, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0);
	}
	
	private void addArmor(ConfigurationSection section, String name, ArmorType type, ArmorPlace place, int leatherRed, int leatherGreen, int leatherBlue, double armorValue, double toughness, double attackDamage, double attackSpeed, double maxHealth, double movementSpeed, double knockbackResistance, double luck, Material cm1, Material cm2, Material cm3, Material cm4, Material cm5, Material cm6, Material cm7, Material cm8, Material cm9, boolean unbreakable, int prot, int fireProt, int feather, int blast, int projectile, int respiration, int aqua, int thorns, int depth, int frost, int unbreaking, int mending, int vanish, int bind){
		ConfigurationSection armor = getSection(section, name);
		setArmorType(armor, type);
		setRecipe(armor, cm1, cm2, cm3, cm4, cm5, cm6, cm7, cm8, cm9);
		setArmorPlace(armor, place);
		armor.set(LEATHER_RED, leatherRed);
		armor.set(LEATHER_GREEN, leatherGreen);
		armor.set(LEATHER_BLUE, leatherBlue);
		armor.set(ARMOR, armorValue);
		armor.set(TOUGHNESS, toughness);
		armor.set(ATTACK_DAMAGE, attackDamage);
		armor.set(ATTACK_SPEED, attackSpeed);
		armor.set(MAX_HEALTH, maxHealth);
		armor.set(MOVEMENT_SPEED, movementSpeed);
		armor.set(KNOCKBACK_RESISTANCE, knockbackResistance);
		armor.set(LUCK, luck);
		armor.set(UNBREAKABLE, unbreakable);
		armor.set(e(PROTECTION_ENVIRONMENTAL), prot);
		armor.set(e(PROTECTION_FIRE), fireProt);
		armor.set(e(PROTECTION_FALL), feather);
		armor.set(e(PROTECTION_EXPLOSIONS), blast);
		armor.set(e(PROTECTION_PROJECTILE), projectile);
		armor.set(e(OXYGEN), respiration);
		armor.set(e(WATER_WORKER), aqua);
		armor.set(e(THORNS), thorns);
		armor.set(e(DEPTH_STRIDER), depth);
		armor.set(e(FROST_WALKER), frost);
		armor.set(e(DURABILITY), unbreaking);
		armor.set(e(MENDING), mending);
		armor.set(e(VANISHING_CURSE), vanish);
		armor.set(e(BINDING_CURSE), bind);
	}
	
	private ConfigurationSection getSection(String path){
		ConfigurationSection section = config.getConfigurationSection(path);
		if(section == null)
			section = config.createSection(path);
		return section;
	}
	
	private ConfigurationSection getSection(ConfigurationSection parent, String path){
		ConfigurationSection section = parent.getConfigurationSection(path);
		if(section == null)
			section = parent.createSection(path);
		return section;
	}
	
	private void setArmorType(ConfigurationSection section, ArmorType type){
		section.set(ARMOR_TYPE, type.toString());
	}
	
	private void setArmorPlace(ConfigurationSection section, ArmorPlace place){
		section.set(ARMOR_SLOT, place.toString());
	}
	
	private void setRecipe(ConfigurationSection section, Material cm1, Material cm2, Material cm3, Material cm4, Material cm5, Material cm6, Material cm7, Material cm8, Material cm9){
		section.set(RECIPE, Lists.newArrayList(m(cm1), m(cm2), m(cm3), m(cm4), m(cm5), m(cm6), m(cm7), m(cm8), m(cm9)));
	}
	
	private String m(Material material){
		return material != null ? material.name().toLowerCase() : "empty";
	}
	
	private String e(Enchantment enchantment){
		return enchantment.getName().toLowerCase();
	}
	
	void refreshConfig(){
		CustomArmor.getInstance().reloadConfig();
		config = CustomArmor.getInstance().getConfig();
	}
	
	public void updateArmor(){
		armorList.clear();
		upgrades.clear();
		enchantments.clear();
		upgradeMap.clear();
		enchantMap.clear();
		useCustomAnvil = config.getBoolean(USE_CUSTOM_ANVIL);
		disableVanillaAnvil = config.getBoolean(DISABLE_VANILLA_ANVIL);
		useCustomEnchanting = config.getBoolean(USE_CUSTOM_ENCHANTING);
		disableVanillaEnchanting = config.getBoolean(DISABLE_VANILLA_ENCHANTING);
		ConfigurationSection section = getSection(ARMOR_TYPES);
		Map<String,Object> map = section.getValues(false);
		Set<Entry<String,Object>> set = map.entrySet();
		for(Entry<String,Object> entry : set){
			if(entry.getValue() instanceof MemorySection){
				MemorySection ms = (MemorySection) entry.getValue();
				ArmorType type = ArmorType.fromString(ms.getString(ARMOR_TYPE));
				ArmorPlace place = ArmorPlace.fromString(ms.getString(ARMOR_SLOT));
				List<String> materials = ms.getStringList(RECIPE);
				Material[] recipe = new Material[materials.size()];
				for(int i = 0; i < 9; i++){
					String s = materials.get(i);
					try {
						recipe[i] = Material.valueOf(s.toUpperCase());
					} catch(Throwable t){
						recipe[i] = Material.AIR;
					}
				}
				double armorValue = ms.getDouble(ARMOR);
				double armorThoughness = ms.getDouble(TOUGHNESS);
				double attackDamage = ms.getDouble(ATTACK_DAMAGE);
				double attackSpeed = ms.getDouble(ATTACK_SPEED);
				double maxHealth = ms.getDouble(MAX_HEALTH);
				double movementSpeed = ms.getDouble(MOVEMENT_SPEED);
				double knockbackResistance = ms.getDouble(KNOCKBACK_RESISTANCE);
				double luck = ms.getDouble(LUCK);
				boolean unbreakable = ms.getBoolean(UNBREAKABLE);
				int prot = ms.getInt(e(PROTECTION_ENVIRONMENTAL));
				int fireProt = ms.getInt(e(PROTECTION_FIRE));
				int feather = ms.getInt(e(PROTECTION_FALL));
				int blast = ms.getInt(e(PROTECTION_EXPLOSIONS));
				int proj = ms.getInt(e(PROTECTION_PROJECTILE));
				int resp = ms.getInt(e(OXYGEN));
				int aqua = ms.getInt(e(WATER_WORKER));
				int thorns = ms.getInt(e(THORNS));
				int depth = ms.getInt(e(DEPTH_STRIDER));
				int frost = ms.getInt(e(FROST_WALKER));
				int unbreaking = ms.getInt(e(DURABILITY));
				int mending = ms.getInt(e(MENDING));
				int vanish = ms.getInt(e(VANISHING_CURSE));
				int bind = ms.getInt(e(BINDING_CURSE));
				int leatherRed = ms.getInt(LEATHER_RED);
				int leatherGreen = ms.getInt(LEATHER_GREEN);
				int leatherBlue = ms.getInt(LEATHER_BLUE);
				ArmorPiece piece = new ArmorPiece(entry.getKey(), type, place, leatherRed, leatherGreen, leatherBlue, armorValue, armorThoughness, attackDamage, attackSpeed, maxHealth, movementSpeed, knockbackResistance, luck, unbreakable, prot, fireProt, feather, blast, proj, resp, aqua, thorns, depth, frost, unbreaking, mending, vanish, bind);
				addArmorPiece(piece);
				ArmorRecipes.addCustomRecipe(recipe, piece.createItemStack());
			}
		}
		section = getSection(UPGRADES);
		map = section.getValues(false);
		set = map.entrySet();
		for(Entry<String,Object> entry : set){
			if(entry.getValue() instanceof MemorySection){
				MemorySection ms = (MemorySection) entry.getValue();
				String courseName = entry.getKey();
				List<String> allowedPieces = ms.getStringList(AVAILABLE_ARMOR);
				List<SingleArmorUpgrade> upgrades = new ArrayList<SingleArmorUpgrade>();
				Material icon = Material.valueOf(ms.getString(UPGRADE_ICON).toUpperCase());
				int level = 1;
				while(true){
					ConfigurationSection levelSection = ms.getConfigurationSection("level " + level);
					if(levelSection == null)
						break;
					double armorValue = levelSection.getDouble(ARMOR);
					double armorToughness = levelSection.getDouble(TOUGHNESS);
					double attackDamage = levelSection.getDouble(ATTACK_DAMAGE);
					double attackSpeed = levelSection.getDouble(ATTACK_SPEED);
					double maxHealth = levelSection.getDouble(MAX_HEALTH);
					double movementSpeed = levelSection.getDouble(MOVEMENT_SPEED);
					double knockbackResistance = levelSection.getDouble(KNOCKBACK_RESISTANCE);
					double luck = levelSection.getDouble(LUCK);
					boolean unbreakable = levelSection.getBoolean(UNBREAKABLE);
					int prot = levelSection.getInt(e(PROTECTION_ENVIRONMENTAL));
					int fireProt = levelSection.getInt(e(PROTECTION_FIRE));
					int feather = levelSection.getInt(e(PROTECTION_FALL));
					int blast = levelSection.getInt(e(PROTECTION_EXPLOSIONS));
					int proj = levelSection.getInt(e(PROTECTION_PROJECTILE));
					int resp = levelSection.getInt(e(OXYGEN));
					boolean aqua = levelSection.getBoolean(e(WATER_WORKER));
					int thorns = levelSection.getInt(e(THORNS));
					int depth = levelSection.getInt(e(DEPTH_STRIDER));
					int frost = levelSection.getInt(e(FROST_WALKER));
					int unbreaking = levelSection.getInt(e(DURABILITY));
					boolean mending = levelSection.getBoolean(e(MENDING));
					boolean vanish = levelSection.getBoolean(e(VANISHING_CURSE));
					boolean bind = levelSection.getBoolean(e(BINDING_CURSE));
					ConfigurationSection priceSection = getSection(levelSection, PRICE);
					Set<Entry<String,Object>> priceEntrySet = priceSection.getValues(false).entrySet();
					Stack[] price = new Stack[priceEntrySet.size()];
					int i = 0;
					for(Entry<String,Object> priceEntry : priceEntrySet){
						Material item = Material.valueOf(priceEntry.getKey().toUpperCase());
						int amount = (Integer) priceEntry.getValue();
						price[i] = new Stack(item, amount);
						i++;
					}
					upgrades.add(new SingleArmorUpgrade(unbreakable, aqua, mending, vanish, bind, armorValue, armorToughness, attackDamage, attackSpeed, maxHealth, movementSpeed, knockbackResistance, luck, prot, fireProt, feather, blast, proj, resp, thorns, depth, frost, unbreaking, price));
					level++;
				}
				addUpgradeCourse(new ArmorUpgradeCourse(allowedPieces.toArray(new String[allowedPieces.size()]), courseName, icon, upgrades.toArray(new SingleArmorUpgrade[upgrades.size()])));
			}
		}
		section = getSection(ENCHANTMENTS);
		map = section.getValues(false);
		set = map.entrySet();
		for(Entry<String,Object> entry : set){
			if(entry.getValue() instanceof MemorySection){
				MemorySection ms = (MemorySection) entry.getValue();
				String courseName = entry.getKey();
				List<String> allowedPieces = ms.getStringList(AVAILABLE_ARMOR);
				List<SingleArmorEnchantment> enchants = new ArrayList<SingleArmorEnchantment>();
				Material icon = Material.valueOf(ms.getString(UPGRADE_ICON).toUpperCase());
				int level = 1;
				while(true){
					ConfigurationSection levelSection = ms.getConfigurationSection("level " + level);
					if(levelSection == null)
						break;
					double armorValue = levelSection.getDouble(ARMOR);
					double armorToughness = levelSection.getDouble(TOUGHNESS);
					double attackDamage = levelSection.getDouble(ATTACK_DAMAGE);
					double attackSpeed = levelSection.getDouble(ATTACK_SPEED);
					double maxHealth = levelSection.getDouble(MAX_HEALTH);
					double movementSpeed = levelSection.getDouble(MOVEMENT_SPEED);
					double knockbackResistance = levelSection.getDouble(KNOCKBACK_RESISTANCE);
					double luck = levelSection.getDouble(LUCK);
					boolean unbreakable = levelSection.getBoolean(UNBREAKABLE);
					int prot = levelSection.getInt(e(PROTECTION_ENVIRONMENTAL));
					int fireProt = levelSection.getInt(e(PROTECTION_FIRE));
					int feather = levelSection.getInt(e(PROTECTION_FALL));
					int blast = levelSection.getInt(e(PROTECTION_EXPLOSIONS));
					int proj = levelSection.getInt(e(PROTECTION_PROJECTILE));
					int resp = levelSection.getInt(e(OXYGEN));
					boolean aqua = levelSection.getBoolean(e(WATER_WORKER));
					int thorns = levelSection.getInt(e(THORNS));
					int depth = levelSection.getInt(e(DEPTH_STRIDER));
					int frost = levelSection.getInt(e(FROST_WALKER));
					int unbreaking = levelSection.getInt(e(DURABILITY));
					boolean mending = levelSection.getBoolean(e(MENDING));
					boolean vanish = levelSection.getBoolean(e(VANISHING_CURSE));
					boolean bind = levelSection.getBoolean(e(BINDING_CURSE));
					ConfigurationSection priceSection = getSection(levelSection, PRICE);
					Set<Entry<String,Object>> priceEntrySet = priceSection.getValues(false).entrySet();
					Stack[] price = new Stack[priceEntrySet.size()];
					int i = 0;
					for(Entry<String,Object> priceEntry : priceEntrySet){
						Material item = Material.valueOf(priceEntry.getKey().toUpperCase());
						int amount = (Integer) priceEntry.getValue();
						price[i] = new Stack(item, amount);
						i++;
					}
					int requiredLevel = levelSection.getInt(REQUIRED_LEVEL);
					int levelCost = levelSection.getInt(LEVEL_COST);
					enchants.add(new SingleArmorEnchantment(unbreakable, aqua, mending, vanish, bind, armorValue, armorToughness, attackDamage, attackSpeed, maxHealth, movementSpeed, knockbackResistance, luck, prot, fireProt, feather, blast, proj, resp, thorns, depth, frost, unbreaking, price, requiredLevel, levelCost));
					level++;
				}
				addEnchantmentCourse(new ArmorEnchantmentCourse(allowedPieces.toArray(new String[allowedPieces.size()]), courseName, icon, enchants.toArray(new SingleArmorEnchantment[enchants.size()])));
			}
		}
		/*
		addUpgradeCourse(new ArmorUpgradeCourse(new String[]{"Blaze Cap"}, "Durability", Material.OBSIDIAN, new SingleArmorUpgrade(false, false, false, false, false, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1),
				new SingleArmorUpgrade(false, false, false, false, false, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 2),
				new SingleArmorUpgrade(false, false, false, false, false, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 3),
				new SingleArmorUpgrade(false, false, false, false, false, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 4),
				new SingleArmorUpgrade(true, false, false, false, false, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, -10)));
				*/
	}
	
	public List<ArmorPiece> getArmor(){
		return armorList;
	}
	
	public boolean useCustomAnvil(){
		return useCustomAnvil;
	}
	
	public boolean disableVanillaAnvil(){
		return disableVanillaAnvil;
	}
	
	public boolean isRegistered(ArmorPiece piece){
		if(piece == null)
			return false;
		return armorList.contains(piece.getOriginal());
	}
	
	public boolean useCustomEnchanting(){
		return useCustomEnchanting;
	}
	
	public boolean disableVanillaEnchanting(){
		return disableVanillaEnchanting;
	}
	
	public List<ArmorUpgradeCourse> getUpgrades(ArmorPiece piece){
		return upgradeMap.get(piece);
	}
	
	public List<ArmorEnchantmentCourse> getEnchantments(ArmorPiece piece){
		return enchantMap.get(piece);
	}
	
	public ArmorUpgradeCourse upgradeFromName(String name){
		for(ArmorUpgradeCourse course : upgrades){
			if(course.getName().equals(name))
				return course;
		}
		return null;
	}
	
	public ArmorEnchantmentCourse enchantmentFromName(String name){
		for(ArmorEnchantmentCourse course : enchantments){
			if(course.getName().equals(name))
				return course;
		}
		return null;
	}
	
	public ArmorPiece armorFromName(String name){
		for(ArmorPiece piece : armorList){
			if(piece.getName().equals(name))
				return piece;
		}
		return null;
	}
	
	private void addArmorPiece(ArmorPiece piece){
		armorList.add(piece);
		upgradeMap.put(piece, new ArrayList<ArmorUpgradeCourse>());
		enchantMap.put(piece, new ArrayList<ArmorEnchantmentCourse>());
	}
	
	private void addUpgradeCourse(ArmorUpgradeCourse course){
		upgrades.add(course);
		String[] list = course.getAllowedArmor();
		for(String piece : list){
			ArmorPiece ap = armorFromName(piece);
			if(ap != null)
				upgradeMap.get(ap).add(course);
			else
				Bukkit.getLogger().log(Level.WARNING, "Unregistered armor type '" + piece + "' is on the list for upgrade '" + course + "'. Make sure you spelled the armor type right!");
		}
	}
	
	private void addEnchantmentCourse(ArmorEnchantmentCourse course){
		enchantments.add(course);
		String[] list = course.getAllowedArmor();
		for(String piece : list){
			ArmorPiece ap = armorFromName(piece);
			if(ap != null)
				enchantMap.get(ap).add(course);
			else
				Bukkit.getLogger().log(Level.WARNING, "Unregistered armor type '" + piece + "' is on the list for enchantment '" + course + "'. Make sure you spelled the armor type right!");
		}
	}
}
