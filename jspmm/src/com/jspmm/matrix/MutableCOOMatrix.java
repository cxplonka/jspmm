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

import it.unimi.dsi.fastutil.longs.Long2FloatOpenHashMap;

/**
 *
 * @author Christian Plonka (cplonka81@gmail.com)
 */
public class MutableCOOMatrix extends Matrix {

    private final Long2FloatOpenHashMap _backBuffer = new Long2FloatOpenHashMap();

    public MutableCOOMatrix(int nrow, int ncol) {
        super(nrow, ncol);
    }

    public static MutableCOOMatrix create(float[] values, int ncol) {
        int nrow = values.length / ncol;
        MutableCOOMatrix ret = new MutableCOOMatrix(nrow, ncol);
        // scan column major
        for (int i = 0; i < nrow; i++) {
            for (int j = 0; j < ncol; j++) {
                float value = values[i * ncol + j];
                if (value != 0) {
                    ret.set(i, j, value);
                }
            }
        }
        return ret;
    }

    @Override
    public float get(int i, int j) {
        return _backBuffer.get(pack(i, j));
    }

    @Override
    public void set(int i, int j, float value) {
        // keep update dimensions
        nrow = Math.max(i, nrow);
        ncol = Math.max(j, ncol);
        _backBuffer.put(pack(i, j), value);
    }

    public int getNnz() {
        return _backBuffer.size();
    }

    public void merge(MutableCOOMatrix m) {
        // keep update dimensions
        nrow = Math.max(m.nrow, nrow);
        ncol = Math.max(m.ncol, ncol);
        _backBuffer.putAll(m._backBuffer);
    }

    static long pack(int x, int y) {
        long xPacked = ((long) x) << 32;
        long yPacked = y & 0xFFFFFFFFL;
        return xPacked | yPacked;
    }
}
