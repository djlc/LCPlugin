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
			if (sender instanceof Player) {
				player = (Player) sender;
				if (command.getName().equalsIgnoreCase("iteminfo")) {
					Material material = player.getInventory().getItemInMainHand().getType();
					sender.sendMessage("Material : " + material.toString());
					MaterialData materialData = player.getInventory().getItemInMainHand().getData();
					sender.sendMessage("MaterialData" + materialData.toString());
					sender.sendMessage("ItemID : " + material.getId() + ":" + materialData.getItemTypeId());
				}
			} else {
				plugin.getLogger().info(ChatColor.RED + "Please run this command in game.");
			}
		}

		return true;
	}

}
