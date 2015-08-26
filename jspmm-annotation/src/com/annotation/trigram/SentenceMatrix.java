/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.annotation.trigram;

import com.jspmm.util.CRSStreamMatrix;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

/**
 * Sentences - 3gram matrix will be streamed out as CRS matrix format
 *
 * @author cplonka
 */
class SentenceMatrix {

    final Object2IntOpenHashMap<String> gramIdx;

    public SentenceMatrix(Object2IntOpenHashMap<String> gramIdx) {
        this.gramIdx = gramIdx;
    }

    public void generate(String input, String output) throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(input))) {
            //stream out as ccs format
            try (CRSStreamMatrix out = new CRSStreamMatrix(output)) {
                //start with 0
                out.writeRowPtr(0);
                //
                int nnz = 0, nrow = 0;
                String line;
                while ((line = reader.readLine()) != null) {
                    String prepared = preProcess(line);
                    //start and end spaces
                    for (int i = 0, size = prepared.length() - 2; i < size; i++) {
                        String gram = prepared.substring(i, i + 3);
                        int colIdx = gramIdx.getInt(gram);
                        if (colIdx == -1) {
                            //trigram not in index
                            continue;
                        }
                        //trigram found, mark
                        out.writeValue(1);
                        out.writeColIdx(colIdx);
                        nnz++;
                    }
                    out.writeRowPtr(nnz);
                    nrow++;
                }
                //dont forget to specify, before close
                out.nrow = nrow;
                out.ncol = gramIdx.size();
            }
        }
    }

    protected String preProcess(String line) {
        line = line.trim().toLowerCase().replace(",", " ");
        return " " + line.replaceAll(" +", " ") + " ";
    }
}
