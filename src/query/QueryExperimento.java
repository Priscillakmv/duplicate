/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package query;

import classificacao.ClusterizacaoIncrementalIngenua;
import Index.BlockIndex;
import Index.SimilarityIndex;
import de.hpi.fgis.dude.datasource.CSVSource;
import de.hpi.fgis.dude.datasource.DataSource;
import de.hpi.fgis.dude.datasource.XMLSource;
import de.hpi.fgis.dude.similarityfunction.contentbased.util.SoundEx;
import de.hpi.fgis.dude.util.GlobalConfig;
import de.hpi.fgis.dude.util.data.DuDeObject;
import de.hpi.fgis.dude.util.data.json.JsonValue;
import graph.Vertice;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import org.apache.commons.codec.language.DoubleMetaphone;

/**
 *
 * @author Priscilla
 *
 *
 */
public class QueryExperimento {

    BlockIndex bi;
    SimilarityIndex si;

    public QueryExperimento(BlockIndex bi, SimilarityIndex si) {
        this.bi = bi;
        this.si = si;
    }

    public QueryExperimento(BlockIndex bi) {
        this.bi = bi;

    }

    public QueryExperimento() {

    }

  
    public void query() throws FileNotFoundException {
        GlobalConfig.getInstance().setInMemoryObjectThreshold(1000);

        // sets the CSV data source
        CSVSource dataSource = new CSVSource("cd", new File("cd.csv"));
        dataSource.enableHeader();
        dataSource.addIdAttributes("pk");
        long start = System.currentTimeMillis();

        DoubleMetaphone db = new DoubleMetaphone();
        int achou = 0;
        int nAchou = 0;
        int total = 0;

        for (Iterator<DuDeObject> iterator = dataSource.iterator(); iterator.hasNext();) {
            DuDeObject next = iterator.next();

            String pk = next.getAttributeValue("pk").toString();
            String block = next.getAttributeValue("artist").toString();

            String keyBlock = db.encode(block);

            boolean clusterId = bi.getId(pk, keyBlock, "cd");
            total++;
            if (clusterId) {
                achou++;
               
            } else {
                nAchou++;
               
            }

        }
        System.err.println((System.currentTimeMillis() - start) + " ms");
        System.err.println("total " + total + " n achou " + nAchou + "Achou " + achou);
    }
    public CSVSource carregaDadosCora() throws FileNotFoundException {
        GlobalConfig.getInstance().setInMemoryObjectThreshold(1000);

        // sets the CSV data source
        CSVSource dataSource = new CSVSource("cora", new File("coraCSV.csv"));
        dataSource.enableHeader();
        dataSource.addIdAttributes("ID");

        return dataSource;


    }
    
     public CSVSource carregaDadosFebrl() throws FileNotFoundException {
        GlobalConfig.getInstance().setInMemoryObjectThreshold(1000);

        // sets the CSV data source
        CSVSource dataSource = new CSVSource("febrl", new File("febrl.csv"));
        dataSource.enableHeader();
        dataSource.addIdAttributes("id");
        
        return dataSource;

    }

    public CSVSource carregaDadosCD() throws FileNotFoundException {
        GlobalConfig.getInstance().setInMemoryObjectThreshold(1000);

        // sets the CSV data source
        CSVSource dataSource = new CSVSource("cd", new File("cd.csv"));
        dataSource.enableHeader();
        dataSource.addIdAttributes("pk");
        
        return dataSource;

    }
    
        /**
     * 
     * @param dataSource
     * @param tamanho porcentagem de elementos que n]ao se deseja guardar informações
     * @return as tuplas que se deseja ter informações em um blooco que sera processado
     */
    public BlockIndex blocaConsultaReduzidaFixa (CSVSource dataSource, int tamanho){
        
        BlockIndex bi2 = new BlockIndex();
        DoubleMetaphone db = new DoubleMetaphone();
        int numeroElementos = 0;
        
        for (Iterator<DuDeObject> iterator = dataSource.iterator(); iterator.hasNext();) {
            DuDeObject next = iterator.next();
            if (numeroElementos<tamanho) {
                String pk = next.getAttributeValue("pk").toString();
            String block = next.getAttributeValue("title").toString();
            String block2 = next.getAttributeValue("artist").toString();
                    

            String keyBlock = db.encode(block);
            Vertice v1 = new Vertice(pk, "cd", -1, block, block2);
            bi2.insertVertice(keyBlock, v1);
                numeroElementos++;
                
            }else{
            
            numeroElementos++;
            }

            

        }
        System.out.println(" Tamanho bloco " + bi2.getNumeroElementos());
        return bi2;
        
    }
    /**
     * 
     * @param dataSource
     * @param tamanho porcentagem de elementos que n]ao se deseja guardar informações
     * @return as tuplas que se deseja ter informações em um blooco que sera processado
     */
    public BlockIndex blocaConsultaReduzida (CSVSource dataSource, int tamanho){
        
        BlockIndex bi2 = new BlockIndex();
        DoubleMetaphone db = new DoubleMetaphone();
        int numeroElementos = 0;
        
        for (Iterator<DuDeObject> iterator = dataSource.iterator(); iterator.hasNext();) {
            DuDeObject next = iterator.next();
            if (numeroElementos%2==0) {
                numeroElementos++;
                
            }else{
            String pk = next.getAttributeValue("pk").toString();
            String block = next.getAttributeValue("title").toString();
            String block2 = next.getAttributeValue("artist").toString();
                    

            String keyBlock = db.encode(block);
            Vertice v1 = new Vertice(pk, "cd", -1, block, block2);
            bi2.insertVertice(keyBlock, v1);
            numeroElementos++;
            }

            

        }
        
        return bi2;
        
    }
    
    public BlockIndex blocaConsultaFebrl(CSVSource dataSource){
       // int count = 0;
        BlockIndex bi2 = new BlockIndex();
       // SoundEx db = new SoundEx();
        
       // DoubleMetaphone db = new DoubleMetaphone();
        for (Iterator<DuDeObject> iterator = dataSource.iterator(); iterator.hasNext();) {
            DuDeObject next = iterator.next();
            String block = "";
            String block2 = "";
            //System.err.println(" " + next.getAttributeValue("id"));

            String pk = next.getAttributeValue("id").toString();
            if (next.getAttributeValue("given_name")!=null) {
                block = next.getAttributeValue("given_name").toString();
                //System.err.println(" Teste 1");
            }
            if (next.getAttributeValue("surname")!= null) {
                block2 = next.getAttributeValue("surname").toString();
               // System.err.println(" Teste 2");
            }
         

            String keyBlock = next.getAttributeValue("blocking").toString();
            //String keyBlock = db.getSoundEx(block2);
            Vertice v1 = new Vertice(pk, "febrl", -1, block, block2);
            bi2.insertVertice(keyBlock, v1);
           // count ++;
         
            

        }
      //  System.out.println(" Tamanho count:  "+ count);
      
       
        return bi2;
    }
    
    public BlockIndex blocaConsultaCD (CSVSource dataSource){
      
        BlockIndex bi2 = new BlockIndex();
       // SoundEx db = new SoundEx();
        
        DoubleMetaphone db = new DoubleMetaphone();
        for (Iterator<DuDeObject> iterator = dataSource.iterator(); iterator.hasNext();) {
            DuDeObject next = iterator.next();

            String pk = next.getAttributeValue("pk").toString();
            String block = next.getAttributeValue("title").toString();
            String block2 = next.getAttributeValue("artist").toString();
                    

            String keyBlock = db.doubleMetaphone(block);
            //String keyBlock = db.getSoundEx(block2);
            Vertice v1 = new Vertice(pk, "cd", -1, block, block2);
            bi2.insertVertice(keyBlock, v1);
         
            

        }
      
       
        return bi2;
        
    }
    
  
    
     public BlockIndex blocaConsultaCora (CSVSource dataSource){
        
        BlockIndex bi2 = new BlockIndex();
        DoubleMetaphone db = new DoubleMetaphone();
        for (Iterator<DuDeObject> iterator = dataSource.iterator(); iterator.hasNext();) {
            DuDeObject next = iterator.next();

            String pk = next.getAttributeValue("ID").toString();
            JsonValue block =  next.getAttributeValue("TITLE");
            //JsonValue block2 =  next.getAttributeValue("title");
            
            if (block == null ) {
               continue;
            }else{
              String block2="";  
                 String keyBlock = db.encode(block.toString());
            Vertice v1 = new Vertice(pk, "cora", -1, block.toString(), block2.toString());
            bi2.insertVertice(keyBlock, v1);

            }
                  

           
                
            
            
            
        }
        return bi2;
        
    }

    public void queryBuscaTudo(ClusterizacaoIncrementalIngenua cl) throws FileNotFoundException {

        CSVSource dataSource = carregaDadosCD();
        
        long start = System.currentTimeMillis();    
        int achou = 0;
        int nAchou = 0;
        int total = 0;
        
       BlockIndex bi2 = blocaConsultaCD(dataSource);
       
       //ClusterizacaoIngenua cluster = new ClusterizacaoIngenua(this.bi, this.si);
       cl.cluserizaAcessandoBIeSI(bi2);

       
        //bi e si reais
      
       
        System.out.println(" Tamanho bloco  " + bi2.getNumeroElementos());
        System.out.println((System.currentTimeMillis() - start) + " ms");
        System.out.println("total " + total + " n achou " + nAchou + "Achou " + achou);
       System.out.println("++++++++++++++=Fim++++++++++++++++++++++++++++=");
       dataSource.cleanUp();

    }

   /** public static void main(String[] args) throws FileNotFoundException {
        QueryLevensteinExperimentCD q = new QueryLevensteinExperimentCD();
        q.queryBuscaTudo();

    }**/

}
