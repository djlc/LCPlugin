package com.github.djlc;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.economy.EconomyResponse;

public final class Tutorial extends JavaPlugin {

	public static Economy econ = null;

	@Override
	public void onEnable() {
		getServer().getPluginManager().registerEvents(new MyListener(), this);
		if (!setupEconomy()) {
			getLogger().info("Disabled due to no Vault dependency found!");
			getServer().getPluginManager().disablePlugin(this);
			return;
		}
		getLogger().info("Plugin has been enabled.");
	}

	@Override
	public void onDisable() {
		getLogger().info("Plugin has been disabled.");
	}

	private boolean setupEconomy() {
		if (getServer().getPluginManager().getPlugin("Vault") == null) return false;
		RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
		if (rsp == null) {
			return false;
		}
		econ = rsp.getProvider();
		return (econ != null);
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
		if (cmd.getName().equalsIgnoreCase("basic")) {
			if (sender instanceof Player) {
				Player player = (Player)sender;
				getLogger().info("Hi! " + ChatColor.AQUA + player.getName());
				return true;
			} else {
				getLogger().info(ChatColor.RED + "Please run this command in game.");
				return false;
			}
		}

		return false;
	}
}
