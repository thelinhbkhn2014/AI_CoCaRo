/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.thelinh.model;

import java.awt.Point;

/**
 *
 * @author Admin
 */
public class OCo {

    public static final int width = 25;
    public static final int height = 25;
    
    private int row;
    private int column;
    private Point location;
    private int player;

    public OCo(int row, int column, Point location, int player) {
        this.row = row;
        this.column = column;
        this.location = location;
        this.player = player;
    }

    public OCo() {
    }

    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public int getColumn() {
        return column;
    }

    public void setColumn(int column) {
        this.column = column;
    }

    public Point getLocation() {
        return location;
    }

    public void setLocation(Point location) {
        this.location = location;
    }

    public int getPlayer() {
        return player;
    }

    public void setPlayer(int player) {
        this.player = player;
    }
    
    
    
}
