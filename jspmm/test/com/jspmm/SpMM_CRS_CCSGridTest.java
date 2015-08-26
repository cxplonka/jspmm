/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jspmm;

import com.jspmm.concurrent.SpMM;
import com.jspmm.gridgain.GridEntry;
import com.jspmm.matrix.CCSMatrix;
import com.jspmm.matrix.CRSMatrix;
import com.jspmm.matrix.MutableCOOMatrix;
import org.gridgain.grid.GridException;
import org.junit.AfterClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.BeforeClass;

/**
 *
 * @author cplonka
 */
public class SpMM_CRS_CCSGridTest {

    static CRSMatrix mat0;
    static CRSMatrix mat1;
    static CRSMatrix mat2;
    static GridEntry grid;
    static SpMM spmm;

    @BeforeClass
    public static void setUpClass() throws GridException {
        grid = GridEntry.start();
        spmm = SpMM.create(grid.getGrid().compute().executorService());
        mat0 = CRSMatrix.create(Data.m0, 5);
        mat1 = CRSMatrix.create(Data.m1, 3);
    }

    @AfterClass
    public static void tearDownClass() throws GridException {        
        spmm.shutdown();
        GridEntry.stop();
    }

    @Test
    public void testSpMM_Grid_m0() throws Exception {
        MutableCOOMatrix m = spmm.multiply(mat0, CCSMatrix.create(Data.m1, 3));
        for (int i = 0; i < Data.p_m0_m1.length; i++) {
            int r = i / m.ncol;
            int c = i % m.ncol;
            assertEquals(m.get(r, c), Data.p_m0_m1[i], 0);
        }
    }

    @Test
    public void testSpMM_Grid_m1() throws Exception {
        MutableCOOMatrix m = spmm.multiply(mat1, CCSMatrix.create(Data.m2, 5));
        for (int i = 0; i < Data.p_m1_m2.length; i++) {
            int r = i / m.ncol;
            int c = i % m.ncol;
            assertEquals(m.get(r, c), Data.p_m1_m2[i], 0);
        }
    }
}
