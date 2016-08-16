/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package classificacao;

import Index.BlockIndex;
import Index.SimilarityIndex;
import avaliacao.Cluster;
import graph.Aresta;
import graph.Vertice;
import java.util.ArrayList;
import java.util.Map;

/**
 *
 * @author Priscilla
 */
public class Garbage {
    
    public SimilarityIndex limpaSI(BlockIndex bi, SimilarityIndex si) {
            
        for (Map.Entry<String, ArrayList<Vertice>> entrySet : bi.getMapaNomes().entrySet()) {
           Cluster cluster = new Cluster(si);

            String key1 = entrySet.getKey();
            ArrayList<Vertice> value = entrySet.getValue();
            for (int i = 0; i < value.size(); i++) {
                Vertice get = value.get(i);
                cluster.insereVertice(get);
                
            }
            for (Map.Entry<Integer, ArrayList<Vertice>> entrySet1 : cluster.getMapaArestas().entrySet()) {
                Integer key2 = entrySet1.getKey();
                ArrayList <Vertice> value1 = entrySet1.getValue();
                si = cluster.criaCluster2(value1, key1);
                
            }
                   
            
        }
     //  Cluster aux = new Cluster(si);
   //   si = aux.deletaSIComVerticesSemArestas2(si);
       
        
        
        return si;
    }
    public BlockIndex limpaBI(BlockIndex bi) {

        for (Map.Entry<String, ArrayList<Vertice>> entrySet : bi.getMapaNomes().entrySet()) {
            String key1 = entrySet.getKey();
            ArrayList<Vertice> value = entrySet.getValue();
            Cluster cluster = new Cluster();
            for (int i = 0; i < value.size(); i++) {
                Vertice get = value.get(i);
                cluster.insereVertice(get);
                
            }
            for (Map.Entry<Integer, ArrayList<Vertice>> entrySet1 : cluster.getMapaArestas().entrySet()) {
                Integer key = entrySet1.getKey();
               ArrayList<Vertice> value1 = entrySet1.getValue();
               
               if (value1.size()==1) {
                  ArrayList<Vertice> value3 = bi.getMapaNomes().get(key1);
                   for (int i = 0; i < value3.size(); i++) {
                       if (value3.get(i).getId().equalsIgnoreCase(value1.get(0).getId())) {
                           value.remove(i);
                       }
                       
                   }
                
            }
                
            }
            
           
        }
       
        return bi;
    }
}
