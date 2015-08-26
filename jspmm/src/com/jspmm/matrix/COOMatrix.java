/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jspmm.matrix;

/**
 * Coordinate Storage Format (COO)
 *
 * @author cplonka
 */
public class COOMatrix extends AbstractMatrix {

    public final float[] values;
    public final int[] rowIdx;
    public final int[] colIdx;

    public COOMatrix(int nrow, int ncol, float[] values, int[] rowIdx, int[] colIdx) {
        super(nrow, ncol);
        this.values = values;
        this.rowIdx = rowIdx;
        this.colIdx = colIdx;
    }

    /**
     * create an COO from an dense matrix
     *
     * @param values
     * @param ncol
     * @return
     */
    public static COOMatrix create(float[] values, int ncol) {
        int nz = 0;
        int row = values.length / ncol;
        //count nonzero elements
        for (float value : values) {
            if (value != 0) {
                nz++;
            }
        }
        //
        float[] val = new float[nz];
        int[] rowIdx = new int[nz];
        int[] colIdx = new int[nz];
        //scan column major
        for (int i = 0, idx = 0; i < row; i++) {
            for (int j = 0; j < ncol; j++) {
                float value = values[i * ncol + j];
                if (value != 0) {
                    val[idx] = value;
                    rowIdx[idx] = i;
                    colIdx[idx++] = j;
                }
            }
        }

        return new COOMatrix(row, ncol, val, rowIdx, colIdx);
    }

    public float[] multiply(float[] vector) {
        if (ncol != vector.length) {
            throw new IllegalArgumentException("Can not multipy matrixes [col != row].");
        }

        float[] ret = new float[nrow];
        for (int i = 0; i < values.length; i++) {
            ret[rowIdx[i]] += values[i] * vector[colIdx[i]];
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
