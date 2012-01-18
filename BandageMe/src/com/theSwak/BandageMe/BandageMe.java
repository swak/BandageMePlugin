package com.theSwak.BandageMe;

import java.util.logging.Logger;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class BandageMe extends JavaPlugin {
	public static BandageMe plugin;
	public final Logger logger = Logger.getLogger("Minecraft");
	public final BandagePlayerListener playerListener = new BandagePlayerListener(this);

	@Override
	public void onDisable() {
		PluginDescriptionFile pdfFile = this.getDescription();
		this.logger.info(pdfFile.getName() + " is now disabled.");

	}
	@Override
	public void onEnable() {
		PluginDescriptionFile pdfFile = this.getDescription();
		this.logger.info(pdfFile.getName() + " version " + pdfFile.getVersion() + " is now enabled.");
		PluginManager pm = getServer().getPluginManager();
		pm.registerEvent(Event.Type.PLAYER_INTERACT_ENTITY, this.playerListener, Event.Priority.Normal, this);
	}

	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
		Player player = (Player) sender;
		World world = player.getWorld();

		if(commandLabel.equalsIgnoreCase("bandageme")) {
			if(args.length == 0) {
				Block targetblock = player.getTargetBlock(null, 20);
				Location location = targetblock.getLocation();
				world.strikeLightning(location);
				world.createExplosion(location, 2);
			} else if (args.length == 1) {
				if(player.getServer().getPlayer(args[0]) != null) {
					Player targetplayer = player.getServer().getPlayer(args[0]);
					Location location = targetplayer.getLocation();
					world.strikeLightning(location);
					player.sendMessage(ChatColor.GRAY + "Bandaging player " + targetplayer.getDisplayName());
				} else {
					player.sendMessage(ChatColor.RED + "Error: The player is offline.");
				}
			} else if (args.length > 1) {
				player.sendMessage(ChatColor.RED + "Error: Too many arguments!");
			}
		}

		return false;
	}
}