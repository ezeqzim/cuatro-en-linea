package com.example.ezeqzim.cuatro_en_linea.BackEnd.Player;

import com.example.ezeqzim.cuatro_en_linea.BackEnd.Statistics.*;

import java.util.Map;

public class PlayerStatisticsWinDecorator extends PlayerStatisticsDecorator {
    private WinStatistic statistic;

    public PlayerStatisticsWinDecorator(PlayerStatistics playerStatistics) {
        super(playerStatistics);
        this.statistic = new WinStatistic();
    }

    @Override
    public Map<String, Statistic> getStatistics() {
        Map<String, Statistic> list = playerStatistics.getStatistics();
        list.put("wins", statistic);
        return list;
    }

    @Override
    public void addGameResult() {
        addWin();
    }

    private void addWin(){
        statistic.addWin();
    }
}
