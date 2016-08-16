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
import java.util.Map;

/**
 *
 * @author Priscilla
 */
public class FuncoesObjetivo {

    private Cluster cluster;
    private String key;
    private SimilarityIndex si;

    public Cluster getCluster() {
        return cluster;
    }

    public void setCluster(Cluster cluster) {
        this.cluster = cluster;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public SimilarityIndex getSi() {
        return si;
    }

    public void setSi(SimilarityIndex si) {
        this.si = si;
    }

    public int getClusterDeOrigem() {
        return clusterDeOrigem;
    }

    public void setClusterDeOrigem(int clusterDeOrigem) {
        this.clusterDeOrigem = clusterDeOrigem;
    }

    public int getClusterDeDestino() {
        return clusterDeDestino;
    }

    public void setClusterDeDestino(int clusterDeDestino) {
        this.clusterDeDestino = clusterDeDestino;
    }

    public double getSimilaridade() {
        return similaridade;
    }

    public void setSimilaridade(double similaridade) {
        this.similaridade = similaridade;
    }
    private int clusterDeOrigem;
    private int clusterDeDestino;
    private double similaridade;

    public FuncoesObjetivo(Cluster cluster, String key, SimilarityIndex si) {
        this.cluster = cluster;
        this.key = key;
        this.si = si;
        this.clusterDeDestino=-1;
        this.clusterDeOrigem=-1;
        this.similaridade=0;
    }

    public double getCoesaoAnteriorLocal(ArrayList<Vertice> vertice) {
        Cluster cluster = new Cluster(si);
        cluster.criaCluster(vertice, key);

        ArrayList<Vertice> v1 = cluster.getVertices(vertice.get(0).getClusterId());
        double maxCoesao = -1;
        if (v1 == null) {
            return maxCoesao;
        } else {
            return maxCoesao = getCoesao(v1);
        }

    }

    public Vertice getCoesaoTuplaSemCluster(ArrayList<Vertice> v1) {
        double maxcoesao = 0;
        Vertice v2 = null;

        for (int i = 0; i < v1.size(); i++) {
            Vertice get = v1.get(i);
            if (get.getSimilarity() > maxcoesao) {
                maxcoesao = get.getSimilarity();
                v2 = get;
            }

        }

        return v2;

    }

    public double getCoesao(ArrayList<Vertice> v1) {
        double coesao = 0;

        for (int i = 0; i < v1.size(); i++) {
            Vertice get = v1.get(i);
            coesao = coesao + get.getSimilarity();

        }

        return coesao / v1.size();

    }

    public double getSomaSimilaridades(ArrayList<Vertice> v1) {
        double soma = 0;

        for (int i = 0; i < v1.size(); i++) {
            Vertice get = v1.get(i);
            soma = soma + get.getSimilarity();

        }

        return soma / v1.size();

    }

    /**
     * depois verificar a questão de quebra clusters ou unir clusters. Por
     * exemplo, se a Max coesao fica muito baixa, pode ser que se tenha que
     * quebra um cluster em dois. DAdo que se tá comparando aqui é pq a
     * similaridade isolada foi alta.
     *
     * @param vertice
     * @param key
     * @param sourceId
     * @return
     */
    public Vertice getCoesaoLocal() {

        //corrigir clusster -1
        Vertice clusterMaxCoesao = null;
        double maxCoesao = -1;
        double coesaoAtual = -1;
        double coesaoAnterior = -1;

        for (Map.Entry<Integer, ArrayList<Vertice>> entrySet : this.cluster.getMapaArestas().entrySet()) { //pra cada cluster
            int key = entrySet.getKey();
            ArrayList<Vertice> v1 = entrySet.getValue();
            // System.out.println(" Esntrou 2");
            if (key == -1) {
                System.out.println(" Esntrou 2");
                Vertice maxSimilaridade = getCoesaoTuplaSemCluster(v1);
                if (maxSimilaridade != null) {
                    if (maxSimilaridade.getSimilarity() > maxCoesao) {
                        maxCoesao = maxSimilaridade.getSimilarity();
                        clusterMaxCoesao = maxSimilaridade;
                    }

                }

            } else {
                coesaoAtual = getCoesao(v1);//calcula coesao se insere a tupla
                coesaoAnterior = getCoesaoAnteriorLocal(v1);
                if (coesaoAnterior < coesaoAtual && coesaoAtual > maxCoesao) {
                    maxCoesao = coesaoAtual;
                    clusterMaxCoesao = v1.get(0);
                }
            }

        }

        return clusterMaxCoesao;

    }

    public Vertice getCoesaoLocalInicializacao() {

        //corrigir clusster -1
        Vertice clusterMaxCoesao = null;
        double maxCoesao = -1;
        double coesaoAtual = -1;
        double coesaoAnterior = -1;

        for (Map.Entry<Integer, ArrayList<Vertice>> entrySet : this.cluster.getMapaArestas().entrySet()) { //pra cada cluster
            int key = entrySet.getKey();
            ArrayList<Vertice> v1 = entrySet.getValue();
            coesaoAtual = getCoesao(v1);//calcula coesao se insere a tupla
            coesaoAnterior = getCoesaoAnteriorLocal(v1);
            if (coesaoAnterior < coesaoAtual && coesaoAtual > maxCoesao) { //reverCoesaoAnterior
                maxCoesao = coesaoAtual;
                clusterMaxCoesao = v1.get(0);
            }

        }

        return clusterMaxCoesao;

    }

    public void getCalculoAverageLink() {

        //corrigir clusster -1
        int clusterMaxSimilaridadeOrigem = -1;
        int clusterMaxSimilaridadeDestino = -1;
        double maxsimilaridade = 0;

        for (Map.Entry<Integer, ArrayList<Vertice>> entrySet : this.cluster.getMapaArestas().entrySet()) { //pra cada cluster
            int key = entrySet.getKey();
            ArrayList<Vertice> getCluster = entrySet.getValue();

            for (Map.Entry<Integer, ArrayList<Vertice>> entrySet1 : this.cluster.getMapaArestas().entrySet()) {
                int key1 = entrySet1.getKey();
                ArrayList<Vertice> getClusterASerComparado = entrySet1.getValue();
                if (key1 != key) {
                   
                    for (int i = 0; i < getCluster.size(); i++) {
                        Vertice get = getCluster.get(i);
                        int j;
                         double somaPorElemento = 0;
                        for (j = 0; j < getClusterASerComparado.size(); j++) {
                            Vertice get1 = getClusterASerComparado.get(j);
                            Aresta aresta = this.si.getAresta(this.key, get, get1.getId(), get1.getSourceId());
                            if (aresta!=null) {
                                      somaPorElemento = somaPorElemento + aresta.getSimilaridade();
                                      
                                    
                            }
                          
                        }
                      
                        double aux =  (somaPorElemento/(double)(getClusterASerComparado.size()));
                        get.setSimilarity(aux);
                        if (aux>1) {
                          System.err.println("****************************************************************  "+j + " " + somaPorElemento + " "+aux);
                            
                        }
                    }
                    double mediaSimilaridadeEntreDoisClusters = getSomaSimilaridades(getCluster);
                    if (mediaSimilaridadeEntreDoisClusters > maxsimilaridade) {
                        clusterMaxSimilaridadeOrigem = key;
                        clusterMaxSimilaridadeDestino = key1;
                        maxsimilaridade = mediaSimilaridadeEntreDoisClusters;
                    }

                }

            }

        }
        if (maxsimilaridade !=0) {
            this.clusterDeOrigem = clusterMaxSimilaridadeOrigem;
            this.clusterDeDestino = clusterMaxSimilaridadeDestino;
            this.similaridade = maxsimilaridade;
            
        }
        if (maxsimilaridade >1) {
            //System.err.println(" PQ Deus?????");
            
        }

    }

}
