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
import com.jspmm.util.Range;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;

/**
 *
 * @author Christian Plonka (cplonka81@gmail.com)
 */
class CRSCCSMapper implements MapReduce<CRSMatrix, CCSMatrix, MutableCOOMatrix> {

    int nrow;
    int ncol;

    @Override
    public List<Callable<MutableCOOMatrix>> map(CRSMatrix a, CCSMatrix b, int nodes) {
        List<Callable<MutableCOOMatrix>> tasks = new ArrayList<>();
        this.nrow = a.nrow;
        this.ncol = b.ncol;
        // create tasks
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
