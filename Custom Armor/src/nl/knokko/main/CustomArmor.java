package nl.knokko.main;

import java.io.File;

import nl.knokko.command.CommandCustomArmor;
import nl.knokko.gui.CustomAnvil;
import nl.knokko.gui.CustomEnchanting;
import nl.knokko.recipes.ArmorRecipes;

import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.java.JavaPluginLoader;

public class CustomArmor extends JavaPlugin {
	
	private static CustomArmor instance;
	
	private static boolean active;
	
	public static CustomArmor getInstance(){
		if(instance == null)
			throw new IllegalStateException("Can't get instance while plug-in is disabled!");
		return instance;
	}
	
	private Config config;
	private CustomAnvil anvil;
	private CustomEnchanting ench;
	private CommandCustomArmor command;

	public CustomArmor() {}

	public CustomArmor(JavaPluginLoader loader, PluginDescriptionFile description, File dataFolder, File file) {
		super(loader, description, dataFolder, file);
	}
	
	@Override
	public void onEnable(){
		instance = this;
		config = new Config(this);
		anvil = new CustomAnvil(this);
		ench = new CustomEnchanting(this);
		command = new CommandCustomArmor(this);
		Bukkit.getPluginManager().registerEvents(anvil, this);
		Bukkit.getPluginManager().registerEvents(ench, this);
		Bukkit.getPluginManager().registerEvents(command, this);
		getCommand("customarmor").setExecutor(command);
		activate();
	}
	
	public boolean activate(){
		if(active)
			return false;
		ArmorRecipes.enable();
		config.refreshConfig();
		config.initDefaults();
		config.updateArmor();
		active = true;
		return true;
	}
	
	public boolean reload(){
		if(!active)
			return false;
		ArmorRecipes.clearCustomRecipes();
		config.refreshConfig();
		config.initDefaults();
		config.updateArmor();
		return true;
	}
	
	public boolean deactivate(){
		if(!active)
			return false;
		ArmorRecipes.disable();
		active = false;
		return true;
	}
	
	@Override
	public void onDisable(){
		deactivate();
		instance = null;
	}
	
	public Config getArmorConfig(){
		return config;
	}
}
