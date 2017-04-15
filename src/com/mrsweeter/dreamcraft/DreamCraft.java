package com.mrsweeter.dreamcraft;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import com.mrsweeter.dreamcraft.Commands.Commands;
import com.mrsweeter.dreamcraft.Config.Loader;
import com.mrsweeter.dreamcraft.Config.PluginConfiguration;
import com.mrsweeter.dreamcraft.Listeners.Craft;
import com.mrsweeter.dreamcraft.Listeners.Enchantment;

public class DreamCraft extends JavaPlugin	{
	
	static Logger log = Logger.getLogger("Minecraft");
	static PluginManager pm = Bukkit.getPluginManager();
	public static String color = "&";
	
	Map<String, PluginConfiguration> configs = new HashMap<String, PluginConfiguration>();
	public static List<String> blacklist_item = new ArrayList<String>();
	public static List<String> blacklist_coloration = new ArrayList<String>();
	public static List<String> blacklist_enchant = new ArrayList<String>();
	public static List<String> blacklist_itemEnchant = new ArrayList<String>();
	public static Map<Character, Material> converter = new HashMap<Character, Material>();
	
	public void onEnable()	{
		configs.put("lang", new PluginConfiguration(this, "lang.yml", "lang.yml", null));
		configs.put("blacklist", new PluginConfiguration(this, "blacklist.yml", "blacklist.yml", null));
		configs.put("converter", new PluginConfiguration(this, "shapeConverter.yml", "shapeConverter.yml", null));
		configs.put("craft", new PluginConfiguration(this, "craft.yml"));
		
		Loader.loadLanguage(configs.get("lang"));
		Loader.loadBlacklist(this, configs.get("blacklist"));
		Loader.loadConverter(configs.get("converter"));
		Loader.loadCraft(this, configs.get("craft"));
		
		pm.registerEvents(new Enchantment(this), this);
		pm.registerEvents(new Craft(this), this);
		
		getCommand("dcrreload").setExecutor(new Commands(this));
		getCommand("dcrhelp").setExecutor(new Commands(this));
		
		log.info(Color.GREEN + "=============== " + Color.YELLOW + "DreamCraft enable" + Color.GREEN + " ===============" + Color.RESET);
		
	}
	
	public void onDisable()	{
		
	}

	public PluginConfiguration getAConfig(String name)	{
		PluginConfiguration pc = configs.get(name);
		if (pc == null)	{
			String file = name + ".yml";
			pc = new PluginConfiguration(this, file);
			configs.put(name, pc);
		}
		return pc;
	}
	
	public Map<String, PluginConfiguration> getAllConfig()	{
		return configs;
	}

}
