package me.markiscool.kitbuilder.utility;

import org.bukkit.permissions.Permission;

/**
 * This enum holds constant permissions.
 * See #getPermission() to get the Permission object.
 */
public enum Perm {

    KIT_BUILDER("kitbuilder.help"),
    CREATE_KIT("kitbuilder.create"),
    KIT("kitbuilder.kit"),
    KITS("kitbuilder.kits"),
    EDIT_KIT("kitbuilder.edit"),
    NO_COOLDOWNS("kitbuilder.nocooldowns"),
    NO_CHARGE("kitbuilder.nocharge");

    private Permission permission;

    /**
     * Made constructor
     * @param node the permission node.
     */
    Perm(String node) {
        this.permission = new Permission(node);
    }

    /**
     * @return Permission object
     */
    public Permission getPermission() {
        return permission;
    }


}
