package com.github.djlc.util;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.serialization.ConfigurationSerializable;

public class SerializableLocation implements ConfigurationSerializable {

	// データ
	private String worldName;
	private double x, y, z;

	// Location
	private Location location = null;

	// Key
	private static final String LOCATON_WORLDNAME = "world";
	private static final String LOCATON_X = "x";
	private static final String LOCATON_Y = "y";
	private static final String LOCATON_Z = "z";

	// コンストラクタ
	public SerializableLocation(Location location) {
		this.location = location;
		worldName = location.getWorld().getName();
		x = location.getX();
		y = location.getY();
		z = location.getZ();
	}

	@Override
	public Map<String, Object> serialize() {
		Map<String, Object> map = new HashMap<>();
		map.put(LOCATON_WORLDNAME, worldName);
		map.put(LOCATON_X, x);
		map.put(LOCATON_Y, y);
		map.put(LOCATON_Z, z);
		return map;
	}

	public static SerializableLocation deserialize(Map<String, Object> map) {
		// デシリアライズしたデータを引数としてLocationを作成

		Location loc = new Location(Bukkit.getWorld((String) map.get(LOCATON_WORLDNAME)), (double) map.get(LOCATON_X),
				(double) map.get(LOCATON_Y), (double) map.get(LOCATON_Z));

		// Locationを引数としてSerializableLocationを作成
		SerializableLocation instance = new SerializableLocation(loc);

		return instance;
	}

	public String getWorldName() {
		return worldName;
	}

	public void setWorldName(String worldName) {
		this.worldName = worldName;
	}

	public double getX() {
		return x;
	}

	public void setX(double x) {
		this.x = x;
	}

	public double getY() {
		return y;
	}

	public void setY(double y) {
		this.y = y;
	}

	public double getZ() {
		return z;
	}

	public void setZ(double z) {
		this.z = z;
	}

	public Location getLocation() {
		return location;
	}

	public void setLocation(Location location) {
		this.location = location;
	}
}
