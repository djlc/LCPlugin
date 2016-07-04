package com.github.djlc;

import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockDamageEvent;

public final class PlayerListener implements Listener {

	@EventHandler
	public void onPlayerSet(BlockDamageEvent event) {
		if (UserCommandManager.isRunning(event.getPlayer().getName())) {
			Location l = event.getBlock().getLocation().add(0, 1, 0);
		}
	}
}
