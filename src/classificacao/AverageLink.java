/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package classificacao;

import Index.BlockIndex;
import Index.SimilarityIndex;
import avaliacao.Cluster;
import avaliacao.FuncoesObjetivo;
import avaliacao.Precisao;
import graph.Aresta;
import graph.Vertice;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;
import uk.ac.shef.wit.simmetrics.similaritymetrics.AbstractStringMetric;
import uk.ac.shef.wit.simmetrics.similaritymetrics.Levenshtein;

/**
 *
 * @author Priscilla
 */
public class AverageLink {
    public BlockIndex bi = new BlockIndex();
    public SimilarityIndex si = new SimilarityIndex();
    public int controleClusterId=0;
    private int totalCerto;
    private int totalErrado;
    private long tempoParaDiminuir = 0;

    public long getTempoParaDiminuir() {
        return tempoParaDiminuir;
    }

    public void setTempoParaDiminuir(long tempoParaDiminuir) {
        this.tempoParaDiminuir = tempoParaDiminuir;
    }

    public BlockIndex getBi() {
        return bi;
    }

    public void setBi(BlockIndex bi) {
        this.bi = bi;
    }

    public SimilarityIndex getSi() {
        return si;
    }

    public void setSi(SimilarityIndex si) {
        this.si = si;
    }

    public int getControleClusterId() {
        return controleClusterId;
    }

    public void setControleClusterId(int controleClusterId) {
        this.controleClusterId = controleClusterId;
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

    public AverageLink() {
       
        this.bi = new BlockIndex();
       this.si = new SimilarityIndex();
    controleClusterId=0;
  
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
                            arestaAux = this.si.getAresta(key, get1, get2.getId(), get2.getSourceId());
                            //se retornar null é pq n existe a aresta em si. N tem similaridade medida previamente
                            if (arestaAux != null){
                                double similaridadeRecuperada = arestaAux.getSimilaridade();
                                siNotNull.verificaSimilaridade(similaridadeRecuperada, key, get1.getId(), get1.getSourceId(), get2.getId(), get2.getSourceId(), key);
                                
                            }else{
                                
                                AbstractStringMetric metric = new Levenshtein();
                                float result1 = metric.getSimilarity(get1.getConteudoComparacao(), get2.getConteudoComparacao());
                                float result2 = metric.getSimilarity(get1.getConteudoComparacao2(), get2.getConteudoComparacao2());
                                float result = (float) (result1 + result2) / 2;
                                //float result = (float) (result1 + result2) / 2;
                                siNotNull.verificaSimilaridade(result, key, get1.getId(), get1.getSourceId(), get2.getId(), get2.getSourceId(), key);
                                //se for incremental tem que atualizar o SI Geral
                                si.verificaSimilaridade(result, key, get1.getId(), get1.getSourceId(), get2.getId(), get2.getSourceId(), key);

                            }
                          
                        }

                    }

                }

        }
        return siNotNull;

        
    }
    public SimilarityIndex singleLinkSINotNullTradicional(BlockIndex bi2) throws IOException {
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
                            arestaAux = this.si.getAresta(key, get1, get2.getId(), get2.getSourceId());
                            //se retornar null é pq n existe a aresta em si. N tem similaridade medida previamente
                            if (arestaAux != null){
                                double similaridadeRecuperada = arestaAux.getSimilaridade();
                                siNotNull.verificaSimilaridade(similaridadeRecuperada, key, get1.getId(), get1.getSourceId(), get2.getId(), get2.getSourceId(), key);
                                
                            }else{
                                
                                AbstractStringMetric metric = new Levenshtein();
                                float result1 = metric.getSimilarity(get1.getConteudoComparacao(), get2.getConteudoComparacao());
                                float result2 = metric.getSimilarity(get1.getConteudoComparacao2(), get2.getConteudoComparacao2());
                                float result = (float) (result1 + result2) / 2;
                                //float result = (float) (result1 + result2) / 2;
                                siNotNull.verificaSimilaridade(result, key, get1.getId(), get1.getSourceId(), get2.getId(), get2.getSourceId(), key);
                                //se for incremental tem que atualizar o SI Geral
                                //si.verificaSimilaridade(result, key, get1.getId(), get1.getSourceId(), get2.getId(), get2.getSourceId(), key);

                            }
                          
                        }

                    }

                }

        }
        return siNotNull;

        
    }
        public void MudaElementosDeClusterMerge( int clusterIdOrigem, int clusterIdDestino, String key) {

        if (bi.getMapaNomes().get(key)!=null) {
             
                  ArrayList <Vertice> value = this.bi.getMapaNomes().get(key);
                int clusterRecuperadoBI;

        for (int i = 0; i < value.size(); i++) {
            Vertice get = value.get(i);
            clusterRecuperadoBI = bi.getclusterId(get.getId(), key, get.getSourceId());
            if (clusterRecuperadoBI == clusterIdOrigem) {
                get.setClusterId(clusterIdDestino);

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
       
       public void calculaSI(SimilarityIndex siNew, BlockIndex bi2) {
        for (Map.Entry<String, ArrayList<Vertice>> entrySet : bi2.getMapaNomes().entrySet()) {
            String key = entrySet.getKey();
            
            
        }
        
}
     public void averageLinkIncremental(BlockIndex bi2) throws IOException {
        //verifica quais do BI2 estão armazenados no BI oficial e copia cluster
        Precisao precisao = new Precisao();
        precisao.InicializaGoldStandardCD();
        SimilarityIndex si2= new SimilarityIndex();

        //recupera os clusters
        bi2 = inicializaClusterLocalIncremental(bi2);     
       si2 = singleLinkSINotNull(bi2);
      //  System.out.println(" tamanho SI " +si2.getTamanho());
         
          //inicializar si2 antes tudo
            // si2 = calculaSI(siNew, bi2);
   
        //clusteriza as instancias de bi2 que estao sem cluster. Acessa SI ou atualiza SI
        for (Map.Entry<String, ArrayList<Vertice>> entrySet : bi2.getMapaNomes().entrySet()) { //pra cada bloco
           
            String key = entrySet.getKey();
            // System.out.println(" ******************************* Bloco " + key  );
            int clusterRecuperadoBI;

             ArrayList<Vertice> value = entrySet.getValue();
             Cluster cluster = new Cluster(si2);            
            
            for (int i = 0; i < value.size(); i++) {
                Vertice get = value.get(i);
                cluster.insereVertice(get);
                
            }
            FuncoesObjetivo funcaoObjetivo= new FuncoesObjetivo(cluster, key, si2);
            funcaoObjetivo.getCalculoAverageLink();
            while (funcaoObjetivo.getSimilaridade()>0.9) {   
                //System.out.println(" COntrole dos clusters por bloco");
                //junta e cria novo cluster
              bi2 =  MudaElementosDeCluster(bi2, funcaoObjetivo.getClusterDeOrigem(), funcaoObjetivo.getClusterDeDestino(), key);
              Cluster cluster2 = new Cluster(si2);
              ArrayList <Vertice> vertices = bi2.getMapaNomes().get(key);
                for (int i = 0; i < vertices.size(); i++) {
                    Vertice get = vertices.get(i);
                    cluster2.insereVertice(get);
                }
                
                funcaoObjetivo.setCluster(cluster2);
                funcaoObjetivo.setClusterDeDestino(-1);
                funcaoObjetivo.setClusterDeOrigem(-1);
                funcaoObjetivo.setSimilaridade(0);
                funcaoObjetivo.getCalculoAverageLink();
            }
            //perpetua se for incremental
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

    
    } 
/**
 * Atualizando o BI geral
 * @param bi2
 * @throws IOException 
 */
    public void averageLinkIncrementalWithBIAtualizado(BlockIndex bi2) throws IOException {
        //verifica quais do BI2 estão armazenados no BI oficial e copia cluster
        Precisao precisao = new Precisao();
        precisao.InicializaGoldStandardCD();
        SimilarityIndex si2= new SimilarityIndex();

        //recupera os clusters
        bi2 = inicializaClusterLocalIncremental(bi2);     
       si2 = singleLinkSINotNull(bi2);
     //   System.out.println(" tamanho SI " +si2.getTamanho());
         
          //inicializar si2 antes tudo
            // si2 = calculaSI(siNew, bi2);
   
        //clusteriza as instancias de bi2 que estao sem cluster. Acessa SI ou atualiza SI
        for (Map.Entry<String, ArrayList<Vertice>> entrySet : bi2.getMapaNomes().entrySet()) { //pra cada bloco
           
            String key = entrySet.getKey();
            // System.out.println(" ******************************* Bloco " + key  );
            int clusterRecuperadoBI;

             ArrayList<Vertice> value = entrySet.getValue();
             Cluster cluster = new Cluster(si2);            
            
            for (int i = 0; i < value.size(); i++) {
                Vertice get = value.get(i);
                cluster.insereVertice(get);
                
            }
            FuncoesObjetivo funcaoObjetivo= new FuncoesObjetivo(cluster, key, si2);
            funcaoObjetivo.getCalculoAverageLink();
            while (funcaoObjetivo.getSimilaridade()>0.9) {   
                //System.out.println(" COntrole dos clusters por bloco");
                //junta e cria novo cluster
              bi2 =  MudaElementosDeCluster(bi2, funcaoObjetivo.getClusterDeOrigem(), funcaoObjetivo.getClusterDeDestino(), key);
              
              long tempoInicialAux = System.currentTimeMillis();
              MudaElementosDeClusterMerge(funcaoObjetivo.getClusterDeOrigem(), funcaoObjetivo.getClusterDeDestino(), key);
               this.tempoParaDiminuir = this.tempoParaDiminuir+(System.currentTimeMillis()-tempoInicialAux );
              Cluster cluster2 = new Cluster(si2);
              ArrayList <Vertice> vertices = bi2.getMapaNomes().get(key);
                for (int i = 0; i < vertices.size(); i++) {
                    Vertice get = vertices.get(i);
                    cluster2.insereVertice(get);
                }
                
                funcaoObjetivo.setCluster(cluster2);
                funcaoObjetivo.setClusterDeDestino(-1);
                funcaoObjetivo.setClusterDeOrigem(-1);
                funcaoObjetivo.setSimilaridade(0);
                funcaoObjetivo.getCalculoAverageLink();
            }
            //perpetua se for incremental
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

    
    }
    
     public void averageLinkTradicional(BlockIndex bi2) throws IOException {
        //verifica quais do BI2 estão armazenados no BI oficial e copia cluster
        Precisao precisao = new Precisao();
        precisao.InicializaGoldStandardCD();
        SimilarityIndex si2= new SimilarityIndex();

        //recupera os clusters
        bi2 = inicializaClusterLocalIncremental(bi2);     
       si2 = singleLinkSINotNullTradicional(bi2);
       // System.out.println(" tamanho SI " +si2.getTamanho());
         
          //inicializar si2 antes tudo
            // si2 = calculaSI(siNew, bi2);
   
        //clusteriza as instancias de bi2 que estao sem cluster. Acessa SI ou atualiza SI
        for (Map.Entry<String, ArrayList<Vertice>> entrySet : bi2.getMapaNomes().entrySet()) { //pra cada bloco
           
            String key = entrySet.getKey();
            // System.out.println(" ******************************* Bloco " + key  );
            int clusterRecuperadoBI;

             ArrayList<Vertice> value = entrySet.getValue();
             Cluster cluster = new Cluster(si2);            
            
            for (int i = 0; i < value.size(); i++) {
                Vertice get = value.get(i);
                cluster.insereVertice(get);
                
            }
            FuncoesObjetivo funcaoObjetivo= new FuncoesObjetivo(cluster, key, si2);
            funcaoObjetivo.getCalculoAverageLink();
            while (funcaoObjetivo.getSimilaridade()>0.9) {   
                //System.out.println(" COntrole dos clusters por bloco");
                //junta e cria novo cluster
              bi2 =  MudaElementosDeCluster(bi2, funcaoObjetivo.getClusterDeOrigem(), funcaoObjetivo.getClusterDeDestino(), key);
              Cluster cluster2 = new Cluster(si2);
              ArrayList <Vertice> vertices = bi2.getMapaNomes().get(key);
                for (int i = 0; i < vertices.size(); i++) {
                    Vertice get = vertices.get(i);
                    cluster2.insereVertice(get);
                }
                
                funcaoObjetivo.setCluster(cluster2);
                funcaoObjetivo.setClusterDeDestino(-1);
                funcaoObjetivo.setClusterDeOrigem(-1);
                funcaoObjetivo.setSimilaridade(0);
                funcaoObjetivo.getCalculoAverageLink();
            }
            //perpetua se for incremental
            //copiaBlocoFinalToClusterGlobal(bi2, key);
            
        }
      precisao.clusterToPair(bi2);
  // System.out.println("Precisao " + precisao.getPrecisao());
  //System.out.println("Cobertura " + precisao.getCobertura());
 //  System.out.println("F-Measure " + (2 * ((precisao.getCobertura() * precisao.getPrecisao()) / (precisao.getPrecisao() + precisao.getCobertura()))));
this.totalCerto = precisao.getCerto();
    //    System.out.println(" Certo " + precisao.getCerto());
this.totalErrado = precisao.getErrado();
       // System.out.println("Errado " + precisao.getErrado());

    
    }   
        
        
    
    
}
