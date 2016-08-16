/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

import de.hpi.fgis.dude.output.CSVOutput;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

/**
 *
 * @author Priscilla
 */
public class Arquivo {
    private String nomeArquivo;

    public Arquivo(String nomeArquivo) {
        this.nomeArquivo = nomeArquivo;
    }
    public void insereTemposEmTXT(double tempos[]) throws IOException{
         FileWriter arq = new FileWriter(this.nomeArquivo); 
       PrintWriter gravarArq = new PrintWriter(arq);
        
        for (int i = 0; i < tempos.length; i++) {
            double tempo = tempos[i];
             gravarArq.printf(", "+tempo);
            
        }
        arq.close();

        
    }
    
    
    
}
