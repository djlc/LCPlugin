package com.github.djlc;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.Location;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.inventory.ItemStack;

public class MyCraftableBlock implements ConfigurationSerializable {

	// Key
	private static final String LOCATON = "location";
	private static final String ITEMSTACK = "item";

	// ブロックの座標
	protected Location location = null;

	// ブロックのItemStack
	protected ItemStack itemStack = null;

	// コンストラクタ
	public MyCraftableBlock(ItemStack itemStack, Location location) {
		this.itemStack = itemStack;
		this.location = location;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof MyCraftableBlock) {
			MyCraftableBlock m = (MyCraftableBlock) obj;

			// itemStackとLocationが一致すればtrueを返す
			return (m.itemStack.equals(this.itemStack) && m.location.equals(this.location));
		}
		return false;
	}

	@Override
	public Map<String, Object> serialize() {
		Map<String, Object> map = new HashMap<>();
		map.put(LOCATON, new SerializableLocation(location));
		map.put(ITEMSTACK, itemStack);
		return map;
	}

	public static MyCraftableBlock deserialize(Map<String, Object> map) {
		SerializableLocation loc = (SerializableLocation) map.get(LOCATON);
		ItemStack item = (ItemStack) map.get(ITEMSTACK);
		MyCraftableBlock instance = new MyCraftableBlock(item, loc.getLocation());
		return instance;
	}
}
