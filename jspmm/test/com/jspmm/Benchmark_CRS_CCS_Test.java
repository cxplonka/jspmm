/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jspmm;

import com.jspmm.cl.CL;
import com.jspmm.concurrent.SpMM;
import com.jspmm.matrix.AbstractMatrix;
import com.jspmm.matrix.CCSMatrix;
import com.jspmm.matrix.CRSMatrix;
import com.jspmm.util.CCSStreamMatrix;
import com.jspmm.util.CRSStreamMatrix;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author cplonka
 */
public class Benchmark_CRS_CCS_Test {

    static CRSMatrix m0;
    static CCSMatrix m1;
    static CL context;
    static SpMM service;

    @BeforeClass
    public static void setUpClass() {
        context = CL.create();
        service = SpMM.create();
        m0 = CRSStreamMatrix.readCRSMatrix("d:/crs_sentences.mat");
        m1 = CCSStreamMatrix.readCCSMatrix("d:/ccs_term.mat");
    }

    @Test
    public void testSpMM_m1() {
        long t0 = System.currentTimeMillis();
        AbstractMatrix m = context.multiply(m0, m1);
        long t1 = System.currentTimeMillis();
        System.out.println((t1 - t0) + "ms");
    }

    public static void main(String[] arg) {
//        Util.createCRSMatrix("d:/crs_sentences.mat", 100, 19000, 0.03);
//        Util.createCCSMatrix("d:/ccs_term.mat", 19000, 218754, 0.00125);
    }
}
