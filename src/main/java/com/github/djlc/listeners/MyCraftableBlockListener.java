package com.github.djlc.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.server.PluginDisableEvent;
import org.bukkit.event.server.PluginEnableEvent;

import com.github.djlc.LCPlugin;
import com.github.djlc.lists.MyCraftableBlockList;

public class MyCraftableBlockListener implements Listener {

	// プラグイン本体
	private LCPlugin plugin = null;

	// コンストラクタ
	public MyCraftableBlockListener(LCPlugin plugin) {
		this.plugin = plugin;
	}

	@EventHandler
	private void onPluginEnable(PluginEnableEvent event) {
		if (event.getPlugin().equals(plugin)) {
			MyCraftableBlockList.loadData(plugin);
		}
	}

	@EventHandler
	public void onPluginDisable(PluginDisableEvent event) {
		if (event.getPlugin().equals(plugin)) {
			MyCraftableBlockList.saveData(plugin);
		}
	}
}
