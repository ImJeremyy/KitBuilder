package me.markiscool.kitbuilder.listeners;

import me.markiscool.kitbuilder.kit.Kit;
import me.markiscool.kitbuilder.utility.Chat;
import me.markiscool.kitbuilder.utility.Lang;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Class listens for chat. See GUIClickListener.java. That class uses add(UUID, Kit, Int) method and
 * remove() as well. It handles editing attributes of classes, like the name and cooldown of the kit.
 */
public class ChatListener implements Listener {

    private Map<UUID, Kit> uuids;
    private Map<UUID, Integer> tasks;
    private String prefix;

    /**
     * Intitializes the maps and prefix
     */
    public ChatListener() {
        uuids = new HashMap<>();
        tasks = new HashMap<>();
        prefix = Lang.PREFIX.getMessage();
    }

    /**
     * Adds to both maps
     * @param uuid uuid of the player
     * @param kit kit to edit
     * @param task task = 0, then changing cooldown, if the task = 1, then we're changing the cost
     */
    public void add(UUID uuid, Kit kit, int task) {
        uuids.put(uuid, kit);
        tasks.put(uuid, task);
    }

    /**
     * Removes from both maps
     * @param uuid
     */
    public void remove(UUID uuid) {
        uuids.remove(uuid);
        tasks.remove(uuid);
    }

    /**
     * Chat listener, for when they chat to change attributes of the kit
     * @param event
     */
    @EventHandler
    public void onChat(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();
        UUID uuid = player.getUniqueId();
        if(uuids.containsKey(uuid)) {
            Kit kit = uuids.get(player.getUniqueId());
            event.setCancelled(true);
            int task = tasks.get(uuid);
            if (task == 0) {
                double seconds = 0;
                String message = event.getMessage();
                try {
                    seconds = Double.parseDouble(message);
                } catch (Exception ex) {
                    player.sendMessage(prefix + Chat.colourize("&cInvalid input. Make sure it's a number."));
                }
                kit.setCooldown((int) seconds);
                player.sendMessage(prefix + Chat.colourize("&aKit &6" + kit.getName() + " &anow has a cooldown of &6" + kit.getCooldown()));
            } else if(task == 1) {
                String name = event.getMessage();
                kit.setName(name);
                player.sendMessage(prefix + Chat.colourize("&aKit &6" + kit.getName() + " &anow has a name of " + kit.getName()));
            }
            remove(player.getUniqueId());
        }
    }

}
