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

        for (i = 0; i < p; i++) { //i - row
            for (j = 0; j < r; j++, value = 0) { //j - col
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
