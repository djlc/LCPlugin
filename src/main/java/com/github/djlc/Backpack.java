package com.github.djlc;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.event.Listener;
import org.bukkit.inventory.Inventory;

public class Backpack implements Listener, MyCraftableBlock {

	// インベントリのリスト
	private static List<Inventory> list = new ArrayList<Inventory>();

	public Backpack() {
	}

	@Override
	public void serialize() {
	}

	@Override
	public void deserialize() {
	}

}
