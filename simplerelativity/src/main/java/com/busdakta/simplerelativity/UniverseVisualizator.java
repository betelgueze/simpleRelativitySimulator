/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.busdakta.simplerelativity;

import java.awt.Dimension;
import java.awt.Toolkit;
import javax.swing.*;

/**
 *
 * @author famer
 */
public class UniverseVisualizator {    
    //swing components
    JFrame frame;
    UniverseVisualizatorJPanel unvp;
    
    public UniverseVisualizator(Universe un)
    {   
        frame = new JFrame("SimpleRelativitySimulator");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        //setting up proper window size
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int width = screenSize.width;
        int height = screenSize.height;
        
        //pick rectangular window size
        if(width < height)
            height = width;
        else
            width = height;
        
        unvp = new UniverseVisualizatorJPanel(un,width,height);
        //set the window size
        frame.setSize(width, height);
        frame.add(unvp);
        
        frame.setVisible(true);
    }

    boolean update() {
        frame.repaint();
        return true;
    }
}
