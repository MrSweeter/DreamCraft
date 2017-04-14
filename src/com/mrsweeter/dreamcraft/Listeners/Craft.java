package com.mrsweeter.dreamcraft.Listeners;

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
		event.getInventory().getMatrix();
		
		if (DreamCraft.blacklist_item.contains(result.toString().toLowerCase()))	{
			event.getInventory().setResult(new ItemStack(Material.AIR));
			
			for (HumanEntity p : event.getViewers())	{
				p.sendMessage(Language.noCraftAllow);
			}
		}
	}
}
