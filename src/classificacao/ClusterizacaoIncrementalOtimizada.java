/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package classificacao;

import avaliacao.FuncoesObjetivo;
import Index.BlockIndex;
import Index.SimilarityIndex;
import avaliacao.Cluster;
import avaliacao.Precisao;
import graph.Aresta;
import graph.Vertice;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;
import uk.ac.shef.wit.simmetrics.similaritymetrics.AbstractStringMetric;
import uk.ac.shef.wit.simmetrics.similaritymetrics.Jaro;
import uk.ac.shef.wit.simmetrics.similaritymetrics.Levenshtein;

/**
 *
 * @author Priscilla
 */
public class ClusterizacaoIncrementalOtimizada {

    BlockIndex bi;
    SimilarityIndex si;
    int controleClusterId;

    public ClusterizacaoIncrementalOtimizada(BlockIndex bi, SimilarityIndex si) {
        this.bi = bi;
        this.si = si;
        this.controleClusterId = 0;

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

    public void funcaoObjetivo(BlockIndex bi2, String key, Vertice v1) {
        ArrayList<Vertice> vertices = bi2.getMapaNomes().get(key);

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
     * Muito baixa a precisão. Coloca muitas tuplas como duplicadas, mas não
     * são. Insere so os que n tem cluster. Sem corrigir nada. É incremental
     *
     * @param bi2
     * @throws IOException
     */
    public void cluserizaOtimizado1(BlockIndex bi2) throws IOException {
        //verifica quais do BI2 estão armazenados no BI oficial e copia cluster
        Precisao precisao = new Precisao();
        precisao.InicializaGoldStandardCD();

        //recupera os clusters
        inicializaClusterLocal(bi2);

        //clusteriza as instancias de bi2 que estao sem cluster. Acessa SI ou atualiza SI
        for (Map.Entry<String, ArrayList<Vertice>> entrySet : bi2.getMapaNomes().entrySet()) { //pra cada bloco
            String key = entrySet.getKey();
            int clusterRecuperadoBI;

            ArrayList<Vertice> value = entrySet.getValue();
            for (int i = 0; i < value.size(); i++) {//pra o array de cada bloco da entrada
                // System.err.println(" teste  ++++++++++++++" + controleApagar);
                Cluster cluster = new Cluster(si);
                Vertice get1 = value.get(i);
                if (get1.getClusterId() == -1) {
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
                                get2.setSimilarity(result);

                                cluster.insereVertice(get2);

                                si.verificaSimilaridade(result, key, get1.getId(), get1.getSourceId(), get2.getId(), get2.getSourceId(), key);

                            } else {
                                get2.setSimilarity(this.si.getAresta(key, get1, get2.getId(), get2.getSourceId()).getSimilaridade());

                                cluster.insereVertice(get2);

                            }
                            //  }

                        }

                    }

                }

                FuncoesObjetivo obj = new FuncoesObjetivo(cluster, key, this.si);
                Vertice verticeCoesaoCalculada = obj.getCoesaoLocal();
                if (verticeCoesaoCalculada != null) {
                    if (verticeCoesaoCalculada.getClusterId() == -1) {
                        get1.setClusterId(controleClusterId);
                        bi.insertVertice(key, get1);
                        bi.insertVertice(key, verticeCoesaoCalculada);
                        bi2.setclusterId(verticeCoesaoCalculada.getId(), key, verticeCoesaoCalculada.getSourceId(), controleClusterId);
                        // precisao.inserePairDuplicado("cddb", get1.getId(), "cddb", verticeCoesaoCalculada.getId());
                        controleClusterId++;

                    } else {
                        get1.setClusterId(verticeCoesaoCalculada.getClusterId());
                        // precisao.inserePairDuplicado("cddb", get1.getId(), "cddb", verticeCoesaoCalculada.getId());
                        bi.insertVertice(key, get1);

                    }

                } else {
                    get1.setClusterId(controleClusterId);
                    controleClusterId++;
                    bi.insertVertice(key, get1);

                }

            }

        }

        //bi2.printBlockIndex();
        precisao.clusterToPair(bi2);
        System.out.println("Precisao " + precisao.getPrecisao());
        System.out.println("Cobertura " + precisao.getCobertura());
        System.out.println("F-Measure " + ((2 * precisao.getPrecisao() * precisao.getCobertura()) / precisao.getPrecisao() + precisao.getCobertura()));

        // System.out.println("**************Fim*************");
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

    public void cluserizaOtimizado4Cora(BlockIndex bi2) throws IOException {
        //verifica quais do BI2 estão armazenados no BI oficial e copia cluster
        Precisao precisao = new Precisao();
        precisao.InicializaGoldStandardCora();

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
     * Faz em lote. Tradicional. Sem incrementos. recebe toda a entrada e
     * clusteriza. Opcao 1 com precisao 0.7 e cobertura 0.4. 141 certo e 39
     * errado
     *
     * @param bi2
     * @throws IOException
     */
    public void cluserizaOtimizado2(BlockIndex bi2) throws IOException {
        //verifica quais do BI2 estão armazenados no BI oficial e copia cluster
        Precisao precisao = new Precisao();
        precisao.InicializaGoldStandardCD();

        //recupera os clusters
        inicializaClusterLocalLote(bi2);

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
                Vertice verticeCoesaoCalculada = obj.getCoesaoLocalInicializacao();
                if (verticeCoesaoCalculada != null) {
                    if (verticeCoesaoCalculada.getClusterId() == -1) {
                        bi.insertVertice(key, get1);

                    } else {
                        get1.setClusterId(verticeCoesaoCalculada.getClusterId());
                        // precisao.inserePairDuplicado("cddb", get1.getId(), "cddb", verticeCoesaoCalculada.getId());
                        bi.insertVertice(key, get1);
                        bi.insertVertice(key, verticeCoesaoCalculada);

                    }

                } else {
                    //get1.setClusterId(controleClusterId);
                    //controleClusterId++;
                    bi.insertVertice(key, get1);

                }

            }

        }

        //bi2.printBlockIndex();
        precisao.clusterToPair(bi2);
        System.out.println("Precisao " + precisao.getPrecisao());
        System.out.println("Cobertura 7 " + precisao.getCobertura());
        System.out.println("F-Measure " + ((2 * precisao.getCobertura() * precisao.getCobertura()) / precisao.getPrecisao() + precisao.getCobertura()));

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
     * Faz incremental. Com incrementos. Mas sem corrigir erros
     *
     * @param bi2
     * @throws IOException
     */
    public void cluserizaOtimizado3Ingenua(BlockIndex bi2) throws IOException {
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
                                    si.verificaSimilaridade(result, key, get1.getId(), get1.getSourceId(), get2.getId(), get2.getSourceId(), key);

                                    if (result > limiar) {
                                        get2.setSimilarity(result);

                                        cluster.insereVertice(get2);


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

    public void singleLink(BlockIndex bi2) throws IOException {
        //verifica quais do BI2 estão armazenados no BI oficial e copia cluster
        Precisao precisao = new Precisao();
        precisao.InicializaGoldStandardCD();

        //recupera os clusters
        bi2 = inicializaClusterLocalIncremental(bi2);

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
                            arestaAux = this.si.getAresta(key, get1, get2.getId(), get2.getSourceId());
                            //se retornar null é pq n existe a aresta em si. N tem similaridade medida previamente
                            if (arestaAux == null) { //mede similaridade
                               // AbstractStringMetric metric = new Levenshtein();
                                 AbstractStringMetric metric = new Levenshtein();
                                float result1 = metric.getSimilarity(get1.getConteudoComparacao(), get2.getConteudoComparacao());
                                float result2 = metric.getSimilarity(get1.getConteudoComparacao2(), get2.getConteudoComparacao2());
                                float result = (float) (result1 + result2) / 2;
                                //float result = (float) (result1 + result2) / 2;
                                if (result > limiar) {
                                    si.verificaSimilaridade(result, key, get1.getId(), get1.getSourceId(), get2.getId(), get2.getSourceId(), key);

                                    if (result > maxSimilaridade) {
                                        maxSimilaridade = result;
                                        //System.err.print(" Teste agraaa +++++  ");
                                        arestaMax = this.si.getAresta(key, get1, get2.getId(), get2.getSourceId());
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
                            //  }

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
            copiaBlocoFinalToClusterGlobal(bi2,key);

        }

        //bi2.printBlockIndex();
        precisao.clusterToPair(bi2);
        System.out.println("Precisao " + precisao.getPrecisao());
        System.out.println("Cobertura " + precisao.getCobertura());
        System.out.println("F-Measure " + (2 * ((precisao.getCobertura() * precisao.getPrecisao()) / (precisao.getPrecisao() + precisao.getCobertura()))));

        // System.out.println("**************Fim*************");
    }

    private void copiaBlocoFinalToClusterGlobal(BlockIndex bi2, String key2) {
        
      ArrayList <Vertice> value =  bi2.getMapaNomes().get(key2);
            for (int i = 0; i < value.size(); i++) {
                Vertice get = value.get(i);
               bi.insertVertice(key2, get);

            }

    }

}
