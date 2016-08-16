/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package avaliacao;


import Index.SimilarityIndex;
import graph.Aresta;
import graph.Vertice;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import uk.ac.shef.wit.simmetrics.similaritymetrics.AbstractStringMetric;
import uk.ac.shef.wit.simmetrics.similaritymetrics.Levenshtein;

/**
 *
 * @author Priscilla
 */
public class Cluster {
   private  Map<Integer, ArrayList<Vertice>> mapaArestas;
   private SimilarityIndex si;

    public SimilarityIndex getSi() {
        return si;
    }

    public void setSi(SimilarityIndex si) {
        this.si = si;
    }

    public Cluster(SimilarityIndex si) {
        this.mapaArestas = new HashMap<Integer, ArrayList<Vertice>>();
        this.si = si;
    }
     public Cluster() {
        this.mapaArestas = new HashMap<Integer, ArrayList<Vertice>>();
       
    }

    public Map<Integer, ArrayList<Vertice>> getMapaArestas() {
        return mapaArestas;
    }
     public ArrayList<Vertice> getVertices (int clusterId) {
         
         for (Map.Entry<Integer, ArrayList<Vertice>> entrySet : this.mapaArestas.entrySet()) {
             Integer key = entrySet.getKey();
             ArrayList<Vertice> value = entrySet.getValue();
             if (key==clusterId) {
                 return value;
                 
             }
             
         }
        return null;
    }
    
    /**
     * Cria um cluster com as similaridades entre cada par
     * @param value
     * @param key 
     */
    public void criaCluster(ArrayList <Vertice> value, String key){
        for (int i = 0; i < value.size(); i++) {
            Vertice get = value.get(i);
            for (int j = i+1; j < value.size(); j++) {
                Vertice get1 = value.get(j);
                Aresta arestaAux = this.si.getAresta(key, get, get1.getId(), get1.getSourceId());
               if (arestaAux == null) { //mede similaridade
                                    AbstractStringMetric metric = new Levenshtein();
                                    float result1 = metric.getSimilarity(get.getConteudoComparacao(), get1.getConteudoComparacao());
                                    float result2 = metric.getSimilarity(get.getConteudoComparacao2(), get1.getConteudoComparacao2());
                                    float result = (float) (result1 + result2) / 2;
                                    get1.setSimilarity(result);
                                    insereVertice(get1);                                    
                                    si.verificaSimilaridade(result, key, get.getId(), get.getSourceId(), get1.getId(), get1.getSourceId(), key);
                                    
                                } else {                                    
                                        get1.setSimilarity(this.si.getAresta(key, get, get1.getId(), get1.getSourceId()).getSimilaridade());
                                        insereVertice(get1);

                                    

                                }
                
            }
            
        }
    
        
    }
   
    
     /**
     * Cria um cluster com as similaridades entre cada par. Removendo as similaridades dentro de um msm cluster
     * @param value
     * @param key
     */
    public SimilarityIndex criaCluster2(ArrayList<Vertice> value, String key) {
        for (int i = 0; i < value.size(); i++) {
            Vertice get = value.get(i);
            for (int j = i + 1; j < value.size(); j++) {
                Vertice get1 = value.get(j);
                Aresta arestaAux = this.si.getAresta(key, get, get1.getId(), get1.getSourceId());
                if (arestaAux != null) { //mede similaridade
                    this.si.removeAresta(key, get, get1.getId(), get.getSourceId());
                    this.si.removeAresta(key, get1, get.getId(), get1.getSourceId());
                  /**  int posicao = si.getVerticeOrigemOrdenado(key, get.getId(), get.getSourceId());
                    if (posicao != -1) {
                        if ((si.getArraySimilaridadePorVertice(get.getId(), key, get.getSourceId()) == null)||(si.getArraySimilaridadePorVertice(get.getId(), key, get.getSourceId()).size() == 0)) {
                            Iterator itr = si.getMapaArestas().get(key).keySet().iterator();
                            int pos = 0;
                            while (itr.hasNext()) {
                                  if (pos == posicao) {
                                    itr.remove();
                                    break;
                                }
                                pos++;

                            }

                        }

                    }**/

                }

            }

        }
        
        //tirar os vazios. Tentar
        
       /** for (Map.Entry<Integer, ArrayList<Vertice>> entrySet : si.getMapaArestas()) {
            Integer key1 = entrySet.getKey();
            ArrayList<Vertice> value1 = entrySet.getValue();
            
        }**/
        
     /**   for (int k = 0; k < value.size(); k++) {
            Vertice get = value.get(k);
            int posicao = this.si.getVerticeOrigemOrdenado(key, get.getId(), get.getSourceId());
             if (posicao >=0) {
            if ((this.si.getArraySimilaridadePorVertice2(get.getId(), key, get.getSourceId()) == null)||(this.si.getArraySimilaridadePorVertice2(get.getId(), key, get.getSourceId()).size() == 0)) {
                            Iterator itr = this.si.getMapaArestas().get(key).keySet().iterator();
                            int pos = 0;
                            while (itr.hasNext()) {
                           Object element = itr.next();
                                  if (pos == posicao) {
                                    itr.remove();
                                    break;
                                }
                                pos++;

                            }

                        }

            
        }
        }**/
        
        for (int k = 0; k < value.size(); k++) {
            Vertice get = value.get(k);
            
            ArrayList <Aresta> aux= this.si.getArraySimilaridadePorVertice2(get.getId(), key, get.getSourceId());     
            if (si.getMapaArestas().containsKey(key)) {
                
            
            if ((aux == null)||(aux.size() == 0)) {
                            Iterator itr = this.si.getMapaArestas().get(key).keySet().iterator();
                            //int pos = 0;
                            while (itr.hasNext()) {
                           Vertice element = (Vertice)itr.next();
                                  if (element.getId().equals(get.getId())) {
                                    itr.remove();
                                    break;
                                }
                                //pos++;

                            }

                        }
        }

            
        }
        
        return this.si;

    }
    
        public SimilarityIndex deletaSIComVerticesSemArestas(SimilarityIndex si2) {

        for (Map.Entry<String, Map<Vertice, ArrayList<Aresta>>> entrySet : si2.getMapaArestas().entrySet()) {
            String key = entrySet.getKey();
            Map<Vertice, ArrayList<Aresta>> value = entrySet.getValue();
            for (Map.Entry<Vertice, ArrayList<Aresta>> entrySet1 : value.entrySet()) {
                Vertice key1 = entrySet1.getKey();
                ArrayList<Aresta> value1 = entrySet1.getValue();
                if (value == null || value1.isEmpty()) {
                    
                    //pode ser que n esteja ordenado as origens
                    int posicao = si2.getVerticeOrigemOrdenado(key, key1.getId(), key1.getSourceId());
                    if (posicao >= 0) {
                        //     if ((si2.getArraySimilaridadePorVertice2(key1.getId(), key, key1.getSourceId()) == null)||(this.si.getArraySimilaridadePorVertice2(get.getId(), key, get.getSourceId()).size() == 0)) {
                        Iterator itr = si2.getMapaArestas().get(key).keySet().iterator();
                        int pos = 0;
                        while (itr.hasNext()) {
                            Object element = itr.next();
                            if (pos == posicao) {
                                itr.remove();
                                break;
                            }
                            pos++;

                        }

                    }

                }

            }

        }
return si2;
    
}
    public SimilarityIndex deletaSIComVerticesSemArestas2(SimilarityIndex si2) {

        for (Map.Entry<String, Map<Vertice, ArrayList<Aresta>>> entrySet : si2.getMapaArestas().entrySet()) {
            String key = entrySet.getKey();
            
            Map<Vertice, ArrayList<Aresta>> value = entrySet.getValue();
           
                
            
            for (Map.Entry<Vertice, ArrayList<Aresta>> entrySet1 : value.entrySet()) {
                Vertice key1 = entrySet1.getKey();
                ArrayList<Aresta> value1 = entrySet1.getValue();
                if (value == null || value1.isEmpty()) {
                //  si2.getMapaArestas().get(key).re;
                   
                        //     if ((si2.getArraySimilaridadePorVertice2(key1.getId(), key, key1.getSourceId()) == null)||(this.si.getArraySimilaridadePorVertice2(get.getId(), key, get.getSourceId()).size() == 0)) {
                        Iterator itr = si2.getMapaArestas().get(key).keySet().iterator();
                
                        while (itr.hasNext()) {
                            Object element = itr.next();
                            if (element.equals(key1)) {
                                itr.remove();
                                break;
                            }
                          

                        }

                    

                }

           
        }

        }
return si2;
    
}

public void setMapaArestas( Map<Integer, ArrayList<Vertice>> mapaArestas) {
        this.mapaArestas = mapaArestas;
    }
    
     public ArrayList <Vertice> getVertices(Vertice v){
        if (this.mapaArestas.containsKey(v.getClusterId())) {
            ArrayList <Vertice> v2 = this.mapaArestas.get(v.getClusterId());
            if (v2!=null) {
                return v2;
                
            }
            else{
                return null;
            }
            
        }
        
        
        return null;
        
    }
    
    public void insereVertice(Vertice v){
        if (this.mapaArestas.containsKey(v.getClusterId())) {
            this.mapaArestas.get(v.getClusterId()).add(v);
            
        }
        else{
            ArrayList <Vertice> v2 = new ArrayList<Vertice>();
            v2.add(v);
            this.mapaArestas.put(v.getClusterId(), v2);
        }
        
        
        
    }
    public void insereVertice(ArrayList <Vertice> value, Vertice v){
         for (int i = 0; i < value.size(); i++) {
                Vertice get = value.get(i);
                if (get.getClusterId() == v.getClusterId()&&!v.getId().equals(get.getId())) {
                     this.insereVertice(get);
                }
               
                
            }
        
        
        
    }
    public void removeVertice(Vertice v){
        if (this.mapaArestas.containsKey(v.getClusterId())) {
            ArrayList <Vertice> vertice = this.mapaArestas.get(v.getClusterId());
            for (int i = 0; i < vertice.size(); i++) {
                Vertice get = vertice.get(i);
                if (get.getId().equals(v.getId())&& get.getSourceId().equals(v.getSourceId())) {
                    vertice.remove(i);
                    
                }
                
            }
            
        }
        
    }
      public void removeVerticeClusterErrado(int key){
        if (this.mapaArestas.containsKey(key)) {
            ArrayList <Vertice> vertice = this.mapaArestas.get(key);
            for (int i = 0; i < vertice.size(); i++) {
                Vertice get = vertice.get(i);
                if ( get.getClusterId()!=key) {
                    vertice.remove(i);
                    
                }
                
            }
            
        }
        
    }
}
