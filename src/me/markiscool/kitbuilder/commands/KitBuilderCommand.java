package me.markiscool.kitbuilder.commands;

import me.markiscool.kitbuilder.KitBuilderPlugin;
import me.markiscool.kitbuilder.utility.Chat;
import me.markiscool.kitbuilder.utility.Lang;
import me.markiscool.kitbuilder.utility.Perm;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.List;

/**
 * Help command
 * - Shows a list of commands
 */
public class KitBuilderCommand implements CommandExecutor {

    private String prefix;

    /**
     * Initializes String prefix
     * @param plugin Main instance of plugin
     */
    public KitBuilderCommand(KitBuilderPlugin plugin) {
        prefix = Lang.PREFIX.getMessage();
    }

    /**
     * /kitbuilder [help]
     * @return always true
     */
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if(sender.hasPermission(Perm.KIT_BUILDER.getPermission())) {
            sendHelpMessage(sender);
        } else {
            sender.sendMessage(prefix + Lang.NO_PERMISSION.getMessage());
        }
        return true;
    }

    /**
     * Sends them the help message
     * @param sender CommandSender to send message to
     */
    private void sendHelpMessage(CommandSender sender) {
        sender.sendMessage(prefix + Chat.colourize("&6Made by: MarkIsCool"));
        sender.sendMessage(Chat.colourize("&b&lKitBuilder Commands:"));
        sender.sendMessage(Chat.colourize("&a/kitbuilder [help] &f- &bOpens this message"));
        sender.sendMessage(Chat.colourize("&a/createkit <kit_name> &f- &bCreate a new kit and opens the editor GUI"));
        sender.sendMessage(Chat.colourize("&a/editkit <kit_name> &f- &bOpens the kit editor GUI"));
        sender.sendMessage(Chat.colourize("&a/kit <kit_name> &f- &bCreates a new kit"));
        sender.sendMessage(Chat.colourize("&a/kits &f- &bList of kits"));
    }
}
