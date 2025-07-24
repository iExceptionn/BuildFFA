package me.iexception.buildffa.game.Interfaces;

import me.iexception.buildffa.game.User;

import java.util.UUID;

public interface IUserManager {

    void createUser(UUID uuid);
    void loadUser(UUID uuid);
    void saveUser(User user);
    void teleportToSpawn(User user);
    User getUser(UUID uuid);
}
