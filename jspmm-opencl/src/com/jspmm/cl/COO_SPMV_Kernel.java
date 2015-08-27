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

import com.nativelibs4java.opencl.CLBuffer;
import com.nativelibs4java.opencl.CLContext;
import com.nativelibs4java.opencl.CLKernel;
import com.nativelibs4java.opencl.CLMem;
import com.nativelibs4java.opencl.CLProgram;
import com.nativelibs4java.opencl.CLQueue;
import java.io.IOException;
import java.io.InputStream;
import com.jspmm.matrix.COOMatrix;
import com.jspmm.util.Util;
import org.bridj.Pointer;

/**
 *
 * @author Christian Plonka (cplonka81@gmail.com)
 */
final class COO_SPMV_Kernel {

    static final int LOCAL_SIZE = 16;
    static String source = null;

    static {
        InputStream stream = null;
        try {
            //read kernel source
            source = Util.readContent(stream = CL.class.getResourceAsStream(
                    "/com/jspmm/cl/coo_spmv.cl"));
        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            Util.quiteClose(stream);
        }
    }

    final CLContext context;
    private CLKernel kernel;

    public COO_SPMV_Kernel(CLContext context) {
        this.context = context;
    }

    public float[] multiply(COOMatrix m0, float[] v) {
        //crs matrix A
        Pointer<Float> ptr_m0 = Pointer.allocateFloats(m0.values.length).order(context.getByteOrder());
        ptr_m0.setFloats(m0.values);
        //dense vector v
        Pointer<Float> ptr_m1 = Pointer.allocateFloats(v.length).order(context.getByteOrder());
        ptr_m1.setFloats(v);
        //product y
        Pointer<Float> ptr_ret = Pointer.allocateFloats(m0.nrow).order(context.getByteOrder());
        //column index
        Pointer<Integer> ptr_rowIdx = Pointer.allocateInts(m0.rowIdx.length).order(context.getByteOrder());
        ptr_rowIdx.setInts(m0.rowIdx);
        //row pointer
        Pointer<Integer> ptr_colIdx = Pointer.allocateInts(m0.colIdx.length).order(context.getByteOrder());
        ptr_colIdx.setInts(m0.colIdx);
        //bind buffers
        CLBuffer<Float> cl_m0 = context.createFloatBuffer(CLMem.Usage.Input, ptr_m0, false);
        CLBuffer<Float> cl_m1 = context.createFloatBuffer(CLMem.Usage.Input, ptr_m1, false);
        CLBuffer<Float> cl_ret = context.createFloatBuffer(CLMem.Usage.Output, ptr_ret, false);
        CLBuffer<Integer> cl_rowIdx = context.createIntBuffer(CLMem.Usage.Input, ptr_rowIdx, false);
        CLBuffer<Integer> cl_colIdx = context.createIntBuffer(CLMem.Usage.Input, ptr_colIdx, false);

        CLQueue queue = context.createDefaultQueue();
        try {
            floatMult(cl_m0, cl_m1, cl_ret, cl_rowIdx, cl_colIdx, m0.values.length, queue);
            //
            cl_ret.read(queue, ptr_ret, true);
        } catch (Exception e) {
            throw e;
        } finally {
            cl_m0.release();
            cl_m1.release();
            cl_ret.release();
            cl_rowIdx.release();
            cl_colIdx.release();
            queue.release();
        }
        return ptr_ret.getFloats();
    }

    void floatMult(CLBuffer<Float> m0, CLBuffer<Float> m1, CLBuffer<Float> result,
            CLBuffer<Integer> rowIdx, CLBuffer<Integer> colIdx, int nz, CLQueue queue) {
        if (kernel == null) {
            CLProgram clprogram = context.createProgram(source);
            kernel = clprogram.createKernel("coo_spmv_mult");
        }
        kernel.setArgs(result, m0, m1, rowIdx, colIdx, nz);

        /* global worksize must be a multiple of local work size */
        int global0 = nz + (LOCAL_SIZE - (nz & (LOCAL_SIZE - 1)));

        int[] local = new int[]{LOCAL_SIZE};
        int[] global = new int[]{global0};

        kernel.enqueueNDRange(queue, global, local);
        /* wait for complete the kernel */
        queue.finish();
    }
}
