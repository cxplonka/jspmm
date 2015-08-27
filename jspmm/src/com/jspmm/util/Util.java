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

import com.jspmm.matrix.AbstractMatrix;
import java.io.BufferedReader;
import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.nio.file.Files;
import java.util.Random;

/**
 *
 * @author Christian Plonka (cplonka81@gmail.com)
 */
public class Util {

    static public String readContent(InputStream stream) throws IOException {
        StringBuilder contents = new StringBuilder();

        try (BufferedReader input = new BufferedReader(new InputStreamReader(stream))) {
            String line;
            while ((line = input.readLine()) != null) {
                contents.append(line);
                contents.append(System.getProperty("line.separator"));
            }
        }
        return contents.toString();
    }

    public static FloatBuffer createDirectFloatBuffer(final int size) {
        final FloatBuffer buf = ByteBuffer.allocateDirect(4 * size).order(ByteOrder.nativeOrder()).asFloatBuffer();
        return buf;
    }

    public static IntBuffer createDirectIntBuffer(final int size) {
        final IntBuffer buf = ByteBuffer.allocateDirect(4 * size).order(ByteOrder.nativeOrder()).asIntBuffer();
        return buf;
    }

    public static void quiteClose(Closeable stream) {
        try {
            if (stream != null) {
                stream.close();
            }
        } catch (IOException e) {
            /* Ignore */
        }
    }

    public static void random(AbstractMatrix m, double density, int max) {
        Random rnd = new Random();
        if (density == 0) {
            return;
        }
        for (int i = 0; i < m.nrow; i++) {
            for (int j = 0; j < m.ncol; j++) {
                if (rnd.nextDouble() < density) {
                    m.set(i, j, rnd.nextInt(max));
                }
            }
        }
    }

    public static void print(AbstractMatrix m) {
        for (int i = 0; i < m.nrow; i++) {
            for (int j = 0; j < m.ncol; j++) {
                System.out.print(m.get(i, j) + " ");
            }
            System.out.println("");
        }
    }

    public static int numberNonZero(AbstractMatrix m) {
        int ret = 0;
        for (int i = 0; i < m.nrow; i++) {
            for (int j = 0; j < m.ncol; j++) {
                if (m.get(i, j) != 0) {
                    ret++;
                }
            }
        }
        return ret;
    }

    public static void createCRSMatrix(String file, int nrow, int ncol, double density) {
        try (CRSStreamMatrix out = new CRSStreamMatrix(file, nrow, ncol)) {
            //start with 0
            out.writeRowPtr(0);
            Random rnd = new Random();
            for (int i = 0, nnz = 0; i < nrow; i++) {
                for (int j = 0; j < ncol; j++) {
                    if (rnd.nextDouble() < density) {
                        int value = rnd.nextInt(100);
                        if (value != 0) {
                            out.writeValue(value);
                            out.writeColIdx(j);
                            nnz++;
                        }
                    }
                }
                out.writeRowPtr(nnz);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void createCCSMatrix(String file, int nrow, int ncol, double density) {
        try (CCSStreamMatrix out = new CCSStreamMatrix(file, nrow, ncol)) {
            //start with 0
            out.writeColPtr(0);
            Random rnd = new Random();
            for (int j = 0, nnz = 0; j < ncol; j++) {
                for (int i = 0; i < nrow; i++) {
                    if (rnd.nextDouble() < density) {
                        int value = rnd.nextInt(100);
                        if (value != 0) {                            
                            out.writeValue(value);
                            out.writeRowIdx(i);
                            nnz++;
                        }
                    }
                }
                out.writeColPtr(nnz);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static float[] toFloat(byte[] bytes) {
        ByteBuffer byteBuffer = ByteBuffer.wrap(bytes);
        FloatBuffer buffer = byteBuffer.asFloatBuffer();
        float ret[] = new float[buffer.capacity()];
        buffer.get(ret);
        return ret;
    }

    public static int[] toInt(byte[] bytes) {
        ByteBuffer byteBuffer = ByteBuffer.wrap(bytes);
        IntBuffer buffer = byteBuffer.asIntBuffer();
        int ret[] = new int[buffer.capacity()];
        buffer.get(ret);
        return ret;
    }

    public static void mergeFiles(File[] files, OutputStream out) {
        //every files
        for (File file : files) {
            try {
                out.write(Files.readAllBytes(file.toPath()));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }    
}