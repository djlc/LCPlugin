package com.github.djlc;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import net.milkbowl.vault.economy.EconomyResponse;

public class Cube implements CommandExecutor {

	private final Tutorial instance;

	public Cube(Tutorial instance) {
		this.instance = instance;
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
				instance.getLogger().info(ChatColor.RED + "Please run this command in game.");
				return true;
			}
			// 引数の個数が5個ちょうどなら
			if (args.length == 5) {
				int n = Integer.parseInt(args[0]);
				int x = Integer.parseInt(args[1]);
				int y = Integer.parseInt(args[2]);
				int z = Integer.parseInt(args[3]);
				Material m = Material.getMaterial(args[4]);

				// 指定したMaterialがBlockなら
				if (m.isBlock()) {
					Location l = p.getLocation().add(x, y, z);

					// 料金が足りているか？
					double price = n * n * n;
					if (Tutorial.econ.getBalance(p) >= price) {
						// 支払い
						EconomyResponse er = Tutorial.econ.withdrawPlayer(p, price);
						if (er.transactionSuccess()) {
							sender.sendMessage("$" + er.amount + " was removed.");
							// 立方体を作成
							create(l, n, m);
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
