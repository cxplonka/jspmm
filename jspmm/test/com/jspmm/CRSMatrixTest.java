/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jspmm;

import com.jspmm.matrix.CRSMatrix;
import com.jspmm.cl.CL;
import java.io.File;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.BeforeClass;

/**
 *
 * @author cplonka
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
    static CL context;
    static File tmpDir = new File(System.getProperty("java.io.tmpdir"));

    @BeforeClass
    public static void setUpClass() {
        context = CL.create();
        mat0 = CRSMatrix.create(Data.m0, 5);
        mat1 = CRSMatrix.create(Data.m1, 3);
        mat2 = CRSMatrix.create(Data.m2, 5);
    }

    @Test
    public void testCreate_m0() {
        assertEquals(mat0.nrow, 5);
        assertEquals(mat0.ncol, 5);
        assertArrayEquals(mat0.colIdx, m0_colIdx);
        assertArrayEquals(mat0.rowPtr, m0_rowPtr);
        assertArrayEquals(mat0.values, m0_values, 0);
    }

    @Test
    public void testCreate_m1() {
        assertEquals(mat1.nrow, 5);
        assertEquals(mat1.ncol, 3);
        assertArrayEquals(mat1.colIdx, m1_colIdx);
        assertArrayEquals(mat1.rowPtr, m1_rowPtr);
        assertArrayEquals(mat1.values, m1_values, 0);
    }

    @Test
    public void testCreate_m2() {
        assertEquals(mat2.nrow, 3);
        assertEquals(mat2.ncol, 5);
        assertArrayEquals(mat2.colIdx, m2_colIdx);
        assertArrayEquals(mat2.rowPtr, m2_rowPtr);
        assertArrayEquals(mat2.values, m2_values, 0);
    }

    @Test
    public void testSpMv_CPU_m0() {
        assertArrayEquals(mat0.multiply(Data.v0), Data.p0, 0);
    }

    @Test
    public void testSpMv_OCL_m0() {
        assertArrayEquals(context.multiply(mat0, Data.v0), Data.p0, 0);
    }

    @Test
    public void testSpMv_CPU_m1() {
        assertArrayEquals(mat1.multiply(Data.v1), Data.p1, 0);
    }

    @Test
    public void testSpMv_OCL_m1() {
        assertArrayEquals(context.multiply(mat1, Data.v1), Data.p1, 0);
    }

    @Test
    public void testSpMv_CPU_m2() {
        assertArrayEquals(mat2.multiply(Data.v2), Data.p2, 0);
    }

    @Test
    public void testSpMv_OCL_m2() {
        assertArrayEquals(context.multiply(mat2, Data.v2), Data.p2, 0);
    }    
}