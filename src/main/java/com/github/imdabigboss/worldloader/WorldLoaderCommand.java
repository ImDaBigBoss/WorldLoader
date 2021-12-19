package com.github.imdabigboss.worldloader;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.ArrayList;

public class WorldLoaderCommand implements CommandExecutor, TabExecutor {
    private WorldLoader plugin;

    public WorldLoaderCommand(WorldLoader plugin) {
        super();
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 0) {
            sendHelp(sender);
            return true;
        }

        if (args[0].equalsIgnoreCase("add")) {
            if (args.length != 2) {
                sendHelp(sender);
                return true;
            }
            if (!sender.isOp()) {
                sender.sendMessage(ChatColor.RED + "You don't have sufficient permissions to do that.");
                return true;
            }

            sender.sendMessage(ChatColor.GREEN + "Creating world " + args[1] + "...");
            WorldUtils.loadWorld(args[1]);

            List<String> maps;
            if (WorldLoader.getInstance().getConfig().contains("allMaps")) {
                if (WorldLoader.getInstance().getConfig().get("allMaps") != null) {
                    maps = WorldLoader.getInstance().getConfig().getStringList("allMaps");
                } else {
                    maps = new ArrayList<>();
                }
            } else {
                maps = new ArrayList<>();
            }
            maps.add(args[1]);
            WorldLoader.getInstance().getConfig().set("allMaps", maps);

            sender.sendMessage(ChatColor.GREEN + "Done!");
        } else if (args[0].equalsIgnoreCase("tp")) {
            if (args.length != 2) {
                sendHelp(sender);
                return true;
            }
            if (!(sender instanceof Player)) {
                sender.sendMessage(ChatColor.RED + "You must be a player to do that.");
            }

            Player player = (Player) sender;
            Location spawn = WorldUtils.getSpawnLocation(args[1]);

            if (spawn == null) {
                sender.sendMessage(ChatColor.RED + "World " + args[1] + " does not exist.");
            } else {
                sender.sendMessage(ChatColor.GREEN + "Teleporting you to " + args[1] + "...");
                player.teleport(spawn);
                sender.sendMessage(ChatColor.GREEN + "Done!");
            }
        } else if (args[0].equalsIgnoreCase("delete")) {
            if (args.length != 2) {
                sendHelp(sender);
                return true;
            }
            if (!sender.isOp()) {
                sender.sendMessage(ChatColor.RED + "You don't have sufficient permissions to do that.");
                return true;
            }
            if (args[1].equalsIgnoreCase("world")) {
                sender.sendMessage(ChatColor.RED + "You cannot delete that world.");
            }

            World world = WorldLoader.getInstance().getServer().getWorld(args[1]);
            if (world == null) {
                sender.sendMessage(ChatColor.RED + "World " + args[1] + " does not exist.");
                return true;
            }

            Location spawn = WorldUtils.getSpawnLocation();
            for (Player player : world.getPlayers()) {
                player.teleport(spawn);
            }

            sender.sendMessage(ChatColor.GREEN + "Deleting world " + args[1] + "...");
            WorldUtils.deleteWorld(args[1]);
            if (WorldLoader.getInstance().getConfig().contains("allMaps")) {
                if (WorldLoader.getInstance().getConfig().get("allMaps") != null) {
                    List<String> maps = WorldLoader.getInstance().getConfig().getStringList("allMaps");
                    maps.remove(args[1]);
                    WorldLoader.getInstance().getConfig().set("allMaps", maps);
                }
            }
            sender.sendMessage(ChatColor.GREEN + "Done!");
        }

        WorldLoader.getInstance().saveConfig();
        return true;
    }

    private void sendHelp(CommandSender sender) {
        sender.sendMessage("WorldLoader Commands:\n - add <world>\n - tp <world>\n - delete <world>");
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        ArrayList<String> cmds = new ArrayList<>();
        if (args.length == 1) {
            cmds.add("add");
            cmds.add("tp");
            cmds.add("delete");
        }
        return cmds;
    }
}
