package nl.knokko.armor.upgrade;

import org.bukkit.Material;

public class SingleArmorUpgrade {
	
	private ArmorUpgradeCourse course;
	private int upgradeLevel;
	
	private final boolean addUnbreakable;
	private final boolean addAquaAffinity;
	private final boolean addMending;
	private final boolean addVanishCurse;
	private final boolean addBindingCurse;
	
	private final double extraArmor;
	private final double extraToughness;
	
	private final double extraAttackDamage;
	private final double extraAttackSpeed;
	private final double extraHealth;
	private final double extraSpeed;
	private final double extraKnockbackResistance;
	private final double extraLuck;
	
	private final int extraProtection;
	private final int extraFireProtection;
	private final int extraFeatherFalling;
	private final int extraBlastProtection;
	private final int extraProjectileProtection;
	private final int extraRespiration;
	private final int extraThorns;
	private final int extraDepthsStrider;
	private final int extraFrostWalker;
	private final int extraUnbreaking;
	
	private final Stack[] price;

	public SingleArmorUpgrade(boolean addUnbreakable, boolean addAquaAffinity, boolean addMending, boolean addVanishCurse, boolean addBindingCurse,
			double extraArmor, double extraToughness, double extraAttackDamage, double extraAttackSpeed, double extraHealth, double extraSpeed,
			double extraKnockbackResistance, double extraLuck, int extraProtection, int extraFireProtection, int extraFeatherFalling,
			int extraBlastProtection, int extraProjectileProtection, int extraRespiration, int extraThorns, int extraDepthsStrider, int extraFrostWalker, int extraUnbreaking, Stack... price) {
		this.addUnbreakable = addUnbreakable;
		this.addAquaAffinity = addAquaAffinity;
		this.addMending = addMending;
		this.addVanishCurse = addVanishCurse;
		this.addBindingCurse = addBindingCurse;
		this.extraArmor = extraArmor;
		this.extraToughness = extraToughness;
		this.extraAttackDamage = extraAttackDamage;
		this.extraAttackSpeed = extraAttackSpeed;
		this.extraHealth = extraHealth;
		this.extraSpeed = extraSpeed;
		this.extraKnockbackResistance = extraKnockbackResistance;
		this.extraLuck = extraLuck;
		this.extraProtection = extraProtection;
		this.extraFireProtection = extraFireProtection;
		this.extraFeatherFalling = extraFeatherFalling;
		this.extraBlastProtection = extraBlastProtection;
		this.extraProjectileProtection = extraProjectileProtection;
		this.extraRespiration = extraRespiration;
		this.extraThorns = extraThorns;
		this.extraDepthsStrider = extraDepthsStrider;
		this.extraFrostWalker = extraFrostWalker;
		this.extraUnbreaking = extraUnbreaking;
		this.price = price;
	}

	public boolean addUnbreakable() {
		return addUnbreakable;
	}

	public boolean addAquaAffinity() {
		return addAquaAffinity;
	}

	public boolean addMending() {
		return addMending;
	}

	public boolean addVanishCurse() {
		return addVanishCurse;
	}

	public boolean addBindingCurse() {
		return addBindingCurse;
	}

	public double getExtraArmor() {
		return extraArmor;
	}

	public double getExtraToughness() {
		return extraToughness;
	}

	public double getExtraAttackDamage() {
		return extraAttackDamage;
	}

	public double getExtraAttackSpeed() {
		return extraAttackSpeed;
	}

	public double getExtraHealth() {
		return extraHealth;
	}

	public double getExtraSpeed() {
		return extraSpeed;
	}

	public double getExtraKnockbackResistance() {
		return extraKnockbackResistance;
	}

	public double getExtraLuck() {
		return extraLuck;
	}

	public int getExtraProtection() {
		return extraProtection;
	}

	public int getExtraFireProtection() {
		return extraFireProtection;
	}

	public int getExtraFeatherFalling() {
		return extraFeatherFalling;
	}

	public int getExtraBlastProtection() {
		return extraBlastProtection;
	}

	public int getExtraProjectileProtection() {
		return extraProjectileProtection;
	}

	public int getExtraRespiration() {
		return extraRespiration;
	}

	public int getExtraThorns() {
		return extraThorns;
	}

	public int getExtraDepthsStrider() {
		return extraDepthsStrider;
	}

	public int getExtraFrostWalker() {
		return extraFrostWalker;
	}

	public int getExtraUnbreaking() {
		return extraUnbreaking;
	}
	
	public ArmorUpgradeCourse getCourse(){
		return course;
	}
	
	public int getLevel(){
		return upgradeLevel;
	}
	
	public Stack[] getPrice(){
		return price;
	}
	
	void setCourse(ArmorUpgradeCourse course){
		this.course = course;
	}
	
	void setUpgradeLevel(int level){
		upgradeLevel = level;
	}
	
	public static class Stack {
		
		private final Material item;
		private final int amount;
		
		public Stack(Material item, int amount){
			this.item = item;
			this.amount = amount;
		}
		
		public Material getItem(){
			return item;
		}
		
		public int getAmount(){
			return amount;
		}
	}
}
