package com.mrsweeter.dreamcraft.Listeners;

import java.util.List;
import java.util.Map;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.AnvilInventory;
import org.bukkit.inventory.ItemStack;

import com.mrsweeter.dreamcraft.DreamCraft;
import com.mrsweeter.dreamcraft.Language;

public class Anvil implements Listener	{
	
	DreamCraft pl;
	
	public Anvil(DreamCraft pl)	{
		this.pl = pl;
	}
	
	@EventHandler
	public void onClickAnvil(InventoryClickEvent event)	{
		
		List<String> enchants = null;
		Player p = (Player) event.getWhoClicked();
		// 2 Event in same time, lose xp and clickInv, p.getLevel() return the level before the 2 events --> Level lose cancel
		
		if (event.getCurrentItem() instanceof ItemStack)	{
			
			Map<org.bukkit.enchantments.Enchantment, Integer> enchant = event.getCurrentItem().getEnchantments();
			enchants = Enchantment.loadStringEnchant(enchant);
			enchants.retainAll(DreamCraft.blacklist_enchant);
		}
		
		if (event.getClickedInventory() instanceof AnvilInventory && event.getRawSlot() == 2)	{
			
			ItemStack[] items = ((AnvilInventory) event.getInventory()).getContents();
			
			if (items[0] != null)	{
				
				if (DreamCraft.blacklist_itemAnvil.contains(items[0].getType().toString().toLowerCase()))	{
					
					
					event.getView().getPlayer().sendMessage(Language.noMergeAnvil);
					p.setLevel(p.getLevel());
					event.setCancelled(true);
					
				} else if (enchants != null)	{
					
					if (items[1] != null && DreamCraft.blacklist_itemEnchant.contains(items[0].getType().toString().toLowerCase()))	{
						
						event.getView().getPlayer().sendMessage(Language.noEnchantItem);
						p.setLevel(p.getLevel());
						event.setCancelled(true);
						
					} else if (items[1] != null && enchants.size() > 0)	{
						
						event.getView().getPlayer().sendMessage(Language.noEnchant + " [§7" + enchants.get(0) + "§c]");
						p.setLevel(p.getLevel());
						event.setCancelled(true);
					}
				}
			}
		}
	}
}
