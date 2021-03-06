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

import com.jspmm.matrix.CCSMatrix;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.BeforeClass;

/**
 *
 * @author Christian Plonka (cplonka81@gmail.com)
 */
public class CCSMatrixTest {

    static CCSMatrix mat0;
    static final float[] m0_values = new float[]{2, 4, 3, 3, 2, 2, 1, 4, 1, 3};
    static final int[] m0_rowIdx = new int[]{1, 4, 3, 0, 2, 4, 0, 1, 3, 4};
    static final int[] m0_colPtr = new int[]{0, 2, 3, 6, 9, 10};
    //
    static CCSMatrix mat1;
    static final float[] m1_values = new float[]{2, 4, 3, 3, 2, 2};
    static final int[] m1_rowIdx = new int[]{1, 4, 3, 0, 2, 4};
    static final int[] m1_colPtr = new int[]{0, 2, 3, 6};
    //
    static CCSMatrix mat2;
    static final float[] m2_values = new float[]{2, 3, 2, 1, 4};
    static final int[] m2_rowIdx = new int[]{1, 0, 2, 0, 1};
    static final int[] m2_colPtr = new int[]{0, 1, 1, 3, 5, 5};

    @BeforeClass
    public static void setUpClass() {
        mat0 = CCSMatrix.create(Data.m0, 5);
        mat1 = CCSMatrix.create(Data.m1, 3);
        mat2 = CCSMatrix.create(Data.m2, 5);
    }

    @Test
    public void testCreate_m0() {
        assertEquals(mat0.nrow, 5);
        assertEquals(mat0.ncol, 5);
        assertArrayEquals(mat0.rowIdx, m0_rowIdx);
        assertArrayEquals(mat0.colPtr, m0_colPtr);
        assertArrayEquals(mat0.values, m0_values, 0);
    }

    @Test
    public void testCreate_m1() {
        assertEquals(mat1.nrow, 5);
        assertEquals(mat1.ncol, 3);
        assertArrayEquals(mat1.rowIdx, m1_rowIdx);
        assertArrayEquals(mat1.colPtr, m1_colPtr);
        assertArrayEquals(mat1.values, m1_values, 0);
    }

    @Test
    public void testCreate_m2() {
        assertEquals(mat2.nrow, 3);
        assertEquals(mat2.ncol, 5);
        assertArrayEquals(mat2.rowIdx, m2_rowIdx);
        assertArrayEquals(mat2.colPtr, m2_colPtr);
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