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

import com.jspmm.matrix.CRSMatrix;
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
public class CRSStreamMatrix implements Closeable {

    final String file;
    final DataOutputStream rowPtrStream;
    final DataOutputStream colIdxStream;
    final DataOutputStream valuesStream;
    final File fColIdx;
    final File fRowPtr;
    final File fValues;
    public int nrow;
    public int ncol;
    private int nnz = 0;

    public CRSStreamMatrix(String file) throws IOException {
        this.file = file;

        fColIdx = new File(String.format("%s.colIdx.tmp", file));
        fRowPtr = new File(String.format("%s.rowPtr.tmp", file));
        fValues = new File(String.format("%s.values.tmp", file));

        colIdxStream = new DataOutputStream(new BufferedOutputStream(new FileOutputStream(fColIdx)));
        rowPtrStream = new DataOutputStream(new BufferedOutputStream(new FileOutputStream(fRowPtr)));
        valuesStream = new DataOutputStream(new BufferedOutputStream(new FileOutputStream(fValues)));
    }

    public CRSStreamMatrix(String file, int nrow, int ncol) throws IOException {
        this(file);
        this.nrow = nrow;
        this.ncol = ncol;
    }

    public void writeColIdx(int idx) throws IOException {
        colIdxStream.writeInt(idx);
    }

    public void writeRowPtr(int ptr) throws IOException {
        rowPtrStream.writeInt(ptr);
    }

    public void writeValue(float value) throws IOException {
        valuesStream.writeFloat(value);
        nnz++;
    }

    @Override
    public void close() throws IOException {
        Util.quiteClose(colIdxStream);
        Util.quiteClose(rowPtrStream);
        Util.quiteClose(valuesStream);
        //merge all together
        try (DataOutputStream out = new DataOutputStream(new BufferedOutputStream(
                new FileOutputStream(file)))) {
            out.writeInt(nrow);
            out.writeInt(ncol);
            out.writeInt(nnz); //nnz = values/idx length, ptr len = nrow|ncol            
            Util.mergeFiles(new File[]{fValues, fColIdx, fRowPtr}, out);
        } finally {
            fColIdx.delete();
            fRowPtr.delete();
            fValues.delete();
        }
    }

    public static CRSMatrix readCRSMatrix(String file) {
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
            int[] colIdx = toInt(bytes);
            //read row pointer
            bytes = new byte[(nrow + 1) * 4];
            in.read(bytes);
            int[] rowPtr = toInt(bytes);
            //
            return new CRSMatrix(nrow, ncol, values, colIdx, rowPtr);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
