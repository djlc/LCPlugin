package com.github.djlc;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.server.PluginDisableEvent;
import org.bukkit.event.server.PluginEnableEvent;
import org.bukkit.inventory.ItemStack;

public class MyCraftableBlockManager implements Listener {

	// Key
	protected static final String KEY = "blocks";

	// プラグイン本体
	protected LCPlugin plugin = null;

	// 位置とブロックのハッシュマップ
	protected static Map<Location, MyCraftableBlock> blockList = new HashMap<>();
	protected static List<MyCraftableBlock> data = new ArrayList<>();

	// コンストラクタ
	public MyCraftableBlockManager(LCPlugin plugin) {
		this.plugin = plugin;
	}

	// データの読み込み
	public void loadData() {

	}

	@EventHandler
	public void onBlockPlace(BlockPlaceEvent event) {
		// 置いたブロックの位置とアイテム情報を取得
		Location location = event.getBlock().getLocation().clone();
		ItemStack item = event.getPlayer().getInventory().getItemInMainHand().clone();
		item.setAmount(1);

		// Pluginで追加されたブロックかどうか？
		for (ItemStack e : CustomItemAndRecipeManager.getItemList()) {
			if (item.equals(e)) {
				// ブロック登録
				event.setCancelled(true);
				MyCraftableBlock m = new MyCraftableBlock(item, location);
				blockList.put(location, m);
				data.add(m);
				event.setCancelled(false);
				return;
			}
		}
	}

	@EventHandler
	public void onBlockBreak(BlockBreakEvent event) {
		// 破壊したブロックの位置を取得
		Location location = event.getBlock().getLocation();
		if (!blockList.containsKey(location)) {
			return;
		}

		// 破壊したブロックをドロップ
		event.setCancelled(true);
		event.getBlock().setType(Material.AIR);
		if (event.getPlayer().getGameMode().equals(GameMode.SURVIVAL)) {
			event.getPlayer().getWorld().dropItemNaturally(location, blockList.get(location).itemStack);
		}

		// ブロック登録解除
		blockList.remove(location);
	}

	@EventHandler
	private void onPluginEnable(PluginEnableEvent event) {
		if (event.getPlugin().equals(plugin)) {
		}
	}

	@EventHandler
	public void onPluginDisable(PluginDisableEvent event) {
		if (event.getPlugin().equals(plugin)) {
			// データを保存
			plugin.getConfig().set(KEY, data);
		}
	}
}
