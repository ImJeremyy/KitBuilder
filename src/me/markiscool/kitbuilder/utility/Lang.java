package me.markiscool.kitbuilder.utility;

/**
 * This enum contains commonly used phrases like the plugin prefix.
 * Use getMessage() to get the String
 */
public enum Lang {
    PREFIX("&b&l[&r&6Kit&6Builder&b&l]&r "),
    NOT_A_PLAYER("&cYou are not a player."),
    NO_PERMISSION("&cYou do not have permission to this command."),
    INVALID_ARGUMENTS("&cInvalid arguments."),
    NOT_ENOUGH_MONEY("&cYou cannot afford that."),
    INVENTORY_FULL("&cInventory full."),
    KIT_EMPTY("&cThis kit is empty."),
    KIT_NOT_FOUND("&cKit not found.");

    private String message;

    /**
     * Main constructor
     * @param message Insert message here, use & for chat color
     */
    Lang(String message) {
        this.message = Chat.colourize(message);
    }

    /**
     * Get the colourized message.
     * @return colourized String
     */
    public String getMessage() {
        return message;
    }

    /**
     * Override the default message using this method
     * @param message change to message, use & for chat color
     */
    public void setMessage(String message) {
        this.message = Chat.colourize(message);
    }
}
