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
import com.jspmm.matrix.DenseFloatMatrix;
import static org.junit.Assert.assertArrayEquals;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author Christian Plonka (cplonka81@gmail.com)
 */
public class DenseMatrixTest {

    static DenseFloatMatrix mat0;
    static DenseFloatMatrix mat1;
    static DenseFloatMatrix mat2;

    static final SpMM spmm = new SingleSpMM();

    @BeforeClass
    public static void setUpClass() {
        mat0 = DenseFloatMatrix.create(Data.m0, 5);
        mat1 = DenseFloatMatrix.create(Data.m1, 3);
        mat2 = DenseFloatMatrix.create(Data.m2, 5);
    }

    @Test
    public void testSpMv_OCL_m0() {
        assertArrayEquals(
                spmm.multiply(mat0, DenseFloatMatrix.create(Data.v0, 1), DenseFloatMatrix.class).values,
                Data.p0,
                0);
    }

    @Test
    public void testSpMv_OCL_m1() {
        assertArrayEquals(
                spmm.multiply(mat1, DenseFloatMatrix.create(Data.v1, 1), DenseFloatMatrix.class).values,
                Data.p1,
                0);
    }

    @Test
    public void testSpMv_OCL_m2() {
        assertArrayEquals(
                spmm.multiply(mat2, DenseFloatMatrix.create(Data.v2, 1), DenseFloatMatrix.class).values,
                Data.p2,
                0);
    }

    @Test
    public void testSpMM_OCL_m0() {
        assertArrayEquals(
                spmm.multiply(mat0, mat1, DenseFloatMatrix.class).values,
                Data.p_m0_m1,
                0);
    }

    @Test
    public void testSpMM_OCL_m1() {
        assertArrayEquals(
                spmm.multiply(mat1, mat2, DenseFloatMatrix.class).values,
                Data.p_m1_m2,
                0);
    }
}
