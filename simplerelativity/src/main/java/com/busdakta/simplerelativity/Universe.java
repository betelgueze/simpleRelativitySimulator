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
    public final UpperSpaceUnit space[][][];
    private final int middleIndex;
    private final Parameters.Neighbourhood nt;
    public boolean changed;
    public int matterParticles;
    public int matterParticlesBefore;
    public boolean matterGenerationStopped;
    public boolean matterGenerationStarted;
    public final int spaceSize;
    
    public Universe(int oneDimSize, Parameters.Neighbourhood n)
    {
        middleIndex = oneDimSize / 2;
        spaceSize = oneDimSize;
        nt = n;
        changed = false;
        matterGenerationStopped = false;
        matterGenerationStarted = false;
        matterParticles = 0;
        matterParticlesBefore = 0;
        
        //space has mantinels which acts as absolute accepters of energy
        //and also in next step returns that energy where it came from
        // we have to quite uneffectively initialize them
        space = new UpperSpaceUnit[oneDimSize][oneDimSize][oneDimSize];
        for(int i= (spaceSize - 1); i > -1; --i)
        {
            for(int j= (spaceSize - 1); j > -1; --j)
            {
                for(int k= (spaceSize - 1); k > -1; --k)
                {
                    if(     ((k == (spaceSize - 1)) || k == 0) ||
                            ((j == (spaceSize - 1)) || j == 0) ||
                            ((i == (spaceSize - 1)) || i == 0))
                        space[i][j][k] = new UpperSpaceUnit(i,j,k,spaceSize);
                }
            }
        }
        for(int i= (spaceSize - 2); i > 0; --i)
        {
            for(int j= (spaceSize - 2); j > 0; --j)
            {
                for(int k= (spaceSize - 2); k > 0; --k)
                {
                    space[i][j][k] = new SpaceUnit();
                }
            }
        }
    }
    
    public void initFromInfSingularity()
    {
        space[middleIndex][middleIndex][middleIndex].setInfEnergy();
        changed = true;
    }

    void step(int time) {
        changed = false;
        
        //first we distribute energy
        for(int i= 0; i < spaceSize; ++i)
        {
            for(int j= 0; j < spaceSize; ++j)
            {
                for(int k= 0; k < spaceSize; ++k)
                {
                    UpperSpaceUnit su = space[i][j][k];
                    if(su.energyBefore > 0)
                        su.spreadEnergy(this,i,j,k);
                }
            }
        }
        
        //then we analyze matter creation
        if(changed)
        {
            for(int i= 0; i < spaceSize; ++i)
            {
                for(int j= 0; j < spaceSize; ++j)
                {
                    for(int k= 0; k < spaceSize; ++k)
                    {
                        if(space[i][j][k].applyEnergyChange(time,i,j,k) && matterGenerationStarted == false)
                            matterGenerationStarted = true;
                    }
                }
            }
        }
        matterParticles = Matter.matterCount;
        if(matterParticlesBefore != matterParticles)
            matterParticlesBefore = matterParticles;
        else
            matterGenerationStopped = true;
        
        //then we apply simple powers of the universe
    }

    void clearEnergyBeforeStep() {
        for(int i= 0; i < spaceSize; ++i)
        {
            for(int j= 0; j < spaceSize; ++j)
            {
                for(int k= 0; k < spaceSize; ++k)
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
        for(int i= 0; i < spaceSize; ++i)
        {
            for(int j= 0; j < spaceSize; ++j)
            {
                for(int k= 0; k < spaceSize; ++k)
                {
                    aggregatedEnergy += space[i][j][k].energy;
                }
            }
        }
        return aggregatedEnergy;
    }
    
    UpperSpaceUnit leftNeighbour(int x, int y, int z)
    {
        if(x > 0)
            return space[x-1][y][z];
        else
            return null;
    }
    
    UpperSpaceUnit rightNeighbour(int x, int y, int z)
    {
        if(x < (spaceSize - 1))
            return space[x+1][y][z];
        else
            return null;
    }
    
    UpperSpaceUnit bottomNeighbour(int x, int y, int z)
    {
        if(y > 0)
            return space[x][y-1][z];
        else
            return null;
    }
    
    UpperSpaceUnit topNeighbour(int x, int y, int z)
    {
        if(y < (spaceSize - 1))
            return space[x][y+1][z];
        else
            return null;
    }
    
    UpperSpaceUnit frontNeighbour(int x, int y, int z)
    {
        if(z > 0)
            return space[x][y][z-1];
        else
            return null;
    }
    
    UpperSpaceUnit backNeighbour(int x, int y, int z)
    {
        if(z < (spaceSize - 1))
            return space[x][y][z+1];
        else
            return null;
    }
    
    boolean isMantinel(int x, int y, int z)
    {
        if(x == 0 || x == (spaceSize - 1))
            return true;
        else if(y == 0 || y == (spaceSize - 1))
            return true;
        else if(z == 0 || z == (spaceSize - 1))
            return true;
        else
            return false;
    }

    void applyEnergyPressure() {
        for(int i= 1; i < (spaceSize-1); ++i)
        {
            for(int j= 1; j < (spaceSize-1); ++j)
            {
                for(int k= 1; k < (spaceSize-1); ++k)
                {                    
                    UpperSpaceUnit su = space[i][j][k];
                    UpperSpaceUnit suNeighbour;
                    
                    int index = 0;
                    suNeighbour = leftNeighbour(i, j, k);                    
                    su.energyPressure[index] = suNeighbour.getEnergy();
                    
                    ++index;                    
                    suNeighbour = rightNeighbour(i, j, k);                    
                    su.energyPressure[index] = suNeighbour.getEnergy();
                    
                    ++index;                    
                    suNeighbour = topNeighbour(i, j, k);                    
                    su.energyPressure[index] = suNeighbour.getEnergy();
                    
                    ++index;                    
                    suNeighbour = bottomNeighbour(i, j, k);                    
                    su.energyPressure[index] = suNeighbour.getEnergy();
                    
                    ++index;                    
                    suNeighbour = frontNeighbour(i, j, k);                    
                    su.energyPressure[index] = suNeighbour.getEnergy();
                    
                    ++index;                    
                    suNeighbour = backNeighbour(i, j, k);
                    su.energyPressure[index] = suNeighbour.getEnergy();
                }
            }
        }
    }

    boolean calculateInvariants() {
        //check energy is not disappearing
        int aggrEnergy = 0;
        for(int i= 0; i < spaceSize; ++i)
        {
            for(int j= 0; j < spaceSize; ++j)
            {
                for(int k= 0; k < spaceSize; ++k)
                {
                    aggrEnergy += space[i][j][k].energy;
                }
            }
        }
        if(aggrEnergy != Integer.MAX_VALUE)
            System.err.println("energy has changed to:"+ Integer.toString(aggrEnergy) + " diff:" + Integer.toString(Integer.MAX_VALUE - aggrEnergy));
        return true;
    }

    void initFromSecondStep() {
        space[middleIndex][middleIndex][middleIndex].energy = 357913942;
        space[middleIndex + 1][middleIndex][middleIndex].energy = 357913941;
        space[middleIndex - 1][middleIndex][middleIndex].energy = 357913941;
        space[middleIndex][middleIndex + 1][middleIndex].energy = 357913941;
        space[middleIndex][middleIndex - 1][middleIndex].energy = 357913941;
        space[middleIndex][middleIndex][middleIndex + 1].energy = 357913941;
        space[middleIndex][middleIndex][middleIndex - 1].energy = 357913941;
        
        this.applyEnergyPressure();

        changed = true;
    }

}
