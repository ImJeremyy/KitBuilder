package me.markiscool.kitbuilder.kit;

import me.markiscool.kitbuilder.utility.Items;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionDefault;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Kit {

    private String name;
    private Permission permission;
    private Map<Integer, ItemStack> items;
    private Inventory gui;
    private Inventory kitgui;

    private final String permissionBeginning = "kitbuilder.kit.";

    /**
     * This constructor is for creating entirely new kits.
     * @param name Name of the kit
     */
    public Kit(final String name) {
        this.name = name;
        this.permission = new Permission(permissionBeginning + name.toLowerCase());
        this.permission.setDefault(PermissionDefault.OP);
        this.items = new HashMap<>();
        this.generateGUI();
        this.generateKitGUI();
    }

    /**
     * This constructor if for pulling from .yml files.
     * @param name Name of the kit
     * @param kitItems .yml section where items are held
     */
    public Kit(final String name, final ConfigurationSection kitItems) {
        this.name = name;
        this.permission = new Permission(permissionBeginning + name.toLowerCase());
        this.permission.setDefault(PermissionDefault.OP);
        this.items = new HashMap<>();
        this.generateGUI();

        if(kitItems != null) {
            for(String i : kitItems.getKeys(false)) {
                //eliminates the _# at the end of each material name and gets the material name in the end
                char[] m = i.toCharArray();
                char[] f = new char[m.length - 2];
                for(int l = 0; l < f.length; l++) {
                    f[l] = m[l];
                }
                String matName = new String(f).toLowerCase();
                Material material = Material.valueOf(matName.toUpperCase());
                int amount = kitItems.getInt(i + ".amount");
                ItemStack item = new ItemStack(material, amount);
                ItemMeta meta = item.getItemMeta();
                if(kitItems.contains(i + ".meta")) {
                    meta.setDisplayName(kitItems.getString(i + ".meta.display_name"));
                    if(kitItems.contains(i + ".meta.lore")) {
                        meta.setLore(new ArrayList<>(kitItems.getStringList(i + ".meta.lore")));
                    }
                }
                if(kitItems.contains(i + ".enchantments")) {
                    for(String ench : kitItems.getConfigurationSection(i + ".enchantments").getKeys(false)) {
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
        for (int i = 0; i < gui.getSize(); i ++) {
            gui.setItem(i, Items.blank);
        }
        gui.setItem(10, Items.kit);
        gui.setItem(13, Items.receive);
        gui.setItem(16, Items.delete);
    }

    /**
     * Generates the Kit GUI - only meant to be used in constructor
     */
    public void generateKitGUI() {
        kitgui = Bukkit.createInventory(null, 54, name);
        for(Map.Entry<Integer, ItemStack> entry : items.entrySet()) {
            ItemStack item = entry.getValue();
            int slot = entry.getKey();
            kitgui.setItem(slot, item);
        }
        for(int i = 36; i < 45; i++) {
            kitgui.setItem(i, Items.black);
        }
        kitgui.setItem(53, Items.save);
    }

    /**
     * @return Name of the kit
     */
    public String getName() {
        return name;
    }

    /**
     * Will look something like "kitbuilder.kit.NAME"
     * See #getName()
     * @return Permission object
     */
    public Permission getPermission() {
        return permission;
    }

    /**
     * Integer is the inventory slot number
     * ItemStack is the ItemStack/
     * @return Map of Integer-ItemStack
     */
    public Map<Integer, ItemStack> getItems() {
        return items;
    }

    /**
     *
     * @param items
     */
    public void setItems(Map<Integer, ItemStack> items) {
        this.items = items;
    }

    /**
     * @return GUI meant to be opened see GUIClickListener.java
     */
    public Inventory getGUI() {
        return gui;
    }

    /**
     * @return GUI where user edits the kit
     */
    public Inventory getKitGUI() {
        return kitgui;
    }

}
