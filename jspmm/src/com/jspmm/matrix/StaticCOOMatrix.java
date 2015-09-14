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
 * Coordinate Storage Format (COO)
 *
 * @author Christian Plonka (cplonka81@gmail.com)
 */
public class StaticCOOMatrix extends AbstractMatrix {

    public final float[] values;
    public final int[] rowIdx;
    public final int[] colIdx;

    public StaticCOOMatrix(int nrow, int ncol, float[] values, int[] rowIdx, int[] colIdx) {
        super(nrow, ncol);
        this.values = values;
        this.rowIdx = rowIdx;
        this.colIdx = colIdx;
    }

    /**
     * Create an COO from an dense matrix
     *
     * @param values
     * @param ncol
     * @return
     */
    public static StaticCOOMatrix create(float[] values, int ncol) {
        int nz = 0;
        int row = values.length / ncol;
        // count nonzero elements
        for (float value : values) {
            if (value != 0) {
                nz++;
            }
        }
        //
        float[] val = new float[nz];
        int[] rowIdx = new int[nz];
        int[] colIdx = new int[nz];
        // scan column major
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

        return new StaticCOOMatrix(row, ncol, val, rowIdx, colIdx);
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
        // find row and col
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void set(int i, int j, float value) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
