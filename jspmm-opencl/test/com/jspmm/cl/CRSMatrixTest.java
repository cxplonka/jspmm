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

import com.jspmm.SingleSpMM;
import com.jspmm.SpMM;
import com.jspmm.matrix.CCSMatrix;
import com.jspmm.matrix.CRSMatrix;
import com.jspmm.matrix.DenseFloatMatrix;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.BeforeClass;

/**
 *
 * @author Christian Plonka (cplonka81@gmail.com)
 */
public class CRSMatrixTest {

    static CRSMatrix mat0;
    static final float[] m0_values = new float[]{3, 1, 2, 4, 2, 3, 1, 4, 2, 3};
    static final int[] m0_colIdx = new int[]{2, 3, 0, 3, 2, 1, 3, 0, 2, 4};
    static final int[] m0_rowPtr = new int[]{0, 2, 4, 5, 7, 10};
    //
    static CRSMatrix mat1;
    static final float[] m1_values = new float[]{3, 2, 2, 3, 4, 2};
    static final int[] m1_colIdx = new int[]{2, 0, 2, 1, 0, 2};
    static final int[] m1_rowPtr = new int[]{0, 1, 2, 3, 4, 6};
    //
    static CRSMatrix mat2;
    static final float[] m2_values = new float[]{3, 1, 2, 4, 2};
    static final int[] m2_colIdx = new int[]{2, 3, 0, 3, 2};
    static final int[] m2_rowPtr = new int[]{0, 2, 4, 5};

    static final SpMM spmm = new SingleSpMM();

    @BeforeClass
    public static void setUpClass() {
        mat0 = CRSMatrix.create(Data.m0, 5);
        mat1 = CRSMatrix.create(Data.m1, 3);
        mat2 = CRSMatrix.create(Data.m2, 5);
    }

    @Test
    public void testSpMv_OCL_m0() {
        assertArrayEquals(
                spmm.multiply(mat0, CCSMatrix.create(Data.v0, 1), DenseFloatMatrix.class).values,
                Data.p0,
                0);
    }

    @Test
    public void testSpMv_OCL_m1() {
        assertArrayEquals(
                spmm.multiply(mat1, CCSMatrix.create(Data.v1, 1), DenseFloatMatrix.class).values,
                Data.p1,
                0);
    }

    @Test
    public void testSpMv_OCL_m2() {
        assertArrayEquals(
                spmm.multiply(mat2, CCSMatrix.create(Data.v2, 1), DenseFloatMatrix.class).values,
                Data.p2,
                0);
    }
}
