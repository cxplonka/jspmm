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
package com.jspmm.util;

import com.jspmm.matrix.CCSMatrix;
import static com.jspmm.util.Util.toFloat;
import static com.jspmm.util.Util.toInt;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.Closeable;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 *
 * @author Christian Plonka (cplonka81@gmail.com)
 */
public class CCSStreamMatrix implements Closeable {

    final String file;
    final DataOutputStream colPtrStream;
    final DataOutputStream rowIdxStream;
    final DataOutputStream valuesStream;
    final File fRowIdx;
    final File fColPtr;
    final File fValues;
    public int nrow;
    public int ncol;
    private int nnz = 0;

    public CCSStreamMatrix(String file) throws IOException {
        this.file = file;

        fRowIdx = new File(String.format("%s.rowIdx.tmp", file));
        fColPtr = new File(String.format("%s.colPtr.tmp", file));
        fValues = new File(String.format("%s.values.tmp", file));

        rowIdxStream = new DataOutputStream(new BufferedOutputStream(new FileOutputStream(fRowIdx)));
        colPtrStream = new DataOutputStream(new BufferedOutputStream(new FileOutputStream(fColPtr)));
        valuesStream = new DataOutputStream(new BufferedOutputStream(new FileOutputStream(fValues)));
    }

    public CCSStreamMatrix(String file, int nrow, int ncol) throws IOException {
        this(file);
        this.nrow = nrow;
        this.ncol = ncol;
    }

    public void writeRowIdx(int idx) throws IOException {
        rowIdxStream.writeInt(idx);
    }

    public void writeColPtr(int ptr) throws IOException {
        colPtrStream.writeInt(ptr);
    }

    public void writeValue(float value) throws IOException {
        valuesStream.writeFloat(value);
        nnz++;
    }

    @Override
    public void close() throws IOException {
        Util.quiteClose(rowIdxStream);
        Util.quiteClose(colPtrStream);
        Util.quiteClose(valuesStream);
        //merge all together
        try (DataOutputStream out = new DataOutputStream(new BufferedOutputStream(
                new FileOutputStream(file)))) {
            out.writeInt(nrow);
            out.writeInt(ncol);
            out.writeInt(nnz); //nnz = values/idx length, ptr len = nrow|ncol            
            Util.mergeFiles(new File[]{fValues, fRowIdx, fColPtr}, out);
        } finally {
            fRowIdx.delete();
            fColPtr.delete();
            fValues.delete();
        }
    }

    public static CCSMatrix readCCSMatrix(String file) {
        try (DataInputStream in = new DataInputStream(new BufferedInputStream(new FileInputStream(file)))) {
            int nrow = in.readInt();
            int ncol = in.readInt();
            int nnz = in.readInt();
            //
            byte[] bytes = new byte[nnz * 4];
            //read values
            in.read(bytes);
            float[] values = toFloat(bytes);
            //read column index
            in.read(bytes);
            int[] rowIdx = toInt(bytes);
            //read row pointer
            bytes = new byte[(ncol + 1) * 4];
            in.read(bytes);
            int[] colPtr = toInt(bytes);
            //
            return new CCSMatrix(nrow, ncol, values, rowIdx, colPtr);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
