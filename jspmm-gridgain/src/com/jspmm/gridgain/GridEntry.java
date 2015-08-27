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
package com.jspmm.gridgain;

import org.gridgain.grid.Grid;
import org.gridgain.grid.GridConfiguration;
import org.gridgain.grid.GridException;
import org.gridgain.grid.GridGain;
import org.gridgain.grid.marshaller.optimized.GridOptimizedMarshaller;

/**
 *
 * @author Christian Plonka (cplonka81@gmail.com)
 */
public final class GridEntry {

    private static final String GRID = "compute-grid-jspmm";
    private static GridEntry instance;
    private final Grid grid;

    private GridEntry() throws GridException {
        grid = GridGain.start(createCfg());
    }

    /**
     * start grid node locally
     *
     * @return
     * @throws GridException
     */
    public static GridEntry start() throws GridException {
        if (instance == null) {
            instance = new GridEntry();
        }
        return instance;
    }

    public static void stop() throws GridException {
        if (instance != null) {
            //stop this grid instance
            instance.grid.close();
            GridGain.stop(GRID, false);
        }
    }

    static GridConfiguration createCfg() {
        //default GridTcpDiscoverySPI with multicast finder
        GridConfiguration cfg = new GridConfiguration();
//        cfg.setPeerClassLoadingEnabled(true);
        //allow objects with no serializable
        GridOptimizedMarshaller marshaller = new GridOptimizedMarshaller();
        marshaller.setRequireSerializable(false);
        cfg.setMarshaller(marshaller);
        cfg.setGridName(GRID);
        return cfg;
    }

    public Grid getGrid() {
        return grid;
    }

    public static void main(String[] arg) throws GridException {
        GridGain.start(createCfg());
    }
}
