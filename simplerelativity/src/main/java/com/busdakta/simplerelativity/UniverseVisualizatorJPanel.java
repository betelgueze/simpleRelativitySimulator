/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.busdakta.simplerelativity;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.DataBufferInt;
import java.awt.image.Raster;
import java.awt.image.WritableRaster;
import javax.swing.JPanel;

/**
 *
 * @author famer
 */
class UniverseVisualizatorJPanel extends JPanel
{
    Universe unref;
    
    BufferedImage xyimg;
    BufferedImage xzimg;
    BufferedImage yzimg;
    
    int width;
    int height;

    public UniverseVisualizatorJPanel(Universe un, int w, int h)
    {
        unref = un;
        
        width = w;
        height = h;
    }

    @Override
    public void paintComponent(Graphics g)
    {
        //update xy image
        //construct onedim array
        int[] matrix = new int[unref.spaceSize * unref.spaceSize];
        
        getXYViewIntoArray(matrix,unref.spaceSize);

        DataBufferInt buffer = new DataBufferInt(matrix, matrix.length);

        int[] bandMasks = {0xFF0000, 0xFF00, 0xFF, 0xFF000000}; // ARGB (yes, ARGB, as the masks are R, G, B, A always) order
        WritableRaster raster = Raster.createPackedRaster(buffer, unref.spaceSize, unref.spaceSize, unref.spaceSize, bandMasks, null);

        ColorModel cm = ColorModel.getRGBdefault();
        xyimg = new BufferedImage(cm, raster, cm.isAlphaPremultiplied(), null);
        
        getYZViewIntoArray(matrix,unref.spaceSize);

        buffer = new DataBufferInt(matrix, matrix.length);
        
        raster = Raster.createPackedRaster(buffer, unref.spaceSize, unref.spaceSize, unref.spaceSize, bandMasks, null);

        yzimg = new BufferedImage(cm, raster, cm.isAlphaPremultiplied(), null);
        
        getXZViewIntoArray(matrix,unref.spaceSize);

        buffer = new DataBufferInt(matrix, matrix.length);
        
        raster = Raster.createPackedRaster(buffer, unref.spaceSize, unref.spaceSize, unref.spaceSize, bandMasks, null);

        xzimg = new BufferedImage(cm, raster, cm.isAlphaPremultiplied(), null);

        
        g.drawImage(xyimg, 0, 0, width/2 -1, height/2 - 1, null);
        g.drawImage(xzimg, width/2, 0, width/2 -1, height/2 - 1, null);
        g.drawImage(yzimg, 0, height/2, width/2 -1, height/2 - 1, null);
    }

    private void getXYViewIntoArray(int[] matrix, int stride) {
        int index = 0;
        for(int i= 0; i < unref.spaceSize; ++i)
        {            
            for(int j= 0; j < unref.spaceSize; ++j)
            {
                int resVal = 0;
                for(int k= 0; k < unref.spaceSize; ++k)
                {
                    resVal += unref.space[i][j][k].energy;
                }
                matrix[index] = resVal;
                ++index;
            }
        }
    }
    
    private void getYZViewIntoArray(int[] matrix, int stride) {
        int index = 0;
        for(int i= 0; i < unref.spaceSize; ++i)
        {            
            for(int k= 0; k < unref.spaceSize; ++k)
            {
                int resVal = 0;
                for(int j= 0; j < unref.spaceSize; ++j)
                {
                    resVal += unref.space[i][j][k].energy;
                }
                matrix[index] = resVal;
                ++index;
            }
        }
    }
    
    private void getXZViewIntoArray(int[] matrix, int stride) {
        int index = 0;
        for(int k= 0; k < unref.spaceSize; ++k)
        {            
            for(int j= 0; j < unref.spaceSize; ++j)
            {
                int resVal = 0;
                for(int i= 0; i < unref.spaceSize; ++i)
                {
                    resVal += unref.space[i][j][k].energy;
                }
                matrix[index] = resVal;
                ++index;
            }
        }
    }
}