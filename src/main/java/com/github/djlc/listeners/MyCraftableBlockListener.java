package com.github.djlc.listeners;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
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
	public void onBlockBreak(BlockBreakEvent event) {
		// 破壊したブロックの位置を取得
		Location location = event.getBlock().getLocation();
		if (!MyCraftableBlockList.contains(location)) {
			return;
		}

		// 破壊したブロックをドロップ
		event.setCancelled(true);
		event.getBlock().setType(Material.AIR);
		event.getPlayer().getWorld().dropItemNaturally(location, MyCraftableBlockList.get(location).getItemStack());

		// ブロック登録解除
		MyCraftableBlockList.remove(location);
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
