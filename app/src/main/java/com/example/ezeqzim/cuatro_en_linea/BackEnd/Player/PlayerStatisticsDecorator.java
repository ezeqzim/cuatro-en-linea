package com.example.ezeqzim.cuatro_en_linea.BackEnd.Player;

import com.example.ezeqzim.cuatro_en_linea.BackEnd.Statistics.*;

import java.util.Map;

abstract class PlayerStatisticsDecorator extends PlayerStatistics {
    PlayerStatistics playerStatistics;
    PlayerStatisticsDecorator(PlayerStatistics playerStatistics) {
        this.playerStatistics = playerStatistics;
    }

    public abstract Map<String, Statistic> getStatistics();

    public abstract void addGameResult();
}
