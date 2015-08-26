/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jspmm;

import com.jspmm.cl.CL;
import com.jspmm.concurrent.SpMM;
import com.jspmm.matrix.CCSMatrix;
import com.jspmm.matrix.COOMatrix;
import com.jspmm.matrix.CRSMatrix;
import com.jspmm.matrix.MutableCOOMatrix;
import org.gridgain.grid.GridException;
import org.junit.AfterClass;
import static org.junit.Assert.*;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author cplonka
 */
public class SpMM_CRS_CCSTest {

    static CRSMatrix mat0;
    static CRSMatrix mat1;
    static CRSMatrix mat2;
    static CL context;
    static SpMM service;

    @BeforeClass
    public static void setUpClass() {
        context = CL.create();
        service = SpMM.create();
        mat0 = CRSMatrix.create(Data.m0, 5);
        mat1 = CRSMatrix.create(Data.m1, 3);
        mat2 = CRSMatrix.create(Data.m2, 5);
    }

    @AfterClass
    public static void tearDownClass() throws GridException {
        service.shutdown();
    }

    @Test
    public void testSpMv_CPU_m0() {
        assertArrayEquals(mat0.multiply(CCSMatrix.create(Data.v0, 1)).values, Data.p0, 0);
    }

    @Test
    public void testSpMv_CPU_m1() {
        assertArrayEquals(mat1.multiply(CCSMatrix.create(Data.v1, 1)).values, Data.p1, 0);
    }

    @Test
    public void testSpMv_CPU_m2() {
        assertArrayEquals(mat2.multiply(CCSMatrix.create(Data.v2, 1)).values, Data.p2, 0);
    }

    @Test
    public void testSpMM_CPU_m0() {
        assertArrayEquals(mat0.multiply(CCSMatrix.create(Data.m1, 3)).values, Data.p_m0_m1, 0);
    }

    @Test
    public void testSpMM_CPU_m1() {
        assertArrayEquals(mat1.multiply(CCSMatrix.create(Data.m2, 5)).values, Data.p_m1_m2, 0);
    }

    @Test
    public void testSpMM_OCL_m0() {
        COOMatrix m = context.multiply(mat0, CCSMatrix.create(Data.m1, 3));
        for (int i = 0; i < m.values.length; i++) {
            int r = m.rowIdx[i];
            int c = m.colIdx[i];
            int idx = r * m.ncol + c;
            assertEquals(m.values[i], Data.p_m0_m1[idx], 0);
        }
    }

    @Test
    public void testSpMM_OCL_m1() {
        COOMatrix m = context.multiply(mat1, CCSMatrix.create(Data.m2, 5));
        for (int i = 0; i < m.values.length; i++) {
            int r = m.rowIdx[i];
            int c = m.colIdx[i];
            int idx = r * m.ncol + c;
            assertEquals(m.values[i], Data.p_m1_m2[idx], 0);
        }
    }

    @Test
    public void testSpMM_Parallel_m0() throws Exception {
        MutableCOOMatrix m = service.multiply(mat0, CCSMatrix.create(Data.m1, 3));
        for (int i = 0; i < Data.p_m0_m1.length; i++) {
            int r = i / m.ncol;
            int c = i % m.ncol;
            assertEquals(m.get(r, c), Data.p_m0_m1[i], 0);
        }
    }

    @Test
    public void testSpMM_Parallel_m1() throws Exception {
        MutableCOOMatrix m = service.multiply(mat1, CCSMatrix.create(Data.m2, 5));
        for (int i = 0; i < Data.p_m1_m2.length; i++) {
            int r = i / m.ncol;
            int c = i % m.ncol;
            assertEquals(m.get(r, c), Data.p_m1_m2[i], 0);
        }
    }
}
