package com.github.djlc;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.ExplosionPrimeEvent;
import org.bukkit.event.server.PluginDisableEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;

public final class LevelingTNT implements Listener, MyCraftableBlock {

	// keys
	private static final String LEVELING_TNT = "levelingtnt";

	// Plugin
	private final LCPlugin plugin;

	// 配置された整地TNTの位置とItemMeta(種類)のハッシュ
	// (シリアライズが必要)
	private static Map<Location, ItemMeta> blocks = new HashMap<>();

	// 独自TNTのItemMeta(種類)
	private static Map<Integer, ItemMeta> itemMetas = new HashMap<>();

	// TNTの爆破範囲のMap
	private static Map<ItemMeta, Integer> radius = new HashMap<>();

	public LevelingTNT(LCPlugin plugin) {

		this.plugin = plugin;

		// MK 1
		addItemAndRecipe(new ItemStack(Material.TNT), 0, "Leveling TNT MK1 (3x3)", "***", "*%*", "***",
				new HashMap<Character, Material>() {
					{
						put('*', Material.REDSTONE);
						put('%', Material.TNT);
					}
				});
		radius.put(itemMetas.get(0), 1);

		// MK 2
		addItemAndRecipe(new ItemStack(Material.TNT), 1, "Leveling TNT MK2 (5x5)", "***", "*%*", "***",
				new HashMap<Character, Material>() {
					{
						put('*', Material.IRON_INGOT);
						put('%', Material.TNT);
					}
				});
		radius.put(itemMetas.get(1), 2);

		// MK 3
		addItemAndRecipe(new ItemStack(Material.TNT), 2, ChatColor.AQUA + "Leveling TNT MK3 (9x9)", "***", "*%*", "***",
				new HashMap<Character, Material>() {
					{
						put('*', Material.GOLD_INGOT);
						put('%', Material.TNT);
					}
				});
		radius.put(itemMetas.get(2), 4);

		// MK 4
		addItemAndRecipe(new ItemStack(Material.TNT), 3, ChatColor.RED + "Leveling TNT MK4 (15x15)", "***", "*%*",
				"***", new HashMap<Character, Material>() {
					{
						put('*', Material.DIAMOND);
						put('%', Material.TNT);
					}
				});
		radius.put(itemMetas.get(3), 7);

		// データ読み込み
		deserialize();
	}

	private void addItemAndRecipe(ItemStack item, int itemID, String name, String a, String b, String c,
			Map<Character, Material> list) {
		// アイテムの追加
		ItemMeta itemMeta = item.getItemMeta();
		itemMeta.setDisplayName(name);
		item.setItemMeta(itemMeta);
		itemMetas.put(itemID, itemMeta);

		// レシピの追加
		ShapedRecipe newItem = new ShapedRecipe(item);
		newItem.shape(a, b, c);
		for (Map.Entry<Character, Material> map : list.entrySet()) {
			newItem.setIngredient(map.getKey(), map.getValue());
		}

		// レシピの登録
		Bukkit.getServer().addRecipe(newItem);
	}

	private int getItemID(ItemMeta itemMeta) {
		for (Map.Entry<Integer, ItemMeta> e : itemMetas.entrySet()) {
			if (e.getValue().equals(itemMeta)) {
				return e.getKey();
			}
		}
		return -1;
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
					if (!location.getBlock().getType().equals(Material.BEDROCK)) {
						if (location.getBlock().getType().equals(Material.TNT)) {
							location.getBlock().setType(Material.AIR);
							explosion(location.clone(), radius.get(blocks.get(location)));
							blocks.remove(location);
						}
						location.getBlock().breakNaturally();
					}
					location.setX(location.getX() + 1.0);
				}
				location.setX(location.getX() - n);
				location.setY(location.getY() + 1.0);
			}
			location.setY(location.getY() - n);
			location.setZ(location.getZ() + 1.0);
		}
	}

	// そのアイテムが整地TNTかどうか
	private boolean isMyItem(ItemMeta meta) {
		return itemMetas.containsValue(meta);
	}

	// ItemMetaからItemStackを取得する
	private ItemStack getItemFromItemMeta(Material material, ItemMeta itemMeta) {
		ItemStack item = new ItemStack(material);
		item.setItemMeta(itemMeta);
		return item;
	}

	@EventHandler
	public void onPluginDisable(PluginDisableEvent event) {
		if (event.getPlugin().equals(plugin)) {
			serialize();
		}
	}

	@EventHandler
	@Override
	public void onBlockPlace(BlockPlaceEvent event) {
		// 置かれたブロックがTNTかどうか？
		if (!event.getBlock().getType().equals(Material.TNT))
			return;

		// 置いたブロックの位置とアイテム情報を取得
		Location location = event.getBlock().getLocation().clone();
		ItemStack item = event.getPlayer().getInventory().getItemInMainHand().clone();
		item.setAmount(1);

		// Pluginで追加されたTNTかどうか？
		if (!isMyItem(item.getItemMeta()))
			return;

		// ブロック登録
		event.setCancelled(true);
		blocks.put(location, item.getItemMeta());
		event.setCancelled(false);

	}

	@EventHandler
	@Override
	public void onBlockBreak(BlockBreakEvent event) {
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
	private void blockExplode(ExplosionPrimeEvent event) {
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
		tnt.getWorld().playSound(location, Sound.ENTITY_GENERIC_EXPLODE, 1, 1);
		explosion(location.clone(), radius.get(blocks.get(location)));

		// 登録解除
		blocks.remove(location);
		refleshList();
	}

	private void refleshList() {
		for (Iterator<Location> i = blocks.keySet().iterator(); i.hasNext();) {
			if (!i.next().getBlock().getType().equals(Material.TNT)) {
				i.remove();
			}
		}
	}

	@Override
	public void serialize() {
		// world名, <itemID, 座標>
		Map<String, Map<Integer, List<List<Double>>>> data = new HashMap<>();
		for (Map.Entry<Location, ItemMeta> e : blocks.entrySet()) {
			if (!data.containsKey(e.getKey().getWorld().getName())) {
				data.put(e.getKey().getWorld().getName(), new HashMap<Integer, List<List<Double>>>());
			}
			if (!data.get(e.getKey().getWorld().getName()).containsKey(getItemID(e.getValue()))) {
				data.get(e.getKey().getWorld().getName()).put(getItemID(e.getValue()), new ArrayList<List<Double>>());
			}
			data.get(e.getKey().getWorld().getName()).get(getItemID(e.getValue()))
					.add(Arrays.asList(e.getKey().getX(), e.getKey().getY(), e.getKey().getZ()));
		}
		List<Map<String, Map<Integer, List<List<Double>>>>> temp = new ArrayList<>();
		temp.add(data);
		plugin.getConfig().set(LEVELING_TNT, temp);
	}

	@Override
	public void deserialize() {
		FileConfiguration conf = plugin.getConfig();
		if (conf.contains(LEVELING_TNT)) {
			List<Map<?, ?>> list = conf.getMapList(LEVELING_TNT);
			if (list.size() == 0)
				return;
			Map<?, ?> map = list.get(0);
			for (Map.Entry<?, ?> e : map.entrySet()) {
				String worldName = (String) e.getKey();
				@SuppressWarnings("unchecked")
				Map<Integer, List<List<Double>>> hash = (Map<Integer, List<List<Double>>>) e.getValue();
				for (Map.Entry<Integer, List<List<Double>>> f : hash.entrySet()) {
					for (int i = 0; i < f.getValue().size(); i++) {
						List<Double> g = f.getValue().get(i);
						Location l = new Location(Bukkit.getWorld(worldName), g.get(0), g.get(1), g.get(2));
						if (l.getBlock().getType().equals(Material.TNT)) {
							blocks.put(l, itemMetas.get(f.getKey()));
							System.out.println(l.toString() + " was loaded.");
						}
					}
				}
			}
		}
	}
}
