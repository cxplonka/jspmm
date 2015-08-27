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
 * @author Christian Plonka (cplonka81@gmail.com)
 */
public class DenseFloatMatrix extends AbstractMatrix {

    public final float[] values;

    public DenseFloatMatrix(int nrow, int ncol) {
        this(nrow, ncol, new float[nrow * ncol]);
    }

    public DenseFloatMatrix(int nrow, int ncol, float[] values) {
        super(nrow, ncol);
        this.values = values;
    }

    public static DenseFloatMatrix create(float[] values, int col) {
        return new DenseFloatMatrix(values.length / col, col, values);
    }

    public float[] multiply(float[] vector) {
        return multiply(create(vector, 1)).values;
    }

    @Override
    public float get(int i, int j) {
        return values[i * ncol + j];
    }

    @Override
    public void set(int i, int j, float value) {
        values[i * ncol + j] = value;
    }

    public <T extends AbstractMatrix> T multiply(AbstractMatrix m, Class<T> resultBuffer) {
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

        int p = nrow;
        int r = m.ncol;
        int q = ncol;
        int i, j, k;
        float value = 0;

        for (i = 0; i < p; i++) { // i - row
            for (j = 0; j < r; j++, value = 0) { // j - col
                for (k = 0; k < q; k++) {
                    value += get(i, k) * m.get(k, j);
                }
                ret.set(i, j, value);
            }
        }

        return ret;
    }

    public DenseFloatMatrix multiply(AbstractMatrix m) {
        return multiply(m, DenseFloatMatrix.class);
    }
}
