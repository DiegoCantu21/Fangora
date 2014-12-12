/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fangora;

import java.awt.Point;

/**
 *
 * @author diegocantu
 */
public interface GridDrawData {
    public int getCellHeight();
    public int getCellWidth();
    
    public Point getcellSystemCoordinate(Point cellCoordinate);
    
    
}
