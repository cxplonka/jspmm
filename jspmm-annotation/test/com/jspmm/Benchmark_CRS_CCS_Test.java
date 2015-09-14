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

import com.jspmm.matrix.AbstractMatrix;
import com.jspmm.matrix.CCSMatrix;
import com.jspmm.matrix.CRSMatrix;
import com.jspmm.matrix.MutableCOOMatrix;
import com.jspmm.util.CCSStreamMatrix;
import com.jspmm.util.CRSStreamMatrix;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author Christian Plonka (cplonka81@gmail.com)
 */
public class Benchmark_CRS_CCS_Test {

    static CRSMatrix m0;
    static CCSMatrix m1;

    static SpMM service;

    @BeforeClass
    public static void setUpClass() {
        service = new StreamSpMM();

        m0 = CRSStreamMatrix.readCRSMatrix("d:/crs_sentences.mat");
        m1 = CCSStreamMatrix.readCCSMatrix("d:/ccs_term.mat");
    }

    @Test
    public void testSpMM_m1() {
        long t0 = System.currentTimeMillis();
        AbstractMatrix m = service.multiply(m0, m1, MutableCOOMatrix.class);
        long t1 = System.currentTimeMillis();
        System.out.println((t1 - t0) + "ms");
    }

    public static void main(String[] arg) {
//        Util.createCRSMatrix("d:/crs_sentences.mat", 100, 19000, 0.03);
//        Util.createCCSMatrix("d:/ccs_term.mat", 19000, 218754, 0.00125);
    }
}
