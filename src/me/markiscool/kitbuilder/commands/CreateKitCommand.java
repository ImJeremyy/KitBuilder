package me.markiscool.kitbuilder.commands;

import me.markiscool.kitbuilder.utility.Chat;
import me.markiscool.kitbuilder.utility.Lang;
import me.markiscool.kitbuilder.utility.Perm;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CreateKitCommand implements CommandExecutor {

    private String prefix;

    public CreateKitCommand() {
        prefix = Lang.PREFIX.getMessage();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if(sender instanceof Player) {
            Player player = (Player) sender;
            if(player.hasPermission(Perm.CREATE_KIT.getPermission())) {
                if(args.length == 1) {
                    //TODO
                    //1. Create new Kit Object
                    //2. Open kit gui
                } else {
                    player.sendMessage(prefix + Lang.INVALID_ARGUMENTS.getMessage() + Chat.colourize(" &7/createkit <kit_name>"));
                }
            } else {
                player.sendMessage(prefix + Lang.NO_PERMISSION.getMessage());
            }
        } else {
            sender.sendMessage(prefix + Lang.NOT_A_PLAYER.getMessage());
        }
        return true;
    }
}
