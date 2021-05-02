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
public class SpaceUnit extends UpperSpaceUnit {
    private int curTime;
    public boolean containsMatter;
    
    public SpaceUnit()
    {
        curTime = energy = energyBefore = 0;
        energyPressure = new int[6];
        containsMatter = false;
    }

    @Override
    public void setInfEnergy() {
        energy = Integer.MAX_VALUE;
    }
    
    @Override
    public int getEnergy()
    {
        return energy;
    }
    
    @Override
    public int getEnergyWillingToAccept(int providedEnergy, int selfPressure, int maxEnergy)
    {
        return ((maxEnergy - selfPressure) * providedEnergy) / maxEnergy;
    }
    
    @Override
    void spreadEnergy(Universe un, int x, int y, int z) {
        //if su is matter its energy cannot fall below limit value
        int energyToRedistribute;
        if(containsMatter && energyBefore > Parameters.limitEnergyVal)
             energyToRedistribute = (energyBefore - Parameters.limitEnergyVal) >> 1;
        else
            energyToRedistribute = energyBefore >> 1;
        
        if(energyToRedistribute == 0)
            return;

        int [] energySlices = evaluateEnergySlices(energyToRedistribute);
        spreadEnergyToNeighbours(energySlices,un,x,y,z);
        
        //and finally update my own energy
        int aggr = aggregatedEnergy(energySlices);
        energy += energyBefore - aggr;
        
        //if we distributed some energy, there is some universe change
        if(aggr != 0)
            un.changed = true;
    }

    @Override
    boolean applyEnergyChange(int time, int x, int y, int z) {
        if(energy <= Parameters.limitEnergyVal)
        {
            createMatterAtPos(time,x,y,z);
            energy -= Parameters.matterEnergy;
            energyBefore = energy;
            return true;
        }
        energyBefore = energy;
        return false;
    }

    /*
        From energyBefore and from energy pressure of its neighbours calculates 
        how much energy is actualy spreaded to its neighbours
    */
    private int[] evaluateEnergySlices(int energyToRedistribute) {
        //energyBefore
        //energyToRedistribute
        //energyPressure[] 
        //calculate energy that are neighbours willing to accept
        int [] res = new int[6];
        int i=0;
        for(int item : energyPressure)
        {
            res[i] = (int) Math.floor(getSlice(energyBefore,item,energyToRedistribute));
            ++i;
        }
        
        //check whether there is higher consumption than energy provided
        int energyConsumption = aggregatedEnergy(res);
        
        if(energyConsumption > energyToRedistribute)
        {
            double factor = energyToRedistribute / energyConsumption;
            for(int item : energyPressure)
            {
                item = (int) factor*item;
            }  
        }
                
        return res;
    }

    private void spreadEnergyToNeighbours(int[] energySlices, Universe un, int x, int y, int z) {
        un.leftNeighbour    (x, y, z).energy    += energySlices[0];
        un.rightNeighbour   (x, y, z).energy    += energySlices[1];
        un.topNeighbour     (x, y, z).energy    += energySlices[2];
        un.bottomNeighbour  (x, y, z).energy    += energySlices[3];        
        un.frontNeighbour   (x, y, z).energy    += energySlices[4];
        un.backNeighbour    (x, y, z).energy    += energySlices[5];      
    }

    private int aggregatedEnergy(int [] aggr) {
        int res = 0;
        for(int i=0; i<6;++i)
            res += aggr[i];
        
        return res;
    }

    private double getSlice(int max, int act, int avail) {
        double maxd = max;
        return ((maxd - act) / maxd) * avail;
    }

    private void createMatterAtPos(int time, int x, int y, int z) {
        
    }

}
