package com.gmail.doubledare1202;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import lib.PatPeter.SQLibrary.SQLite;

import org.bukkit.plugin.InvalidDescriptionException;
import org.bukkit.plugin.InvalidPluginException;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.UnknownDependencyException;
import org.bukkit.plugin.java.JavaPlugin;
import org.mcstats.Metrics;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import com.gmail.doubledare1202.listener.HomesPlayerListener;

public class TooManyHomes extends JavaPlugin{

	public SQLite sqlite;
	public Actions actions;
	public HomesPlayerListener playerListener;
	public HomesCommands homesExecutor;
	public HomesTabCompleter homesTabCompleter;

	public int Default_MAX_HOMEPOINT_NUM;

	@Override
	public void onEnable() {
		downloadSQLibraly();
		sqlConnection();
		sqlTableCheck();
		actions = new Actions(this,sqlite);

		playerListener = new HomesPlayerListener(this,actions);
		PluginManager pm = getServer().getPluginManager();
		pm.registerEvents(playerListener, this);

		homesExecutor = new HomesCommands(this, actions);
		getCommand("toomanyhomes").setExecutor(homesExecutor);
		homesTabCompleter = new HomesTabCompleter(this);
		getCommand("toomanyhomes").setTabCompleter(homesTabCompleter);

		loadConfig();
		saveConfig();

		String a = getConfig().getString("Default_MAX_HOMEPOINT_NUM");
		Default_MAX_HOMEPOINT_NUM = Integer.parseInt(a);
		try {
			Metrics metrics = new Metrics(this);
			if(metrics.start()){
			//getLogger().info("Sucsessfully send to mcmtrics");
			}
		} catch (IOException e) {
			// Failed to submit the stats :-(
			//getLogger().info("mcmtrics is null");
		}
	}

	@Override
	public void onDisable(){
		if (this.getServer().getPluginManager().isPluginEnabled("SQLibrary")) {
			/*
		try {
			//sqlite.query("VACUUM;");
			getLogger().info("Execute dump;");
			sqlite.query(".output ./dump.txt");
			sqlite.query(".dump");

			sqlite.close();
		} catch (SQLException e) {
			getLogger().info("Failed to dump;");
		}
		sqlite.close();
		}
		*/
		//sqlite.close();
		getLogger().info("TooManyHomes onDisable");
		getServer().getPluginManager().disablePlugin(this);
		}
		sqlite.close();
	}

	public void loadConfig(){
		getConfig().addDefault("Default_MAX_HOMEPOINT_NUM","2");
		getConfig().options().copyDefaults(true);
	}

	public void sqlConnection() {
		//getLogger().info("table_name has been created");
		sqlite = new SQLite(getLogger(),
				"TooManyHomes",//plugin name
				getDataFolder().getAbsolutePath(),//file name
				"toomanyhomes_database");
		try {
			sqlite.open();
		} catch (Exception e) {
			this.getLogger().info(e.getMessage());
			getPluginLoader().disablePlugin(this);
		}
	}

	public void sqlTableCheck() {
		if(sqlite.checkTable("player_homes_list")){
			return;
		}else{
			try{
				sqlite.query("CREATE TABLE player_homes_list (name VARCHAR(50), "
						+ "uuid VARCHAR(50),"
						+ "world VARCHAR(50),"
						+ "x VARCHAR(50),"
						+ "y VARCHAR(50),"
						+ "z VARCHAR(50),"
						+ "vx VARCHAR(50),"
						+ "vy VARCHAR(50),"
						+ "vz VARCHAR(50),"
						+ "PRIMARY KEY(name,uuid));");
			}catch(Exception e){
				getLogger().info("wolf_player has been created");
			}
		}

	}

	public void downloadSQLibraly(){
		if (!this.getServer().getPluginManager().isPluginEnabled("SQLibrary")) {
			try {
				getLogger().info("Downloadind dependecy: SQLibrary");
				URL url = new URL("http://repo.dakanilabs.com/content/repositories/public/lib/PatPeter/SQLibrary/SQLibrary/maven-metadata.xml");
				URLConnection urlConnection = url.openConnection();
				InputStream in = new BufferedInputStream(urlConnection.getInputStream());

				DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
				DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
				Document doc = dBuilder.parse(in);
				final String version = "7.1";//doc.getElementsByTagName("latest").item(0).getTextContent();
				in.close();
				url = new URL("http://repo.dakanilabs.com/content/repositories/public/lib/PatPeter/SQLibrary/SQLibrary/" + version + "/SQLibrary-" + version + ".jar");
				final String path = getDataFolder().getParentFile().getAbsoluteFile() + "/SQLibrary-" + version + ".jar";
				ReadableByteChannel rbc = Channels.newChannel(url.openStream());
				FileOutputStream fos = new FileOutputStream(path);
				fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
				getLogger().info("Finished downloading SQLibrary-" + version + ". Loading dependecy");
				this.getServer().getPluginManager().loadPlugin(new File(path));
				getLogger().info("Loaded SQLibrary");
			} catch (MalformedURLException ex) {
				getLogger().info("Failed to download dependency");
			} catch (IOException ex) {
				getLogger(). info("Failed to download dependency");
			} catch (ParserConfigurationException ex) {
				getLogger().info("Failed to d"
						+ "ownload dependency");
			} catch (SAXException ex) {
				getLogger(). info("Failed to download dependency");
			} catch (InvalidPluginException ex) {
				getLogger().info("Failed to load dependency");
			} catch (InvalidDescriptionException ex) {
				getLogger(). info("Failed to load dependency");
			} catch (UnknownDependencyException ex) {
				getLogger(). info("Failed to load dependency");
			}
		}else{
			getLogger().info("Already downloaded 'SQLibraliy'");
		}
		if (!this.getServer().getPluginManager().isPluginEnabled("SQLibrary")) {
			getLogger().info("TooManyHomes is disabling. Please download 'SQLibrary'");
			getLogger().info("URL 'https://dev.bukkit.org/projects/sqlibrary'");
			getServer().getPluginManager().disablePlugin(this);
		}
	}
}
