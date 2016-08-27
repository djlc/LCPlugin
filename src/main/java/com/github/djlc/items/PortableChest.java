package com.github.djlc.items;

import java.util.Map;

import org.bukkit.configuration.serialization.ConfigurationSerializable;

public class PortableChest implements ConfigurationSerializable {

	@Override
	public Map<String, Object> serialize() {
		
		return null;
	}
	
	public static PortableChest deserialize(Map<String, Object> map) {
		PortableChest instance = new PortableChest();
		return instance;
	}

}
