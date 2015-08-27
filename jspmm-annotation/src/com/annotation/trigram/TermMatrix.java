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

import com.jspmm.util.CCSStreamMatrix;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Term - 3gram matrix will be streamed out as CCS matrix format
 *
 * @author Christian Plonka (cplonka81@gmail.com)
 */
class TermMatrix {

    private Object2IntOpenHashMap<String> gramIdx = new Object2IntOpenHashMap<>();
    private final List<String> terms = new ArrayList<>();

    public TermMatrix() {
        gramIdx.defaultReturnValue(-1);
    }

    public void generate(String inFile, String outFile) throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(inFile))) {
            //stream out as ccs format
            try (CCSStreamMatrix out = new CCSStreamMatrix(outFile)) {
                //start with 0
                out.writeColPtr(0);
                //
                int nnz = 0;
                String term;
                while ((term = reader.readLine()) != null) {
                    String prepared = preProcess(term);
                    //start and end spaces
                    for (int i = 0, size = prepared.length() - 2; i < size; i++) {
                        String gram = prepared.substring(i, i + 3);
                        //store gram with index (rowIdx)
                        int rowIdx = gramIdx.getInt(gram);
                        if (rowIdx == -1) {
                            gramIdx.put(gram, rowIdx = gramIdx.size());
                        }
                        //store term with index (colIdx)
                        out.writeValue(1f / size);
                        out.writeRowIdx(rowIdx);
                        nnz++;
                    }
                    out.writeColPtr(nnz);
                    terms.add(term);
                }
                //dont forget to specify, before close
                out.nrow = gramIdx.size();
                out.ncol = terms.size();
            }
        }
    }
    
    public String getTerm(int idx){
        return terms.get(idx);
    }

    protected String preProcess(String line) {
        line = line.trim().toLowerCase().replace(",", " ");
        return " " + line.replaceAll(" +", " ") + " ";
    }

    public Object2IntOpenHashMap<String> getGramIdx() {
        return gramIdx;
    }

    public void storeIndex(String file) throws IOException {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(file))) {
            out.writeObject(gramIdx);
        }
    }

    public void loadIndex(String file) throws IOException {
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(file))) {
            try {
                gramIdx = (Object2IntOpenHashMap<String>) in.readObject();
            } catch (ClassNotFoundException ex) {
                throw new IOException(ex);
            }
        }
    }
}
