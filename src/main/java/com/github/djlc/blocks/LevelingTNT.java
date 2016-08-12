package com.github.djlc.blocks;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.Location;
import org.bukkit.inventory.ItemStack;

import com.github.djlc.util.SerializableLocation;

public class LevelingTNT extends MyCraftableBlock {

	// Key
	private static final String RADIUS = "radius";

	// 半径
	private int radius = 0;

	public LevelingTNT(ItemStack itemStack, Location location, int radius) {
		super(itemStack, location);
		this.setRadius(radius);
	}

	public int getRadius() {
		return radius;
	}

	public void setRadius(int radius) {
		this.radius = radius;
	}

	@Override
	public Map<String, Object> serialize() {
		Map<String, Object> map = new HashMap<>();
		map.put(LOCATON, new SerializableLocation(location));
		map.put(ITEMSTACK, itemStack);
		map.put(RADIUS, radius);
		return map;
	}

	public static LevelingTNT deserialize(Map<String, Object> map) {
		SerializableLocation loc = (SerializableLocation) map.get(LOCATON);
		ItemStack item = (ItemStack) map.get(ITEMSTACK);
		int rad = (int) map.get(RADIUS);
		LevelingTNT instance = new LevelingTNT(item, loc.getLocation(), rad);
		return instance;
	}
}
