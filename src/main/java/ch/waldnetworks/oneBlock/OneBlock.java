package ch.waldnetworks.oneBlock;

import ch.waldnetworks.oneBlock.commands.Commands;
import ch.waldnetworks.oneBlock.database.DataBase;
import com.zaxxer.hikari.HikariDataSource;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.List;

public final class  OneBlock extends JavaPlugin {
    private HikariDataSource dataSource;
    private File file = new File(getDataFolder(), "config.yml");
    private FileConfiguration config = null;


    @Override
    public void onEnable() {
        // Plugin startup logic
        saveDefaultConfig();
        config = YamlConfiguration.loadConfiguration(file);
        config.options().setHeader(List.of(
                "#################################",
                "#### OneBlock by The12Forest ####",
                "#################################"
        ));
        //saveSettings();

        DataBase dataBase = new DataBase(this);
        dataBase.initDataBase();

        Commands commands = new Commands(this, dataBase);
        commands.onEnable();

        getServer().getPluginManager().registerEvents(new MyListener(dataBase, config, this), this);
        getLogger().info("OneBlock Plugin has been enabled");
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    private void saveSettings() {
        try {
            config.save(file);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }



}
