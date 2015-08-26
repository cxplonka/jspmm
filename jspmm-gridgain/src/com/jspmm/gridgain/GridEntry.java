/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jspmm.gridgain;

import org.gridgain.grid.Grid;
import org.gridgain.grid.GridConfiguration;
import org.gridgain.grid.GridException;
import org.gridgain.grid.GridGain;
import org.gridgain.grid.marshaller.optimized.GridOptimizedMarshaller;

/**
 *
 * @author cplonka
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
