/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jspmm.matrix;

import it.unimi.dsi.fastutil.longs.Long2FloatOpenHashMap;

/**
 *
 * @author cplonka
 */
public class MutableCOOMatrix extends AbstractMatrix {

    private final Long2FloatOpenHashMap _backBuffer = new Long2FloatOpenHashMap();

    public MutableCOOMatrix(int nrow, int ncol) {
        super(nrow, ncol);
    }

    public static MutableCOOMatrix create(float[] values, int ncol) {
        int nrow = values.length / ncol;
        MutableCOOMatrix ret = new MutableCOOMatrix(nrow, ncol);
        //scan column major
        for (int i = 0; i < nrow; i++) {
            for (int j = 0; j < ncol; j++) {
                float value = values[i * ncol + j];
                if (value != 0) {
                    ret.set(i, j, value);
                }
            }
        }
        return ret;
    }

    @Override
    public float get(int i, int j) {
        return _backBuffer.get(pack(i, j));
    }

    @Override
    public void set(int i, int j, float value) {
        _backBuffer.put(pack(i, j), value);
    }

    public int getNnz(){
        return _backBuffer.size();
    }
    
    public void merge(MutableCOOMatrix m){
        _backBuffer.putAll(m._backBuffer);
    }
    
    static long pack(int x, int y) {
        long xPacked = ((long) x) << 32;
        long yPacked = y & 0xFFFFFFFFL;
        return xPacked | yPacked;
    }
}