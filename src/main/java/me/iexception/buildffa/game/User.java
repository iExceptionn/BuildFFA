package me.iexception.buildffa.game;

import java.util.UUID;

public class User {

    private UUID uuid;
    private String name;
    private Integer kills;
    private Integer deaths;
    private Integer killstreak;
    private Integer bestStreak;
    private Integer level;
    private double xp;
    private double coins;

    public User(UUID uuid, String name, Integer kills, Integer deaths, Integer killstreak, Integer bestStreak, Integer level, double xp, double coins){
        this.uuid = uuid;
        this.name = name;
        this.kills = kills;
        this.deaths = deaths;
        this.killstreak = killstreak;
        this.bestStreak = bestStreak;
        this.level = level;
        this.xp = xp;
        this.coins = coins;
    }

    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getKills() {
        return kills;
    }

    public void setKills(Integer kills) {
        this.kills = kills;
    }

    public Integer getDeaths() {
        return deaths;
    }

    public void setDeaths(Integer deaths) {
        this.deaths = deaths;
    }

    public Integer getKillstreak() {
        return killstreak;
    }

    public void setKillstreak(Integer killstreak) {
        this.killstreak = killstreak;
    }

    public Integer getBestStreak() {
        return bestStreak;
    }

    public void setBestStreak(Integer bestStreak) {
        this.bestStreak = bestStreak;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public double getXp() {
        return xp;
    }

    public void setXp(double xp) {
        this.xp = xp;
    }

    public double getCoins() {
        return coins;
    }

    public void setCoins(double coins) {
        this.coins = coins;
    }
}
