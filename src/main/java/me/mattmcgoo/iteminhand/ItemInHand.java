//Plugin by: Mattmcgoo

package me.mattmcgoo.iteminhand;

import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.inventory.ItemStack;

public final class ItemInHand extends JavaPlugin implements Listener {

    @Override
    public void onEnable() {
        getServer().getPluginManager().registerEvents(this, this);
    }

    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();
        String message = event.getMessage();

        //Check if the message contains [i]
        if (message.contains("[i]")) {
            ItemStack heldItem = player.getInventory().getItemInMainHand();
            if (heldItem != null && heldItem.getType() != null) {
                String itemName = heldItem.getItemMeta().getDisplayName();//Get item name for display
                if (itemName == null || itemName.isEmpty()) {
                    itemName = heldItem.getType().toString();//Use item type if no custom name
                }

                //Check if item name contains color codes
                if (itemName.contains("&")) {
                    itemName = ChatColor.translateAlternateColorCodes('&', itemName);
                }

                //Check if player has multiple of the item in their hand
                int quantity = heldItem.getAmount();
                String displayMessage = message.replace("[i]", itemName + " x" + quantity);
                event.setMessage(displayMessage);
            }
        }
    }
}