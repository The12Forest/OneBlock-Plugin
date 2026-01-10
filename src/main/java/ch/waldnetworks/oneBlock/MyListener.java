package ch.waldnetworks.oneBlock;

import ch.waldnetworks.oneBlock.database.DataBase;
import ch.waldnetworks.oneBlock.database.additional_function.OneBlockData;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Boss;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockExplodeEvent;
import org.bukkit.event.block.BlockPlaceEvent;

import java.util.List;
import java.util.Random;

public class MyListener implements Listener {
    private final DataBase dataBase;
    private final FileConfiguration config;

    public MyListener(DataBase dataBase, FileConfiguration config) {
        this.dataBase = dataBase;
        this.config = config;
    }


    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        //int X = event.getBlock().getLocation().getBlockX();
        //int Y = event.getBlock().getLocation().getBlockY();
        //int Z = event.getBlock().getLocation().getBlockZ();
        //Component Message = Component.text("Block broken at " + X + " " + Y + " " + Z);
        //Bukkit.getServer().sendMessage(Message);
        //event.getPlayer().sendMessage("Block broken at " + X + " " + Y + " " + Z);

        Block block = event.getBlock();
        if (dataBase.checkOneBlock(block)) {
            event.setCancelled(true);
            OneBlockData oneBlockData = dataBase.getOneBlock(block);
            int progress = oneBlockData.getProgress();
            int level = oneBlockData.getLevel();
            int level_size = config.getInt((level > config.getInt("Max_Level") ? 0 : level) + ".level-size");


            event.getPlayer().sendActionBar(Component.text("Level: " + level + " Progress: " + progress + "/" + level_size));

            if (level == 0) {
                level = oneBlockData.getLevel();
                dataBase.setBlockLevel(oneBlockData, level + 1);
                dataBase.setBlockProgress(oneBlockData, 0);
                level = 0;
            } else if (level_size <= progress) {
                dataBase.setBlockLevel(oneBlockData, level + 1);
                dataBase.setBlockProgress(oneBlockData, 0);
            } else {
                dataBase.setBlockProgress(oneBlockData, progress + 1);
            }

            Random random = new Random();
            List<String> blocks = config.getStringList((level > config.getInt("Max_Level") ? 0 : level) + ".blocks");
            String block_name = blocks.get(random.nextInt(blocks.size()));
            Material material = Material.matchMaterial(block_name);
            if (material == null) {
                Bukkit.getLogger().severe("Invalid block name in Level: " + (level > config.getInt("Max_Level") ? 0 : level) + ". Block: " + block_name + " cannot be found!");
                block.setType(Material.GRASS_BLOCK, false);
                return;
            }
            block.setType(material, false);
        }
    }

    @EventHandler
    public void onBlockExplode(BlockExplodeEvent event) {
        List<Block> blocks = event.blockList();
        for (Block block : blocks) {
            if (dataBase.checkOneBlock(block)) {
                event.setCancelled(true);
            }
        }
    }
}
