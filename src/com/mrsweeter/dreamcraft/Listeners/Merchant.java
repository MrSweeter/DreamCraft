package com.mrsweeter.dreamcraft.Listeners;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Villager;
import org.bukkit.entity.Villager.Profession;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.MerchantRecipe;

import com.mrsweeter.dreamcraft.DreamCraft;
import com.mrsweeter.dreamcraft.Config.PluginConfiguration;

public class Merchant implements Listener	{
	
	DreamCraft pl;
	PluginConfiguration config;
	
	public Merchant(DreamCraft main)	{
		pl = main;
		config = pl.getAConfig("craft");
	}
	
	@EventHandler
	public void onInteractWithMerchant(PlayerInteractEntityEvent event)	{
		
		if (event.getRightClicked() instanceof Villager)	{
			Villager villager = (Villager) event.getRightClicked();
			if (villager.getProfession() == Profession.NITWIT)	{
				
				
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
					
					if (section.isSet("villager"))	{
						ConfigurationSection pnj = section.getConfigurationSection("villager");
						List<MerchantRecipe> recipes = new ArrayList<MerchantRecipe>();
						
						for (String key2 : pnj.getKeys(false))	{
							
							List<String> ingredients = pnj.getStringList(key2);
							if (ingredients.size() == 2 || ingredients.size() == 1)	{
								
								MerchantRecipe recipe = new MerchantRecipe(item, 1000);
								for (String str : ingredients)	{
									recipe.addIngredient(new ItemStack(Material.getMaterial(str.toUpperCase())));
								}
								
								recipes.add(recipe);
							}
							
						}
						villager.setRecipes(recipes);
					}
				}
			}
		}
	}
}
