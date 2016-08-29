package com.github.djlc.recipes;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.server.PluginEnableEvent;

import com.github.djlc.LCPlugin;

// 追加レシピをまとめて追加するクラス
public class RecipeListener implements Listener {
	
	// プラグイン本体
	private LCPlugin plugin = null;
	
	public RecipeListener(LCPlugin plugin) {
		this.plugin = plugin;
	}
	
	@EventHandler
	public void onPluginEnable(PluginEnableEvent event) {

		if (event.getPlugin().equals(plugin)) {
			new LevelingTNTRecipe();
			new PortableChestRecipe();
		}
	}
}
