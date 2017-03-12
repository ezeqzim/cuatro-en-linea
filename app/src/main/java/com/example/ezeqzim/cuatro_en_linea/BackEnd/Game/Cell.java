package com.example.ezeqzim.cuatro_en_linea.BackEnd.Game;

public class Cell {
    private int row, col;
    private Object content;

    Cell(Cell other){
        this.row = other.getRow();
        this.col = other.getCol();
        this.content = other.getContent();
    }

    Cell(int row, int col, Object content){
        this.row = row;
        this.col = col;
        this.content = content;
    }

    public int getRow(){
        return row;
    }

    public int getCol(){
        return col;
    }

    Object getContent(){
        return content;
    }

    void setContent(Object content){
        this.content = content;
    }
}
