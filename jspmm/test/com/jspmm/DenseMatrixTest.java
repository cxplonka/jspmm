/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jspmm;

import com.jspmm.matrix.DenseFloatMatrix;
import com.jspmm.cl.CL;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author cplonka
 */
public class DenseMatrixTest {

    static DenseFloatMatrix mat0;
    static DenseFloatMatrix mat1;
    static DenseFloatMatrix mat2;
    static CL context;

    @BeforeClass
    public static void setUpClass() {
        context = CL.create();
        mat0 = DenseFloatMatrix.create(Data.m0, 5);
        mat1 = DenseFloatMatrix.create(Data.m1, 3);
        mat2 = DenseFloatMatrix.create(Data.m2, 5);
    }

    @Test
    public void testCreate_m0() {
        assertEquals(mat0.nrow, 5);
        assertEquals(mat0.ncol, 5);
        assertArrayEquals(mat0.values, Data.m0, 0);
    }

    @Test
    public void testCreate_m1() {
        assertEquals(mat1.nrow, 5);
        assertEquals(mat1.ncol, 3);
        assertArrayEquals(mat1.values, Data.m1, 0);
    }

    @Test
    public void testCreate_m2() {
        assertEquals(mat2.nrow, 3);
        assertEquals(mat2.ncol, 5);
        assertArrayEquals(mat2.values, Data.m2, 0);
    }

    @Test
    public void testSpMv_CPU_m0() {
        assertArrayEquals(mat0.multiply(Data.v0), Data.p0, 0);
    }

    @Test
    public void testSpMv_OCL_m0() {
        assertArrayEquals(context.multiply(mat0,
                DenseFloatMatrix.create(Data.v0, 1)).values, Data.p0, 0);
    }

    @Test
    public void testSpMv_CPU_m1() {
        assertArrayEquals(mat1.multiply(Data.v1), Data.p1, 0);
    }

    @Test
    public void testSpMv_OCL_m1() {
        assertArrayEquals(context.multiply(mat1,
                DenseFloatMatrix.create(Data.v1, 1)).values, Data.p1, 0);
    }

    @Test
    public void testSpMv_CPU_m2() {
        assertArrayEquals(mat2.multiply(Data.v2), Data.p2, 0);
    }

    @Test
    public void testSpMv_OCL_m2() {
        assertArrayEquals(context.multiply(mat2,
                DenseFloatMatrix.create(Data.v2, 1)).values, Data.p2, 0);
    }

    @Test
    public void testSpMM_CPU_m0() {
        assertArrayEquals(mat0.multiply(mat1).values, Data.p_m0_m1, 0);
    }

    @Test
    public void testSpMM_OCL_m0() {
        assertArrayEquals(context.multiply(mat0, mat1).values, Data.p_m0_m1, 0);
    }
    
    @Test
    public void testSpMM_CPU_m1() {
        assertArrayEquals(mat1.multiply(mat2).values, Data.p_m1_m2, 0);
    }

    @Test
    public void testSpMM_OCL_m1() {
        assertArrayEquals(context.multiply(mat1, mat2).values, Data.p_m1_m2, 0);
    }
}
