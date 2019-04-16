package me.markiscool.kitbuilder.commands;

import me.markiscool.kitbuilder.KitBuilderPlugin;
import me.markiscool.kitbuilder.kit.Kit;
import me.markiscool.kitbuilder.kit.KitManager;
import me.markiscool.kitbuilder.utility.Chat;
import me.markiscool.kitbuilder.utility.Lang;
import me.markiscool.kitbuilder.utility.Perm;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.Map;

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
                        if(player.hasPermission(kit.getPermission())) {
                            if (! kit.getItems().isEmpty()) {
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
