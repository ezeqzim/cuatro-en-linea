package com.example.ezeqzim.cuatro_en_linea.BackEnd.Game.Modes;

import com.example.ezeqzim.cuatro_en_linea.BackEnd.Game.WinStatus;
import com.example.ezeqzim.cuatro_en_linea.BackEnd.Player.Player;

public class JustPlay extends GameMode{
    public JustPlay() {}

    public WinStatus informWinEvent(Player player) {
        return WinStatus.NONE;
    }
}
