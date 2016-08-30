package com.github.djlc.util;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.block.Sign;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.server.PluginDisableEvent;
import org.bukkit.event.server.PluginEnableEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

public class Date implements CommandExecutor, Listener {

	// プラグイン本体
	private JavaPlugin plugin;

	// keys
	private static final String COMMAND_NAME = "date";
	private static final String DATESIGN = "datesign";

	// 管理する看板の位置
	private static List<Location> dateSignLocations = new ArrayList<Location>();

	// コンストラクタ
	public Date(JavaPlugin plugin) {
		this.plugin = plugin;
	}

	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (command.getName().equalsIgnoreCase(COMMAND_NAME)) {

			// Playerからのコマンドか？
			if (!(sender instanceof Player)) {
				plugin.getLogger().info(new java.util.Date().toString());
				return true;
			}

			if (args.length == 0) {
				// 現在の日付を表示
				sender.sendMessage(new java.util.Date().toString());
				return true;
			}
		}

		return true;
	}

	@SuppressWarnings("unchecked")
	@EventHandler
	private void onPluginEnable(PluginEnableEvent event) {
		if (event.getPlugin().equals(plugin)) {

			// データの読み込み
			if (plugin.getConfig().contains(DATESIGN)) {
				List<SerializableLocation> temp = (List<SerializableLocation>) plugin.getConfig().getList(DATESIGN);
				for (SerializableLocation e : temp) {
					dateSignLocations.add(e.getLocation());
				}

				// タイマーの実行
				new BukkitRunnable() {
					public void run() {
						for (Location l : dateSignLocations) {
							Sign sign = (Sign) l.getBlock().getState();
							java.util.Date date = new java.util.Date();
							SimpleDateFormat f1 = new SimpleDateFormat("yyyy/MM/dd");
							SimpleDateFormat f2 = new SimpleDateFormat("HH:mm");
							sign.setLine(1, f1.format(date));
							sign.setLine(2, "[" + dayOfWeekToString() + "]");
							sign.setLine(3, f2.format(date));
							sign.update(); // こ↑こ↓重要
						}
					}
				}.runTaskTimer(plugin, 0, 40);
			}
		}
	}

	@EventHandler
	public void onPluginDisable(PluginDisableEvent event) {
		if (event.getPlugin().equals(plugin)) {
			// データの保存
			List<SerializableLocation> data = new ArrayList<>();
			for (Location l : dateSignLocations) {
				data.add(new SerializableLocation(l));
			}
			plugin.getConfig().set(DATESIGN, data);
		}
	}

	// [Date]と看板の2行目に書いた時に呼び出される
	@EventHandler
	public void onCreateSign(SignChangeEvent event) {
		if (event.getLine(1).equalsIgnoreCase("[Date]")) {
			java.util.Date date = new java.util.Date();
			SimpleDateFormat f1 = new SimpleDateFormat("yyyy/MM/dd");
			SimpleDateFormat f2 = new SimpleDateFormat("HH:mm");
			event.setLine(1, f1.format(date));
			event.setLine(2, "[" + dayOfWeekToString() + "]");
			event.setLine(3, f2.format(date));
			dateSignLocations.add(event.getBlock().getLocation());
		}
	}

	// date看板が破壊された時の処理
	@EventHandler
	public void onRemoveSign(BlockBreakEvent event) {
		if (dateSignLocations.contains((event.getBlock().getLocation()))) {
			dateSignLocations.remove(event.getBlock().getLocation());
		}
	}

	// 曜日の取得
	private String dayOfWeekToString() {
		String[] weekName = { ChatColor.RED + "SUN" + ChatColor.RESET, "MON", "TUE", "WED", "THU", "FRI",
				ChatColor.BLUE + "SAT" + ChatColor.RESET };
		return weekName[Calendar.getInstance().get(Calendar.DAY_OF_WEEK) - 1];
	}
}
