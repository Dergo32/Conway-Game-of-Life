/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package conwaygameoflife;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JFrame;

/**
 * Simulation of Conway's Game of Life
 * @author 464064
 */
public class ConwayGameOfLife implements MouseListener, ActionListener, Runnable{

    //TODO: Add randomize and clearing functionality
    //grid of cells
    boolean[][] cells = new boolean[50][50];
        
    JFrame frame = new JFrame("Conway's Game of Life");
    GUIPanel panel = new GUIPanel(cells);
    
    Container buttons = new Container();
    JButton start = new JButton("Start");
    JButton step = new JButton("Step");
    JButton stop = new JButton("Stop");
    
    JButton random = new JButton("Randomize");
    //generate random booleans using java.util.random
    private Random randBoo = new Random();
    JButton clear = new JButton("Clear");
    
    boolean isRunning = false;
    
    public ConwayGameOfLife(){
        //window
        frame.setSize(700,700);
        frame.setLayout(new BorderLayout());
        //grid
        frame.add(panel, BorderLayout.CENTER);
        panel.addMouseListener(this);
        //side buttons (start, stop, step)
        buttons.setLayout(new GridLayout(3,1));
        buttons.add(start);
        start.addActionListener(this);
        buttons.add(step);
        step.addActionListener(this);
        buttons.add(stop);
        stop.addActionListener(this);
        buttons.add(random);
        random.addActionListener(this);
        buttons.add(clear);
        clear.addActionListener(this);
        
        frame.add(buttons, BorderLayout.WEST);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
       new ConwayGameOfLife();  //create new instance
    }

    //combination of press and release
    @Override
    public void mouseClicked(MouseEvent e) {
    }

    //press down mouse button over
    @Override
    public void mousePressed(MouseEvent e) {
    }

    //release mouse button over
    @Override
    public void mouseReleased(MouseEvent e) {
        //for mouse events, x is where in row and y is which row
        //for debugging
        //System.out.println(e.getX()+","+e.getY());
        
        //divide panel into cells
        double width = (double)panel.getWidth()/cells[0].length;
        double height = (double)panel.getHeight()/cells.length;
        //find column and row by dividing position of release by width or height
        int column = Math.min(cells[0].length - 1, (int)(e.getX() / width));
        int row  = Math.min(cells.length - 1, (int)(e.getY() / height));
        cells[row][column] = !cells[row][column];   //alternate between enabled and disabled
        frame.repaint();    //special java method that repaints the frame
    }

    //hover over
    @Override
    public void mouseEntered(MouseEvent e) {
    }

    //hover out of
    @Override
    public void mouseExited(MouseEvent e) {
    }

    //run the simulation
    public void actionPerformed(ActionEvent e) {
        //start btn
        //endless repeating simulations
        if(e.getSource().equals(start)){
            //use multiple threads to update window while simulation is running
            //simulation is its own process
            isRunning = true;
            if(isRunning){
                Thread t = new Thread(this);
                t.start();  //thread starts new process using run method to run simulation
            }
        }
        //step btn
        //one cycle of simulation
        if(e.getSource().equals(step)){
            //not running continously
            isRunning = false;
            simulation();
        }
        //stop btn
        if(e.getSource().equals(stop)){
            isRunning = false;
        }
        
        //random btn
        if(e.getSource().equals(random)){
            for(int row = 0; row < cells.length; row++){
                for(int column = 0; column < cells[0].length; column++){
                    cells[row][column] = randBoo.nextBoolean();
                    frame.repaint();
                }
            }    
        }
        
        //clear btn
        if(e.getSource().equals(clear)){
            for(int row = 0; row < cells.length; row++){
                for(int column = 0; column < cells[0].length; column++){
                    cells[row][column] = false;
                    frame.repaint();
                }
            }    
        }
    }
    
    //Conway's Game of Life, in an algorithm
    private void simulation(){
        //do not update cells until after checking
        //nextgen is what cells are alive based on neighbors in current gen, not other neigbors in future gen
        boolean[][] nextgen = new boolean[cells.length][cells[0].length];
        for(int row = 0; row < cells.length; row++){
            for(int column = 0; column < cells[0].length; column++){
                //check cell neighbors
                int neighborCount = 0;
 
                //top side
                //fix this
                if(row > 0 && cells[row-1][column] == true){
                    neighborCount++;
                }

                //bottom side
                if(row < cells.length-1 && cells[row+1][column] == true){
                    neighborCount++;
                }
                
                //right side
                if(column < cells[0].length-1 && cells[row][column+1] == true){
                    neighborCount++;
                }
                
                //left side
                if(column > 0 && cells[row][column-1] == true){
                    neighborCount++;
                }
                
                //upper right corner
                if(row > 0 && column < cells[0].length-1 && cells[row-1][column+1] == true){
                    neighborCount++;
                }
                
                //upper left corner
                if(row > 0 && column > 0 && cells[row-1][column-1] == true){
                    neighborCount++;
                }
                
                //lower right corner
                if(row < cells.length-1 && column < cells[0].length-1 && cells[row+1][column+1] == true){
                    neighborCount++;
                }
                
                //lower left corner
                if(row < cells.length-1 && column > 0 && cells[row+1][column-1] == true){
                    neighborCount++;
                }
                
                //if alive
                if(cells[row][column]){
                    if(neighborCount == 2 || neighborCount == 3){
                        nextgen[row][column] = true;
                    }else{
                        nextgen[row][column] = false;
                    }
                //else if dead    
                }else{
                    if(neighborCount == 3){
                        nextgen[row][column] = true;
                    }else{
                        nextgen[row][column] = false;
                    }
                }
            }
        }
        //after checking everything, update cells
        cells = nextgen;
        panel.setGrid(nextgen);
        frame.repaint();
    }

    //start new thread of execution
    @Override
    public void run() {
        while(isRunning){
            simulation();
            //wait half a second before running again (note: thread sleep may cause problems
            try {
                Thread.sleep(200);
                //thread may be interrrupted by another process
            } catch (InterruptedException ex) {
                Logger.getLogger(ConwayGameOfLife.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
}
