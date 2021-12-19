package com.github.imdabigboss.worldloader;

import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Logger;

public final class WorldLoader extends JavaPlugin {
    private static final Logger log = Logger.getLogger("Minecraft");
    private static WorldLoader instance = null;

    @Override
    public void onEnable() {
        instance = this;
        this.saveDefaultConfig();

        log.info(String.format("[%s] Enabled Version %s", getDescription().getName(), getDescription().getVersion()));

        if (getConfig().contains("allMaps")) {
            if (getConfig().get("allMaps") != null) {
                for (String map : getConfig().getStringList("allMaps")) {
                    WorldUtils.loadWorld(map);
                }
            }
        }

        this.getCommand("worldloader").setExecutor(new WorldLoaderCommand(this));
    }

    @Override
    public void onDisable() {

    }

    public static WorldLoader getInstance() {
        return instance;
    }
    public static Logger getLog() {
        return log;
    }
}
