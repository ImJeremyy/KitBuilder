package me.markiscool.kitbuilder;

import me.markiscool.kitbuilder.commands.*;
import me.markiscool.kitbuilder.kit.KitManager;
import me.markiscool.kitbuilder.listeners.ChatListener;
import me.markiscool.kitbuilder.listeners.GUIClickListener;
import me.markiscool.kitbuilder.utility.Lang;
import me.markiscool.kitbuilder.utility.Metrics;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.event.Listener;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * Main plugin class
 * onEnable() is called when the server starts
 *
 * To do list (if I have free time):
 * - Add an armor & off hand slot in the kit editor
 * - Make KitCommand more readable (with the time stamps and stuff)
 *
 * TODO right away:
 * - Allow owners to give kits to others /kit <name> [player]
 * - Add cost editor to kit gui edit thingy
 * - Check for 1.14. compatability
 *
 * New Permission:
 * - kitbuilder.nocharge
 */
public class KitBuilderPlugin extends JavaPlugin {

    private ChatListener chatListener;
    private KitManager kitManager;
    private Economy economy;

    /**
     * Run when the server turns on
     */
    @Override
    public void onEnable() {
        registerManagers();
        registerListeners();
        registerCommands();
        new Metrics(this); //bStats
    }

    /**
     * Run when server is shutting down
     */
    @Override
    public void onDisable() {
        kitManager.push();
    }

    /**
     * Registeres manager classes as well as the main config (config.yml).
     */
    private void registerManagers() {
        //create data folder, in case it doesn't exist.
        if(!getDataFolder().exists()) {
            getDataFolder().mkdir();
        }
        //checking for config default values
        if(!getConfig().contains("debug-mode")) {
            getConfig().set("debug-mode", false); //note - set debug mode to false if you want your server to be less laggy.
        }
        if(!getConfig().contains("prefix")) {
            getConfig().set("prefix", Lang.PREFIX.getMessage());
        } else {
            Lang.PREFIX.setMessage(getConfig().getString("prefix"));
        }
        saveConfig();

        //creating kit manager object
        kitManager = new KitManager(this);
        if(!registerEconomy()) {
            economy = null;
        }
    }

    /**
     * Regsiters listeners
     * See me.markiscool.kitbuilder.listeners package for all the listeners
     */
    private void registerListeners() {
        Object[] listeners = {
                chatListener = new ChatListener(),
                new GUIClickListener(this)
        };
        PluginManager pm = getServer().getPluginManager();
        for(Object o : listeners) {
            pm.registerEvents((Listener) o, this);
        }
    }

    /**
     * Registers commands
     * Commands must also correspond with plugin.yml "commands" section
     */
    private void registerCommands() {
        getCommand("createkit").setExecutor(new CreateKitCommand(this));
        getCommand("kit").setExecutor(new KitCommand(this));
        getCommand("kits").setExecutor(new KitsCommand(this));
        getCommand("editkit").setExecutor(new EditKitCommand(this));
        getCommand("kitbuilder").setExecutor(new KitBuilderCommand(this));
    }

    private boolean registerEconomy() {
        if (getServer().getPluginManager().getPlugin("Vault") == null) {
            return false;
        }
        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            return false;
        }
        economy = rsp.getProvider();
        return economy != null;
    }

    /**
     * Get the instance of KitManager
     * @return KitManager instance
     */
    public KitManager getKitManager() {
        return kitManager;
    }

    /**
     * Get the instance of ChatListener
     * @return ChatListener instance
     */
    public ChatListener getChatListener() {
        return chatListener;
    }

    /**
     * Get the instance of Economy
     * @return Economy instance
     */
    public Economy getEconomy() {
        return economy;
    }

    /**
     * Returns 20 (1 second). if debug mode is true. Otherwise, returns 180000 (15 minutes)
     * 20 ticks = 1 second.
     * Note: Set debug-mode to false if you want your server to be less laggy
     * @return Push scheduler delay in ticks
     */
    public int getDelay() {
        return getConfig().getBoolean("debug-mode") ? 20 : 18000;
    }


}
