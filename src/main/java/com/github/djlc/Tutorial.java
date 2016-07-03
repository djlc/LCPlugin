package com.github.djlc;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.economy.EconomyResponse;

public final class Tutorial extends JavaPlugin {

	public static Economy econ = null;

	@Override
	public void onEnable() {
		getServer().getPluginManager().registerEvents(new MyListener(), this);
		if (!setupEconomy()) {
			getLogger().info("Disabled due to no Vault dependency found!");
			getServer().getPluginManager().disablePlugin(this);
			return;
		}
		getLogger().info("Plugin has been enabled.");
	}

	@Override
	public void onDisable() {
		getLogger().info("Plugin has been disabled.");
	}

	private boolean setupEconomy() {
		if (getServer().getPluginManager().getPlugin("Vault") == null) return false;
		RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
		if (rsp == null) {
			return false;
		}
		econ = rsp.getProvider();
		return (econ != null);
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
		if (cmd.getName().equalsIgnoreCase("basic")) {
			if (sender instanceof Player) {
				Player player = (Player)sender;
				getLogger().info("Hi! " + ChatColor.AQUA + player.getName());
				return true;
			} else {
				getLogger().info(ChatColor.RED + "Please run this command in game.");
				return false;
			}
		}

		// 1辺nブロックの中の詰まった立方体を任意の固体ブロックで作成する.
		if (cmd.getName().equalsIgnoreCase("cube")) {

			// Playerからのコマンドか？
			if (sender instanceof Player) {
				Player p = (Player) sender;

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
						if (econ.getBalance(p) >= price) {
							// 支払い
							EconomyResponse er = econ.withdrawPlayer(p, price);
							if (er.transactionSuccess()) {
								sender.sendMessage("$" + er.amount + " was removed.");
								// 立方体を作成
								new Cube(l, n, m);
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
			} else {
				getLogger().info(ChatColor.RED + "Please run this command in game.");
				return true;
			}
		}


		return false;
	}
}
