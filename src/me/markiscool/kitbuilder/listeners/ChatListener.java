package me.markiscool.kitbuilder.listeners;

import me.markiscool.kitbuilder.kit.Kit;
import me.markiscool.kitbuilder.utility.Chat;
import me.markiscool.kitbuilder.utility.Lang;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.util.*;

public class ChatListener implements Listener {

    private Map<UUID, Kit> uuids;
    private String prefix;

    public ChatListener() {
        uuids = new HashMap<>();
        prefix = Lang.PREFIX.getMessage();
    }

    public void add(UUID uuid, Kit kit) {
        uuids.put(uuid, kit);
    }

    public void remove(UUID uuid) {
        uuids.remove(uuid);
    }

    @EventHandler
    public void onChat(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();
        if(uuids.containsKey(player.getUniqueId())) {
            double seconds;
            String message = event.getMessage();
            if(!message.equals("")) {
                try {
                    seconds = Double.parseDouble(message);
                    event.setCancelled(true);
                    Kit kit = uuids.get(player.getUniqueId());
                    kit.setCooldown((int) seconds);
                    player.sendMessage(prefix + Chat.colourize("&aKit &6" + kit.getName() + " &anow has a cooldown of &6" + seconds));
                    remove(player.getUniqueId());
                } catch (Exception ex) {
                    player.sendMessage(prefix + Chat.colourize("&cInvalid input. Make sure it's a number."));
                }
            } else {
                event.setCancelled(true);
                player.sendMessage(prefix + Chat.colourize("&cJob cancelled."));
            }
        }
    }

}
