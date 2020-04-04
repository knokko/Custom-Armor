package nl.knokko.armor;

import org.bukkit.Material;

public enum ArmorPlace {
	
	HELMET("head"),
	CHESTPLATE("chest"),
	LEGGINGS("legs"),
	BOOTS("feet");
	
	public static ArmorPlace fromString(String string){
		return valueOf(string.toUpperCase());
	}
	
	public static ArmorPlace fromMaterial(Material material){
		switch(material){
		case LEATHER_HELMET : return HELMET;
		case GOLDEN_HELMET : return HELMET;
		case CHAINMAIL_HELMET : return HELMET;
		case IRON_HELMET : return HELMET;
		case DIAMOND_HELMET : return HELMET;
		case LEATHER_CHESTPLATE : return CHESTPLATE;
		case GOLDEN_CHESTPLATE : return CHESTPLATE;
		case CHAINMAIL_CHESTPLATE : return CHESTPLATE;
		case IRON_CHESTPLATE : return CHESTPLATE;
		case DIAMOND_CHESTPLATE : return CHESTPLATE;
		case LEATHER_LEGGINGS : return LEGGINGS;
		case GOLDEN_LEGGINGS : return LEGGINGS;
		case CHAINMAIL_LEGGINGS : return LEGGINGS;
		case IRON_LEGGINGS : return LEGGINGS;
		case DIAMOND_LEGGINGS : return LEGGINGS;
		case LEATHER_BOOTS : return BOOTS;
		case GOLDEN_BOOTS : return BOOTS;
		case CHAINMAIL_BOOTS : return BOOTS;
		case IRON_BOOTS : return BOOTS;
		case DIAMOND_BOOTS : return BOOTS;
		default : return null;
		}
	}
	
	private final String slot;
	
	ArmorPlace(String slot){
		this.slot = slot;
	}
	
	@Override
	public String toString(){
		return name().toLowerCase();
	}
	
	public String getSlot(){
		return slot;
	}
}
