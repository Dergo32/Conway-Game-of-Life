/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package conwaygameoflife;

import java.awt.Color;
import java.awt.Graphics;
import javax.swing.JPanel;

/**
 * Draws the screen of Conway's Game of Life - This is not the main class
 * @author 464064
 */

//"extends" means that class has all functions of JPanel (can be placed into components, etc.)
public class GUIPanel extends JPanel {
    boolean[][] grid;
    double width;  //measures width of cell
    //use double because of uneven number of pixels per cell; prevent rounding
    double height; //measures height of cell
    
    //get input from other class and pass to this class
    public GUIPanel(boolean[][] input){
        grid = input;
    }
    
    //override paint component
    @Override
    public void paintComponent(Graphics g){
        super.paintComponent(g);    //"super" means use parent's (JPanel's) version of class
        //get size from panel and split it into sections
        width = (double)this.getWidth()/grid[0].length;
        height = (double)this.getHeight()/grid.length;
        
        //color of clicked square
        g.setColor(Color.BLACK);
        for(int row = 0; row < grid.length; row++){
            for(int column = 0; column < grid[0].length; column++){
                //draw box if cell is alive
                if(grid[row][column] == true){
                    //add 1 to the width and height to fill in possible gaps
                    //row is where in the row, column is which row
                    g.fillRect((int)Math.round(column*width), (int)Math.round(row*height), (int)(width+1), (int)(height+1));
                }
            }
        }
        
        //must set color before drawing
        //to create new color, use "new Color(R,G,B)
        g.setColor(Color.GRAY);
        
        //create grid of squares
        //x is which row, y is where in the row
        for(int x = 0; x < grid[0].length+1; x++){
            //"long" is larger, longer version of an int
            g.drawLine((int)Math.round(x*width),0,(int)Math.round(x*width),this.getHeight());
        }
        
        for(int y = 0; y < grid.length+1; y++){
            g.drawLine(0,(int)Math.round(y*height),this.getWidth(),(int)Math.round(y*height));
        }
        
    }
    //set grid to uppdated version of 
    void setGrid(boolean[][]nextgen){
        grid = nextgen;
    }
}
