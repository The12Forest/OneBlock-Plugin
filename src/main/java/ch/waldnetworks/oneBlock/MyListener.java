package ch.waldnetworks.oneBlock;

import ch.waldnetworks.oneBlock.database.DataBase;
import ch.waldnetworks.oneBlock.database.additional_function.OneBlockData;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockExplodeEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;
import java.util.Random;

public class MyListener implements Listener {
    private final DataBase dataBase;
    private final FileConfiguration config;
    private final JavaPlugin plugin;
    private final int Max_Level;
    private final Random random;

    public MyListener(DataBase dataBase, FileConfiguration config, JavaPlugin plugin) {
        this.dataBase = dataBase;
        this.config = config;
        this.plugin = plugin;
        this.Max_Level = config.getInt("Max_Level");
        this.random = new Random();
    }


    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        Block block = event.getBlock();
        if (!dataBase.checkOneBlock(block) ) {return;}
        //event.setCancelled(true);

        OneBlockData oneBlockData = dataBase.getOneBlock(block);
        int progress = oneBlockData.getProgress();
        int Out_Level = oneBlockData.getLevel();
        int Actual_Level = (Out_Level > this.Max_Level ? 0 : Out_Level);
        int level_size = config.getInt(Actual_Level + ".level-size");

        event.getPlayer().sendActionBar(Component.text("Level: " + Out_Level + " Progress: " + progress + "/" + level_size));

        if (level_size <= progress) {
            dataBase.setBlockLevel(oneBlockData, Out_Level + 1);
            dataBase.setBlockProgress(oneBlockData, 0);
        } else {
            dataBase.setBlockProgress(oneBlockData, progress + 1);
        }

        Bukkit.getScheduler().runTask(plugin, () -> {

            List<String> blocks = config.getStringList(Actual_Level + ".blocks");
            String block_name = blocks.get(this.random.nextInt(blocks.size()));
            Material material = Material.matchMaterial(block_name);
            if (material == null) {
                Bukkit.getLogger().severe("Invalid block name in Level: " + Actual_Level + ". Block: " + block_name + " cannot be found!");
                block.setType(Material.GRASS_BLOCK, false);
                return;
            }
            block.setType(material, false);
        });
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
