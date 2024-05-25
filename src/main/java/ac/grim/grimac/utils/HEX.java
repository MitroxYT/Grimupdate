package ac.grim.grimac.utils;

import org.bukkit.ChatColor;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HEX {
    private static final Pattern HEX_PATTERN = Pattern.compile("&#([a-fA-F0-9]{6})");

    public static String translateHexColorCodes(String message) {
        Matcher matcher = HEX_PATTERN.matcher(message);
        StringBuffer buffer = new StringBuffer();

        while (matcher.find()) {
            String hexColor = matcher.group(1);
            String replacement = ChatColor.COLOR_CHAR + "x"
                    + ChatColor.COLOR_CHAR + hexColor.charAt(0) + ChatColor.COLOR_CHAR + hexColor.charAt(1)
                    + ChatColor.COLOR_CHAR + hexColor.charAt(2) + ChatColor.COLOR_CHAR + hexColor.charAt(3)
                    + ChatColor.COLOR_CHAR + hexColor.charAt(4) + ChatColor.COLOR_CHAR + hexColor.charAt(5);

            matcher.appendReplacement(buffer, replacement);
        }
        matcher.appendTail(buffer);
        
        return ChatColor.translateAlternateColorCodes('&', buffer.toString());
    }
}
