/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Index;

import graph.Vertice;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;


/**
 *
 * @author Priscilla
 */
public class BlockIndex {

    Map<String, ArrayList<Vertice>> mapaNomes;

    public BlockIndex() {
        this.mapaNomes = new HashMap<String, ArrayList<Vertice>>();

    }
    
    public void printBlockIndex (){
        
        System.err.println(" tamanho ");
            for (Map.Entry<String, ArrayList<Vertice>> entrySet1 : mapaNomes.entrySet()) {
                String key = entrySet1.getKey();
                ArrayList<Vertice> value = entrySet1.getValue();
                System.err.println("Chave de blocagem " + key +" "+ mapaNomes.size());
                System.err.println();
                for (int i = 0; i < value.size(); i++) {
                    Vertice get = value.get(i);
                    System.err.println(get.getId() + " Vertice da chave " + key + " Cluster " + value.get(i).getClusterId());
                    
                }
                
            }
            
        
    }
    
    
    /**public void insereClusterBI(String key, Vertice v1, Vertice v2, String SourceId) {
       
        
        if (this.mapaNomes.containsKey(key)) {       
            
            for (int i = 0; i < mapaNomes.get(key).size(); i++) {
                   Vertice get = mapaNomes.get(keyBloco).get(i);
                 if (verticeId.equals(get.getId()) && sourceId.equals(get.getSourceId())) {
                     
                     return get.getClusterId();
                     
                 }
               }
             
        }
        
           

        }
    } **/
 public  ArrayList<Vertice> insertClusterId(Vertice v1, ArrayList<Vertice> vertices) {
         
      
       
             for (int i = 0; i < vertices.size(); i++) {
                   Vertice get = vertices.get(i);
                 if ((v1.getId()==get.getId()) && (v1.getSourceId()== get.getSourceId())){                                     
                     
                          get.setClusterId(v1.getClusterId());
                          System.out.println(" Ja tinha o vertice no CI  ");
                          
                          return vertices;
                     
                     
                 }
               }
             
                 vertices.add(v1);
                 return vertices;
         
     
            
             
                         
            
         
     
    }   
        

    public void insertVertice(String key, Vertice newVertice) {
            
                        Vertice get = null;

        if (this.mapaNomes.containsKey(key)) {
             Vertice v1 = null;
            
            ArrayList <Vertice> aux = this.mapaNomes.get(key);
             for (int i = 0; i < aux.size(); i++) {
                    get = aux.get(i);
                 if ((newVertice.getId().equals(get.getId())) && (newVertice.getSourceId().equals(get.getSourceId()))){   
                     v1 = get;
                     
                 }
               }
             if (v1 ==null) {
                aux.add(newVertice);
            }else{
                 get.setClusterId(newVertice.getClusterId());
             }
             
               
            
          

        } else {
            ArrayList<Vertice> aux = new ArrayList<Vertice>();
            aux.add(newVertice);
            this.mapaNomes.put(key, aux);
           

        }
    }

   

    public void insertVerticeList(String key, ArrayList<Vertice> newVertice) {
        if (this.mapaNomes.containsKey(key)) {
            this.mapaNomes.get(key).addAll(newVertice);

        } else {
            this.mapaNomes.put(key, newVertice);

        }
    }

    public Map<String, ArrayList<Vertice>> getMapaNomes() {
        return mapaNomes;
    }

   

   

    public void setMapaNomes(Map<String, ArrayList<Vertice>> mapaNomes) {
        this.mapaNomes = mapaNomes;
    }
     public  int getclusterId(String verticeId, String keyBloco, String sourceId) {
         
       
         if (mapaNomes.get(keyBloco)!=null) {
             for (int i = 0; i < mapaNomes.get(keyBloco).size(); i++) {
                   Vertice get = mapaNomes.get(keyBloco).get(i);
                 if (verticeId.equals(get.getId()) && sourceId.equals(get.getSourceId())) {
                                       
                    return  get.getClusterId();
                     
                 }
               }
            
             
                         
            
         } 
        return -1;
    }
     
     public Vertice getclusterIdOrdenado(Vertice verticeId, String keyBloco) {

        int saida = -1;
       
        if (mapaNomes.get(keyBloco) != null) {

            saida = Collections.binarySearch(mapaNomes.get(keyBloco), verticeId, new Comparator() {

                @Override
                public int compare(Object o1, Object o2) {
                    Vertice p1 = (Vertice) o1;
                    Vertice p2 = (Vertice) o2;
                    return Integer.compare(Integer.valueOf(p1.getId()), Integer.valueOf(p2.getId()));

                }

            });

        }
        
         if (saida>=0) {
             return mapaNomes.get(keyBloco).get(saida);
         }else{
             return null;
         }
         
        
    }
     
     public  void setclusterId(String verticeId, String keyBloco, String sourceId, int clusterId) {
         
       
         if (mapaNomes.get(keyBloco)!=null) {
             for (int i = 0; i < mapaNomes.get(keyBloco).size(); i++) {
                   Vertice get = mapaNomes.get(keyBloco).get(i);
                 if (verticeId.equals(get.getId()) && sourceId.equals(get.getSourceId())) {
                                       
                     get.setClusterId(clusterId);
                     
                 }
               }
            
             
                         
            
         } 
       
    }
     
     
    
     public  boolean getId(String verticeId, String keyBloco, String sourceId) {
         
          if (mapaNomes.get(keyBloco)!=null) {
             for (int i = 0; i < mapaNomes.get(keyBloco).size(); i++) {
                   Vertice get = mapaNomes.get(keyBloco).get(i);
                 if (verticeId.equals(get.getId()) && sourceId.equals(get.getSourceId())) {
                                       
                     return true;
                     
                 }
               }
          }
            
             
                         
        
        return false;
    }
     
      
    
    public int getNumeroElementos() {
        
        int tamanho =0;
        
        
        for (Map.Entry<String, ArrayList<Vertice>> entrySet : this.mapaNomes.entrySet()) {
            //String key = entrySet.getKey();
            tamanho = tamanho + entrySet.getValue().size();
            
        }
        
        return tamanho;
    }
     public int getNumeroBlocos() {
        
       
        return this.mapaNomes.size();
        
        
    }
     
     
        public int removeVertice() {
        
       
        return this.mapaNomes.size();
        
        
    }

   /**public static void main(String[] args) {
       BlockIndex BI = new BlockIndex();
       if (!BI.equals(BI)){
           System.out.println("OK");
           
       }
       else{
            System.out.println("NOT OK");
       }
   }**/
    
   /** public static void main(String[] args) {
        BlockIndex BI = new BlockIndex();
        Vertice v1 = new Vertice(1, 11, 21);
        Vertice v2 = new Vertice(2, 12, 22);
        Vertice v3 = new Vertice(3, 13, 23);
        
        BI.insertVertice("teste1", v1);
        BI.insertVertice("teste1", v2);
        BI.insertVertice("teste2", v3);
        
        
        
        System.out.println (BI.getMapaNomes().get("teste1").get(0).getClusterId());
                System.out.println (BI.getMapaNomes().get("teste1").get(1).getClusterId());


    }**/

}
