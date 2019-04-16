package me.markiscool.kitbuilder.utility;

import me.markiscool.kitbuilder.KitBuilderPlugin;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;
import java.util.List;

/**
 * Contains the common ItemStack objects - keeps it in cache so we don't have to keep making new objects
 */
public class Items {

    public static ItemStack blank;
    public static ItemStack kit;
    public static ItemStack delete;
    public static ItemStack receive;
    public static ItemStack black;
    public static ItemStack save;
    public static ItemStack quit;

    static {
        if(KitBuilderPlugin.getVersion() == Version.v1_12_R1) {
            blank = generateItemStack(Material.STAINED_GLASS_PANE, 1, (byte) 0, "", Arrays.asList("&f-"));
            black = generateItemStack(Material.STAINED_GLASS_PANE, 1, (byte) 15,  "", Arrays.asList("&l-x-"));
        } else if(KitBuilderPlugin.getVersion() == Version.v1_13_R1 || KitBuilderPlugin.getVersion() == Version.v1_13_R2) {
            blank = generateItemStack(Material.STAINED_GLASS_PANE, 1, "", Arrays.asList("&f-"));
            black = generateItemStack(Material.STAINED_GLASS_PANE, 1, "", Arrays.asList("&l-x-"));
        }
        kit = generateItemStack(Material.DIAMOND_SWORD, 1, "&bEdit this kit", Arrays.asList("&aAdd and remove items in the kit"));
        delete = generateItemStack(Material.BARRIER, 1, "&cDelete this kit", Arrays.asList("&cYOU CANNOT UNDO THIS!"));
        receive = generateItemStack(Material.EMERALD, 1, "&aReceive this kit", Arrays.asList("&bMake sure you have room in your inventory!"));
        save = generateItemStack(Material.EMERALD_BLOCK,1, "&aSave Changes", Arrays.asList("&aClick this will save the kit changes above."));
        quit = generateItemStack(Material.BARRIER, 1, "&cClose", Arrays.asList("&fClick me to close"));
    }

    /**
     * Quickly create a new ItemStack object not having to go through the ItemMeta stuff
     * @param material - Material of the item
     * @param amount 1 to 64
     * @param name - DisplayName (use & for chat color)
     * @param lore - Use Arrays.asList() (use & for chat color)
     * @return New ItemStack object
     */
    public static ItemStack generateItemStack(Material material, int amount, String name, List<String> lore) {
        ItemStack item = new ItemStack(material, amount);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(Chat.colourize(name));
        if(lore != null)  meta.setLore(Chat.colourize(lore));
        item.setItemMeta(meta);
        return item;
    }

    /**
     * Quickly create a new ItemStack object not having to go through the ItemMeta stuff
     * This method has a byte data parameter for special items (like glass panes)
     * @param material - Material of the item
     * @param amount - 1 to 64
     * @param data - Item data
     * @param name - DisplayName (use & for chat color)
     * @param lore - Use Arrays.asList() (use & for chat color)
     * @return New ItemStack object
     */
    public static ItemStack generateItemStack(Material material, int amount, byte data, String name, List<String> lore) {
        ItemStack item = new ItemStack(material, amount, (short) 0, data);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(Chat.colourize(name));
        if(lore != null)  meta.setLore(Chat.colourize(lore));
        item.setItemMeta(meta);
        return item;
    }

}
