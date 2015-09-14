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
package com.jspmm.cl;

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

    static final CLSpMM clspmm = CLSpMM.create();

    @BeforeClass
    public static void setUpClass() {
        mat0 = CCSMatrix.create(Data.m0, 5);
        mat1 = CCSMatrix.create(Data.m1, 3);
        mat2 = CCSMatrix.create(Data.m2, 5);
    }

    @Test
    public void testSpMv_OCL_m0() {
        assertArrayEquals(clspmm.multiply(mat0, Data.v0), Data.p0, 0);
    }

    @Test
    public void testSpMv_OCL_m1() {
        assertArrayEquals(clspmm.multiply(mat1, Data.v1), Data.p1, 0);
    }

    @Test
    public void testSpMv_OCL_m2() {
        assertArrayEquals(clspmm.multiply(mat2, Data.v2), Data.p2, 0);
    }
}
