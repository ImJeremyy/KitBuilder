package me.markiscool.kitbuilder.utility;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;
import java.util.List;

public class Items {

    public static ItemStack blank;

    static {
        blank = generateItemStack(Material.WHITE_STAINED_GLASS_PANE, "", Arrays.asList("&f-"));
    }

    /**
     * Quickly create a new ItemStack object not having to go through the ItemMeta stuff
     * @param material - Material of the item
     * @param name - DisplayName (use & for chat color)
     * @param lore - Use Arrays.asList() (use & for chat color)
     * @return New ItemStack object
     */
    public static ItemStack generateItemStack(Material material, String name, List<String> lore) {
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(Chat.colourize(name));
        if(lore != null)  meta.setLore(Chat.colourize(lore));
        item.setItemMeta(meta);
        return item;
    }

}
