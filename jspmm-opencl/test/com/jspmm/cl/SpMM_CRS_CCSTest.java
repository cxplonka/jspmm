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

import com.jspmm.SpMM;
import com.jspmm.matrix.CCSMatrix;
import com.jspmm.matrix.CRSMatrix;
import com.jspmm.matrix.StaticCOOMatrix;
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

    static final SpMM spmm = CLSpMM.create();

    @BeforeClass
    public static void setUpClass() {
        mat0 = CRSMatrix.create(Data.m0, 5);
        mat1 = CRSMatrix.create(Data.m1, 3);
        mat2 = CRSMatrix.create(Data.m2, 5);
    }

    @Test
    public void testSpMM_OCL_m0() {
        StaticCOOMatrix m = spmm.multiply(mat0, CCSMatrix.create(Data.m1, 3), StaticCOOMatrix.class);
        for (int i = 0; i < m.values.length; i++) {
            int r = m.rowIdx[i];
            int c = m.colIdx[i];
            int idx = r * m.ncol + c;
            assertEquals(m.values[i], Data.p_m0_m1[idx], 0);
        }
    }

    @Test
    public void testSpMM_OCL_m1() {
        StaticCOOMatrix m = spmm.multiply(mat1, CCSMatrix.create(Data.m2, 5), StaticCOOMatrix.class);
        for (int i = 0; i < m.values.length; i++) {
            int r = m.rowIdx[i];
            int c = m.colIdx[i];
            int idx = r * m.ncol + c;
            assertEquals(m.values[i], Data.p_m1_m2[idx], 0);
        }
    }
}
