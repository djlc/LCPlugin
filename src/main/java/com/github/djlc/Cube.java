package com.github.djlc;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import net.milkbowl.vault.economy.EconomyResponse;

public class Cube implements CommandExecutor {

	private final LCPlugin plugin;

	public Cube(LCPlugin plugin) {
		this.plugin = plugin;
	}

	private void create(Location l, int n, Material m) {
		for (int i = 0; i < n; i++) {
			for (int j = 0; j < n; j++) {
				for (int k = 0; k < n; k++) {
					l.getBlock().setType(m);
					l.setX(l.getX() + 1.0);
				}
				l.setX(l.getX() - n);
				l.setY(l.getY() + 1.0);
			}
			l.setY(l.getY() - n);
			l.setZ(l.getZ() + 1.0);
		}
	}

	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		// 1辺nブロックの中の詰まった立方体を任意の固体ブロックで作成する.
		if (cmd.getName().equalsIgnoreCase("cube")) {
			Player p;
			// Playerからのコマンドか？
			if (sender instanceof Player) {
				p = (Player) sender;
			} else {
				plugin.getLogger().info(ChatColor.RED + "Please run this command in game.");
				return true;
			}
			// 引数の個数が5個ちょうどなら
			if (args.length == 5) {

				// 辺の長さ
				int n = Integer.parseInt(args[0]);

				// 相対座標
				int x = Integer.parseInt(args[1]);
				int y = Integer.parseInt(args[2]);
				int z = Integer.parseInt(args[3]);

				// ブロック
				Material m = Material.getMaterial(args[4]);
				// nullなら名前が間違っていると通知
				if (m == null) {
					sender.sendMessage(ChatColor.RED + "Block name is wrong!");
					return true;
				}

				// 指定したMaterialがBlockなら
				if (m.isBlock()) {
					Location l = p.getLocation().add(x, y, z);

					// プレイヤーのインベントリ
					PlayerInventory pi = p.getInventory();
					// スタック
					ItemStack is = null;
					int cnt = 0;
					for (int i=0; i<pi.getSize(); i++) {
						is = pi.getItem(i);
						if (is != null && is.getType().equals(m)) cnt += is.getAmount();
					}

					// 個数が足りているか？
					int num = n * n * n;
					if (cnt < num) {
						sender.sendMessage(ChatColor.RED + "You don't have enough Blocks!");
						return true;
					}

					// 料金が足りているか？
					double price = num;
					if (LCPlugin.econ.getBalance(p) >= price) {
						// 支払い
						EconomyResponse er = LCPlugin.econ.withdrawPlayer(p, price);
						if (er.transactionSuccess()) {
							// 支払い通知
							sender.sendMessage("$" + er.amount + " was removed.");
							// 必要個数のブロックをインベントリから差し引く
							int id = 0;
							while(num > 0) {
								// 対象のブロックが来るまでループを回す
								while(true) {
									is = pi.getItem(id);
									if (is != null && is.getType().equals(m)) {
										break;
									} else {
										id++;
									}
								}

								// 対象のブロックをインベントリから差し引く
								if (num >= is.getAmount()) {
									num -= is.getAmount();
									pi.setItem(id, null);
								} else {
									pi.setItem(id, new ItemStack(m, is.getAmount() - num));
									sender.sendMessage("Finished");
									num = 0;
								}
							}
							// インベントリの更新
							p.updateInventory();
							// 立方体を作成
							create(l, n, m);
							// 使用したブロックの個数を通知
							sender.sendMessage(ChatColor.AQUA + "filled " + n * n * n + " blocks.");
						} else {
							sender.sendMessage("An error occured: " + er.errorMessage);
						}
					} else {
						sender.sendMessage(ChatColor.RED + "You can't afford this!");
					}
				} else {
					sender.sendMessage(ChatColor.RED + "This is not BLOCK!");
				}
				return true;
			} else {
				sender.sendMessage(ChatColor.RED + "Wrong Arguments!");
			}
		}
		return false;
	}
}
