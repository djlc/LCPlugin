package com.github.djlc.listeners;

import java.io.IOException;
import java.util.Arrays;
import java.util.UUID;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Chest;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.server.PluginDisableEvent;
import org.bukkit.event.server.PluginEnableEvent;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.github.djlc.LCPlugin;
import com.github.djlc.blocks.PortableChest;
import com.github.djlc.lists.MyCraftableBlockList;
import com.github.djlc.lists.PortableChestItemList;

public class PortableChestListener implements Listener {

	// プラグイン本体
	private LCPlugin plugin = null;

	public PortableChestListener(LCPlugin plugin) {
		this.plugin = plugin;
	}

	@EventHandler
	public void onBlockPlace(BlockPlaceEvent event) {
		// 置いたブロックの位置とアイテム情報を取得
		Location location = event.getBlock().getLocation().clone();
		ItemStack item = event.getPlayer().getInventory().getItemInMainHand().clone();
		item.setAmount(1);

		// Pluginで追加されたブロックかどうか？
		if (PortableChest.isPortableChest(item)) {
			// ブロック登録
			event.setCancelled(true);
			if (item.getItemMeta().hasLore()) {
				MyCraftableBlockList.add(new PortableChest(item, location, item.getItemMeta().getLore().get(0)));
				PortableChestItemList.remove(item.getItemMeta().getLore().get(0));
			} else {
				MyCraftableBlockList.add(new PortableChest(item, location));
			}
			event.setCancelled(false);
		}
	}

	@EventHandler
	public void onBlockBreak(BlockBreakEvent event) {
		// 破壊したブロックの位置を取得
		Location location = event.getBlock().getLocation();

		// ブロックの取得
		PortableChest block = null;
		if (MyCraftableBlockList.get(location) instanceof PortableChest) {
			block = (PortableChest) MyCraftableBlockList.get(location);
		} else {
			return;
		}

		// アイテムの取得
		ItemStack itemStack = block.getItemStack();

		// メタデータの取得
		ItemMeta itemMeta = itemStack.getItemMeta();

		// UUIDが存在しないなら, ドロップしたアイテムの一意性を保証するためのUUIDの取得
		if (!itemMeta.hasLore()) {
			UUID id = UUID.randomUUID();
			// UUIDのセット
			itemMeta.setLore(Arrays.asList(id.toString()));
			itemStack.setItemMeta(itemMeta);
			// リストに追加
			PortableChestItemList.put(id.toString(), block.getInventory());
		} else {
			PortableChestItemList.put(itemMeta.getLore().get(0), block.getInventory());
		}

		// 破壊したブロックをドロップ
		event.setCancelled(true);
		event.getBlock().setType(Material.AIR);
		event.getPlayer().getWorld().dropItemNaturally(location, itemStack);

		// ブロック登録解除
		MyCraftableBlockList.remove(location);
	}

	@EventHandler
	public void onChestOpen(InventoryOpenEvent event) {
		InventoryHolder ih = event.getInventory().getHolder();
		Location location = null;
		if (ih instanceof Chest) {
			location = ((Chest) ih).getLocation();
			// インベントリを開く
			if (MyCraftableBlockList.contains(location)) {
				event.setCancelled(true);
				event.getPlayer().getWorld().playSound(location, Sound.BLOCK_CHEST_OPEN, 1, 1);
				event.getPlayer().openInventory(((PortableChest) MyCraftableBlockList.get(location)).getInventory());
			}
		}
	}

	@EventHandler
	private void onPluginEnable(PluginEnableEvent event) throws IOException {
		if (event.getPlugin().equals(plugin)) {
			PortableChestItemList.loadData(plugin);
		}
	}

	@EventHandler
	public void onPluginDisable(PluginDisableEvent event) {
		if (event.getPlugin().equals(plugin)) {
			PortableChestItemList.saveData(plugin);
		}
	}
}
