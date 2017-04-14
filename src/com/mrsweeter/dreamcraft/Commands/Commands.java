package com.mrsweeter.dreamcraft.Commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import com.mrsweeter.dreamcraft.DreamCraft;
import com.mrsweeter.dreamcraft.Language;
import com.mrsweeter.dreamcraft.Config.Loader;

public class Commands implements CommandExecutor	{
	
	private DreamCraft pl;

	public Commands(DreamCraft dreamHelper) {
		pl = dreamHelper;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String commandLabel, String[] args) {
		
		if (sender.hasPermission(command.getPermission()))	{
			commandLabel = command.getLabel();
			switch (commandLabel)	{
			case "dcrreload":
				
				Loader.loadAllConfig(pl.getAllConfig());
				Loader.loadLanguage(pl.getAConfig("lang"));
				Loader.loadBlacklist(pl, pl.getAConfig("blacklist"));
				Loader.loadConverter(pl.getAConfig("converter"));
				Loader.loadCraft(pl, pl.getAConfig("craft"));
				sender.sendMessage("§c[§aDreamCraft§c] " + Language.reload);
				
				break;
			}
		} else {
			sender.sendMessage(Language.noPerm);
		}
		return true;
	}
}
