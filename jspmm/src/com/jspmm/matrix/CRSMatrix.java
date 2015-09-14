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
 * CRS (Compressed Row Storage) Matrix
 *
 * @author Christian Plonka (cplonka81@gmail.com)
 */
public class CRSMatrix extends Matrix {

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
     * Create an CRS from an dense matrix/vector
     *
     * @param values
     * @param ncol
     * @return
     */
    public static CRSMatrix create(float[] values, int ncol) {
        int nz = 0;
        int nrow = values.length / ncol;
        // count nonzero elements
        for (float value : values) {
            if (value != 0) {
                nz++;
            }
        }
        //
        float[] val = new float[nz];
        int[] colIdx = new int[nz];
        int[] rowPtr = new int[nrow + 1];
        // scan row major
        for (int i = 0, nnz = 0; i < nrow; i++) {
            for (int j = 0; j < ncol; j++) {
                float value = values[i * ncol + j];
                if (value != 0) {
                    val[nnz] = value;
                    // col idx for this values
                    colIdx[nnz++] = j;
                }
            }
            rowPtr[i + 1] = nnz;
        }

        return new CRSMatrix(nrow, ncol, val, colIdx, rowPtr);
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
