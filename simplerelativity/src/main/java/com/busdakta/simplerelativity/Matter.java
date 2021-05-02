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
public class Matter {
    public int absoluteVelocity = 0;
    public Vec3 velocity = new Vec3(0,0,0);
    public int weight = 0;
    public int momentum = 0;
    public int timeCreated = -1;
    
    public Matter()
    {
    }
    
    boolean isActuallyMatter() {
        return weight > 0;
    }

    void createUnitWeight(int time) {
        weight = 1;
        timeCreated = time;
    }
    
}
