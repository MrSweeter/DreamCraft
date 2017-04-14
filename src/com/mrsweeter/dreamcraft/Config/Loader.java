package com.mrsweeter.dreamcraft.Config;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.ShapelessRecipe;

import com.mrsweeter.dreamcraft.DreamCraft;
import com.mrsweeter.dreamcraft.Language;

public class Loader {
	
public static void loadAllConfig(Map<String, PluginConfiguration> configs)	{
		
		for (String str : configs.keySet()){
			configs.get(str).reload();
		}
	}

	public static void loadBlacklist(DreamCraft pl, PluginConfiguration config) {
		
		DreamCraft.blacklist_item.clear();
		DreamCraft.blacklist_craft.clear();
		
		List<String> list = config.getStringList("item-craft");
		for (String str : list)	{
			DreamCraft.blacklist_item.add(str.toLowerCase());
		}
		
		list = config.getStringList("item-recipe");
		for (String str : list)	{
			
			Material mat = Material.getMaterial(str.toUpperCase());
			
			Iterator<Recipe> it = pl.getServer().recipeIterator();
			Recipe recipe;
			while(it.hasNext())	{
				recipe = it.next();
				if (recipe != null && recipe.getResult().getType() == mat)	{
					it.remove();
				}
			}
		}
	}
	
	public static void loadLanguage(PluginConfiguration config)	{
		
		DreamCraft.color = config.getString("color");
		Language.noCraftAllow = config.getString("noCraftAllow").replace(DreamCraft.color, "§");		
		Language.noPerm = config.getString("noPerm").replace(DreamCraft.color, "§");		
		Language.reload = config.getString("reload").replace(DreamCraft.color, "§");
		
	}

	public static void loadCraft(DreamCraft pl, PluginConfiguration config) {
		
		for (String key :  config.getKeys(false))	{
			
			ConfigurationSection section = config.getConfigurationSection(key);
			byte b = 0;
			int quantity = 1;
			if (section.isSet("quantity"))	{
				quantity = section.getInt("quantity");
			}
			if (section.isSet("data"))	{
				b = (byte) section.getInt("data");
			}
			ItemStack item = new ItemStack(Material.getMaterial(section.getString("item").toUpperCase()), quantity, b);
			
			if (section.isSet("craft"))	{
				ConfigurationSection craft = section.getConfigurationSection("craft");
				
				for (String recipe : craft.getKeys(false))	{
					
					ShapelessRecipe shape = new ShapelessRecipe(item);
					List<String> ingredients = craft.getStringList(recipe);
					
					for (String str : ingredients)	{
						shape.addIngredient(Material.getMaterial(str.toUpperCase()));
					}
					pl.getServer().addRecipe(shape);
				}
			}
			if (section.isSet("shape"))	{
				ConfigurationSection shapes = section.getConfigurationSection("shape");
				
				for (String recipe : shapes.getKeys(false))	{
					
					ShapedRecipe shape = new ShapedRecipe(item);
					List<String> shapeChar = shapes.getStringList(recipe);
					
					if (shapeChar.size() == 3)	{
						
						shape.shape(shapeChar.get(0), shapeChar.get(1), shapeChar.get(2));
						String block = shapeChar.get(0) + shapeChar.get(1) + shapeChar.get(2);
						
						for (char c : DreamCraft.converter.keySet())	{
							if (block.indexOf(c) != -1)	{
								shape.setIngredient(c, DreamCraft.converter.get(c));
							}
						}
						pl.getServer().addRecipe(shape);
					}
				}
			}
		}
	}

	public static void loadConverter(PluginConfiguration config) {
		
		for (String str : config.getKeys(false))	{
			if (str.length() == 1)	{
				char c = str.charAt(0);
				DreamCraft.converter.put(c, Material.getMaterial(config.getString(str).toUpperCase()));
			}
		}
	}
}
