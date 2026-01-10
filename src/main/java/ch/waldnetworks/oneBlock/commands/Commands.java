package ch.waldnetworks.oneBlock.commands;

import ch.waldnetworks.oneBlock.database.DataBase;
import org.bukkit.plugin.java.JavaPlugin;

public class Commands {
    private final JavaPlugin plugin;
    private final DataBase dataBase;

    public Commands(JavaPlugin plugin, DataBase dataBase) {
        this.dataBase = dataBase;
        this.plugin = plugin;
    }

    public void onEnable() {
        plugin.getCommand("oneblock").setExecutor(new OneBlockCommand(dataBase));
    }


}
