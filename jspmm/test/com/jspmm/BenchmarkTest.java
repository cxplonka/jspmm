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

//import com.jspmm.cl.CL;
//import com.nativelibs4java.opencl.CLContext;
//import com.nativelibs4java.opencl.JavaCL;
import java.io.IOException;
import org.junit.Test;

/**
 *
 * @author Christian Plonka (cplonka81@gmail.com)
 */
public class BenchmarkTest {

    static final int SIZE = 1096; //must be 3.6s(extern) (AMD 290X) for 5000    

//    static final void openCLTest0() {
//        //on nvidia faster
//        DenseFloatMatrix m0 = new DenseFloatMatrix(SIZE, SIZE);
//        DenseFloatMatrix m1 = new DenseFloatMatrix(SIZE, SIZE);
//
//        CLContext ctx = JavaCL.createBestContext();
//        System.out.println(ctx);
//        DenseFloatMatrix ret = CL.create(ctx).multiply(m0, m1);
//    }

    @Test
    public void javaTest0() {
//        DenseFloatMatrix m0 = new DenseFloatMatrix(SIZE, SIZE);
//        DenseFloatMatrix m1 = new DenseFloatMatrix(SIZE, SIZE);
//        DenseFloatMatrix ret = m0.stream(m1, DenseFloatMatrix.class);
    }

    public static void main(String[] arg) throws IOException {
        long memory = SIZE * SIZE * 3 * 32l;
        System.out.println(memory / 8 / 1024 + "Kb");

        System.out.println("test 0 - my opencl");
        long t0 = System.currentTimeMillis();
//        openCLTest0();
        long t1 = System.currentTimeMillis();
        System.out.println("time in ms: " + (t1 - t0));

        double gflops = 2e-6 * Math.pow(SIZE, 3) / (t1 - t0);
        System.out.println("gflops/s: " + gflops);

        System.out.println("test 2 - java single core");
        t0 = System.currentTimeMillis();
//        javaTest0();
        t1 = System.currentTimeMillis();
        System.out.println("time in ms: " + (t1 - t0));
    }
}
