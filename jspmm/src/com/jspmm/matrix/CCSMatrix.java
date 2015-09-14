/* 
 * The MIT License
 *
 * Copyright 2015 Christian Plonka.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package com.jspmm.matrix;

/**
 *
 * Compressed Column Storage (CCS/CSC) Matrix
 *
 * @author Christian Plonka (cplonka81@gmail.com)
 */
public class CCSMatrix extends Matrix {

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
