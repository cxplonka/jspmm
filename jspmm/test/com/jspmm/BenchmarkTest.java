/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jspmm;

import com.jspmm.matrix.DenseFloatMatrix;
import com.jspmm.cl.CL;
import com.nativelibs4java.opencl.CLContext;
import com.nativelibs4java.opencl.JavaCL;
import java.io.IOException;

/**
 *
 * @author cplonka
 */
public class BenchmarkTest {

    static final int SIZE = 4096; //must be 3.6s(extern) (AMD 290X) for 5000    

    static final void openCLTest0() {
        //on nvidia faster
        DenseFloatMatrix m0 = new DenseFloatMatrix(SIZE, SIZE);
        DenseFloatMatrix m1 = new DenseFloatMatrix(SIZE, SIZE);

        CLContext ctx = JavaCL.createBestContext();
        System.out.println(ctx);
        DenseFloatMatrix ret = CL.create(ctx).multiply(m0, m1);
    }

    static final void javaTest0() {
        DenseFloatMatrix m0 = new DenseFloatMatrix(SIZE, SIZE);
        DenseFloatMatrix m1 = new DenseFloatMatrix(SIZE, SIZE);
        DenseFloatMatrix ret = m0.multiply(m1);
    }

    public static void main(String[] arg) throws IOException {
        long memory = SIZE * SIZE * 3 * 32l;
        System.out.println(memory / 8 / 1024 + "Kb");

        System.out.println("test 0 - my opencl");
        long t0 = System.currentTimeMillis();
        openCLTest0();
        long t1 = System.currentTimeMillis();
        System.out.println("time in ms: " + (t1 - t0));

        double gflops = 2e-6 * Math.pow(SIZE, 3) / (t1 - t0);
        System.out.println("gflops/s: " + gflops);

        System.out.println("test 2 - java single core");
        t0 = System.currentTimeMillis();
        javaTest0();
        t1 = System.currentTimeMillis();
        System.out.println("time in ms: " + (t1 - t0));
    }
}
