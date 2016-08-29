package com.github.djlc.blocks;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.inventory.ItemStack;

import com.github.djlc.base64.ItemStackToBase64;
import com.github.djlc.base64.LocationToBase64;
import com.github.djlc.util.SerializableLocation;

@SuppressWarnings("serial")
public class LevelingTNT extends MyCraftableBlock {

	// アイテム名
	private static final List<String> LEVELING_TNT = Arrays.asList("Leveling TNT MK1 (3x3x3)",
			"Leveling TNT MK2 (5x5x5)", ChatColor.AQUA + "Leveling TNT MK3 (9x9x9)",
			ChatColor.RED + "Leveling TNT MK4 (15x15x15)");

	// ブロックの種類
	private int blockType;

	// TNTの判別用のMap
	private static final Map<String, Integer> mapType = new HashMap<String, Integer>() {
		{
			for (int i = 0; i < LEVELING_TNT.size(); i++) {
				put(LEVELING_TNT.get(i), i);
			}
		}
	};

	// TNTの種類別の爆破半径
	private static final Map<Integer, Integer> mapRadius = new HashMap<Integer, Integer>() {
		{
			put(0, 1);
			put(1, 2);
			put(2, 4);
			put(3, 7);
		}
	};

	public LevelingTNT(ItemStack itemStack, Location location) {
		super(itemStack, location);
		blockType = mapType.get(itemStack.getItemMeta().getDisplayName());
	}

	@Override
	public Map<String, Object> serialize() {
		Map<String, Object> map = new HashMap<>();
		map.put(LOCATON, LocationToBase64.encode(location));
		map.put(ITEMSTACK, ItemStackToBase64.encode(itemStack));
		return map;
	}

	public static LevelingTNT deserialize(Map<String, Object> map) throws IOException {
		ItemStack itemStack = ItemStackToBase64.decode((String) map.get(ITEMSTACK));
		Location location = LocationToBase64.decode((String) map.get(LOCATON));
		return new LevelingTNT(itemStack, location);
	}

	public static boolean isLevelingTNT(ItemStack item) {
		return LEVELING_TNT.contains(item.getItemMeta().getDisplayName());
	}

	public int getRadius() {
		return mapRadius.get(this.blockType);
	}
}
