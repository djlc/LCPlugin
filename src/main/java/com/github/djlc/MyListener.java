package com.github.djlc;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

public final class MyListener implements Listener {

	@EventHandler
	public void onPlayerMove(PlayerMoveEvent event) {
		/*
		Location loc = event.getPlayer().getLocation();
		loc.setY(loc.getY() + 5.0);
		Block b = loc.getBlock();
		b.setType(Material.STONE);
		*/
	}

	public void onLogin(PlayerMoveEvent event) {

	}
}
