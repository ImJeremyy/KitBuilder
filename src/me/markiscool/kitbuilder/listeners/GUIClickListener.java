package me.markiscool.kitbuilder.listeners;

import me.markiscool.kitbuilder.KitBuilderPlugin;
import me.markiscool.kitbuilder.kit.Kit;
import me.markiscool.kitbuilder.kit.KitManager;
import me.markiscool.kitbuilder.utility.Chat;
import me.markiscool.kitbuilder.utility.Items;
import me.markiscool.kitbuilder.utility.Lang;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.Map;

public class GUIClickListener implements Listener {

    private KitManager m_kit;
    private String prefix;

    public GUIClickListener(KitBuilderPlugin plugin) {
        m_kit = plugin.getKitManager();
        prefix = Lang.PREFIX.getMessage();
    }

    @EventHandler
    public void onClick(InventoryClickEvent event) {
        if(event.getWhoClicked() instanceof Player) {
            Player player = (Player) event.getWhoClicked();
            String inventoryName = player.getOpenInventory().getTitle();
            if(m_kit.containsKitName(inventoryName)) {
                event.setCancelled(true);
                ItemStack item = event.getCurrentItem();
                Kit kit = m_kit.getKit(inventoryName);
                if(item.equals(Items.kit)) {
                    player.closeInventory();
                    player.openInventory(kit.getKitGUI());
                } else if(item.equals(Items.delete)) {
                    m_kit.remove(kit);
                    player.sendMessage(prefix + Chat.colourize("&aSuccessfully removed kit &c" + Chat.strip(kit.getName())));
                } else if(item.equals(Items.receive)) {
                    Inventory inventory = player.getInventory();
                    int emptySlots = getEmptySlots(inventory);
                    if(emptySlots >= kit.getItems().size()) {
                        for(Map.Entry<Integer, ItemStack> entry : kit.getItems().entrySet()) {
                            int slot = entry.getKey();
                            ItemStack i = entry.getValue();
                            ItemStack check = inventory.getItem(slot);
                            if(check != null) {
                                if(check.getType().equals(Material.AIR)) {
                                    inventory.setItem(slot, i);
                                } else {
                                    inventory.addItem(i);
                                }
                            } else {
                                inventory.setItem(slot, i);
                            }
                        }
                        player.sendMessage(prefix + Chat.colourize("&aSuccessfully received kit &6" + kit.getName()));
                    } else {
                        player.sendMessage(prefix + Chat.colourize("&CYour inventory is fulL!"));
                    }
                }
            }
        }
    }

    private int getEmptySlots(Inventory inventory) {
        int count = 0;
        for(ItemStack is : inventory.getContents()) {
            if(is == null) {
                count++;
            } else if(is.getType().equals(Material.AIR)) {
                count++;
            }
        }
        return count;
    }

}
