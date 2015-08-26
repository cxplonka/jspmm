/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jspmm.concurrent;

import com.jspmm.matrix.AbstractMatrix;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;

/**
 *
 * @author cplonka
 * @param <T0>
 * @param <T1>
 * @param <R>
 */
public interface MapReduce<T0 extends AbstractMatrix, T1 extends AbstractMatrix, R extends AbstractMatrix> {

    public List<Callable<R>> map(T0 a, T1 b, int nodes);

    public R reduce(List<Future<R>> tasks);
}
