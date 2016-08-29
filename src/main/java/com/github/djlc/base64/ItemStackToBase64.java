package com.github.djlc.base64;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.bukkit.inventory.ItemStack;
import org.bukkit.util.io.BukkitObjectInputStream;
import org.bukkit.util.io.BukkitObjectOutputStream;
import org.yaml.snakeyaml.external.biz.base64Coder.Base64Coder;

public class ItemStackToBase64 {
	public static String encode(ItemStack itemStack) {
		try {
			ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
			BukkitObjectOutputStream dataOutput = new BukkitObjectOutputStream(outputStream);
			
			// ConfigulationSerializableを実装しているのでそのまま書き出す
			dataOutput.writeObject(itemStack);
			
			// シリアライズ
			dataOutput.close();
			return Base64Coder.encodeLines(outputStream.toByteArray());
		} catch (IOException e) {
			throw new IllegalStateException("Unable to save item stack.", e);
		}
	}
	
	public static ItemStack decode(String data) throws IOException {
		ByteArrayInputStream inputStream = new ByteArrayInputStream(Base64Coder.decodeLines(data));
		try {
			BukkitObjectInputStream dataInput = new BukkitObjectInputStream(inputStream);
			
			// ConfigulationSerializableを実装しているのでそのまま読み込む
			ItemStack itemStack = (ItemStack) dataInput.readObject();
			
			dataInput.close();
			return itemStack;
		} catch (ClassNotFoundException | IOException e) {
			throw new IOException("Unable to decode class type.", e);
		}
	}
}
