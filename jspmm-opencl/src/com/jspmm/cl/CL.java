/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
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
 * @author cplonka
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