/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jspmm.cl;

import com.nativelibs4java.opencl.CLBuffer;
import com.nativelibs4java.opencl.CLContext;
import com.nativelibs4java.opencl.CLKernel;
import com.nativelibs4java.opencl.CLMem;
import com.nativelibs4java.opencl.CLProgram;
import com.nativelibs4java.opencl.CLQueue;
import java.io.IOException;
import java.io.InputStream;
import com.jspmm.matrix.CRSMatrix;
import com.jspmm.util.Util;
import org.bridj.Pointer;

/**
 *
 * @author cplonka
 */
final class CRS_SPMV_Kernel {

    static final int LOCAL_SIZE = 16;
    static String source = null;

    static {
        InputStream stream = null;
        try {
            //read kernel source
            source = Util.readContent(stream = CL.class.getResourceAsStream(
                    "/com/jspmm/cl/crs_spmv.cl"));
        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            Util.quiteClose(stream);
        }
    }

    final CLContext context;
    private CLKernel kernel;

    public CRS_SPMV_Kernel(CLContext context) {
        this.context = context;
    }

    public float[] multiply(CRSMatrix m0, float[] v) {
        //crs matrix A
        Pointer<Float> ptr_m0 = Pointer.allocateFloats(m0.values.length).order(context.getByteOrder());
        ptr_m0.setFloats(m0.values);
        //dense vector v
        Pointer<Float> ptr_m1 = Pointer.allocateFloats(v.length).order(context.getByteOrder());
        ptr_m1.setFloats(v);
        //product y
        Pointer<Float> ptr_ret = Pointer.allocateFloats(m0.nrow).order(context.getByteOrder());
        //column index
        Pointer<Integer> ptr_colIdx = Pointer.allocateInts(m0.colIdx.length).order(context.getByteOrder());
        ptr_colIdx.setInts(m0.colIdx);
        //row pointer
        Pointer<Integer> ptr_rowPtr = Pointer.allocateInts(m0.rowPtr.length).order(context.getByteOrder());
        ptr_rowPtr.setInts(m0.rowPtr);
        //bind buffers
        CLBuffer<Float> cl_m0 = context.createFloatBuffer(CLMem.Usage.Input, ptr_m0, false);
        CLBuffer<Float> cl_m1 = context.createFloatBuffer(CLMem.Usage.Input, ptr_m1, false);
        CLBuffer<Float> cl_ret = context.createFloatBuffer(CLMem.Usage.Output, ptr_ret, false);
        CLBuffer<Integer> cl_colIdx = context.createIntBuffer(CLMem.Usage.Input, ptr_colIdx, false);
        CLBuffer<Integer> cl_rowPtr = context.createIntBuffer(CLMem.Usage.Input, ptr_rowPtr, false);

        CLQueue queue = context.createDefaultQueue();
        try {
            floatMult(cl_m0, cl_m1, cl_ret, cl_colIdx, cl_rowPtr, m0.nrow, queue);
            //
            cl_ret.read(queue, ptr_ret, true);
        } catch (Exception e) {
            throw e;
        } finally {
            cl_m0.release();
            cl_m1.release();
            cl_ret.release();
            cl_colIdx.release();
            cl_rowPtr.release();
            queue.release();
        }
        return ptr_ret.getFloats();
    }

    void floatMult(CLBuffer<Float> m0, CLBuffer<Float> m1, CLBuffer<Float> result,
            CLBuffer<Integer> colIdx, CLBuffer<Integer> rowPtr, int p, CLQueue queue) {
        if (kernel == null) {
            CLProgram clprogram = context.createProgram(source);
            kernel = clprogram.createKernel("crs_spmv_mult");
        }
        kernel.setArgs(result, m0, m1, colIdx, rowPtr, p);

        /* global worksize must be a multiple of local work size */
        int global0 = p + (LOCAL_SIZE - (p & (LOCAL_SIZE - 1)));

        int[] local = new int[]{LOCAL_SIZE};
        int[] global = new int[]{global0};

        kernel.enqueueNDRange(queue, global, local);
        /* wait for complete the kernel */
        queue.finish();
    }
}
