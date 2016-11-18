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
    private int mode;

    
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

    public int getMode() {
        return mode;
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
        mode = 1;
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
    
    public void startPlayerVSCom(Graphics g){
        ready = true;
        oCoArrray();
        for(OCo oCo : stList){
            g.clearRect(oCo.getLocation().x, oCo.getLocation().y, OCo.width, OCo.height);
        }     
        turn = 1;
        mode = 2;
        stList = new Stack<OCo>();
        veBanCo(g);
        startComputer(g);
    }
    
    //AI
    private long[] attackScore = {0, 64, 4096, 262144, 16777216, 1073741824};
    private long[] defendScore = {0, 8, 512, 32768, 2097152, 134217728 };

 
    // attack
    
    private long attackScoreVertical(int currentRow, int currentColumn){
        long atkScore = 0;
        int eHuman = 0;
        int ePC = 0;
        int eHuman2 = 0;
        int ePC2 = 0;
        // tren xuong
            for(int i = 1; i < 5 && currentRow + i < BanCo.rowNumbers; i++){
                if(oCoArray[currentRow + i][currentColumn].getPlayer() == 1){
                    ePC++;
                }
                else if(oCoArray[currentRow + i][currentColumn].getPlayer() == -1){
                    eHuman++;
                    break;
                }
                else{
                    for(int j = 2; j < 6 && currentRow + j < BanCo.rowNumbers; j++){
                        if(oCoArray[currentRow + j][currentColumn].getPlayer() == 1){
                            ePC2++;
                        }
                        else if(oCoArray[currentRow + j][currentColumn].getPlayer() == -1){
                            eHuman2++;
                            break;
                        }
                        else{
                            break;
                        }
                    }
                    break;
                }
            }
            for(int i = 1; i < 5 && currentRow - i >= 0; i++){
                if(oCoArray[currentRow - i][currentColumn].getPlayer() == 1){
                    ePC++;
                }
                else if(oCoArray[currentRow - i][currentColumn].getPlayer() == -1){
                    eHuman++;
                    break;
                }
                else{
                    for(int j = 2; j < 6 && currentRow - j >= 0; j++){
                        if(oCoArray[currentRow - j][currentColumn].getPlayer() == 1){
                            ePC2++;
                        }
                        else if(oCoArray[currentRow - j][currentColumn].getPlayer() == -1){
                            eHuman2++;
                            break;
                        }
                        else{
                            break;
                        }
                    }
                    break;
                }
            }
        
        
       
       
            if(eHuman == 2){
                return 0;
            }
            
            if(eHuman == 0){
                atkScore += attackScore[ePC] * 2;
            }
            else{
                atkScore += attackScore[ePC];
            }
            
            if(eHuman2 == 0){
                atkScore += attackScore[ePC2] * 2;
            }
            else{
                atkScore += attackScore[ePC2];
            }
            
            if(ePC >= ePC2){
                atkScore -= 1;
            }
            else{
                atkScore -= 2;
            }
            
            if(ePC == 4){
                atkScore *= 4;
            }
            
            if(ePC == 0){
                atkScore += defendScore[eHuman] * 2;
            }
            else{
                atkScore += defendScore[eHuman];
            }
            
            if(ePC2 == 0){
                atkScore += defendScore[eHuman2] * 2;
            }
            else{
                atkScore += defendScore[eHuman2];
            }                         
        
        return atkScore;
    }
    
    private long attackScoreHorizontal(int currentRow, int currentColumn){
        long atkScore = 0;
        int eHuman = 0;
        int ePC = 0;
        int eHuman2 = 0;
        int ePC2 = 0;
        // trai sang
        
            for(int i = 1; i < 5 && currentColumn + i < BanCo.columnNumbers; i++){
                if(oCoArray[currentRow][currentColumn + i].getPlayer() == 1){
                    ePC++;
                }
                else if(oCoArray[currentRow][currentColumn + i].getPlayer() == -1){
                    eHuman++;
                    break;
                }
                else{
                    for(int j = 2; j < 6 && currentColumn + j < BanCo.columnNumbers; j++){
                        if(oCoArray[currentRow][currentColumn + j].getPlayer() == 1){
                            ePC2++;
                        }
                        else if(oCoArray[currentRow][currentColumn + j].getPlayer() == -1){
                            eHuman2++;
                            break;
                        }
                        else{
                            break;
                        }
                    }
                    break;
                }
            }
            
            for(int i = 1; i < 5 && currentColumn - i >= 0; i++){
                if(oCoArray[currentRow][currentColumn - i].getPlayer() == 1){
                    ePC++;
                }
                else if(oCoArray[currentRow][currentColumn - i].getPlayer() == -1){
                    eHuman++;
                    break;
                }
                else{
                    for(int j = 2; j < 6 && currentColumn - j >= 0; j++){
                        if(oCoArray[currentRow][currentColumn - j].getPlayer() == 1){
                            ePC2++;
                        }
                        else if(oCoArray[currentRow][currentColumn - j].getPlayer() == -1){
                            eHuman2++;
                            break;
                        }
                        else{
                            break;
                        }
                    }
                    break;
                }
            }
        
        
        
        
        
            if(eHuman == 2){
                return 0;
            }
            
            if(eHuman == 0){
                atkScore += attackScore[ePC] * 2;
            }
            else{
                atkScore += attackScore[ePC];
            }
            
            if(eHuman2 == 0){
                atkScore += attackScore[ePC2] * 2;
            }
            else{
                atkScore += attackScore[ePC2];
            }
            
            if(ePC >= ePC2){
                atkScore -= 1;
            }
            else{
                atkScore -= 2;
            }
            
            if(ePC == 4){
                atkScore *= 4;
            }
            
            if(ePC == 0){
                atkScore += defendScore[eHuman] * 2;
            }
            else{
                atkScore += defendScore[eHuman];
            }
            
            if(ePC2 == 0){
                atkScore += defendScore[eHuman2] * 2;
            }
            else{
                atkScore += defendScore[eHuman2];
            }
                          
       
        
        return atkScore;
    }
    
    private long attackScoreRightDiagonal(int currentRow, int currentColumn){
        long atkScore = 0;
        int eHuman = 0;
        int ePC = 0;
        int eHuman2 = 0;
        int ePC2 = 0;
        
        // xuong
        
            for(int i = 1; i < 5 && currentColumn + i < BanCo.columnNumbers && currentRow + i < BanCo.rowNumbers; i++){
                if(oCoArray[currentRow + i][currentColumn + i].getPlayer() == 1){
                    ePC++;
                }
                else if(oCoArray[currentRow + i][currentColumn + i].getPlayer() == -1){
                    eHuman++;
                    break;
                }
                else{
                    for(int j = 2; j < 6 && currentColumn + j < BanCo.columnNumbers && currentRow + j < BanCo.rowNumbers; j++){
                        if(oCoArray[currentRow + j][currentColumn + j].getPlayer() == 1){
                            ePC2++;
                        }
                        else if(oCoArray[currentRow + j][currentColumn + j].getPlayer() == -1){
                            eHuman2++;
                            break;
                        }
                        else{
                            break;
                        }
                    }
                    break;
                }
            }
            for(int i = 1; i < 5 && currentColumn - i >= 0 && currentRow - i >= 0; i++){
                if(oCoArray[currentRow - i][currentColumn - i].getPlayer() == 1){
                    ePC++;
                }
                else if(oCoArray[currentRow - i][currentColumn - i].getPlayer() == -1){
                    eHuman++;
                    break;
                }
                else{
                    for(int j = 2; j < 6 && currentColumn - j >= 0 && currentRow - j >= 0; j++){
                        if(oCoArray[currentRow - j][currentColumn - j].getPlayer() == 1){
                            ePC2++;
                        }
                        else if(oCoArray[currentRow - j][currentColumn - j].getPlayer() == -1){
                            eHuman2++;
                            break;
                        }
                        else{
                            break;
                        }
                    }
                    break;
                }
            }
            
        
       
        
        
            if(eHuman == 2){
                return 0;
            }
            
            if(eHuman == 0){
                atkScore += attackScore[ePC] * 2;
            }
            else{
                atkScore += attackScore[ePC];
            }
            
            if(eHuman2 == 0){
                atkScore += attackScore[ePC2] * 2;
            }
            else{
                atkScore += attackScore[ePC2];
            }
            
            if(ePC >= ePC2){
                atkScore -= 1;
            }
            else{
                atkScore -= 2;
            }
            
            if(ePC == 4){
                atkScore *= 4;
            }
            
            if(ePC == 0){
                atkScore += defendScore[eHuman] * 2;
            }
            else{
                atkScore += defendScore[eHuman];
            }
            
            if(ePC2 == 0){
                atkScore += defendScore[eHuman2] * 2;
            }
            else{
                atkScore += defendScore[eHuman2];
            }
                          
       
        return atkScore;
    }
    
    private long attackScoreLeftDiagonal(int currentRow, int currentColumn){
        long atkScore = 0;
        int eHuman = 0;
        int ePC = 0;
        int eHuman2 = 0;
        int ePC2 = 0;
      
        // len
       
            for(int i = 1; i < 5 && currentColumn + i < BanCo.columnNumbers && currentRow - i >= 0; i++){
                if(oCoArray[currentRow - i][currentColumn + i].getPlayer() == 1){
                    ePC++;
                }
                else if(oCoArray[currentRow - i][currentColumn + i].getPlayer() == -1){
                    eHuman++;
                    break;
                }
                else{
                    for(int j = 2; j < 6 && currentColumn + j < BanCo.columnNumbers && currentRow - j >= 0; j++){
                        if(oCoArray[currentRow - j][currentColumn + j].getPlayer() == 1){
                            ePC2++;
                        }
                        else if(oCoArray[currentRow - j][currentColumn + j].getPlayer() == -1){
                            eHuman2++;
                            break;
                        }
                        else{
                            break;
                        }
                    }
                    break;
                }
            }
            for(int i = 1; i < 5 && currentColumn - i >= 0 && currentRow + i < BanCo.rowNumbers; i++){
                if(oCoArray[currentRow + i][currentColumn - i].getPlayer() == 1){
                    ePC++;
                }
                else if(oCoArray[currentRow + i][currentColumn - i].getPlayer() == -1){
                    eHuman++;
                    break;
                }
                else{
                    for(int j = 2; j < 6 && currentColumn - j >= 0 && currentRow + j < BanCo.rowNumbers; j++){
                        if(oCoArray[currentRow + j][currentColumn - j].getPlayer() == 1){
                            ePC2++;
                        }
                        else if(oCoArray[currentRow + j][currentColumn - j].getPlayer() == -1){
                            eHuman2++;
                            break;
                        }
                        else{
                            break;
                        }
                    }
                    break;
                }
            }
       
        
        
        
        
            if(eHuman == 2){
                return 0;
            }
            
            if(eHuman == 0){
                atkScore += attackScore[ePC] * 2;
            }
            else{
                atkScore += attackScore[ePC];
            }
            
            if(eHuman2 == 0){
                atkScore += attackScore[ePC2] * 2;
            }
            else{
                atkScore += attackScore[ePC2];
            }
            
            if(ePC >= ePC2){
                atkScore -= 1;
            }
            else{
                atkScore -= 2;
            }
            
            if(ePC == 4){
                atkScore *= 4;
            }
            
            if(ePC == 0){
                atkScore += defendScore[eHuman] * 2;
            }
            else{
                atkScore += defendScore[eHuman];
            }
            
            if(ePC2 == 0){
                atkScore += defendScore[eHuman2] * 2;
            }
            else{
                atkScore += defendScore[eHuman2];
            }
                          
       
        return atkScore;
    }
    
    
    
    // defend
    private long defendScoreVertical(int currentRow, int currentColumn){
        long defScore = 0;
        int eHuman = 0;
        int ePC = 0;
        int eHuman2 = 0;
        int ePC2 = 0;
        
        // tren xuong
       
            for(int i = 1; i < 5 && currentRow + i < BanCo.rowNumbers; i++){
                if(oCoArray[currentRow + i][currentColumn].getPlayer() == 1){
                    ePC++;
                    break;
                }
                else if(oCoArray[currentRow + i][currentColumn].getPlayer() == -1){
                    eHuman++;
                    
                }
                else{
                    for(int j = 2; j < 6 && currentRow + j < BanCo.rowNumbers; j++){
                        if(oCoArray[currentRow + j][currentColumn].getPlayer() == 1){
                            ePC2++;
                            break;
                        }
                        else if(oCoArray[currentRow + j][currentColumn].getPlayer() == -1){
                            eHuman2++;
                        }
                        else{
                            break;
                        }
                    }
                }
            }
            for(int i = 1; i < 5 && currentRow - i >= 0; i++){
                if(oCoArray[currentRow - i][currentColumn].getPlayer() == 1){
                    ePC++;
                    break;
                }
                else if(oCoArray[currentRow - i][currentColumn].getPlayer() == -1){
                    eHuman++;
                    
                }
                else{
                    for(int j = 2; j < 6 && currentRow - j >= 0; j++){
                        if(oCoArray[currentRow - j][currentColumn].getPlayer() == 1){
                            ePC2++;
                            break;
                        }
                        else if(oCoArray[currentRow - j][currentColumn].getPlayer() == -1){
                            eHuman2++;
                        }
                        else{
                            break;
                        }
                    }
                }
            }
       
        
        
        
       
            if(ePC == 2){
                return 0;
            }
            
            if(ePC == 0){
                defScore += defendScore[eHuman] * 2;
            }
            else{
                defScore += defendScore[eHuman];
            }
            
            if(eHuman >= eHuman2){
                defScore -= 1;
            }
            else{
                defScore -= 2;
            }
            
            if(eHuman == 4){
                defScore *= 4;
            }
                          
       
        return defScore;
    }
    
    private long defendScoreHorizontal(int currentRow, int currentColumn){
        long defScore = 0;
        int eHuman = 0;
        int ePC = 0;
        int eHuman2 = 0;
        int ePC2 = 0;
        // trai sang
        
            for(int i = 1; i < 5 && currentColumn + i < BanCo.columnNumbers; i++){
                if(oCoArray[currentRow][currentColumn + i].getPlayer() == 1){
                    ePC++;
                    break;
                }
                else if(oCoArray[currentRow][currentColumn + i].getPlayer() == -1){
                    eHuman++;
                   
                }
                else{
                    for(int j = 2; j < 6 && currentColumn + j < BanCo.columnNumbers; j++){
                        if(oCoArray[currentRow][currentColumn + j].getPlayer() == 1){
                            ePC2++;
                            break;
                        }
                        else if(oCoArray[currentRow][currentColumn + j].getPlayer() == -1){
                            eHuman2++;
                        }
                        else{
                            break;
                        }
                    }
                }
            }
            for(int i = 1; i < 5 && currentColumn - i >= 0; i++){
                if(oCoArray[currentRow][currentColumn - i].getPlayer() == 1){
                    ePC++;
                    break;
                }
                else if(oCoArray[currentRow][currentColumn - i].getPlayer() == -1){
                    eHuman++;
                    
                }
                else{
                    for(int j = 2; j < 6 && currentColumn - j >= 0; j++){
                        if(oCoArray[currentRow][currentColumn - j].getPlayer() == 1){
                            ePC2++;
                            break;
                        }
                        else if(oCoArray[currentRow][currentColumn - j].getPlayer() == -1){
                            eHuman2++;
                        }
                        else{
                            break;
                        }
                    }
                }
            }
       
        
        
            if(ePC == 2){
                return 0;
            }
            
            if(ePC == 0){
                defScore += defendScore[eHuman] * 2;
            }
            else{
                defScore += defendScore[eHuman];
            }
            
            if(eHuman >= eHuman2){
                defScore -= 1;
            }
            else{
                defScore -= 2;
            }
            
            if(eHuman == 4){
                defScore *= 4;
            }
                          
        
        return defScore;
    }
    
    private long defendScoreRightDiagonal(int currentRow, int currentColumn){
        long defScore = 0;
        int eHuman = 0;
        int ePC = 0;
        int eHuman2 = 0;
        int ePC2 = 0;
        
        // xuong
        
            for(int i = 1; i < 5 && currentColumn + i < BanCo.columnNumbers && currentRow + i < BanCo.rowNumbers; i++){
                if(oCoArray[currentRow + i][currentColumn + i].getPlayer() == 1){
                    ePC++;
                    break;
                }
                else if(oCoArray[currentRow + i][currentColumn + i].getPlayer() == -1){
                    eHuman++;
                  
                }
                else{
                    for(int j = 2; j < 6 && currentColumn + j < BanCo.columnNumbers && currentRow + j < BanCo.rowNumbers; j++){
                        if(oCoArray[currentRow + j][currentColumn + j].getPlayer() == 1){
                            ePC2++;
                            break;
                        }
                        else if(oCoArray[currentRow + j][currentColumn + j].getPlayer() == -1){
                            eHuman2++;
                        }
                        else{
                            break;
                        }
                    }
                }
            }
            for(int i = 1; i < 5 && currentColumn - i >= 0 && currentRow - i >= 0; i++){
                if(oCoArray[currentRow - i][currentColumn - i].getPlayer() == 1){
                    ePC++;
                    break;
                }
                else if(oCoArray[currentRow - i][currentColumn - i].getPlayer() == -1){
                    eHuman++;
                
                }
                else{
                    for(int j = 2; j < 6 && currentColumn - j >= 0 && currentRow - j >= 0; j++){
                        if(oCoArray[currentRow - j][currentColumn - j].getPlayer() == 1){
                            ePC2++;
                            break;
                        }
                        else if(oCoArray[currentRow - j][currentColumn - j].getPlayer() == -1){
                            eHuman2++;
                        }
                        else{
                            break;
                        }
                    }
                }
            }
        
            if(ePC == 2){
                return 0;
            }
            
            if(ePC == 0){
                defScore += defendScore[eHuman] * 2;
            }
            else{
                defScore += defendScore[eHuman];
            }
            
            if(eHuman >= eHuman2){
                defScore -= 1;
            }
            else{
                defScore -= 2;
            }
            
            if(eHuman == 4){
                defScore *= 4;
            }
                          
        
        
        return defScore;
    }
    
    private long defendScoreLeftDiagonal(int currentRow, int currentColumn){
        long defScore = 0;
        int eHuman = 0;
        int ePC = 0;
        int eHuman2 = 0;
        int ePC2 = 0;
        // len
        
            for(int i = 1; i < 5 && currentColumn + i < BanCo.columnNumbers && currentRow - i >= 0; i++){
                if(oCoArray[currentRow - i][currentColumn + i].getPlayer() == 1){
                    ePC++;
                    break;
                }
                else if(oCoArray[currentRow - i][currentColumn + i].getPlayer() == -1){
                    eHuman++;
                 
                }
                else{
                    for(int j = 2; j < 6 && currentColumn + j < BanCo.columnNumbers && currentRow - j >= 0; j++){
                        if(oCoArray[currentRow - j][currentColumn + j].getPlayer() == 1){
                            ePC2++;
                            break;
                        }
                        else if(oCoArray[currentRow - j][currentColumn + j].getPlayer() == -1){
                            eHuman2++;
                        }
                        else{
                            break;
                        }
                    }
                    break;
                }
            }
            for(int i = 1; i < 5 && currentColumn - i >= 0 && currentRow + i < BanCo.rowNumbers; i++){
                if(oCoArray[currentRow + i][currentColumn - i].getPlayer() == 1){
                    ePC++;
                    break;
                }
                else if(oCoArray[currentRow + i][currentColumn - i].getPlayer() == -1){
                    eHuman++;
                 
                }
                else{
                    for(int j = 2; j < 6 && currentColumn - j >= 0 && currentRow + j < BanCo.rowNumbers; j++){
                        if(oCoArray[currentRow + j][currentColumn - j].getPlayer() == 1){
                            ePC2++;
                            break;
                        }
                        else if(oCoArray[currentRow + j][currentColumn - j].getPlayer() == -1){
                            eHuman2++;
                        }
                        else{
                            break;
                        }
                    }
                    break;
                }
            }
       
            if(ePC == 2){
                return 0;
            }
            
            if(ePC == 0){
                defScore += defendScore[eHuman] * 2;
            }
            else{
                defScore += defendScore[eHuman];
            }
            
            if(eHuman >= eHuman2){
                defScore -= 1;
            }
            else{
                defScore -= 2;
            }
            
            if(eHuman == 4){
                defScore *= 4;
            }
                          
        
        
        return defScore;
    }
  
    
    
    /*
    
    // Ham tim nuoc di cho may
    private int maxDepth = 11;
    private int maxMove = 3;
    private int depth = 0;
    private boolean fWin = false;
    public int fEnd = 1;
    Point[] PCMove = new Point[maxMove + 2];
    Point[] HumanMove = new Point[maxMove + 2];
    Point[] WinMove = new Point[maxDepth + 2];
    Point[] LoseMove = new Point[maxDepth + 2];
    private void findMove(){
        if(depth > maxDepth) 
            return;
        depth++;
        fWin = false;
        boolean fLose = false;
        Point pcMove = new Point();
        Point humanMove = new Point();
        int countMove = 0;
        evaluateScore(1);
        //lay ra 3 nuoc di co diem cao nhat cho may
        //them
        for(OCo oCo : stList){
                    if(oCo.getPlayer() != 0)
                        scoreArrray[oCo.getLocation().x/25][oCo.getLocation().y/25] = 0;
                }
        //
        Point temp = new Point();
        for(int i = 0; i < maxMove; i++){
            temp = maxPosition();
            PCMove[i] = temp;
            scoreArrray[temp.x][temp.y] = 0;
        }
        
        // lay 3 nuoc di max trong PCMove ra oanh thu?
        countMove = 0;
        while(countMove < maxMove){
            pcMove = PCMove[countMove++];
            oCoArray[pcMove.x][pcMove.y].setPlayer(1);
            WinMove[depth - 1] = pcMove;
            
            //Tim nuoc di toi uu cho nguoi choi
            resetScoreArrray();
            evaluateScore(-1);           
            //lay ra 3 o diem cao nhat cho nguoi
            //them
            for(OCo oCo : stList){
                    if(oCo.getPlayer() != 0)
                        scoreArrray[oCo.getLocation().x/25][oCo.getLocation().y/25] = 0;
                }
            //
            for(int i = 0; i < maxMove; i++){
                temp = maxPosition();
                HumanMove[i] = temp;
                scoreArrray[temp.x][temp.y] = 0;
            }
            
            //oanh thu?
            for(int i = 0; i < maxMove; i++){
                humanMove = HumanMove[i];
                oCoArray[humanMove.x][humanMove.y].setPlayer(-1);
                
                if(checkWin()){
                    if(finish == Finish.Player1){
                        fWin = true;
                    }
                    if(finish == Finish.Player2){
                        fLose = true;
                    }
                    
                }
                if(fLose){
                    oCoArray[pcMove.x][pcMove.y].setPlayer(0);
                    oCoArray[humanMove.x][humanMove.y].setPlayer(0);
                    break;
                }
                if(fWin){
                    oCoArray[pcMove.x][pcMove.y].setPlayer(0);
                    oCoArray[humanMove.x][humanMove.y].setPlayer(0);
                    return;
                }
                findMove();
                oCoArray[humanMove.x][humanMove.y].setPlayer(0);
            }
            oCoArray[pcMove.x][pcMove.y].setPlayer(0);
            
        }
        
    }
    
    public void AI(){
        for(int i = 0; i < maxMove; i++){
            WinMove[i] = new Point();
            PCMove[i] = new Point();
            HumanMove[i] = new Point();
        }
        depth = 0;
        findMove();
    }
    */
    int x,y;
    public void startComputer(Graphics g){
        if(stList.empty()){
            play(BanCo.columnNumbers/2 * OCo.height + 1, BanCo.rowNumbers/2 * OCo.width +1, g);       
        }
        else{           
            OCo oCo = searchMove();
            play(oCo.getLocation().x + 1, oCo.getLocation().y + 1, g);
        }
    }
    
   
    private OCo searchMove() {
        OCo oCoResult = new OCo();
        long maxScore = 0;
        
        for(int i = 0; i < BanCo.rowNumbers; i++){
            for(int j = 0; j < BanCo.columnNumbers; j++){
                if(oCoArray[i][j].getPlayer() == 0){
                    long atkScore = attackScoreVertical(i, j) + attackScoreHorizontal(i, j) + attackScoreRightDiagonal(i, j) + attackScoreLeftDiagonal(i, j);
                    long defScore = defendScoreVertical(i,j) + defendScoreHorizontal(i,j) + defendScoreRightDiagonal(i,j) + defendScoreLeftDiagonal(i,j);             
                    long tempScore = atkScore > defScore? atkScore : defScore;
                    long sumScore = (atkScore + defScore) > tempScore ? (atkScore + defScore) : tempScore;
                    if(maxScore < sumScore){
                        maxScore = sumScore;
                        oCoResult = new OCo(oCoArray[i][j].getRow(), oCoArray[i][j].getColumn(), oCoArray[i][j].getLocation(), oCoArray[i][j].getPlayer());
                    }
                }
                
            }
        }
        
        return oCoResult;
    }
        
    
}
