package com.github.djlc;

import org.bukkit.Location;
import org.bukkit.inventory.ItemStack;

public class LevelingTNT extends MyCraftableBlock {

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
}
