package com.github.djlc.lists;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.bukkit.inventory.Inventory;

import com.github.djlc.LCPlugin;
import com.github.djlc.base64.InventoryToBase64;

public class PortableChestItemList {
	
	private static final String KEY = "items";
	
	private static Map<String, Inventory> map = new HashMap<>();
	
	public static Inventory get(String id) {
		return map.get(id);
	}
	
	public static void put(String id, Inventory inv) {
		map.put(id, inv);
	}
	
	public static void remove(String id) {
		map.remove(id);
	}
	
	public static boolean contains(String id) {
		return map.containsKey(id);
	}
	
	// データの読み込み
	@SuppressWarnings("unchecked")
	public static void loadData(LCPlugin plugin) throws IOException {
		Map<String, String> data = (Map<String, String>)(plugin.getConfig().getMapList(KEY).get(0));
		if (!data.isEmpty()) {
			for (Map.Entry<String, String> e : data.entrySet()) {
				map.put(e.getKey(), InventoryToBase64.decode(e.getValue()));
			}
			System.out.println("portable chest item data loaded.");
		}
	}

	// データの保存
	public static void saveData(LCPlugin plugin) {
		Map<String, String> serializedMap = new HashMap<>();
		for (Map.Entry<String, Inventory> e : map.entrySet()) {
			serializedMap.put(e.getKey(), InventoryToBase64.encode(e.getValue()));
		}
		plugin.getConfig().set(KEY, Arrays.asList(serializedMap));
		System.out.println("portable chest item data saved.");
	}
}
