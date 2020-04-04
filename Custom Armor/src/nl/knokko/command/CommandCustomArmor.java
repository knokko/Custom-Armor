package nl.knokko.command;

import java.util.ArrayList;

import nl.knokko.main.CustomArmor;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class CommandCustomArmor implements CommandExecutor, Listener {
	
	private static final String NAME_EXAMPLE = "Generate an example config";
	
	private static final String USE = ChatColor.YELLOW + "You should use: /customarmor enable/disable/reload/compress/example";
	
	private final CustomArmor plugin;
	
	public CommandCustomArmor(CustomArmor plugin){
		this.plugin = plugin;
	}

	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if(sender.isOp()){
			if(args.length == 1){
				if(args[0].equals("enable")){
					if(plugin.activate())
						sender.sendMessage(ChatColor.GREEN + "The custom armor recipes have been enabled.");
					else
						sender.sendMessage(ChatColor.YELLOW + "The custom armor recipes were already enabled.");
				}
				else if(args[0].equals("reload")){
					if(plugin.reload())
						sender.sendMessage(ChatColor.GREEN  + "The custom armor config has been reloaded.");
					else
						sender.sendMessage(ChatColor.YELLOW + "You can't reload this plugin while it is inactive, use instead :/customarmor enable");
				}
				else if(args[0].equals("disable")){
					if(plugin.deactivate())
						sender.sendMessage(ChatColor.GREEN + "The custom armor recipes have been disabled and the normal recipes have been restored.");
					else
						sender.sendMessage(ChatColor.YELLOW + "The custom armor recipes were already disabled.");
				}
				else if(args[0].equals("compress")){
					plugin.getArmorConfig().compress();
					sender.sendMessage(ChatColor.GREEN + "The unused data of the config should have been deleted.");
				}
				else if(args[0].equals("example") && sender instanceof Player){
					((Player) sender).openInventory(createExampleMenu());
				}
				else
					sender.sendMessage(USE);
			}
			else
				sender.sendMessage(USE);
		}
		else
			sender.sendMessage(ChatColor.RED + "Only operators can use this command.");
		return false;
	}
	
	@EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
	public void onInventoryClick(final InventoryClickEvent event){
		Inventory inv = event.getInventory();
		if(inv != null && event.getView().getTitle().equals(NAME_EXAMPLE)){
			event.setCancelled(true);
			if(event.getCurrentItem() != null){
				Material type = event.getCurrentItem().getType();
				if(type == Material.LAVA_BUCKET)
					plugin.getArmorConfig().exampleNoArmor();
				else if(type == Material.CHAINMAIL_CHESTPLATE)
					plugin.getArmorConfig().exampleChainmail();
				else if(type == Material.CACTUS)
					plugin.getArmorConfig().exampleCactusBlaze();
				else if(type == Material.OBSIDIAN)
					plugin.getArmorConfig().exampleDurability();
				else if(type == Material.BLAZE_POWDER)
					plugin.getArmorConfig().exampleBlazeUpgrades();
				if(type != Material.BARRIER)
					plugin.reload();
				Bukkit.getScheduler().runTask(plugin, new Runnable(){

					public void run() {
						event.getWhoClicked().closeInventory();
					}
				});
			}
		}
	}
	
	private static Inventory createExampleMenu(){
		Inventory menu = Bukkit.createInventory(null, 9, NAME_EXAMPLE);
		menu.setItem(0, getNamedStack(Material.BARRIER, "Close", "Closes this menu"));
		menu.setItem(2, getNamedStack(Material.LAVA_BUCKET, "No Armor", "Generates a config file", "which will remove", "all armor recipes."));
		menu.setItem(3, getNamedStack(Material.CHAINMAIL_CHESTPLATE, "Add chain armor", "Generates a config file", "which will have", "all normal recipes", "and adds chainmail", "armor which can", "be crafted with flint."));
		menu.setItem(4, getNamedStack(Material.CACTUS, "Cactus/Blaze example", "Generates a config file", "which will add cactus", "and blaze armor.", "This is useful", "as example config."));
		menu.setItem(5, getNamedStack(Material.OBSIDIAN, "Durability upgrade example", "Generates a config file", "with all normal armor,", "but it will allow", "players to upgrade", "armor in an anvil.", "This should be a", "useful upgrading example."));
		menu.setItem(6, getNamedStack(Material.BLAZE_POWDER, "Blaze upgrade/enchant example", "Generates a config file", "with all normal armor", " and blaze armor.", "Blaze armor can be", "upgraded in an anvil", "and enchanted in an", "enchantment table."));
		return menu;
	}
	
	private static ItemStack getNamedStack(Material material, String name, String... lore){
		ItemMeta meta = Bukkit.getItemFactory().getItemMeta(material);
		meta.setDisplayName(name);
		ArrayList<String> loreList = new ArrayList<String>();
		for(String line : lore)
			loreList.add(line);
		meta.setLore(loreList);
		ItemStack stack = new ItemStack(material);
		stack.setItemMeta(meta);
		return stack;
	}
}
