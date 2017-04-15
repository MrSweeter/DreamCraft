package com.mrsweeter.dreamcraft.Listeners;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.enchantment.EnchantItemEvent;
import org.bukkit.event.enchantment.PrepareItemEnchantEvent;

import com.mrsweeter.dreamcraft.DreamCraft;
import com.mrsweeter.dreamcraft.Language;

public class Enchantment implements Listener	{
	
DreamCraft pl;
	
	public Enchantment(DreamCraft pl)	{
		this.pl = pl;
	}
	
	@EventHandler
	public void onPrepareEnchant(PrepareItemEnchantEvent event)	{
		
		Material mat = event.getItem().getType();
		
		if (DreamCraft.blacklist_itemEnchant.contains(mat.toString().toLowerCase()))	{
			
			event.setCancelled(true);
		}
	}
	
	@EventHandler
	public void onEnchant(EnchantItemEvent event)	{
		
		Map<org.bukkit.enchantments.Enchantment, Integer> enchant = event.getEnchantsToAdd();
		List<String> enchants = loadStringEnchant(enchant);
		enchants.retainAll(DreamCraft.blacklist_enchant);
		
		if (enchants.size() > 0)	{
			event.setCancelled(true);
			event.getEnchanter().sendMessage(Language.noEnchant + " [" + enchants.get(0) + "]");
		}
	}
	
	private static List<String> loadStringEnchant(Map<org.bukkit.enchantments.Enchantment, Integer> list)	{
		
		List<String> l = new ArrayList<String>();
		
		for (org.bukkit.enchantments.Enchantment i : list.keySet())	{
			l.add(i.getName().toString().toLowerCase());
		}
		return l;
	}
}
