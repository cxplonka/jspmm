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

import com.jspmm.matrix.StaticCOOMatrix;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author Christian Plonka (cplonka81@gmail.com)
 */
public class COOMatrixTest {

    static StaticCOOMatrix mat0;
    static final float[] m0_values = new float[]{3, 1, 2, 4, 2, 3, 1, 4, 2, 3};
    static final int[] m0_rowIdx = new int[]{0, 0, 1, 1, 2, 3, 3, 4, 4, 4};
    static final int[] m0_colIdx = new int[]{2, 3, 0, 3, 2, 1, 3, 0, 2, 4};
    //
    static StaticCOOMatrix mat1;
    static final float[] m1_values = new float[]{3, 2, 2, 3, 4, 2};
    static final int[] m1_rowIdx = new int[]{0, 1, 2, 3, 4, 4};
    static final int[] m1_colIdx = new int[]{2, 0, 2, 1, 0, 2};
    //
    static StaticCOOMatrix mat2;
    static final float[] m2_values = new float[]{3, 1, 2, 4, 2};
    static final int[] m2_rowIdx = new int[]{0, 0, 1, 1, 2};
    static final int[] m2_colIdx = new int[]{2, 3, 0, 3, 2};

    @BeforeClass
    public static void setUpClass() {
        mat0 = StaticCOOMatrix.create(Data.m0, 5);
        mat1 = StaticCOOMatrix.create(Data.m1, 3);
        mat2 = StaticCOOMatrix.create(Data.m2, 5);
    }

    @Test
    public void testCreate_m0() {
        assertEquals(mat0.nrow, 5);
        assertEquals(mat0.ncol, 5);
        assertArrayEquals(mat0.rowIdx, m0_rowIdx);
        assertArrayEquals(mat0.colIdx, m0_colIdx);
        assertArrayEquals(mat0.values, m0_values, 0);
    }

    @Test
    public void testCreate_m1() {
        assertEquals(mat1.nrow, 5);
        assertEquals(mat1.ncol, 3);
        assertArrayEquals(mat1.rowIdx, m1_rowIdx);
        assertArrayEquals(mat1.colIdx, m1_colIdx);
        assertArrayEquals(mat1.values, m1_values, 0);
    }

    @Test
    public void testCreate_m2() {
        assertEquals(mat2.nrow, 3);
        assertEquals(mat2.ncol, 5);
        assertArrayEquals(mat2.rowIdx, m2_rowIdx);
        assertArrayEquals(mat2.colIdx, m2_colIdx);
        assertArrayEquals(mat2.values, m2_values, 0);
    }

    @Test
    public void testSpMv_CPU_m0() {
        assertArrayEquals(mat0.multiply(Data.v0), Data.p0, 0);
    }

    @Test
    public void testSpMv_CPU_m1() {
        assertArrayEquals(mat1.multiply(Data.v1), Data.p1, 0);
    }

    @Test
    public void testSpMv_CPU_m2() {
        assertArrayEquals(mat2.multiply(Data.v2), Data.p2, 0);
    }
}