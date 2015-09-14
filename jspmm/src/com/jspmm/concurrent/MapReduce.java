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

import com.jspmm.matrix.Matrix;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;

/**
 *
 * @author Christian Plonka (cplonka81@gmail.com)
 * @param <T0>
 * @param <T1>
 * @param <R>
 */
public interface MapReduce<T0 extends Matrix, T1 extends Matrix, R extends Matrix> {

    public List<Callable<R>> map(T0 a, T1 b, int nodes);

    public R reduce(List<Future<R>> tasks);
}
