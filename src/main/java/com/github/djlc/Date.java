package com.github.djlc;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.block.Sign;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.server.PluginDisableEvent;
import org.bukkit.event.server.PluginEnableEvent;
import org.bukkit.scheduler.BukkitRunnable;

public class Date implements CommandExecutor, Listener {

	// keys
	private static final String COMMAND_NAME = "date";
	private static final String DATESIGN = "datesign";

	// 本体
	private final LCPlugin plugin;

	// 管理する看板の位置
	private List<Location> dateSignLocations = new ArrayList<Location>();

	public Date(LCPlugin plugin) {
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

	// プラグインが有効化されるとき
	private boolean eFlag = false;

	@EventHandler
	private void onPluginEnable(PluginEnableEvent event) {
		if (!eFlag) {
			deserialize();
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
			}.runTaskTimer(plugin, 0, 400);
			eFlag = true;
		}
	}

	private boolean dFlag = false;

	@EventHandler
	public void onPluginDisable(PluginDisableEvent event) {
		if (!dFlag) {
			serialize();
			dFlag = true;
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

	public void serialize() {
		Map<String, List<List<Double>>> data = new HashMap<>();
		for (Location l : dateSignLocations) {
			if (!data.containsKey(l.getWorld().getName())) {
				data.put(l.getWorld().getName(), new ArrayList<List<Double>>());
			}
			data.get(l.getWorld().getName()).add(Arrays.asList(l.getX(), l.getY(), l.getZ()));
		}
		List<Map<String, List<List<Double>>>> temp = new ArrayList<>();
		temp.add(data);
		plugin.getConfig().set(DATESIGN, temp);
	}

	@SuppressWarnings("unchecked")
	private void deserialize() {
		FileConfiguration conf = plugin.getConfig();
		if (conf.contains(DATESIGN)) {
			List<Map<?, ?>> list = conf.getMapList(DATESIGN);
			if (list.size() == 0)
				return;
			Map<?, ?> map = list.get(0);
			for (Map.Entry<?, ?> e : map.entrySet()) {
				String worldName = (String) e.getKey();
				List<List<Double>> pos = (List<List<Double>>) e.getValue();
				for (List<Double> f : pos) {
					dateSignLocations.add(new Location(Bukkit.getWorld(worldName), f.get(0), f.get(1), f.get(2)));
				}
			}
		}
	}
}
