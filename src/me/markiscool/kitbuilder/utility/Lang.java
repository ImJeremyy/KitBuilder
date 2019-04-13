package me.markiscool.kitbuilder.utility;

/**
 * This enum contains commonly used phrases like the plugin prefix.
 * Use getMessage() to get the String
 */
public enum Lang {
    PREFIX("&b[&6Kit&aBuilder&b]&r "),
    NOT_A_PLAYER("&cYou are not a player."),
    NO_PERMISSION("&cYou do not have permission to this command."),
    INVALID_ARGUMENTS("&cInvalid arguments.");

    private String message;

    /**
     * Main constructor
     * @param message Insert message here, use & for chat color
     */
    Lang(String message) {
        this.message = message;
    }

    /**
     * Get the colourized message.
     * @return colourized String
     */
    public String getMessage() {
        return Chat.colourize(message);
    }
}
