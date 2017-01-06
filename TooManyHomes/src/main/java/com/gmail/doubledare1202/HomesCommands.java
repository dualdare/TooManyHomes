package com.gmail.doubledare1202;

import java.sql.SQLException;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class HomesCommands implements CommandExecutor{

	public TooManyHomes plugin;
	public Actions actions;

	public HomesCommands(TooManyHomes plugin,Actions actions){
		this.plugin = plugin;
		this.actions = actions;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command arg1, String arg2,
			String[] args) {
		if(!(sender.getName().equalsIgnoreCase("CONSOLE"))){
			if(!(args.length == 0)){
				Player player = (Player)sender;
				if(args.length > 1 && args[0].equalsIgnoreCase("set")){
					if(checkHavePermission(player, "toomanyhomes.default")){
						try {
							actions.set(player,args[1]);
						} catch (SQLException e) {
							plugin.getLogger().info("Error Occurred" + e.toString() + "Please Report to forum");
						}
					}
					return true;
				}else if(args.length > 0 && (args[0].equalsIgnoreCase("list")) || args[0].equalsIgnoreCase("ls")){
					if(checkHavePermission(player, "toomanyhomes.default")){
						try {
							if(args.length == 1){
								actions.list(player);
							}else{
								actions.listOfOtherPlayer(player, args[1]);
							}
						} catch (SQLException e) {
							plugin.getLogger().info("Error Occurred" + e.toString() + "Please Report to forum");
						}
					}
					return true;
				}else if(args.length > 0 && args[0].equalsIgnoreCase("help")){
					if(checkHavePermission(player, "toomanyhomes.default")){
						actions.help(player);
					}
					return true;
				}else if(args.length > 2 && args[0].equalsIgnoreCase("delete")){
					if(checkHavePermission(player, "toomanyhomes.admin")){
						try {
							actions.deleteOtherPoint(player, args[1],args[2]);
						} catch (SQLException e) {
							plugin.getLogger().info("Error Occurred" + e.toString() + "Please Report to forum");
						}
					}
					return true;
				}else if(args.length > 1 && args[0].equalsIgnoreCase("delete")){
					if(checkHavePermission(player, "toomanyhomes.default")){
						try {
							actions.delete(player, args[1]);
						} catch (SQLException e) {
							plugin.getLogger().info("Error Occurred" + e.toString() + "Please Report to forum");
						}
					}
					return true;
				}else if(args.length > 2 && (args[0].equalsIgnoreCase("tp")) || args[0].equalsIgnoreCase("teleport")){
					if(checkHavePermission(player, "toomanyhomes.admin")){
						try {
							actions.teleportToOtherPoint(player, args[1],args[2]);
						} catch (SQLException e) {
							plugin.getLogger().info("Error Occurred" + e.toString() + "Please Report to forum");
						}
					}
					return true;
				}else if(args.length > 0){
					if(checkHavePermission(player, "toomanyhomes.teleport")){
						try {
							actions.teleportToHomePoint(player, args[0]);
						} catch (SQLException e) {
							plugin.getLogger().info("Error Occurred" + e.toString() + "Please Report to forum");
						}
					}
					return true;
				}
			}else{
				String msg = "%logo&fThis Command is &e/toomanyhomes <homePoint>";
				Messenger.message(sender, null, msg, null, null, null, null);
				return true;
			}
		}
		plugin.getLogger().info("Sorry. This plugin Command don't execute from CONSOLE!");
		return true;
	}

	public boolean checkHavePermission(Player player,String permission){
		if(player.hasPermission(permission)){
			return true;
		}else{
			//player.sendMessage("You don't have permission.");
			String msg = "%logo&bSorry.You don't have Permission!";
			Messenger.message(null, player, msg, null, null, null, null);
			return false;
		}
	}

}
