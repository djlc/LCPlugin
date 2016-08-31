package com.github.djlc;

import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import com.github.djlc.blocks.LevelingTNT;
import com.github.djlc.blocks.PortableChest;
import com.github.djlc.command.Cube;
import com.github.djlc.command.GetItemInfo;
import com.github.djlc.listeners.LevelingTNTListener;
import com.github.djlc.listeners.MyCraftableBlockListener;
import com.github.djlc.listeners.PortableChestListener;
import com.github.djlc.recipes.RecipeListener;
import com.github.djlc.util.DateSign;

import net.milkbowl.vault.economy.Economy;

public final class LCPlugin extends JavaPlugin {

	public static Economy econ = null;

	@Override
	public void onEnable() {

		// Serializable Class
		ConfigurationSerialization.registerClass(LevelingTNT.class);
		ConfigurationSerialization.registerClass(PortableChest.class);

		// Register Listener
		getServer().getPluginManager().registerEvents(new Cube(this), this);
		getServer().getPluginManager().registerEvents(new DateSign(this), this);
		getServer().getPluginManager().registerEvents(new MyCraftableBlockListener(this), this);
		getServer().getPluginManager().registerEvents(new RecipeListener(this), this);
		getServer().getPluginManager().registerEvents(new LevelingTNTListener(), this);
		getServer().getPluginManager().registerEvents(new PortableChestListener(this), this);

		// onCommand
		getCommand("cube").setExecutor(new Cube(this));
		getCommand("iteminfo").setExecutor(new GetItemInfo(this));
		getCommand("date").setExecutor(new DateSign(this));

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
