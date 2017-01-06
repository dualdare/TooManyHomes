package com.gmail.doubledare1202.listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import com.gmail.doubledare1202.Actions;
import com.gmail.doubledare1202.TooManyHomes;

public class HomesPlayerListener implements Listener{
	private TooManyHomes plugin;
	private Actions actions;
	//コントラスタ
	public HomesPlayerListener(TooManyHomes plugin, Actions actions) {
		this.plugin = plugin;
		this.actions = actions;
	}
	// no use this class
	@EventHandler(priority = EventPriority.LOWEST)
	public void LowestLogin(PlayerJoinEvent event) {
		/*
		Player player = event.getPlayer();
		CommandSender sender = (CommandSender) player;
		Bukkit.getServer().dispatchCommand(sender,
				"tellraw @a {\"text\":\"free stuff\",\"clickEvent\":"
				+ "{\"action\":run_command\",\"value\":\"/i 64\""+
			  "64\"}}");
		 */
	}
}
