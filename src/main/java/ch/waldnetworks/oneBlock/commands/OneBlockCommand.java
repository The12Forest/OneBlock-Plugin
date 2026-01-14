package ch.waldnetworks.oneBlock.commands;

import ch.waldnetworks.oneBlock.database.DataBase;
import ch.waldnetworks.oneBlock.database.additional_function.OneBlockData;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.bukkit.util.RayTraceResult;
import org.bukkit.util.StringUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

import static java.lang.String.valueOf;

public class OneBlockCommand implements TabExecutor {
    private final DataBase dataBase;

    public OneBlockCommand(DataBase dataBase) {
        this.dataBase = dataBase;
    }


    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof final Player player)) {
            return true;
        }
        World world = player.getWorld();
        if (args.length == 0) {
            player.sendMessage(Component.text("Invalid argument!", TextColor.color(0xFF0000)));
            return true;
        }

        if (args[0].equalsIgnoreCase("set")) {
            if (args.length != 4) {
                player.sendMessage("Correct use: /" + command + "<x> <y> <z>");
                player.sendMessage("PS: Coordinates are absolute-coordinates");
                return true;
            }
            int x = Integer.parseInt(args[1]);
            int y = Integer.parseInt(args[2]);
            int z = Integer.parseInt(args[3]);
            dataBase.addOneBlockToDB(world.getBlockAt(x, y, z));
            player.sendMessage(Component.text("Successfully added OneBlock!", TextColor.color(0x00FF00)));
        } else if (args[0].equalsIgnoreCase("delete") || args[0].equalsIgnoreCase("remove")) {
            if (args.length != 4) {
                player.sendMessage("Correct use: /" + command + "<x> <y> <z>");
                player.sendMessage("PS: Coordinates are absolute-coordinates");
                return true;
            }
            int x = Integer.parseInt(args[1]);
            int y = Integer.parseInt(args[2]);
            int z = Integer.parseInt(args[3]);
            dataBase.delOneBlockFromDB(world.getBlockAt(x, y, z));
            player.sendMessage(Component.text("Successfully deleted OneBlock!", TextColor.color(0x00FF00)));

        } else if (args[0].equalsIgnoreCase("deletebyid")) {
            int ID = Integer.parseInt(args[1]);
            dataBase.delOneBlockByIDFromDB(ID);
            player.sendMessage(Component.text("Successfully deleted OneBlock!", TextColor.color(0x00FF00)));

        } else if (args[0].equalsIgnoreCase("deleteAllOnebLocks")) {
            dataBase.delAllOneBlockFromDB();
            player.sendMessage(Component.text("Successfully deleted All OneBlocks!", TextColor.color(0x00FF00)));
        } else if (args[0].equalsIgnoreCase("list")) {
            List<OneBlockData> blocks = dataBase.listOneBlocks();
            if  (blocks.isEmpty()) {player.sendMessage("No Blocks found!"); return true;}

            String header = String.format("%-3s | %-11s | %3s %3s %3s", "ID", "World", "X", "Y", "Z").toUpperCase();
            String header2 = "=========================";
            player.sendMessage(header);
            player.sendMessage(header2);

            for (OneBlockData b : blocks) {
                player.sendMessage(String.format("%-3s | %-12s | %3d %3d %3d",
                        b.getID(), b.getWorld(), b.getX(), b.getY(), b.getZ()));
            }

        } else {
            player.sendMessage(Component.text("Invalid argument!", TextColor.color(0xFF0000)));
        }
        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        final List<String> completions = new ArrayList<>();
        if (!(sender instanceof final Player player)) {return completions;}

        if (args.length == 1) {
            StringUtil.copyPartialMatches(args[0], List.of("set", "delete", "list", "deleteByID", "deleteAllOnebLocks"), completions);
        } else if (args.length >= 2 && (args[0].equalsIgnoreCase("set") || args[0].equalsIgnoreCase("delete"))) {
            Block block = this.getTargetBlock(player);
            if (block != null) {
                if (args.length == 2) {
                    completions.add(block.getLocation().getBlockX() + " " + block.getLocation().getBlockY() + " " + block.getLocation().getBlockZ());
                } else if (args.length == 3) {
                    completions.add(block.getLocation().getBlockY() + " " + block.getLocation().getBlockZ());
                } else if (args.length == 4) {
                    completions.add(valueOf(block.getLocation().getBlockZ()));
                }
            } else if (args.length <= 4) {
                completions.add("Please target a Block or enter Block-Cordinates!");
            }
        }
        return completions;
    }

    private Block getTargetBlock(Player player) {
        int maxDistance = 10; // blocks
        RayTraceResult result = player.rayTraceBlocks(maxDistance);

        if (result == null) {
            return null; // player is not looking at a block
        }

        // use the coordinates
        return result.getHitBlock();
    }
}
