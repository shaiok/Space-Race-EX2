package com.example.spacerace.Logic;

public class Entity {
    private int row ;
    private int col ;

    public Entity(int row, int col) {
        this.row = row;
        this.col = col;
    }

    public Entity() {
    }

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }

    public Entity setRow(int row) {
        this.row = row;
        return this;
    }

    public Entity setCol(int col) {
        this.col = col;
        return this;
    }
}
