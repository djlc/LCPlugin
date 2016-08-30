package com.github.djlc.listeners;

import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.block.Chest;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

import com.github.djlc.blocks.PortableChest;
import com.github.djlc.lists.MyCraftableBlockList;

public class PortableChestListener implements Listener {

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
			MyCraftableBlockList.add(new PortableChest(item, location));
			event.setCancelled(false);
		}
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
}
