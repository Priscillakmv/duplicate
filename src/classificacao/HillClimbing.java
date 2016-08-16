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
public class HillClimbing {
    
        private SimilarityIndex si;
    private BlockIndex bi;
    int controleClusterId;

    public HillClimbing(SimilarityIndex si, BlockIndex bi) {

        this.si = si;
        this.bi = bi;
        this.controleClusterId = 0;
    }
    public void correcao(BlockIndex bi2, String key, Vertice v) throws IOException {
        //verifica quais do BI2 estão armazenados no BI oficial e copia cluster

        ArrayList<Vertice> value = bi2.getMapaNomes().get(key);
        ArrayList<Vertice> anterior = new ArrayList<Vertice>();
        ArrayList<Vertice> novo = null;
        Cluster cl = new Cluster();
        // Vertice vGlobal;
        cl.insereVertice(value, v);//pega todos do cluster alterado

        for (Map.Entry<Integer, ArrayList<Vertice>> entrySet : cl.getMapaArestas().entrySet()) {
            int key1 = entrySet.getKey();
            ArrayList<Vertice> value1 = entrySet.getValue();
                //vGlobal = value1.get(0);

            //Aqui, antes de mudar de bloco, deve refazer o processo, caso algo tenha mudado
            do {
                anterior.clear();
                novo = value1;
                anterior.addAll(value1);

                for (int i = 0; i < value1.size(); i++) {//pra o array de cada bloco da entrada
                    // System.err.println(" teste  ++++++++++++++" + controleApagar);
                    Cluster cluster = new Cluster(si);
                    Vertice get3 = value1.get(i);
                    Vertice get1 = null;
                    for (int j = 0; j < bi2.getMapaNomes().get(key).size(); j++) {
                        Vertice get4 = bi2.getMapaNomes().get(key).get(j);
                        if (get3.getId().equals(get4.getId()) && get3.getSourceId().equals(get4.getSourceId())) {
                            get1 = get4;
                        }

                    }

                    double limiar = 0.9; //limiar
                    Aresta arestaMax = null;
                    Aresta arestaAux = null;
                    double maxSimilaridade = 0;

                    for (int j = 0; j < value.size(); j++) {//descobrir por instancia sem bloco a instancia de maior similaridade

                        Vertice get2 = value.get(j);
                        if (!get2.getId().equals(get1.getId())) {
                            if (get2.getClusterId() != -1) {
                                arestaAux = this.si.getAresta(key, get1, get2.getId(), get2.getSourceId());
                                //se retornar null é pq n existe a aresta em si. N tem similaridade medida previamente
                                if (arestaAux == null) { //mede similaridade
                                    AbstractStringMetric metric = new Levenshtein();
                                    float result1 = metric.getSimilarity(get1.getConteudoComparacao(), get2.getConteudoComparacao());
                                    float result2 = metric.getSimilarity(get1.getConteudoComparacao2(), get2.getConteudoComparacao2());
                                    float result = (float) (result1 + result2) / 2;
                                    if (result > limiar) {
                                        get2.setSimilarity(result);

                                        cluster.insereVertice(get2);

                                        si.verificaSimilaridade(result, key, get1.getId(), get1.getSourceId(), get2.getId(), get2.getSourceId(), key);

                                    }

                                } else {

                                    if (this.si.getAresta(key, get1, get2.getId(), get2.getSourceId()).getSimilaridade() > limiar) {
                                        get2.setSimilarity(this.si.getAresta(key, get1, get2.getId(), get2.getSourceId()).getSimilaridade());

                                        cluster.insereVertice(get2);
                                    }

                                }
                            }

                        }

                    }

                    FuncoesObjetivo obj = new FuncoesObjetivo(cluster, key, this.si);
                    Vertice verticeCoesaoCalculada = obj.getCoesaoLocal();
                    if (verticeCoesaoCalculada != null) {
                        if (verticeCoesaoCalculada.getClusterId() == -1) {
                            bi.insertVertice(key, get1);

                        } else {
                            if (get1.getClusterId() != verticeCoesaoCalculada.getClusterId()) {
                                get1.setClusterId(verticeCoesaoCalculada.getClusterId());
                                get3.setClusterId(verticeCoesaoCalculada.getClusterId());
                                // precisao.inserePairDuplicado("cddb", get1.getId(), "cddb", verticeCoesaoCalculada.getId());
                                bi.insertVertice(key, get1);

                                System.err.println("  **********************************8");
                                cl.insereVertice(value, verticeCoesaoCalculada);

                            } else {
                                bi.insertVertice(key, get1);
                            }
                        //alterou um cluster
                            // cl.removeVertice(get1);

                            //bi.insertVertice(key, verticeCoesaoCalculada);
                        }

                    } else {
                        //get1.setClusterId(controleClusterId);
                        //controleClusterId++;
                        bi.insertVertice(key, get1);

                    }

                }
                cl.removeVerticeClusterErrado(key1);
                novo = value1;
                if (!this.grafoIgual2(anterior, novo)) {
                    System.err.println(" Teste que está corrigindo &&&&&&&   ");

                }

            } while (!this.grafoIgual2(anterior, novo));

        }

        //bi2.printBlockIndex();
        // System.out.println("**************Fim*************");
    }

    private boolean grafoIgual2(ArrayList<Vertice> anterior, ArrayList<Vertice> novo) {
        boolean grafoIgual = true;
        for (int i = 0; i < novo.size(); i++) {
            if (grafoIgual == true) {

                Vertice get = novo.get(i);
                for (int j = 0; j < anterior.size(); j++) {
                    Vertice get1 = novo.get(j);
                    if (get.getId().equals(get1.getId()) && get.getSourceId().equals(get1.getSourceId())) {
                        grafoIgual = true;
                        break;

                    } else {
                        grafoIgual = false;
                    }

                }
            }
        }
        return grafoIgual;
    }

    public BlockIndex inicializaClusterLocal(BlockIndex bi2) {
        for (Map.Entry<String, ArrayList<Vertice>> entrySet : bi2.getMapaNomes().entrySet()) {

            String key = entrySet.getKey();
            int clusterRecuperadoBI;
            ArrayList<Vertice> value = entrySet.getValue();
            for (int i = 0; i < value.size(); i++) {
                Vertice get = value.get(i);
                clusterRecuperadoBI = this.bi.getclusterId(get.getId(), key, get.getSourceId());
                get.setClusterId(clusterRecuperadoBI);

            }

        }
        return bi2;
    }
     private boolean grafoIgual(ArrayList<Vertice> anterior, ArrayList<Vertice> novo) {
        boolean grafoIgual = true;
        for (int i = 0; i < novo.size(); i++) {
            if (grafoIgual == true) {

                Vertice get = novo.get(i);
                for (int j = 0; j < anterior.size(); j++) {
                    Vertice get1 = novo.get(j);
                    if (get.equals(get1)) {
                        grafoIgual = true;
                        break;

                    } else {
                        grafoIgual = false;
                    }

                }
            }
        }
        return grafoIgual;
    }

    
    public BlockIndex inicializaClusterLocalLote(BlockIndex bi2) {
        System.out.println("  Tm " + bi2.getNumeroElementos());

        for (Map.Entry<String, ArrayList<Vertice>> entrySet : bi2.getMapaNomes().entrySet()) {

            //String key = entrySet.getKey();
            //int clusterRecuperadoBI;
            ArrayList<Vertice> value = entrySet.getValue();
            for (int i = 0; i < value.size(); i++) {
                Vertice get = value.get(i);
                //clusterRecuperadoBI = this.bi.getclusterId(get.getId(), key, get.getSourceId());
                get.setClusterId(this.controleClusterId);
                this.controleClusterId++;

            }

        }

        return bi2;
    }

    
     /**
     * Faz em lote. Tradicional. Sem incrementos. recebe toda a entrada e
     * clusteriza.Igual ao da literatura Opcao 1 com precisao 0.7 e cobertura
     * 0.4. 141 certo e 39 errado
     *
     * @param bi2
     * @throws IOException
     */
    public void cluserizaOtimizado4(BlockIndex bi2) throws IOException {
        //verifica quais do BI2 estão armazenados no BI oficial e copia cluster
        Precisao precisao = new Precisao();
        precisao.InicializaGoldStandardCD();

        //recupera os clusters
        bi2 = inicializaClusterLocalLote(bi2);

        //clusteriza as instancias de bi2 que estao sem cluster. Acessa SI ou atualiza SI
        for (Map.Entry<String, ArrayList<Vertice>> entrySet : bi2.getMapaNomes().entrySet()) { //pra cada bloco
            String key = entrySet.getKey();
            int clusterRecuperadoBI;

            ArrayList<Vertice> value = entrySet.getValue();
            ArrayList<Vertice> anterior = new ArrayList<Vertice>();
            ArrayList<Vertice> novo = null;

            //Aqui, antes de mudar de bloco, deve refazer o processo, caso algo tenha mudado
            do {

                anterior.clear();
                novo = value;
                anterior.addAll(value);
                for (int i = 0; i < value.size(); i++) {//pra o array de cada bloco da entrada
                    // System.err.println(" teste  ++++++++++++++" + controleApagar);
                    Cluster cluster = new Cluster(si);
                    Vertice get1 = value.get(i);

                    double limiar = 0.9; //limiar
                    Aresta arestaMax = null;
                    Aresta arestaAux = null;
                    double maxSimilaridade = 0;

                    for (int j = 0; j < value.size(); j++) {//descobrir por instancia sem bloco a instancia de maior similaridade

                        if (i != j) {

                            Vertice get2 = value.get(j);
                            // if (get2.getClusterId() != -1) {
                            arestaAux = this.si.getAresta(key, get1, get2.getId(), get2.getSourceId());
                            //se retornar null é pq n existe a aresta em si. N tem similaridade medida previamente
                            if (arestaAux == null) { //mede similaridade
                                AbstractStringMetric metric = new Levenshtein();
                                float result1 = metric.getSimilarity(get1.getConteudoComparacao(), get2.getConteudoComparacao());
                                float result2 = metric.getSimilarity(get1.getConteudoComparacao2(), get2.getConteudoComparacao2());
                                float result = (float) (result1 + result2) / 2;
                                if (result > limiar) {
                                    get2.setSimilarity(result);

                                    cluster.insereVertice(get2);

                                    si.verificaSimilaridade(result, key, get1.getId(), get1.getSourceId(), get2.getId(), get2.getSourceId(), key);

                                }

                            } else {

                                if (this.si.getAresta(key, get1, get2.getId(), get2.getSourceId()).getSimilaridade() > limiar) {
                                    get2.setSimilarity(this.si.getAresta(key, get1, get2.getId(), get2.getSourceId()).getSimilaridade());

                                    cluster.insereVertice(get2);
                                }

                            }
                            //  }

                        }

                    }

                    FuncoesObjetivo obj = new FuncoesObjetivo(cluster, key, this.si);
                    Vertice verticeCoesaoCalculada = obj.getCoesaoLocal();
                    if (verticeCoesaoCalculada != null) {
                        if (verticeCoesaoCalculada.getClusterId() == -1) {
                            bi.insertVertice(key, get1);

                        } else {

                            get1.setClusterId(verticeCoesaoCalculada.getClusterId());
                            // precisao.inserePairDuplicado("cddb", get1.getId(), "cddb", verticeCoesaoCalculada.getId());
                            bi.insertVertice(key, get1);

                            //bi.insertVertice(key, verticeCoesaoCalculada);
                        }

                    } else {
                        //get1.setClusterId(controleClusterId);
                        //controleClusterId++;
                        bi.insertVertice(key, get1);

                    }

                }
                novo = value;
            } while (!this.grafoIgual(anterior, novo));

        }

        //bi2.printBlockIndex();
        precisao.clusterToPair(bi2);
        System.out.println("Precisao " + precisao.getPrecisao());
        System.out.println("Cobertura 7 " + precisao.getCobertura());
        System.out.println("F-Measure " + (2 * ((precisao.getCobertura() * precisao.getPrecisao()) / (precisao.getPrecisao() + precisao.getCobertura()))));

        // System.out.println("**************Fim*************");
    }
    /**
     * Faz incremental. Com incrementos. Mas sem corrigir erros
     *
     * @param bi2
     * @throws IOException
     */
    public void cluserizaOtimizado3(BlockIndex bi2) throws IOException {
        //verifica quais do BI2 estão armazenados no BI oficial e copia cluster
        Precisao precisao = new Precisao();
        precisao.InicializaGoldStandardCD();

        //recupera os clusters
        bi2 = inicializaClusterLocal(bi2);

        //clusteriza as instancias de bi2 que estao sem cluster. Acessa SI ou atualiza SI
        for (Map.Entry<String, ArrayList<Vertice>> entrySet : bi2.getMapaNomes().entrySet()) { //pra cada bloco
            String key = entrySet.getKey();
            int clusterRecuperadoBI;

            ArrayList<Vertice> value = entrySet.getValue();
            for (int i = 0; i < value.size(); i++) {//pra o array de cada bloco da entrada
                // System.err.println(" teste  ++++++++++++++" + controleApagar);
                Cluster cluster = new Cluster(si);
                Vertice get1 = value.get(i);

                double limiar = 0.9; //limiar
                Aresta arestaMax = null;
                Aresta arestaAux = null;
                double maxSimilaridade = 0;

                if (get1.getClusterId() == -1) {
                    for (int j = 0; j < value.size(); j++) {//descobrir por instancia sem bloco a instancia de maior similaridade
                        if (i != j) {

                            Vertice get2 = value.get(j);

                            if (get2.getClusterId() != -1) {
                                arestaAux = this.si.getAresta(key, get1, get2.getId(), get2.getSourceId());
                                //se retornar null é pq n existe a aresta em si. N tem similaridade medida previamente
                                if (arestaAux == null) { //mede similaridade
                                    AbstractStringMetric metric = new Levenshtein();
                                    float result1 = metric.getSimilarity(get1.getConteudoComparacao(), get2.getConteudoComparacao());
                                    float result2 = metric.getSimilarity(get1.getConteudoComparacao2(), get2.getConteudoComparacao2());
                                    float result = (float) (result1 + result2) / 2;
                                    if (result > limiar) {
                                        get2.setSimilarity(result);

                                        cluster.insereVertice(get2);

                                        si.verificaSimilaridade(result, key, get1.getId(), get1.getSourceId(), get2.getId(), get2.getSourceId(), key);

                                    }

                                } else {

                                    if (this.si.getAresta(key, get1, get2.getId(), get2.getSourceId()).getSimilaridade() > limiar) {
                                        get2.setSimilarity(this.si.getAresta(key, get1, get2.getId(), get2.getSourceId()).getSimilaridade());

                                        cluster.insereVertice(get2);
                                    }

                                }
                                //  }

                            }
                        }

                    }

                    FuncoesObjetivo obj = new FuncoesObjetivo(cluster, key, this.si);
                    Vertice verticeCoesaoCalculada = obj.getCoesaoLocalInicializacao();
                    if (verticeCoesaoCalculada != null) {
                        if (verticeCoesaoCalculada.getClusterId() == -1) {
                            // System.out.println(" Errado   &&&&&");
                            get1.setClusterId(controleClusterId);
                            verticeCoesaoCalculada.setClusterId(controleClusterId);
                            bi.insertVertice(key, get1);
                            bi.insertVertice(key, verticeCoesaoCalculada);

                        } else {
                            if (get1.getClusterId() != verticeCoesaoCalculada.getClusterId()) {
                                //   System.err.println(" %%%%%%%%%%%%%%%%%%%%%%%%%");
                                get1.setClusterId(verticeCoesaoCalculada.getClusterId());
                                // precisao.inserePairDuplicado("cddb", get1.getId(), "cddb", verticeCoesaoCalculada.getId());
                                bi.insertVertice(key, get1);
                                //bi.insertVertice(key, verticeCoesaoCalculada);
                            } else {
                                bi.insertVertice(key, get1);
                            }

                        }

                    } else {
                        get1.setClusterId(controleClusterId);
                        controleClusterId++;
                        bi.insertVertice(key, get1);

                    }
                }

            }

        }

        //bi2.printBlockIndex();
        precisao.clusterToPair(bi2);
        System.out.println("Precisao " + precisao.getPrecisao());
        System.out.println("Cobertura " + precisao.getCobertura());
        System.out.println("F-Measure " + (2 * ((precisao.getCobertura() * precisao.getPrecisao()) / (precisao.getPrecisao() + precisao.getCobertura()))));

        // System.out.println("**************Fim*************");
    }

    /**
     * Faz incremental. Com incrementos e correções
     *
     * @param bi2
     * @throws IOException
     */
    public void cluserizaOtimizado5(BlockIndex bi2) throws IOException {
        //verifica quais do BI2 estão armazenados no BI oficial e copia cluster
        Precisao precisao = new Precisao();
        precisao.InicializaGoldStandardCD();

        //recupera os clusters
        bi2 = inicializaClusterLocal(bi2);

        //clusteriza as instancias de bi2 que estao sem cluster. Acessa SI ou atualiza SI
        for (Map.Entry<String, ArrayList<Vertice>> entrySet : bi2.getMapaNomes().entrySet()) { //pra cada bloco
            String key = entrySet.getKey();
            int clusterRecuperadoBI;

            ArrayList<Vertice> value = entrySet.getValue();
            for (int i = 0; i < value.size(); i++) {//pra o array de cada bloco da entrada
                // System.err.println(" teste  ++++++++++++++" + controleApagar);
                Cluster cluster = new Cluster(si);
                Vertice get1 = value.get(i);

                double limiar = 0.9; //limiar
                Aresta arestaMax = null;
                Aresta arestaAux = null;
                double maxSimilaridade = 0;

                if (get1.getClusterId() == -1) {
                    for (int j = 0; j < value.size(); j++) {

                        if (i != j) {

//descobrir por instancia sem bloco a instancia de maior similaridade
                            Vertice get2 = value.get(j);
                            if (get2.getClusterId() != -1) {
                                arestaAux = this.si.getAresta(key, get1, get2.getId(), get2.getSourceId());
                                //se retornar null é pq n existe a aresta em si. N tem similaridade medida previamente
                                if (arestaAux == null) { //mede similaridade
                                    AbstractStringMetric metric = new Levenshtein();
                                    float result1 = metric.getSimilarity(get1.getConteudoComparacao(), get2.getConteudoComparacao());
                                    float result2 = metric.getSimilarity(get1.getConteudoComparacao2(), get2.getConteudoComparacao2());
                                    float result = (float) (result1 + result2) / 2;
                                    if (result > limiar) {
                                        get2.setSimilarity(result);

                                        cluster.insereVertice(get2);

                                        si.verificaSimilaridade(result, key, get1.getId(), get1.getSourceId(), get2.getId(), get2.getSourceId(), key);

                                    }

                                } else {

                                    if (this.si.getAresta(key, get1, get2.getId(), get2.getSourceId()).getSimilaridade() > limiar) {
                                        get2.setSimilarity(this.si.getAresta(key, get1, get2.getId(), get2.getSourceId()).getSimilaridade());

                                        cluster.insereVertice(get2);
                                    }

                                }
                                //  }

                            }
                        }

                    }

                    FuncoesObjetivo obj = new FuncoesObjetivo(cluster, key, this.si);
                    Vertice verticeCoesaoCalculada = obj.getCoesaoLocal();
                    if (verticeCoesaoCalculada != null) {
                        if (verticeCoesaoCalculada.getClusterId() == -1) {
                            System.out.println(" Errado   &&&&&");
                            get1.setClusterId(controleClusterId);
                            verticeCoesaoCalculada.setClusterId(controleClusterId);
                            bi.insertVertice(key, get1);
                            bi.insertVertice(key, verticeCoesaoCalculada);

                        } else {

                            if (get1.getClusterId() != verticeCoesaoCalculada.getClusterId()) {
                                get1.setClusterId(verticeCoesaoCalculada.getClusterId());
                                // precisao.inserePairDuplicado("cddb", get1.getId(), "cddb", verticeCoesaoCalculada.getId());
                                bi.insertVertice(key, get1);
                                //bi.insertVertice(key, verticeCoesaoCalculada);
                                //Inseriu em um cluster existente. Deve-se corrigir o cluster
                                System.err.println(" %%%%%%%%%%%%%%%%%%%%%%%%%");
                                correcao(bi2, key, get1);
                                //bi.insertVertice(key, verticeCoesaoCalculada);
                            } else {
                                bi.insertVertice(key, get1);
                            }
                        }

                    } else {
                        get1.setClusterId(controleClusterId);
                        controleClusterId++;
                        bi.insertVertice(key, get1);

                    }
                }

            }

        }

        //bi2.printBlockIndex();
        precisao.clusterToPair(bi2);
        System.out.println("Precisao " + precisao.getPrecisao());
        System.out.println("Cobertura " + precisao.getCobertura());
        System.out.println("F-Measure " + (2 * ((precisao.getCobertura() * precisao.getPrecisao()) / (precisao.getPrecisao() + precisao.getCobertura()))));

        // System.out.println("**************Fim*************");
    }

    
}
