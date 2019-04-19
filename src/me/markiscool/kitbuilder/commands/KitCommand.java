package me.markiscool.kitbuilder.commands;

import me.markiscool.kitbuilder.KitBuilderPlugin;
import me.markiscool.kitbuilder.kit.Kit;
import me.markiscool.kitbuilder.kit.KitManager;
import me.markiscool.kitbuilder.utility.Chat;
import me.markiscool.kitbuilder.utility.Lang;
import me.markiscool.kitbuilder.utility.Perm;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.Map;
import java.util.concurrent.TimeUnit;

import static me.markiscool.kitbuilder.utility.InvUtil.getEmptySlots;

public class KitCommand implements CommandExecutor {

    private String prefix;
    private KitManager m_kit;

    public KitCommand(KitBuilderPlugin plugin) {
        prefix = Lang.PREFIX.getMessage();
        m_kit = plugin.getKitManager();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if(sender instanceof Player) {
            Player player = (Player) sender;
            if(sender.hasPermission(Perm.KIT.getPermission())) {
                if(args.length == 1) {
                    String kitName = args[0];
                    if(m_kit.containsKitName(kitName)) {
                        Kit kit = m_kit.getKit(kitName);
                        if(kit.canReceiveKit(player.getUniqueId())) {
                            if (player.hasPermission(kit.getPermission())) {
                                if (!kit.getItems().isEmpty()) {
                                    Inventory inventory = player.getInventory();
                                    int emptySlots = getEmptySlots(inventory);
                                    if (emptySlots >= kit.getItems().size()) {
                                        for (Map.Entry<Integer, ItemStack> entry : kit.getItems().entrySet()) {
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
                                        if(!player.hasPermission(Perm.NO_COOLDOWNS.getPermission())) {
                                            kit.addCooldownPlayer(player.getUniqueId(), System.currentTimeMillis());
                                        }
                                        player.sendMessage(prefix + Chat.colourize("&aSuccessfully received kit &6" + kit.getName()));
                                    } else {
                                        player.sendMessage(prefix + Chat.colourize("&cYour inventory is full!"));
                                    }
                                } else {
                                    player.sendMessage(prefix + Chat.colourize("&cThis kit is empty!"));
                                }
                            } else {
                                player.sendMessage(prefix + Lang.NO_PERMISSION.getMessage());
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
                        player.sendMessage(prefix + Chat.colourize("&cKit not found."));
                    }
                } else {
                    player.sendMessage(prefix + Lang.INVALID_ARGUMENTS.getMessage());
                }
            } else {
                sender.sendMessage(prefix + Lang.NO_PERMISSION.getMessage());
            }
        } else {
            sender.sendMessage(prefix + Lang.NOT_A_PLAYER.getMessage());
        }
        return true;
    }
}
