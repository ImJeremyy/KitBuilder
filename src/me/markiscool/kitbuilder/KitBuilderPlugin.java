package me.markiscool.kitbuilder;

import me.markiscool.kitbuilder.kit.KitManager;
import org.bukkit.plugin.java.JavaPlugin;

public class KitBuilderPlugin extends JavaPlugin {

    private KitManager kitManager;

    @Override
    public void onEnable() {
        registerManagers();
    }

    @Override
    public void onDisable() {

    }

    private void registerManagers() {
        kitManager = new KitManager(this);
    }

    public KitManager getKitManager() {
        return kitManager;
    }

}
