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
import com.jspmm.matrix.CRSMatrix;
import com.jspmm.matrix.DenseFloatMatrix;
import static org.junit.Assert.*;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
  * @author Christian Plonka (cplonka81@gmail.com)
 */
public class SpMM_CRS_CCSTest {

    static CRSMatrix mat0;
    static CRSMatrix mat1;
    static CRSMatrix mat2;
    
    static final SpMM cpu = new SingleSpMM();
    static final SpMM stream = new StreamSpMM();

    @BeforeClass
    public static void setUpClass() {
        mat0 = CRSMatrix.create(Data.m0, 5);
        mat1 = CRSMatrix.create(Data.m1, 3);
        mat2 = CRSMatrix.create(Data.m2, 5);
    }

    @Test
    public void testSpMv_CPU_m0() {
        assertArrayEquals(
                cpu.multiply(mat0, CCSMatrix.create(Data.v0, 1), DenseFloatMatrix.class).values,
                Data.p0,
                0);
    }

    @Test
    public void testSpMv_CPU_m1() {
        assertArrayEquals(
                cpu.multiply(mat1, CCSMatrix.create(Data.v1, 1), DenseFloatMatrix.class).values,
                Data.p1,
                0);
    }

    @Test
    public void testSpMv_CPU_m2() {
        assertArrayEquals(
                cpu.multiply(mat2, CCSMatrix.create(Data.v2, 1), DenseFloatMatrix.class).values,
                Data.p2,
                0);
    }

    @Test
    public void testSpMM_CPU_m0() {
        assertArrayEquals(
                cpu.multiply(mat0, CCSMatrix.create(Data.m1, 3), DenseFloatMatrix.class).values,
                Data.p_m0_m1,
                0);
    }

    @Test
    public void testSpMM_CPU_m1() {
        assertArrayEquals(
                cpu.multiply(mat1, CCSMatrix.create(Data.m2, 5), DenseFloatMatrix.class).values,
                Data.p_m1_m2,
                0);
    }

    @Test
    public void testSpMv_STREAM_m0() {
        assertArrayEquals(
                stream.multiply(mat0, CCSMatrix.create(Data.v0, 1), DenseFloatMatrix.class).values,
                Data.p0,
                0);
    }

    @Test
    public void testSpMv_STREAM_m1() {
        assertArrayEquals(
                stream.multiply(mat1, CCSMatrix.create(Data.v1, 1), DenseFloatMatrix.class).values,
                Data.p1,
                0);
    }

    @Test
    public void testSpMv_STREAM_m2() {
        assertArrayEquals(
                stream.multiply(mat2, CCSMatrix.create(Data.v2, 1), DenseFloatMatrix.class).values,
                Data.p2,
                0);
    }

    @Test
    public void testSpMM_STREAM_m0() {
        assertArrayEquals(
                stream.multiply(mat0, CCSMatrix.create(Data.m1, 3), DenseFloatMatrix.class).values,
                Data.p_m0_m1,
                0);
    }

    @Test
    public void testSpMM_STREAM_m1() {
        assertArrayEquals(
                stream.multiply(mat1, CCSMatrix.create(Data.m2, 5), DenseFloatMatrix.class).values,
                Data.p_m1_m2,
                0);
    }
}
