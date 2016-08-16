/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package avaliacao;

import Index.BlockIndex;
import de.hpi.fgis.dude.datasource.CSVSource;
import de.hpi.fgis.dude.output.statisticoutput.SimpleStatisticOutput;
import de.hpi.fgis.dude.output.statisticoutput.StatisticOutput;
import de.hpi.fgis.dude.postprocessor.StatisticComponent;
import de.hpi.fgis.dude.util.GoldStandard;
import de.hpi.fgis.dude.util.data.DuDeObject;
import de.hpi.fgis.dude.util.data.DuDeObjectPair;
import graph.Vertice;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

/**
 *
 * @author Priscilla
 */
public class Precisao {

   
    GoldStandard goldStandard;
    StatisticComponent st ;
    int truePositive = 0;
    int falsePositive = 0;


    public Precisao() {
        
        

    }

    public void InicializaGoldStandardCD() throws FileNotFoundException, IOException {
        CSVSource goldstandardSource = new CSVSource("goldstandard", new File("cd_gold.csv"));
        goldstandardSource.enableHeader();
        GoldStandard goldStandard = new GoldStandard(goldstandardSource);
        goldStandard.setFirstElementsObjectIdAttributes("disc1_id");
        goldStandard.setSecondElementsObjectIdAttributes("disc2_id");
        goldStandard.setSourceIdLiteral("cd");
        this.goldStandard = goldStandard;
        this.st = new StatisticComponent(goldStandard, null);
        


    }
    
      public void InicializaGoldStandardCora() throws FileNotFoundException, IOException {
        CSVSource goldstandardSource = new CSVSource("goldstandard", new File("cora_goldCSV.csv"));
        goldstandardSource.enableHeader();
        GoldStandard goldStandard = new GoldStandard(goldstandardSource);
        goldStandard.setFirstElementsObjectIdAttributes("id1");
        goldStandard.setSecondElementsObjectIdAttributes("id2");
        goldStandard.setSourceIdLiteral("cora");
        this.goldStandard = goldStandard;
        this.st = new StatisticComponent(goldStandard, null);
         


    }
    public void InicializaGoldStandardFebrl() throws FileNotFoundException, IOException {
        CSVSource goldstandardSource = new CSVSource("goldstandard", new File("gold_febrl.csv"));
        goldstandardSource.enableHeader();
        GoldStandard goldStandard = new GoldStandard(goldstandardSource);
        goldStandard.setFirstElementsObjectIdAttributes("febrl1_id");
        goldStandard.setSecondElementsObjectIdAttributes("febrl2_id");
        goldStandard.setSourceIdLiteral("febrl");
        this.goldStandard = goldStandard;
        this.st = new StatisticComponent(goldStandard, null);
        


    }

    public GoldStandard getGoldStandard() {
        return goldStandard;
    }

    public void setGoldStandard(GoldStandard goldStandard) {
        this.goldStandard = goldStandard;
    }

    public StatisticComponent getSt() {
        return st;
    }

    public void setSt(StatisticComponent st) {
        this.st = st;
    }

    public int getCerto() {
        return truePositive;
    }

    public void setCerto(int certo) {
        this.truePositive = certo;
    }

    public int getErrado() {
        return falsePositive;
    }

    public void setErrado(int errado) {
        this.falsePositive = errado;
    }

    public void inserePairDuplicado(String fonte1, String tuplaId1, String fonte2, String tuplaId2) {
        DuDeObject dude1 = new DuDeObject(fonte1, tuplaId1);
        DuDeObject dude2 = new DuDeObject(fonte2, tuplaId2);
        DuDeObjectPair dudePair = new DuDeObjectPair(dude1, dude2);
        st.addDuplicate(dudePair);
        if (st.isDuplicate(dudePair)) {
           // System.err.println(" Iehh");
            truePositive++;
            
        }
        else{
           //  System.err.println(" no");
             falsePositive++;
        }

       
          }
    
    public double getPrecisao(){
        StatisticOutput statisticOutput = new SimpleStatisticOutput(System.out, this.st);
        //System.out.println(" Total certo " + truePositive);
         //  System.out.println(" Total errado " + falsePositive);
        return statisticOutput.getStatistics().getPrecision();
        
        
    }
     public double getCobertura(){
        StatisticOutput statisticOutput = new SimpleStatisticOutput(System.out, this.st);
        return statisticOutput.getStatistics().getRecall();
    }
      public void insereParesPorCluster(Cluster cluster){
         for (Map.Entry<Integer, ArrayList<Vertice>> entrySet : cluster.getMapaArestas().entrySet()) {
             Integer key = entrySet.getKey();
             ArrayList<Vertice> value = entrySet.getValue();
             for (int i = 0; i < value.size(); i++) {
                 Vertice get = value.get(i);
                 for (int j = i+1; j < value.size(); j++) {
                     Vertice get1 = value.get(j);
                       inserePairDuplicado("cd", get.getId(), "cd", get1.getId());
                     
                 }
                 
             }
             
         }
       
         
     }
      
        public void insereParesPorClusterCora(Cluster cluster){
         for (Map.Entry<Integer, ArrayList<Vertice>> entrySet : cluster.getMapaArestas().entrySet()) {
             Integer key = entrySet.getKey();
             ArrayList<Vertice> value = entrySet.getValue();
             for (int i = 0; i < value.size(); i++) {
                 Vertice get = value.get(i);
                 for (int j = i+1; j < value.size(); j++) {
                     Vertice get1 = value.get(j);
                       inserePairDuplicado("cora", get.getId(), "cora", get1.getId());
                     
                 }
                 
             }
             
         }
       
         
     }
     public void insereParesPorClusterFebrl(Cluster cluster){
         for (Map.Entry<Integer, ArrayList<Vertice>> entrySet : cluster.getMapaArestas().entrySet()) {
             Integer key = entrySet.getKey();
             ArrayList<Vertice> value = entrySet.getValue();
             for (int i = 0; i < value.size(); i++) {
                 Vertice get = value.get(i);
                 for (int j = i+1; j < value.size(); j++) {
                     Vertice get1 = value.get(j);
                       inserePairDuplicado("febrl", get.getId(), "febrl", get1.getId());
                     
                 }
                 
             }
             
         }
       
         
     }
      public void clusterToPair(BlockIndex bi2){
         
         
         for (Map.Entry<String, ArrayList<Vertice>> entrySet : bi2.getMapaNomes().entrySet()) {
             Cluster cluster = new Cluster();
            
           ArrayList<Vertice> value = entrySet.getValue();
             
             for (int i = 0; i < value.size(); i++) {
                 Vertice get = value.get(i);
                 cluster.insereVertice(get);
                 
             }
             insereParesPorCluster(cluster);
             
             
         }
         
     }
     
    
     public void clusterToPairFerl(BlockIndex bi2){
         
         
         for (Map.Entry<String, ArrayList<Vertice>> entrySet : bi2.getMapaNomes().entrySet()) {
             Cluster cluster = new Cluster();
            
           ArrayList<Vertice> value = entrySet.getValue();
             
             for (int i = 0; i < value.size(); i++) {
                 Vertice get = value.get(i);
                 cluster.insereVertice(get);
                 
             }
             insereParesPorCluster(cluster);
             
             
         }
         
     }
 public void clusterToPairFebrl(BlockIndex bi2){
         
         
         for (Map.Entry<String, ArrayList<Vertice>> entrySet : bi2.getMapaNomes().entrySet()) {
             Cluster cluster = new Cluster();
            
           ArrayList<Vertice> value = entrySet.getValue();
             
             for (int i = 0; i < value.size(); i++) {
                 Vertice get = value.get(i);
                 cluster.insereVertice(get);
                 
             }
             insereParesPorClusterFebrl(cluster);
             
             
         }
         
     }
  
 
 public void clusterToPairCora(BlockIndex bi2){
         
         
         for (Map.Entry<String, ArrayList<Vertice>> entrySet : bi2.getMapaNomes().entrySet()) {
             Cluster cluster = new Cluster();
            
           ArrayList<Vertice> value = entrySet.getValue();
             
             for (int i = 0; i < value.size(); i++) {
                 Vertice get = value.get(i);
                 cluster.insereVertice(get);
                 
             }
             insereParesPorClusterCora(cluster);
             
             
         }
         
     }
}
