package me.iexception.buildffa.utils;

import me.iexception.buildffa.BuildFFA;
import me.iexception.buildffa.game.User;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;

public class ScoreboardUtils {

    public static void setScoreboard(Player player, User user) {
        Scoreboard scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
        Objective objective = scoreboard.registerNewObjective("sidebar", "dummy");
        objective.setDisplayName(ChatUtils.format(FileManager.get("config.yml").getString("scoreboard.title")));
        objective.setDisplaySlot(DisplaySlot.SIDEBAR);

        Map<String, Object> lines =  FileManager.get("config.yml").getConfigurationSection("scoreboard.lines").getValues(false);
        Map<String, Team> teams = new HashMap<>();

        for (Map.Entry<String, Object> entry : lines.entrySet()) {
            int score = Integer.parseInt(entry.getKey());
            String lineRaw = (String) entry.getValue();
            String line = replacePlaceholders(lineRaw, user, player);
            String uniqueEntry = ChatUtils.format(line) + ChatUtils.format("§" + Integer.toHexString(score).substring(0,1));

            if (containsPlaceholders(lineRaw)) {
                Team team = scoreboard.registerNewTeam("line" + score);
                team.addEntry(uniqueEntry);
                teams.put("line" + score, team);
                objective.getScore(uniqueEntry).setScore(score);
            } else {
                objective.getScore(uniqueEntry).setScore(score);
            }
        }

        player.setScoreboard(scoreboard);

        new BukkitRunnable() {
            @Override
            public void run() {
                for (Map.Entry<String, Team> teamEntry : teams.entrySet()) {
                    String teamName = teamEntry.getKey();
                    Team team = teamEntry.getValue();

                    int lineNumber = Integer.parseInt(teamName.replace("line", ""));
                    String lineTemplate = FileManager.get("config.yml").getString("scoreboard.lines." + lineNumber);

                    String replaced = replacePlaceholders(lineTemplate, user, player);

                    String prefix = replaced;
                    String suffix = "";

                    if (replaced.length() > 16) {
                        prefix = replaced.substring(0, 16);
                        suffix = replaced.substring(16);
                    }

                    team.setPrefix(ChatUtils.format(prefix));
                    team.setSuffix(ChatUtils.format(suffix));
                }
            }
        }.runTaskTimer(BuildFFA.getInstance(), 0L, 20L);
    }

    public static boolean containsPlaceholders(String text) {
        return text.contains("%player%") ||
                text.contains("%ping%") ||
                text.contains("%kills%") ||
                text.contains("%deaths%") ||
                text.contains("%kdr%") ||
                text.contains("%killstreak%") ||
                text.contains("%beststreak%") ||
                text.contains("%level%") ||
                text.contains("%xp%") ||
                text.contains("%xp_next%") ||
                text.contains("%coins%");
    }

    private static String replacePlaceholders(String text, User user, Player p) {
        if (text == null) return "";

        double KDR = user.getDeaths() == 0 ? user.getKills() : ((double) user.getKills() / user.getDeaths());
        DecimalFormat df = new DecimalFormat("#0.00");

        int ping = 0;
        try {
            Object entityPlayer = p.getClass().getMethod("getHandle").invoke(p);
            ping = (int) entityPlayer.getClass().getField("ping").get(entityPlayer);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return text.replace("{kills}", String.valueOf(user.getKills()))
                .replace("{deaths}", String.valueOf(user.getDeaths()))
                .replace("{kdr}", df.format(KDR))
                .replace("{killstreak}", String.valueOf(user.getKillstreak()))
                .replace("{bestKillstreak}", String.valueOf(user.getBestStreak()))
                .replace("{level}", String.valueOf(user.getLevel()))
                .replace("{xp}", String.valueOf(user.getXp()))
                .replace("{next_xp}", String.valueOf(0))
                .replace("{coins}", df.format(user.getCoins()))
                .replace("{ping}", String.valueOf(ping));
    }

}
