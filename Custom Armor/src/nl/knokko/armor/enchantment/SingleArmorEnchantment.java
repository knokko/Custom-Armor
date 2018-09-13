package nl.knokko.armor.enchantment;

import nl.knokko.armor.upgrade.SingleArmorUpgrade.Stack;

public class SingleArmorEnchantment {
	
	private ArmorEnchantmentCourse course;
	
	private int upgradeLevel;
	
	private int levelRequirement;
	private int levelCost;
	
	private final Stack[] price;
	
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

	public SingleArmorEnchantment(boolean addUnbreakable, boolean addAquaAffinity, boolean addMending, boolean addVanishCurse, boolean addBindingCurse,
			double extraArmor, double extraToughness, double extraAttackDamage, double extraAttackSpeed, double extraHealth, double extraSpeed,
			double extraKnockbackResistance, double extraLuck, int extraProtection, int extraFireProtection, int extraFeatherFalling,
			int extraBlastProtection, int extraProjectileProtection, int extraRespiration, int extraThorns, int extraDepthsStrider, int extraFrostWalker, int extraUnbreaking, Stack[] price, int levelRequirement, int levelCost) {
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
		this.levelRequirement = levelRequirement;
		this.levelCost = levelCost;
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
	
	public Stack[] getPrice(){
		return price;
	}
	
	public int getRequiredLevel(){
		return levelRequirement;
	}
	
	public int getLevelCost(){
		return levelCost;
	}
	
	public ArmorEnchantmentCourse getCourse(){
		return course;
	}
	
	public int getLevel(){
		return upgradeLevel;
	}
	
	void setCourse(ArmorEnchantmentCourse course){
		this.course = course;
	}
	
	void setUpgradeLevel(int level){
		upgradeLevel = level;
	}
}
