package com.github.djlc.util;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.configuration.serialization.ConfigurationSerializable;

public class CustomItem implements ConfigurationSerializable {

	// 定数
	private static final String PARAMETER = "parameter";
	private static final String RECIPE = "recipe";

	// データ
	private CustomRecipe recipe;
	private Object parameter;

	// コンストラクタ
	public CustomItem(CustomRecipe recipe, Object parameter) {
		this.recipe = recipe;
		this.parameter = parameter;
	}

	// シリアライズ
	@Override
	public Map<String, Object> serialize() {

		Map<String, Object> map = new HashMap<>();

		// シリアライズするデータをMapに追加
		map.put(RECIPE, recipe);
		map.put(PARAMETER, parameter);

		return map;
	}

	// デシリアライズ
	public static CustomItem deserialize(Map<String, Object> map) {

		// recipe
		CustomRecipe recipe = (CustomRecipe) map.get(RECIPE);

		// parameter
		Object parameter = map.get(PARAMETER);

		return new CustomItem(recipe, parameter);
	}

}
