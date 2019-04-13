package me.markiscool.kitbuilder;

import me.markiscool.kitbuilder.kit.KitManager;
import me.markiscool.kitbuilder.listeners.GUIClickListener;
import org.bukkit.event.Listener;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class KitBuilderPlugin extends JavaPlugin {

    private KitManager kitManager;

    /**
     * Run when the server turns on
     */
    @Override
    public void onEnable() {
        registerManagers();
    }

    /**
     * Run when server is shutting down
     */
    @Override
    public void onDisable() {

    }

    /**
     * Registeres manager classes as well as the main config (config.yml).
     */
    private void registerManagers() {
        kitManager = new KitManager(this);
        if(!getConfig().contains("debug-mode")) {
            getConfig().set("debug-mode", false);
            saveConfig();
        }
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
        return getConfig().getBoolean("debug-mode") ? 20 : 18000;
    }

}
