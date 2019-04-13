package me.markiscool.kitbuilder.kit;

import me.markiscool.kitbuilder.KitBuilderPlugin;
import me.markiscool.kitbuilder.utility.Chat;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

public class KitManager {

    private KitBuilderPlugin plugin;

    private Set<Kit> kits;

    private File kitsFile;
    private YamlConfiguration kitscfg;

    public KitManager(KitBuilderPlugin plugin) {
        this.plugin = plugin;
        createFile();
    }

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

    private void saveFile() {
        try {
            kitscfg.save(kitsFile);
        } catch (IOException ex) {
            ex.printStackTrace();
            plugin.getLogger().warning(Chat.colourize("&ckits.yml could not be saved"));
        }
    }

    public void add(Kit kit) {
        if(!kits.contains(kits)) {
            kits.add(kit);
        }
    }

    public void remove(Kit kit) {
        if(kits.contains(kit)) {
            kits.remove(kit);
        }
    }

    public boolean contains(Kit kit) {
        return kits.contains(kit);
    }

    public void push() {

    }

    public void pull() {

    }

}
