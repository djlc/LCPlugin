package com.github.djlc.blocks;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import com.github.djlc.base64.InventoryToBase64;
import com.github.djlc.base64.ItemStackToBase64;
import com.github.djlc.base64.LocationToBase64;
import com.github.djlc.lists.PortableChestItemList;

// 破壊してもアイテムを保持するチェスト
public class PortableChest extends MyCraftableBlock {

	// 定数
	private static final int INVENTORY_SIZE = 54;
	private static final String TITLE = "Portable Chest";
	private static final String INVENTORY = "inventory";

	// インベントリ
	private Inventory inventory = null;

	// コンストラクタ
	public PortableChest(ItemStack itemStack, Location location) {
		super(itemStack, location);
		inventory = Bukkit.createInventory(null, INVENTORY_SIZE, TITLE);
	}

	public PortableChest(ItemStack itemStack, Location location, String id) {
		super(itemStack, location);
		this.inventory = (PortableChestItemList.contains(id)) ? PortableChestItemList.get(id)
				: Bukkit.createInventory(null, INVENTORY_SIZE, TITLE);
	}

	private PortableChest(ItemStack itemStack, Location location, Inventory inventory) {
		super(itemStack, location);
		this.inventory = inventory;
	}

	@Override
	public Map<String, Object> serialize() {
		Map<String, Object> map = new HashMap<>();
		map.put(ITEMSTACK, ItemStackToBase64.encode(itemStack));
		map.put(LOCATON, LocationToBase64.encode(location));
		map.put(INVENTORY, InventoryToBase64.encode(inventory));
		return map;
	}

	public static PortableChest deserialize(Map<String, Object> map) throws IOException {
		ItemStack itemStack = ItemStackToBase64.decode((String) map.get(ITEMSTACK));
		Location location = LocationToBase64.decode((String) map.get(LOCATON));
		Inventory inventory = InventoryToBase64.decode((String) map.get(INVENTORY));

		PortableChest instance = new PortableChest(itemStack, location);
		instance.inventory = inventory;

		return instance;
	}

	public Inventory getInventory() {
		return this.inventory;
	}

	public static boolean isPortableChest(ItemStack item) {
		return TITLE.equals(item.getItemMeta().getDisplayName());
	}

}
