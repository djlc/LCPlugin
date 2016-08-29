package com.github.djlc.base64;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.util.io.BukkitObjectInputStream;
import org.bukkit.util.io.BukkitObjectOutputStream;
import org.yaml.snakeyaml.external.biz.base64Coder.Base64Coder;

public class LocationToBase64 {
	public static String encode(Location location) {
		try {
			ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
			BukkitObjectOutputStream dataOutput = new BukkitObjectOutputStream(outputStream);
			
			// ワールド名
			dataOutput.writeUTF(location.getWorld().getName());
			// x座標
			dataOutput.writeDouble(location.getX());
			// y座標
			dataOutput.writeDouble(location.getY());
			// z座標
			dataOutput.writeDouble(location.getZ());
			// yaw
			dataOutput.writeFloat(location.getYaw());
			// pitch
			dataOutput.writeFloat(location.getPitch());
			
			// シリアライズ
			dataOutput.close();
			return Base64Coder.encodeLines(outputStream.toByteArray());
		} catch (IOException e) {
			throw new IllegalStateException("Unable to save location data.", e);
		}
	}
	
	public static Location decode(String data) throws IOException {
		ByteArrayInputStream inputStream = new ByteArrayInputStream(Base64Coder.decodeLines(data));
		try {
			BukkitObjectInputStream dataInput = new BukkitObjectInputStream(inputStream);
			
			// ワールド名
			World world = Bukkit.getServer().getWorld(dataInput.readUTF());
			// x座標
			double x = dataInput.readDouble();
			// y座標
			double y = dataInput.readDouble();
			// z座標
			double z = dataInput.readDouble();
			// yaw
			float yaw = dataInput.readFloat();
			// pitch
			float pitch = dataInput.readFloat();
			
			// デシリアライズ
			dataInput.close();
			return new Location(world, x, y, z, yaw, pitch);
		} catch (IOException e) {
			throw new IOException("Unable to decode class type.", e);
		}
	}
}
