package com.github.imdabigboss.worldloader;

import org.bukkit.*;

import java.io.*;

public class WorldUtils {
    public static boolean deleteWorld(String world) {
        World spigotWorld = WorldLoader.getInstance().getServer().getWorld(world);
        File path = spigotWorld.getWorldFolder();
        WorldLoader.getInstance().getServer().unloadWorld(spigotWorld, false);

        return deleteWorld(path);
    }

    public static boolean deleteWorld(File path) {
        if(path.exists()) {
            File[] files = path.listFiles();
            if (files == null) {
                return false;
            }

            for (File file : files) {
                if (file.isDirectory()) {
                    deleteWorld(file);
                } else {
                    file.delete();
                }
            }
        }
        return path.delete();
    }

    public static void unloadWorld(String world) {
        World spigotWorld = WorldLoader.getInstance().getServer().getWorld(world);
        if (spigotWorld != null) {
            WorldLoader.getInstance().getServer().unloadWorld(spigotWorld, true);
        }
    }

    public static Location getSpawnLocation() {
        return getSpawnLocation("world");
    }

    public static Location getSpawnLocation(String world) {
        World spigotWorld = WorldLoader.getInstance().getServer().getWorld(world);
        if (spigotWorld != null) {
            return spigotWorld.getSpawnLocation();
        } else {
            return null;
        }
    }

    public static World loadWorld(String name) {
        WorldCreator wc = new WorldCreator(name);
        wc.type(WorldType.NORMAL);
        return wc.createWorld();
    }
}
