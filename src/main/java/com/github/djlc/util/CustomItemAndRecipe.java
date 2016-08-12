package com.github.djlc.util;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.inventory.ItemStack;

public class CustomItemAndRecipe implements ConfigurationSerializable {

	// 定数
	public static final String RECIPE = "recipes";
	private static final String ITEM = "item";
	private static final String CONTENT = "content";
	private static final String NAME = "name";
	private static final String LIST = "list";
	private static final String PARAMETER = "parameter";

	// データ
	private ItemStack item;
	private String a, b, c, name;
	private Map<String, Integer> list;
	private Object parameter;

	// コンストラクタ(デシリアライズ用)
	public CustomItemAndRecipe() {

	}

	// コンストラクタ(通常用)
	public CustomItemAndRecipe(ItemStack item, String name, String a, String b, String c, Map<String, Integer> list,
			Object parameter) {
		this.item = item;
		this.a = a;
		this.b = b;
		this.c = c;
		this.name = name;
		this.list = list;
		this.parameter = parameter;
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
		map.put(PARAMETER, getParameter());

		return map;
	}

	// デシリアライズ
	@SuppressWarnings("unchecked")
	public static CustomItemAndRecipe deserialize(Map<String, Object> map) {

		// インスタンスを作成
		CustomItemAndRecipe instance = new CustomItemAndRecipe();

		// item
		instance.item = (ItemStack) map.get(ITEM);

		// content
		List<String> list = (List<String>) map.get(CONTENT);
		instance.a = list.get(0);
		instance.b = list.get(1);
		instance.c = list.get(2);

		// name
		instance.name = (String) map.get(NAME);

		// list
		instance.list = (Map<String, Integer>) map.get(LIST);

		// parameter
		instance.parameter = map.get(PARAMETER);

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

	public Object getParameter() {
		return parameter;
	}
}
