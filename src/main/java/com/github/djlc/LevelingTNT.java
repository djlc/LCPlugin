package com.github.djlc;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.ExplosionPrimeEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;

public final class LevelingTNT implements Listener {

	private static HashMap<Location, ItemMeta> blocks = new HashMap<Location, ItemMeta>();
	private static ArrayList<ItemMeta> itemMetas = new ArrayList<ItemMeta>();

	public LevelingTNT() {
		// MK 1
		addItemAndRecipe(new ItemStack(Material.TNT), "Leveling TNT MK1 (3x3)", "***", "*%*", "***",
				new HashMap<Character, Material>() {
					{
						put('*', Material.REDSTONE);
						put('%', Material.TNT);
					}
				});
		// MK 2
		addItemAndRecipe(new ItemStack(Material.TNT), "Leveling TNT MK2 (5x5)", "***", "*%*", "***",
				new HashMap<Character, Material>() {
					{
						put('*', Material.IRON_INGOT);
						put('%', Material.TNT);
					}
				});
		// MK 3
		addItemAndRecipe(new ItemStack(Material.TNT), ChatColor.AQUA + "Leveling TNT MK3 (9x9)", "***", "*%*", "***",
				new HashMap<Character, Material>() {
					{
						put('*', Material.GOLD_INGOT);
						put('%', Material.TNT);
					}
				});
		// MK 4
		addItemAndRecipe(new ItemStack(Material.TNT), ChatColor.RED + "Leveling TNT MK4 (15x15)", "***", "*%*", "***",
				new HashMap<Character, Material>() {
					{
						put('*', Material.DIAMOND);
						put('%', Material.TNT);
					}
				});
	}

	private void addItemAndRecipe(ItemStack item, String name, String a, String b, String c,
			HashMap<Character, Material> list) {
		// アイテムの追加
		ItemMeta itemMeta = item.getItemMeta();
		itemMeta.setDisplayName(name);
		item.setItemMeta(itemMeta);
		itemMetas.add(itemMeta);

		// レシピの追加
		ShapedRecipe newItem = new ShapedRecipe(item);
		newItem.shape(a, b, c);
		for (Map.Entry<Character, Material> map : list.entrySet()) {
			newItem.setIngredient(map.getKey(), map.getValue());
		}

		// レシピの登録
		Bukkit.getServer().addRecipe(newItem);
	}

	// num*2+1の立方体の空洞を作る
	private void explosion(Location location, int num) {
		// 座標を立方体の頂点に移動
		location.subtract(num, num, num);

		// 1辺の長さ
		int n = num * 2 + 1;

		// 範囲内のブロックをドロップさせる
		for (int i = 0; i < n; i++) {
			for (int j = 0; j < n; j++) {
				for (int k = 0; k < n; k++) {
					if (!location.getBlock().getType().equals(Material.BEDROCK)) location.getBlock().breakNaturally();
					location.setX(location.getX() + 1.0);
				}
				location.setX(location.getX() - n);
				location.setY(location.getY() + 1.0);
			}
			location.setY(location.getY() - n);
			location.setZ(location.getZ() + 1.0);
		}
	}

	private boolean isMyItem(ItemMeta meta) {
		boolean isMyItem = false;
		for (ItemMeta im : itemMetas) {
			if (im.equals(meta)) {
				isMyItem = true;
				break;
			}
		}
		return isMyItem;
	}

	private ItemStack getItemFromItemMeta(Material material, ItemMeta itemMeta) {
		ItemStack item = new ItemStack(material);
		item.setItemMeta(itemMeta);
		return item;
	}

	@EventHandler
	void blockPlaced(BlockPlaceEvent event) {
		// 置かれたブロックがTNTかどうか？
		if (!event.getBlock().getType().equals(Material.TNT))
			return;

		// 置いたブロックの位置とアイテム情報を取得
		Location location = event.getBlock().getLocation();
		ItemStack item = event.getPlayer().getInventory().getItemInMainHand();
		item.setAmount(1);

		// Pluginで追加されたTNTかどうか？
		if (!isMyItem(item.getItemMeta()))
			return;

		// ブロック登録
		event.setCancelled(true);
		blocks.put(location.clone(), item.getItemMeta()); // 重要 : clone()
		event.setCancelled(false);

	}

	@EventHandler
	void blockBroke(BlockBreakEvent event) {
		// ブロックの位置を取得
		Location location = event.getBlock().getLocation();
		if (!blocks.containsKey(location) || !event.getBlock().getType().equals(Material.TNT)) {
			return;
		} else {
			if (!isMyItem(blocks.get(location)))
				return;
			event.setCancelled(true);
		}

		// 破壊したブロックをドロップ
		event.getBlock().setType(Material.AIR);
		if (event.getPlayer().getGameMode().equals(GameMode.SURVIVAL)) {
			event.getPlayer().getWorld().dropItemNaturally(location,
					getItemFromItemMeta(Material.TNT, blocks.get(location)));
		}
		// ブロック登録解除
		blocks.remove(location);
	}

	@EventHandler
	void blockExplode(ExplosionPrimeEvent event) {
		// Entityを取得
		Entity entity = event.getEntity();

		// それがTNTかどうか
		if (!(entity instanceof TNTPrimed)) {
			return;
		}
		TNTPrimed tnt = (TNTPrimed) entity;

		// 爆破位置の調整
		Location location = tnt.getLocation();
		double distance = 2.0;
		for (Location l : blocks.keySet()) {
			if (l.distance(location) < distance) {
				distance = l.distance(location);
				location = l.clone();
				distance = l.distance(location);
			}
		}
		if (!blocks.containsKey(location)) {
			return;
		}

		// デフォルト爆発を無効化
		event.setCancelled(true);

		// 立方体型に爆破
		int size = 0;
		if (itemMetas.get(0).equals(blocks.get(location))) {
			size = 1;
		} else if (itemMetas.get(1).equals(blocks.get(location))) {
			size = 2;
		} else if (itemMetas.get(2).equals(blocks.get(location))) {
			size = 4;
		} else if (itemMetas.get(3).equals(blocks.get(location))) {
			size = 7;
		}
		tnt.getWorld().playSound(location, Sound.ENTITY_GENERIC_EXPLODE, 1, 1);
		explosion(location, size);

		// 登録解除
		blocks.remove(location);
	}
}
