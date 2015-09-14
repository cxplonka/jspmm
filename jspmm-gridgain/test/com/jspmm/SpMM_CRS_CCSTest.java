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

import com.jspmm.concurrent.ExecutorSpMM;
import com.jspmm.gridgain.GridEntry;
import com.jspmm.matrix.AbstractMatrix;
import com.jspmm.matrix.CCSMatrix;
import com.jspmm.matrix.CRSMatrix;
import com.jspmm.matrix.MutableCOOMatrix;
import org.gridgain.grid.GridException;
import org.junit.AfterClass;
import static org.junit.Assert.assertEquals;
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

    static SpMM spmm;

    @BeforeClass
    public static void setUpClass() throws GridException {
        spmm = ExecutorSpMM.create(GridEntry.start().getGrid().compute().executorService());

        mat0 = CRSMatrix.create(Data.m0, 5);
        mat1 = CRSMatrix.create(Data.m1, 3);
        mat2 = CRSMatrix.create(Data.m2, 5);
    }

    @AfterClass
    public static void tearDownClass() throws GridException {
        GridEntry.stop();
    }

    @Test
    public void testSpMM_Parallel_m0() throws Exception {
        AbstractMatrix m = spmm.multiply(mat0, CCSMatrix.create(Data.m1, 3), MutableCOOMatrix.class);
        for (int i = 0; i < Data.p_m0_m1.length; i++) {
            int r = i / m.ncol;
            int c = i % m.ncol;
            assertEquals(m.get(r, c), Data.p_m0_m1[i], 0);
        }
    }

    @Test
    public void testSpMM_Parallel_m1() throws Exception {
        AbstractMatrix m = spmm.multiply(mat1, CCSMatrix.create(Data.m2, 5), MutableCOOMatrix.class);
        for (int i = 0; i < Data.p_m1_m2.length; i++) {
            int r = i / m.ncol;
            int c = i % m.ncol;
            assertEquals(m.get(r, c), Data.p_m1_m2[i], 0);
        }
    }
}
