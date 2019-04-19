package me.markiscool.kitbuilder.kit;

import me.markiscool.kitbuilder.KitBuilderPlugin;
import me.markiscool.kitbuilder.utility.Chat;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

/**
 * There should only be one instance of this class.
 */
public class KitManager {

    private KitBuilderPlugin plugin;

    private Set<Kit> kits;

    private File kitsFile;
    private FileConfiguration kitscfg;

    /**
     * Main constructor
     * 1. Transfers instance of main Plugin class
     * 2. Attempts to create kits.yml
     * > initializes FileConfiguration object as well
     * 3. Initializes the main Set<Kit> object
     * 4. Pulls from kits.yml
     * > doesn't do anything if file is empty
     * 5. Registers the push scheduler
     * > Sets the periodic delay to 1 second if debug-mode is true, otherwise it is every 15 minutes.
     * @param plugin Instance of Plugin class
     */
    public KitManager(KitBuilderPlugin plugin) {
        this.plugin = plugin;
        createFile();
        kits = new HashSet<>();
        pull();
        registerPushScheduler();
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
        if(!kitscfg.contains("kits")) {
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
        },100, plugin.getDelay());
    }

    /**
     * Push cache onto kits.yml
     */
    public void push() {
        kitscfg.set("kits", null);
        kitscfg.createSection("kits");
        for(Kit kit : kits) {
            String kitName = kit.getName();
            String permissionNode = kit.getPermission().getName();
            long cooldown = kit.getCooldown();
            kitscfg.set("kits." + kitName + ".permission", permissionNode);
            kitscfg.set("kits." + kitName + ".cooldown", cooldown);
            for(Map.Entry<Integer, ItemStack> entry : kit.getItems().entrySet()) {
                ItemStack item = entry.getValue();
                ItemMeta meta = item.getItemMeta();
                int slot = entry.getKey();
                String kitPath = "kits." + kitName + ".kit." + item.getType().name().toLowerCase() + "_0.";
                int c = 0;
                while(kitscfg.contains(kitPath)) {
                    kitPath = "kits." + kitName + ".kit." + item.getType().name().toLowerCase() + "_" + c + ".";
                    c++;
                }
                kitscfg.set(kitPath + "amount", item.getAmount());
                kitscfg.set(kitPath + "slot", slot);
                if(meta != null) {
                    kitscfg.set(kitPath + "meta.display_name", meta.getDisplayName());
                    if(meta.getLore() != null) kitscfg.set(kitPath + "lore", meta.getLore());
                }
                for(Map.Entry<Enchantment, Integer> ench : item.getEnchantments().entrySet()) {
                    int level = ench.getValue();
                    Enchantment enchantment = ench.getKey();
                    kitscfg.set(kitPath + "enchantments." + enchantment.getName(), level);
                }
            }
            String playersPath = "kits." + kitName + ".players";
            kitscfg.createSection(playersPath);
            for(Map.Entry<UUID, Long> entry : kit.getCooldownPlayers().entrySet()) {
                kitscfg.set(playersPath + "." + entry.getKey(), entry.getValue());
            }
        }
        saveFile();
    }

    /**
     * Pull info on kits.yml and implement them into kits objects
     * and adds them to KitManager
     */
    public void pull() {
        kits.clear();
        for(String k : kitscfg.getConfigurationSection("kits").getKeys(false)) {
            ConfigurationSection cfgsec = kitscfg.getConfigurationSection("kits." + k);
            Kit kit = new Kit(k, cfgsec); //this constructor contains pull code
            add(kit);
        }
    }

    /**
     * Adds a kit if it is not already in the set
     * @param kit kit to add
     */
    public void add(Kit kit) {
        if(!kits.contains(kit)) {
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

    /**
     * @return Set<Kit>
     */
    public Set<Kit> getKits() {
        return kits;
    }

}
