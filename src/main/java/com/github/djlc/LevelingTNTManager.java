package com.github.djlc;

import java.util.Iterator;
import java.util.Map;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.ExplosionPrimeEvent;

public class LevelingTNTManager extends MyCraftableBlockManager {

	public LevelingTNTManager(LCPlugin plugin) {
		super(plugin);
	}

	@EventHandler
	private void blockExplode(ExplosionPrimeEvent event) {
		// Entityを取得
		Entity entity = event.getEntity();

		// それがTNTかどうか
		if (!(entity instanceof TNTPrimed)) {
			return;
		}
		TNTPrimed tnt = (TNTPrimed) entity;

		// 爆破位置の調整
		Location location = tnt.getLocation();
		double distance = 2.0;
		for (Map.Entry<Location, MyCraftableBlock> e : blockList.entrySet()) {
			Location l = e.getKey();
			if (e.getValue() instanceof LevelingTNT && l.distance(location) < distance) {
				distance = l.distance(location);
				location = l.clone();
				distance = l.distance(location);
			}
		}
		if (!blockList.containsKey(location)) {
			return;
		}

		// デフォルト爆発を無効化
		event.setCancelled(true);

		// 立方体型に爆破
		tnt.getWorld().playSound(location, Sound.ENTITY_GENERIC_EXPLODE, 1, 1);
		explosion((LevelingTNT) blockList.get(location));

		// 登録解除
		blockList.remove(location);
		refleshList();
	}

	// リストに残っているが実際には存在しないブロックをリストから除去する
	private void refleshList() {
		for (Iterator<Location> i = blockList.keySet().iterator(); i.hasNext();) {
			if (!i.next().getBlock().getType().equals(Material.TNT)) {
				i.remove();
			}
		}
	}

	// num*2+1の立方体の空洞を作る
	private void explosion(LevelingTNT tnt) {
		// 位置
		Location location = tnt.location;
		// 半径
		int radius = tnt.getRadius();

		// 座標を立方体の頂点に移動
		location.subtract(radius, radius, radius);

		// 1辺の長さ
		int n = radius * 2 + 1;

		// 範囲内のブロックをドロップさせる
		for (int i = 0; i < n; i++) {
			for (int j = 0; j < n; j++) {
				for (int k = 0; k < n; k++) {
					// 岩盤は破壊しない
					if (!location.getBlock().getType().equals(Material.BEDROCK)) {
						// LevelingTNTが存在すれば連鎖させる
						if (location.getBlock().getType().equals(Material.TNT)) {
							location.getBlock().setType(Material.AIR);
							explosion((LevelingTNT) blockList.get(location));
							blockList.remove(location);
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
