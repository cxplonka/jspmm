/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jspmm.matrix;

/**
 *
 * @author cplonka
 */
public abstract class AbstractMatrix {

    public final int nrow;
    public final int ncol;

    public AbstractMatrix(int nrow, int ncol) {
        this.nrow = nrow;
        this.ncol = ncol;
    }

    public abstract float get(int i, int j);

    public abstract void set(int i, int j, float value);

}
