package com.github.djlc.recipes;

import java.util.HashMap;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

@SuppressWarnings("serial")
public class PortableChestRecipe {
	private static final String ITEM_NAME = "Portable Chest";
	
	static {
		new CustomRecipe(new ItemStack(Material.CHEST), ITEM_NAME, "*#*", "#%#", "*#*", new HashMap<String, Integer>() {
			{
				put("*", 331);
				put("#", 264);
				put("%", 54);
			}
		});
		System.out.println("PortableChest Recipe was loaded.");		
	}
}
