package me.markiscool.kitbuilder.utility;

public enum Lang {
    PREFIX("&b[&6Kit&aBuilder&b]&r "),
    NOT_A_PLAYER("&cYou are not a player."),
    NO_PERMISSION("&cYou do not have permission to this command."),
    INVALID_ARGUMENTS("&cInvalid arguments.");

    private String message;

    Lang(String message) {
        this.message = message;
    }

    public String getMessage() {
        return Chat.colourize(message);
    }
}
