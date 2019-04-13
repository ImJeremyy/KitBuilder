package me.markiscool.kitbuilder.utility;

import org.bukkit.ChatColor;

import java.util.ArrayList;
import java.util.List;

/**
 * Chat util class
 * This class contains static methods such as colourize() and strip() which are commonly used
 * methods for chat color usage.
 */
public class Chat {

    /**
     * @param message Message to colourize
     * @return colourized String
     */
    public static String colourize(String message) {
        return ChatColor.translateAlternateColorCodes('&', message);
    }

    /**
     * @param message Message to strip of chat color
     * @return raw message (without chat color)
     */
    public static String strip(String message) {
        return ChatColor.stripColor(message);
    }

    /**
     * Colourize a List<string>
     * @param message the List<String> object you wish to colourize. Use & for chat color
     * @return a new List<String> object with colourized messages
     */
    public static List<String> colourize(List<String> message) {
        List<String> m = new ArrayList<String>();
        for (String s : message) {
            m.add(colourize(s));
        }
        return m;
    }

}
