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

import com.jspmm.matrix.Matrix;
import com.jspmm.matrix.CCSMatrix;
import com.jspmm.matrix.CRSMatrix;

/**
 *
 * Matrix definition (SPMM):
 * 
 *        C     =     A     x     B
 *      -----       -----       -----
 *        r           q           r
 *      *****       *****       *****
 *    p *****     p *****     q *****
 *      *****       *****       ***** 
 * 
 * Matrix definition (SPMV):
 * 
 *        y     =     A     x     x
 *      -----       -----       -----
 *       r(1)         q          r(1)
 *        *         *****         *
 *      p *       p *****       q *
 *        *         *****         *
 * 
 * Matrix is saved in Row-Major.
 *
 * @author Christian Plonka (cplonka81@gmail.com)
 */
public interface SpMM {

    public <T extends Matrix> T multiply(CRSMatrix m0, CCSMatrix m1, Class<T> result);

    public <T extends Matrix> T multiply(Matrix m0, Matrix m1, Class<T> result);
}
