/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.busdakta.simplerelativity;

/**
 *
 * @author famer
 */
public class SimpleUniverseSimulator {
    public static void main(String[] args){
        
	System.out.println("Hello World");
        //pick neighbourhood type
        Parameters.Neighbourhood n = Parameters.Neighbourhood.MOORE;
        
	Universe u = new Universe(666,n);
        u.initFromInfSingularity();
        
        int time = 0;
        while(u.changed)
        {
            u.clearEnergyBeforeStep();
            u.step(time);
            
            if(u.matterGenerationStopped)
                throw new UnsupportedOperationException("Post matter generation - not supported yet");
            
            ++time;
        }
        
    }
}
