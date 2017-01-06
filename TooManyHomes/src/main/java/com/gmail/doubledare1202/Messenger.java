package com.gmail.doubledare1202;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Messenger {
	private Messenger(){

	}
	public static void message(CommandSender sender, Player player, String message, String four, String five, String six, Double cost) {
		if (message != null) {
			message = message
					.replaceAll("%version", "1.0-alpha")
					.replaceAll("%pl", "&eTooManyHomes")
					.replaceAll("%logo", "&e[TooManyHomes]")
					.replaceAll("%4",four)
					.replaceAll("%5",five)
					.replaceAll("%6",six)
					//.replaceAll("%pl", "&eWe're Wolf")
					.replaceAll("%=", "&c====");
					//.replaceAll("%sender", sender.getName());
					//.replaceAll("%player", player.getName());
			message = ChatColor.translateAlternateColorCodes('&',message);
			if (cost != null) {
				message = message.replaceAll("%costs", Double.toString(cost));
			}
			if (player != null) {
				player.sendMessage(message);
			} else if (sender != null) {
				sender.sendMessage(message);
			}
		}
		// If message is null
		else {
			if (player != null) {
				//player.sendMessage(ChatColor.DARK_RED + "Somehow this message is not defined. Please check your localization.yml");

			} else if (sender != null) {
				//sender.sendMessage(ChatColor.DARK_RED + "Somehow this message is not defined. Please check your localization.yml");

			}
		}
	}
}
