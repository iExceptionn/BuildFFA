package me.iexception.buildffa;

import com.zaxxer.hikari.HikariDataSource;
import me.iexception.buildffa.commands.BuildffaCommand;
import me.iexception.buildffa.database.DatabaseManager;
import me.iexception.buildffa.game.listeners.PlayerListener;
import me.iexception.buildffa.utils.FileManager;
import me.iexception.buildffa.utils.MessageUtils;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.sql.SQLException;

public final class BuildFFA extends JavaPlugin {

    private static BuildFFA instance;
    public static HikariDataSource hikariDataSource;

    public static BuildFFA getInstance(){
        return instance;
    }

    @Override
    public void onEnable() {
        instance = this;
        loadConfigs();
        loadCommands();
        loadEvents();
        MessageUtils.getInstance().loadMessages();

        // Database
        connectDatabase();
        DatabaseManager.getInstance().createDatabase();

        MessageUtils.getInstance().sendConsoleMessage("plugin-enable", String.valueOf(MessageUtils.getInstance().loadedMessage.size()));

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic

        MessageUtils.getInstance().sendConsoleMessage("plugin-disable");
    }

    private void loadConfigs(){

        FileManager.load(this, "config.yml");
        FileManager.load(this, "messages.yml");

    }

    private void loadCommands(){

        getCommand("buildffa").setExecutor(new BuildffaCommand());

    }

    private void loadEvents(){

        PluginManager pluginManager = Bukkit.getPluginManager();
        pluginManager.registerEvents(new PlayerListener(), this);

    }

    private void connectDatabase() {

        hikariDataSource = new HikariDataSource();

        hikariDataSource.setDataSourceClassName("com.mysql.jdbc.jdbc2.optional.MysqlDataSource");

        String host = FileManager.get("config.yml").getString("database.settings.host");
        String[] hostPort = host.split(":");
        String name = FileManager.get("config.yml").getString("database.settings.database-name");
        String user = FileManager.get("config.yml").getString("database.settings.user");
        String password = FileManager.get("config.yml").getString("database.settings.password");

        hikariDataSource.addDataSourceProperty("serverName", hostPort[0]);
        hikariDataSource.addDataSourceProperty("port", Integer.parseInt(hostPort[1]));
        hikariDataSource.addDataSourceProperty("databaseName", name);
        hikariDataSource.addDataSourceProperty("user", user);
        hikariDataSource.addDataSourceProperty("password", password);

        hikariDataSource.addDataSourceProperty("verifyServerCertificate", false);
        hikariDataSource.addDataSourceProperty("useSSL", false);

        try {
            hikariDataSource.getConnection();
        } catch (SQLException e) {
            Bukkit.getPluginManager().disablePlugin(this);
            MessageUtils.getInstance().sendConsoleMessage("No-database-connection");
            try {
                hikariDataSource.getConnection().close();
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
            throw new RuntimeException(e);
        }
    }
}
