/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package classificacao;

import Index.BlockIndex;
import Index.SimilarityIndex;
import avaliacao.Precisao;
import de.hpi.fgis.dude.similarityfunction.contentbased.util.SmithWatermanDistance;
import graph.Aresta;
import graph.Vertice;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import uk.ac.shef.wit.simmetrics.similaritymetrics.AbstractStringMetric;
import uk.ac.shef.wit.simmetrics.similaritymetrics.BlockDistance;
import uk.ac.shef.wit.simmetrics.similaritymetrics.CosineSimilarity;
import uk.ac.shef.wit.simmetrics.similaritymetrics.EuclideanDistance;
import uk.ac.shef.wit.simmetrics.similaritymetrics.JaroWinkler;
import uk.ac.shef.wit.simmetrics.similaritymetrics.Levenshtein;
import uk.ac.shef.wit.simmetrics.similaritymetrics.MongeElkan;

/**
 *
 * @author Priscilla
 */
public class SingleLink {

    public long getTempoParaDiminuir() {
        return tempoParaDiminuir;
    }

    public void setTempoParaDiminuir(long tempoParaDiminuir) {
        this.tempoParaDiminuir = tempoParaDiminuir;
    }

    public int getTotalCerto() {
        return totalCerto;
    }

    public void setTotalCerto(int totalCerto) {
        this.totalCerto = totalCerto;
    }

    public int getTotalErrado() {
        return totalErrado;
    }

    public void setTotalErrado(int totalErrado) {
        this.totalErrado = totalErrado;
    }

    public int getControleClusterId() {
        return controleClusterId;
    }

    public void setControleClusterId(int controleClusterId) {
        this.controleClusterId = controleClusterId;
    }

    private SimilarityIndex si;
    private BlockIndex bi;
private int totalCerto = 0;
private int totalErrado = 0;
private long tempoParaDiminuir = 0;

    public long getTempoIncremental() {
        return tempoIncremental;
    }

    public void setTempoIncremental(long tempoIncremental) {
        this.tempoIncremental = tempoIncremental;
    }

    public long getTempoTradicional() {
        return tempoTradicional;
    }

    public void setTempoTradicional(long tempoTradicional) {
        this.tempoTradicional = tempoTradicional;
    }
private long tempoIncremental = 0;
private long tempoTradicional = 0;
    public SimilarityIndex getSi() {
        return si;
    }

    public void setSi(SimilarityIndex si) {
        this.si = si;
    }

    public BlockIndex getBi() {
        return bi;
    }

    public void setBi(BlockIndex bi) {
        this.bi = bi;
    }
    int controleClusterId;

    public SingleLink(SimilarityIndex si, BlockIndex bi) {

        this.si = si;
        this.bi = bi;
        this.controleClusterId = 0;
    }
    
      public BlockIndex inicializaClusterLocalIncrementalPorChave(BlockIndex bi2, String key) {
          
          ArrayList<Vertice> value = bi2.getMapaNomes().get(key);
          if (value!=null) {
                
            int clusterRecuperadoBI;
           
            for (int i = 0; i < value.size(); i++) {
                Vertice get = value.get(i);
                clusterRecuperadoBI = this.bi.getclusterId(get.getId(), key, get.getSourceId());
                if (clusterRecuperadoBI == -1) {
                    get.setClusterId(this.controleClusterId);
                    this.controleClusterId++;

                } else {
                    
                    get.setClusterId(clusterRecuperadoBI);
                }

            }
          }
   

         

        
        return bi2;
    }

    public BlockIndex inicializaClusterLocalIncremental(BlockIndex bi2) {
        for (Map.Entry<String, ArrayList<Vertice>> entrySet : bi2.getMapaNomes().entrySet()) {

            String key = entrySet.getKey();
            int clusterRecuperadoBI;
            ArrayList<Vertice> value = entrySet.getValue();
            for (int i = 0; i < value.size(); i++) {
                Vertice get = value.get(i);
                clusterRecuperadoBI = this.bi.getclusterId(get.getId(), key, get.getSourceId());
                if (clusterRecuperadoBI == -1) {
                    get.setClusterId(this.controleClusterId);
                    this.controleClusterId++;

                } else {
                    
                    get.setClusterId(clusterRecuperadoBI);
                }

            }

        }
        return bi2;
    }
    
    /**
     * 
     * @param bi2
     * @param key
     * @param v1
     * @return 
     */
    public BlockIndex inicializaClusterLocalIncrementalOrdenado(BlockIndex bi2) {
        //  bi2 = ordenaBI(bi2); chamei fora
        int total = 0;
        for (Map.Entry<String, ArrayList<Vertice>> entrySet : bi2.getMapaNomes().entrySet()) {
            String key = entrySet.getKey();
            ArrayList<Vertice> value = entrySet.getValue();

            Vertice v1 = null;
            

            System.out.println(" Tamanho bloco " + value.size());
            for (int i = 0; i < value.size(); i++) {

                Vertice get = value.get(i);
                v1 = bi.getclusterIdOrdenado(get, key);
                if (v1 != null) {//existe o cluster
                    get.setClusterId(v1.getClusterId());
                    total++;
                } else {

                    get.setClusterId(this.controleClusterId);
                    this.controleClusterId++;
                }
            }
        }
        System.out.println(" Total recuperado: " + total);
        return bi2;
    }

    public BlockIndex MudaElementosDeCluster(BlockIndex bi2, int clusterIdOrigem, int clusterIdDestino, String key) {

        ArrayList<Vertice> value = bi2.getMapaNomes().get(key);
        

        int clusterRecuperadoBI;

        for (int i = 0; i < value.size(); i++) {
            Vertice get = value.get(i);
            clusterRecuperadoBI = bi2.getclusterId(get.getId(), key, get.getSourceId());
            if (clusterRecuperadoBI == clusterIdOrigem) {
                get.setClusterId(clusterIdDestino);

            }

        }

        return bi2;
    }
    /**
     * alterando o SI apagando o que não será usado para a base febrl
     * @param bi2
     * @param clusterIdOrigem
     * @param clusterIdDestino
     * @param key
     * @return 
     */
    public BlockIndex MudaElementosDeCluster2(Vertice verticeDestino,Vertice verticeOrigem, BlockIndex bi2, int clusterIdOrigem, int clusterIdDestino, String key, SimilarityIndex siNew) {

       ArrayList<Aresta> arestaOrigem = siNew.getArraySimilaridadePorVertice(verticeOrigem.getId(), key, verticeOrigem.getSourceId());
        ArrayList<Aresta> arestaDestino = siNew.getArraySimilaridadePorVertice(verticeDestino.getId(), key, verticeDestino.getSourceId());
       // ArrayList<Vertice> value = bi2.getMapaNomes().get(key);
        
       
        ArrayList<Vertice> value = bi2.getMapaNomes().get(key);
        

        int clusterRecuperadoBI;

        for (int i = 0; i < value.size(); i++) {
            Vertice get = value.get(i);
            clusterRecuperadoBI = bi2.getclusterId(get.getId(), key, get.getSourceId());
            if (clusterRecuperadoBI == clusterIdOrigem) {
                get.setClusterId(clusterIdDestino);

            }

        }
           ArrayList <Vertice> listaVerticePorCluster = new ArrayList<Vertice>();

           for (int j = 0; j < value.size(); j++) {
            Vertice get = value.get(j);
            clusterRecuperadoBI = bi2.getclusterId(get.getId(), key, get.getSourceId());
            if (clusterRecuperadoBI == clusterIdDestino) {
                  listaVerticePorCluster.add(get);

            }

        }
           for (int i = 0; i < listaVerticePorCluster.size(); i++) {
            Vertice get = listaVerticePorCluster.get(i);
            ArrayList<Aresta> arestaPorElemento = siNew.getArraySimilaridadePorVertice(get.getId(), key, get.getSourceId());
            //para cada vertice recupera a lista de arestas
               for (int j = 0; j < arestaPorElemento.size(); j++) {
                   Aresta get1 = arestaPorElemento.get(j);
                   for (int k = 0; k < listaVerticePorCluster.size(); k++) {
                       Vertice get2 = listaVerticePorCluster.get(k);
                        if (get1.getDestino().getId().equalsIgnoreCase(get2.getId())) {
                       System.err.println(" &&&&&&&&&&&&&&&&&&&&&&&&&&");
                       siNew.removeAresta(key, get1.getOrigem(), get1.getDestino().getId(), key);
                       si.removeAresta(key, get1.getOrigem(), get1.getDestino().getId(), key);
                   }
                   }
                  
                   
               }
            
        }

        //return bi2;
              
        
        
        return bi2;
    }
    
       public void MudaElementosDeClusterMerge(int clusterIdOrigem, int clusterIdDestino, String key) {

        ArrayList<Vertice> value = this.bi.getMapaNomes().get(key);
           if (value!=null) {
                int clusterRecuperadoBI;
           
        for (int i = 0; i < value.size(); i++) {
            Vertice get = value.get(i);
            clusterRecuperadoBI = bi.getclusterId(get.getId(), key, get.getSourceId());
            if (clusterRecuperadoBI == clusterIdOrigem) {
                get.setClusterId(clusterIdDestino);
                
               // alterei aqui
                bi.insertVertice(key, get);

            }

        }
               
           }

       
          

    }

    private void copiaBlocoFinalToClusterGlobal(BlockIndex bi2, String key2) {

        ArrayList<Vertice> value = bi2.getMapaNomes().get(key2);
        for (int i = 0; i < value.size(); i++) {
            Vertice get = value.get(i);
            bi.insertVertice(key2, get);

        }

    }

      private void preProcessamentoAmostraIncremental(BlockIndex bi2, String key2) {

        ArrayList<Vertice> value = bi2.getMapaNomes().get(key2);
        for (int i = 0; i < value.size(); i++) {
            Vertice get = value.get(i);
            bi.insertVertice(key2, get);

        }

    }
      
     public SimilarityIndex singleLinkSINotNull(BlockIndex bi2) throws IOException {
        //verifica quais do BI2 estão armazenados no BI oficial e copia cluster
       
        SimilarityIndex siNotNull = new SimilarityIndex();

        //recupera os clusters
       // bi2 = inicializaClusterLocalIncremental(bi2);

        //clusteriza as instancias de bi2 que estao sem cluster. Acessa SI ou atualiza SI
        for (Map.Entry<String, ArrayList<Vertice>> entrySet : bi2.getMapaNomes().entrySet()) { //pra cada bloco
            String key = entrySet.getKey();
           

            ArrayList<Vertice> value = entrySet.getValue();
                
                Aresta arestaAux = null;
               

                for (int i = 0; i < value.size(); i++) {//pra o array de cada bloco da entrada
                    // System.err.println(" teste  ++++++++++++++" + controleApagar);
                    //Cluster cluster = new Cluster(si);
                    Vertice get1 = value.get(i);
                    for (int j = i + 1; j < value.size(); j++) {

                        //descobrir por instancia sem bloco a instancia de maior similaridade
                        Vertice get2 = value.get(j);
                        if (get2.getClusterId() != get1.getClusterId()) {
                            
                            System.out.println(" ****************************************8Teste 1");
                            arestaAux = this.si.getAresta(key, get1, get2.getId(), get2.getSourceId());
                            //se retornar null é pq n existe a aresta em si. N tem similaridade medida previamente
                            if (arestaAux != null){
                                                            System.out.println(" ****************************************8Teste 2");

                                double similaridadeRecuperada = arestaAux.getSimilaridade();
                                siNotNull.verificaSimilaridade(similaridadeRecuperada, key, get1.getId(), get1.getSourceId(), get2.getId(), get2.getSourceId(), key);
                                
                            }
                          
                        }

                    }

                }

        }
        return siNotNull;

        
    }
     /**
      * //mudei pra tirar o si geral
      * Recuoera SI previous por bloco
      * @param bi2
      * @return
      * @throws IOException 
      */
      public SimilarityIndex singleLinkSINotNull(BlockIndex bi2, String bloco) throws IOException {
        //verifica quais do BI2 estão armazenados no BI oficial e copia cluster
       
        SimilarityIndex siNotNull = new SimilarityIndex();
        double limiar = 0.9;

        //recupera os clusters
       // bi2 = inicializaClusterLocalIncremental(bi2);

        //clusteriza as instancias de bi2 que estao sem cluster. Acessa SI ou atualiza SI
        for (Map.Entry<String, ArrayList<Vertice>> entrySet : bi2.getMapaNomes().entrySet()) { //pra cada bloco
            String key = entrySet.getKey();
            
            if (key.equalsIgnoreCase(bloco)) {            
            ArrayList<Vertice> value = entrySet.getValue();
                
                Aresta arestaAux = null;
               

                for (int i = 0; i < value.size(); i++) {//pra o dado array do bloco da entrada
                    // System.err.println(" teste  ++++++++++++++" + controleApagar);
                    //Cluster cluster = new Cluster(si);
                    Vertice get1 = value.get(i);
                    for (int j = i + 1; j < value.size(); j++) {

                        //descobrir por instancia sem bloco a instancia de maior similaridade
                        Vertice get2 = value.get(j);
                        if (get2.getClusterId() != get1.getClusterId()) {
                         arestaAux = this.si.getAresta(key, get1, get2.getId(), get2.getSourceId());
                            //se retornar null é pq n existe a aresta em si. N tem similaridade medida previamente
                        if (arestaAux != null){
                          double similaridadeRecuperada = arestaAux.getSimilaridade();
                          
                          //fazer a busca ordenada
                         siNotNull.verificaSimilaridade(similaridadeRecuperada, key, get1.getId(), get1.getSourceId(), get2.getId(), get2.getSourceId(), key);
                                
                         }else{                      
                            
                            //mede similaridade
                                // AbstractStringMetric metric = new Levenshtein();
                                
                                AbstractStringMetric metric = new Levenshtein();
                                float result1 = metric.getSimilarity(get1.getConteudoComparacao(), get2.getConteudoComparacao());
                                float result2 = metric.getSimilarity(get1.getConteudoComparacao2(), get2.getConteudoComparacao2());
                               // float result = (float) (result1 + result2) / 2;
                                float result = (float) (result1 + result2) / 2;
                                //siNew.verificaSimilaridade(result, key, get1.getId(), get1.getSourceId(), get2.getId(), get2.getSourceId(), key);
//float result = (float) ( result2) ;
                                if (result > limiar) {
                                    //coloquei aqui. Tirei de cima
                                  siNotNull.verificaSimilaridade(result, key, get1.getId(), get1.getSourceId(), get2.getId(), get2.getSourceId(), key);
                                  // long tempoInicialAux = System.currentTimeMillis(); 
                                  
                                  //insere e ordena o array
                                 si.verificaSimilaridade(result, key, get1.getId(), get1.getSourceId(), get2.getId(), get2.getSourceId(), key);   
                              //descontar tempo de ordenação
                                    //ordenaSIPorVerticeOrigem(key, get1);
                                //  this.tempoParaDiminuir = this.tempoParaDiminuir+(System.currentTimeMillis()-tempoInicialAux );
                                }

                          } 
                            
                          
                        }

                   }

             }
        }

        }
        return siNotNull;

        
    }
      
      /**
      * //mudei pra tirar o si geral
      * Recuoera SI previous por bloco
      * @param bi2
      * @return
      * @throws IOException 
      */
      public SimilarityIndex singleLinkSINotNullOrdenadoCora(BlockIndex bi2) throws IOException {
        //verifica quais do BI2 estão armazenados no BI oficial e copia cluster
       
        SimilarityIndex siNotNull = new SimilarityIndex();
        double limiar = 0.9;

        //recupera os clusters
       // bi2 = inicializaClusterLocalIncremental(bi2);

        //clusteriza as instancias de bi2 que estao sem cluster. Acessa SI ou atualiza SI
        for (Map.Entry<String, ArrayList<Vertice>> entrySet : bi2.getMapaNomes().entrySet()) { //pra cada bloco
            String key = entrySet.getKey();
            
                       
            ArrayList<Vertice> value = entrySet.getValue();
                
                Aresta arestaAux = null;
               

                for (int i = 0; i < value.size(); i++) {//pra o dado array do bloco da entrada
                    // System.err.println(" teste  ++++++++++++++" + controleApagar);
                    //Cluster cluster = new Cluster(si);
                    Vertice get1 = value.get(i);
                    for (int j = i + 1; j < value.size(); j++) {

                        //descobrir por instancia sem bloco a instancia de maior similaridade
                        Vertice get2 = value.get(j);
                        if (get2.getClusterId() != get1.getClusterId()) {
                         arestaAux = this.si.getArestaOrdenada(key, get1, get2.getId(), get2.getSourceId());
                            //se retornar null é pq n existe a aresta em si. N tem similaridade medida previamente
                        if (arestaAux != null){
                          double similaridadeRecuperada = arestaAux.getSimilaridade();
                          
                          //fazer a busca ordenada
                         siNotNull.verificaSimilaridade(similaridadeRecuperada, key, get1.getId(), get1.getSourceId(), get2.getId(), get2.getSourceId(), key);
                                
                         }else{                      
                            
                            //mede similaridade
                                // AbstractStringMetric metric = new Levenshtein();
                                
                                AbstractStringMetric metric = new Levenshtein();
                                float result1 = metric.getSimilarity(get1.getConteudoComparacao(), get2.getConteudoComparacao());
                                float result2 = metric.getSimilarity(get1.getConteudoComparacao2(), get2.getConteudoComparacao2());
                               // float result = (float) (result1 + result2) / 2;
                               // float result = (float) (result1 + result2) / 2;
                                //siNew.verificaSimilaridade(result, key, get1.getId(), get1.getSourceId(), get2.getId(), get2.getSourceId(), key);
float result = (float) ( result2) ;
                                if (result > limiar) {
                                    //coloquei aqui. Tirei de cima
                                  siNotNull.verificaSimilaridade(result, key, get1.getId(), get1.getSourceId(), get2.getId(), get2.getSourceId(), key);
                                  // long tempoInicialAux = System.currentTimeMillis(); 
                                  
                                  //insere e ordena o array
                              //descontar tempo de ordenação
                                 
                                 long tempoInicialAux2 = System.currentTimeMillis();
                                   si.verificaSimilaridade(result, key, get1.getId(), get1.getSourceId(), get2.getId(), get2.getSourceId(), key);   
                                    ordenaSIPorVerticeOrigem(key, get1);      
                                    ordenaListaVerticeSI(key);

                                    this.tempoParaDiminuir = this.tempoParaDiminuir+(System.currentTimeMillis()-tempoInicialAux2);
                                //  this.tempoParaDiminuir = this.tempoParaDiminuir+(System.currentTimeMillis()-tempoInicialAux );
                                }

                          } 
                            
                          
                        }

                   }

             }
        

        }
        return siNotNull;

        
    }
       /**
      * //mudei pra tirar o si geral
      * Recuoera SI previous por bloco
      * @param bi2
      * @return
      * @throws IOException 
      */
      public SimilarityIndex singleLinkSINotNullOrdenadoFebrl(BlockIndex bi2) throws IOException {
        //verifica quais do BI2 estão armazenados no BI oficial e copia cluster
       
        SimilarityIndex siNotNull = new SimilarityIndex();
        double limiar = 0.9;
         long tempoInicialAux4 = 0;
        long tempoInicialAuxTradicional = System.currentTimeMillis();
        long tempoSubtrairTradicional = 0 ;

         int controle = 0;

        //recupera os clusters
       // bi2 = inicializaClusterLocalIncremental(bi2);

        //clusteriza as instancias de bi2 que estao sem cluster. Acessa SI ou atualiza SI
        for (Map.Entry<String, ArrayList<Vertice>> entrySet : bi2.getMapaNomes().entrySet()) { //pra cada bloco
            String key = entrySet.getKey();
            
                  
            ArrayList<Vertice> value = entrySet.getValue();
                
                Aresta arestaAux = null;
               

                for (int i = 0; i < value.size(); i++) {//pra o dado array do bloco da entrada
                    // System.err.println(" teste  ++++++++++++++" + controleApagar);
                    //Cluster cluster = new Cluster(si);
                    Vertice get1 = value.get(i);
                    for (int j = i + 1; j < value.size(); j++) {

                        //descobrir por instancia sem bloco a instancia de maior similaridade
                        Vertice get2 = value.get(j);
                        if (get2.getClusterId() != get1.getClusterId()) {
                            
                            long tempoInicialAux5 = System.currentTimeMillis();
                            
                            //alterei para recuperar certo do SI
                         //arestaAux = this.si.getArestaOrdenada(key, get1, get2.getId(), get2.getSourceId());
                            arestaAux = this.si.getArestaOrdenada3(key, get1, get2.getId(), get2.getSourceId());

                            
                         tempoInicialAux4 = tempoInicialAux4 + (System.currentTimeMillis()-tempoInicialAux5);
                            //se retornar null é pq n existe a aresta em si. N tem similaridade medida previamente
                        if (arestaAux != null){
                          double similaridadeRecuperada = arestaAux.getSimilaridade();
                          
                          //fazer a busca ordenada
                         siNotNull.verificaSimilaridade(similaridadeRecuperada, key, get1.getId(), get1.getSourceId(), get2.getId(), get2.getSourceId(), key);
                                
                         }else{                      
                            
                            //mede similaridade
                                // AbstractStringMetric metric = new Levenshtein();
                                
                                AbstractStringMetric metric = new Levenshtein();
                                float result1 = metric.getSimilarity(get1.getConteudoComparacao(), get2.getConteudoComparacao());
                                float result2 = metric.getSimilarity(get1.getConteudoComparacao2(), get2.getConteudoComparacao2());
                               // float result = (float) (result1 + result2) / 2;
                                float result = (float) (result1 + result2) / 2;
                                //siNew.verificaSimilaridade(result, key, get1.getId(), get1.getSourceId(), get2.getId(), get2.getSourceId(), key);
//float result = (float) ( result2) ;
                                if (result > limiar) {
                                    //coloquei aqui. Tirei de cima
                                  siNotNull.verificaSimilaridade(result, key, get1.getId(), get1.getSourceId(), get2.getId(), get2.getSourceId(), key);
                                  // long tempoInicialAux = System.currentTimeMillis(); 
                                  
                                 
                                 
                                 long tempoInicialAux2 = System.currentTimeMillis();
                                  //insere e ordena o array
                                 si.verificaSimilaridade(result, key, get1.getId(), get1.getSourceId(), get2.getId(), get2.getSourceId(), key);   
                                  //descontar tempo de ordenação
                                    ordenaSIPorVerticeOrigem(key, get1);
                                    ordenaListaVerticeSI(key);
                                    this.tempoParaDiminuir = this.tempoParaDiminuir+(System.currentTimeMillis()-tempoInicialAux2);
                                    tempoSubtrairTradicional = tempoSubtrairTradicional + (System.currentTimeMillis()-tempoInicialAux2);
                                //  this.tempoParaDiminuir = this.tempoParaDiminuir+(System.currentTimeMillis()-tempoInicialAux );
                                }

                          } 
                            
                          
                        }else{
                           controle++;
                        }

                   }

             }
        

        }
        this.tempoTradicional = (System.currentTimeMillis()-tempoInicialAuxTradicional)-tempoSubtrairTradicional;
          System.err.println(" Tempk para acessar SI: " + tempoInicialAux4 +" tamanho de dados em cluster igual " + controle);
          if (controle>0) {
              
              System.out.println(" &&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&");
              
          }
        return siNotNull;

        
    }
    
     
    /**
      * //mudei pra tirar o si geral Recuoera SI previous por bloco
     *
     * @param bi2
     * @return
     * @throws IOException
     */
    public SimilarityIndex singleLinkSINotNullOrdenadoFebrl2(BlockIndex bi2) throws IOException {
        //verifica quais do BI2 estão armazenados no BI oficial e copia cluster

        SimilarityIndex siNotNull = new SimilarityIndex();
        double limiar = 0.9;
        long tempoInicialAux4 = 0;
        long tempoInicialAuxTradicional = System.currentTimeMillis();
        long tempoSubtrairTradicional = 0;

        int controle = 0;

        //recupera os clusters
        // bi2 = inicializaClusterLocalIncremental(bi2);
        //clusteriza as instancias de bi2 que estao sem cluster. Acessa SI ou atualiza SI
        for (Map.Entry<String, ArrayList<Vertice>> entrySet : bi2.getMapaNomes().entrySet()) { //pra cada bloco
            String key = entrySet.getKey();

            ArrayList<Vertice> value = entrySet.getValue();

            Aresta arestaAux = null;

            for (int i = 0; i < value.size(); i++) {//pra o dado array do bloco da entrada
                // System.err.println(" teste  ++++++++++++++" + controleApagar);
                //Cluster cluster = new Cluster(si);
                Vertice get1 = value.get(i);
                ArrayList<Aresta> arestaPorVertice = si.getArraySimilaridadePorVertice2(get1.getId(), key, get1.getSourceId());
                for (int j = i + 1; j < value.size(); j++) {

                    //descobrir por instancia sem bloco a instancia de maior similaridade
                    Vertice get2 = value.get(j);
                    if (get2.getClusterId() != get1.getClusterId()) {

                        long tempoInicialAux5 = System.currentTimeMillis();

                            //alterei para recuperar certo do SI
                        //arestaAux = this.si.getArestaOrdenada(key, get1, get2.getId(), get2.getSourceId());
                        arestaAux = this.si.getArestaOrdenada4(key, get1, get2.getId(), get2.getSourceId(), arestaPorVertice);

                        tempoInicialAux4 = tempoInicialAux4 + (System.currentTimeMillis() - tempoInicialAux5);
                        //se retornar null é pq n existe a aresta em si. N tem similaridade medida previamente
                        if (arestaAux != null) {
                            double similaridadeRecuperada = arestaAux.getSimilaridade();

                            //fazer a busca ordenada
                            siNotNull.verificaSimilaridade(similaridadeRecuperada, key, get1.getId(), get1.getSourceId(), get2.getId(), get2.getSourceId(), key);

                        } else {

                            //mede similaridade
                            // AbstractStringMetric metric = new Levenshtein();
                            AbstractStringMetric metric = new Levenshtein();
                            float result1 = metric.getSimilarity(get1.getConteudoComparacao(), get2.getConteudoComparacao());
                            float result2 = metric.getSimilarity(get1.getConteudoComparacao2(), get2.getConteudoComparacao2());
                            // float result = (float) (result1 + result2) / 2;
                            float result = (float) (result1 + result2) / 2;
                            //siNew.verificaSimilaridade(result, key, get1.getId(), get1.getSourceId(), get2.getId(), get2.getSourceId(), key);
//float result = (float) ( result2) ;
                            if (result > limiar) {
                                //coloquei aqui. Tirei de cima
                                siNotNull.verificaSimilaridade(result, key, get1.getId(), get1.getSourceId(), get2.getId(), get2.getSourceId(), key);
                                  // long tempoInicialAux = System.currentTimeMillis(); 

                                long tempoInicialAux2 = System.currentTimeMillis();
                                //insere e ordena o array
                                si.verificaSimilaridade(result, key, get1.getId(), get1.getSourceId(), get2.getId(), get2.getSourceId(), key);
                                //descontar tempo de ordenação
                                ordenaSIPorVerticeOrigem(key, get1);
                                ordenaListaVerticeSI(key);
                                this.tempoParaDiminuir = this.tempoParaDiminuir + (System.currentTimeMillis() - tempoInicialAux2);
                                tempoSubtrairTradicional = tempoSubtrairTradicional + (System.currentTimeMillis() - tempoInicialAux2);
                                //  this.tempoParaDiminuir = this.tempoParaDiminuir+(System.currentTimeMillis()-tempoInicialAux );
                            }

                        }

                    } else {
                        controle++;
                    }

                }

            }

        }
        this.tempoTradicional = (System.currentTimeMillis() - tempoInicialAuxTradicional) - tempoSubtrairTradicional;
        System.err.println(" Tempk para acessar SI: " + tempoInicialAux4 + " tamanho de dados em cluster igual " + controle);
        if (controle > 0) {

            System.out.println(" &&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&");

        }
        return siNotNull;

    }

    public void singleLinkIngenuo(BlockIndex bi2) throws IOException {
        //verifica quais do BI2 estão armazenados no BI oficial e copia cluster
        Precisao precisao = new Precisao();
        precisao.InicializaGoldStandardCD();
        SimilarityIndex siNew = new SimilarityIndex();

        //recupera os clusters
        bi2 = inicializaClusterLocalIncremental(bi2);

        if (!this.si.getMapaArestas().isEmpty()) {
            siNew = singleLinkSINotNull(bi2);

        }

        //clusteriza as instancias de bi2 que estao sem cluster. Acessa SI ou atualiza SI
        for (Map.Entry<String, ArrayList<Vertice>> entrySet : bi2.getMapaNomes().entrySet()) { //pra cada bloco
            String key = entrySet.getKey();
            int clusterRecuperadoBI;

            ArrayList<Vertice> value = entrySet.getValue();

            boolean continua = true;

            while (continua) {

                Aresta arestaMax = null;
                Aresta arestaAux = null;
                double maxSimilaridade = 0;
                double limiar = 0.9; //limiar

                for (int i = 0; i < value.size(); i++) {//pra o array de cada bloco da entrada
                    // System.err.println(" teste  ++++++++++++++" + controleApagar);
                    //Cluster cluster = new Cluster(si);
                    Vertice get1 = value.get(i);
                    for (int j = i + 1; j < value.size(); j++) {

                        //descobrir por instancia sem bloco a instancia de maior similaridade
                        Vertice get2 = value.get(j);
                        if (get2.getClusterId() != get1.getClusterId()) {
                           
                          //  arestaAux = si.getAresta(key, get1, get2.getId(), get2.getSourceId());
                             arestaAux = siNew.getAresta(key, get1, get2.getId(), get2.getSourceId());
                            
                            //se retornar null é pq n existe a aresta em si. N tem similaridade medida previamente
                            if (arestaAux == null) { //mede similaridade
                                // AbstractStringMetric metric = new Levenshtein();
                                AbstractStringMetric metric = new Levenshtein();
                                float result1 = metric.getSimilarity(get1.getConteudoComparacao(), get2.getConteudoComparacao());
                                float result2 = metric.getSimilarity(get1.getConteudoComparacao2(), get2.getConteudoComparacao2());
                                float result = (float) (result1 + result2) / 2;
                                //float result = (float) (result1 + result2) / 2;
                                siNew.verificaSimilaridade(result, key, get1.getId(), get1.getSourceId(), get2.getId(), get2.getSourceId(), key);
                                si.verificaSimilaridade(result, key, get1.getId(), get1.getSourceId(), get2.getId(), get2.getSourceId(), key);

                                if (result > limiar) {

                                    if (result > maxSimilaridade) {
                                        maxSimilaridade = result;
                                        //System.err.print(" Teste agraaa +++++  ");
                                       
                                      //  arestaMax = this.si.getAresta(key, get1, get2.getId(), get2.getSourceId());
                                            arestaMax = siNew.getAresta(key, get1, get2.getId(), get2.getSourceId());
                                        
                                        //System.err.println(" teste continua  " + arestaMax.getDestino().getId());

                                    }

                                }

                            } else {

                                double similaridadeRecuperada = arestaAux.getSimilaridade();
                                if (similaridadeRecuperada > limiar && similaridadeRecuperada > maxSimilaridade) {
                                    arestaMax = arestaAux;
                                    maxSimilaridade = similaridadeRecuperada;
                                    //tem que recuperar a similaridade no SI

                                }

                            }
                          
                        }

                    }

                }

                if (maxSimilaridade > limiar && arestaMax != null) {

                    int clusterRecuperadoDestino = bi2.getclusterId(arestaMax.getDestino().getId(), key, arestaMax.getDestino().getSourceId());
                    int clusterRecuperadoOrigem = bi2.getclusterId(arestaMax.getOrigem().getId(), key, arestaMax.getOrigem().getSourceId());
                    arestaMax.getOrigem().setClusterId(clusterRecuperadoDestino);
                    MudaElementosDeCluster(bi2, clusterRecuperadoOrigem, clusterRecuperadoDestino, key);
                 
                } else {
                    continua = false;
                }
            }

            //Finaliza em um bloco
            copiaBlocoFinalToClusterGlobal(bi2, key);

        }

        precisao.clusterToPair(bi2);
   System.out.println("Precisao " + precisao.getPrecisao());
  System.out.println("Cobertura " + precisao.getCobertura());
   System.out.println("F-Measure " + (2 * ((precisao.getCobertura() * precisao.getPrecisao()) / (precisao.getPrecisao() + precisao.getCobertura()))));
this.totalCerto = precisao.getCerto();
this.totalErrado = precisao.getErrado();
    }
    
    //Ara o caso tradicional n contar tempo de acesso as estruturas
    public void singleLinkIngenuo2(BlockIndex bi2) throws IOException {
        //verifica quais do BI2 estão armazenados no BI oficial e copia cluster
        Precisao precisao = new Precisao();
        precisao.InicializaGoldStandardCD();
        SimilarityIndex siNew= new SimilarityIndex();

        //recupera os clusters
        bi2 = inicializaClusterLocalIncremental(bi2);
        
        if (!this.si.getMapaArestas().isEmpty()) {
            siNew = singleLinkSINotNull(bi2);
            
        }

        //clusteriza as instancias de bi2 que estao sem cluster. Acessa SI ou atualiza SI
        for (Map.Entry<String, ArrayList<Vertice>> entrySet : bi2.getMapaNomes().entrySet()) { //pra cada bloco
            String key = entrySet.getKey();
            int clusterRecuperadoBI;

            ArrayList<Vertice> value = entrySet.getValue();

            boolean continua = true;

            while (continua) {

                Aresta arestaMax = null;
                Aresta arestaAux = null;
                double maxSimilaridade = 0;
                double limiar = 0.9; //limiar

                for (int i = 0; i < value.size(); i++) {//pra o array de cada bloco da entrada
                    // System.err.println(" teste  ++++++++++++++" + controleApagar);
                    //Cluster cluster = new Cluster(si);
                    Vertice get1 = value.get(i);
                    for (int j = i + 1; j < value.size(); j++) {

                        //descobrir por instancia sem bloco a instancia de maior similaridade
                        Vertice get2 = value.get(j);
                        if (get2.getClusterId() != get1.getClusterId()) {
                           
                          //  arestaAux = si.getAresta(key, get1, get2.getId(), get2.getSourceId());
                             arestaAux = siNew.getAresta(key, get1, get2.getId(), get2.getSourceId());
                            
                            //se retornar null é pq n existe a aresta em si. N tem similaridade medida previamente
                            if (arestaAux == null) { //mede similaridade
                                // AbstractStringMetric metric = new Levenshtein();
                                AbstractStringMetric metric = new Levenshtein();
                                float result1 = metric.getSimilarity(get1.getConteudoComparacao(), get2.getConteudoComparacao());
                                float result2 = metric.getSimilarity(get1.getConteudoComparacao2(), get2.getConteudoComparacao2());
                                float result = (float) (result1 + result2) / 2;
                                //float result = (float) (result1 + result2) / 2;
                                siNew.verificaSimilaridade(result, key, get1.getId(), get1.getSourceId(), get2.getId(), get2.getSourceId(), key);
                               // si.verificaSimilaridade(result, key, get1.getId(), get1.getSourceId(), get2.getId(), get2.getSourceId(), key);

                                if (result > limiar) {

                                    if (result > maxSimilaridade) {
                                        maxSimilaridade = result;
                                        //System.err.print(" Teste agraaa +++++  ");
                                       
                                      //  arestaMax = this.si.getAresta(key, get1, get2.getId(), get2.getSourceId());
                                            arestaMax = siNew.getAresta(key, get1, get2.getId(), get2.getSourceId());
                                        
                                        //System.err.println(" teste continua  " + arestaMax.getDestino().getId());

                                    }

                                }

                            } else {

                                double similaridadeRecuperada = arestaAux.getSimilaridade();
                                if (similaridadeRecuperada > limiar && similaridadeRecuperada > maxSimilaridade) {
                                    arestaMax = arestaAux;
                                    maxSimilaridade = similaridadeRecuperada;
                                    //tem que recuperar a similaridade no SI

                                }

                            }
                          
                        }

                    }

                }

                if (maxSimilaridade > limiar && arestaMax != null) {

                    int clusterRecuperadoDestino = bi2.getclusterId(arestaMax.getDestino().getId(), key, arestaMax.getDestino().getSourceId());
                    int clusterRecuperadoOrigem = bi2.getclusterId(arestaMax.getOrigem().getId(), key, arestaMax.getOrigem().getSourceId());
                    arestaMax.getOrigem().setClusterId(clusterRecuperadoDestino);
                    MudaElementosDeCluster(bi2, clusterRecuperadoOrigem, clusterRecuperadoDestino, key);
                 
                } else {
                    continua = false;
                }
            }

            //Finaliza em um bloco
            copiaBlocoFinalToClusterGlobal(bi2, key);

        }

        precisao.clusterToPair(bi2);
  // System.out.println("Precisao " + precisao.getPrecisao());
  //System.out.println("Cobertura " + precisao.getCobertura());
  // System.out.println("F-Measure " + (2 * ((precisao.getCobertura() * precisao.getPrecisao()) / (precisao.getPrecisao() + precisao.getCobertura()))));
this.totalCerto = precisao.getCerto();
this.totalErrado = precisao.getErrado();
    }
    
     public void singleLink(BlockIndex bi2) throws IOException {
        //verifica quais do BI2 estão armazenados no BI oficial e copia cluster
        Precisao precisao = new Precisao();
        precisao.InicializaGoldStandardCD();
        SimilarityIndex siNew= new SimilarityIndex();

        //recupera os clusters
        bi2 = inicializaClusterLocalIncremental(bi2);
        
        if (!this.si.getMapaArestas().isEmpty()) {
            System.out.println(" " + si.getTamanho());
            siNew = singleLinkSINotNull(bi2);
            
        }
        

        //clusteriza as instancias de bi2 que estao sem cluster. Acessa SI ou atualiza SI
        for (Map.Entry<String, ArrayList<Vertice>> entrySet : bi2.getMapaNomes().entrySet()) { //pra cada bloco
            System.out.println(""+ " Por Bloco");
            String key = entrySet.getKey();
            int clusterRecuperadoBI;

            ArrayList<Vertice> value = entrySet.getValue();

            boolean continua = true;

            while (continua) {

                Aresta arestaMax = null;
                Aresta arestaAux = null;
                double maxSimilaridade = 0;
                double limiar = 0.9; //limiar

                for (int i = 0; i < value.size(); i++) {//pra o array de cada bloco da entrada
                    // System.err.println(" teste  ++++++++++++++" + controleApagar);
                    //Cluster cluster = new Cluster(si);
                    Vertice get1 = value.get(i);
                    for (int j = i + 1; j < value.size(); j++) {

                        //descobrir por instancia sem bloco a instancia de maior similaridade
                        Vertice get2 = value.get(j);
                        if (get2.getClusterId() != get1.getClusterId()) {
                           
                          //  arestaAux = si.getAresta(key, get1, get2.getId(), get2.getSourceId());
                             arestaAux = siNew.getAresta(key, get1, get2.getId(), get2.getSourceId());
                            
                            //se retornar null é pq n existe a aresta em si. N tem similaridade medida previamente
                            if (arestaAux == null) { //mede similaridade
                                // AbstractStringMetric metric = new Levenshtein();
                                AbstractStringMetric metric = new Levenshtein();
                                float result1 = metric.getSimilarity(get1.getConteudoComparacao(), get2.getConteudoComparacao());
                                float result2 = metric.getSimilarity(get1.getConteudoComparacao2(), get2.getConteudoComparacao2());
                                float result = (float) (result1 + result2) / 2;
                                //float result = (float) (result1 + result2) / 2;
                                siNew.verificaSimilaridade(result, key, get1.getId(), get1.getSourceId(), get2.getId(), get2.getSourceId(), key);

                                if (result > limiar) {
                                    si.verificaSimilaridade(result, key, get1.getId(), get1.getSourceId(), get2.getId(), get2.getSourceId(), key);

                                    if (result > maxSimilaridade) {
                                        maxSimilaridade = result;
                                        //System.err.print(" Teste agraaa +++++  ");
                                       
                                      //  arestaMax = this.si.getAresta(key, get1, get2.getId(), get2.getSourceId());
                                            arestaMax = siNew.getAresta(key, get1, get2.getId(), get2.getSourceId());
                                        
                                        //System.err.println(" teste continua  " + arestaMax.getDestino().getId());

                                    }

                                }

                            } else {

                                double similaridadeRecuperada = arestaAux.getSimilaridade();
                                if (similaridadeRecuperada > limiar && similaridadeRecuperada > maxSimilaridade) {
                                    arestaMax = arestaAux;
                                    maxSimilaridade = similaridadeRecuperada;
                                    //tem que recuperar a similaridade no SI

                                }

                            }
                          
                        }

                    }

                }

                if (maxSimilaridade > limiar && arestaMax != null) {

                    int clusterRecuperadoDestino = bi2.getclusterId(arestaMax.getDestino().getId(), key, arestaMax.getDestino().getSourceId());
                    int clusterRecuperadoOrigem = bi2.getclusterId(arestaMax.getOrigem().getId(), key, arestaMax.getOrigem().getSourceId());
                    arestaMax.getOrigem().setClusterId(clusterRecuperadoDestino);
                    MudaElementosDeCluster(bi2, clusterRecuperadoOrigem, clusterRecuperadoDestino, key);
                 
                } else {
                    continua = false;
                }
            }

            //Finaliza em um bloco
            copiaBlocoFinalToClusterGlobal(bi2, key);

        }

        precisao.clusterToPair(bi2);
   System.out.println("Precisao " + precisao.getPrecisao());
  System.out.println("Cobertura " + precisao.getCobertura());
   System.out.println("F-Measure " + (2 * ((precisao.getCobertura() * precisao.getPrecisao()) / (precisao.getPrecisao() + precisao.getCobertura()))));
this.totalCerto = precisao.getCerto();


 System.out.println(" Certo " + precisao.getCerto());
this.totalErrado = precisao.getErrado();
      System.out.println("Errado " + precisao.getErrado());


        System.out.println(" tamanho SI " + siNew.getTamanho());

    }
     /**
      * Sem se preocuar com o tamanho do SI que deu problem com a base Febrl
      * @param bi2
      * @throws IOException 
      */
     public void singleLinkFebrl(BlockIndex bi2) throws IOException {
        //verifica quais do BI2 estão armazenados no BI oficial e copia cluster
        Precisao precisao = new Precisao();
        precisao.InicializaGoldStandardFebrl();
        SimilarityIndex siNew= new SimilarityIndex();

        //recupera os clusters
        bi2 = inicializaClusterLocalIncremental(bi2);
        System.out.println(" Temanho: " + bi2.getNumeroElementos() + "numero Blocos: " + bi2.getNumeroBlocos());
        
        if (!this.si.getMapaArestas().isEmpty()) {
            System.out.println(" " + si.getTamanho());
            siNew = singleLinkSINotNull(bi2);
            
        }
        

        //clusteriza as instancias de bi2 que estao sem cluster. Acessa SI ou atualiza SI
        for (Map.Entry<String, ArrayList<Vertice>> entrySet : bi2.getMapaNomes().entrySet()) { //pra cada bloco
            String key = entrySet.getKey();
            int clusterRecuperadoBI;

            ArrayList<Vertice> value = entrySet.getValue();

            boolean continua = true;

            while (continua) {

                Aresta arestaMax = null;
                Aresta arestaAux = null;
                double maxSimilaridade = 0;
                double limiar = 0.9; //limiar

                for (int i = 0; i < value.size(); i++) {//pra o array de cada bloco da entrada
                    // System.err.println(" teste  ++++++++++++++" + controleApagar);
                    //Cluster cluster = new Cluster(si);
                    Vertice get1 = value.get(i);
                    for (int j = i + 1; j < value.size(); j++) {

                        //descobrir por instancia sem bloco a instancia de maior similaridade
                        Vertice get2 = value.get(j);
                        if (get2.getClusterId() != get1.getClusterId()) {
                           
                          //  arestaAux = si.getAresta(key, get1, get2.getId(), get2.getSourceId());
                             arestaAux = siNew.getAresta(key, get1, get2.getId(), get2.getSourceId());
                             
                            
                            //se retornar null é pq n existe a aresta em si. N tem similaridade medida previamente
                            if (arestaAux == null) { //mede similaridade
                                // AbstractStringMetric metric = new Levenshtein();
                                
                                AbstractStringMetric metric = new Levenshtein();
                                float result1 = metric.getSimilarity(get1.getConteudoComparacao(), get2.getConteudoComparacao());
                                float result2 = metric.getSimilarity(get1.getConteudoComparacao2(), get2.getConteudoComparacao2());
                                float result = (float) (result1 + result2) / 2;
                                //float result = (float) (result1 + result2) / 2;
                                
                                siNew.verificaSimilaridade(result, key, get1.getId(), get1.getSourceId(), get2.getId(), get2.getSourceId(), key);

                                if (result > limiar) {
                                    //coloquei aqui. Tirei de cima
                                // siNew.verificaSimilaridade(result, key, get1.getId(), get1.getSourceId(), get2.getId(), get2.getSourceId(), key);

                                    si.verificaSimilaridade(result, key, get1.getId(), get1.getSourceId(), get2.getId(), get2.getSourceId(), key);

                                    if (result > maxSimilaridade) {
                                        maxSimilaridade = result;
                                        //System.err.print(" Teste agraaa +++++  ");
                                       
                                      //  arestaMax = this.si.getAresta(key, get1, get2.getId(), get2.getSourceId());
                                            arestaMax = siNew.getAresta(key, get1, get2.getId(), get2.getSourceId());
                                        
                                        //System.err.println(" teste continua  " + arestaMax.getDestino().getId());

                                    }

                                }

                            } else {

                                double similaridadeRecuperada = arestaAux.getSimilaridade();
                                if (similaridadeRecuperada > limiar && similaridadeRecuperada > maxSimilaridade) {
                                    arestaMax = arestaAux;
                                    maxSimilaridade = similaridadeRecuperada;
                                    //tem que recuperar a similaridade no SI

                                }

                            }
                          
                        }

                    }

                }

                if (maxSimilaridade > limiar && arestaMax != null) {

                    int clusterRecuperadoDestino = bi2.getclusterId(arestaMax.getDestino().getId(), key, arestaMax.getDestino().getSourceId());
                    int clusterRecuperadoOrigem = bi2.getclusterId(arestaMax.getOrigem().getId(), key, arestaMax.getOrigem().getSourceId());
                    arestaMax.getOrigem().setClusterId(clusterRecuperadoDestino);
                    //MudaElementosDeCluster2(arestaMax.getDestino(), arestaMax.getOrigem(),bi2, clusterRecuperadoOrigem, clusterRecuperadoDestino, key, siNew);
                    MudaElementosDeCluster(bi2, clusterRecuperadoOrigem, clusterRecuperadoDestino, key);

//para reduzir tamanho dos índices
                    //limpaSIDentroDeumMesmoCluster(siNew, si, clusterRecuperadoOrigem, clusterRecuperadoDestino);
                 
                } else {
                    continua = false;
                }
                
            }

            //Finaliza em um bloco
            copiaBlocoFinalToClusterGlobal(bi2, key);

        }

        precisao.clusterToPairFebrl(bi2);
   System.out.println("Precisao " + precisao.getPrecisao());
  System.out.println("Cobertura " + precisao.getCobertura());
   System.out.println("F-Measure " + (2 * ((precisao.getCobertura() * precisao.getPrecisao()) / (precisao.getPrecisao() + precisao.getCobertura()))));
this.totalCerto = precisao.getCerto();


 System.out.println(" Certo " + precisao.getCerto());
this.totalErrado = precisao.getErrado();
      System.out.println("Errado " + precisao.getErrado());


        System.out.println(" tamanho SI " + siNew.getTamanho());

    }
     
     /**
      * Faz a recuperacao do SI por bloco
      * @param bi2
      * @throws IOException 
      */
     public void singleLinkFebrl3(BlockIndex bi2) throws IOException {
        //verifica quais do BI2 estão armazenados no BI oficial e copia cluster
        Precisao precisao = new Precisao();
        precisao.InicializaGoldStandardFebrl();
       // SimilarityIndex siNew= new SimilarityIndex();

        //recupera os clusters
        bi2 = inicializaClusterLocalIncremental(bi2);
        System.out.println(" Temanho: " + bi2.getNumeroElementos() + "numero Blocos: " + bi2.getNumeroBlocos());
        
       // if (!this.si.getMapaArestas().isEmpty()) {
         //   System.out.println(" " + si.getTamanho());
           // siNew = singleLinkSINotNull(bi2);
            
        //}
        

        //clusteriza as instancias de bi2 que estao sem cluster. Acessa SI ou atualiza SI
        for (Map.Entry<String, ArrayList<Vertice>> entrySet : bi2.getMapaNomes().entrySet()) { //pra cada bloco
            String key = entrySet.getKey();
            SimilarityIndex siNew= new SimilarityIndex();
            siNew = singleLinkSINotNull(bi2, key);
            int clusterRecuperadoBI;

            ArrayList<Vertice> value = entrySet.getValue();

            boolean continua = true;

            while (continua) {

                Aresta arestaMax = null;
                Aresta arestaAux = null;
                double maxSimilaridade = 0;
                double limiar = 0.9; //limiar

                for (int i = 0; i < value.size(); i++) {//pra o array de cada bloco da entrada
                    // System.err.println(" teste  ++++++++++++++" + controleApagar);
                    //Cluster cluster = new Cluster(si);
                    Vertice get1 = value.get(i);
                    for (int j = i + 1; j < value.size(); j++) {

                        //descobrir por instancia sem bloco a instancia de maior similaridade
                        Vertice get2 = value.get(j);
                        if (get2.getClusterId() != get1.getClusterId()) {
                           
                          //  arestaAux = si.getAresta(key, get1, get2.getId(), get2.getSourceId());
                             arestaAux = siNew.getAresta(key, get1, get2.getId(), get2.getSourceId());
                             
                            
                            //se retornar null é pq n existe a aresta em si. N tem similaridade medida previamente
                            if (arestaAux == null) { //mede similaridade
                                // AbstractStringMetric metric = new Levenshtein();
                                
                                AbstractStringMetric metric = new Levenshtein();
                                float result1 = metric.getSimilarity(get1.getConteudoComparacao(), get2.getConteudoComparacao());
                                float result2 = metric.getSimilarity(get1.getConteudoComparacao2(), get2.getConteudoComparacao2());
                                float result = (float) (result1 + result2) / 2;
                                //float result = (float) (result1 + result2) / 2;
                                //siNew.verificaSimilaridade(result, key, get1.getId(), get1.getSourceId(), get2.getId(), get2.getSourceId(), key);

                                if (result > limiar) {
                                    //coloquei aqui. Tirei de cima
                                  siNew.verificaSimilaridade(result, key, get1.getId(), get1.getSourceId(), get2.getId(), get2.getSourceId(), key);

                                    si.verificaSimilaridade(result, key, get1.getId(), get1.getSourceId(), get2.getId(), get2.getSourceId(), key);

                                    if (result > maxSimilaridade) {
                                        maxSimilaridade = result;
                                        //System.err.print(" Teste agraaa +++++  ");
                                       
                                      //  arestaMax = this.si.getAresta(key, get1, get2.getId(), get2.getSourceId());
                                            arestaMax = siNew.getAresta(key, get1, get2.getId(), get2.getSourceId());
                                        
                                        //System.err.println(" teste continua  " + arestaMax.getDestino().getId());

                                    }

                                }

                            } else {

                                double similaridadeRecuperada = arestaAux.getSimilaridade();
                                if (similaridadeRecuperada > limiar && similaridadeRecuperada > maxSimilaridade) {
                                    arestaMax = arestaAux;
                                    maxSimilaridade = similaridadeRecuperada;
                                    //tem que recuperar a similaridade no SI

                                }

                            }
                          
                        }

                    }

                }

                if (maxSimilaridade > limiar && arestaMax != null) {

                    int clusterRecuperadoDestino = bi2.getclusterId(arestaMax.getDestino().getId(), key, arestaMax.getDestino().getSourceId());
                    int clusterRecuperadoOrigem = bi2.getclusterId(arestaMax.getOrigem().getId(), key, arestaMax.getOrigem().getSourceId());
                    arestaMax.getOrigem().setClusterId(clusterRecuperadoDestino);
                    //MudaElementosDeCluster2(arestaMax.getDestino(), arestaMax.getOrigem(),bi2, clusterRecuperadoOrigem, clusterRecuperadoDestino, key, siNew);
                    MudaElementosDeCluster(bi2, clusterRecuperadoOrigem, clusterRecuperadoDestino, key);

//para reduzir tamanho dos índices
                    //limpaSIDentroDeumMesmoCluster(siNew, si, clusterRecuperadoOrigem, clusterRecuperadoDestino);
                 
                } else {
                    continua = false;
                }
                
            }

            //Finaliza em um bloco
            copiaBlocoFinalToClusterGlobal(bi2, key);

        }

        precisao.clusterToPairFebrl(bi2);
   System.out.println("Precisao " + precisao.getPrecisao());
  System.out.println("Cobertura " + precisao.getCobertura());
   System.out.println("F-Measure " + (2 * ((precisao.getCobertura() * precisao.getPrecisao()) / (precisao.getPrecisao() + precisao.getCobertura()))));
this.totalCerto = precisao.getCerto();


 System.out.println(" Certo " + precisao.getCerto());
this.totalErrado = precisao.getErrado();
      System.out.println("Errado " + precisao.getErrado());


        //System.out.println(" tamanho SI " + siNew.getTamanho());

    }
     
     
       /**
      * Vai fazer por bloco sem usar ordenado
      * @param bi2
      * @throws IOException 
      */
     public void singleLinkFebrl4(BlockIndex bi2) throws IOException {
        //verifica quais do BI2 estão armazenados no BI oficial e copia cluster
        Precisao precisao = new Precisao();
        precisao.InicializaGoldStandardFebrl();
       // SimilarityIndex siNew= new SimilarityIndex();

        //recupera os clusters
        bi2 = inicializaClusterLocalIncremental(bi2);
        System.out.println(" Temanho: " + bi2.getNumeroElementos() + "numero Blocos: " + bi2.getNumeroBlocos());
        
       // if (!this.si.getMapaArestas().isEmpty()) {
         //   System.out.println(" " + si.getTamanho());
           // siNew = singleLinkSINotNull(bi2);
            
        //}
        

        //clusteriza as instancias de bi2 que estao sem cluster. Acessa SI ou atualiza SI
        for (Map.Entry<String, ArrayList<Vertice>> entrySet : bi2.getMapaNomes().entrySet()) { //pra cada bloco
            String key = entrySet.getKey();
            SimilarityIndex siNew= new SimilarityIndex();
            siNew = singleLinkSINotNull(bi2, key);
            int clusterRecuperadoBI;

            ArrayList<Vertice> value = entrySet.getValue();
            //preencher matriz sinew
            siNew = preencheSiNew(key,value,siNew);
            
            //ordenaSI
            //siNew = ordenaSi(siNew, key);

            boolean continua = true;

            while (continua) {

                Aresta arestaMax = null;
                Aresta arestaAux = null;
                double maxSimilaridade = 0;
                double limiar = 0.9; //limiar

                for (int i = 0; i < value.size(); i++) {//pra o array de cada bloco da entrada
                    // System.err.println(" teste  ++++++++++++++" + controleApagar);
                    //Cluster cluster = new Cluster(si);
                    Vertice get1 = value.get(i);
                    for (int j = i + 1; j < value.size(); j++) {

                        //descobrir por instancia sem bloco a instancia de maior similaridade
                        Vertice get2 = value.get(j);
                        if (get2.getClusterId() != get1.getClusterId()) {
                           
                          //  arestaAux = si.getAresta(key, get1, get2.getId(), get2.getSourceId());
                             arestaAux = siNew.getAresta(key, get1, get2.getId(), get2.getSourceId());
                             
                            
                            //se retornar null é pq n existe a aresta em si. N tem similaridade medida previamente
                            if (arestaAux != null) { //mede similaridade
                                 double similaridadeRecuperada = arestaAux.getSimilaridade();
                                if ( similaridadeRecuperada > maxSimilaridade) {
                                    arestaMax = arestaAux;
                                    maxSimilaridade = similaridadeRecuperada;
                                    //tem que recuperar a similaridade no SI

                                }
                                
                            } 
                          
                        }

                    }

                }

                if (maxSimilaridade > limiar && arestaMax != null) {

                    int clusterRecuperadoDestino = bi2.getclusterId(arestaMax.getDestino().getId(), key, arestaMax.getDestino().getSourceId());
                    int clusterRecuperadoOrigem = bi2.getclusterId(arestaMax.getOrigem().getId(), key, arestaMax.getOrigem().getSourceId());
                    arestaMax.getOrigem().setClusterId(clusterRecuperadoDestino);
                    //MudaElementosDeCluster2(arestaMax.getDestino(), arestaMax.getOrigem(),bi2, clusterRecuperadoOrigem, clusterRecuperadoDestino, key, siNew);
                    MudaElementosDeCluster(bi2, clusterRecuperadoOrigem, clusterRecuperadoDestino, key);

//para reduzir tamanho dos índices
                    //limpaSIDentroDeumMesmoCluster(siNew, si, clusterRecuperadoOrigem, clusterRecuperadoDestino);
                 
                } else {
                    continua = false;
                }
                
            }

            //Finaliza em um bloco
            copiaBlocoFinalToClusterGlobal(bi2, key);

        }

        precisao.clusterToPairFebrl(bi2);
   System.out.println("Precisao " + precisao.getPrecisao());
  System.out.println("Cobertura " + precisao.getCobertura());
   System.out.println("F-Measure " + (2 * ((precisao.getCobertura() * precisao.getPrecisao()) / (precisao.getPrecisao() + precisao.getCobertura()))));
this.totalCerto = precisao.getCerto();


 System.out.println(" Certo " + precisao.getCerto());
this.totalErrado = precisao.getErrado();
      System.out.println("Errado " + precisao.getErrado());


        //System.out.println(" tamanho SI " + siNew.getTamanho());

    }
    
     
     /**
      * Vai fazer por bloco  ordenado//apaguei SI geram
      * @param bi2
      * @throws IOException 
      */
     public void singleLinkFebrl5(BlockIndex bi2) throws IOException {
        //verifica quais do BI2 estão armazenados no BI oficial e copia cluster
        Precisao precisao = new Precisao();
        precisao.InicializaGoldStandardFebrl();
       // SimilarityIndex siNew= new SimilarityIndex();    //recupera os clusters
        bi2 = inicializaClusterLocalIncremental(bi2);
        System.out.println(" Temanho: " + bi2.getNumeroElementos() + "numero Blocos: " + bi2.getNumeroBlocos());
      

        //clusteriza as instancias de bi2 que estao sem cluster. Acessa SI ou atualiza SI
        for (Map.Entry<String, ArrayList<Vertice>> entrySet : bi2.getMapaNomes().entrySet()) { //pra cada bloco
            String key = entrySet.getKey();
            SimilarityIndex siNew= new SimilarityIndex();
            //Preenche a matriz
            siNew = singleLinkSINotNull(bi2, key);
            int clusterRecuperadoBI;

            ArrayList<Vertice> value = entrySet.getValue();
            //preencher matriz sinew
            //siNew = preencheSiNew(key,value,siNew);
            
            //ordenaSI
            
            siNew = ordenaSi(siNew, key);

            boolean continua = true;

            while (continua) {

                Aresta arestaMax = null;
                //Aresta arestaAux = null;
                double maxSimilaridade = 0;
                if (siNew.getMapaPorBloco(key)!=null) {
                    
               
               
                for (Map.Entry<Vertice, ArrayList<Aresta>> entrySet1 : siNew.getMapaPorBloco(key).entrySet()) {
                    Vertice key1 = entrySet1.getKey();
                     ArrayList<Aresta> value1 = entrySet1.getValue();
                     if (!value1.isEmpty()) {
                          Aresta aux = value1.get(0);
                    if (aux!=null) {
                        if (aux.getSimilaridade()>maxSimilaridade) {
                            maxSimilaridade = aux.getSimilaridade();
                            arestaMax = aux;
                            
                        }
                        
                    } 
                    }
                  
                }
            }
                if (arestaMax==null) {
                    continua=false;
                }
                else{
                    siNew.removeAresta(key, arestaMax.getOrigem(), arestaMax.getDestino().getId(), arestaMax.getDestino().getSourceId());
                 //verifica se é de clusters distintps
                    
                        
                    int clusterRecuperadoDestino = bi2.getclusterId(arestaMax.getDestino().getId(), key, arestaMax.getDestino().getSourceId());
                    int clusterRecuperadoOrigem = bi2.getclusterId(arestaMax.getOrigem().getId(), key, arestaMax.getOrigem().getSourceId());
                    if (clusterRecuperadoDestino!=clusterRecuperadoOrigem) {
                           arestaMax.getOrigem().setClusterId(clusterRecuperadoDestino);
                    //MudaElementosDeCluster2(arestaMax.getDestino(), arestaMax.getOrigem(),bi2, clusterRecuperadoOrigem, clusterRecuperadoDestino, key, siNew);
                    MudaElementosDeCluster(bi2, clusterRecuperadoOrigem, clusterRecuperadoDestino, key);
                        
                        
                    }
                 
                    
                }
                            
            }

            //Finaliza em um bloco
            copiaBlocoFinalToClusterGlobal(bi2, key);

        }

        precisao.clusterToPairFebrl(bi2);
   System.out.println("Precisao " + precisao.getPrecisao());
  System.out.println("Cobertura " + precisao.getCobertura());
   System.out.println("F-Measure " + (2 * ((precisao.getCobertura() * precisao.getPrecisao()) / (precisao.getPrecisao() + precisao.getCobertura()))));
this.totalCerto = precisao.getCerto();


 System.out.println(" Certo " + precisao.getCerto());
this.totalErrado = precisao.getErrado();
      System.out.println("Errado " + precisao.getErrado());


        //System.out.println(" tamanho SI " + siNew.getTamanho());

    }
    
      /**
      * Vai fazer por bloco  ordenado//apaguei SI geram
      * @param bi2
      * @throws IOException 
      */
     public void singleLinkCora5(BlockIndex bi2) throws IOException {
        //verifica quais do BI2 estão armazenados no BI oficial e copia cluster
        Precisao precisao = new Precisao();
        precisao.InicializaGoldStandardCora();
       // SimilarityIndex siNew= new SimilarityIndex();    //recupera os clusters
        bi2 = inicializaClusterLocalIncremental(bi2);
        System.out.println(" Temanho: " + bi2.getNumeroElementos() + "numero Blocos: " + bi2.getNumeroBlocos());
      

        //clusteriza as instancias de bi2 que estao sem cluster. Acessa SI ou atualiza SI
        for (Map.Entry<String, ArrayList<Vertice>> entrySet : bi2.getMapaNomes().entrySet()) { //pra cada bloco
            String key = entrySet.getKey();
            SimilarityIndex siNew= new SimilarityIndex();
            //Preenche a matriz
            siNew = singleLinkSINotNullOrdenadoCora(bi2);
            int clusterRecuperadoBI;

            ArrayList<Vertice> value = entrySet.getValue();
            //preencher matriz sinew
            //siNew = preencheSiNew(key,value,siNew);
            
            //ordenaSI
            
            siNew = ordenaSi(siNew, key);

            boolean continua = true;

            while (continua) {

                Aresta arestaMax = null;
                //Aresta arestaAux = null;
                double maxSimilaridade = 0;
                if (siNew.getMapaPorBloco(key)!=null) {
                    
               
               
                for (Map.Entry<Vertice, ArrayList<Aresta>> entrySet1 : siNew.getMapaPorBloco(key).entrySet()) {
                    Vertice key1 = entrySet1.getKey();
                     ArrayList<Aresta> value1 = entrySet1.getValue();
                     if (!value1.isEmpty()) {
                          Aresta aux = value1.get(0);
                    if (aux!=null) {
                        if (aux.getSimilaridade()>maxSimilaridade) {
                            maxSimilaridade = aux.getSimilaridade();
                            arestaMax = aux;
                            
                        }
                        
                    } 
                    }
                  
                }
            }
                if (arestaMax==null) {
                    continua=false;
                }
                else{
                    siNew.removeAresta(key, arestaMax.getOrigem(), arestaMax.getDestino().getId(), arestaMax.getDestino().getSourceId());
                 //verifica se é de clusters distintps
                    
                        
                    int clusterRecuperadoDestino = bi2.getclusterId(arestaMax.getDestino().getId(), key, arestaMax.getDestino().getSourceId());
                    int clusterRecuperadoOrigem = bi2.getclusterId(arestaMax.getOrigem().getId(), key, arestaMax.getOrigem().getSourceId());
                    if (clusterRecuperadoDestino!=clusterRecuperadoOrigem) {
                           arestaMax.getOrigem().setClusterId(clusterRecuperadoDestino);
                    //MudaElementosDeCluster2(arestaMax.getDestino(), arestaMax.getOrigem(),bi2, clusterRecuperadoOrigem, clusterRecuperadoDestino, key, siNew);
                    MudaElementosDeCluster(bi2, clusterRecuperadoOrigem, clusterRecuperadoDestino, key);
                        
                        
                    }
                 
                    
                }
                            
            }

            //Finaliza em um bloco
            copiaBlocoFinalToClusterGlobal(bi2, key);

        }

        precisao.clusterToPairCora(bi2);
   System.out.println("Precisao " + precisao.getPrecisao());
  System.out.println("Cobertura " + precisao.getCobertura());
   System.out.println("F-Measure " + (2 * ((precisao.getCobertura() * precisao.getPrecisao()) / (precisao.getPrecisao() + precisao.getCobertura()))));
this.totalCerto = precisao.getCerto();


 System.out.println(" Certo " + precisao.getCerto());
this.totalErrado = precisao.getErrado();
      System.out.println("Errado " + precisao.getErrado());


        //System.out.println(" tamanho SI " + siNew.getTamanho());

    }
     
        private BlockIndex ordenaBI(BlockIndex bi) {
            System.err.println("Tamanho BI ondenando " + bi.getNumeroElementos());

            for (Map.Entry<String, ArrayList<Vertice>> entrySet : bi.getMapaNomes().entrySet()) {
              String verticeKey = entrySet.getKey();
                Collections.sort (bi.getMapaNomes().get(verticeKey), new Comparator() {
            public int compare(Object o1, Object o2) {
                Vertice p1 = (Vertice) o1;
                Vertice p2 = (Vertice) o2;
                return Integer.valueOf(p1.getId()) < Integer.valueOf(p2.getId())? -1 : (Integer.valueOf(p1.getId())> Integer.valueOf(p2.getId()) ? +1 : 0);
            }
        });
             
              
          }
        
          
        
    
    return bi;
    
    
    }
   private SimilarityIndex ordenaSIPorVerticeOrigem(String key, Vertice v1) {
        ArrayList<Vertice> values2 = new ArrayList<Vertice>(si.getMapaArestas().get(key).keySet());
        ArrayList<Aresta> values = si.getArraySimilaridadePorVertice(v1.getId(), key, v1.getSourceId());

        Collections.sort(values, new Comparator() {
            public int compare(Object o1, Object o2) {
                Aresta p1 = (Aresta) o1;
                Aresta p2 = (Aresta) o2;
                return Integer.valueOf(p1.getDestino().getId()) < Integer.valueOf(p2.getDestino().getId()) ? -1 : (Integer.valueOf(p1.getDestino().getId()) > Integer.valueOf(p2.getDestino().getClusterId()) ? +1 : 0);
            }
        });

        return si;

    }
   private SimilarityIndex ordenaListaVerticeSI(String key) {
          ArrayList<Vertice> values = new ArrayList<Vertice>(si.getMapaArestas().get(key).keySet());

        Collections.sort(values, new Comparator() {
            public int compare(Object o1, Object o2) {
                Vertice p1 = (Vertice) o1;
                Vertice p2 = (Vertice) o2;
                return Integer.valueOf(p1.getId()) < Integer.valueOf(p2.getId()) ? -1 : (Integer.valueOf(p1.getId()) > Integer.valueOf(p2.getId()) ? +1 : 0);
            }
        });
        
        return si;

    }
        /**   private SimilarityIndex ordenaVerticeOrigemDoSI(String key, Vertice v1) {
       // Map<Vertice, ArrayList<Aresta>> values2 = si.getMapaPorBloco(key);
      // valuevalues2.keySet()

        //Collections.sort(values, new Comparator() {
            public int compare(Object o1, Object o2) {
                Aresta p1 = (Aresta) o1;
                Aresta p2 = (Aresta) o2;
                return Integer.valueOf(p1.getDestino().getId()) < Integer.valueOf(p2.getDestino().getId()) ? -1 : (Integer.valueOf(p1.getDestino().getId()) > Integer.valueOf(p2.getDestino().getClusterId()) ? +1 : 0);
            }
        });

        return si;

    }**/

     /**
      * Vai fazer por bloco  ordenado//apaguei SI geram//com merge
      * @param bi2
      * @throws IOException 
      */
     public void singleLinkFebrl6(BlockIndex bi2) throws IOException {
        //verifica quais do BI2 estão armazenados no BI oficial e copia cluster
        Precisao precisao = new Precisao();
        Garbage garbageSI = new Garbage();
        precisao.InicializaGoldStandardFebrl();
       // SimilarityIndex siNew= new SimilarityIndex();    //recupera os clusters
        //inicializei por bloco
       // bi2 = inicializaClusterLocalIncremental(bi2);
       this.tempoParaDiminuir = 0;
       
       long tempoInicialAux2 = System.currentTimeMillis();         
        bi= ordenaBI(bi);
        System.err.println(" Tamanho SI antes " +si.getTamanhoVertices());         
         si = garbageSI.limpaSI(bi, si);         
         System.err.println(" Tamanho SI depois " + si.getTamanhoVertices());
        this.tempoParaDiminuir = this.tempoParaDiminuir+(System.currentTimeMillis()-tempoInicialAux2); 
        
        long tempoInicialAux5 = System.currentTimeMillis();
            bi2 = inicializaClusterLocalIncrementalOrdenado(bi2);
            System.err.println(" tempo: Acessar BI "+ (System.currentTimeMillis()- tempoInicialAux5) + " Tamanho do BI " + bi.getNumeroElementos() + " numero de bloco "+ bi.getNumeroBlocos());
            SimilarityIndex siNew= new SimilarityIndex();
            siNew = singleLinkSINotNullOrdenadoFebrl(bi2);
       
       // System.out.println(" Temanho: " + bi2.getNumeroElementos() + "numero Blocos: " + bi2.getNumeroBlocos());
       
      

        //clusteriza as instancias de bi2 que estao sem cluster. Acessa SI ou atualiza SI
        for (Map.Entry<String, ArrayList<Vertice>> entrySet : bi2.getMapaNomes().entrySet()) { //pra cada bloco
            String key = entrySet.getKey();
            //SimilarityIndex siNew= new SimilarityIndex();
            //Preenche a matriz
            
            //long tempoInicialAux4 = System.currentTimeMillis();               
                
           // siNew = singleLinkSINotNullOrdenadoFebrl(bi2);
          //  System.err.println(" tempo: Acessar SI"+ (System.currentTimeMillis()- tempoInicialAux4));
            int clusterRecuperadoBI;

            ArrayList<Vertice> value = entrySet.getValue();
            //preencher matriz sinew
            //siNew = preencheSiNew(key,value,siNew);
            
            //ordenaSI
            
            siNew = ordenaSi(siNew, key);

            boolean continua = true;

            while (continua) {

                Aresta arestaMax = null;
                //Aresta arestaAux = null;
                double maxSimilaridade = 0;
                if (siNew.getMapaPorBloco(key)!=null) {
                    
                for (Map.Entry<Vertice, ArrayList<Aresta>> entrySet1 : siNew.getMapaPorBloco(key).entrySet()) {
                    Vertice key1 = entrySet1.getKey();
                     ArrayList<Aresta> value1 = entrySet1.getValue();
                     if (!value1.isEmpty()) {
                          Aresta aux = value1.get(0);
                    if (aux!=null) {
                        if (aux.getSimilaridade()>maxSimilaridade) {
                            maxSimilaridade = aux.getSimilaridade();
                            arestaMax = aux;
                            
                        }
                        
                    } 
                    }
                  
                }
            }
                if (arestaMax==null) {
                    continua=false;
                }
                else{
                    siNew.removeAresta(key, arestaMax.getOrigem(), arestaMax.getDestino().getId(), arestaMax.getDestino().getSourceId());
                 //verifica se é de clusters distintps
                    
                        
                    int clusterRecuperadoDestino = bi2.getclusterId(arestaMax.getDestino().getId(), key, arestaMax.getDestino().getSourceId());
                    int clusterRecuperadoOrigem = bi2.getclusterId(arestaMax.getOrigem().getId(), key, arestaMax.getOrigem().getSourceId());
                    if (clusterRecuperadoDestino!=clusterRecuperadoOrigem) {
                           arestaMax.getOrigem().setClusterId(clusterRecuperadoDestino);
                    //MudaElementosDeCluster2(arestaMax.getDestino(), arestaMax.getOrigem(),bi2, clusterRecuperadoOrigem, clusterRecuperadoDestino, key, siNew);
                    MudaElementosDeCluster(bi2, clusterRecuperadoOrigem, clusterRecuperadoDestino, key);
                    long tempoInicialAux = System.currentTimeMillis();
                  MudaElementosDeClusterMerge(clusterRecuperadoOrigem, clusterRecuperadoDestino, key);
                       
                    this.tempoParaDiminuir = this.tempoParaDiminuir+(System.currentTimeMillis()-tempoInicialAux );
                        
                        
                    }
                 
                    
                }
                            
            }

            //Finaliza em um bloco
            copiaBlocoFinalToClusterGlobal(bi2, key);

        }

        precisao.clusterToPairFebrl(bi2);
   System.out.println("Precisao " + precisao.getPrecisao());
  System.out.println("Cobertura " + precisao.getCobertura());
   System.out.println("F-Measure " + (2 * ((precisao.getCobertura() * precisao.getPrecisao()) / (precisao.getPrecisao() + precisao.getCobertura()))));
this.totalCerto = precisao.getCerto();


 System.out.println(" Certo " + precisao.getCerto());
this.totalErrado = precisao.getErrado();
      System.out.println("Errado " + precisao.getErrado());
         //System.err.println(" Tempo para subtrais  " + tempoParaDiminuir);


        //System.out.println(" tamanho SI " + siNew.getTamanho());

    }
     
     /**
      * Vai fazer por bloco  ordenado//apaguei SI geram//com merge//Aleterei o Sinotnull para pegar o conjunto de arestas uma unica vez
      * @param bi2
      * @throws IOException 
      */
     public void singleLinkFebrl7(BlockIndex bi2) throws IOException {
        //verifica quais do BI2 estão armazenados no BI oficial e copia cluster
        Precisao precisao = new Precisao();
        Garbage garbageSI = new Garbage();
        precisao.InicializaGoldStandardFebrl();
       // SimilarityIndex siNew= new SimilarityIndex();    //recupera os clusters
        //inicializei por bloco
       // bi2 = inicializaClusterLocalIncremental(bi2);
       this.tempoParaDiminuir = 0;
       
       long tempoInicialAux2 = System.currentTimeMillis();         
        bi= ordenaBI(bi);
        System.err.println(" Tamanho SI antes " +si.getTamanhoVertices());         
         si = garbageSI.limpaSI(bi, si);         
         System.err.println(" Tamanho SI depois " + si.getTamanhoVertices());
        this.tempoParaDiminuir = this.tempoParaDiminuir+(System.currentTimeMillis()-tempoInicialAux2); 
        
        long tempoInicialAux5 = System.currentTimeMillis();
            bi2 = inicializaClusterLocalIncrementalOrdenado(bi2);
            System.err.println(" tempo: Acessar BI "+ (System.currentTimeMillis()- tempoInicialAux5) + " Tamanho do BI " + bi.getNumeroElementos() + " numero de bloco "+ bi.getNumeroBlocos());
            SimilarityIndex siNew= new SimilarityIndex();
            siNew = singleLinkSINotNullOrdenadoFebrl2(bi2);
       
       // System.out.println(" Temanho: " + bi2.getNumeroElementos() + "numero Blocos: " + bi2.getNumeroBlocos());
       
      

        //clusteriza as instancias de bi2 que estao sem cluster. Acessa SI ou atualiza SI
        for (Map.Entry<String, ArrayList<Vertice>> entrySet : bi2.getMapaNomes().entrySet()) { //pra cada bloco
            String key = entrySet.getKey();
            //SimilarityIndex siNew= new SimilarityIndex();
            //Preenche a matriz
            
            //long tempoInicialAux4 = System.currentTimeMillis();               
                
           // siNew = singleLinkSINotNullOrdenadoFebrl(bi2);
          //  System.err.println(" tempo: Acessar SI"+ (System.currentTimeMillis()- tempoInicialAux4));
            int clusterRecuperadoBI;

            ArrayList<Vertice> value = entrySet.getValue();
            //preencher matriz sinew
            //siNew = preencheSiNew(key,value,siNew);
            
            //ordenaSI
            
            siNew = ordenaSi(siNew, key);

            boolean continua = true;

            while (continua) {

                Aresta arestaMax = null;
                //Aresta arestaAux = null;
                double maxSimilaridade = 0;
                if (siNew.getMapaPorBloco(key)!=null) {
                    
                for (Map.Entry<Vertice, ArrayList<Aresta>> entrySet1 : siNew.getMapaPorBloco(key).entrySet()) {
                    Vertice key1 = entrySet1.getKey();
                     ArrayList<Aresta> value1 = entrySet1.getValue();
                     if (!value1.isEmpty()) {
                          Aresta aux = value1.get(0);
                    if (aux!=null) {
                        if (aux.getSimilaridade()>maxSimilaridade) {
                            maxSimilaridade = aux.getSimilaridade();
                            arestaMax = aux;
                            
                        }
                        
                    } 
                    }
                  
                }
            }
                if (arestaMax==null) {
                    continua=false;
                }
                else{
                    siNew.removeAresta(key, arestaMax.getOrigem(), arestaMax.getDestino().getId(), arestaMax.getDestino().getSourceId());
                 //verifica se é de clusters distintps
                    
                        
                    int clusterRecuperadoDestino = bi2.getclusterId(arestaMax.getDestino().getId(), key, arestaMax.getDestino().getSourceId());
                    int clusterRecuperadoOrigem = bi2.getclusterId(arestaMax.getOrigem().getId(), key, arestaMax.getOrigem().getSourceId());
                    if (clusterRecuperadoDestino!=clusterRecuperadoOrigem) {
                        //alterei aqui comentei esta parte da aresta
                      arestaMax.getOrigem().setClusterId(clusterRecuperadoDestino);
                    //MudaElementosDeCluster2(arestaMax.getDestino(), arestaMax.getOrigem(),bi2, clusterRecuperadoOrigem, clusterRecuperadoDestino, key, siNew);
                   bi2 = MudaElementosDeCluster(bi2, clusterRecuperadoOrigem, clusterRecuperadoDestino, key);
                    
                     long tempoInicialAux = System.currentTimeMillis();
                     MudaElementosDeClusterMerge(clusterRecuperadoOrigem, clusterRecuperadoDestino, key);                       
                    this.tempoParaDiminuir = this.tempoParaDiminuir+(System.currentTimeMillis()-tempoInicialAux );
                        
                        
                    }
                 
                    
                }
                            
            }

            //Finaliza em um bloco
            copiaBlocoFinalToClusterGlobal(bi2, key);

        }

        precisao.clusterToPairFebrl(bi2);
   System.out.println("Precisao " + precisao.getPrecisao());
  System.out.println("Cobertura " + precisao.getCobertura());
   System.out.println("F-Measure " + (2 * ((precisao.getCobertura() * precisao.getPrecisao()) / (precisao.getPrecisao() + precisao.getCobertura()))));
this.totalCerto = precisao.getCerto();


 System.out.println(" Certo " + precisao.getCerto());
this.totalErrado = precisao.getErrado();
      System.out.println("Errado " + precisao.getErrado());
         //System.err.println(" Tempo para subtrais  " + tempoParaDiminuir);


        //System.out.println(" tamanho SI " + siNew.getTamanho());

    }
     
      /**
      * Vai fazer por bloco  ordenado//apaguei SI geram//com merge
      * @param bi2
      * @throws IOException 
      */
     public void singleLinkCora6(BlockIndex bi2) throws IOException {
        //verifica quais do BI2 estão armazenados no BI oficial e copia cluster
         Garbage garbageSI = new Garbage();
         Precisao precisao = new Precisao();
        precisao.InicializaGoldStandardCora();
        tempoParaDiminuir = 0;
      
        long tempoInicialAux2 = System.currentTimeMillis();
        bi = ordenaBI(bi);
         System.err.println(" Antes SI " +si.getTamanho());
       garbageSI.limpaSI(this.bi, this.si);
       System.err.println(" Depois SI " +si.getTamanho());
        this.tempoParaDiminuir = this.tempoParaDiminuir+(System.currentTimeMillis()-tempoInicialAux2);
        System.out.println(" Temanho: " + bi2.getNumeroElementos() + "numero Blocos: " + bi2.getNumeroBlocos());
          bi2=inicializaClusterLocalIncrementalOrdenado(bi2);
           SimilarityIndex siNew;
            //Preenche a matriz //por chave e com busca binária
          
            siNew = singleLinkSINotNullOrdenadoCora(bi2);
       
      

        //clusteriza as instancias de bi2 que estao sem cluster. Acessa SI ou atualiza SI
        for (Map.Entry<String, ArrayList<Vertice>> entrySet : bi2.getMapaNomes().entrySet()) { //pra cada bloco
            String key = entrySet.getKey();
           
            int clusterRecuperadoBI;

            ArrayList<Vertice> value = entrySet.getValue();
            //preencher matriz sinew
            //siNew = preencheSiNew(key,value,siNew);
            
            //ordenaSI por similaridade
            
            siNew = ordenaSi(siNew, key);

            boolean continua = true;

            while (continua) {

                Aresta arestaMax = null;
                //Aresta arestaAux = null;
                double maxSimilaridade = 0;
                if (siNew.getMapaPorBloco(key)!=null) {
                    
               
               
                for (Map.Entry<Vertice, ArrayList<Aresta>> entrySet1 : siNew.getMapaPorBloco(key).entrySet()) {
                    Vertice key1 = entrySet1.getKey();
                     ArrayList<Aresta> value1 = entrySet1.getValue();
                     if (!value1.isEmpty()) {
                          Aresta aux = value1.get(0);
                    if (aux!=null) {
                        if (aux.getSimilaridade()>maxSimilaridade) {
                            maxSimilaridade = aux.getSimilaridade();
                            arestaMax = aux;
                            
                        }
                        
                    } 
                    }
                  
                }
            }
                if (arestaMax==null) {
                    continua=false;
                }
                else{
                    siNew.removeAresta(key, arestaMax.getOrigem(), arestaMax.getDestino().getId(), arestaMax.getDestino().getSourceId());
                 //verifica se é de clusters distintps
                    
                        
                    int clusterRecuperadoDestino = bi2.getclusterId(arestaMax.getDestino().getId(), key, arestaMax.getDestino().getSourceId());
                    int clusterRecuperadoOrigem = bi2.getclusterId(arestaMax.getOrigem().getId(), key, arestaMax.getOrigem().getSourceId());
                    if (clusterRecuperadoDestino!=clusterRecuperadoOrigem) {
                           arestaMax.getOrigem().setClusterId(clusterRecuperadoDestino);
                    //MudaElementosDeCluster2(arestaMax.getDestino(), arestaMax.getOrigem(),bi2, clusterRecuperadoOrigem, clusterRecuperadoDestino, key, siNew);
                    MudaElementosDeCluster(bi2, clusterRecuperadoOrigem, clusterRecuperadoDestino, key);
                    long tempoInicialAux = System.currentTimeMillis();
                    MudaElementosDeClusterMerge(clusterRecuperadoOrigem, clusterRecuperadoDestino, key);
                       
                    this.tempoParaDiminuir = this.tempoParaDiminuir+(System.currentTimeMillis()-tempoInicialAux );
                        
                        
                    }
                 
                    
                }
                            
            }

            //Finaliza em um bloco
            copiaBlocoFinalToClusterGlobal(bi2, key);

        }

        precisao.clusterToPairCora(bi2);
   System.out.println("Precisao " + precisao.getPrecisao());
  System.out.println("Cobertura " + precisao.getCobertura());
   System.out.println("F-Measure " + (2 * ((precisao.getCobertura() * precisao.getPrecisao()) / (precisao.getPrecisao() + precisao.getCobertura()))));
this.totalCerto = precisao.getCerto();


 System.out.println(" Certo " + precisao.getCerto());
this.totalErrado = precisao.getErrado();
      System.out.println("Errado " + precisao.getErrado());
         //System.err.println(" Tempo para subtrais  " + tempoParaDiminuir);


        //System.out.println(" tamanho SI " + siNew.getTamanho());

    }
    
    
    /**
      * Se preocuando com o tamanho do SI que deu problem com a base Febrl
      * @param bi2
      * @throws IOException 
      */
     public void singleLinkFebrl2(BlockIndex bi2) throws IOException {
        //verifica quais do BI2 estão armazenados no BI oficial e copia cluster
        Precisao precisao = new Precisao();
        precisao.InicializaGoldStandardFebrl();
        SimilarityIndex siNew= new SimilarityIndex();

        //recupera os clusters
        bi2 = inicializaClusterLocalIncremental(bi2);
        System.out.println(" Temanho: " + bi2.getNumeroElementos() + "numero Blocos: " + bi2.getNumeroBlocos());
        
        if (!this.si.getMapaArestas().isEmpty()) {
            System.out.println(" " + si.getTamanho());
            siNew = singleLinkSINotNull(bi2);
            
        }
        

        //clusteriza as instancias de bi2 que estao sem cluster. Acessa SI ou atualiza SI
        for (Map.Entry<String, ArrayList<Vertice>> entrySet : bi2.getMapaNomes().entrySet()) { //pra cada bloco
            String key = entrySet.getKey();
            int clusterRecuperadoBI;

            ArrayList<Vertice> value = entrySet.getValue();

            boolean continua = true;

            while (continua) {

                Aresta arestaMax = null;
                Aresta arestaAux = null;
                double maxSimilaridade = 0;
                double limiar = 0.9; //limiar

                for (int i = 0; i < value.size(); i++) {//pra o array de cada bloco da entrada
                    // System.err.println(" teste  ++++++++++++++" + controleApagar);
                    //Cluster cluster = new Cluster(si);
                    Vertice get1 = value.get(i);
                    for (int j = i + 1; j < value.size(); j++) {

                        //descobrir por instancia sem bloco a instancia de maior similaridade
                        Vertice get2 = value.get(j);
                        if (get2.getClusterId() != get1.getClusterId()) {
                           
                          //  arestaAux = si.getAresta(key, get1, get2.getId(), get2.getSourceId());
                             arestaAux = siNew.getAresta(key, get1, get2.getId(), get2.getSourceId());
                             
                            
                            //se retornar null é pq n existe a aresta em si. N tem similaridade medida previamente
                            if (arestaAux == null) { //mede similaridade
                                // AbstractStringMetric metric = new Levenshtein();
                                
                                AbstractStringMetric metric = new Levenshtein();
                                float result1 = metric.getSimilarity(get1.getConteudoComparacao(), get2.getConteudoComparacao());
                                float result2 = metric.getSimilarity(get1.getConteudoComparacao2(), get2.getConteudoComparacao2());
                                float result = (float) (result1 + result2) / 2;
                                //float result = (float) (result1 + result2) / 2;
                                siNew.verificaSimilaridade(result, key, get1.getId(), get1.getSourceId(), get2.getId(), get2.getSourceId(), key);

                                if (result > limiar) {
                                    //coloquei aqui. Tirei de cima
                                 // siNew.verificaSimilaridade(result, key, get1.getId(), get1.getSourceId(), get2.getId(), get2.getSourceId(), key);

                                    si.verificaSimilaridade(result, key, get1.getId(), get1.getSourceId(), get2.getId(), get2.getSourceId(), key);

                                    if (result > maxSimilaridade) {
                                        maxSimilaridade = result;
                                        //System.err.print(" Teste agraaa +++++  ");
                                       
                                      //  arestaMax = this.si.getAresta(key, get1, get2.getId(), get2.getSourceId());
                                            arestaMax = siNew.getAresta(key, get1, get2.getId(), get2.getSourceId());
                                        
                                        //System.err.println(" teste continua  " + arestaMax.getDestino().getId());

                                    }

                                }

                            } else {

                                double similaridadeRecuperada = arestaAux.getSimilaridade();
                                if (similaridadeRecuperada > limiar && similaridadeRecuperada > maxSimilaridade) {
                                    arestaMax = arestaAux;
                                    maxSimilaridade = similaridadeRecuperada;
                                    //tem que recuperar a similaridade no SI

                                }

                            }
                          
                        }

                    }

                }

                if (maxSimilaridade > limiar && arestaMax != null) {

                    int clusterRecuperadoDestino = bi2.getclusterId(arestaMax.getDestino().getId(), key, arestaMax.getDestino().getSourceId());
                    int clusterRecuperadoOrigem = bi2.getclusterId(arestaMax.getOrigem().getId(), key, arestaMax.getOrigem().getSourceId());
                    arestaMax.getOrigem().setClusterId(clusterRecuperadoDestino);
                   // MudaElementosDeCluster(bi2, clusterRecuperadoOrigem, clusterRecuperadoDestino, key);
                    //para reduzir tamanho dos índices
                   MudaElementosDeCluster2(arestaMax.getDestino(), arestaMax.getOrigem(),bi2, clusterRecuperadoOrigem, clusterRecuperadoDestino, key, siNew);
                    //limpaSIDentroDeumMesmoCluster(siNew, si, clusterRecuperadoOrigem, clusterRecuperadoDestino);
                 
                } else {
                    continua = false;
                }
                
            }

            //Finaliza em um bloco
            copiaBlocoFinalToClusterGlobal(bi2, key);

        }

        precisao.clusterToPairFebrl(bi2);
   System.out.println("Precisao " + precisao.getPrecisao());
  System.out.println("Cobertura " + precisao.getCobertura());
   System.out.println("F-Measure " + (2 * ((precisao.getCobertura() * precisao.getPrecisao()) / (precisao.getPrecisao() + precisao.getCobertura()))));
this.totalCerto = precisao.getCerto();


 System.out.println(" Certo " + precisao.getCerto());
this.totalErrado = precisao.getErrado();
      System.out.println("Errado " + precisao.getErrado());


        System.out.println(" tamanho SI " + siNew.getTamanho());

    }
     
    
    /**
     * nesta versão perpetuei os merges para os demais elementos do BI. Para avaliar que unir merges sem comparar é melhor ou não
     * @param bi2
     * @throws IOException 
     */
    public void singleLinkComBlockIndexMerge(BlockIndex bi2) throws IOException {
        //verifica quais do BI2 estão armazenados no BI oficial e copia cluster
        Precisao precisao = new Precisao();
        precisao.InicializaGoldStandardCD();
        SimilarityIndex siNew= new SimilarityIndex();

        //recupera os clusters
        bi2 = inicializaClusterLocalIncremental(bi2);
        
        if (!this.si.getMapaArestas().isEmpty()) {
            System.out.println(" " + si.getTamanho());
            siNew = singleLinkSINotNull(bi2);
            
        }
        

        //clusteriza as instancias de bi2 que estao sem cluster. Acessa SI ou atualiza SI
        for (Map.Entry<String, ArrayList<Vertice>> entrySet : bi2.getMapaNomes().entrySet()) { //pra cada bloco
            String key = entrySet.getKey();
            int clusterRecuperadoBI;

            ArrayList<Vertice> value = entrySet.getValue();

            boolean continua = true;

            while (continua) {

                Aresta arestaMax = null;
                Aresta arestaAux = null;
                double maxSimilaridade = 0;
                double limiar = 0.9; //limiar

                for (int i = 0; i < value.size(); i++) {//pra o array de cada bloco da entrada
                    // System.err.println(" teste  ++++++++++++++" + controleApagar);
                    //Cluster cluster = new Cluster(si);
                    Vertice get1 = value.get(i);
                    for (int j = i + 1; j < value.size(); j++) {

                        //descobrir por instancia sem bloco a instancia de maior similaridade
                        Vertice get2 = value.get(j);
                        if (get2.getClusterId() != get1.getClusterId()) {
                           
                          //  arestaAux = si.getAresta(key, get1, get2.getId(), get2.getSourceId());
                             arestaAux = siNew.getAresta(key, get1, get2.getId(), get2.getSourceId());
                            
                            //se retornar null é pq n existe a aresta em si. N tem similaridade medida previamente
                            if (arestaAux == null) { //mede similaridade
                                // AbstractStringMetric metric = new Levenshtein();
                                AbstractStringMetric metric = new Levenshtein();
                                float result1 = metric.getSimilarity(get1.getConteudoComparacao(), get2.getConteudoComparacao());
                                float result2 = metric.getSimilarity(get1.getConteudoComparacao2(), get2.getConteudoComparacao2());
                                float result = (float) (result1 + result2) / 2;
                                //float result = (float) (result1 + result2) / 2;
                                siNew.verificaSimilaridade(result, key, get1.getId(), get1.getSourceId(), get2.getId(), get2.getSourceId(), key);

                                if (result > limiar) {
                                    si.verificaSimilaridade(result, key, get1.getId(), get1.getSourceId(), get2.getId(), get2.getSourceId(), key);

                                    if (result > maxSimilaridade) {
                                        maxSimilaridade = result;
                                        //System.err.print(" Teste agraaa +++++  ");
                                       
                                      //  arestaMax = this.si.getAresta(key, get1, get2.getId(), get2.getSourceId());
                                            arestaMax = siNew.getAresta(key, get1, get2.getId(), get2.getSourceId());
                                        
                                        //System.err.println(" teste continua  " + arestaMax.getDestino().getId());

                                    }

                                }

                            } else {

                                double similaridadeRecuperada = arestaAux.getSimilaridade();
                                if (similaridadeRecuperada > limiar && similaridadeRecuperada > maxSimilaridade) {
                                    arestaMax = arestaAux;
                                    maxSimilaridade = similaridadeRecuperada;
                                    //tem que recuperar a similaridade no SI

                                }

                            }
                          
                        }

                    }

                }

                if (maxSimilaridade > limiar && arestaMax != null) {

                    int clusterRecuperadoDestino = bi2.getclusterId(arestaMax.getDestino().getId(), key, arestaMax.getDestino().getSourceId());
                    int clusterRecuperadoOrigem = bi2.getclusterId(arestaMax.getOrigem().getId(), key, arestaMax.getOrigem().getSourceId());
                    arestaMax.getOrigem().setClusterId(clusterRecuperadoDestino);
                    MudaElementosDeCluster(bi2, clusterRecuperadoOrigem, clusterRecuperadoDestino, key);
                    MudaElementosDeClusterMerge(clusterRecuperadoOrigem, clusterRecuperadoDestino, key);
                 
                } else {
                    continua = false;
                }
            }

            //Finaliza em um bloco
            copiaBlocoFinalToClusterGlobal(bi2, key);

        }

        precisao.clusterToPair(bi2);
   System.out.println("Precisao " + precisao.getPrecisao());
  System.out.println("Cobertura " + precisao.getCobertura());
   System.out.println("F-Measure " + (2 * ((precisao.getCobertura() * precisao.getPrecisao()) / (precisao.getPrecisao() + precisao.getCobertura()))));
this.totalCerto = precisao.getCerto();
this.totalErrado = precisao.getErrado();
        System.out.println(" tamanho SI " + siNew.getTamanho());

    }
    //n contar tempo de acesso as estruturas
    public void singleLink2(BlockIndex bi2) throws IOException {
        //verifica quais do BI2 estão armazenados no BI oficial e copia cluster
        Precisao precisao = new Precisao();
        precisao.InicializaGoldStandardCD();
        SimilarityIndex siNew= new SimilarityIndex();

        //recupera os clusters
        bi2 = inicializaClusterLocalIncremental(bi2);
        
        if (!this.si.getMapaArestas().isEmpty()) {
            System.out.println(" " + si.getTamanho());
            siNew = singleLinkSINotNull(bi2);
            
        }
        

        //clusteriza as instancias de bi2 que estao sem cluster. Acessa SI ou atualiza SI
        for (Map.Entry<String, ArrayList<Vertice>> entrySet : bi2.getMapaNomes().entrySet()) { //pra cada bloco
            String key = entrySet.getKey();
            int clusterRecuperadoBI;

            ArrayList<Vertice> value = entrySet.getValue();

            boolean continua = true;

            while (continua) {

                Aresta arestaMax = null;
                Aresta arestaAux = null;
                double maxSimilaridade = 0;
                double limiar = 0.9; //limiar

                for (int i = 0; i < value.size(); i++) {//pra o array de cada bloco da entrada
                    // System.err.println(" teste  ++++++++++++++" + controleApagar);
                    //Cluster cluster = new Cluster(si);
                    Vertice get1 = value.get(i);
                    for (int j = i + 1; j < value.size(); j++) {

                        //descobrir por instancia sem bloco a instancia de maior similaridade
                        Vertice get2 = value.get(j);
                        if (get2.getClusterId() != get1.getClusterId()) {
                           
                          //  arestaAux = si.getAresta(key, get1, get2.getId(), get2.getSourceId());
                             arestaAux = siNew.getAresta(key, get1, get2.getId(), get2.getSourceId());
                            
                            //se retornar null é pq n existe a aresta em si. N tem similaridade medida previamente
                            if (arestaAux == null) { //mede similaridade
                                // AbstractStringMetric metric = new Levenshtein();
                                AbstractStringMetric metric = new Levenshtein();
                                float result1 = metric.getSimilarity(get1.getConteudoComparacao(), get2.getConteudoComparacao());
                                float result2 = metric.getSimilarity(get1.getConteudoComparacao2(), get2.getConteudoComparacao2());
                                float result = (float) (result1 + result2) / 2;
                                //float result = (float) (result1 + result2) / 2;
                                siNew.verificaSimilaridade(result, key, get1.getId(), get1.getSourceId(), get2.getId(), get2.getSourceId(), key);

                                if (result > limiar) {
                                   // si.verificaSimilaridade(result, key, get1.getId(), get1.getSourceId(), get2.getId(), get2.getSourceId(), key);

                                    if (result > maxSimilaridade) {
                                        maxSimilaridade = result;
                                        //System.err.print(" Teste agraaa +++++  ");
                                       
                                      //  arestaMax = this.si.getAresta(key, get1, get2.getId(), get2.getSourceId());
                                            arestaMax = siNew.getAresta(key, get1, get2.getId(), get2.getSourceId());
                                        
                                        //System.err.println(" teste continua  " + arestaMax.getDestino().getId());

                                    }

                                }

                            } else {

                                double similaridadeRecuperada = arestaAux.getSimilaridade();
                                if (similaridadeRecuperada > limiar && similaridadeRecuperada > maxSimilaridade) {
                                    arestaMax = arestaAux;
                                    maxSimilaridade = similaridadeRecuperada;
                                    //tem que recuperar a similaridade no SI

                                }

                            }
                          
                        }

                    }

                }

                if (maxSimilaridade > limiar && arestaMax != null) {

                    int clusterRecuperadoDestino = bi2.getclusterId(arestaMax.getDestino().getId(), key, arestaMax.getDestino().getSourceId());
                    int clusterRecuperadoOrigem = bi2.getclusterId(arestaMax.getOrigem().getId(), key, arestaMax.getOrigem().getSourceId());
                    arestaMax.getOrigem().setClusterId(clusterRecuperadoDestino);
                    MudaElementosDeCluster(bi2, clusterRecuperadoOrigem, clusterRecuperadoDestino, key);
                 
                } else {
                    continua = false;
                }
            }

            //Finaliza em um bloco
            copiaBlocoFinalToClusterGlobal(bi2, key);

        }

        precisao.clusterToPair(bi2);
   System.out.println("Precisao " + precisao.getPrecisao());
  System.out.println("Cobertura " + precisao.getCobertura());
   System.out.println("F-Measure " + (2 * ((precisao.getCobertura() * precisao.getPrecisao()) / (precisao.getPrecisao() + precisao.getCobertura()))));
this.totalCerto = precisao.getCerto();
this.totalErrado = precisao.getErrado();
        System.out.println(" tamanho SI " + siNew.getTamanho());

    }

    private void limpaSIDentroDeumMesmoCluster(SimilarityIndex siNew, SimilarityIndex si, int clusterRecuperadoOrigem, int clusterRecuperadoDestino, Vertice v) {
      // siNew.get
        
        
    }

    private SimilarityIndex preencheSiNew(String key, ArrayList<Vertice> value, SimilarityIndex siNew) {
        Aresta arestaAux = null;
         double limiar = 0.9;
        
           for (int i = 0; i < value.size(); i++) {//pra o array de cada bloco da entrada
                    // System.err.println(" teste  ++++++++++++++" + controleApagar);
                    //Cluster cluster = new Cluster(si);
                    Vertice get1 = value.get(i);
                    for (int j = i + 1; j < value.size(); j++) {

                        //descobrir por instancia sem bloco a instancia de maior similaridade
                        Vertice get2 = value.get(j);
                        if (get2.getClusterId() != get1.getClusterId()) {
                           
                          //  arestaAux = si.getAresta(key, get1, get2.getId(), get2.getSourceId());
                             arestaAux = siNew.getAresta(key, get1, get2.getId(), get2.getSourceId());
                             
                            
                            //se retornar null é pq n existe a aresta em si. N tem similaridade medida previamente
                            if (arestaAux == null) { //mede similaridade
                                // AbstractStringMetric metric = new Levenshtein();
                                
                                AbstractStringMetric metric = new Levenshtein();
                                float result1 = metric.getSimilarity(get1.getConteudoComparacao(), get2.getConteudoComparacao());
                                float result2 = metric.getSimilarity(get1.getConteudoComparacao2(), get2.getConteudoComparacao2());
                                float result = (float) (result1 + result2) / 2;
                                //float result = (float) (result1 + result2) / 2;
                                //siNew.verificaSimilaridade(result, key, get1.getId(), get1.getSourceId(), get2.getId(), get2.getSourceId(), key);

                                if (result > limiar) {
                                    //coloquei aqui. Tirei de cima
                                  siNew.verificaSimilaridade(result, key, get1.getId(), get1.getSourceId(), get2.getId(), get2.getSourceId(), key);

                                  si.verificaSimilaridade(result, key, get1.getId(), get1.getSourceId(), get2.getId(), get2.getSourceId(), key);

                                
                                }

                            } 
                          
                        }

                    }

                }
           return siNew;
    }
/**
 * Ordena pela similaridade
 * @param siNew
 * @param key
 * @return 
 */
    private SimilarityIndex ordenaSi(SimilarityIndex siNew, String key) {

     
        if (siNew.getMapaPorBloco(key)!=null) {
            for (Map.Entry<Vertice, ArrayList<Aresta>> entrySet : siNew.getMapaPorBloco(key).entrySet()) {
              Vertice verticeKey = entrySet.getKey();
                Collections.sort (siNew.getMapaPorBloco(key).get(verticeKey), new Comparator() {
            public int compare(Object o1, Object o2) {
                Aresta p1 = (Aresta) o1;
                Aresta p2 = (Aresta) o2;
                //alterei aqui adicionando o igual
                return p1.getSimilaridade() < p2.getSimilaridade() ? -1 : (p1.getSimilaridade() > p2.getSimilaridade() ? +1 : 0);
            }
        });
             
              
          }
        }
          
        
    
    return siNew;
    }

    private boolean clustersDiferentes(ArrayList<Vertice> value, Vertice origem, Vertice destino) {
        Vertice v1 =null;
        Vertice v2 =null;
        for (int i = 0; i < value.size(); i++) {
            Vertice get = value.get(i);
            if (get.getId().equalsIgnoreCase(origem.getId())) {
                v1=get;
                
            }
           if (get.getId().equalsIgnoreCase(destino.getId())) {
                v2=get;
                
            }
            
        }
        if (v1!= null && v2!=null) {
            if (v1.getClusterId()!=v2.getClusterId()) {
                return true;
            }
        }
        return false;
    }
    
}
