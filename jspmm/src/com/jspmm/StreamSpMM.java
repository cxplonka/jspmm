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
package com.jspmm;

import com.jspmm.matrix.Matrix;
import com.jspmm.matrix.CCSMatrix;
import com.jspmm.matrix.CRSMatrix;
import java.util.stream.IntStream;

/**
 *
 * @author Christian Plonka (cplonka81@gmail.com)
 */
public class StreamSpMM implements SpMM {

    @Override
    public <T extends Matrix> T multiply(CRSMatrix m0, CCSMatrix m1, Class<T> result) {
        if (m0.ncol != m1.nrow) {
            throw new IllegalArgumentException("Can not multipy matrixes [col != row].");
        }

        try {
            final T ret = result.getConstructor(new Class[]{
                int.class, int.class}).newInstance(m0.nrow, m1.ncol);

            IntStream.range(0, ret.nrow * ret.ncol).parallel().forEach(id -> {
                int rowA = id / ret.ncol;
                int colB = id % ret.ncol;

                float sum = 0;
                // each nonzero in A row
                for (int i = m0.rowPtr[rowA]; i < m0.rowPtr[rowA + 1]; i++) {
                    // each nonzero in B column
                    for (int j = m1.colPtr[colB]; j < m1.colPtr[colB + 1]; j++) {
                        if (m0.colIdx[i] == m1.rowIdx[j]) {
                            sum += m0.values[i] * m1.values[j];
                            break;
                        }
                    }
                }
                // add nonzero calculated values
                if (sum != 0) {
                    ret.set(rowA, colB, sum);
                }
            });

            return ret;
        } catch (Exception ex) {
            throw new UnsupportedOperationException("Matrix type dont supported.", ex);
        }
    }

    @Override
    public <T extends Matrix> T multiply(Matrix m0, Matrix m1, Class<T> result) {
        if (m0.ncol != m1.nrow) {
            throw new IllegalArgumentException("Can not multipy matrixes [col != row].");
        }
        try {
            final T ret = result.getConstructor(new Class[]{
                int.class, int.class}).newInstance(m0.nrow, m1.ncol);

            IntStream.range(0, ret.nrow * ret.ncol).parallel().forEach(id -> {
                int i = id / ret.ncol;
                int j = id % ret.ncol;

                float sum = 0;
                for (int k = 0; k < m0.ncol; k++) {
                    sum += m0.get(i, k) * m1.get(k, j);
                }
                ret.set(i, j, sum);
            });
            return ret;
        } catch (Exception ex) {
            throw new UnsupportedOperationException("Matrix type dont supported.", ex);
        }
    }

    public float[] multiply(CRSMatrix m0, float[] vector) {
        if (m0.ncol != vector.length) {
            throw new IllegalArgumentException("Can not multipy matrixes [col != row].");
        }

        float[] ret = new float[m0.nrow];
        IntStream.range(0, ret.length).parallel().forEach(id -> {
            for (int j = m0.rowPtr[id]; j < m0.rowPtr[id + 1]; j++) {
                ret[id] += m0.values[j] * vector[m0.colIdx[j]];
            }
        });

        return ret;
    }
}
