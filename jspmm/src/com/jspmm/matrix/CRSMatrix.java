/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jspmm.matrix;

/**
 * CRS (Compressed Row Storage) Matrix
 *
 * @author cplonka
 */
public class CRSMatrix extends AbstractMatrix {

    public final float[] values;
    public final int[] colIdx;
    public final int[] rowPtr;

    public CRSMatrix(int nrow, int ncol, float[] values, int[] colIdx, int[] rowPtr) {
        super(nrow, ncol);
        this.values = values;
        this.colIdx = colIdx;
        this.rowPtr = rowPtr;
    }

    /**
     * create an CRS from an dense matrix
     *
     * @param values
     * @param ncol
     * @return
     */
    public static CRSMatrix create(float[] values, int ncol) {
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
        int[] colIdx = new int[nz];
        int[] rowPtr = new int[nrow + 1];
        //scan row major
        for (int i = 0, nnz = 0; i < nrow; i++) {
            for (int j = 0; j < ncol; j++) {
                float value = values[i * ncol + j];
                if (value != 0) {
                    val[nnz] = value;
                    //col idx for this values
                    colIdx[nnz++] = j;
                }
            }
            rowPtr[i + 1] = nnz;
        }

        return new CRSMatrix(nrow, ncol, val, colIdx, rowPtr);
    }

    public float[] multiply(float[] vector) {
        if (ncol != vector.length) {
            throw new IllegalArgumentException("Can not multipy matrixes [col != row].");
        }

        float[] ret = new float[nrow];
        for (int i = 0; i < ret.length; i++) {
            for (int j = rowPtr[i]; j < rowPtr[i + 1]; j++) {
                ret[i] += values[j] * vector[colIdx[j]];
            }
        }
        return ret;
    }

    public <T extends AbstractMatrix> T multiply(CCSMatrix m, Class<T> resultBuffer) {
        if (ncol != m.nrow) {
            throw new IllegalArgumentException("Can not multipy matrixes [col != row].");
        }

        T ret = null;
        try {
            ret = resultBuffer.getConstructor(new Class[]{
                int.class, int.class}).newInstance(nrow, m.ncol);
        } catch (Exception ex) {
            throw new UnsupportedOperationException("Matrix type dont supported.");
        }

        for (int rowA = 0; rowA < nrow; rowA++) { //each row in A
            for (int colB = 0; colB < m.ncol; colB++) { //each column in B
                float value = 0;
                //each nonzero in A row
                for (int i = rowPtr[rowA]; i < rowPtr[rowA + 1]; i++) {
                    //each nonzero in B column
                    for (int j = m.colPtr[colB]; j < m.colPtr[colB + 1]; j++) {
                        if (colIdx[i] == m.rowIdx[j]) {
                            value += values[i] * m.values[j];
                            break;
                        }
                    }
                }
                //add nonzero calculated values
                if (value != 0) {
                    ret.set(rowA, colB, value);
                }
            }
        }

        return ret;
    }

    public DenseFloatMatrix multiply(CCSMatrix m) {
        return multiply(m, DenseFloatMatrix.class);
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