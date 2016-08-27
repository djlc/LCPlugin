package com.github.djlc.lists;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.bukkit.Location;

import com.github.djlc.LCPlugin;
import com.github.djlc.blocks.MyCraftableBlock;

public class MyCraftableBlockList {

	// Key
	private static final String KEY = "blocks";

	// 位置とブロックのハッシュマップ
	private static Map<Location, MyCraftableBlock> blockList = new HashMap<>();

	// ブロックの追加
	public static void add(MyCraftableBlock block) {
		blockList.put(block.getLocation(), block);
	}

	// ブロックの削除
	public static void remove(Location location) {
		blockList.remove(location);
	}

	// ブロックが存在するか
	public static boolean contains(Location location) {
		return blockList.containsKey(location);
	}

	// ブロックの取得
	public static MyCraftableBlock get(Location location) {
		return blockList.get(location);
	}

	// 最短距離にあるブロックの座標を取得
	public static Location getNearestLocation(Location location) {
		Location nearest = location;
		double d = Double.MAX_VALUE;
		if (blockList.isEmpty()) return null;
		
		for (Location l : blockList.keySet()) {
			if (d > l.distance(location)) {
				d = l.distance(location);
				nearest = l;
			}
		}
		return nearest;
	}

	// データの読み込み
	@SuppressWarnings("unchecked")
	public static void loadData(LCPlugin plugin) {
		List<MyCraftableBlock> data = (List<MyCraftableBlock>) plugin.getConfig().getList(KEY);
		if (data != null) {
			for (MyCraftableBlock e : data) {
				if (e.getItemStack().getType().equals(e.getLocation().getBlock().getType())) {
					blockList.put(e.getLocation(), e);
				}
			}

			System.out.println("block loaded.");
		}
	}

	// データの保存
	public static void saveData(LCPlugin plugin) {

		// Listに入れて保存する
		List<MyCraftableBlock> data = new ArrayList<>();
		for (MyCraftableBlock e : blockList.values()) {
			if (e.getItemStack().getType().equals(e.getLocation().getBlock().getType())) {
				data.add(e);
			}
		}
		plugin.getConfig().set(KEY, data);

		System.out.println("block saved.");
	}

	// blockListのKey(Location)
	public static Set<Location> keySet() {
		return blockList.keySet();
	}

	// blockListのValue(MyCraftableBlock)
	public static Collection<MyCraftableBlock> values() {
		return blockList.values();
	}

}
