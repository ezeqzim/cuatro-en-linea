package com.example.ezeqzim.cuatro_en_linea.BackEnd.Player;

public class Player {
    private PlayerProfile playerProfile;
    private PlayerStatistics playerStatistics;
    private int color;

    public Player(PlayerProfile playerProfile, PlayerStatistics playerStatistics, int color) {
        this.playerProfile = playerProfile;
        this.playerStatistics = playerStatistics;
        this.color = color;
    }

    public final PlayerProfile getPlayerProfile() {
        return playerProfile;
    }

    public final PlayerStatistics getPlayerStatistics() {
        return playerStatistics;
    }

    public final int getColor() {
        return color;
    }
}
