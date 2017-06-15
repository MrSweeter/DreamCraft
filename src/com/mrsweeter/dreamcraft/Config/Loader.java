package com.mrsweeter.dreamcraft.Config;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.FurnaceRecipe;
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
		DreamCraft.blacklist_coloration.clear();
		DreamCraft.blacklist_enchant.clear();
		DreamCraft.blacklist_itemEnchant.clear();
		pl.getServer().resetRecipes();
		
		List<String> list;
		
		if (config.isSet("item-craft"))	{
			list = config.getStringList("item-craft");
			for (String str : list)	{
				DreamCraft.blacklist_item.add(str.toLowerCase());
			}
		}
		
		if (config.isSet("item-incompatible"))	{
			list = config.getStringList("item-incompatible");
			for (String str : list)	{
				DreamCraft.blacklist_coloration.add(str.toLowerCase());
			}
		}
		
		if (config.isSet("item-enchant"))	{
			list = config.getStringList("item-enchant");
			for (String str : list)	{
				DreamCraft.blacklist_itemEnchant.add(str.toLowerCase());
			}
		}
		if (config.isSet("item-modify"))	{
			list = config.getStringList("item-modify");
			for (String str : list)	{
				DreamCraft.blacklist_itemAnvil.add(str.toLowerCase());
			}
		}
		
		if (config.isSet("enchant"))	{
			list = config.getStringList("enchant");
			for (String str : list)	{
				DreamCraft.blacklist_enchant.add(str.toLowerCase());
			}
		}
		
		if (config.isSet("item-recipe"))	{
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
	}
	
	public static void loadLanguage(PluginConfiguration config)	{
		
		DreamCraft.color = config.getString("color");
		Language.noCraftAllow = config.getString("noCraftAllow").replace(DreamCraft.color, "§");		
		Language.noPerm = config.getString("noPerm").replace(DreamCraft.color, "§");		
		Language.reload = config.getString("reload").replace(DreamCraft.color, "§");
		Language.incompatibleItem = config.getString("incompatibleItem").replace(DreamCraft.color, "§");
		Language.noEnchant = config.getString("forbiddenEnchant").replace(DreamCraft.color, "§");
		
	}

	@SuppressWarnings("deprecation")
	public static void loadCraft(DreamCraft pl, PluginConfiguration config) {
		
		for (String key :  config.getKeys(false))	{
			
			//DreamCraft.log.info("Creation of item " + key);
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
				int i = 0;
				for (String recipe : craft.getKeys(false))	{
					
					ShapelessRecipe shape;
					
					if (Bukkit.getServer().getVersion().contains("1.12"))	{
						shape = new ShapelessRecipe(new NamespacedKey(pl, key+"shapeless"+i), item);
					} else {
						shape = new ShapelessRecipe(item);
					}
					List<String> ingredients = craft.getStringList(recipe);
					
					for (String str : ingredients)	{
						shape.addIngredient(Material.getMaterial(str.toUpperCase()));
					}
					pl.getServer().addRecipe(shape);
					i++;
				}
				//DreamCraft.log.info("Craft shapeless complete");
			}
			if (section.isSet("shape"))	{
				ConfigurationSection shapes = section.getConfigurationSection("shape");
				int i = 0;;
				for (String recipe : shapes.getKeys(false))	{
					ShapedRecipe shape;
					if (Bukkit.getServer().getVersion().contains("1.12"))	{
						shape = new ShapedRecipe(new NamespacedKey(pl, key+"shape"+i), item);
					} else {
						shape = new ShapedRecipe(item);
					}
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
					i++;
					//DreamCraft.log.info("Craft with shape complete");
				}
			}
			if (section.isSet("furnace"))	{
				ConfigurationSection furnace = section.getConfigurationSection("furnace");
				
				List<String> toCook = furnace.getStringList("to-cook");
				
				for (String str : toCook)	{
					
					Material source = Material.getMaterial(str.toUpperCase());
					FurnaceRecipe recipe = new FurnaceRecipe(item, source);
					pl.getServer().addRecipe(recipe);
				}
				//DreamCraft.log.info("Furnace recipe complete");
			}
		}
		//DreamCraft.log.info("craft.yml loaded");
	}

	public static void loadConverter(PluginConfiguration config) {
		
		DreamCraft.converter.clear();
		for (String str : config.getKeys(false))	{
			if (str.length() == 1)	{
				char c = str.charAt(0);
				DreamCraft.converter.put(c, Material.getMaterial(config.getString(str).toUpperCase()));
			}
		}
	}
}
