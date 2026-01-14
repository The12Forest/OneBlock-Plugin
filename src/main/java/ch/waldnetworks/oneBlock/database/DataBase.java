package ch.waldnetworks.oneBlock.database;

import ch.waldnetworks.oneBlock.database.additional_function.OneBlockData;
import com.zaxxer.hikari.HikariDataSource;
import org.bukkit.block.Block;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DataBase {
    private final JavaPlugin plugin;
    private HikariDataSource dataSource;
    private Connection db;

    public DataBase(JavaPlugin plugin) {
        this.dataSource = new HikariDataSource();
        this.dataSource.setJdbcUrl("jdbc:sqlite:" + new File(plugin.getDataFolder(), "data.db"));
        this.plugin = plugin;


        try {
            this.db = this.dataSource.getConnection();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        plugin.getDataFolder().mkdirs();

        try {

            final String sqlDataBaseInit = "CREATE TABLE IF NOT EXISTS OneBlocks (ID INTEGER PRIMARY KEY AUTOINCREMENT,world VARCHAR(10) NOT NULL, x INTEGER NOT NULL,  y INTEGER NOT NULL, z INTEGER NOT NULL, level INTEGER DEFAULT 1, progress INTEGER DEFAULT 0);)";
            final PreparedStatement statement = db.prepareStatement(sqlDataBaseInit);
            statement.executeUpdate();

        } catch (SQLException e) {
            plugin.getLogger().severe(e.getMessage());
        }
    }

    public void shutdown() {
        this.dataSource.close();
    }


    public void addOneBlockToDB(Block block) {
        int x = block.getLocation().getBlockX();
        int y = block.getLocation().getBlockY();
        int z = block.getLocation().getBlockZ();

        try {


            final String sqlDataBaseInit = "INSERT INTO OneBlocks (world, x, y, z) VALUES (?, ?, ?, ?);)";
            final PreparedStatement statement = db.prepareStatement(sqlDataBaseInit);
            statement.setString(1, block.getWorld().getName());
            statement.setInt(2, x);
            statement.setInt(3, y);
            statement.setInt(4, z);
            statement.executeUpdate();

        } catch (SQLException e) {
            plugin.getLogger().severe(e.getMessage());
        }
    }


    public void delOneBlockFromDB(Block block) {
        int x = block.getLocation().getBlockX();
        int y = block.getLocation().getBlockY();
        int z = block.getLocation().getBlockZ();

        try {


            final String sqlDataBaseInit = "DELETE FROM OneBlocks where world = ? and x = ? and y = ? and z = ?;)";
            final PreparedStatement statement = db.prepareStatement(sqlDataBaseInit);
            statement.setString(1, block.getWorld().getName());
            statement.setInt(2, x);
            statement.setInt(3, y);
            statement.setInt(4, z);
            statement.executeUpdate();


        } catch (SQLException e) {
            plugin.getLogger().severe(e.getMessage());
        }
    }

    public void delOneBlockByIDFromDB(int ID) {
        try {


            final String sqlDataBaseInit = "DELETE FROM OneBlocks where ID = ?;)";
            final PreparedStatement statement = db.prepareStatement(sqlDataBaseInit);
            statement.setInt(1, ID);
            statement.executeUpdate();


        } catch (SQLException e) {
            plugin.getLogger().severe(e.getMessage());
        }
    }

    public void delAllOneBlockFromDB() {
        try {


            String sqlDataBaseInit = "DROP TABLE OneBlocks;";
            PreparedStatement statement = db.prepareStatement(sqlDataBaseInit);
            statement.executeUpdate();

            sqlDataBaseInit = "CREATE TABLE IF NOT EXISTS OneBlocks (ID INTEGER PRIMARY KEY AUTOINCREMENT,world VARCHAR(10) NOT NULL, x INTEGER NOT NULL,  y INTEGER NOT NULL, z INTEGER NOT NULL, level INTEGER DEFAULT 1, progress INTEGER DEFAULT 0);)";
            statement = db.prepareStatement(sqlDataBaseInit);
            statement.executeUpdate();


        } catch (SQLException e) {
            plugin.getLogger().severe(e.getMessage());
        }
    }

    public boolean checkOneBlock(Block block) {
        int x = block.getLocation().getBlockX();
        int y = block.getLocation().getBlockY();
        int z = block.getLocation().getBlockZ();

        final String sql =
                "SELECT 1 FROM OneBlocks WHERE world = ? AND x = ? AND y = ? AND z = ? LIMIT 1";


        PreparedStatement stmt = null;
        try {
            stmt = db.prepareStatement(sql);


            stmt.setString(1, block.getWorld().getName());
            stmt.setInt(2, x);
            stmt.setInt(3, y);
            stmt.setInt(4, z);

            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next(); // TRUE wenn Eintrag existiert
            }

        } catch (SQLException e) {
            plugin.getLogger().severe(e.getMessage());
            return false;
        }
    }

    public List<OneBlockData> listOneBlocks() {
        List<OneBlockData> blocks = new ArrayList<>();
        final String sql = "SELECT * FROM OneBlocks";

        try (PreparedStatement stmt = db.prepareStatement(sql);

             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                blocks.add(new OneBlockData(
                        rs.getString("world"),
                        rs.getInt("x"),
                        rs.getInt("y"),
                        rs.getInt("z"),
                        rs.getInt("ID"),
                        rs.getInt("level"),
                        rs.getInt("progress")
                ));
            }
        } catch (SQLException e) {
            plugin.getLogger().severe(e.getMessage());
        }

        return blocks;
    }

    public OneBlockData getOneBlock(Block block) {
        OneBlockData oneBlockData = null;
        String world = block.getWorld().getName();
        int x = block.getLocation().getBlockX();
        int y = block.getLocation().getBlockY();
        int z = block.getLocation().getBlockZ();

        final String sql = "SELECT * FROM OneBlocks Where world = ? AND x = ? AND y = ? AND z = ? LIMIT 1";
        try (PreparedStatement stmt = db.prepareStatement(sql)) {


             stmt.setString(1, world);
             stmt.setInt(2, x);
             stmt.setInt(3, y);
             stmt.setInt(4, z);

             ResultSet rs = stmt.executeQuery();

             return new OneBlockData(
                    rs.getString("world"),
                    rs.getInt("x"),
                    rs.getInt("y"),
                    rs.getInt("z"),
                    rs.getInt("ID"),
                    rs.getInt("level"),
                    rs.getInt("progress")
             );

        } catch (SQLException e) {
            plugin.getLogger().severe(e.getMessage());
            return null;
        }

    }

    public void setBlockProgress(OneBlockData oneBlockData, int progress) {
        int ID = oneBlockData.getID();
        final String sql = "UPDATE OneBlocks SET progress = ? WHERE ID = ?;";
        try (PreparedStatement stmt = db.prepareStatement(sql)) {

            stmt.setInt(1, progress);
            stmt.setInt(2, ID);
            stmt.executeUpdate();

        } catch (SQLException e) {
            plugin.getLogger().severe(e.getMessage());
        }
    }

    public void setBlockLevel(OneBlockData oneBlockData, int level) {
        int ID = oneBlockData.getID();
        final String sql = "UPDATE OneBlocks SET level = ? WHERE ID = ?;";
        try (PreparedStatement stmt = db.prepareStatement(sql)) {

            stmt.setInt(1, level);
            stmt.setInt(2, ID);
            stmt.executeUpdate();

        } catch (SQLException e) {
            plugin.getLogger().severe(e.getMessage());
        }
    }
}
