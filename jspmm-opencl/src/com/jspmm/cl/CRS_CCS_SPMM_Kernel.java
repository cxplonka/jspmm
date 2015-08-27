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

import com.jspmm.matrix.CCSMatrix;
import com.jspmm.matrix.COOMatrix;
import com.jspmm.matrix.CRSMatrix;
import com.jspmm.util.Util;
import com.nativelibs4java.opencl.CLBuffer;
import com.nativelibs4java.opencl.CLContext;
import com.nativelibs4java.opencl.CLKernel;
import com.nativelibs4java.opencl.CLMem;
import com.nativelibs4java.opencl.CLProgram;
import com.nativelibs4java.opencl.CLQueue;
import java.io.IOException;
import java.io.InputStream;
import org.bridj.Pointer;

/**
 *
 * @author Christian Plonka (cplonka81@gmail.com)
 */
final class CRS_CCS_SPMM_Kernel {

    static final int LOCAL_SIZE = 16;
    static String source = null;

    static {
        InputStream stream = null;
        try {
            //read kernel source
            source = Util.readContent(stream = CL.class.getResourceAsStream(
                    "/com/jspmm//cl/crs_ccs_spmm.cl"));
        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            Util.quiteClose(stream);
        }
    }

    final CLContext context;
    private CLKernel nnz_kernel;
    private CLKernel kernel;

    public CRS_CCS_SPMM_Kernel(CLContext context) {
        this.context = context;
    }

    public COOMatrix multiply(CRSMatrix m0, CCSMatrix m1) {
        //A
        Pointer<Float> ptr_m0 = Pointer.allocateFloats(m0.values.length).order(context.getByteOrder());
        ptr_m0.setFloats(m0.values);
        //B
        Pointer<Float> ptr_m1 = Pointer.allocateFloats(m1.values.length).order(context.getByteOrder());
        ptr_m1.setFloats(m1.values);

        //A matrix - column index
        Pointer<Integer> ptr_aColIdx = Pointer.allocateInts(m0.colIdx.length).order(context.getByteOrder());
        ptr_aColIdx.setInts(m0.colIdx);
        //A matrix - row pointer
        Pointer<Integer> ptr_aRowPtr = Pointer.allocateInts(m0.rowPtr.length).order(context.getByteOrder());
        ptr_aRowPtr.setInts(m0.rowPtr);

        //B matrix - row index
        Pointer<Integer> ptr_bRowIdx = Pointer.allocateInts(m1.rowIdx.length).order(context.getByteOrder());
        ptr_bRowIdx.setInts(m1.rowIdx);
        //B matrix - column pointer
        Pointer<Integer> ptr_bColPtr = Pointer.allocateInts(m1.colPtr.length).order(context.getByteOrder());
        ptr_bColPtr.setInts(m1.colPtr);

        //bind buffer
        CLBuffer<Float> cl_m0 = context.createFloatBuffer(CLMem.Usage.Input, ptr_m0, false);
        CLBuffer<Float> cl_m1 = context.createFloatBuffer(CLMem.Usage.Input, ptr_m1, false);
        CLBuffer<Integer> cl_aColIdx = context.createIntBuffer(CLMem.Usage.Input, ptr_aColIdx, false);
        CLBuffer<Integer> cl_aRowPtr = context.createIntBuffer(CLMem.Usage.Input, ptr_aRowPtr, false);
        CLBuffer<Integer> cl_bRowIdx = context.createIntBuffer(CLMem.Usage.Input, ptr_bRowIdx, false);
        CLBuffer<Integer> cl_bColPtr = context.createIntBuffer(CLMem.Usage.Input, ptr_bColPtr, false);

        CLQueue queue = context.createDefaultQueue();
        try {
            //first pass
            int nnz = crs_ccs_nnz(cl_m0, cl_m1, cl_aColIdx, cl_aRowPtr,
                    cl_bRowIdx, cl_bColPtr, m0.nrow, m1.ncol, queue);
            
            //C matrix - row index
            Pointer<Integer> ptr_cRowIdx = Pointer.allocateInts(nnz).order(context.getByteOrder());
            Pointer<Integer> ptr_cColIdx = Pointer.allocateInts(nnz).order(context.getByteOrder());
            Pointer<Float> ptr_cValues = Pointer.allocateFloats(nnz).order(context.getByteOrder());

            CLBuffer<Integer> cl_cRowIdx = context.createIntBuffer(CLMem.Usage.Output, ptr_cRowIdx, false);
            CLBuffer<Integer> cl_cColIdx = context.createIntBuffer(CLMem.Usage.Output, ptr_cColIdx, false);
            CLBuffer<Float> cl_cValues = context.createFloatBuffer(CLMem.Usage.Output, ptr_cValues, false);

            try {
                //second pass
                floatMult(cl_m0, cl_m1, cl_cValues, cl_aColIdx, cl_aRowPtr,
                        cl_bRowIdx, cl_bColPtr, cl_cRowIdx, cl_cColIdx, m0.nrow, m1.ncol, queue);

                //read back results
                cl_cColIdx.read(queue, ptr_cColIdx, true);
                cl_cRowIdx.read(queue, ptr_cRowIdx, true);
                cl_cValues.read(queue, ptr_cValues, true);
                
                return new COOMatrix(m0.nrow, m1.ncol,
                        ptr_cValues.getFloats(),
                        ptr_cRowIdx.getInts(),
                        ptr_cColIdx.getInts());
            } finally {
                cl_cRowIdx.release();
                cl_cColIdx.release();
                cl_cValues.release();
            }
        } catch (Exception e) {
            throw e;
        } finally {
            cl_m0.release();
            cl_m1.release();
            cl_aColIdx.release();
            cl_aRowPtr.release();
            cl_bRowIdx.release();
            cl_bColPtr.release();
            queue.release();
        }
    }

    void floatMult(CLBuffer<Float> m0, CLBuffer<Float> m1, CLBuffer<Float> mc,
            CLBuffer<Integer> cl_aColIdx, CLBuffer<Integer> cl_aRowPtr,
            CLBuffer<Integer> cl_bRowIdx, CLBuffer<Integer> cl_bColPtr,
            CLBuffer<Integer> cl_cRowIdx, CLBuffer<Integer> cl_cColIdx,
            int p, int r, CLQueue queue) {

        if (kernel == null) {
            CLProgram clprogram = context.createProgram(source);
            kernel = clprogram.createKernel("crs_ccs_spmm_mult");
        }

        Pointer<Integer> ptr_nnz = Pointer.allocateInt();
        CLBuffer<Integer> cl_nnz = context.createIntBuffer(CLMem.Usage.Output, ptr_nnz);

        kernel.setArgs(m0, m1, mc, cl_aColIdx, cl_aRowPtr, cl_bRowIdx,
                cl_bColPtr, cl_cRowIdx, cl_cColIdx, p, r, cl_nnz);

        /* global worksize must be a multiple of local work size */
        int global0 = p + (LOCAL_SIZE - (p & (LOCAL_SIZE - 1)));
        int global1 = r + (LOCAL_SIZE - (r & (LOCAL_SIZE - 1)));

        int[] local = new int[]{LOCAL_SIZE, LOCAL_SIZE};
        int[] global = new int[]{global0, global1};

        kernel.enqueueNDRange(queue, global, local);
        /* wait for complete the kernel */
        queue.finish();
    }

    int crs_ccs_nnz(CLBuffer<Float> m0, CLBuffer<Float> m1,
            CLBuffer<Integer> cl_aColIdx, CLBuffer<Integer> cl_aRowPtr,
            CLBuffer<Integer> cl_bRowIdx, CLBuffer<Integer> cl_bColPtr,
            int p, int r, CLQueue queue) {

        if (nnz_kernel == null) {
            CLProgram clprogram = context.createProgram(source);
            nnz_kernel = clprogram.createKernel("crs_ccs_spmm_nnz");
        }

        Pointer<Integer> ptr_nnz = Pointer.allocateInt();
        CLBuffer<Integer> cl_nnz = context.createIntBuffer(CLMem.Usage.Output, ptr_nnz);

        nnz_kernel.setArgs(m0, m1, cl_aColIdx, cl_aRowPtr, cl_bRowIdx,
                cl_bColPtr, p, r, cl_nnz);

        /* global worksize must be a multiple of local work size */
        int global0 = p + (LOCAL_SIZE - (p & (LOCAL_SIZE - 1)));
        int global1 = r + (LOCAL_SIZE - (r & (LOCAL_SIZE - 1)));

        int[] local = new int[]{LOCAL_SIZE, LOCAL_SIZE};
        int[] global = new int[]{global0, global1};

        nnz_kernel.enqueueNDRange(queue, global, local);
        /* wait for complete the kernel */
        queue.finish();
        //read nnz count                
        cl_nnz.read(queue, ptr_nnz, true);

        return ptr_nnz.get();
    }
}
