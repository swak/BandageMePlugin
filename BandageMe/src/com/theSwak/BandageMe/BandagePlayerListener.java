package com.theSwak.BandageMe;

import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerListener;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class BandagePlayerListener extends PlayerListener {
	public static BandageMe plugin; // reference to plug-in

	private static Material bandageMaterial = Material.STRING; // material to use for bandage
	private static int bandageMatUsage = 4; // amount of material to use

	public BandagePlayerListener(BandageMe instance) { // CONSTRUCTOR
		plugin = instance;
	}


	// BUKKIT EVENTS
	public void onPlayerInteractEntity(PlayerInteractEntityEvent event) { // on player right-click interact with an entity
		Player player = event.getPlayer(); // current player

		if (player.isSneaking() && player.getItemInHand().getType() == bandageMaterial) { // if player is sneaking with string in hand while right-clicking on an entity
			ItemStack bandageItemUse = new ItemStack(bandageMaterial, bandageMatUsage); // create item stack to drop

			if (player.getItemInHand().getAmount() >= bandageItemUse.getAmount()) { // if player has enough item in hand 
				Entity targetCreature = event.getRightClicked();

				player.sendMessage("Attempting to heal " + targetCreature.toString()); // tell the player the target they are healing
				removeBandage(player, bandageItemUse); // remove bandage from player

				//
				if (targetCreature != null && targetCreature instanceof LivingEntity) {
					player.sendMessage("creature ok");
				}
			}
		}
	}

	// METHODS
	public void removeBandage(Player player, ItemStack bandageItemUse) { // remove bandage from player
		Inventory pInventory = player.getInventory();
		pInventory.removeItem(bandageItemUse);

	}

}