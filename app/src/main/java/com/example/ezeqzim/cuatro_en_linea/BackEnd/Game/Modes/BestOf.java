package com.example.ezeqzim.cuatro_en_linea.BackEnd.Game.Modes;

import com.example.ezeqzim.cuatro_en_linea.BackEnd.Game.WinStatus;
import com.example.ezeqzim.cuatro_en_linea.BackEnd.Player.Player;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BestOf extends GameMode {
    private int bestOf;
    private Map<Player, Integer> winsByPlayer;

    public BestOf(int bestOf, List<Player> players) {
        this.bestOf = bestOf;
        winsByPlayer = new HashMap<>();
        for (Player player: players) {
            winsByPlayer.put(player, 0);
        }
    }

    public WinStatus informWinEvent(Player player) {
        int newValue = winsByPlayer.get(player) + 1;
        if (newValue > bestOf / 2)
            return WinStatus.WIN;
        else {
            winsByPlayer.put(player, newValue);
            return WinStatus.NONE;
        }
    }
}
