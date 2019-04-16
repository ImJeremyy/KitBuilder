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
        blank = generateItemStack(XMaterial.WHITE_STAINED_GLASS_PANE, 1, "", Arrays.asList("&f-"));
        black = generateItemStack(XMaterial.BLACK_STAINED_GLASS_PANE, 1, "", Arrays.asList("&f-"));
        kit = generateItemStack(XMaterial.DIAMOND_SWORD, 1, "&bEdit this kit", Arrays.asList("&aAdd and remove items in the kit"));
        delete = generateItemStack(XMaterial.BARRIER, 1, "&cDelete this kit", Arrays.asList("&cYOU CANNOT UNDO THIS!"));
        receive = generateItemStack(XMaterial.EMERALD, 1, "&aReceive this kit", Arrays.asList("&bMake sure you have room in your inventory!"));
        save = generateItemStack(XMaterial.EMERALD_BLOCK,1, "&aSave Changes", Arrays.asList("&aClick this will save the kit changes above."));
        quit = generateItemStack(XMaterial.BARRIER, 1, "&cClose", Arrays.asList("&fClick me to close"));
    }

    /**
     * Quickly create a new ItemStack object not having to go through the ItemMeta stuff
     * @param material - Material of the item
     * @param amount 1 to 64
     * @param name - DisplayName (use & for chat color)
     * @param lore - Use Arrays.asList() (use & for chat color)
     * @return New ItemStack object
     */
    public static ItemStack generateItemStack(XMaterial material, int amount, String name, List<String> lore) {
        ItemStack item = new ItemStack(material.parseMaterial(), amount);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(Chat.colourize(name));
        if(lore != null)  meta.setLore(Chat.colourize(lore));
        item.setItemMeta(meta);
        return item;
    }

}
