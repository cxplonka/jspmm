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
import java.util.concurrent.Callable;

/**
 * https://www.cs.fsu.edu/research/projects/rose_report.pdf
 *
 * @author cplonka
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
