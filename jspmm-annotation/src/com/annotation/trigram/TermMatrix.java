/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
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
 * @author cplonka
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
