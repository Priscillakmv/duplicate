/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
Precisa ajustar para o cora
 */
package query;

import Index.BlockIndex;
import de.hpi.fgis.dude.datasource.CSVSource;


import de.hpi.fgis.dude.util.GlobalConfig;
import de.hpi.fgis.dude.util.data.DuDeObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Iterator;
import org.apache.commons.codec.language.DoubleMetaphone;

/**
 *
 * @author Priscilla
 * 
 * 
 */

public class QueryLevensteinExperimentCora {
    public static BlockIndex bi;

    public QueryLevensteinExperimentCora(BlockIndex bi) {
        this.bi = bi;
    }
    public void query() throws FileNotFoundException{
        GlobalConfig.getInstance().setInMemoryObjectThreshold(1000);

        // sets the CSV data source
        CSVSource dataSource = new CSVSource("cora", new File(".csv"));
        dataSource.enableHeader();
        dataSource.addIdAttributes("pk");
        long start = System.currentTimeMillis();
        
          DoubleMetaphone db = new DoubleMetaphone();
          int achou = 0;
        int nAchou = 0;
        int total = 0;
         for (Iterator <DuDeObject> iterator =  dataSource.iterator(); iterator.hasNext();) {
             DuDeObject next = iterator.next();
             
             String pk = next.getAttributeValue("pk").toString();
             String block = next.getAttributeValue("artist").toString();   
            
             String keyBlock = db.encode(block);
            boolean clusterId = bi.getId(pk, keyBlock, "cd");
            total++;
             if (clusterId) {
                achou++;
                // System.err.println(" Imprime " + pk + " " + clusterId);
                 
                 
             }else{
                 nAchou++;
                 //System.err.println(" Imprime " + pk + " " + clusterId);
             }
             
             
             
         }
         System.err.println( (System.currentTimeMillis() - start) + " ms");
         System.err.println("total " + total + " n achou " + nAchou + "Achou " + achou);
    }
    
    
    
}
