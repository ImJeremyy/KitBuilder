package me.markiscool.kitbuilder.commands;

import me.markiscool.kitbuilder.KitBuilderPlugin;
import me.markiscool.kitbuilder.kit.Kit;
import me.markiscool.kitbuilder.kit.KitManager;
import me.markiscool.kitbuilder.utility.Chat;
import me.markiscool.kitbuilder.utility.Lang;
import me.markiscool.kitbuilder.utility.Perm;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import java.util.HashSet;
import java.util.Set;

public class KitsCommand implements CommandExecutor {

    private String prefix;
    private KitManager m_kit;

    /**
     * Initializes KitManager instance as well as String prefix
     * @param plugin Main instance of plugin
     */
    public KitsCommand(KitBuilderPlugin plugin) {
        m_kit = plugin.getKitManager();
        prefix = Lang.PREFIX.getMessage();
    }

    /**
     * /kits
     * @return always true
     */
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if(sender.hasPermission(Perm.KITS.getPermission())) {
            Set<Kit> kits = m_kit.getKits();
            if(!kits.isEmpty()) {
                String msg = ChatColor.GOLD + "Kits: "+ ChatColor.WHITE;
                for(Kit kit : kits) {
                    if(sender.hasPermission(kit.getPermission())) {
                        msg += kit.getName()  + ", ";
                    }
                }
                sender.sendMessage(msg);
            } else {
                sender.sendMessage(prefix + Chat.colourize("&cNo kits! &7Create one with /createkit <kit_name>"));
            }
        } else {
            sender.sendMessage(prefix + Lang.NO_PERMISSION.getMessage());
        }
        return true;
    }
}
