/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jspmm;

import com.jspmm.matrix.CCSMatrix;
import com.jspmm.cl.CL;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.BeforeClass;

/**
 *
 * @author cplonka
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
    static CL context;

    @BeforeClass
    public static void setUpClass() {
        context = CL.create();
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
