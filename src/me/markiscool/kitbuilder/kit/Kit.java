package me.markiscool.kitbuilder.kit;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionDefault;

public class Kit {

    private String name;
    private Permission permission;
    private ItemStack[] items;
    private Inventory gui;

    private final String permissionBeginning = "kitbuilder.kit.";

    /**
     * This constructor is for creating entirely new kits.
     * @param name Name of the kit
     */
    public Kit(final String name) {
        this.name = name;
        this.permission = new Permission(permissionBeginning + name.toLowerCase());
        this.permission.setDefault(PermissionDefault.OP);
        this.items = new ItemStack[36];
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
    }

    public String getName() {
        return name;
    }

    public Permission getPermission() {
        return permission;
    }

    public ItemStack[] getItems() {
        return items;
    }

    public Inventory getGUI() {
        return gui;
    }

}
