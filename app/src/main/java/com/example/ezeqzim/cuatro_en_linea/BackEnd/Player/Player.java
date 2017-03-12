package com.example.ezeqzim.cuatro_en_linea.BackEnd.Player;

public class Player {
    private PlayerProfile playerProfile;
    private PlayerStatistics playerStatistics;
    private String name;

    public Player(String name, PlayerStatistics playerStatistics) {
        this.name = name;
        this.playerStatistics = playerStatistics;
    }

    public final String getName() {
        return name;
    }

    public final PlayerStatistics getPlayerStatistics() {
        return playerStatistics;
    }
}
