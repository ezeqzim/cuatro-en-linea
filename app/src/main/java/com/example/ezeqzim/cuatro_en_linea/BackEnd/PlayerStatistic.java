package com.example.ezeqzim.cuatro_en_linea.BackEnd;

public class PlayerStatistic {
    private Player player;
    private int wins;

    PlayerStatistic(Player player, int wins){
        this.player = player;
        this.wins = wins;
    }

    void addWin(){
        wins++;
    }

    public Player getPlayer(){
        return player;
    }

    public int getWins(){
        return wins;
    }
}
