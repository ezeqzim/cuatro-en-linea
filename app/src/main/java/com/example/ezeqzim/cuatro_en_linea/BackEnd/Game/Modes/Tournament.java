package com.example.ezeqzim.cuatro_en_linea.BackEnd.Game.Modes;

import com.example.ezeqzim.cuatro_en_linea.BackEnd.Game.WinStatus;
import com.example.ezeqzim.cuatro_en_linea.BackEnd.Player.Player;

import java.util.List;

public class Tournament extends GameMode {
    public Tournament(List<Player> players) {
    }

    public WinStatus informWinEvent(Player player) {
        return WinStatus.NONE;
    }
}
