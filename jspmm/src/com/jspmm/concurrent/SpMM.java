/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
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
 * @author cplonka
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
