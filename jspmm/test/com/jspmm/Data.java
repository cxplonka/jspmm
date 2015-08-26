/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jspmm;

/**
 *
 * @author cplonka
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
