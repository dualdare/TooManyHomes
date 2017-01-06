package com.gmail.doubledare1202;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.UUID;

import lib.PatPeter.SQLibrary.SQLite;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

public class Actions {

	private TooManyHomes plugin;
	private SQLite sqlite;

	String msg = "0";

	UUIDFetcher fetcher = new UUIDFetcher(Arrays.asList("evilmidget38", "mbaxter"));

	public Actions(TooManyHomes plugin,SQLite sqlite) {
		this.plugin = plugin;
		this.sqlite = sqlite;
	}

	public void help(Player player) {
		//player.sendMessage("This is toomanyhomes help!");
		//String msg = "0";
		msg = "%= %logo &c- &ehelp %=";
		Messenger.message(null, player, msg, null, null, null, null);
		msg = "&e/toomanyhomes help &fThis Command.";
		Messenger.message(null, player, msg, null, null, null, null);
		msg = "&e/toomanyhomes list &fConfirm your home point's";
		Messenger.message(null, player, msg, null, null, null, null);
		msg = "&e/toomanyhomes set <homeName> &fSet homePoint as <homeName>";
		Messenger.message(null, player, msg, null, null, null, null);
		msg = "&e/toomanyhomes <homeName> &fTeleport your <homePoint>";
		Messenger.message(null, player, msg, null, null, null, null);
		msg = "&e/toomanyhomes delete <homeName> &fDelete your <homePoint>";
		Messenger.message(null, player, msg, null, null, null, null);
		msg = "&e/toomanyhomes teleport <player> <homeName> &fTeleport <player>'s <homePoint>";
		Messenger.message(null, player, msg, null, null, null, null);
		msg = "&e/toomanyhomes delete <player> <homeName> &fDelete <player>'s <homePoint>";
		Messenger.message(null, player, msg, null, null, null, null);
	}

	public void set(Player player,String homeName) throws SQLException{
		Integer ints = plugin.Default_MAX_HOMEPOINT_NUM;
		//player.sendMessage(ints.toString());
		if(player.hasPermission("toomanyhomes.admin")){
			//player.sendMessage("actions.adminにきた");
			Location location = player.getLocation();
			double x = location.getX();
			double y = location.getY();
			double z = location.getZ();
			World world = location.getWorld();
			String worldName = world.getName();
			UUID uuid = player.getUniqueId();
			String id = uuid.toString();

			Vector vector = location.getDirection();
			double vx = vector.getX();
			double vy = vector.getY();
			double vz = vector.getZ();

			/*player.sendMessage("INSERT INTO player_homes_list VALUES('"
					+ homeName +"','"+ id +"','" + worldName + "'," + String.valueOf(x) +","
					+ String.valueOf(y) +","
					+ String.valueOf(z) +");");*/
			boolean noDouble = true;
			try{
				sqlite.query("INSERT INTO player_homes_list VALUES('"
						+ homeName +"','"+ id +"','" + worldName + "'," + String.valueOf(x) +","
						+ String.valueOf(y) +","
						+ String.valueOf(z) +","
						+ String.valueOf(vx) + ","
						+ String.valueOf(vy) + ","
						+ String.valueOf(vz) + ");");
			}catch(SQLException e){
				noDouble = false;
			}
			if(noDouble){
				msg = "%logo&aSucsessfully! &fyou register &e&n"+ homeName
						+ " &f(" + Integer.toString((int)x) + ","+ Integer.toString((int)y)
						+ ","+ Integer.toString((int)z) + "," + worldName + ")";
				Messenger.message(null, player, msg, null, null, null, null);
			}else{
				msg = "%logo&bSorry. &e&n"+homeName+" &fis already used.";
				Messenger.message(null, player, msg, null, null, null, null);
			}
		}else if(checkHomePointNumber(player) < plugin.Default_MAX_HOMEPOINT_NUM){
			//player.sendMessage("actionsにきた");
			Location location = player.getLocation();
			double x = location.getX();
			double y = location.getY();
			double z = location.getZ();
			World world = location.getWorld();
			String worldName = world.getName();
			UUID uuid = player.getUniqueId();
			String id = uuid.toString();

			Vector vector = location.getDirection();
			double vx = vector.getX();
			double vy = vector.getY();
			double vz = vector.getZ();

			/*player.sendMessage("INSERT INTO player_homes_list VALUES('"
					+ homeName +"','"+ id +"','" + worldName + "'," + String.valueOf(x) +","
					+ String.valueOf(y) +","
					+ String.valueOf(z) +");");*/
			boolean noDouble = true;
			try{
				sqlite.query("INSERT INTO player_homes_list VALUES('"
						+ homeName +"','"+ id +"','" + worldName + "'," + String.valueOf(x) +","
						+ String.valueOf(y) +","
						+ String.valueOf(z) +","
						+ String.valueOf(vx) + ","
						+ String.valueOf(vy) + ","
						+ String.valueOf(vz) + ");");
			}catch(SQLException e){
				noDouble = false;
			}
			if(noDouble){
				msg = "%logo&aSucsessfully! &fyou register &e&n"+ homeName
						+ " &f(" + Integer.toString((int)x) + ","+ Integer.toString((int)y)
						+ ","+ Integer.toString((int)z) + "," + worldName + ")";
				Messenger.message(null, player, msg, null, null, null, null);
			}else{
				msg = "%logo&bSorry. &e&n"+homeName+" &fis already used.";
			}
		}else{
			//player.sendMessage("登録できるHomePointがMAXです");
			msg = "%logo&bSorry, &fThe home point you can register is maximum!";
			Messenger.message(null, player, msg, null, null, null, null);
		}
	}

	public void list(Player player) throws SQLException{
		//result.next()で次の行に移動する
		msg = "%= %logo &c- &elist %=";
		Messenger.message(null, player, msg, null, null, null, null);
		UUID uuid = player.getUniqueId();
		String id = uuid.toString();
		ResultSet result = sqlite.query("SELECT * FROM player_homes_list WHERE uuid='"
				+ id + "';");
		CommandSender sender = (CommandSender)player;
		int count = 1;
		boolean isTellraw = false;
		boolean haveHomePoint = false;
		while(result.next()){
			String msg;
			String name = result.getString(1);
			String worldName = result.getString(3);
			int x = result.getInt(4);
			int y = result.getInt(5);
			int z = result.getInt(6);
			if(player.hasPermission("toomanyhomes.admin") || player.isOp() ||
					player.hasPermission("minecraft.bukkit.tellraw") ||
					player.hasPermission("minecraft.command.tellraw") ){
				msg = "tellraw @p [\"\",{\"text\":\""+ count +".\"},"
						+ "{\"text\":\"" + name + "\",\"underlined\":true,\"color\":\"yellow\""
						+ ",\"clickEvent\":{\"action\":\"run_command\",\"value\":"
						+ "\"/toomanyhomes " +name+"\"}"
						+ ",\"hoverEvent\":{\"action\":\"show_text\",\"value\":\"Click Teleport!\"}}"
						+ ",{\"text\":\" \",\"color\":\"yellow\"},{\"text\":"
						+ "\"(" + x + "," + y +"," + z +"," + worldName + ")\"}]\"";
				Bukkit.getServer().dispatchCommand(sender, msg);
				isTellraw = true;
				haveHomePoint = true;
			}else{
				StringBuilder sb = new StringBuilder();
				sb.append(count);
				sb.append(".&e&n" + name + "&f(");
				sb.append(x);
				sb.append(",");
				sb.append(y);
				sb.append(",");
				sb.append(z);
				sb.append(",");
				sb.append(worldName + ")");
				msg = sb.toString();
				Messenger.message(null, player, msg, null, null, null, null);
				haveHomePoint = true;
			}
			count++;
			//perms.get(player.getUniqueId()).unsetPermission("permission.here");
		}
		if(!haveHomePoint){
			msg = "&bSorry,&fyou don't have HomePoint.Please &e/toomanyhomes help";
			Messenger.message(null, player, msg, null, null, null, null);
		}
		if(isTellraw){
			msg = "&eThis is &atellraw mode!";
			Messenger.message(null, player, msg, null, null, null, null);
		}
	}

	public void listOfOtherPlayer(Player player,String playerName) throws SQLException{
		msg = "%= %logo &c- &b"+playerName+"'s &elist %=";
		Messenger.message(null, player, msg, null, null, null, null);
		UUID uuid = null;
		boolean canOut = true;
		/*
		Player totoplayer = Bukkit.getPlayer(playerName);
		if(!(totoplayer.isOnline())){
			try {
				uuid = UUIDFetcher.getUUIDOf(playerName);
				player.sendMessage(uuid.toString());
			} catch (Exception e) {
				msg = "%logo&bSorry. Failed to get &e"+player +"&f's UUID. "
						+ "Maybe the UUID server is down";
				Messenger.message(null, player, msg, null, null, null, null);
				uuid = null;
				canOut = false;
			}
		}else{
			uuid = totoplayer.getUniqueId();
		}
		player.sendMessage(uuid.toString());
	}*/
		try{
			uuid = UUIDFetcher.getUUIDOf(playerName);
			String a = uuid.toString();
		}catch(Exception e){
			msg = "%logo&bSorry. &fFailed to get &e"+playerName +"&f's UUID. "
					+ "Maybe the UUID server is down";
			Messenger.message(null, player, msg, null, null, null, null);
			canOut = false;
		}
		ResultSet result = null;
		if(canOut){
			try{
				String id = uuid.toString();
				result = sqlite.query("SELECT * FROM player_homes_list WHERE uuid='"
						+ id + "';");
				String a = result.getString(1);
			}catch(SQLException e){
				msg = "&bSorry,&e"+playerName+"'s &fHomePoint list was not found...";
				Messenger.message(null, player, msg, null, null, null, null);
				canOut = false;
			}
		}
		if(canOut){
			//String id = uuid.toString();
			/*
			ResultSet result = sqlite.query("SELECT * FROM player_homes_list WHERE uuid='"
					+ id + "';");*/
			CommandSender sender = (CommandSender)player;
			int count = 1;
			boolean isTellraw = false;
			//boolean haveHomePoint = false;
			while(result.next()){
				String msg;
				String name = result.getString(1);
				String worldName = result.getString(3);
				int x = result.getInt(4);
				int y = result.getInt(5);
				int z = result.getInt(6);
				if(player.hasPermission("toomanyhomes.admin") || player.isOp() ||
						player.hasPermission("minecraft.bukkit.tellraw") ||
						player.hasPermission("minecraft.command.tellraw") ){
					msg = "tellraw @p [\"\",{\"text\":\""+ count +".\"},"
							+ "{\"text\":\"" + name + "\",\"underlined\":true,\"color\":\"yellow\""
							+ ",\"clickEvent\":{\"action\":\"run_command\",\"value\":"
							+ "\"/toomanyhomes tp "+playerName+" " +name+"\"}"
							+ ",\"hoverEvent\":{\"action\":\"show_text\",\"value\":\"Click Teleport!\"}}"
							+ ",{\"text\":\" \",\"color\":\"yellow\"},{\"text\":"
							+ "\"(" + x + "," + y +"," + z +"," + worldName + ")\"}]\"";
					Bukkit.getServer().dispatchCommand(sender, msg);
					isTellraw = true;
					//haveHomePoint = true;
				}else{
					StringBuilder sb = new StringBuilder();
					sb.append(count);
					sb.append(".&e&n" + name + "&f(");
					sb.append(x);
					sb.append(",");
					sb.append(y);
					sb.append(",");
					sb.append(z);
					sb.append(",");
					sb.append(worldName + ")");
					msg = sb.toString();
					Messenger.message(null, player, msg, null, null, null, null);
					//haveHomePoint = true;
				}
				count++;
				//perms.get(player.getUniqueId()).unsetPermission("permission.here");
			}
			/*if(!haveHomePoint){
				msg = "&bSorry,you don't have HomePoint &fPlease &e/toomanyhomes help";
				Messenger.message(null, player, msg, null, null, null, null);
			}*/
			if(isTellraw){
				msg = "&eThis is &atellraw mode!";
				Messenger.message(null, player, msg, null, null, null, null);
			}
		}
	}


	public void teleportToHomePoint(Player player, String homeName) throws SQLException{
		if(checkOnList(player.getUniqueId(), homeName, player)){
			Location location = getTeleportLocation(player,homeName,player.getUniqueId());
			//上書きしたlocationにtp
			msg = "%logo&aSucsessfully! &fTeleport to &e&n" + homeName;
			Messenger.message(null, player, msg, null, null, null, null);
			player.teleport(location);
		}else{
			//player.sendMessage("homePointが設定されていません");
			msg = "%logo&bSorry, &fyour &e&n"+homeName +"&f was not found...";
			Messenger.message(null, player, msg, null, null, null, null);
		}
		/*
		//実装はここまでこっからテスト
		UUID uuid = null;
		try {
			uuid = UUIDFetcher.getUUIDOf(player.getName());
		} catch (Exception e) {
			e.printStackTrace();
		}

		player.sendMessage(uuid.toString());
		CommandSender sender = (CommandSender) player;
		Bukkit.getServer().dispatchCommand(sender,
				"tellraw @a [\"\",{\"text\":\"Welcome to \"},"
						+ "{\"text\":\"Minecraft.Tools\",\"clickEvent\":"
						+ "{\"action\":\"run_command\",\"value\":\"/tp 1 1 1\"}}]");
		 */
	}

	public void delete(Player player,String homeName) throws SQLException{
		UUID uuid = player.getUniqueId();
		String id = uuid.toString();
		try{
			ResultSet result = sqlite.query("SELECT name FROM player_homes_list WHERE uuid='"
					+ id + "' AND name = '"
					+ homeName + "';");
			//ここでなにも入ってないなら例外発生
			String a = result.getString(1);

			//player.sendMessage(homeName + "を削除します");
			msg = "%logo&aSucsessfully! &fDelete &e&n"+homeName ;
			Messenger.message(null, player, msg, null, null, null, null);
			sqlite.query("DELETE FROM player_homes_list WHERE uuid='"
					+ id + "' AND name = '"
					+ homeName + "';");

		}catch(SQLException e){
			//player.sendMessage(homeName + "が見つかりませんでした");
			msg = "%logo&bSorry, &fyour &e&n" +homeName+" &fwas not found...";
			Messenger.message(null, player, msg, null, null, null, null);
		}
	}

	public void deleteOtherPoint(Player player,String playerName,String homeName) throws SQLException{
		UUID uuid = null;
		boolean canDelete = true;
		/*
		Player totoplayer = Bukkit.getPlayer(playerName);
		if(!(totoplayer.isOnline())){
			try {
				uuid = UUIDFetcher.getUUIDOf(playerName);
				player.sendMessage(uuid.toString());
			} catch (Exception e) {
				msg = "%logo&bSorry. Failed to get &e"+player +"&f's UUID. "
						+ "Maybe the UUID server is down";
				Messenger.message(null, player, msg, null, null, null, null);
				uuid = null;
				canDelete = false;
			}
		}else{
			uuid = totoplayer.getUniqueId();
		}
		 */
		try{
			uuid = UUIDFetcher.getUUIDOf(playerName);
			String a = uuid.toString();
		}catch(Exception e){
			msg = "%logo&bSorry. &fFailed to get &e"+playerName +"&f's UUID. "
					+ "Maybe the UUID server is down";
			Messenger.message(null, player, msg, null, null, null, null);
			canDelete = false;
		}
		if(canDelete){
			Player toPlayer = Bukkit.getPlayer(uuid);
			//msg = "%logo&aSucsessfully! &fDelete &e" + playerName + "'s &e&n"+ homeName;
			//Messenger.message(null, player, msg, null, null, null, null);
			//delete(toPlayer,homeName);
			//UUID uuid = player.getUniqueId();
			String id = uuid.toString();
			try{
				ResultSet result = sqlite.query("SELECT name FROM player_homes_list WHERE uuid='"
						+ id + "' AND name = '"
						+ homeName + "';");
				//ここでなにも入ってないなら例外発生
				String a = result.getString(1);

				//player.sendMessage(homeName + "を削除します");
				msg = "%logo&aSucsessfully! &fDelete &e" + playerName + "'s &e&n"+ homeName;
				Messenger.message(null, player, msg, null, null, null, null);
				sqlite.query("DELETE FROM player_homes_list WHERE uuid='"
						+ id + "' AND name = '"
						+ homeName + "';");

			}catch(SQLException e){
				//player.sendMessage(homeName + "が見つかりませんでした");
				msg = "%logo&bSorry, &e"+playerName+" &e&n" +homeName+" &fwas not found...";
				Messenger.message(null, player, msg, null, null, null, null);
			}
		}
	}

	public void teleportToOtherPoint(Player player, String playerName,
			String homeName) throws SQLException{
		UUID uuid = null;
		boolean canTp = true;
		/*
		Player toPlayer = Bukkit.getPlayer(playerName);
		if(!(toPlayer.isOnline())){
			try {
				uuid = UUIDFetcher.getUUIDOf(playerName);
				String a = uuid.toString();
			} catch (Exception e) {
				msg = "%logo&bSorry. Failed to get &e"+player +"&f's UUID. "
						+ "Maybe the UUID server is down";
				Messenger.message(null, player, msg, null, null, null, null);
				uuid = null;
				canTp = false;
			}
		}else{
			uuid = toPlayer.getUniqueId();
		}*/
		try{
			uuid = UUIDFetcher.getUUIDOf(playerName);
			String a = uuid.toString();
		}catch(Exception e){
			msg = "%logo&bSorry. &fFailed to get &e"+playerName +"&f's UUID. "
					+ "Maybe the UUID server is down";
			Messenger.message(null, player, msg, null, null, null, null);
			canTp = false;
		}

		if(canTp){
			if(checkOnList(uuid, homeName,player)){
				Location location = getTeleportLocation(player, homeName,uuid);
				msg = "%logo&aSucsessfully! &fTeleport to &e" + playerName+ "'s &e&n"+homeName;
				Messenger.message(null, player, msg, null, null, null, null);
				player.teleport(location);
				//player.sendMessage("checkonList = true");
			}else{
				//player.sendMessage("checkonListに失敗しました。");
				msg = "%logo&bSorry, &e"+ playerName+"'s &e&n" +homeName+" &fwas not found...";
				Messenger.message(null, player, msg, null, null, null, null);
			}
		}else{
			msg = "%logo&bSorry, &e"+ playerName+"'s &e&n" +homeName+" &fwas not found...";
			Messenger.message(null, player, msg, null, null, null, null);
			//player.sendMessage("teleportに失敗しました。");
		}
	}

	public Location getTeleportLocation(Player player , String homeName,UUID uuid) throws SQLException{
		//UUID uuid = player.getUniqueId();
		String id = uuid.toString();
		ResultSet result = null;
		result = sqlite.query("SELECT * FROM player_homes_list WHERE uuid='"
				+ id + "' AND name = '"
				+ homeName + "';");
		Location location = player.getLocation();
		String worldName = result.getString(3);
		double x = result.getDouble(4);
		double y = result.getDouble(5);
		double z = result.getDouble(6);
		Vector vector = location.getDirection();
		double vx = result.getDouble(7);
		double vy = result.getDouble(8);
		double vz = result.getDouble(9);
		World world = plugin.getServer().getWorld(worldName);

		location.setWorld(world);
		location.setX(x);
		location.setY(y);
		location.setZ(z);

		vector.setX(vx);
		vector.setY(vy);
		vector.setZ(vz);
		location.setDirection(vector);
		//上書きしたlocationにtp
		return location;
	}

	//ちゃんとsqliteに指定したuuidのhomePointがあるかどうが
	public boolean checkOnList(UUID uuid,String homeName,Player player){
		boolean check = false;
		String id = uuid.toString();
		ResultSet result;
		//player.sendMessage("checkonListにきた");
		if(!(uuid.equals(null))){
			try{
				result = sqlite.query("SELECT * FROM player_homes_list WHERE uuid='"
						+ id + "' AND name = '"
						+ homeName + "';");
				String a = result.getString(1);
				check = true;
				//player.sendMessage("trynikita");
			}catch(SQLException e){
				//player.sendMessage("見つかりませんでした");
				//player.sendMessage("catch");
				check = false;
			}
		}
		return check;
	}

	//uuidのhomePointはいくつあるか
	public int checkHomePointNumber(Player player){
		//player.sendMessage("checkHomePointにきた");
		ResultSet result;
		String id = player.getUniqueId().toString();
		try{
			result = sqlite.query("SELECT name FROM player_homes_list WHERE uuid='"
					+ id + "';");
			String a = result.getString(1);
		}catch(SQLException e){
			//player.sendMessage("checkHomePointでミス");
			return 0;
		}
		int count = 0;
		try {
			while(result.next()){
				count++;
			}
		} catch (SQLException e) {
			//player.sendMessage("checkHomePointでミス２");
			return 0;
		}
		Integer ints = count;
		//player.sendMessage("countは" + ints.toString());
		return count;
	}

	//playerNameのUUIDを取得 Fetcherがだめな場合はnulllを返してやる
	//廃止
	public UUID getUUID(String playerName){
		Player player = Bukkit.getPlayer(playerName);
		if(player.isOnline()){
			return player.getUniqueId();
		}else{
			try {
				UUID uuid = UUIDFetcher.getUUIDOf(playerName);
				return uuid;
			} catch (Exception e) {
				return null;
			}
		}
	}

}
