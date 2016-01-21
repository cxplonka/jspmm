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
package com.annotation.trigram;

import com.jspmm.util.CRSStreamMatrix;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

/**
 * Sentences - 3gram matrix will be streamed out as CRS matrix format
 *
 * @author Christian Plonka (cplonka81@gmail.com)
 */
class SentenceMatrix {

    final Object2IntOpenHashMap<String> gramIdx;

    public SentenceMatrix(Object2IntOpenHashMap<String> gramIdx) {
        this.gramIdx = gramIdx;
    }

    public void generate(String input, String output) throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(input))) {
            //stream out as crs format
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
