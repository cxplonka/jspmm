/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jspmm;

import com.jspmm.matrix.COOMatrix;
import com.jspmm.cl.CL;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author cplonka
 */
public class COOMatrixTest {

    static COOMatrix mat0;
    static final float[] m0_values = new float[]{3, 1, 2, 4, 2, 3, 1, 4, 2, 3};
    static final int[] m0_rowIdx = new int[]{0, 0, 1, 1, 2, 3, 3, 4, 4, 4};
    static final int[] m0_colIdx = new int[]{2, 3, 0, 3, 2, 1, 3, 0, 2, 4};
    //
    static COOMatrix mat1;
    static final float[] m1_values = new float[]{3, 2, 2, 3, 4, 2};
    static final int[] m1_rowIdx = new int[]{0, 1, 2, 3, 4, 4};
    static final int[] m1_colIdx = new int[]{2, 0, 2, 1, 0, 2};
    //
    static COOMatrix mat2;
    static final float[] m2_values = new float[]{3, 1, 2, 4, 2};
    static final int[] m2_rowIdx = new int[]{0, 0, 1, 1, 2};
    static final int[] m2_colIdx = new int[]{2, 3, 0, 3, 2};
    static CL context;

    @BeforeClass
    public static void setUpClass() {
        context = CL.create();
        mat0 = COOMatrix.create(Data.m0, 5);
        mat1 = COOMatrix.create(Data.m1, 3);
        mat2 = COOMatrix.create(Data.m2, 5);
    }

    @Test
    public void testCreate_m0() {
        assertEquals(mat0.nrow, 5);
        assertEquals(mat0.ncol, 5);
        assertArrayEquals(mat0.rowIdx, m0_rowIdx);
        assertArrayEquals(mat0.colIdx, m0_colIdx);
        assertArrayEquals(mat0.values, m0_values, 0);
    }

    @Test
    public void testCreate_m1() {
        assertEquals(mat1.nrow, 5);
        assertEquals(mat1.ncol, 3);
        assertArrayEquals(mat1.rowIdx, m1_rowIdx);
        assertArrayEquals(mat1.colIdx, m1_colIdx);
        assertArrayEquals(mat1.values, m1_values, 0);
    }

    @Test
    public void testCreate_m2() {
        assertEquals(mat2.nrow, 3);
        assertEquals(mat2.ncol, 5);
        assertArrayEquals(mat2.rowIdx, m2_rowIdx);
        assertArrayEquals(mat2.colIdx, m2_colIdx);
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
