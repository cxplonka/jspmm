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
package com.jspmm.concurrent;

import com.jspmm.matrix.CCSMatrix;
import com.jspmm.matrix.CRSMatrix;
import com.jspmm.matrix.MutableCOOMatrix;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.logging.Logger;

/**
 *
 * @author Christian Plonka (cplonka81@gmail.com)
 */
public final class SpMM {

    public static final Logger log = Logger.getLogger(SpMM.class.getName());

    private static final int nThreads = Runtime.getRuntime().availableProcessors();
    private final ExecutorService context;
    private static ExecutorService defaultContext;
    private int taskSplitSize = nThreads;

    public static SpMM create() {
        if (defaultContext == null) {
            defaultContext = Executors.newFixedThreadPool(nThreads);
        }
        return new SpMM(defaultContext);
    }

    public static SpMM create(ExecutorService context) {
        return new SpMM(context);
    }

    private SpMM(ExecutorService context) {
        this.context = context;
    }

    public MutableCOOMatrix multiply(CRSMatrix m0, CCSMatrix m1) {
        try {
            MapReduce<CRSMatrix, CCSMatrix, MutableCOOMatrix> mapper
                    = CRSCCSMapper.class.newInstance();
            //execute tasks on grid
            List<Future<MutableCOOMatrix>> ret = context.invokeAll(mapper.map(m0, m1, taskSplitSize));
            log.info(String.format("Split CRSxCCS SpMM into [%s] subtasks.", ret.size()));
            //merge result together and wait for completion
            MutableCOOMatrix result = mapper.reduce(ret);
            log.info(String.format("Finished calculation of A[%sx%s] * B[%sx%s].",
                    m0.nrow, m0.ncol, m1.nrow, m1.ncol));
            return result;
        } catch (Exception ex) {
            throw new RuntimeException("can not calculate m0xm1.", ex);
        }
    }

    public void setTaskSplitSize(int taskSplitSize) {
        this.taskSplitSize = taskSplitSize;
    }

    public int getTaskSplitSize() {
        return taskSplitSize;
    }

    public void shutdown() {
        context.shutdown();
    }
}
