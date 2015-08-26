/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jspmm.matrix;

/**
 *
 * Compressed Column Storage (CCS/CSC) Matrix
 *
 * @author cplonka
 */
public class CCSMatrix extends AbstractMatrix {

    public final float[] values;
    public final int[] rowIdx;
    public final int[] colPtr;

    public CCSMatrix(int nrow, int ncol, float[] values, int[] rowIdx, int[] colPtr) {
        super(nrow, ncol);
        this.values = values;
        this.rowIdx = rowIdx;
        this.colPtr = colPtr;
    }

    /**
     * create an CCS from an dense matrix
     *
     * @param values
     * @param ncol
     * @return
     */
    public static CCSMatrix create(float[] values, int ncol) {
        int nz = 0;
        int nrow = values.length / ncol;
        //count nonzero elements
        for (float value : values) {
            if (value != 0) {
                nz++;
            }
        }
        //
        float[] val = new float[nz];
        int[] rowIdx = new int[nz];
        int[] colPtr = new int[ncol + 1];
        //scan column major
        for (int j = 0, nnz = 0; j < ncol; j++) {
            for (int i = 0; i < nrow; i++) {
                float value = values[i * ncol + j];
                if (value != 0) {
                    val[nnz] = value;
                    //row idx for this values
                    rowIdx[nnz++] = i;
                }
            }
            //next col, col_ptr
            colPtr[j + 1] = nnz;
        }

        return new CCSMatrix(nrow, ncol, val, rowIdx, colPtr);
    }

    public float[] multiply(float[] vector) {
        if (ncol != vector.length) {
            throw new IllegalArgumentException("Can not multipy matrixes [col != row].");
        }

        float[] ret = new float[nrow];
        for (int i = 0; i < ncol; i++) {
            for (int j = colPtr[i]; j < colPtr[i + 1]; j++) {
                ret[rowIdx[j]] += values[j] * vector[i];
            }
        }
        return ret;
    }

    @Override
    public float get(int i, int j) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void set(int i, int j, float value) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
