/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jspmm.concurrent;

import com.jspmm.matrix.CCSMatrix;
import com.jspmm.matrix.CRSMatrix;
import com.jspmm.matrix.MutableCOOMatrix;
import com.jspmm.util.Range;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;

/**
 *
 * @author cplonka
 */
class CRSCCSMapper implements MapReduce<CRSMatrix, CCSMatrix, MutableCOOMatrix> {

    int nrow;
    int ncol;

    @Override
    public List<Callable<MutableCOOMatrix>> map(CRSMatrix a, CCSMatrix b, int nodes) {
        List<Callable<MutableCOOMatrix>> tasks = new ArrayList<>();
        this.nrow = a.nrow;
        this.ncol = b.ncol;
        //create tasks
        if (nodes > 0) {
            for (Range ir : Range.subRange(a.nrow, nodes)) {
                for (Range jr : Range.subRange(b.ncol, nodes)) {
                    tasks.add(new CRSCCSTask(ir, jr, a, b));
                }
            }
        }
        return tasks;
    }

    @Override
    public MutableCOOMatrix reduce(List<Future<MutableCOOMatrix>> tasks) {
        MutableCOOMatrix m = new MutableCOOMatrix(nrow, ncol);
        for (Future<MutableCOOMatrix> future : tasks) {
            try {
                m.merge(future.get());
            } catch (Exception ex) {
                throw new RuntimeException("Can not unmap results.", ex);
            }
        }
        return m;
    }
}
