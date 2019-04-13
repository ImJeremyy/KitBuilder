package me.markiscool.kitbuilder.utility;

import org.bukkit.ChatColor;

import java.util.ArrayList;
import java.util.List;

public class Chat {

    public static String colourize(String message) {
        return ChatColor.translateAlternateColorCodes('&', message);
    }

    public static List<String> colourize(List<String> message) {
        List<String> m = new ArrayList<String>();
        for (String s : message) {
            m.add(colourize(s));
        }
        return m;
    }

}
