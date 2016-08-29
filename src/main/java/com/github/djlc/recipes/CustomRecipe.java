package com.github.djlc.recipes;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;

// レシピを登録するためのクラス
public class CustomRecipe implements ConfigurationSerializable {
	// 定数
	public static final String RECIPE = "recipes";
	private static final String ITEM = "item";
	private static final String CONTENT = "content";
	private static final String NAME = "name";
	private static final String LIST = "list";

	// データ
	private ItemStack item;
	private String a, b, c, name;
	private Map<String, Integer> list;

	// コンストラクタ(通常用)
	public CustomRecipe(ItemStack item, String name, String a, String b, String c, Map<String, Integer> list) {
		this.item = item;
		this.a = a;
		this.b = b;
		this.c = c;
		this.name = name;
		this.list = list;
		addRecipe();
	}

	@SuppressWarnings("deprecation")
	private void addRecipe() {
		// アイテムの追加
		ItemMeta itemMeta = item.getItemMeta();
		itemMeta.setDisplayName(name);
		item.setItemMeta(itemMeta);

		// レシピの追加
		ShapedRecipe newItem = new ShapedRecipe(item);
		newItem.shape(a, b, c);
		for (Map.Entry<String, Integer> map : list.entrySet()) {
			newItem.setIngredient(map.getKey().charAt(0), Material.getMaterial(map.getValue().intValue()));
		}

		// レシピの登録
		Bukkit.getServer().addRecipe(newItem);
	}

	// シリアライズ
	@Override
	public Map<String, Object> serialize() {

		Map<String, Object> map = new HashMap<>();

		// シリアライズするデータをMapに追加
		map.put(ITEM, item);
		map.put(CONTENT, Arrays.asList(a, b, c));
		map.put(NAME, name);
		map.put(LIST, list);

		return map;
	}

	// デシリアライズ
	@SuppressWarnings("unchecked")
	public static CustomRecipe deserialize(Map<String, Object> map) {

		// item
		ItemStack item = (ItemStack) map.get(ITEM);

		// content
		List<String> content = (List<String>) map.get(CONTENT);
		String a = content.get(0);
		String b = content.get(1);
		String c = content.get(2);

		// name
		String name = (String) map.get(NAME);

		// list
		Map<String, Integer> list = (Map<String, Integer>) map.get(LIST);

		return new CustomRecipe(item, name, a, b, c, list);
	}
}
