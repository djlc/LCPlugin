package com.github.djlc.listeners;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.ExplosionPrimeEvent;
import org.bukkit.inventory.ItemStack;

import com.github.djlc.blocks.LevelingTNT;
import com.github.djlc.lists.MyCraftableBlockList;

public class LevelingTNTListener implements Listener {

	@EventHandler
	public void onBlockPlace(BlockPlaceEvent event) {
		// 置いたブロックの位置とアイテム情報を取得
		Location location = event.getBlock().getLocation().clone();
		ItemStack item = event.getPlayer().getInventory().getItemInMainHand().clone();
		item.setAmount(1);

		// Pluginで追加されたブロックかどうか？
		if (LevelingTNT.isLevelingTNT(item)) {
			// ブロック登録
			event.setCancelled(true);
			MyCraftableBlockList.add(new LevelingTNT(item, location));
			event.setCancelled(false);
		}
	}

	// エンティティが爆発するときに呼び出される
	@EventHandler
	private void blockExplode(ExplosionPrimeEvent event) {
		// Entityを取得
		Entity entity = event.getEntity();

		// それがTNTかどうか
		if (!(entity instanceof TNTPrimed)) {
			return;
		}
		TNTPrimed tnt = (TNTPrimed) entity;

		// ブロックリストの中で最短距離の位置にあるブロックを取得
		Location location = MyCraftableBlockList.getNearestLocation(tnt.getLocation());

		if (location == null || tnt.getLocation().distance(location) >= 1) {
			return;
		}

		// デフォルト爆発を無効化
		event.setCancelled(true);

		// 立方体型に爆破
		explosion(tnt, (LevelingTNT) MyCraftableBlockList.get(location.clone()));

		// 登録解除
		MyCraftableBlockList.remove(location);
	}

	// num*2+1の立方体の空洞を作る
	private void explosion(TNTPrimed tnt, LevelingTNT levelingTNT) {
		// 位置
		Location location = levelingTNT.getLocation().clone();
		// 半径
		int radius = levelingTNT.getRadius();
		// 座標を立方体の頂点に移動
		location.subtract(radius, radius, radius);

		// 1辺の長さ
		int n = radius * 2 + 1;

		// 爆破音を鳴らす
		tnt.getWorld().playSound(location, Sound.ENTITY_GENERIC_EXPLODE, 1, 1);

		// 範囲内のブロックをドロップさせる
		for (int i = 0; i < n; i++) {
			for (int j = 0; j < n; j++) {
				for (int k = 0; k < n; k++) {
					// 岩盤は破壊しない
					if (!location.getBlock().getType().equals(Material.BEDROCK)) {
						// LevelingTNTが存在すれば連鎖させる
						if (location.getBlock().getType().equals(Material.TNT)) {
							location.getBlock().setType(Material.AIR);
							explosion(tnt, (LevelingTNT) MyCraftableBlockList.get(location.clone()));
							MyCraftableBlockList.remove(location);
						}
						location.getBlock().breakNaturally();
					}
					location.setX(location.getX() + 1.0);
				}
				location.setX(location.getX() - n);
				location.setY(location.getY() + 1.0);
			}
			location.setY(location.getY() - n);
			location.setZ(location.getZ() + 1.0);
		}
	}
}
