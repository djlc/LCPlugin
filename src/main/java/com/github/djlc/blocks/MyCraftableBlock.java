package com.github.djlc.blocks;

import org.bukkit.Location;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.inventory.ItemStack;

public abstract class MyCraftableBlock implements ConfigurationSerializable {

	// Key
	protected static final String LOCATON = "location";
	protected static final String ITEMSTACK = "item";

	// ブロックの座標
	protected Location location = null;

	// ブロックのItemStack
	protected ItemStack itemStack = null;

	// コンストラクタ
	public MyCraftableBlock(ItemStack itemStack, Location location) {
		this.itemStack = itemStack;
		this.location = location;
	}

	// getter
	public Location getLocation() {
		return location;
	}

	public ItemStack getItemStack() {
		return itemStack;
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
}
