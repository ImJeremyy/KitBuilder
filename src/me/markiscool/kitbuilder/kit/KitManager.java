package me.markiscool.kitbuilder.kit;

import me.markiscool.kitbuilder.KitBuilderPlugin;
import me.markiscool.kitbuilder.utility.Chat;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.Set;

public class KitManager {

    private KitBuilderPlugin plugin;

    private Set<Kit> kits;

    private File kitsFile;
    private YamlConfiguration kitscfg;

    /**
     * Main constructor
     * @param plugin Instance of Plugin class
     */
    public KitManager(KitBuilderPlugin plugin) {
        this.plugin = plugin;
        createFile();
        pull();
    }

    /**
     * Attempt to create a .yml file named "kits" within the plugin directory folder.
     * If the .yml file already exists, then it's okay. the FileConfiguration object will still load its contents
     * If the .yml file was made only for the first time, this method will check for a section named "kits" within it.
     * If it doesn't exist, we will create the section and save.
     * Will send warning to console (logger) if exceptions are thrown when trying to create
     */
    private void createFile() {
        kitsFile = new File(plugin.getDataFolder(), "kits.yml");
        try{
            kitsFile.createNewFile();
        } catch (IOException ex) {
            ex.printStackTrace();
            plugin.getLogger().warning(Chat.colourize("&ckits.yml could not be created."));
        }
        kitscfg = YamlConfiguration.loadConfiguration(kitsFile);
        if(kitscfg.contains("kits")) {
            kitscfg.createSection("kits");
            saveFile();
        }
    }

    /**
     * Attempts to any new edits.
     * Will print to console if exceptions are thrown
     */
    private void saveFile() {
        try {
            kitscfg.save(kitsFile);
        } catch (IOException ex) {
            ex.printStackTrace();
            plugin.getLogger().warning(Chat.colourize("&ckits.yml could not be saved"));
        }
    }

    /**
     * Registers a scheduler that will push kit stuff every so often
     */
    private void registerPushScheduler() {
        Bukkit.getScheduler().runTaskTimer(plugin, (Runnable) new BukkitRunnable() {
            @Override
            public void run() {
                push();
            }
        },1000, plugin.getDelay());
    }

    /**
     * Push cache onto kits.yml
     */
    public void push() {
        for(Kit kit : kits) {
            for(Map.Entry<Integer, ItemStack> entry : kit.getItems().entrySet()) {

            }
        }
    }

    /**
     * Pull info on kits.yml and implement them into kits objects
     * and adds them to KitManager
     */
    public void pull() {

    }

    /**
     * Adds a kit if it is not already in the set
     * @param kit kit to add
     */
    public void add(Kit kit) {
        if(!kits.contains(kits)) {
            kits.add(kit);
        }
    }

    /**
     * Removes a kit if it exists within the set
     * @param kit kit to remove
     */
    public void remove(Kit kit) {
        if(kits.contains(kit)) {
            kits.remove(kit);
        }
    }

    /**
     * @param kit kit to see
     * @return true if the kit is found
     */
    public boolean contains(Kit kit) {
        return kits.contains(kit);
    }

    /**
     * Check to see if a kit with a certain name exists
     * @param kitName kit name to check
     * @return true if the kit name is found
     */
    public boolean containsKitName(String kitName) {
        for(Kit k : kits) {
            if(k.getName().equalsIgnoreCase(kitName)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Get kit object from name
     * @return kit object
     */
    public Kit getKit(String kitName) {
        for(Kit kit : kits) {
            if(kit.getName().equalsIgnoreCase(kitName)) {
                return kit;
            }
        }
        return null;
    }

}
