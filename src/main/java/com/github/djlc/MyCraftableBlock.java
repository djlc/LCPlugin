package com.github.djlc;

import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;

public interface MyCraftableBlock extends MySerializable {

	@EventHandler
	public void onBlockPlace(BlockPlaceEvent event);

	@EventHandler
	public void onBlockBreak(BlockBreakEvent event);

}
