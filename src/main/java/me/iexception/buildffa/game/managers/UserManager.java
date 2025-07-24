package me.iexception.buildffa.game.managers;

import me.iexception.buildffa.BuildFFA;
import me.iexception.buildffa.game.Interfaces.IUserManager;
import me.iexception.buildffa.game.User;
import me.iexception.buildffa.utils.FileManager;
import me.iexception.buildffa.utils.MessageUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.UUID;

public class UserManager implements IUserManager {

    private final static UserManager userManager = new UserManager();
    public ArrayList<User> loadedUsers = new ArrayList<>();

    public void createUser(UUID uuid) {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        String playerName = Bukkit.getServer().getPlayer(uuid).getName();

        try (Connection connection = BuildFFA.hikariDataSource.getConnection()) {
            PreparedStatement selectStatement = connection.prepareStatement("SELECT * FROM `player_data` WHERE uuid = ?");
            selectStatement.setString(1, uuid.toString());
            ResultSet resultSet = selectStatement.executeQuery();

            if (!resultSet.next()) {
                PreparedStatement insertStatement = connection.prepareStatement(
                        "INSERT INTO `player_data` (`uuid`, `name`, `first_joined`, `last_joined`, `kills`, `deaths`, `killstreak`, `beststreak`, `level`, `xp`, `coins`) " +
                                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)"
                );

                insertStatement.setString(1, uuid.toString());
                insertStatement.setString(2, playerName);
                insertStatement.setString(3, dtf.format(now));
                insertStatement.setString(4, dtf.format(now));
                insertStatement.setInt(5, 0); // kills
                insertStatement.setInt(6, 0); // deaths
                insertStatement.setInt(7, 0); // killstreak
                insertStatement.setInt(8, 0); // beststreak
                insertStatement.setInt(9, 1); // level
                insertStatement.setInt(10, 0); // xp
                insertStatement.setInt(11, 0); // coins

                insertStatement.executeUpdate();
                insertStatement.close();
            }

            resultSet.close();
            selectStatement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            loadUser(uuid);
        }
    }

    @Override
    public void loadUser(UUID uuid) {
        try (Connection connection = BuildFFA.hikariDataSource.getConnection()) {

            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM `player_data` WHERE uuid = ?");
            preparedStatement.setString(1, uuid.toString());
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                int kills = resultSet.getInt("kills");
                int deaths = resultSet.getInt("deaths");
                int killstreak = resultSet.getInt("killstreak");
                int beststreak = resultSet.getInt("beststreak");
                int level = resultSet.getInt("level");
                double xp = resultSet.getDouble("xp");
                double coins = resultSet.getDouble("coins");

                Player player = Bukkit.getPlayer(uuid);
                String name = player != null ? player.getName() : resultSet.getString("name");

                User user = new User(uuid, name, kills, deaths, killstreak, beststreak, level, xp, coins);
                loadedUsers.add(user);

                MessageUtils.getInstance().sendConsoleMessage("loadedplayer", player.getName());

            } else {
                createUser(uuid);
            }

            resultSet.close();
            preparedStatement.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public void saveUser(User user) {
        if (loadedUsers.contains(user)) {
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
            LocalDateTime now = LocalDateTime.now();

            try (Connection connection = BuildFFA.hikariDataSource.getConnection()) {
                PreparedStatement updateStatement = connection.prepareStatement(
                        "UPDATE `player_data` SET " +
                                "`name` = ?, " +
                                "`last_joined` = ?, " +
                                "`kills` = ?, " +
                                "`deaths` = ?, " +
                                "`killstreak` = ?, " +
                                "`beststreak` = ?, " +
                                "`level` = ?, " +
                                "`xp` = ?, " +
                                "`coins` = ? " +
                                "WHERE `uuid` = ?"
                );

                updateStatement.setString(1, user.getName());
                updateStatement.setString(2, dtf.format(now));
                updateStatement.setInt(3, user.getKills());
                updateStatement.setInt(4, user.getDeaths());
                updateStatement.setInt(5, user.getKillstreak());
                updateStatement.setInt(6, user.getBestStreak());
                updateStatement.setInt(7, user.getLevel());
                updateStatement.setDouble(8, user.getXp());
                updateStatement.setDouble(9, user.getCoins());
                updateStatement.setString(10, user.getUuid().toString());

                updateStatement.executeUpdate();
                updateStatement.close();

                loadedUsers.remove(user);

            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                MessageUtils.getInstance().sendConsoleMessage("savedplayer", user.getName());
            }
            return;
        }

        MessageUtils.getInstance().sendConsoleMessage("savedplayerfailed", user.getName());
    }

    @Override
    public void teleportToSpawn(User user) {
        Player player = Bukkit.getPlayer(user.getUuid());
        String raw = FileManager.get("config.yml").getString("spawn");

        if (raw == null || raw.split(";").length < 6) {
            return;
        }

        String[] spawnCoords = raw.split(";");

        Location location = new Location(
                Bukkit.getWorld(spawnCoords[0]),
                Double.parseDouble(spawnCoords[1]),
                Double.parseDouble(spawnCoords[2]),
                Double.parseDouble(spawnCoords[3]),
                Float.parseFloat(spawnCoords[4]),
                Float.parseFloat(spawnCoords[5])
        );

        player.teleport(location);
    }

    @Override
    public User getUser(UUID uuid) {

        for (User user : loadedUsers) {
            if (user.getUuid() == uuid) {
                return user;
            }
        }

        return null;
    }


    public static UserManager getInstance() {
        return userManager;
    }
}
