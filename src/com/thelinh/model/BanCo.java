/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.thelinh.model;

import java.awt.Graphics;
import java.awt.Point;



/**
 *
 * @author Admin
 */
public class BanCo {
    public static int rowNumbers;
    public static int columnNumbers;


    public BanCo() {
        rowNumbers = 0;
        columnNumbers = 0;
    }

    public BanCo(int rowNumbers, int columnNumbers) {
        this.rowNumbers = rowNumbers;
        this.columnNumbers = columnNumbers;
    }
    
    public static void veBanCo(Graphics g){
        for(int i = 0; i <= columnNumbers; i++){
            g.drawLine(OCo.width * i, 0, OCo.width * i, OCo.height * rowNumbers);
        }
        
        for(int j = 0; j <= rowNumbers; j++){
            g.drawLine(0, OCo.height * j, OCo.width * columnNumbers, OCo.height * j);
        }
        
        
    }
    
    // viet thoi, khoai that
    public static void veQuanCoDen(Graphics g, Point location){
        g.fillArc(location.x, location.y, OCo.width, OCo.height, 0, 360);
        
    }
    
    public static void veQuanCoTrang(Graphics g, Point location){
        g.drawArc(location.x, location.y, OCo.width, OCo.height, 0, 360);
        
    }
    
    
}
