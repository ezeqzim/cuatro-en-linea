package com.example.ezeqzim.cuatro_en_linea.BackEnd.Game.Modes;

import com.example.ezeqzim.cuatro_en_linea.BackEnd.Game.WinStatus;
import com.example.ezeqzim.cuatro_en_linea.BackEnd.Player.Player;

public abstract class GameMode {
    public abstract WinStatus informWinEvent(Player player);
}
