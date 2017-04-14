package com.mrsweeter.dreamcraft.Listeners;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.inventory.ItemStack;

import com.mrsweeter.dreamcraft.DreamCraft;
import com.mrsweeter.dreamcraft.Language;

public class Craft implements Listener {
	
	DreamCraft pl;
	
	public Craft(DreamCraft pl)	{
		this.pl = pl;
	}
	
	@EventHandler
	public void onCraft(PrepareItemCraftEvent event)	{
		
		Material result = event.getRecipe().getResult().getType();
		List<String> ingredients = loadStringIngredient(Arrays.asList(event.getInventory().getContents()));
		ingredients.retainAll(DreamCraft.blacklist_coloration);
		
		if (DreamCraft.blacklist_item.contains(result.toString().toLowerCase()))	{
			event.getInventory().setResult(new ItemStack(Material.AIR));
			
			for (HumanEntity p : event.getViewers())	{
				p.sendMessage(Language.noCraftAllow);
			}
		} else if (ingredients.size() >= 2)	{
			event.getInventory().setResult(new ItemStack(Material.AIR));
			
			for (HumanEntity p : event.getViewers())	{
				p.sendMessage(Language.incompatibleItem);
			}
		}
	}
	
	private static List<String> loadStringIngredient(List<ItemStack> list)	{
		
		List<String> l = new ArrayList<String>();
		for (ItemStack i : list)	{
			l.add(i.getType().toString().toLowerCase());
		}
		return l;
	}
}
