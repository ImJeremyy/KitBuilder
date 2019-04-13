package me.markiscool.kitbuilder.commands;

import me.markiscool.kitbuilder.KitBuilderPlugin;
import me.markiscool.kitbuilder.utility.Lang;
import me.markiscool.kitbuilder.utility.Perm;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class KitCommand implements CommandExecutor {

    private String prefix;

    public KitCommand(KitBuilderPlugin plugin) {
        prefix = Lang.PREFIX.getMessage();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if(sender instanceof Player) {
            if(sender.hasPermission(Perm.KITS.getPermission())) {
                //TODO
            } else {
                sender.sendMessage(prefix + Lang.NO_PERMISSION.getMessage());
            }
        } else {
            sender.sendMessage(prefix + Lang.NOT_A_PLAYER.getMessage());
        }
        return false;
    }
}
