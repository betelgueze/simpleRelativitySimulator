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
public class Universe {
    public final SpaceUnit space[][][];
    private final int middleIndex;
    private final Parameters.Neighbourhood nt;
    public boolean changed;
    public int matterParticles;
    public int matterParticlesBefore;
    public boolean matterGenerationStopped;
    public boolean matterGenerationStarted;
    private final int spaceSize;
    
    public Universe(int oneDimSize, Parameters.Neighbourhood n)
    {
        space = new SpaceUnit[oneDimSize][oneDimSize][oneDimSize];
        middleIndex = oneDimSize / 2;
        spaceSize = oneDimSize;
        nt = n;
        changed = false;
        matterGenerationStopped = false;
        matterGenerationStarted = false;
        matterParticles = 0;
        matterParticlesBefore = 0;
    }
    
    public void initFromInfSingularity()
    {
        space[middleIndex][middleIndex][middleIndex].setInfEnergy();
        changed = true;
    }

    void step(int time) {
        changed = false;
        matterParticles = 0;
        
        for(int i=spaceSize; i > 0; --i)
        {
            for(int j=spaceSize; j > 0; --j)
            {
                for(int k=spaceSize; k > 0; --k)
                {
                    SpaceUnit su = space[i][j][k];
                    if(su.energyBefore > 0)
                        su.spreadEnergy(this);
                }
            }
        }
        
        if(changed)
        {
            for(int i=spaceSize; i > 0; --i)
            {
                for(int j=spaceSize; j > 0; --j)
                {
                    for(int k=spaceSize; k > 0; --k)
                    {
                        if(space[i][j][k].applyEnergyChange(time) && matterGenerationStarted == false)
                            matterGenerationStarted = true;
                        
                        if(matterGenerationStarted)
                        {
                            if(space[i][j][k].m.isActuallyMatter())
                                ++matterParticles;
                        }
                    }
                }
            }
        }
        
        if(matterParticlesBefore != matterParticles)
            matterParticlesBefore = matterParticles;
        else
            matterGenerationStopped = true;
    }

    void clearEnergyBeforeStep() {
        for(int i= spaceSize; i > 0; --i)
        {
            for(int j= spaceSize; j > 0; --j)
            {
                for(int k= spaceSize; k > 0; --k)
                {
                    space[i][j][k].energyBefore = space[i][j][k].energy;
                    space[i][j][k].energy = 0;
                }
            }
        }
    }
    
    int AggregateSpaceEnergy()
    {
        int aggregatedEnergy = 0;
        for(int i= spaceSize; i > 0; --i)
        {
            for(int j= spaceSize; j > 0; --j)
            {
                for(int k= spaceSize; k > 0; --k)
                {
                    aggregatedEnergy += space[i][j][k].energy;
                }
            }
        }
        return aggregatedEnergy;
    }
    
}
