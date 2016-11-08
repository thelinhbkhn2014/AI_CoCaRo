/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.thelinh.controller;

import com.thelinh.model.BanCo;
import com.thelinh.model.OCo;
import java.awt.Graphics;
import java.awt.Point;
import java.util.ArrayList;
import java.util.Stack;
import javax.swing.JOptionPane;


/**
 *
 * @author Admin
 */


public class CaRoController {
    private BanCo banCo;
    private OCo[][] oCoArray;
    private Stack<OCo> stList;
    private boolean ready;
    private int turn;   
    public enum Finish{
        HoaCo,
        Player1,
        Player2,
        Computer  
    }   
    private Finish finish;
    
    
    public CaRoController() {
        banCo = new BanCo(20, 20);
        oCoArray = new OCo[BanCo.rowNumbers][BanCo.columnNumbers];
        stList = new Stack<OCo>();
        turn = 1;
    }

    public boolean isReady() {
        return ready;
    }

    public void setReady(boolean ready) {
        this.ready = ready;
    }
      
    
    public void veBanCo(Graphics g){
        banCo.veBanCo(g);
    }
    
    public void oCoArrray(){
        for(int i = 0; i < BanCo.rowNumbers; i++){
            for(int j = 0; j < BanCo.columnNumbers; j++){
                oCoArray[i][j] = new OCo(i, j, new Point(j * OCo.width, i * OCo.height), 0);
            }
        }
    }
    
    // chinh lai
    public boolean play(int mouseX, int mouseY, Graphics g){
        if(mouseX % OCo.width == 0 || mouseY % OCo.height == 0){
            return false;
        }
        int column = mouseX / OCo.width;
        int row = mouseY / OCo.height;
        if(oCoArray[row][column].getPlayer() != 0){
            return false;
        }
        switch(turn){
            case 1:
                oCoArray[row][column].setPlayer(1);
                banCo.veQuanCoDen(g, oCoArray[row][column].getLocation());
                turn = -1;
                stList.push(oCoArray[row][column]);
                break;
            case -1:
                oCoArray[row][column].setPlayer(-1);
                banCo.veQuanCoTrang(g, oCoArray[row][column].getLocation());
                turn = 1;
                stList.push(oCoArray[row][column]);
                break;
            default:
                JOptionPane.showMessageDialog(null, "ERROR");
                break;
        }
               
        return true;
        
    }
    
    public void veLaiQuanCo(Graphics g){           
        for(OCo oCo : stList){
            if(oCo.getPlayer() == 1){
                banCo.veQuanCoDen(g, oCo.getLocation());
            }
            else if(oCo.getPlayer() == -1){
                banCo.veQuanCoTrang(g, oCo.getLocation());
                
            }                    
        }
    }
    
    public void startPlayerVSPlayer(Graphics g){
        ready = true;
        oCoArrray();
        for(OCo oCo : stList){
            g.clearRect(oCo.getLocation().x, oCo.getLocation().y, OCo.width, OCo.height);
        }     
        turn = 1;
        stList = new Stack<OCo>();
        veBanCo(g);
    }
    
    public void Undo(Graphics g){   
        if(!stList.empty()){
             OCo oCo = stList.pop(); // note: object oCo tro vao cung 1 vung nho
             oCoArray[oCo.getRow()][oCo.getColumn()].setPlayer(0);
             if(turn == 1){
                 turn = -1;
             }
             else{
                 turn = 1;
             }
        }  
        veBanCo(g);
        veLaiQuanCo(g);
    }
    
    public void Reset(Graphics g){
        for(OCo oCo : stList){
            g.clearRect(oCo.getLocation().x, oCo.getLocation().y, OCo.width, OCo.height);
        }  
    }
    
    
    
    //Check Win or Lost
    public void endGame(){
        switch(finish){
            case HoaCo:
                JOptionPane.showMessageDialog(null, "Hoa");
                break;
            case Player1:
                JOptionPane.showMessageDialog(null, "Player 1 win");
                break;
            case Player2:
                JOptionPane.showMessageDialog(null, "Player 2 win");
                break; 
            case Computer:
                JOptionPane.showMessageDialog(null, "Computer win");
                break;
        }
        ready = false;
        
    }
    
    
    
    public boolean checkWin(){              
        for(OCo oCo : stList){
            if(vertical(oCo.getRow(), oCo.getColumn(), oCo.getPlayer()) || 
               horizontal(oCo.getRow(), oCo.getColumn(), oCo.getPlayer()) ||
               rightDiagonal(oCo.getRow(), oCo.getColumn(), oCo.getPlayer()) ||
               leftDiagonal(oCo.getRow(), oCo.getColumn(), oCo.getPlayer())){
             
                finish = oCo.getPlayer() == 1 ? Finish.Player1 : Finish.Player2;
                return true;
            }
        }
                     
        int dem = 0;
        for(int i = 0; i < BanCo.rowNumbers; i++){
            for(int j = 0; j < BanCo.columnNumbers; j++){
                if(oCoArray[i][j].getPlayer() == 0){
                    break;
                }
                else{
                    dem++;
                }
            }
        }
        if(dem == 400){
            finish = Finish.HoaCo;
            return true;
        }
        
        return false;
    }
    
    private boolean vertical(int currentRow, int currentColumn, int currentPlayer){
        if(currentRow > BanCo.rowNumbers - 5){
            return false;
        }
        
        int i = 1;
        for(i = 1; i < 5; i++){
            if(oCoArray[currentRow + i][currentColumn].getPlayer() != currentPlayer){
                return false;
            }
            
        }
        
        if(currentRow == 0 || currentRow + i == BanCo.rowNumbers){
            return true;
        }
        
        if(oCoArray[currentRow - 1][currentColumn].getPlayer() == 0 || oCoArray[currentRow + i][currentColumn].getPlayer() == 0){
            return true;
        }
        
        
        return false;
    }
    
    private boolean horizontal(int currentRow, int currentColumn, int currentPlayer){
        if(currentColumn > BanCo.columnNumbers - 5){
            return false;
        }
        
        int i = 1;
        for(i = 1; i < 5; i++){
            if(oCoArray[currentRow][currentColumn + i].getPlayer() != currentPlayer){
                return false;
            }
            
        }
        
        if(currentColumn == 0 || currentColumn + i == BanCo.columnNumbers){
            return true;
        }
        
        if(oCoArray[currentRow][currentColumn - 1].getPlayer() == 0 || oCoArray[currentRow][currentColumn + i].getPlayer() == 0){
            return true;
        }
        
        
        return false;
    }
    
    private boolean rightDiagonal(int currentRow, int currentColumn, int currentPlayer){
        if(currentRow > BanCo.rowNumbers - 5 || currentColumn > BanCo.columnNumbers - 5){
            return false;
        }
        
        int i = 1;
        for(i = 1; i < 5; i++){
            if(oCoArray[currentRow + i][currentColumn + i].getPlayer() != currentPlayer){
                return false;
            }
            
        }
        
        if(currentColumn == 0 || currentRow == 0 || 
           currentColumn + i == BanCo.columnNumbers || currentRow + i == BanCo.rowNumbers){
            return true;
        }
        
        if(oCoArray[currentRow - 1][currentColumn - 1].getPlayer() == 0 || oCoArray[currentRow + i][currentColumn + i].getPlayer() == 0){
            return true;
        }
        
        
        return false;
    }
    
    private boolean leftDiagonal(int currentRow, int currentColumn, int currentPlayer){
        if(currentRow < 4 || currentColumn > BanCo.columnNumbers - 5){
            return false;
        }
        
        int i = 1;
        for(i = 1; i < 5; i++){
            if(oCoArray[currentRow - i][currentColumn + i].getPlayer() != currentPlayer){
                return false;
            }
            
        }
        
        if(currentRow == BanCo.rowNumbers - 1 || currentColumn == 0 || 
           currentColumn + i == BanCo.columnNumbers || currentRow - i == -1){
            return true;
        }
        
        if(oCoArray[currentRow + 1][currentColumn - 1].getPlayer() == 0 || oCoArray[currentRow - i][currentColumn + i].getPlayer() == 0){
            return true;
        }
        
        
        return false;
    }
    
    
                    
}
