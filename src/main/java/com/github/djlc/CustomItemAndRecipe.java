package com.github.djlc;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.inventory.ItemStack;

public class CustomItemAndRecipe implements ConfigurationSerializable {

	// 定数
	public static final String RECIPE = "recipes";
	private static final String RECIPE_ITEM = "item";
	private static final String RECIPE_CONTENT = "content";
	private static final String RECIPE_NAME = "name";
	private static final String RECIPE_LIST = "list";

	// データ
	private ItemStack item;
	private String a, b, c, name;
	private Map<String, Integer> list;

	// コンストラクタ(デシリアライズ用)
	public CustomItemAndRecipe() {

	}

	// コンストラクタ(通常用)
	public CustomItemAndRecipe(ItemStack item, String name, String a, String b, String c, Map<String, Integer> list) {
		this.item = item;
		this.a = a;
		this.b = b;
		this.c = c;
		this.name = name;
		this.list = list;
	}

	// シリアライズ
	@Override
	public Map<String, Object> serialize() {

		Map<String, Object> map = new HashMap<>();

		// シリアライズするデータをMapに追加
		map.put(RECIPE_ITEM, getItem());
		map.put(RECIPE_CONTENT, Arrays.asList(getA(), getB(), getC()));
		map.put(RECIPE_NAME, getName());
		map.put(RECIPE_LIST, getList());

		return map;
	}

	// デシリアライズ
	@SuppressWarnings("unchecked")
	public static CustomItemAndRecipe deserialize(Map<String, Object> map) {

		// インスタンスを作成
		CustomItemAndRecipe instance= new CustomItemAndRecipe();

		// item
		instance.item = (ItemStack) map.get(RECIPE_ITEM);

		// content
		List<String> list = (List<String>) map.get(RECIPE_CONTENT);
		instance.a = list.get(0);
		instance.b = list.get(1);
		instance.c = list.get(2);

		// name
		instance.name = (String) map.get(RECIPE_NAME);

		// list
		instance.list = (Map<String, Integer>) map.get(RECIPE_LIST);

		return instance;
	}

	public ItemStack getItem() {
		return item;
	}

	public String getName() {
		return name;
	}

	public String getA() {
		return a;
	}

	public String getB() {
		return b;
	}

	public String getC() {
		return c;
	}

	public Map<String, Integer> getList() {
		return list;
	}

	public void setList(Map<String, Integer> list) {
		this.list = list;
	}
}
