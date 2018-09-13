package nl.knokko.recipes;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import nl.knokko.main.CustomArmor;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.ShapedRecipe;

import static org.bukkit.Material.*;

public class ArmorRecipes {
	
	private static Material[] BANNED_RECIPES = {
		LEATHER_BOOTS, LEATHER_LEGGINGS, LEATHER_CHESTPLATE, LEATHER_HELMET,
		GOLD_BOOTS, GOLD_LEGGINGS, GOLD_CHESTPLATE, GOLD_HELMET,
		CHAINMAIL_BOOTS, CHAINMAIL_LEGGINGS, CHAINMAIL_CHESTPLATE, CHAINMAIL_HELMET,
		IRON_BOOTS, IRON_LEGGINGS, IRON_CHESTPLATE, IRON_HELMET,
		DIAMOND_BOOTS, DIAMOND_LEGGINGS, DIAMOND_CHESTPLATE, DIAMOND_HELMET};
	
	private static List<Recipe> disabledRecipes = new ArrayList<Recipe>(16);
	private static List<Recipe> customRecipes = new ArrayList<Recipe>();
	
	public static void enable(){
		clearVanillaRecipes();
	}
	
	public static void disable(){
		clearCustomRecipes();
		restoreVanillaRecipes();
	}
	
	public static void addCustomRecipe(Material[] recipe, ItemStack output, String id){
		ShapedRecipe sr = new ShapedRecipe(new NamespacedKey(CustomArmor.getInstance(), id.replace(' ', '_')), output);
		sr.shape("abc", "def", "ghi");
		if(recipe[0] != null)
			sr.setIngredient('a', recipe[0]);
		if(recipe[1] != null)
			sr.setIngredient('b', recipe[1]);
		if(recipe[2] != null)
			sr.setIngredient('c', recipe[2]);
		if(recipe[3] != null)
			sr.setIngredient('d', recipe[3]);
		if(recipe[4] != null)
			sr.setIngredient('e', recipe[4]);
		if(recipe[5] != null)
			sr.setIngredient('f', recipe[5]);
		if(recipe[6] != null)
			sr.setIngredient('g', recipe[6]);
		if(recipe[7] != null)
			sr.setIngredient('h', recipe[7]);
		if(recipe[8] != null)
			sr.setIngredient('i', recipe[8]);
		Bukkit.addRecipe(sr);
		customRecipes.add(sr);
	}
	
	public static void clearVanillaRecipes(){
		Iterator<Recipe> iterator = Bukkit.recipeIterator();
		List<Recipe> newRecipes = new ArrayList<Recipe>();
		while(iterator.hasNext()){
			Recipe recipe = iterator.next();
			if(shouldBeRemoved(recipe.getResult().getType()))
				disabledRecipes.add(recipe);
			else
				newRecipes.add(recipe);
		}
		Bukkit.clearRecipes();
		for(Recipe recipe : newRecipes)
			Bukkit.addRecipe(recipe);
	}
	
	public static void clearCustomRecipes(){
		Iterator<Recipe> iterator = Bukkit.recipeIterator();
		List<Recipe> newRecipes = new ArrayList<Recipe>();
		while(iterator.hasNext()){
			Recipe recipe = iterator.next();
			if(!containsCustom(recipe))
				newRecipes.add(recipe);
		}
		Bukkit.clearRecipes();
		for(Recipe recipe : newRecipes)
			Bukkit.addRecipe(recipe);
		customRecipes.clear();
	}
	
	public static void restoreVanillaRecipes(){
		for(Recipe recipe : disabledRecipes)
			Bukkit.addRecipe(recipe);
		disabledRecipes.clear();
	}
	
	private static boolean shouldBeRemoved(Material result){
		for(Material material : BANNED_RECIPES)
			if(material == result)
				return true;
		return false;
	}
	
	private static boolean containsCustom(Recipe recipe){
		for(Recipe cr : customRecipes){
			if(cr.getResult().equals(recipe.getResult()))
				return true;
		}
		return false;
	}
}
