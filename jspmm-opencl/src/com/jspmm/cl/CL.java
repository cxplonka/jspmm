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

import com.jspmm.matrix.DenseFloatMatrix;
import com.nativelibs4java.opencl.CLContext;
import com.nativelibs4java.opencl.JavaCL;
import com.jspmm.matrix.CCSMatrix;
import com.jspmm.matrix.COOMatrix;
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
 * matrix is saved in Row-Major
 * 
 * @author Christian Plonka (cplonka81@gmail.com)
 */
public final class CL {
    
    private final CLContext context;
    private static CLContext defaultContext;

    public static CL create() {
        if (defaultContext == null) {
            defaultContext = JavaCL.createBestContext();
        }
        return new CL(defaultContext);
    }

    public static CL create(CLContext context) {
        return new CL(context);
    }

    private CL(CLContext context) {
        this.context = context;
    }

    public DenseFloatMatrix multiply(DenseFloatMatrix m0, DenseFloatMatrix m1) {
        if (m0.ncol != m1.nrow) {
            throw new IllegalArgumentException("Can not multipy matrixes [col != row].");
        }
        //
        Dense_SPMM_Kernel kernel = new Dense_SPMM_Kernel(context);
        return kernel.multiply(m0, m1);        
    }
    
    public float[] multiply(CRSMatrix m0, float[] v) {
        if (m0.ncol != v.length) {
            throw new IllegalArgumentException("Can not multipy y=Ax [col != row].");
        }
        //
        CRS_SPMV_Kernel kernel = new CRS_SPMV_Kernel(context);
        return kernel.multiply(m0, v);
    }
    
    public float[] multiply(COOMatrix m0, float[] v) {
        if (m0.ncol != v.length) {
            throw new IllegalArgumentException("Can not multipy y=Ax [col != row].");
        }
        //
        COO_SPMV_Kernel kernel = new COO_SPMV_Kernel(context);
        return kernel.multiply(m0, v);
    }
    
    public float[] multiply(CCSMatrix m0, float[] v) {
        if (m0.ncol != v.length) {
            throw new IllegalArgumentException("Can not multipy y=Ax [col != row].");
        }
        //
        CCS_SPMV_Kernel kernel = new CCS_SPMV_Kernel(context);        
        return kernel.multiply(m0, v);
    }
    
    public COOMatrix multiply(CRSMatrix m0, CCSMatrix m1) {
        if (m0.ncol != m1.nrow) {
            throw new IllegalArgumentException("Can not multipy matrixes [col != row].");
        }
        //
        CRS_CCS_SPMM_Kernel kernel = new CRS_CCS_SPMM_Kernel(context);
        return kernel.multiply(m0, m1);
    }
}