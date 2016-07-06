package com.github.djlc;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class Rect implements CommandExecutor {

	private final LCPlugin plugin;

	public Rect(LCPlugin plugin) {
		this.plugin = plugin;
	}

	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		return false;
	}
}
