package me.markiscool.kitbuilder.utility;

import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

/**
 * Contains static methods that help with inventory things.
 */
public class InvUtil {

    /**
     * @param inventory Inventory to check
     * @return int of how many empty slots there are in said inventory
     */
    public static int getEmptySlots(Inventory inventory) {
        int count = 0;
        for(ItemStack is : inventory.getContents()) {
            if(is == null) {
                count++;
            } else if(is.getType().equals(Material.AIR)) {
                count++;
            }
        }
        return count;
    }
}
