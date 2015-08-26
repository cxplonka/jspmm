/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
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
 * @author cplonka
 */
public class NewClass {

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
