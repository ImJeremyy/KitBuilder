package me.markiscool.kitbuilder.kit;

import me.markiscool.kitbuilder.utility.Items;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionDefault;

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
        this.items = new HashMap<Integer, ItemStack>();
        this.generateGUI();
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
        this.generateGUI();
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
        gui.setItem(17, Items.delete);
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
     * @return Array of ItemStack objects that the kit contains
     */
    public Map<Integer, ItemStack> getItems() {
        return items;
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
