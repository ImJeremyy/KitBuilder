package me.markiscool.kitbuilder.commands;

import me.markiscool.kitbuilder.KitBuilderPlugin;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class KitsCommand implements CommandExecutor {

    public KitsCommand(KitBuilderPlugin plugin) {

    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        return false;
    }
}
