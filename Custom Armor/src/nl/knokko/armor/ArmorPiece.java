package nl.knokko.armor;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

import net.minecraft.server.v1_11_R1.NBTTagCompound;
import net.minecraft.server.v1_11_R1.NBTTagDouble;
import net.minecraft.server.v1_11_R1.NBTTagInt;
import net.minecraft.server.v1_11_R1.NBTTagList;
import net.minecraft.server.v1_11_R1.NBTTagLong;
import net.minecraft.server.v1_11_R1.NBTTagString;
import nl.knokko.armor.enchantment.ArmorEnchantmentCourse;
import nl.knokko.armor.enchantment.SingleArmorEnchantment;
import nl.knokko.armor.upgrade.ArmorUpgradeCourse;
import nl.knokko.armor.upgrade.SingleArmorUpgrade;
import nl.knokko.main.CustomArmor;

import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.craftbukkit.v1_11_R1.inventory.CraftItemStack;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;

import static org.bukkit.enchantments.Enchantment.*;

public final class ArmorPiece implements Comparable<ArmorPiece> {
	
	private final ArmorType type;
	private final ArmorPlace place;
	
	private final String name;
	
	private final double armor;
	private final double toughness;
	
	private final double attackDamage;
	private final double attackSpeed;
	private final double maxHealth;
	private final double movementSpeed;
	private final double knockbackResistance;
	private final double luck;
	
	private final boolean unbreakable;
	private final int leatherRed;
	private final int leatherGreen;
	private final int leatherBlue;
	
	private final int protection;
	private final int fireProtection;
	private final int featherFalling;
	private final int blastProtection;
	private final int projectileProtection;
	private final int respiration;
	private final boolean aquaAffinity;
	private final int thorns;
	private final int depthsStrider;
	private final int frostWalker;
	private final int unbreaking;
	private final boolean mending;
	private final boolean vanishCurse;
	private final boolean bindingCurse;
	
	private UpgradeAdmin upgrades;

	public ArmorPiece(String name, ArmorType type, ArmorPlace place, int leatherRed, int leatherGreen, int leatherBlue, double armor, double toughness, double attackDamage, double attackSpeed, double maxHealth, double movementSpeed, double knockbackResistance, double luck, boolean unbreakable, int prot, int fireProt, int feather, int blast, int projectile, int resp, int aqua, int thorns, int depths, int frost, int unbreaking, int mending, int vanish, int bind) {
		this.name = name;
		this.type = type;
		this.place = place;
		this.leatherRed = leatherRed;
		this.leatherGreen = leatherGreen;
		this.leatherBlue = leatherBlue;
		this.armor = armor;
		this.toughness = toughness;
		this.attackDamage = attackDamage;
		this.attackSpeed = attackSpeed;
		this.maxHealth = maxHealth;
		this.movementSpeed = movementSpeed;
		this.knockbackResistance = knockbackResistance;
		this.luck = luck;
		this.protection = prot;
		this.fireProtection = fireProt;
		this.featherFalling = feather;
		this.blastProtection = blast;
		this.projectileProtection = projectile;
		this.respiration = resp;
		this.aquaAffinity = aqua >= 1;
		this.thorns = thorns;
		this.depthsStrider = depths;
		this.frostWalker = frost;
		this.unbreaking = unbreaking;
		this.mending = mending >= 1;
		this.vanishCurse = vanish >= 1;
		this.bindingCurse = bind >= 1;
		this.unbreakable = unbreakable;
		this.upgrades = new UpgradeAdmin();
	}
	
	private ArmorPiece copyUpgradeAdmin(UpgradeAdmin old){
		upgrades = new UpgradeAdmin(old);
		return this;
	}
	
	private ArmorPiece setUpgradeAdmin(UpgradeAdmin admin){
		upgrades = admin;
		return this;
	}
	
	private ArmorPiece registerUpgrade(SingleArmorUpgrade upgrade){
		upgrades.upgrade(upgrade);
		return this;
	}
	
	private ArmorPiece registerEnchantment(SingleArmorEnchantment enchantment){
		upgrades.enchant(enchantment);
		return this;
	}
	
	@Override
	public String toString(){
		return "ArmorPiece(" + name + "," + type + "," + place + "," + armor + "," + toughness + "," + attackDamage + "," + attackSpeed + "," + maxHealth + "," + movementSpeed + "," + knockbackResistance + "," + luck + "," + unbreakable + "," + protection + "," + fireProtection + ")";
	}
	
	@Override
	public ArmorPiece clone(){
		return new ArmorPiece(name, type, place, leatherRed, leatherGreen, leatherBlue, armor, toughness, attackDamage, attackSpeed, maxHealth, movementSpeed, knockbackResistance, luck, unbreakable, protection, fireProtection, featherFalling, blastProtection, projectileProtection, respiration, aquaAffinity ? 1 : 0, thorns, depthsStrider, frostWalker, unbreaking, mending ? 1 : 0, vanishCurse ? 1 : 0, bindingCurse ? 1 : 0).copyUpgradeAdmin(upgrades);
	}
	
	@Override
	public boolean equals(Object other){
		if(other instanceof ArmorPiece){
			ArmorPiece ap = (ArmorPiece) other;
			if(ap.getName().equals(getName())){
				try {
					Field[] fields = ArmorPiece.class.getDeclaredFields();
					for(Field field : fields){
						if(!field.getName().equals("upgrades") && !field.getName().equals("leatherRed") && !field.getName().equals("leatherGreen") && !field.getName().equals("leatherBlue") && !field.get(this).equals(field.get(ap))){
							System.out.println("field " + field.getName() + " is not equal: (" + field.get(this) + "," + field.get(ap));
							return false;
						}
					}
					return true;
				} catch(Exception ex){
					Bukkit.getLogger().log(Level.SEVERE, "Can't compare nl.knokko.armor.ArmorPiece", ex);
				}
			}
		}
		return false;
	}
	
	@Override
	public int hashCode(){
		return name.hashCode();
	}
	
	public int compareTo(ArmorPiece ap){
		return name.compareTo(ap.name);
	}
	
	/**
	 * @param u The upgrade to apply
	 * @return a copy of this armor piece that has the specified upgrade
	 */
	public ArmorPiece upgrade(SingleArmorUpgrade u){
		return new ArmorPiece(name, type, place, leatherRed, leatherGreen, leatherBlue, armor + u.getExtraArmor(), toughness + u.getExtraToughness(),
				attackDamage + u.getExtraAttackDamage(), attackSpeed + u.getExtraAttackSpeed(), maxHealth + u.getExtraHealth(),
				movementSpeed + u.getExtraSpeed(), knockbackResistance + u.getExtraKnockbackResistance(), luck + u.getExtraLuck(),
				unbreakable || u.addUnbreakable(), protection + u.getExtraProtection(), fireProtection + u.getExtraFireProtection(),
				featherFalling + u.getExtraFeatherFalling(), blastProtection + u.getExtraBlastProtection(), projectileProtection + u.getExtraProjectileProtection(),
				respiration + u.getExtraRespiration(), (aquaAffinity || u.addAquaAffinity()) ? 1 : 0, thorns + u.getExtraThorns(), depthsStrider + u.getExtraDepthsStrider(),
				frostWalker + u.getExtraFrostWalker(), unbreaking + u.getExtraUnbreaking(), (mending || u.addMending()) ? 1 : 0, (vanishCurse || u.addVanishCurse()) ? 1 : 0, (bindingCurse || u.addBindingCurse()) ? 1 : 0).copyUpgradeAdmin(upgrades).registerUpgrade(u);
	}
	
	public ArmorPiece enchant(SingleArmorEnchantment e){
		return new ArmorPiece(name, type, place, leatherRed, leatherGreen, leatherBlue, armor + e.getExtraArmor(), toughness + e.getExtraToughness(),
				attackDamage + e.getExtraAttackDamage(), attackSpeed + e.getExtraAttackSpeed(), maxHealth + e.getExtraHealth(),
				movementSpeed + e.getExtraSpeed(), knockbackResistance + e.getExtraKnockbackResistance(), luck + e.getExtraLuck(),
				unbreakable || e.addUnbreakable(), protection + e.getExtraProtection(), fireProtection + e.getExtraFireProtection(),
				featherFalling + e.getExtraFeatherFalling(), blastProtection + e.getExtraBlastProtection(), projectileProtection + e.getExtraProjectileProtection(),
				respiration + e.getExtraRespiration(), (aquaAffinity || e.addAquaAffinity()) ? 1 : 0, thorns + e.getExtraThorns(), depthsStrider + e.getExtraDepthsStrider(),
				frostWalker + e.getExtraFrostWalker(), unbreaking + e.getExtraUnbreaking(), (mending || e.addMending()) ? 1 : 0, (vanishCurse || e.addVanishCurse()) ? 1 : 0, (bindingCurse || e.addBindingCurse()) ? 1 : 0).copyUpgradeAdmin(upgrades).registerEnchantment(e);
	}
	
	public ArmorPiece getOriginal(){
		if(upgrades.upgrades.isEmpty() && upgrades.enchants.isEmpty())
			return this;
		return upgrades.getOriginal(this);
	}
	
	public ArmorType getType(){
		return type;
	}
	
	public String getName(){
		return name;
	}
	
	public int getUpgradeLevel(ArmorUpgradeCourse course){
		return upgrades.getUpgradeLevel(course.getName());
	}
	
	public int getEnchantmentLevel(ArmorEnchantmentCourse course){
		return upgrades.getEnchantmentLevel(course.getName());
	}
	
	public static ArmorPiece fromItemStack(ItemStack stack){
		net.minecraft.server.v1_11_R1.ItemStack nmsStack = CraftItemStack.asNMSCopy(stack);
		NBTTagCompound compound = (nmsStack.hasTag()) ? nmsStack.getTag() : null;
		if(compound == null)
			return null;
		int leatherRed = 160;
		int leatherGreen = 101;
		int leatherBlue = 64;
		ItemMeta meta = stack.getItemMeta();
		if(meta instanceof LeatherArmorMeta){
			LeatherArmorMeta lam = (LeatherArmorMeta) meta;
			leatherRed = lam.getColor().getRed();
			leatherGreen = lam.getColor().getGreen();
			leatherBlue = lam.getColor().getBlue();
		}
		String name = meta.getDisplayName();
		boolean unbreakable = meta.isUnbreakable();
		NBTTagList modifiers = compound.getList("AttributeModifiers", 10);
		UpgradeAdmin upgrades = new UpgradeAdmin();
		upgrades.load(compound);
		double armor = 0;
		double toughness = 0;
		double attackDamage = 0;
		double attackSpeed = 0;
		double maxHealth = 0;
		double movementSpeed = 0;
		double knockbackResistance = 0;
		double luck = 0;
		if(modifiers != null){
			armor = getAttributeValue(modifiers, "generic.armor");
			toughness = getAttributeValue(modifiers, "generic.armorToughness");
			attackDamage = getAttributeValue(modifiers, "generic.attackDamage");
			attackSpeed = getAttributeValue(modifiers, "generic.attackSpeed");
			maxHealth = getAttributeValue(modifiers, "generic.maxHealth");
			movementSpeed = getAttributeValue(modifiers, "generic.movementSpeed");
			knockbackResistance = getAttributeValue(modifiers, "generic.knockbackResistance");
			luck = getAttributeValue(modifiers, "generic.luck");
		}
		ArmorType type = ArmorType.fromMaterial(stack.getType());
		ArmorPlace place = ArmorPlace.fromMaterial(stack.getType());
		if(type == null || place == null)
			return null;
		return new ArmorPiece(name, type, place, leatherRed, leatherGreen, leatherBlue, armor, toughness, attackDamage,
				attackSpeed, maxHealth, movementSpeed, knockbackResistance, luck, unbreakable, l(stack, PROTECTION_ENVIRONMENTAL),
				l(stack, PROTECTION_FIRE), l(stack, PROTECTION_FALL), l(stack, PROTECTION_EXPLOSIONS), l(stack, PROTECTION_PROJECTILE),
				l(stack, OXYGEN), l(stack, WATER_WORKER), l(stack, THORNS), l(stack, DEPTH_STRIDER), l(stack, FROST_WALKER),
				l(stack, DURABILITY), l(stack, MENDING), l(stack, VANISHING_CURSE), l(stack, BINDING_CURSE)).setUpgradeAdmin(upgrades);
	}
	
	private static int l(ItemStack stack, Enchantment enchantment){
		return stack.getEnchantmentLevel(enchantment);
	}
	
	public ItemStack createItemStack(){
		ItemStack stack = new ItemStack(type.get(place));
		ItemMeta meta = stack.getItemMeta();
		if(meta instanceof LeatherArmorMeta)
			((LeatherArmorMeta) meta).setColor(Color.fromRGB(leatherRed, leatherGreen, leatherBlue));
		meta.setDisplayName(name);
		meta.setUnbreakable(unbreakable);
		stack.setItemMeta(meta);
		net.minecraft.server.v1_11_R1.ItemStack nmsStack = CraftItemStack.asNMSCopy(stack);
		NBTTagCompound compound = (nmsStack.hasTag()) ? nmsStack.getTag() : new NBTTagCompound();
		NBTTagList modifiers = new NBTTagList();
		String slot = place.getSlot();
		addAttribute(modifiers, "generic.armor", slot, armor);
		addAttribute(modifiers, "generic.armorToughness", slot, toughness);
		addAttribute(modifiers, "generic.attackDamage", slot, attackDamage);
		addAttribute(modifiers, "generic.attackSpeed", slot, attackSpeed);
		addAttribute(modifiers, "generic.maxHealth", slot, maxHealth);
		addAttribute(modifiers, "generic.movementSpeed", slot, movementSpeed);
		addAttribute(modifiers, "generic.knockbackResistance", slot, knockbackResistance);
		addAttribute(modifiers, "generic.luck", slot, luck);
		compound.set("AttributeModifiers", modifiers);
		upgrades.save(compound);
		nmsStack.setTag(compound);
		stack = CraftItemStack.asBukkitCopy(nmsStack);
		addEnchantment(stack, Enchantment.PROTECTION_ENVIRONMENTAL, protection);
		addEnchantment(stack, Enchantment.PROTECTION_FIRE, fireProtection);
		addEnchantment(stack, Enchantment.PROTECTION_FALL, featherFalling);
		addEnchantment(stack, Enchantment.PROTECTION_EXPLOSIONS, blastProtection);
		addEnchantment(stack, Enchantment.PROTECTION_PROJECTILE, projectileProtection);
		addEnchantment(stack, Enchantment.OXYGEN, respiration);
		addEnchantment(stack, Enchantment.WATER_WORKER, aquaAffinity ? 1 : 0);
		addEnchantment(stack, Enchantment.THORNS, thorns);
		addEnchantment(stack, Enchantment.DEPTH_STRIDER, depthsStrider);
		addEnchantment(stack, Enchantment.FROST_WALKER, frostWalker);
		addEnchantment(stack, Enchantment.DURABILITY, unbreaking);
		addEnchantment(stack, Enchantment.MENDING, mending ? 1 : 0);
		addEnchantment(stack, Enchantment.VANISHING_CURSE, vanishCurse ? 1 : 0);
		addEnchantment(stack, Enchantment.BINDING_CURSE, bindingCurse ? 1 : 0);
		return stack;
	}
	
	private static void addEnchantment(ItemStack stack, Enchantment enchantment, int level){
		if(level != 0)
			stack.addUnsafeEnchantment(enchantment, level);
	}
	
	private static double getAttributeValue(NBTTagList modifiers, String name){
		for(int i = 0; i < modifiers.size(); i++){
			NBTTagCompound nbt = modifiers.get(i);
			if(nbt.getString("AttributeName").equals(name))
				return nbt.getDouble("Amount");
		}
		return 0;
	}
	
	private static void addAttribute(NBTTagList modifiers, String name, String slot, double value){
		if(value == 0)
			return;
		NBTTagCompound damage = new NBTTagCompound();
		damage.set("AttributeName", new NBTTagString(name));
		damage.set("Name", new NBTTagString(name));
		damage.set("Amount", new NBTTagDouble(value));
		damage.set("Operation", new NBTTagInt(0));
		damage.set("UUIDLeast", new NBTTagLong(System.currentTimeMillis()));
		damage.set("UUIDMost", new NBTTagLong(System.nanoTime()));
		damage.set("Slot", new NBTTagString(slot));
		modifiers.add(damage);
	}
	
	private static class Upgrade {
		
		private final String upgradeName;
		private int level;
		
		private Upgrade(String name, int level){
			upgradeName = name;
			this.level = level;
		}
		
		@Override
		public String toString(){
			return "Upgrade " + upgradeName + " level " + level;
		}
		
		private void save(NBTTagCompound nbt){
			nbt.setInt(upgradeName, level);
		}
		
		private void upgrade(){
			level++;
		}
	}
	
	private static class UpgradeAdmin {
		
		private final List<Upgrade> upgrades;
		private final List<Enchant> enchants;
		
		private UpgradeAdmin(){
			upgrades = new ArrayList<Upgrade>();
			enchants = new ArrayList<Enchant>();
		}
		
		private ArmorPiece getOriginal(ArmorPiece ap) {
			boolean unbreakable = ap.unbreakable;
			boolean aqua = ap.aquaAffinity;
			boolean mending = ap.mending;
			boolean vanish = ap.vanishCurse;
			boolean bind = ap.bindingCurse;
			double armor = ap.armor;
			double toughness = ap.toughness;
			double attackDamage = ap.attackDamage;
			double attackSpeed = ap.attackSpeed;
			double maxHealth = ap.maxHealth;
			double movementSpeed = ap.movementSpeed;
			double knockbackResistance = ap.knockbackResistance;
			double luck = ap.luck;
			int protection = ap.protection;
			int fireProtection = ap.fireProtection;
			int featherFalling = ap.featherFalling;
			int blastProtection = ap.blastProtection;
			int projectileProtection = ap.projectileProtection;
			int respiration = ap.respiration;
			int thorns = ap.thorns;
			int depthStrider = ap.depthsStrider;
			int frostWalker = ap.frostWalker;
			int unbreaking = ap.unbreaking;
			for(Upgrade upgrade : upgrades){
				ArmorUpgradeCourse course = CustomArmor.getInstance().getArmorConfig().upgradeFromName(upgrade.upgradeName);
				for(int level = upgrade.level; level > 0; level--){
					SingleArmorUpgrade au = course.getUpgrade(level);
					armor -= au.getExtraArmor();
					toughness -= au.getExtraToughness();
					attackDamage -= au.getExtraAttackDamage();
					attackSpeed -= au.getExtraAttackSpeed();
					maxHealth -= au.getExtraHealth();
					movementSpeed -= au.getExtraSpeed();
					knockbackResistance -= au.getExtraKnockbackResistance();
					luck -= au.getExtraLuck();
					protection -= au.getExtraProtection();
					fireProtection -= au.getExtraFireProtection();
					featherFalling -= au.getExtraFeatherFalling();
					blastProtection -= au.getExtraBlastProtection();
					projectileProtection -= au.getExtraProjectileProtection();
					respiration -= au.getExtraRespiration();
					thorns -= au.getExtraThorns();
					depthStrider -= au.getExtraDepthsStrider();
					frostWalker -= au.getExtraFrostWalker();
					unbreaking -= au.getExtraUnbreaking();
					if(unbreakable && au.addUnbreakable())
						unbreakable = false;
					if(aqua && au.addAquaAffinity())
						aqua = false;
					if(mending && au.addMending())
						mending = false;
					if(vanish && au.addVanishCurse())
						vanish = false;
					if(bind && au.addBindingCurse())
						bind = false;
				}
			}
			for(Enchant ench : enchants){
				ArmorEnchantmentCourse aec = CustomArmor.getInstance().getArmorConfig().enchantmentFromName(ench.enchantmentName);
				for(int level = ench.level; level > 0; level--){
					SingleArmorEnchantment au = aec.getEnchantment(level);
					armor -= au.getExtraArmor();
					toughness -= au.getExtraToughness();
					attackDamage -= au.getExtraAttackDamage();
					attackSpeed -= au.getExtraAttackSpeed();
					maxHealth -= au.getExtraHealth();
					movementSpeed -= au.getExtraSpeed();
					knockbackResistance -= au.getExtraKnockbackResistance();
					luck -= au.getExtraLuck();
					protection -= au.getExtraProtection();
					fireProtection -= au.getExtraFireProtection();
					featherFalling -= au.getExtraFeatherFalling();
					blastProtection -= au.getExtraBlastProtection();
					projectileProtection -= au.getExtraProjectileProtection();
					respiration -= au.getExtraRespiration();
					thorns -= au.getExtraThorns();
					depthStrider -= au.getExtraDepthsStrider();
					frostWalker -= au.getExtraFrostWalker();
					unbreaking -= au.getExtraUnbreaking();
					if(unbreakable && au.addUnbreakable())
						unbreakable = false;
					if(aqua && au.addAquaAffinity())
						aqua = false;
					if(mending && au.addMending())
						mending = false;
					if(vanish && au.addVanishCurse())
						vanish = false;
					if(bind && au.addBindingCurse())
						bind = false;
				}
			}
			return new ArmorPiece(ap.name, ap.type, ap.place, ap.leatherRed, ap.leatherGreen, ap.leatherBlue, armor, toughness, attackDamage, attackSpeed, maxHealth, movementSpeed, knockbackResistance, luck, unbreakable, protection, fireProtection, featherFalling, blastProtection, projectileProtection, respiration, aqua ? 1 : 0, thorns, depthStrider, frostWalker, unbreaking, mending ? 1 : 0, vanish ? 1 : 0, bind ? 1 : 0);
		}

		@Override
		public String toString(){
			return upgrades + " and " + enchants;
		}
		
		private UpgradeAdmin(UpgradeAdmin old){
			upgrades = new ArrayList<Upgrade>(old.upgrades);
			enchants = new ArrayList<Enchant>(old.enchants);
		}
		
		private void save(NBTTagCompound itemTag){
			NBTTagCompound upgradeTag = new NBTTagCompound();
			NBTTagCompound enchantTag = new NBTTagCompound();
			for(Upgrade upgrade : upgrades)
				upgrade.save(upgradeTag);
			for(Enchant enchant : enchants)
				enchant.save(enchantTag);
			itemTag.set("custom armor upgrades", upgradeTag);
			itemTag.set("custom armor enchantments", enchantTag);
		}
		
		private void load(NBTTagCompound itemTag){
			NBTTagCompound nbt = itemTag.getCompound("custom armor upgrades");
			for(String key : nbt.c())
				upgrades.add(new Upgrade(key, nbt.getInt(key)));
			NBTTagCompound cenchants = itemTag.getCompound("custom armor enchantments");
			for(String key : cenchants.c())
				enchants.add(new Enchant(key, cenchants.getInt(key)));
		}
		
		private void upgrade(SingleArmorUpgrade upgrade){
			for(Upgrade u : upgrades){
				if(u.upgradeName.equals(upgrade.getCourse().getName())){
					u.upgrade();
					return;
				}
			}
			upgrades.add(new Upgrade(upgrade.getCourse().getName(), upgrade.getLevel()));
		}
		
		private void enchant(SingleArmorEnchantment enchantment){
			for(Enchant e : enchants){
				if(e.enchantmentName.equals(enchantment.getCourse().getName())){
					e.enchant();
					return;
				}
			}
			enchants.add(new Enchant(enchantment.getCourse().getName(), enchantment.getLevel()));
		}
		
		private int getUpgradeLevel(String upgradeName){
			for(Upgrade upgrade : upgrades){
				if(upgrade.upgradeName.equals(upgradeName))
					return upgrade.level;
			}
			return 0;
		}
		
		private int getEnchantmentLevel(String enchantName){
			for(Enchant enchant : enchants){
				if(enchant.enchantmentName.equals(enchantName))
					return enchant.level;
			}
			return 0;
		}
	}
	
	private static class Enchant {
		
		private final String enchantmentName;
		private int level;
		
		private Enchant(String name, int level){
			enchantmentName = name;
			this.level = level;
		}
		
		@Override
		public String toString(){
			return "Enchantment " + enchantmentName + " level " + level;
		}
		
		private void save(NBTTagCompound nbt){
			nbt.setInt(enchantmentName, level);
		}
		
		private void enchant(){
			level++;
		}
	}
}
