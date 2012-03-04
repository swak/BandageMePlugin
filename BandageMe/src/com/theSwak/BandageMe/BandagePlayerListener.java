package com.theSwak.BandageMe;

import java.util.Random;

import net.minecraft.server.DataWatcher;
import net.minecraft.server.Packet40EntityMetadata;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.craftbukkit.entity.CraftLivingEntity;
import org.bukkit.craftbukkit.entity.CraftPlayer;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class BandagePlayerListener implements Listener {
	static BandageMe plugin; // reference to plug-in

	private static Material bandageMaterial = Material.STRING; // material to use for bandage
	private static int healAmount = 1; // amount to heal per use

	public BandagePlayerListener(BandageMe instance) { // CONSTRUCTOR
		plugin = instance;
	}

	
	@EventHandler
	public void onPlayerInteractEntity(PlayerInteractEntityEvent event) { // on player right-click interact with an entity
		final Player player = event.getPlayer(); // current player

		if (player.isSneaking() && player.getItemInHand().getType() == bandageMaterial) { // if player is sneaking with string in hand while right-clicking on an entity
			
			if (plugin.playerCoolDowns.containsKey(player)) {
				if (plugin.playerCoolDowns.get(player)) {
					ItemStack bandageItemUse = new ItemStack(bandageMaterial, plugin.getConfig().getInt("bandage.usage-amount")); // create item stack to drop

					if (player.getItemInHand().getAmount() >= bandageItemUse.getAmount()) { // if player has enough item in hand 
						Entity targetCreature = event.getRightClicked();

						// if the player's target exists and is a living entity
						if (targetCreature != null && targetCreature instanceof LivingEntity) {
							final LivingEntity livingTarget = (LivingEntity) targetCreature; // reference to living entity target
							
							if (livingTarget.getHealth() < livingTarget.getMaxHealth()) { // if target isn't fully healed
								removeBandage(player, bandageItemUse); // remove bandage from player
								plugin.playerCoolDowns.put(player, false);
								player.sendMessage("trying to heal... " + livingTarget.toString());
								
								plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() { // delayed event that occurs 6 seconds later
									public void run() {
										Random perChance = new Random(); // create random chance
										if (plugin.getConfig().getInt("bandage.healing-chance") >= perChance.nextInt(100)) {
											if (livingTarget.getHealth() < livingTarget.getMaxHealth() - healAmount) {
												livingTarget.setHealth(livingTarget.getHealth() + healAmount); // add heal amount
												playPotionEffect(player, livingTarget, 0x5F82A8, 60);
												player.sendMessage(livingTarget.toString() + " was healed for " + healAmount + " heart(s).");
											} else {
												livingTarget.setHealth(livingTarget.getMaxHealth());
												player.sendMessage(livingTarget.toString() + " was barely damaged.");
											}
										} else {
											player.sendMessage("Bandage failed.");
										}
										plugin.playerCoolDowns.put(player, true); // reset cool down
									}
								}, 120L);
								//
							} else {
								player.sendMessage(livingTarget.toString() + " is fully healed.");
							}
							
						}
					}
				} else {
					player.sendMessage("wait for cooldown");
				}
			} else {
				plugin.playerCoolDowns.put(player, true);
				player.sendMessage("bandage hasn't been used");
			}
			
		}
	}

	// METHODS
	protected void removeBandage(Player player, ItemStack bandageItemUse) { // remove bandage from player
		Inventory pInventory = player.getInventory();
		pInventory.removeItem(bandageItemUse);

	}
	
	// thanks to (forums.bukkit.org/threads/how-to-create-the-swirly-particle-potion-effect.55988)
	protected void playPotionEffect(final Player player, final LivingEntity entity, int color, int duration) { // plays potion effect on target living entity
		final DataWatcher dw = new DataWatcher();
		dw.a(8, Integer.valueOf(0));
		dw.watch(8, Integer.valueOf(color));
		
		Packet40EntityMetadata packet = new Packet40EntityMetadata(entity.getEntityId(), dw);
		((CraftPlayer)player).getHandle().netServerHandler.sendPacket(packet);
		
		Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
			public void run() {
				DataWatcher dwReal = ((CraftLivingEntity)entity).getHandle().getDataWatcher();
				dw.watch(8, dwReal.getInt(8));
				Packet40EntityMetadata packet = new Packet40EntityMetadata(entity.getEntityId(), dw);
				((CraftPlayer)player).getHandle().netServerHandler.sendPacket(packet);
			}
		}, duration);
	}


}