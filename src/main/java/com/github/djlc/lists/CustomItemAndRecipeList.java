package com.github.djlc.lists;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;

import com.github.djlc.LCPlugin;
import com.github.djlc.util.CustomItemAndRecipe;

// 独自レシピを登録するクラス
public class CustomItemAndRecipeList {

	// 登録されたレシピのリスト
	private static List<CustomItemAndRecipe> recipeList = new ArrayList<>();

	// 登録されたレシピのアイテムとパラメータのハッシュマップ
	private static Map<ItemStack, Object> itemList = new HashMap<>();

	public CustomItemAndRecipeList(LCPlugin plugin) {

		if (!plugin.getConfig().contains(CustomItemAndRecipe.RECIPE)) {
			addRecipe(new ItemStack(Material.TNT), "Leveling TNT MK1 (3x3)", "***", "*%*", "***",
					new HashMap<String, Integer>() {
						{
							put("*", 331);
							put("%", 46);
						}
					}, 1);
			addRecipe(new ItemStack(Material.TNT), "Leveling TNT MK2 (5x5)", "***", "*%*", "***",
					new HashMap<String, Integer>() {
						{
							put("*", 265);
							put("%", 46);
						}
					}, 2);
			addRecipe(new ItemStack(Material.TNT), ChatColor.AQUA + "Leveling TNT MK3 (9x9)", "***", "*%*", "***",
					new HashMap<String, Integer>() {
						{
							put("*", 266);
							put("%", 46);
						}
					}, 4);
			addRecipe(new ItemStack(Material.TNT), ChatColor.RED + "Leveling TNT MK4 (15x15)", "***", "*%*", "***",
					new HashMap<String, Integer>() {
						{
							put("*", 264);
							put("%", 46);
						}
					}, 7);
			plugin.getLogger().info("config.yml does not exist. Set default recipes.");
			plugin.getConfig().set(CustomItemAndRecipe.RECIPE, recipeList);
		} else {
			List<?> temp = plugin.getConfig().getList(CustomItemAndRecipe.RECIPE);
			for (Object e : temp) {
				CustomItemAndRecipe m = (CustomItemAndRecipe) e;
				addRecipe(m);
			}
			plugin.getLogger().info("Load custom recipes from config.yml.");
		}
	}

	@SuppressWarnings("deprecation")
	public static void addRecipe(ItemStack item, String name, String a, String b, String c,
			Map<String, Integer> list, Object parameter) {

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

		// 保存
		recipeList.add(new CustomItemAndRecipe(item, name, a, b, c, list, parameter));
		itemList.put(item, parameter);
	}

	public static void addRecipe(CustomItemAndRecipe myRecipe) {
		addRecipe(myRecipe.getItem(), myRecipe.getName(), myRecipe.getA(), myRecipe.getB(), myRecipe.getC(),
				myRecipe.getList(), myRecipe.getParameter());
	}

	public static boolean contains(ItemStack item) {
		return itemList.containsKey(item);
	}

	public static Object getParameter(ItemStack item) {
		return itemList.get(item);
	}
}
