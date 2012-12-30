package com.theSwak.BandageMe;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import org.bukkit.ChatColor;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class BandageMe extends JavaPlugin {

	public final Logger logger = Logger.getLogger("Minecraft");
	public Map<Player, Boolean> playerCoolDowns = new HashMap<Player, Boolean>();

	private final BandagePlayerListener playerListener = new BandagePlayerListener(this);

	@Override
	public void onDisable() {
		PluginDescriptionFile pdfFile = this.getDescription();
		this.logger.info(pdfFile.getName() + " is now disabled.");

	}
	@Override
	public void onEnable() {
		try {
			final FileConfiguration config = this.getConfig();
			config.options().copyDefaults(true);
			saveConfig();
		} catch(Exception e1) {
			e1.printStackTrace();
		}
		
		final PluginManager pm = getServer().getPluginManager();
		pm.registerEvents(playerListener, this);
		
		PluginDescriptionFile pdfFile = this.getDescription();
		this.logger.info(pdfFile.getName() + " version " + pdfFile.getVersion() + " is now enabled.");
	}

	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
		Player player = null;
		World world = null;
		
		if(sender instanceof Player) {
			player = (Player) sender;
			world = player.getWorld();
		}
		
		if(commandLabel.equalsIgnoreCase("bandageme")) {
			if (args.length == 0) {
				if (sender == null) {
					logger.info("BandageMe: Give the ability to heal another living creature or player.");
				} else {
					Location location = player.getLocation();
					world.playEffect(location, Effect.MOBSPAWNER_FLAMES, 1);
					player.sendMessage(ChatColor.GOLD + "BandageMe: Given the ability to heal another living creature or player.");
				}
				return true;
			} else if (args.length == 1) {
				if (sender != null) {
					if(player.getServer().getPlayer(args[0]) != null) {
						Player targetplayer = player.getServer().getPlayer(args[0]);
						Location location = targetplayer.getLocation();
						world.playEffect(location, Effect.SMOKE, 3);
						targetplayer.sendMessage(ChatColor.GRAY + player.getDisplayName() + " is checking your health.");
						player.sendMessage(ChatColor.GOLD + targetplayer.getDisplayName() + " has " + Integer.toString(targetplayer.getHealth()/2) + " hearts.");
						return true;
					} else {
						player.sendMessage(ChatColor.RED + "Error: The player is offline.");
					}
				}
			} else if (args.length > 1) {
				player.sendMessage(ChatColor.RED + "Error: Too many arguments!");
			}
		}
		return false;
	}
	
	// METHODS
	protected void checkPlayerHealth() {
		// add the console command check player health, only should be available with people who have permissions/admin.
	}
	
	protected void healPlayer() {
		// pass player argument and heal function
	}
	
	
}