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
public class Vec3 {
  public final int x;
  public final int y;
  public final int z;

  public Vec3(int x, int y, int z) {
    this.x = x;
    this.y = y;
    this.z = z;
  }
  
  public Vec3(Vec3 v) {
    this.x = v.x;
    this.y = v.y;
    this.z = v.z;
  }

  // Getters...

  public Vec3 add(Vec3 addend) {
    return new Vec3(x + addend.x, y + addend.y, z + addend.z);
  }

  // Other operations...
}
