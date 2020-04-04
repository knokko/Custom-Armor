package nl.knokko.gui;

import java.util.ArrayList;
import java.util.List;

import nl.knokko.armor.ArmorPiece;
import nl.knokko.armor.enchantment.ArmorEnchantmentCourse;
import nl.knokko.armor.enchantment.SingleArmorEnchantment;
import nl.knokko.armor.upgrade.SingleArmorUpgrade.Stack;
import nl.knokko.main.CustomArmor;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class CustomEnchanting implements Listener {
	
	private static final String TITLE_CUSTOM_ENCHANTMENT_TABLE = "Enchantments";
	
	private final CustomArmor plugin;

	public CustomEnchanting(CustomArmor plugin) {
		this.plugin = plugin;
	}
	
	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
	public void onPlayerInteract(org.bukkit.event.player.PlayerInteractEvent event){
		if(event.getAction() == Action.RIGHT_CLICK_BLOCK && event.getClickedBlock().getType() == Material.ENCHANTING_TABLE){
			if(plugin.getArmorConfig().disableVanillaEnchanting()){
				event.setCancelled(true);
				if(plugin.getArmorConfig().useCustomEnchanting()){
					if(!openCustomEnchanting(event.getPlayer(), event.getItem()))
						event.getPlayer().sendMessage(ChatColor.RED + "You can only open an enchantment table with custom armor in your main hand.");
				}
				else
					event.getPlayer().sendMessage(ChatColor.RED + "Enchantment tables are disabled on this server.");
			}
			else if(openCustomEnchanting(event.getPlayer(), event.getItem()))
				event.setCancelled(true);
		}
	}
	
	@EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
	public void onInventoryClick(InventoryClickEvent event){
		ItemStack stack = event.getCurrentItem();
		if(event.getView().getTitle().equals(TITLE_CUSTOM_ENCHANTMENT_TABLE))
			event.setCancelled(true);
		if(stack != null && event.getView().getTitle().equals(TITLE_CUSTOM_ENCHANTMENT_TABLE) && event.getWhoClicked() instanceof Player){
			event.setCancelled(true);
			Player player = (Player) event.getWhoClicked();
			Material item = stack.getType();
			if(item == Material.BARRIER)
				closeInventory(player);
			else {
				ItemStack current = player.getInventory().getItemInMainHand();
				String name = stack.getItemMeta().getDisplayName();
				ArmorEnchantmentCourse upgrade = plugin.getArmorConfig().enchantmentFromName(name);
				ArmorPiece piece = ArmorPiece.fromItemStack(current);
				if(piece == null)
					closeInventory(player);
				int level = piece.getEnchantmentLevel(upgrade);
				if(level < upgrade.getMaxLevel()){
					SingleArmorEnchantment single = upgrade.getEnchantment(level + 1);
					if(canPay(player, player.getInventory(), single.getPrice(), single.getRequiredLevel(), single.getLevelCost())){
						piece = piece.enchant(single);
						current = piece.createItemStack();
						player.getInventory().setItemInMainHand(current);
						for(Stack st : single.getPrice()){
							int amount = st.getAmount();
							for(ItemStack itemStack : player.getInventory()){
								if(itemStack != null && itemStack.getType() == st.getItem()){
									if(itemStack.getAmount() >= amount){
										itemStack.setAmount(itemStack.getAmount() - amount);
										break;
									}
									else {
										amount -= itemStack.getAmount();
										itemStack.setAmount(0);
									}
								}
							}
						}
						player.setLevel(player.getLevel() - single.getLevelCost());
						openInventory(player, createCustomEnchantmentGui(player, current, piece));
					}
				}
			}
		}
	}
	
	private boolean openCustomEnchanting(Player player, ItemStack stack){
		if(plugin.getArmorConfig().useCustomEnchanting()){
			ArmorPiece piece = ArmorPiece.fromItemStack(stack);
			if(plugin.getArmorConfig().isRegistered(piece)){
				player.openInventory(createCustomEnchantmentGui(player, stack, piece));
				return true;
			}
		}
		return false;
	}
	
	private Inventory createCustomEnchantmentGui(Player player, ItemStack stack, ArmorPiece piece){
		List<ArmorEnchantmentCourse> upgrades = plugin.getArmorConfig().getEnchantments(piece.getOriginal());
		Inventory gui = Bukkit.createInventory(null, multipleOf9(upgrades.size() + 1), TITLE_CUSTOM_ENCHANTMENT_TABLE);
		gui.setItem(0, getNamedStack(Material.BARRIER, "Close", "Close this menu"));
		int i = 1;
		for(ArmorEnchantmentCourse upgrade : upgrades){
			int level = piece.getEnchantmentLevel(upgrade);
			String[] lore;
			if(level == upgrade.getMaxLevel())
				lore = new String[]{ChatColor.GOLD + "Level " + level, ChatColor.GOLD + "This enchantment has reached", ChatColor.GOLD + "the maximum level!"};
			else {
				List<String> description = upgrade.getDescription(level + 1);
				SingleArmorEnchantment sae = upgrade.getEnchantment(level + 1);
				Stack[] price = sae.getPrice();
				boolean canPay = canPay(player, player.getInventory(), price, sae.getRequiredLevel(), sae.getLevelCost());
				lore = new String[description.size() + 9 + price.length];
				lore[0] = "Currently level " + level;
				if(canPay)
					lore[1] = ChatColor.GOLD + "Click to enchant!";
				else
					lore[1] = ChatColor.RED + "Can't pay enchantment price";
				lore[2] = "";
				lore[3] = "The following will";
				lore[4] = "happen if you enchant:";
				int j = 5;
				for(String des : description){
					lore[j] = des;
					j++;
				}
				lore[j] = "";
				lore[j + 1] = (player.getLevel() >= sae.getRequiredLevel() ? ChatColor.YELLOW : ChatColor.RED) + "Requires " + sae.getRequiredLevel() + " levels";
				lore[j + 2] = ChatColor.RED + "Price:";
				lore[j + 3] = ChatColor.RED + "" + sae.getLevelCost() + " levels";
				j+= 4;
				for(Stack st : price){
					lore[j] = st.getAmount() + " " + st.getItem().name().toLowerCase();
					j++;
				}
			}
			gui.setItem(i, getNamedStack(upgrade.getIcon(), upgrade.getName(), lore));
			i++;
		}
		return gui;
	}
	
	private static int multipleOf9(int size){
		int result = size / 9 * 9;
		if(result < size)
			result += 9;
		return result;
	}
	
	private static ItemStack getNamedStack(Material material, String name, String... lore){
		ItemStack stack = new ItemStack(material);
		ItemMeta meta = stack.getItemMeta();
		meta.setDisplayName(name);
		List<String> loreList = new ArrayList<String>(lore.length);
		for(String l : lore)
			loreList.add(l);
		meta.setLore(loreList);
		stack.setItemMeta(meta);
		return stack;
	}
	
	private void closeInventory(final Player player){
		Bukkit.getScheduler().runTask(plugin, new Runnable(){

			public void run() {
				player.closeInventory();
			}
		});
	}
	
	private void openInventory(final Player player, final Inventory inventory){
		Bukkit.getScheduler().runTask(plugin, new Runnable(){

			public void run() {
				player.openInventory(inventory);
			}
		});
	}
	
	private static boolean canPay(Player player, Inventory inv, Stack[] price, int requiredLevel, int levelCost){
		if(player.getLevel() < requiredLevel || player.getLevel() < levelCost)
			return false;
		for(Stack st : price){
			int owned = 0;
			Material item = st.getItem();
			for(ItemStack itemStack : inv.getContents()){
				if(itemStack != null && itemStack.getType() == item)
					owned += itemStack.getAmount();
			}
			if(owned < st.getAmount())
				return false;
		}
		return true;
	}
}
