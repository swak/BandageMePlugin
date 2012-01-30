package com.theSwak.BandageMe;

import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class BandagePlayerListener implements Listener {
	public static BandageMe plugin; // reference to plug-in

	private static Material bandageMaterial = Material.STRING; // material to use for bandage
	private static int bandageMatUsage = 4; // amount of material per use
	private static int healAmount = 1; // amount to heal per use

	public BandagePlayerListener(BandageMe instance) { // CONSTRUCTOR
		plugin = instance;
	}

	
	@EventHandler
	public void onPlayerInteractEntity(PlayerInteractEntityEvent event) { // on player right-click interact with an entity
		Player player = event.getPlayer(); // current player

		if (player.isSneaking() && player.getItemInHand().getType() == bandageMaterial) { // if player is sneaking with string in hand while right-clicking on an entity
			ItemStack bandageItemUse = new ItemStack(bandageMaterial, bandageMatUsage); // create item stack to drop

			if (player.getItemInHand().getAmount() >= bandageItemUse.getAmount()) { // if player has enough item in hand 
				Entity targetCreature = event.getRightClicked();

				// if the player's target exists and is a living entity
				if (targetCreature != null && targetCreature instanceof LivingEntity) {
					LivingEntity livingTarget = (LivingEntity) targetCreature; // reference to living entity target
					if (livingTarget.getHealth() < livingTarget.getMaxHealth()) { // if target isn't fully healed
						player.sendMessage("You are healing " + targetCreature.toString() + ".");
						removeBandage(player, bandageItemUse); // remove bandage from player
						livingTarget.setHealth(livingTarget.getHealth() + healAmount); // add heal amount
					} else {
						player.sendMessage(targetCreature.toString() + " is fully healed.");
					}
					
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