package me.markiscool.kitbuilder.listeners;

import me.markiscool.kitbuilder.KitBuilderPlugin;
import me.markiscool.kitbuilder.kit.Kit;
import me.markiscool.kitbuilder.kit.KitManager;
import me.markiscool.kitbuilder.utility.*;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static me.markiscool.kitbuilder.utility.InvUtil.getEmptySlots;

/**
 * onClick() is called when a player clicks in their inventory
 */
public class GUIClickListener implements Listener {

    private ChatListener chatListener;
    private KitManager m_kit;
    private Economy economy;
    private String prefix;

    public GUIClickListener(KitBuilderPlugin plugin) {
        chatListener = plugin.getChatListener();
        m_kit = plugin.getKitManager();
        prefix = Lang.PREFIX.getMessage();
        if(plugin.getEconomy() != null) economy = plugin.getEconomy();
    }

    /**
     * Checks for the inventory name.. If the inventory name belongs to a kit name, then it will evaluate
     * what to do based on what they clicked.
     * Clicking on air will not do anything.
     * @param event - InventoryClickEvent object passed by the server
     */
    @EventHandler
    public void onClick(InventoryClickEvent event) {
        if(event.getWhoClicked() instanceof Player) {
            Player player = (Player) event.getWhoClicked();
            String inventoryName = player.getOpenInventory().getTitle();
            if(m_kit.containsKitName(inventoryName)) {
                Kit kit = m_kit.getKit(inventoryName);
                ItemStack item = event.getCurrentItem();
                if(item != null) {
                    if (item.equals(Items.kit)) { //open kit gui editor
                        //don't let them take it
                        event.setCancelled(true);
                        if(player.hasPermission(Perm.EDIT_KIT.getPermission())) {
                            //close the original menu
                            player.closeInventory();
                            //update the gui
                            kit.generateKitGUI();
                            //open the kit editor gui
                            player.openInventory(kit.getKitGUI());
                        } else {
                            player.sendMessage(prefix + Lang.NO_PERMISSION.getMessage());
                            player.closeInventory();
                        }
                    } else if (item.equals(Items.delete)) { //delete the kit from KitManager set
                        //don't let them take it
                        event.setCancelled(true);
                        if(player.hasPermission(Perm.EDIT_KIT.getPermission())) {
                            //remove it from the Set<Kit>
                            m_kit.remove(kit);
                            //send them a success message
                            player.sendMessage(prefix + Chat.colourize("&aSuccessfully removed kit &c" + Chat.strip(kit.getName())));
                            //close the inventory
                            player.closeInventory();
                        } else {
                            player.sendMessage(prefix + Lang.NO_PERMISSION.getMessage());
                            player.closeInventory();
                        }
                    } else if (item.equals(Items.receive)) { //receive the kit
                        //don't let them take the item
                        event.setCancelled(true);
                        if(player.hasPermission(Perm.KIT.getPermission())) {
                            if(kit.canReceiveKit(player.getUniqueId())) {
                                if(economy != null) {
                                    if (economy.getBalance(player) >= kit.getCost()) {
                                        if (!kit.getItems().isEmpty()) {
                                            Inventory inventory = player.getInventory();
                                            int emptySlots = getEmptySlots(inventory); //get the empty slots
                                            //if their is room for them to receive the kit...
                                            if (emptySlots >= kit.getItems().size()) {
                                                //give them the kit
                                                for (Map.Entry<Integer, ItemStack> entry : kit.getItems().entrySet()) {
                                                    int slot = entry.getKey();
                                                    ItemStack i = entry.getValue();
                                                    ItemStack check = inventory.getItem(slot); //checks if the desired place is air/null
                                                    if (check != null) {
                                                        if (check.getType().equals(Material.AIR)) {
                                                            inventory.setItem(slot, i);
                                                        } else {
                                                            //that slot is taken..
                                                            inventory.addItem(i);
                                                        }
                                                    } else {
                                                        inventory.setItem(slot, i);
                                                    }
                                                }
                                                player.closeInventory();
                                                if (!player.hasPermission(Perm.NO_CHARGE.getPermission())) {
                                                    economy.withdrawPlayer(player, kit.getCost());
                                                }
                                                if (!player.hasPermission(Perm.NO_COOLDOWNS.getPermission())) {
                                                    kit.addCooldownPlayer(player.getUniqueId(), System.currentTimeMillis());
                                                }
                                                player.sendMessage(prefix + Chat.colourize("&aSuccessfully received kit &6" + kit.getName()));
                                            } else {
                                                player.sendMessage(prefix + Chat.colourize(Lang.INVENTORY_FULL.getMessage()));
                                                player.closeInventory();
                                            }
                                        } else {
                                            player.sendMessage(prefix + Lang.KIT_EMPTY.getMessage());
                                            player.closeInventory();
                                        }
                                    } else {
                                        player.sendMessage(prefix + Lang.NOT_ENOUGH_MONEY.getMessage());
                                    }
                                } else {
                                    if (!kit.getItems().isEmpty()) {
                                        Inventory inventory = player.getInventory();
                                        int emptySlots = getEmptySlots(inventory); //get the empty slots
                                        //if their is room for them to receive the kit...
                                        if (emptySlots >= kit.getItems().size()) {
                                            //give them the kit
                                            for (Map.Entry<Integer, ItemStack> entry : kit.getItems().entrySet()) {
                                                int slot = entry.getKey();
                                                ItemStack i = entry.getValue();
                                                ItemStack check = inventory.getItem(slot); //checks if the desired place is air/null
                                                if (check != null) {
                                                    if (check.getType().equals(Material.AIR)) {
                                                        inventory.setItem(slot, i);
                                                    } else {
                                                        //that slot is taken..
                                                        inventory.addItem(i);
                                                    }
                                                } else {
                                                    inventory.setItem(slot, i);
                                                }
                                            }
                                            player.closeInventory();
                                            if (!player.hasPermission(Perm.NO_CHARGE.getPermission())) {
                                                economy.withdrawPlayer(player, kit.getCost());
                                            }
                                            if (!player.hasPermission(Perm.NO_COOLDOWNS.getPermission())) {
                                                kit.addCooldownPlayer(player.getUniqueId(), System.currentTimeMillis());
                                            }
                                            player.sendMessage(prefix + Chat.colourize("&aSuccessfully received kit &6" + kit.getName()));
                                        } else {
                                            player.sendMessage(prefix + Chat.colourize(Lang.INVENTORY_FULL.getMessage()));
                                            player.closeInventory();
                                        }
                                    } else {
                                        player.sendMessage(prefix + Lang.KIT_EMPTY.getMessage());
                                        player.closeInventory();
                                    }
                                }
                            } else {
                                long secondsLeft = kit.getCooldown() - TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis() - kit.getTimeStamp(player.getUniqueId()));
                                long minutesLeft = TimeUnit.SECONDS.toMinutes(secondsLeft);
                                long hoursLeft = TimeUnit.MINUTES.toHours(minutesLeft);
                                long daysLeft = TimeUnit.HOURS.toDays(hoursLeft);
                                long monthsLeft = (long) Math.floor(daysLeft / 30);
                                System.out.println(secondsLeft + " " + minutesLeft + " " + hoursLeft + " " + daysLeft + " " + monthsLeft);
                                if(monthsLeft <= 0) {
                                    // no months
                                    if (daysLeft <= 0) {
                                        //no days
                                        if (hoursLeft <= 0) {
                                            //no hours
                                            if (minutesLeft <= 0) {
                                                //no minutes
                                                if (secondsLeft <= 0) {
                                                    //no seconds
                                                    System.out.println("weird.. I shouldn't be getting this message right now");
                                                } else {
                                                    //there are seconds
                                                    player.sendMessage(prefix + Chat.colourize("&6" + secondsLeft + " &cseconds left to claim this kit."));
                                                }
                                            } else {
                                                //there are minutes
                                                secondsLeft = secondsLeft % 60;
                                                player.sendMessage(prefix + Chat.colourize("&6" + minutesLeft + " &cminutes and &6" + secondsLeft + " &cseconds left to claim this kit."));
                                            }
                                        } else {
                                            //there are hours
                                            minutesLeft %= 60;
                                            secondsLeft %= 60;
                                            player.sendMessage(prefix + Chat.colourize("&6" + hoursLeft + " &chours, &6" + minutesLeft + " &cminutes and &6" + secondsLeft + " &cseconds left to claim this kit."));
                                        }
                                    } else {
                                        //there are days
                                        daysLeft %= 30;
                                        hoursLeft %= 24;
                                        minutesLeft %= 60;
                                        secondsLeft %= 60;
                                        player.sendMessage(prefix + Chat.colourize("&6" + daysLeft + " &cdays, &6" + hoursLeft + " &chours, &6" + minutesLeft + " &cminutes and &6" + secondsLeft + " &cseconds left to claim this kit."));
                                    }
                                } else {
                                    //there are months
                                    daysLeft %= 30;
                                    hoursLeft %= 24;
                                    minutesLeft %= 60;
                                    secondsLeft %= 60;
                                    player.sendMessage(prefix + Chat.colourize("&6" + monthsLeft + " &cmonths, &6" + daysLeft + " &cdays, &6" + hoursLeft + " &chours, &6" + minutesLeft + " &cminutes and &6" + secondsLeft + " &cseconds left to claim this kit."));
                                }
                            }

                        } else {
                            player.sendMessage(prefix + Lang.NO_PERMISSION.getMessage());
                            player.closeInventory();
                        }
                    } else if (item.equals(Items.save)) { //save the item layout onto kit object
                        event.setCancelled(true);
                        Inventory inventory = event.getClickedInventory();
                        Map<Integer, ItemStack> items = new HashMap<>();
                        for (int i = 0; i < 36; i++) {
                            ItemStack is = inventory.getItem(i);
                            if (is != null) items.put(i, is);
                        }
                        kit.setItems(items);
                        player.closeInventory();
                        player.sendMessage(prefix + Chat.colourize("&aSuccessfully saved kit &6" + kit.getName()));
                    } else if (item.equals(Items.black) || item.equals(Items.blank) || (item.getType().equals(XMaterial.NETHER_STAR.parseMaterial()) && item.getItemMeta().hasLore())) { //separator ItemStack objects
                        //don't let them take it
                        event.setCancelled(true);
                    } else if(item.equals(Items.quit)) {
                        event.setCancelled(true);
                        player.closeInventory();
                    } else if(item.getType().equals(XMaterial.DIAMOND.parseMaterial()) && item.getItemMeta().hasLore()) {
                        event.setCancelled(true);
                        player.closeInventory();
                        player.sendMessage(prefix + Chat.colourize("&aHow long should the cool down be (in seconds)."));
                        chatListener.add(player.getUniqueId(), kit);
                    }
                }
            }
        }
    }
}
