package me.iexception.buildffa.database;

import me.iexception.buildffa.BuildFFA;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseManager {

    private static final DatabaseManager databaseManager = new DatabaseManager();

    public void createDatabase() {

        try {

            Connection connection = BuildFFA.hikariDataSource.getConnection();
            Statement statement = connection.createStatement();

            statement.execute("CREATE TABLE IF NOT EXISTS `player_data` (" +
                    "`uuid` VARCHAR(36) NOT NULL, " +
                    "`name` VARCHAR(36) NOT NULL, " +
                    "`first_joined` VARCHAR(36) NOT NULL, " +
                    "`last_joined` VARCHAR(36) NOT NULL, " +
                    "`kills` INT NOT NULL DEFAULT 0, " +
                    "`deaths` INT NOT NULL DEFAULT 0, " +
                    "`killstreak` INT NOT NULL DEFAULT 0, " +
                    "`beststreak` INT NOT NULL DEFAULT 0, " +
                    "`level` INT NOT NULL DEFAULT 0, " +
                    "`xp` INT NOT NULL DEFAULT 0, " +
                    "`coins` INT NOT NULL DEFAULT 0, " +
                    "PRIMARY KEY (`uuid`))");


            statement.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static DatabaseManager getInstance() {
        return databaseManager;
    }
}
