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
import com.jspmm.matrix.DenseFloatMatrix;
import com.jspmm.util.Util;
import org.bridj.Pointer;

/**
 *
 * @author Christian Plonka (cplonka81@gmail.com)
 */
final class Dense_SPMM_Kernel {

    static final int LOCAL_SIZE = 16;
    static String source = null;

    static {
        InputStream stream = null;
        try {
            //read kernel source
            source = Util.readContent(stream = CLSpMM.class.getResourceAsStream(
                    "/com/jspmm//cl/dense_spmm.cl"));
        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            Util.quiteClose(stream);
        }
    }

    final CLContext context;
    private CLKernel kernel;

    public Dense_SPMM_Kernel(CLContext context) {
        this.context = context;
    }

    public DenseFloatMatrix multiply(DenseFloatMatrix m0, DenseFloatMatrix m1) {        
        //A
        Pointer<Float> ptr_m0 = Pointer.allocateFloats(m0.values.length).order(context.getByteOrder());
        ptr_m0.setFloats(m0.values);
        //B
        Pointer<Float> ptr_m1 = Pointer.allocateFloats(m1.values.length).order(context.getByteOrder());
        ptr_m1.setFloats(m1.values);
        //C
        Pointer<Float> ptr_ret = Pointer.allocateFloats(m0.nrow * m1.ncol).order(context.getByteOrder());
        
        //bind buffer
        CLBuffer<Float> cl_m0 = context.createFloatBuffer(CLMem.Usage.Input, ptr_m0, false);
        CLBuffer<Float> cl_m1 = context.createFloatBuffer(CLMem.Usage.Input, ptr_m1, false);        
        CLBuffer<Float> cl_ret = context.createFloatBuffer(CLMem.Usage.Output, ptr_ret, false);

        CLQueue queue = context.createDefaultQueue();
        try {
            floatMult(cl_m0, cl_m1, cl_ret, m0.nrow, m1.ncol, m0.ncol, queue);
            //
            cl_ret.read(queue, ptr_ret, true);
        } catch (Exception e) {
            throw e;
        } finally {
            cl_m0.release();
            cl_m1.release();
            cl_ret.release();
            queue.release();
        }
        return new DenseFloatMatrix(m0.nrow, m1.ncol, ptr_ret.getFloats());
    }

    void floatMult(CLBuffer<Float> m0, CLBuffer<Float> m1, CLBuffer<Float> result, int p, int r, int q, CLQueue queue) {
        if (kernel == null) {
            CLProgram clprogram = context.createProgram(source);
            kernel = clprogram.createKernel("dense_spmm_mult");
        }
        kernel.setArgs(result, m0, m1, p, r, q);

        /* global worksize must be a multiple of local work size */
        int global0 = p + (LOCAL_SIZE - (p & (LOCAL_SIZE - 1)));
        int global1 = r + (LOCAL_SIZE - (r & (LOCAL_SIZE - 1)));

        int[] local = new int[]{LOCAL_SIZE, LOCAL_SIZE};
        int[] global = new int[]{global0, global1};

        kernel.enqueueNDRange(queue, global, local);
        /* wait for complete the kernel */
        queue.finish();
    }
}
