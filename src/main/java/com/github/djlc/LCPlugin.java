package com.github.djlc;

import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import net.milkbowl.vault.economy.Economy;

public final class LCPlugin extends JavaPlugin {

	public static Economy econ = null;

	@Override
	public void onEnable() {

		// Serializable Class
		ConfigurationSerialization.registerClass(MyRecipe.class);

		// Register Listener
		getServer().getPluginManager().registerEvents(new Cube(this), this);
		getServer().getPluginManager().registerEvents(new Date(this), this);
		getServer().getPluginManager().registerEvents(new MyRecipeManager(this), this);
		//getServer().getPluginManager().registerEvents(new Backpack(), this);

		// onCommand
		getCommand("cube").setExecutor(new Cube(this));
		getCommand("iteminfo").setExecutor(new GetItemInfo(this));
		getCommand("date").setExecutor(new Date(this));

		// VaultAPI
		if (!setupEconomy()) {
			getLogger().info("Disabled due to no Vault dependency found!");
			getServer().getPluginManager().disablePlugin(this);
			return;
		}

		// Enable
		getLogger().info("Plugin has been enabled.");
	}

	@Override
	public void onDisable() {
		// config.yml にシリアライズされたデータを保存する
		saveConfig();
		// Disable
		getLogger().info("Plugin has been disabled.");
	}

	private boolean setupEconomy() {
		if (getServer().getPluginManager().getPlugin("Vault") == null)
			return false;
		RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
		if (rsp == null) {
			return false;
		}
		econ = rsp.getProvider();
		return (econ != null);
	}

}
