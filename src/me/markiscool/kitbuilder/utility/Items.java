package me.markiscool.kitbuilder.utility;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;
import java.util.List;

public class Items {

    public static ItemStack blank;
    public static ItemStack kit;
    public static ItemStack delete;
    public static ItemStack receive;
    public static ItemStack black;
    public static ItemStack save;

    static {
        blank = generateItemStack(Material.WHITE_STAINED_GLASS_PANE, "", Arrays.asList("&f-"));
        kit = generateItemStack(Material.DIAMOND_SWORD, "&bEdit this kit", Arrays.asList("&aAdd and remove items in the kit"));
        delete = generateItemStack(Material.BARRIER, "&cDelete this kit", Arrays.asList("&cYOU CANNOT UNDO THIS!"));
        receive = generateItemStack(Material.EMERALD, "&aReceive this kit", Arrays.asList("&bMake sure you have room in your inventory!"));
        black = generateItemStack(Material.BLACK_STAINED_GLASS_PANE, "", Arrays.asList("&l-x-"));
        save = generateItemStack(Material.EMERALD_BLOCK,"&aSave Changes", Arrays.asList("&aClick this will save the kit changes above."));
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
