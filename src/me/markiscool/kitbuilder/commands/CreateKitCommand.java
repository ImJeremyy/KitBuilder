package me.markiscool.kitbuilder.commands;

import me.markiscool.kitbuilder.KitBuilderPlugin;
import me.markiscool.kitbuilder.kit.Kit;
import me.markiscool.kitbuilder.kit.KitManager;
import me.markiscool.kitbuilder.utility.Chat;
import me.markiscool.kitbuilder.utility.Lang;
import me.markiscool.kitbuilder.utility.Perm;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CreateKitCommand implements CommandExecutor {

    private String prefix;
    private KitManager m_kit;

    public CreateKitCommand(KitBuilderPlugin plugin) {
        prefix = Lang.PREFIX.getMessage();
        m_kit = plugin.getKitManager();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if(sender instanceof Player) {
            Player player = (Player) sender;
            if(player.hasPermission(Perm.CREATE_KIT.getPermission())) {
                if(args.length == 1) {
                    if(!m_kit.containsKitName(args[0])) {
                        Kit kit = new Kit(args[0]);
                        m_kit.add(kit);
                        player.openInventory(kit.getGUI());
                    } else {
                        player.sendMessage(prefix + Chat.colourize("&cKit with this name already exists. Choose a different name!"));
                    }
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
