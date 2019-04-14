package me.markiscool.kitbuilder;

import me.markiscool.kitbuilder.commands.*;
import me.markiscool.kitbuilder.kit.KitManager;
import me.markiscool.kitbuilder.listeners.GUIClickListener;
import me.markiscool.kitbuilder.utility.Lang;
import org.bukkit.event.Listener;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * Main plugin class
 * onEnable() is called when the server starts
 *
 * To do list (if I have free time):
 * - Add more Lang enums (there are a lot of commonly used phrases that are not currently Lang enums)
 * - Make the GUIClickListener more readable
 * - Add an armor & off hand slot
 * - In /kits, only make the kits they have permission to visible.
 * - Add a nether star in each Kit GUI that shows the permissiono for the kit
 */
public class KitBuilderPlugin extends JavaPlugin {

    private KitManager kitManager;

    /**
     * Run when the server turns on
     */
    @Override
    public void onEnable() {
        registerManagers();
        registerListeners();
        registerCommands();
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
    }

    /**
     * Regsiters listeners
     * See me.markiscool.kitbuilder.listeners package for all the listeners
     */
    private void registerListeners() {
        Object[] listeners = {
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

    /**
     * Get the instance of KitManager
     * @return KitManager instance
     */
    public KitManager getKitManager() {
        return kitManager;
    }

    /**
     * Returns 20 (1 minute). if debug mode is true. Otherwise, returns 180000 (15 minutes)
     * 20 ticks = 1 second.
     * @return Push scheduler delay in ticks
     */
    public int getDelay() {
        return getConfig().getBoolean("debug-mode") ? 10 : 18000;
    }

}
