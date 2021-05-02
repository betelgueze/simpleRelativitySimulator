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
        
	Universe u = new Universe(100,n);
        u.initFromInfSingularity();
        
        UniverseVisualizator uv = new UniverseVisualizator(u);
        
        int time = 0;
        while(u.changed)
        {
            //clears energy property of every SpaceUnit
            u.clearEnergyBeforeStep();
            
            //handles currently energy distribution and matter creation
            u.step(time);
            
            //dummy end of simulation
            /*if(u.matterGenerationStarted && u.matterGenerationStopped)
                throw new UnsupportedOperationException("Post matter generation - not supported yet");*/
            
            //calculating invariants
            assert(u.calculateInvariants());
            
            //visualize universe
            assert(uv.update());
            
            //recalculates energy pressure for every SpaceUnit
            u.applyEnergyPressure();
            
            ++time;
        }
        
    }
}
