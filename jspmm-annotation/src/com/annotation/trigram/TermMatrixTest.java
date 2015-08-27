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

import com.jspmm.matrix.CCSMatrix;
import com.jspmm.matrix.CRSMatrix;
import com.jspmm.matrix.DenseFloatMatrix;
import com.jspmm.util.CCSStreamMatrix;
import com.jspmm.util.CRSStreamMatrix;
import com.jspmm.util.Util;
import java.io.IOException;

/**
 *
 * @author Christian Plonka (cplonka81@gmail.com)
 */
public class TermMatrixTest {

    public static void main(String[] arg) throws IOException {
        TermMatrix mesh = new TermMatrix();
        mesh.generate("d:/terms.txt", "d:/terms_ccs.mat");
                
        SentenceMatrix data = new SentenceMatrix(mesh.getGramIdx());
        data.generate("d:/sentences.txt", "d:/sentense_crs.mat");        
        
        CRSMatrix crs = CRSStreamMatrix.readCRSMatrix("d:/sentense_crs.mat");
        System.out.println(crs.nrow + " " + crs.ncol);
        
        CCSMatrix ccs = CCSStreamMatrix.readCCSMatrix("d:/terms_ccs.mat");
        System.out.println(ccs.nrow + " " + ccs.ncol);
        
        DenseFloatMatrix m = crs.multiply(ccs);
        Util.print(m);
    }
}
