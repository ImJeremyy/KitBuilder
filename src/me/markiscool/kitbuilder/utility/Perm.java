package me.markiscool.kitbuilder.utility;

import org.bukkit.permissions.Permission;

public enum Perm {

    CREATE_KIT("kitbuilder.create"),
    KIT("kitbuilder.kit"),
    KITS("kitbuilder.kits");

    private Permission permission;

    Perm(String node) {
        this.permission = new Permission(node);
    }

    public Permission getPermission() {
        return permission;
    }


}
