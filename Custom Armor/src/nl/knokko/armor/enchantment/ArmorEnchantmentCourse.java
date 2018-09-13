package nl.knokko.armor.enchantment;

import java.util.ArrayList;
import java.util.List;

import nl.knokko.armor.ArmorPiece;

import org.bukkit.ChatColor;
import org.bukkit.Material;

public class ArmorEnchantmentCourse {

	private final String[] allowedArmor;
	private final SingleArmorEnchantment[] enchantments;
	
	private final String name;
	private final Material icon;

	public ArmorEnchantmentCourse(String[] allowedArmor, String name, Material icon, SingleArmorEnchantment... upgrades) {
		this.allowedArmor = allowedArmor;
		this.enchantments = upgrades;
		this.name = name;
		this.icon = icon;
		int level = 0;
		for(SingleArmorEnchantment upgrade : upgrades){
			level++;
			upgrade.setCourse(this);
			upgrade.setUpgradeLevel(level);
		}
	}
	
	public int getLevelCount(){
		return enchantments.length;
	}
	
	public int getMaxLevel(){
		return getLevelCount();
	}
	
	public String getName(){
		return name;
	}
	
	public Material getIcon(){
		return icon;
	}
	
	public String[] getAllowedArmor(){
		return allowedArmor;
	}
	
	public boolean canApplyTo(ArmorPiece armor){
		for(String name : allowedArmor)
			if(name.equals(armor))
				return true;
		return false;
	}
	
	public SingleArmorEnchantment getEnchantment(int level){
		return enchantments[level - 1];
	}
	
	public List<String> getDescription(int level){
		List<String> list = new ArrayList<String>();
		SingleArmorEnchantment enchantment = getEnchantment(level);
		addDouble(list, enchantment.getExtraArmor(), "armor");
		addDouble(list, enchantment.getExtraToughness(), "armor toughness");
		addDouble(list, enchantment.getExtraAttackDamage(), "attack damage");
		addDouble(list, enchantment.getExtraAttackSpeed(), "attack speed");
		addDouble(list, enchantment.getExtraHealth(), "max health");
		addDouble(list, enchantment.getExtraSpeed(), "speed");
		addDouble(list, enchantment.getExtraKnockbackResistance(), "knockback resistance");
		addDouble(list, enchantment.getExtraLuck(), "luck");
		addInt(list, enchantment.getExtraProtection(), "protection");
		addInt(list, enchantment.getExtraFireProtection(), "fire protection");
		addInt(list, enchantment.getExtraFeatherFalling(), "feather falling");
		addInt(list, enchantment.getExtraBlastProtection(), "blast protection");
		addInt(list, enchantment.getExtraProjectileProtection(), "projectile protection");
		addInt(list, enchantment.getExtraRespiration(), "respiration");
		addInt(list, enchantment.getExtraThorns(), "thorns");
		addInt(list, enchantment.getExtraDepthsStrider(), "depth strider");
		addInt(list, enchantment.getExtraFrostWalker(), "frost walker");
		addInt(list, enchantment.getExtraUnbreaking(), "unbreaking");
		addBoolean(list, enchantment.addUnbreakable(), "becomes unbreakable");
		addBoolean(list, enchantment.addAquaAffinity(), "adds aqua affinity");
		addBoolean(list, enchantment.addMending(), "adds mending");
		addBoolean(list, enchantment.addVanishCurse(), "adds vanish curse");
		addBoolean(list, enchantment.addBindingCurse(), "adds binding curse");
		return list;
	}
	
	private void addDouble(List<String> list, double value, String name){
		if(value > 0)
			list.add(ChatColor.GREEN + "+ " + value + " " + name);
		else if(value < 0)
			list.add(ChatColor.RED + "- " + -value + " " + name);
	}
	
	private void addInt(List<String> list, int value, String name){
		if(value > 0)
			list.add(ChatColor.GREEN + "+ " + value + " " + name);
		else if(value < 0)
			list.add(ChatColor.RED + "- " + -value + " " + name);
	}
	
	private void addBoolean(List<String> list, boolean value, String name){
		if(value)
			list.add(ChatColor.GREEN + name);
	}
}
