package me.markiscool.kitbuilder.kit;

import me.markiscool.kitbuilder.utility.Chat;
import me.markiscool.kitbuilder.utility.Items;
import me.markiscool.kitbuilder.utility.Perm;
import me.markiscool.kitbuilder.utility.XMaterial;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionDefault;

import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * Kit class
 * Holds inventory gui, cooldown, items and more.
 */
public class Kit {

    private final String permissionBeginning = "kitbuilder.kit.";
    private String name;
    private Permission permission;
    private Map<Integer, ItemStack> items;
    private Inventory gui;
    private Inventory kitgui;
    private long cooldown; //in seconds
    private Map<UUID, Long> cooldownPlayers;
    private double cost;

    /**
     * This constructor is for creating entirely new kits.
     * @param name Name of the kit
     */
    public Kit(final String name) {
        this.name = name;
        this.permission = new Permission(permissionBeginning + name.toLowerCase());
        this.permission.setDefault(PermissionDefault.OP);
        this.items = new HashMap<>();
        this.cooldown = 0;
        this.cooldownPlayers = new HashMap<>();
        this.cost = 0;
        this.generateGUI();
        this.generateKitGUI();
    }

    /**
     * This constructor if for pulling from .yml files.
     * @param name       Name of the kit
     * @param kitSection is the .yml section where items are held
     */
    public Kit(final String name, final ConfigurationSection kitSection) {
        this.name = name;
        this.permission = new Permission(permissionBeginning + name.toLowerCase());
        this.permission.setDefault(PermissionDefault.OP);
        this.items = new HashMap<>();
        this.cooldownPlayers = new HashMap<>();
        this.cooldown = kitSection.getLong("cooldown");
        this.cost = kitSection.getInt("cost");
        this.generateGUI();

        ConfigurationSection kitItems = kitSection.getConfigurationSection("kit");
        if (kitItems != null) {
            for (String i : kitItems.getKeys(false)) {
                //eliminates the _# at the end of each material name and gets the material name in the end
                char[] m = i.toCharArray();
                char[] f = new char[m.length - 2];
                for (int l = 0; l < f.length; l++) {
                    f[l] = m[l];
                }
                String matName = new String(f).toLowerCase();
                Material material = Material.valueOf(matName.toUpperCase());
                int amount = kitItems.getInt(i + ".amount");
                ItemStack item = new ItemStack(material, amount);
                ItemMeta meta = item.getItemMeta();
                if (kitItems.contains(i + ".meta")) {
                    meta.setDisplayName(kitItems.getString(i + ".meta.display_name"));
                    if (kitItems.contains(i + ".meta.lore")) {
                        meta.setLore(new ArrayList<>(kitItems.getStringList(i + ".meta.lore")));
                    }
                }
                if (kitItems.contains(i + ".enchantments")) {
                    for (String ench : kitItems.getConfigurationSection(i + ".enchantments").getKeys(false)) {
                        Enchantment enchantment = Enchantment.getByName(ench);
                        int level = kitItems.getInt(i + ".enchantments." + ench);
                        meta.addEnchant(enchantment, level, true);
                    }
                }
                item.setItemMeta(meta);
                int slot = kitItems.getInt(i + ".slot");
                items.put(slot, item);
            }
        }
        this.generateKitGUI();
    }

    /**
     * Generates the GUI - only use this when first created object
     */
    private void generateGUI() {
        gui = Bukkit.createInventory(null, 27, name);
        for (int i = 0; i < gui.getSize(); i++) {
            gui.setItem(i, Items.blank);
        }
        gui.setItem(4, Items.generateItemStack(XMaterial.NETHER_STAR, 1, "&b" + getName(), Arrays.asList("&aPermission node: &7" + getPermission().getName())));
        gui.setItem(10, Items.kit);
        gui.setItem(13, Items.receive);
        gui.setItem(16, Items.delete);
        gui.setItem(18, Items.quit);
    }

    /**
     * Generates the Kit GUI - only meant to be used in constructor
     */
    public void generateKitGUI() {
        kitgui = Bukkit.createInventory(null, 54, name);
        for (Map.Entry<Integer, ItemStack> entry : items.entrySet()) {
            ItemStack item = entry.getValue();
            int slot = entry.getKey();
            kitgui.setItem(slot, item);
        }
        for (int i = 36; i < 45; i++) {
            kitgui.setItem(i, Items.black);
        }
        kitgui.setItem(53, Items.save);
    }

    /**
     * @return GUI meant to be opened see GUIClickListener.java
     */
    public Inventory getGUI() {
        gui.setItem(11, Items.generateItemStack(XMaterial.DIAMOND, 1, "&bChange the Cooldown", Arrays.asList("&eChange the cooldown &6(in seconds)", "&6Current cooldown: &f" + getCooldown())));
        gui.setItem(12, Items.generateItemStack(XMaterial.GOLD_INGOT, 1 , "&bChange the Cost", Arrays.asList("&eSet the cost of the kit.", "&6Current price: &b" + getCost())));
        gui.setItem(15, Items.generateItemStack(XMaterial.SIGN, 1, "&bChange the Name", Arrays.asList("&eSet the name of the kit.", "&6Current name: &b" + getName())));
        return gui;
    }

    /**
     * @return GUI where user edits the kit
     */
    public Inventory getKitGUI() {
        return kitgui;
    }

    /**
     * @return Name of the kit
     */
    public String getName() {
        return name;
    }

    /**
     * Set the name of the kit, automatically colourizes it already
     * @param name String name of the kit
     */
    public void setName(String name) {
        this.name = Chat.colourize(name);
    }

    /**
     * Will look something like "kitbuilder.kit.NAME"
     * See #getName()
     *
     * @return Permission object
     */
    public Permission getPermission() {
        return permission;
    }

    /**
     * Integer is the inventory slot number
     * ItemStack is the ItemStack/
     *
     * @return Map of Integer-ItemStack
     */
    public Map<Integer, ItemStack> getItems() {
        return items;
    }

    /**
     * @param items Set the items of the kit
     */
    public void setItems(Map<Integer, ItemStack> items) {
        this.items = items;
    }

    /**
     * @return the cool down (in seconds)
     */
    public long getCooldown() {
        return cooldown;
    }

    /**
     * @param cooldownSeconds cool down in seconds
     */
    public void setCooldown(long cooldownSeconds) {
        this.cooldown = cooldownSeconds;
    }

    /**
     * @return Map<UUID, Long> of all the players and their stamps of when they obtained the kit
     */
    public Map<UUID, Long> getCooldownPlayers() {
        return this.cooldownPlayers;
    }

    /**
     * @param uuid      UUID of the player player#getUniqueId()
     * @param timeStamp Should be the current time in milliseconds when they took the kit. Use System#currentTimeMilliseconds()
     */
    public void addCooldownPlayer(UUID uuid, long timeStamp) {
        this.cooldownPlayers.put(uuid, timeStamp);
    }

    /**
     * @param uuid UUID of the player player#getUniqueId()
     */
    public void removeCooldownPlayer(UUID uuid) {
        this.cooldownPlayers.remove(uuid);
    }

    /**
     * @param uuid UUID of the player player#getUniqueId()
     * @return true if their found in the map
     */
    public boolean containsCooldownPlayer(UUID uuid) {
        return cooldownPlayers.containsKey(uuid);
    }

    /**
     * @param uuid UUID of the player player#getUniqueId()
     * @return time stamp of when they were added
     */
    public long getTimeStamp(UUID uuid) {
        if (containsCooldownPlayer(uuid)) {
            return this.cooldownPlayers.get(uuid);
        }
        return System.currentTimeMillis();
    }

    /**
     * @param uuid UUID of the player player#getUniqueId()
     * @return true if they can receive the kit
     */
    public boolean canReceiveKit(UUID uuid) {
        if (Bukkit.getPlayer(uuid).hasPermission(Perm.NO_COOLDOWNS.getPermission())) {
            return true;
        }
        if (containsCooldownPlayer(uuid)) {
            long current = TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis());
            long timeStamp = TimeUnit.MILLISECONDS.toSeconds(getTimeStamp(uuid));
            if ((current - timeStamp) >= cooldown) { //if the time passed is greater or equal to the cool down
                removeCooldownPlayer(uuid);
                return true;
            } else { // they are within cool down
                return false;
            }
        }
        //they are not even in the cool down thingy
        return true;
    }

    /**
     * @return cost (double) of the kit
     */
    public double getCost() {
        return cost;
    }

    /**
     * Sets the cost of the plugin
     * @param cost to obtain the kit
     */
    public void setCost(double cost) {
        this.cost = cost;
    }
}
