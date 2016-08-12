package com.github.djlc.command;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockDamageEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.material.MaterialData;
import org.bukkit.plugin.java.JavaPlugin;

import com.github.djlc.LCPlugin;

import net.milkbowl.vault.economy.EconomyResponse;

public class Cube implements CommandExecutor, Listener {

	// 状態保存(内部クラス)
	private class Stats {
		// 1辺の長さ
		public int size = 0;

		// 立方体を構成するブロックの個数
		public int num = 0;

		// ブロック
		public Material material = null;
		public MaterialData materialData = null;

		// 座標
		private Location location = null;

		public Stats(int size, Material material, MaterialData materialData) {
			this.size = size;
			this.num = size * size * size;
			this.material = material;
			this.materialData = materialData;
		}
	}

	// 本体
	private final JavaPlugin plugin;

	// ユーザーを登録するリスト
	private static Map<Player, Stats> list = new HashMap<Player, Stats>();

	public Cube(JavaPlugin plugin) {
		this.plugin = plugin;
	}

	@EventHandler
	public void onPlayerSet(BlockDamageEvent event) {
		// プレイヤーが登録されているなら
		if (list.containsKey(event.getPlayer())) {
			// ユーザーデータを取得して
			Stats stats = list.get(event.getPlayer());

			// イベントから座標を取得して設定
			stats.location = event.getBlock().getLocation().add(0, 1, 0);

			// 支払い処理して
			if (!pay(event.getPlayer())) {
				// 失敗したら中止してイベントキャンセル
				event.setCancelled(true);
				// リストから削除
				list.remove(event.getPlayer());
				return;
			}

			// インベントリ内のブロックを差し引いて
			removeItems(event.getPlayer());

			// 立方体生成
			create(event.getPlayer());

			// リストから削除
			list.remove(event.getPlayer());
			event.setCancelled(true);
		}
	}

	@SuppressWarnings("deprecation")
	private void create(Player player) {

		Stats stats = list.get(player);

		for (int i = 0; i < stats.size; i++) {
			for (int j = 0; j < stats.size; j++) {
				for (int k = 0; k < stats.size; k++) {
					stats.location.getBlock().setType(stats.material);
					stats.location.getBlock().setData(stats.materialData.getData());
					stats.location.setX(stats.location.getX() + 1.0);
				}
				stats.location.setX(stats.location.getX() - stats.size);
				stats.location.setY(stats.location.getY() + 1.0);
			}
			stats.location.setY(stats.location.getY() - stats.size);
			stats.location.setZ(stats.location.getZ() + 1.0);
		}

		// 使用したブロックの個数を通知
		player.sendMessage(ChatColor.AQUA + "filled " + stats.num + " blocks.");
	}

	private boolean pay(Player player) {

		Stats stats = list.get(player);

		// 支払い処理
		EconomyResponse er = LCPlugin.econ.withdrawPlayer(player, stats.num);

		// 失敗した場合の処理
		if (!er.transactionSuccess()) {
			player.sendMessage("An error occured: " + er.errorMessage);
			return false;
		}

		// 支払い通知
		player.sendMessage("$" + er.amount + " was removed.");

		return true;
	}

	private void removeItems(Player player) {
		// プレイヤーのインベントリ
		PlayerInventory playerInventory = player.getInventory();
		// スタック
		ItemStack itemStack = null;
		// ユーザーデータ
		Stats stats = list.get(player);

		// 必要個数のブロックをインベントリから差し引く
		int id = 0;
		int num = stats.num;
		while (num > 0) {
			// 対象のブロックが来るまでループを回す
			while (true) {
				itemStack = playerInventory.getItem(id);
				if (itemStack != null && itemStack.getType().equals(stats.material)
						&& itemStack.getData().equals(stats.materialData)) {
					break;
				} else {
					id++;
				}
			}

			// 対象のブロックをインベントリから差し引く
			if (num >= itemStack.getAmount()) {
				num -= itemStack.getAmount();
				playerInventory.setItem(id, null);
			} else {
				playerInventory.setItem(id,
						new ItemStack(stats.material, itemStack.getAmount() - num, itemStack.getDurability()));
				num = 0;
			}
		}
		// インベントリの更新
		player.updateInventory();
	}

	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

		// 1辺nブロックの中の詰まった立方体を任意の固体ブロックで作成する.
		if (command.getName().equalsIgnoreCase("cube")) {

			// プレイヤー
			Player player = null;

			// Playerからのコマンドか？
			if (sender instanceof Player) {
				player = (Player) sender;
				// すでに実行中なら中止する
				if (list.containsKey(player)) {
					sender.sendMessage(ChatColor.RED + "You have already run the command!");
					return true;
				}
			} else {
				plugin.getLogger().info(ChatColor.RED + "Please run this command in game.");
				return true;
			}

			// 引数の個数が違うなら
			if (args.length != 1) {
				sender.sendMessage(ChatColor.RED + "Wrong Arguments!");
				return false;
			}

			// 辺の長さ
			int size = Integer.parseInt(args[0]);

			// ブロック
			Material material = player.getInventory().getItemInMainHand().getType();
			MaterialData materialData = player.getInventory().getItemInMainHand().getData();

			// ブロックでないなら通知
			if (!material.isBlock()) {
				sender.sendMessage(ChatColor.RED + "This is not Block!");
				return true;
			}

			// プレイヤーのインベントリ
			PlayerInventory inventory = player.getInventory();

			// スタック
			ItemStack itemStack = null;

			// カウンタ
			int cnt = 0;

			// インベントリ内の指定ブロックの個数をカウント
			for (int i = 0; i < inventory.getSize(); i++) {
				itemStack = inventory.getItem(i);
				if (itemStack != null && itemStack.getType().equals(material)
						&& itemStack.getData().equals(materialData)) {
					cnt += itemStack.getAmount();
				}
			}

			// 必要なブロックの個数
			int num = size * size * size;

			// 個数が足りているか？
			if (cnt < num) {
				sender.sendMessage(ChatColor.RED + "You don't have enough Blocks!");
				return true;
			}

			// 料金が足りているか？
			if (LCPlugin.econ.getBalance(player) < num) {
				sender.sendMessage(ChatColor.RED + "You can't afford this!");
				return true;
			}

			// プレイヤーを登録
			list.put(player, new Stats(size, material, materialData));

			// プレイヤーが次にすべき操作を通知
			sender.sendMessage(ChatColor.AQUA + "Please LEFT CLICK where you want to place the cube.");
		}
		return true;
	}
}
