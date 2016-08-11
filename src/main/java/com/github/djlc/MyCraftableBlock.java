package com.github.djlc;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

public abstract class MyCraftableBlock implements ConfigurationSerializable {

	// ブロックデータ
	protected List<Location> blockData = new ArrayList<>();

	// このクラスを継承しているクラス全体におけるブロックデータ
	// ブロック名とそのブロックデータによるハッシュマップで管理
	protected static Map<String, List<Location>> map = new HashMap<>();

	// ブロック1個のItemStack
	protected ItemStack item = null;

	// ブロックの識別子
	protected String itemCode = "";

	// コンストラクタ
	public MyCraftableBlock(JavaPlugin plugin, Material material, String itemName, String itemCode,
			String a, String b, String c, Map<Character, Material> list) {
		this.itemCode = itemCode;
		addItemAndRecipe(new ItemStack(material, 1), itemName, a, b, c, list);
		map.put(itemCode, blockData);
	}

	// ItemMetaとレシピを追加
	protected void addItemAndRecipe(ItemStack item, String itemName, String a, String b, String c,
			Map<Character, Material> list) {
		// アイテムの追加
		ItemMeta itemMeta = item.getItemMeta();
		itemMeta.setDisplayName(itemName);
		item.setItemMeta(itemMeta);

		// レシピの追加
		ShapedRecipe newItem = new ShapedRecipe(item);
		newItem.shape(a, b, c);
		for (Map.Entry<Character, Material> map : list.entrySet()) {
			newItem.setIngredient(map.getKey(), map.getValue());
		}

		// レシピの登録
		Bukkit.getServer().addRecipe(newItem);
	}

	@EventHandler
	public void onBlockPlace(BlockPlaceEvent event) {
		// 置いたブロックの位置とアイテム情報を取得
		Location location = event.getBlock().getLocation().clone();
		ItemStack item = event.getPlayer().getInventory().getItemInMainHand().clone();
		item.setAmount(1);

		// Pluginで追加されたブロックかどうか？
		if (!item.equals(item))
			return;

		// ブロック登録
		event.setCancelled(true);
		blockData.add(location);
		event.setCancelled(false);
	}

	@EventHandler
	public void onBlockBreak(BlockBreakEvent event) {
		// ブロックの位置を取得
		Location location = event.getBlock().getLocation();
		if (!blockData.contains(location) || !event.getBlock().getType().equals(item.getType())) {
			return;
		}

		// 破壊したブロックをドロップ
		event.setCancelled(true);
		event.getBlock().setType(Material.AIR);
		if (event.getPlayer().getGameMode().equals(GameMode.SURVIVAL)) {
			event.getPlayer().getWorld().dropItemNaturally(location, item.clone());
		}

		// ブロック登録解除
		blockData.remove(location);
	}

	@Override
	public Map<String, Object> serialize() {
		return null;
	}
}
