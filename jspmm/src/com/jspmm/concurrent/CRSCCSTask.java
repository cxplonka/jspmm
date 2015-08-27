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
import java.util.concurrent.Callable;

/**
 * https://www.cs.fsu.edu/research/projects/rose_report.pdf
 *
 * @author Christian Plonka (cplonka81@gmail.com)
 */
class CRSCCSTask implements Callable<MutableCOOMatrix> {

    final Range rowRange;
    final Range colRange;
    final CRSMatrix a;
    final CCSMatrix b;

    public CRSCCSTask(Range rowRange, Range colRange, CRSMatrix a, CCSMatrix b) {
        this.rowRange = rowRange;
        this.colRange = colRange;
        this.a = a;
        this.b = b;
    }

    @Override
    public MutableCOOMatrix call() throws Exception {        
        MutableCOOMatrix ret = new MutableCOOMatrix(a.nrow, b.ncol);
        int rows = (int) rowRange.getLowerBound();
        int rowe = (int) rowRange.getUpperBound();
        int cols = (int) colRange.getLowerBound();
        int cole = (int) colRange.getUpperBound();
        //
        for (int rowA = rows; rowA < rowe; rowA++) { //each row in A
            for (int colB = cols; colB < cole; colB++) { //each column in B
                float value = 0;
                //each nonzero in A row
                for (int i = a.rowPtr[rowA]; i < a.rowPtr[rowA + 1]; i++) {
                    //each nonzero in B column
                    for (int j = b.colPtr[colB]; j < b.colPtr[colB + 1]; j++) {
                        if (a.colIdx[i] == b.rowIdx[j]) {
                            value += a.values[i] * b.values[j];
                            break;
                        }
                    }
                }
                //add nonzero calculated values
                if (value != 0) {
                    ret.set(rowA, colB, value);
                }
            }
        }
        return ret;
    }
}
