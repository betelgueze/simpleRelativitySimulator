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
public class SpaceUnit {
    public int energy;
    public int energyBefore;
    public Matter m;
    
    int curTime;
    
    //internal property
    int x,y,z;
    
    
    public SpaceUnit()
    {
        x = y = z = curTime = energy = energyBefore = 0;
        m = new Matter();
    }
    
    public void setInternalProperties(Vec3 pos)
    {
        x = pos.x;
        y = pos.y;
        z = pos.z;
    }

    public void setInfEnergy() {
        energy = Integer.MAX_VALUE;
    }
    
    void spreadEnergy(Universe un) {
        //if su is matter its energy cannot fall below limit value
        int energyToRedistribute;
        if(m.isActuallyMatter() && energyBefore > Parameters.limitEnergyVal)
             energyToRedistribute = (energyBefore - Parameters.limitEnergyVal) >> 1;
        else
            energyToRedistribute = energyBefore >> 1;
        
        if(energyToRedistribute == 0)
            return;

        int energySlice = energyToRedistribute >> 3;
        int energyAccepted = 0;
        //for every neighbour analyze whether it can accept energy
        energyAccepted += un.space[x][y][z + 1].spreadEnergy(un,energySlice,energyBefore);
        energyAccepted += un.space[x][y + 1][z].spreadEnergy(un,energySlice,energyBefore);
        energyAccepted += un.space[x][y + 1][z + 1].spreadEnergy(un,energySlice,energyBefore);
        energyAccepted += un.space[x + 1][y][z].spreadEnergy(un,energySlice,energyBefore);
        energyAccepted += un.space[x + 1][y][z + 1].spreadEnergy(un,energySlice,energyBefore);
        energyAccepted += un.space[x + 1][y + 1][z].spreadEnergy(un,energySlice,energyBefore);
        
        energy -= (energyToRedistribute - energyAccepted);    
    }

    boolean applyEnergyChange(int time) {
        energyBefore = energy;
        if(energy <= Parameters.limitEnergyVal)
        {
            m.createUnitWeight(time);
            return true;
        }
        return false;
    }

    //returns value of energy accepted
    private int spreadEnergy(Universe un, int providedEnergy, int limitEnergy) {
        //if spaceUnit has bigger energy than limitEnergy, it cannot accept anything
        if(energyBefore >= limitEnergy)
            return 0;
        
        //evaluate how big portion of a provided energy is spaceUnit willing to accept
        int acceptedEnergy = ((limitEnergy - energyBefore) * providedEnergy) / limitEnergy;
        energy += acceptedEnergy;
        return acceptedEnergy;
    }
}
