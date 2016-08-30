package com.github.djlc.recipes;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

@SuppressWarnings("serial")
public class LevelingTNTRecipe {
	
	// 書体を含めたアイテム名
	private static final List<String> ITEM_NAME = Arrays.asList("Leveling TNT MK1 (3x3x3)",
			"Leveling TNT MK2 (5x5x5)", "Leveling TNT MK3 (9x9x9)", "Leveling TNT MK4 (15x15x15)");

	static {
		// Leveling TNT
		new CustomRecipe(new ItemStack(Material.TNT), ITEM_NAME.get(0), "***", "*%*", "***",
				new HashMap<String, Integer>() {
					{
						put("*", 331); // RedStone
						put("%", 46); // TNT
					}
				});
		new CustomRecipe(new ItemStack(Material.TNT), ITEM_NAME.get(1), "***", "*%*", "***",
				new HashMap<String, Integer>() {
					{
						put("*", 265); // Iron Ingot
						put("%", 46);
					}
				});
		new CustomRecipe(new ItemStack(Material.TNT), ChatColor.AQUA + ITEM_NAME.get(2), "***", "*%*", "***",
				new HashMap<String, Integer>() {
					{
						put("*", 266); // Gold Ingot
						put("%", 46);
					}
				});
		new CustomRecipe(new ItemStack(Material.TNT), ChatColor.RED + ITEM_NAME.get(3), "***", "*%*", "***",
				new HashMap<String, Integer>() {
					{
						put("*", 264); // Diamond
						put("%", 46);
					}
				});
		System.out.println("LevelingTNT Recipes were loaded.");
	}
}
