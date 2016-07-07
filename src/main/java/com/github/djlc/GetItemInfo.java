package com.github.djlc;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.material.MaterialData;

public class GetItemInfo implements CommandExecutor {

	// 本体
	private final LCPlugin plugin;

	public GetItemInfo(LCPlugin plugin) {
		this.plugin = plugin;
	}

	@SuppressWarnings("deprecation")
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

		if (command.getName().equalsIgnoreCase("iteminfo")) {
			// プレイヤー
			Player player = null;

			// Playerからのコマンドか？
			if (!(sender instanceof Player)) {
				plugin.getLogger().info(ChatColor.RED + "Please run this command in game.");
			}

			player = (Player) sender;

			if (args.length == 0) {
				// 手に持っているアイテムの情報を表示
				Material material = player.getInventory().getItemInMainHand().getType();
				if (material == Material.AIR) {
					sender.sendMessage(ChatColor.RED + "Select item!");
					return true;
				}
				sender.sendMessage(ChatColor.AQUA + "Material : " + material.toString());
				MaterialData materialData = player.getInventory().getItemInMainHand().getData();
				sender.sendMessage(ChatColor.GREEN + "MaterialData : " + materialData.toString());
				sender.sendMessage(ChatColor.GOLD + "ItemID : " + material.getId() + ":" + materialData.getData());
			} else {
				sender.sendMessage(ChatColor.RED + "Wrong Arguments!");
				return false;
			}
		}

		return true;
	}

}
