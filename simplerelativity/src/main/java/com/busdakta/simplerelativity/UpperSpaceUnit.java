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
public class UpperSpaceUnit {
    enum posInUniverseEnum {GENERAL,LEFTB,RIGHTB,TOPB,BOTTOMB,FRONTB,BACKB};
    posInUniverseEnum posInUniverse;
    
    int energyBefore;
    int energy;
    int[] energyPressure;
    
    public UpperSpaceUnit()
    {
    }
    
    public UpperSpaceUnit(int x, int y, int z, int spaceSize)
    {
        energyBefore = energy = 0;
        
        if(x == 0)
            posInUniverse = posInUniverseEnum.LEFTB;
        else if(y == 0)
            posInUniverse = posInUniverseEnum.BOTTOMB;
        else if(z == 0)
            posInUniverse = posInUniverseEnum.FRONTB;
        else if(x == spaceSize - 1)
            posInUniverse = posInUniverseEnum.RIGHTB;
        else if(y == spaceSize - 1)
            posInUniverse = posInUniverseEnum.TOPB;
        else if(z == spaceSize - 1)
            posInUniverse = posInUniverseEnum.BACKB;
        else
            posInUniverse = posInUniverseEnum.GENERAL;
    }
    
    public int getEnergy()
    {
        return 0;
    }
    
    /**
     * UpperSpaceUnit accepts everything and returns that energy in next step to 
     * its only neighbor, no singularities can evolve here
     * 
     * @param energyProvided - energy provided from its only neighbor
     * @param selfPressure - should be always 0
     * @param maxEnergy - energy of its only neighbor
     * @return energyProvided
     */
    public int getEnergyWillingToAccept(int energyProvided, int selfPressure, int maxEnergy)
    {
        assert(selfPressure == 0);
        return energyProvided;
    }

    void setInfEnergy() {
        throw new UnsupportedOperationException("UpperSpaceUnit cannot have infinite energy!");
    }

    void spreadEnergy(Universe un, int x, int y, int z) {
        switch(posInUniverse)
        {
            case LEFTB -> un.rightNeighbour(x, y, z).energy += energyBefore;
            case RIGHTB -> un.leftNeighbour(x, y, z).energy += energyBefore;
                
            case BOTTOMB -> un.topNeighbour(x, y, z).energy += energyBefore;
            case TOPB -> un.bottomNeighbour(x, y, z).energy += energyBefore;                
                
            case FRONTB -> un.backNeighbour(x, y, z).energy += energyBefore;
            case BACKB -> un.frontNeighbour(x, y, z).energy += energyBefore;       
        }
    }

    boolean applyEnergyChange(int time, int x, int y, int z) {
        energyBefore = energy;
        return false;
    }
}
