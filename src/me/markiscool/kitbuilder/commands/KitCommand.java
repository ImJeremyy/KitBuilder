package me.markiscool.kitbuilder.commands;

import me.markiscool.kitbuilder.KitBuilderPlugin;
import me.markiscool.kitbuilder.kit.Kit;
import me.markiscool.kitbuilder.kit.KitManager;
import me.markiscool.kitbuilder.utility.Chat;
import me.markiscool.kitbuilder.utility.Lang;
import me.markiscool.kitbuilder.utility.Perm;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import static me.markiscool.kitbuilder.utility.InvUtil.getEmptySlots;

public class KitCommand implements CommandExecutor {

    private String prefix;
    private KitManager m_kit;
    private Economy economy;

    /**
     * Initializes economy instance, KitManager and String prefix
     * @param plugin Main instance of plugin
     */
    public KitCommand(KitBuilderPlugin plugin) {
        prefix = Lang.PREFIX.getMessage();
        m_kit = plugin.getKitManager();
        if(plugin.getEconomy() != null) economy = plugin.getEconomy();

    }

    /**
     * /kit command
     * @return always true
     */
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (args.length == 1) {
            if (sender instanceof Player) {
                Player player = (Player) sender;
                UUID uuid = player.getUniqueId();
                String kitName = args[0];
                if (m_kit.containsKitName(kitName)) {
                    Kit kit = m_kit.getKit(kitName);
                    if (kit.canReceiveKit(uuid)) { //checks for cool downs
                        if (economy != null) {
                            if (economy.getBalance(player) >= kit.getCost()) {
                                if (player.hasPermission(kit.getPermission())) {
                                    if (!kit.getItems().isEmpty()) {
                                        Inventory inventory = player.getInventory();
                                        int emptySlots = getEmptySlots(inventory);
                                        if (emptySlots >= kit.getItems().size()) {
                                            giveKit(inventory, kit.getItems());
                                            if (!player.hasPermission(Perm.NO_CHARGE.getPermission())) {
                                                economy.withdrawPlayer(player, kit.getCost());
                                            }
                                            if (!player.hasPermission(Perm.NO_COOLDOWNS.getPermission())) {
                                                kit.addCooldownPlayer(player.getUniqueId(), System.currentTimeMillis());
                                            }
                                            player.sendMessage(prefix + Chat.colourize("&aSuccessfully received kit &6" + kit.getName()));
                                        } else {
                                            player.sendMessage(prefix + Lang.INVENTORY_FULL.getMessage());
                                        }
                                    } else {
                                        player.sendMessage(prefix + Lang.KIT_EMPTY.getMessage());
                                    }
                                } else {
                                    player.sendMessage(prefix + Lang.NO_PERMISSION.getMessage());
                                }
                            } else {
                                player.sendMessage(prefix + Lang.NOT_ENOUGH_MONEY.getMessage());
                            }
                        } else {
                            if (player.hasPermission(kit.getPermission())) {
                                if (!kit.getItems().isEmpty()) {
                                    Inventory inventory = player.getInventory();
                                    int emptySlots = getEmptySlots(inventory);
                                    if (emptySlots >= kit.getItems().size()) {
                                        giveKit(inventory, kit.getItems());
                                        if (!player.hasPermission(Perm.NO_COOLDOWNS.getPermission())) {
                                            kit.addCooldownPlayer(player.getUniqueId(), System.currentTimeMillis());
                                        }
                                        player.sendMessage(prefix + Chat.colourize("&aSuccessfully received kit &6" + kit.getName()));
                                    } else {
                                        player.sendMessage(prefix + Lang.INVENTORY_FULL.getMessage());
                                    }
                                } else {
                                    player.sendMessage(prefix + Lang.KIT_EMPTY.getMessage());
                                }
                            } else {
                                player.sendMessage(prefix + Lang.NO_PERMISSION.getMessage());
                            }
                        }
                    } else {
                        timeStuff(kit, player);
                    }
                } else {
                    player.sendMessage(prefix + Lang.KIT_NOT_FOUND.getMessage());
                }
            } else {
                sender.sendMessage(prefix + Chat.colourize("&cYou must be a player to run this command."));
            }
        } else if (args.length == 2) {
            String kitName = args[0];
            Player targetPlayer;
            try {
                targetPlayer = Bukkit.getPlayer(args[1]);
            } catch (Exception ex) {
                sender.sendMessage(prefix + Lang.PLAYER_NOT_FOUND.getMessage());
                return true;
            }
            if(m_kit.containsKitName(kitName)) {
                Kit kit = m_kit.getKit(kitName);
                giveKit(targetPlayer.getInventory(), kit.getItems());
                sender.sendMessage(prefix + "&aKit &6" + kit.getName() + " &awas sent to &6" +targetPlayer.getName());
                targetPlayer.sendMessage(prefix + "&aKit received from &6" + sender.getName());
            } else {
                sender.sendMessage(prefix + Lang.KIT_NOT_FOUND.getMessage());
            }
        } else {
            sender.sendMessage(prefix + Lang.INVALID_ARGUMENTS.getMessage());
        }
        return true;
    }

    private void timeStuff(Kit kit, Player player) {
        long secondsLeft = kit.getCooldown() - TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis() - kit.getTimeStamp(player.getUniqueId()));
        long minutesLeft = TimeUnit.SECONDS.toMinutes(secondsLeft);
        long hoursLeft = TimeUnit.MINUTES.toHours(minutesLeft);
        long daysLeft = TimeUnit.HOURS.toDays(hoursLeft);
        long monthsLeft = (long) Math.floor(daysLeft / 30);
        System.out.println(secondsLeft + " " + minutesLeft + " " + hoursLeft + " " + daysLeft + " " + monthsLeft);
        if (monthsLeft <= 0) {
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

    private void giveKit(Inventory inventory, Map<Integer, ItemStack> items) {
        for (Map.Entry<Integer, ItemStack> entry : items.entrySet()) {
            int slot = entry.getKey();
            ItemStack i = entry.getValue();
            ItemStack check = inventory.getItem(slot);
            if (check != null) {
                if (check.getType().equals(Material.AIR)) {
                    inventory.setItem(slot, i);
                } else {
                    inventory.addItem(i);
                }
            } else {
                inventory.setItem(slot, i);
            }
        }
    }

}
