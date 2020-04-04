package nl.knokko.armor.upgrade;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Material;

import nl.knokko.armor.ArmorPiece;

public class ArmorUpgradeCourse {
	
	private final String[] allowedArmor;
	private final SingleArmorUpgrade[] upgrades;
	
	private final String name;
	private final Material icon;

	public ArmorUpgradeCourse(String[] allowedArmor, String name, Material icon, SingleArmorUpgrade... upgrades) {
		this.allowedArmor = allowedArmor;
		this.upgrades = upgrades;
		this.name = name;
		this.icon = icon;
		int level = 0;
		for(SingleArmorUpgrade upgrade : upgrades){
			level++;
			upgrade.setCourse(this);
			upgrade.setUpgradeLevel(level);
		}
	}
	
	public int getLevelCount(){
		return upgrades.length;
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
			if(name.equals(armor.getName()))
				return true;
		return false;
	}
	
	public SingleArmorUpgrade getUpgrade(int level){
		return upgrades[level - 1];
	}
	
	public List<String> getDescription(int level){
		List<String> list = new ArrayList<String>();
		SingleArmorUpgrade upgrade = getUpgrade(level);
		addDouble(list, upgrade.getExtraArmor(), "armor");
		addDouble(list, upgrade.getExtraToughness(), "armor toughness");
		addDouble(list, upgrade.getExtraAttackDamage(), "attack damage");
		addDouble(list, upgrade.getExtraAttackSpeed(), "attack speed");
		addDouble(list, upgrade.getExtraHealth(), "max health");
		addDouble(list, upgrade.getExtraSpeed(), "speed");
		addDouble(list, upgrade.getExtraKnockbackResistance(), "knockback resistance");
		addDouble(list, upgrade.getExtraLuck(), "luck");
		addInt(list, upgrade.getExtraProtection(), "protection");
		addInt(list, upgrade.getExtraFireProtection(), "fire protection");
		addInt(list, upgrade.getExtraFeatherFalling(), "feather falling");
		addInt(list, upgrade.getExtraBlastProtection(), "blast protection");
		addInt(list, upgrade.getExtraProjectileProtection(), "projectile protection");
		addInt(list, upgrade.getExtraRespiration(), "respiration");
		addInt(list, upgrade.getExtraThorns(), "thorns");
		addInt(list, upgrade.getExtraDepthsStrider(), "depth strider");
		addInt(list, upgrade.getExtraFrostWalker(), "frost walker");
		addInt(list, upgrade.getExtraUnbreaking(), "unbreaking");
		addBoolean(list, upgrade.addUnbreakable(), "becomes unbreakable");
		addBoolean(list, upgrade.addAquaAffinity(), "adds aqua affinity");
		addBoolean(list, upgrade.addMending(), "adds mending");
		addBoolean(list, upgrade.addVanishCurse(), "adds vanish curse");
		addBoolean(list, upgrade.addBindingCurse(), "adds binding curse");
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
