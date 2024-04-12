//Plugin by: Mattmcgoo

package me.mattmcgoo.iteminhand;

import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;

import java.util.List;

public final class ItemInHand extends JavaPlugin implements Listener {

    @Override
    public void onEnable() {
        getServer().getPluginManager().registerEvents(this, this);
    }

    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();
        String message = event.getMessage();

        // Check if the message contains [i]
        if (message.contains("[i]")) {
            ItemStack heldItem = player.getInventory().getItemInMainHand();
            if (heldItem != null && heldItem.getType() != null) {
                String itemName = heldItem.getItemMeta().getDisplayName();
                if (itemName == null || itemName.isEmpty()) {
                    itemName = heldItem.getType().toString();
                }

                // Format item name with dark gray brackets and yellow color
                itemName = ChatColor.YELLOW + "[" + ChatColor.DARK_GRAY + itemName + ChatColor.YELLOW + "]";

                // Format quantity in yellow
                int quantity = heldItem.getAmount();
                String count = ChatColor.YELLOW + "x" + quantity;

                // Prepare hover text with lore and enchantments
                ItemMeta meta = heldItem.getItemMeta();
                List<String> lore = meta.getLore();
                StringBuilder hoverText = new StringBuilder();
                if (lore != null && !lore.isEmpty()) {
                    for (String loreLine : lore) {
                        hoverText.append(loreLine).append("\n");
                    }
                }
                // Add enchantments
                meta.getEnchants().forEach((enchantment, level) -> {
                    hoverText.append(ChatColor.BLUE).append(enchantment.getKey().getKey()).append(" ").append(level).append("\n");
                });

                // Replace [i] with formatted item name and quantity, and add hover event
                String displayMessage = message.replace("[i]", itemName + " " + count);
                event.setMessage(displayMessage);

                TextComponent itemComponent = new TextComponent(ChatColor.translateAlternateColorCodes('&', displayMessage));
                BaseComponent[] hoverComponents = new BaseComponent[]{new TextComponent(hoverText.toString())};
                itemComponent.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, hoverComponents));

                player.spigot().sendMessage(itemComponent);
            }
        }
    }
}
