package nl.knokko.armor;

import org.bukkit.Material;

import static org.bukkit.Material.*;

public enum ArmorType {
	
	LEATHER(LEATHER_BOOTS, LEATHER_LEGGINGS, LEATHER_CHESTPLATE, LEATHER_HELMET),
	GOLD(GOLDEN_BOOTS, GOLDEN_LEGGINGS, GOLDEN_CHESTPLATE, GOLDEN_HELMET),
	CHAIN(CHAINMAIL_BOOTS, CHAINMAIL_LEGGINGS, CHAINMAIL_CHESTPLATE, CHAINMAIL_HELMET),
	IRON(IRON_BOOTS, IRON_LEGGINGS, IRON_CHESTPLATE, IRON_HELMET),
	DIAMOND(DIAMOND_BOOTS, DIAMOND_LEGGINGS, DIAMOND_CHESTPLATE, DIAMOND_HELMET);
	
	public static ArmorType fromString(String string){
		return valueOf(string.toUpperCase());
	}
	
	public static ArmorType fromMaterial(Material material){
		switch(material){
		case LEATHER_BOOTS : return LEATHER;
		case LEATHER_LEGGINGS : return LEATHER;
		case LEATHER_CHESTPLATE : return LEATHER;
		case LEATHER_HELMET : return LEATHER;
		case GOLDEN_BOOTS : return GOLD;
		case GOLDEN_LEGGINGS : return GOLD;
		case GOLDEN_CHESTPLATE : return GOLD;
		case GOLDEN_HELMET : return GOLD;
		case CHAINMAIL_BOOTS : return CHAIN;
		case CHAINMAIL_LEGGINGS : return CHAIN;
		case CHAINMAIL_CHESTPLATE : return CHAIN;
		case CHAINMAIL_HELMET : return CHAIN;
		case IRON_BOOTS : return IRON;
		case IRON_LEGGINGS : return IRON;
		case IRON_CHESTPLATE : return IRON;
		case IRON_HELMET : return IRON;
		case DIAMOND_BOOTS : return DIAMOND;
		case DIAMOND_LEGGINGS : return DIAMOND;
		case DIAMOND_CHESTPLATE : return DIAMOND;
		case DIAMOND_HELMET : return DIAMOND;
		default : return null;
		}
	}
	
	private final Material boots;
	private final Material leggings;
	private final Material chestplate;
	private final Material helmet;
	
	ArmorType(Material boots, Material legs, Material chest, Material helmet){
		this.boots = boots;
		this.leggings = legs;
		this.chestplate = chest;
		this.helmet = helmet;
	}
	
	@Override
	public String toString(){
		return name().toLowerCase();
	}
	
	public Material getBoots(){
		return boots;
	}
	
	public Material getLeggings(){
		return leggings;
	}
	
	public Material getChestplate(){
		return chestplate;
	}
	
	public Material getHelmet(){
		return helmet;
	}
	
	public Material get(ArmorPlace place){
		switch(place){
		case BOOTS:
			return getBoots();
		case CHESTPLATE:
			return getChestplate();
		case HELMET:
			return getHelmet();
		case LEGGINGS:
			return getLeggings();
		}
		throw new IllegalArgumentException("Unknown ArmorPlace: " + place);
	}
}
