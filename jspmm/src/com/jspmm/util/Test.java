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
package com.jspmm.util;

import com.jspmm.StreamSpMM;
import com.jspmm.matrix.DenseFloatMatrix;
import java.io.IOException;

/**
 *
 * @author cplonka
 */
public class Test {

    static final int SIZE = 1096; //must be 3.6s(extern) (AMD 290X) for 5000    

    static final void javaTest0() {
        DenseFloatMatrix md0 = new DenseFloatMatrix(5, 3);
        DenseFloatMatrix md1 = new DenseFloatMatrix(3, 5);
        DenseFloatMatrix ret = new StreamSpMM().multiply(md0, md1, DenseFloatMatrix.class);
    }

    //test 0 - (5x5)*(5x1)
    static final float[] m0 = new float[]{
        0, 0, 3, 1, 0,
        2, 0, 0, 4, 0,
        0, 0, 2, 0, 0,
        0, 3, 0, 1, 0,
        4, 0, 2, 0, 3
    };
    //test 1 - (5x3)*(3x1)
    static final float[] m1 = new float[]{
        0, 0, 3,
        2, 0, 0,
        0, 0, 2,
        0, 3, 0,
        4, 0, 2
    };
    //m0xm1
    static final float[] p_m0_m1 = new float[]{
        0, 3, 6,
        0, 12, 6,
        0, 0, 4,
        6, 3, 0,
        12, 0, 22
    };

    static final void resultTest0() {
        DenseFloatMatrix md0 = DenseFloatMatrix.create(m0, 5);
        DenseFloatMatrix md1 = DenseFloatMatrix.create(m1, 3);
        DenseFloatMatrix ret = new StreamSpMM().multiply(md0, md1, DenseFloatMatrix.class);
        
        Util.print(ret);
    }

    public static void main(String[] arg) throws IOException {
        long memory = SIZE * SIZE * 3 * 32l;
        System.out.println(memory / 8 / 1024 + "Kb");

        resultTest0();

        System.out.println("test 2 - java single core");
        long t0 = System.currentTimeMillis();
        javaTest0();
        long t1 = System.currentTimeMillis();
        System.out.println("time in ms: " + (t1 - t0));
    }
}
