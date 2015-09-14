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
package com.jspmm.cl;

/**
 *
 * @author Christian Plonka (cplonka81@gmail.com)
 */
public class Data {

    //test 0 - (5x5)*(5x1)
    static final float[] m0 = new float[]{
        0, 0, 3, 1, 0,
        2, 0, 0, 4, 0,
        0, 0, 2, 0, 0,
        0, 3, 0, 1, 0,
        4, 0, 2, 0, 3
    };
    static final float[] v0 = new float[]{
        1,
        3,
        0,
        1,
        2
    };
    static final float[] p0 = new float[]{
        1,
        6,
        0,
        10,
        10
    };
    //test 1 - (5x3)*(3x1)
    static final float[] m1 = new float[]{
        0, 0, 3,
        2, 0, 0,
        0, 0, 2,
        0, 3, 0,
        4, 0, 2
    };
    static final float[] v1 = new float[]{
        1,
        3,
        0
    };
    static final float[] p1 = new float[]{
        0,
        2,
        0,
        9,
        4
    };
    //test 2 - (3x5)*(5x1)
    static final float[] m2 = new float[]{
        0, 0, 3, 1, 0,
        2, 0, 0, 4, 0,
        0, 0, 2, 0, 0
    };
    static final float[] v2 = new float[]{
        1,
        3,
        0,
        1,
        2
    };
    static final float[] p2 = new float[]{
        1,
        6,
        0
    };
    //m0xm1
    static final float[] p_m0_m1 = new float[]{
        0, 3, 6,
        0, 12, 6,
        0, 0, 4,
        6, 3, 0,
        12, 0, 22
    };
    //m1xm2
    static final float[] p_m1_m2 = new float[]{
        0, 0, 6, 0, 0,
        0, 0, 6, 2, 0,
        0, 0, 4, 0, 0,
        6, 0, 0, 12, 0,
        0, 0, 16, 4, 0
    };
}
