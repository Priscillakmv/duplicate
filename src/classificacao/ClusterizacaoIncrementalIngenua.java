/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package classificacao;

import Index.BlockIndex;
import Index.SimilarityIndex;
import avaliacao.Precisao;
import graph.Aresta;
import graph.Vertice;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import uk.ac.shef.wit.simmetrics.similaritymetrics.AbstractStringMetric;
import uk.ac.shef.wit.simmetrics.similaritymetrics.Levenshtein;

/**
 *
 * @author Priscilla
 */
public class ClusterizacaoIncrementalIngenua {

    BlockIndex bi;
    SimilarityIndex si;
    int controleClusterId;

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

    public ClusterizacaoIncrementalIngenua(BlockIndex bi) {
        this.bi = bi;

        controleClusterId = 0;
    }

    /**
     *
     * @param bi
     * @param si
     */
    public ClusterizacaoIncrementalIngenua(BlockIndex bi, SimilarityIndex si) {
        this.bi = bi;
        this.si = si;
        controleClusterId = 0;
    }

    public void cluseriza3(BlockIndex bi2) {
        for (Map.Entry<String, ArrayList<Vertice>> entrySet : bi2.getMapaNomes().entrySet()) {

            String key = entrySet.getKey();
            int clusterRecuperadoBI;
            ArrayList<Vertice> value = entrySet.getValue();
            Aresta arestaAux = null;
            double limiar = 0.9; //limiar
            Aresta arestaMax = null;

            double maxSimilaridade = 0;
            for (int i = 0; i < value.size(); i++) {
                Vertice get = value.get(i);
                //clusterRecuperadoBI = this.bi.getclusterId(get.getId(), key, get.getSourceId());
                // arestaAux = this.si.getAresta(key, get, get2.getId(), get2.getSourceId());
                Vertice aux = si.getVerticeOrigem(key, get.getId(), get.getSourceId());
                if (aux != null) {
                    int clusterRecuperadoSI = aux.getClusterId();
                    if (clusterRecuperadoSI != -1) {
                        get.setClusterId(clusterRecuperadoSI);

                    } else {
                        //tá no SI, mas n tem cluster. CAlcula similaridade

                        for (int j = 0; j < value.size(); j++) {//descobrir por instancia sem bloco a instancia de maior similaridade
                            if (i != j) {
                                Vertice get2 = value.get(j);

                                //se retornar null é pq n existe a aresta em si.N tem similaridade medida previamente
                                arestaAux = this.si.getAresta(key, get, get2.getId(), get2.getSourceId());
                                if (arestaAux == null) { //mede similaridade
                                    AbstractStringMetric metric = new Levenshtein();
                                    float result = metric.getSimilarity(get.getConteudoComparacao(), get2.getConteudoComparacao());
                                    si.verificaSimilaridade(result, key, get.getId(), get.getSourceId(), get2.getId(), get2.getSourceId(), key);
                                    //  System.err.println(" saida o que entrou" + get2.getId());
                                    if (result > limiar && result > maxSimilaridade) {
                                        maxSimilaridade = result;
                                        //System.err.print(" Teste agraaa +++++  ");
                                        arestaMax = this.si.getAresta(key, get, get2.getId(), get2.getSourceId());
                                        //System.err.println(" teste continua  " + arestaMax.getDestino().getId());

                                    }

                                } else {
                                    double similaridadeRecuperada = arestaAux.getSimilaridade();
                                    if (similaridadeRecuperada > limiar && similaridadeRecuperada > maxSimilaridade) {
                                        arestaMax = arestaAux;
                                        maxSimilaridade = similaridadeRecuperada;
                                        //tem que recuperar a similaridade no SI

                                    }

                                    //se ja tem a similaridades
                                }
                                //  si.getAresta(key, get, key, key)
                                // if (si.get) {

                            }

                        }
                        if (arestaMax != null) {//encontrou uma instancia muito similar. REcuperar cluster ou colocar criar novo cluster
                            int clusterRecuperadoBI2 = bi.getclusterId(arestaMax.getDestino().getId(), key, arestaMax.getDestino().getSourceId());
                            if (clusterRecuperadoBI2 == -1) { //a instancia similar n tem cluster. Entao cria cluster
                                get.setClusterId(controleClusterId);
                                arestaMax.getDestino().setClusterId(controleClusterId);
                                controleClusterId++;
                                // this.preencheSIeBI(limiar, key, key, key, key, key, key);
                                //bi.insertVertice(key, get);
                                //bi.insertVertice(key, arestaMax.getDestino());

                            } else {
                                get.setClusterId(clusterRecuperadoBI2);
                                //bi.insertVertice(key, get);

                            }  // }

                        } //else { //não tem similaridade acima do limiar com nenhuma outra instancia
                        //  get1.setClusterId(controleClusterId);

                    }
                } else {
                    for (int j = 0; j < value.size(); j++) {//descobrir por instancia sem bloco a instancia de maior similaridade
                        if (i != j) {
                            Vertice get2 = value.get(j);

                            //se retornar null é pq n existe a aresta em si.N tem similaridade medida previamente
                            arestaAux = this.si.getAresta(key, get, get2.getId(), get2.getSourceId());
                            if (arestaAux == null) { //mede similaridade
                                AbstractStringMetric metric = new Levenshtein();
                                float result = metric.getSimilarity(get.getConteudoComparacao(), get2.getConteudoComparacao());
                                si.verificaSimilaridade(result, key, get.getId(), get.getSourceId(), get2.getId(), get2.getSourceId(), key);
                                //  System.err.println(" saida o que entrou" + get2.getId());
                                if (result > limiar && result > maxSimilaridade) {
                                    maxSimilaridade = result;
                                    //System.err.print(" Teste agraaa +++++  ");
                                    arestaMax = this.si.getAresta(key, get, get2.getId(), get2.getSourceId());
                                    //System.err.println(" teste continua  " + arestaMax.getDestino().getId());

                                }

                            } else {
                                double similaridadeRecuperada = arestaAux.getSimilaridade();
                                if (similaridadeRecuperada > limiar && similaridadeRecuperada > maxSimilaridade) {
                                    arestaMax = arestaAux;
                                    maxSimilaridade = similaridadeRecuperada;
                                    //tem que recuperar a similaridade no SI

                                }

                                //se ja tem a similaridades
                            }
                            //  si.getAresta(key, get, key, key)
                            // if (si.get) {

                        }
                        if (arestaMax != null) {//encontrou uma instancia muito similar. REcuperar cluster ou colocar criar novo cluster
                            int clusterRecuperadoBI2 = bi.getclusterId(arestaMax.getDestino().getId(), key, arestaMax.getDestino().getSourceId());
                            if (clusterRecuperadoBI2 == -1) { //a instancia similar n tem cluster. Entao cria cluster
                                get.setClusterId(controleClusterId);
                                arestaMax.getDestino().setClusterId(controleClusterId);
                                controleClusterId++;
                                // this.preencheSIeBI(limiar, key, key, key, key, key, key);
                                //bi.insertVertice(key, get);
                                //bi.insertVertice(key, arestaMax.getDestino());

                            } else {
                                get.setClusterId(clusterRecuperadoBI2);
                                //bi.insertVertice(key, get);

                            }  // }

                        } //else { //não tem similaridade acima do limiar com nenhuma outra instancia
                        //  get1.setClusterId(controleClusterId);

                    }
                    //calcula similaridades
                }

            }

        }
        //clusteriza as instancias de bi2 que estao sem cluster. Acessa SI ou atualiza SI

        System.out.println("**************Fim*************");

    }

    /**
     *
     * @param similarity
     * @param keyBlockpair1
     * @param pk1
     * @param cd
     * @param pk2
     * @param cd0
     * @param keyBlockpair2
     */
    public void preencheSIeBI(double similarity, String keyBlockpair1, String pk1, String cd, String pk2, String cd0, String keyBlockpair2) {
        int saida = si.verificaSimilaridade(similarity, keyBlockpair1, pk1, cd, pk2, cd0, keyBlockpair2);
        if (saida == -3) { //n tem os dois vertices em SI
            Vertice v1 = new Vertice(pk1, cd, controleClusterId);
            Vertice v2 = new Vertice(pk2, cd0, controleClusterId);
            //pegar o cluster sempre no bi
            if (similarity > 0.9) {
                bi.insertVertice(cd, v1);
                bi.insertVertice(cd0, v2);
                this.controleClusterId++;
            }

        } else if (saida == -2) {
            Vertice v2 = new Vertice(pk2, cd0, bi.getclusterId(pk1, keyBlockpair1, cd));
            if (similarity > 0.9) {
                bi.insertVertice(cd0, v2);
            }

        } else if (saida == -1) {
            Vertice v1 = new Vertice(pk1, cd, bi.getclusterId(pk2, keyBlockpair2, cd0));
            if (similarity > 0.9) {
                bi.insertVertice(cd, v1);
            }

        }
    }

    public void cluseriza2(BlockIndex bi2) {
        //verifica quais do BI2 estão armazenados no BI oficial e copia cluster
        // System.err.println(" 2 " + bi2.getNumeroElementos());
        //System.err.println(" 1 " + bi.getNumeroElementos());
        for (Map.Entry<String, ArrayList<Vertice>> entrySet : bi2.getMapaNomes().entrySet()) {

            String key = entrySet.getKey();
            int clusterRecuperadoBI;
            ArrayList<Vertice> value = entrySet.getValue();
            for (int i = 0; i < value.size(); i++) {
                Vertice get = value.get(i);
                clusterRecuperadoBI = this.bi.getclusterId(get.getId(), key, get.getSourceId());
                if (clusterRecuperadoBI != -1) {
                    get.setClusterId(clusterRecuperadoBI);

                } else {
                    double limiar = 0.9; //limiar
                    Aresta arestaMax = null;
                    Aresta arestaAux = null;
                    double maxSimilaridade = 0;

                    for (int j = 0; j < value.size(); j++) {//descobrir por instancia sem bloco a instancia de maior similaridade
                        if (i != j) {
                            Vertice get2 = value.get(j);

                            //se retornar null é pq n existe a aresta em si.N tem similaridade medida previamente
                            arestaAux = this.si.getAresta(key, get, get2.getId(), get2.getSourceId());
                            if (arestaAux == null) { //mede similaridade
                                AbstractStringMetric metric = new Levenshtein();
                                float result = metric.getSimilarity(get.getConteudoComparacao(), get2.getConteudoComparacao());
                                si.verificaSimilaridade(result, key, get.getId(), get.getSourceId(), get2.getId(), get2.getSourceId(), key);
                                //  System.err.println(" saida o que entrou" + get2.getId());
                                if (result > limiar && result > maxSimilaridade) {
                                    maxSimilaridade = result;
                                    //System.err.print(" Teste agraaa +++++  ");
                                    arestaMax = this.si.getAresta(key, get, get2.getId(), get2.getSourceId());
                                    //System.err.println(" teste continua  " + arestaMax.getDestino().getId());

                                }

                            } else {
                                double similaridadeRecuperada = arestaAux.getSimilaridade();
                                if (similaridadeRecuperada > limiar && similaridadeRecuperada > maxSimilaridade) {
                                    arestaMax = arestaAux;
                                    maxSimilaridade = similaridadeRecuperada;
                                    //tem que recuperar a similaridade no SI

                                }

                                //se ja tem a similaridades
                            }
                            //  si.getAresta(key, get, key, key)
                            // if (si.get) {

                        }

                    }
                    if (arestaMax != null) {//encontrou uma instancia muito similar. REcuperar cluster ou colocar criar novo cluster
                        int clusterRecuperadoBI2 = bi.getclusterId(arestaMax.getDestino().getId(), key, arestaMax.getDestino().getSourceId());
                        if (clusterRecuperadoBI2 == -1) { //a instancia similar n tem cluster. Entao cria cluster
                            get.setClusterId(controleClusterId);
                            arestaMax.getDestino().setClusterId(controleClusterId);
                            controleClusterId++;
                            // this.preencheSIeBI(limiar, key, key, key, key, key, key);
                            bi.insertVertice(key, get);
                            bi.insertVertice(key, arestaMax.getDestino());

                        } else {
                            get.setClusterId(clusterRecuperadoBI2);
                            bi.insertVertice(key, get);

                        }  // }

                    } //else { //não tem similaridade acima do limiar com nenhuma outra instancia
                    //  get1.setClusterId(controleClusterId);
                    //controleClusterId++;
                    // bi.insertVertice(key, get1);
                    //}

                }

            }

        }
        //clusteriza as instancias de bi2 que estao sem cluster. Acessa SI ou atualiza SI

        System.out.println("**************Fim*************");

    }

    /**
     *
     * @param bi2 é um bloco criado na entrada da consulta para formatar a
     * entrada
     */
    public void cluserizaAcessandoBIeSI(BlockIndex bi2) {
        //verifica quais do BI2 estão armazenados no BI oficial e copia cluster
        // System.err.println(" 2 " + bi2.getNumeroElementos());
        //System.err.println(" 1 " + bi.getNumeroElementos());
        for (Map.Entry<String, ArrayList<Vertice>> entrySet : bi2.getMapaNomes().entrySet()) {

            String key = entrySet.getKey();
            int clusterRecuperadoBI;
            ArrayList<Vertice> value = entrySet.getValue();
            for (int i = 0; i < value.size(); i++) {
                Vertice get = value.get(i);
                clusterRecuperadoBI = this.bi.getclusterId(get.getId(), key, get.getSourceId());
                if (clusterRecuperadoBI != -1) {
                    get.setClusterId(clusterRecuperadoBI);

                }

            }

        }
        //clusteriza as instancias de bi2 que estao sem cluster. Acessa SI ou atualiza SI

        for (Map.Entry<String, ArrayList<Vertice>> entrySet : bi2.getMapaNomes().entrySet()) { //pra cada bloco
            String key = entrySet.getKey();
            int clusterRecuperadoBI;

            ArrayList<Vertice> value = entrySet.getValue();
            for (int i = 0; i < value.size(); i++) {//pra o array de cada bloco da entrada
                // System.err.println(" teste  ++++++++++++++" + controleApagar);

                Vertice get1 = value.get(i);
                if (get1.getClusterId() == -1) {
                    double limiar = 0.9; //limiar
                    Aresta arestaMax = null;
                    Aresta arestaAux = null;
                    double maxSimilaridade = 0;

                    for (int j = 0; j < value.size(); j++) {//descobrir por instancia sem bloco a instancia de maior similaridade
                        if (i != j) {
                            Vertice get2 = value.get(j);

                            //se retornar null é pq n existe a aresta em si.N tem similaridade medida previamente
                            arestaAux = this.si.getAresta(key, get1, get2.getId(), get2.getSourceId());
                            if (arestaAux == null) { //mede similaridade
                                AbstractStringMetric metric = new Levenshtein();
                                float result = metric.getSimilarity(get1.getConteudoComparacao(), get2.getConteudoComparacao());
                                si.verificaSimilaridade(result, key, get1.getId(), get1.getSourceId(), get2.getId(), get2.getSourceId(), key);
                                //  System.err.println(" saida o que entrou" + get2.getId());
                                if (result > limiar && result > maxSimilaridade) {
                                    maxSimilaridade = result;
                                    //System.err.print(" Teste agraaa +++++  ");
                                    arestaMax = this.si.getAresta(key, get1, get2.getId(), get2.getSourceId());
                                    //System.err.println(" teste continua  " + arestaMax.getDestino().getId());

                                }

                            } else {
                                double similaridadeRecuperada = arestaAux.getSimilaridade();
                                if (similaridadeRecuperada > limiar && similaridadeRecuperada > maxSimilaridade) {
                                    arestaMax = arestaAux;
                                    maxSimilaridade = similaridadeRecuperada;
                                    //tem que recuperar a similaridade no SI

                                }

                                //se ja tem a similaridades
                            }
                            //  si.getAresta(key, get, key, key)
                            // if (si.get) {

                        }

                    }
                    if (arestaMax != null) {//encontrou uma instancia muito similar. REcuperar cluster ou colocar criar novo cluster
                        int clusterRecuperadoBI2 = bi.getclusterId(arestaMax.getDestino().getId(), key, arestaMax.getDestino().getSourceId());
                        if (clusterRecuperadoBI2 == -1) { //a instancia similar n tem cluster. Entao cria cluster
                            get1.setClusterId(controleClusterId);
                            arestaMax.getDestino().setClusterId(controleClusterId);
                            controleClusterId++;
                            // this.preencheSIeBI(limiar, key, key, key, key, key, key);
                            bi.insertVertice(key, get1);
                            bi.insertVertice(key, arestaMax.getDestino());

                        } else {
                            get1.setClusterId(clusterRecuperadoBI2);
                            bi.insertVertice(key, get1);

                        }  // }

                    } //else { //não tem similaridade acima do limiar com nenhuma outra instancia
                    //  get1.setClusterId(controleClusterId);
                    //controleClusterId++;
                    // bi.insertVertice(key, get1);
                    //}

                }

            }

            //clusterRecuperadoBI = this.bi.getclusterId(get.getId(),key, get.getSourceId());
            //  int
            // AbstractStringMetric similaridade = new Levenshtein();
        }//pra cada bloco
        System.out.println("**************Fim*************");

    }

    public void cluseriza4(BlockIndex bi2) throws IOException {
        //verifica quais do BI2 estão armazenados no BI oficial e copia cluster
        System.err.println(" 2 " + bi2.getNumeroElementos());
       System.err.println(" 1 " + bi.getNumeroElementos());
        Precisao precisao = new Precisao();
        precisao.InicializaGoldStandardCD();
        
        
        for (Map.Entry<String, ArrayList<Vertice>> entrySet : bi2.getMapaNomes().entrySet()) {

            String key = entrySet.getKey();
            int clusterRecuperadoBI;
            ArrayList<Vertice> value = entrySet.getValue();
            for (int i = 0; i < value.size(); i++) {
                Vertice get = value.get(i);
                clusterRecuperadoBI = this.bi.getclusterId(get.getId(), key, get.getSourceId());
                if (clusterRecuperadoBI != -1) {
                    get.setClusterId(clusterRecuperadoBI);

                }

            }

        }
        //clusteriza as instancias de bi2 que estao sem cluster. Acessa SI ou atualiza SI

        for (Map.Entry<String, ArrayList<Vertice>> entrySet : bi2.getMapaNomes().entrySet()) { //pra cada bloco
            String key = entrySet.getKey();
            int clusterRecuperadoBI;

            ArrayList<Vertice> value = entrySet.getValue();
            for (int i = 0; i < value.size(); i++) {//pra o array de cada bloco da entrada
                // System.err.println(" teste  ++++++++++++++" + controleApagar);

                Vertice get1 = value.get(i);
                if (get1.getClusterId() == -1) {
                    double limiar = 0.9; //limiar
                    Aresta arestaMax = null;
                    Aresta arestaAux = null;
                    double maxSimilaridade = 0;

                    for (int j = i + 1; j < value.size(); j++) {//descobrir por instancia sem bloco a instancia de maior similaridade
                        if (i != j) {
                            Vertice get2 = value.get(j);

                            //se retornar null é pq n existe a aresta em si.N tem similaridade medida previamente
                            arestaAux = this.si.getAresta(key, get1, get2.getId(), get2.getSourceId());
                            if (arestaAux == null) { //mede similaridade
                                AbstractStringMetric metric = new Levenshtein();
                                float result1 = metric.getSimilarity(get1.getConteudoComparacao(), get2.getConteudoComparacao());
                                float result2 = metric.getSimilarity(get1.getConteudoComparacao2(), get2.getConteudoComparacao2());
                                float result = (float) (result1 + result2) / 2;
                                // System.err.println("  limiar" + );
                                if (result > limiar) {
                                    si.verificaSimilaridade(result, key, get1.getId(), get1.getSourceId(), get2.getId(), get2.getSourceId(), key);
                                    if (result > maxSimilaridade) {
                                        maxSimilaridade = result;
                                        //System.err.print(" Teste agraaa +++++  ");
                                        arestaMax = this.si.getAresta(key, get1, get2.getId(), get2.getSourceId());
                                        //System.err.println(" teste continua  " + arestaMax.getDestino().getId());

                                    }

                                }

                                //  System.err.println(" saida o que entrou" + get2.getId());
                            } else {
                                double similaridadeRecuperada = arestaAux.getSimilaridade();
                                if (similaridadeRecuperada > limiar && similaridadeRecuperada > maxSimilaridade) {
                                    arestaMax = arestaAux;
                                    maxSimilaridade = similaridadeRecuperada;
                                    //tem que recuperar a similaridade no SI

                                }

                                //se ja tem a similaridades
                            }
                            //  si.getAresta(key, get, key, key)
                            // if (si.get) {

                        }

                    }
                    if (arestaMax != null) {//encontrou uma instancia muito similar. REcuperar cluster ou colocar criar novo cluster
                        int clusterRecuperadoBI2 = bi.getclusterId(arestaMax.getDestino().getId(), key, arestaMax.getDestino().getSourceId());
                        if (clusterRecuperadoBI2 == -1) { //a instancia similar n tem cluster. Entao cria cluster
                            get1.setClusterId(controleClusterId);
                            arestaMax.getDestino().setClusterId(controleClusterId);
                            controleClusterId++;
                            // this.preencheSIeBI(limiar, key, key, key, key, key, key);
                               System.out.println(" Tamanho BI Antes " + bi.getNumeroElementos() );
                            bi.insertVertice(key, get1);
                            bi.insertVertice(key, arestaMax.getDestino());
                            precisao.inserePairDuplicado("cddb", get1.getId(), "cddb", arestaMax.getDestino().getId());
                             System.out.println(" Tamanho BI Depois " + bi.getNumeroElementos() );

                        } else {
                            precisao.inserePairDuplicado("cddb", get1.getId(), "cddb", arestaMax.getDestino().getId());
                            get1.setClusterId(clusterRecuperadoBI2);
                             System.out.println(" Tamanho BI Antes " + bi.getNumeroElementos() );
                            bi.insertVertice(key, get1);
                              System.out.println(" Tamanho BI Depois " + bi.getNumeroElementos() );

                        }  // }

                    } //else { //não tem similaridade acima do limiar com nenhuma outra instancia
                    //  get1.setClusterId(controleClusterId);
                    //controleClusterId++;
                    // bi.insertVertice(key, get1);
                    //}

                }

            }

            //clusterRecuperadoBI = this.bi.getclusterId(get.getId(),key, get.getSourceId());
            //  int
            // AbstractStringMetric similaridade = new Levenshtein();
        }//pra cada bloco
        System.out.println("Precisao " + precisao.getPrecisao());
        System.out.println("Cobertura " + precisao.getCobertura());

        System.out.println("**************Fim*************");

    }

    public void cluseriza42(BlockIndex bi2) {
        //verifica quais do BI2 estão armazenados no BI oficial e copia cluster
        // System.err.println(" 2 " + bi2.getNumeroElementos());
        //System.err.println(" 1 " + bi.getNumeroElementos());
        for (Map.Entry<String, ArrayList<Vertice>> entrySet : bi2.getMapaNomes().entrySet()) {

            String key = entrySet.getKey();
            int clusterRecuperadoBI;
            ArrayList<Vertice> value = entrySet.getValue();
            for (int i = 0; i < value.size(); i++) {
                Vertice get = value.get(i);
                clusterRecuperadoBI = this.bi.getclusterId(get.getId(), key, get.getSourceId());
                if (clusterRecuperadoBI != -1) {
                    get.setClusterId(clusterRecuperadoBI);

                }

            }

        }
        //clusteriza as instancias de bi2 que estao sem cluster. Acessa SI ou atualiza SI

        for (Map.Entry<String, ArrayList<Vertice>> entrySet : bi2.getMapaNomes().entrySet()) { //pra cada bloco
            String key = entrySet.getKey();
            int clusterRecuperadoBI;

            ArrayList<Vertice> value = entrySet.getValue();
            for (int i = 0; i < value.size(); i++) {//pra o array de cada bloco da entrada
                // System.err.println(" teste  ++++++++++++++" + controleApagar);

                Vertice get1 = value.get(i);
                if (get1.getClusterId() == -1) {
                    double limiar = 0.9; //limiar
                    Aresta arestaMax = null;
                    Aresta arestaAux = null;
                    double maxSimilaridade = 0;

                    for (int j = 0; j < value.size(); j++) {//descobrir por instancia sem bloco a instancia de maior similaridade
                        if (i != j) {
                            Vertice get2 = value.get(j);

                            //se retornar null é pq n existe a aresta em si.N tem similaridade medida previamente
                            arestaAux = this.si.getAresta(key, get1, get2.getId(), get2.getSourceId());
                            if (arestaAux == null) { //mede similaridade
                                AbstractStringMetric metric = new Levenshtein();
                                float result = metric.getSimilarity(get1.getConteudoComparacao(), get2.getConteudoComparacao());
                                //  float result2 = metric.getSimilarity(get1.getConteudoComparacao2(), get2.getConteudoComparacao2());
                                //float result = (float) (result1 +result2)/2;
                                // System.err.println("  limiar" + );
                                if (result > limiar) {
                                    si.verificaSimilaridade(result, key, get1.getId(), get1.getSourceId(), get2.getId(), get2.getSourceId(), key);
                                    if (result > maxSimilaridade) {
                                        maxSimilaridade = result;
                                        //System.err.print(" Teste agraaa +++++  ");
                                        arestaMax = this.si.getAresta(key, get1, get2.getId(), get2.getSourceId());
                                        //System.err.println(" teste continua  " + arestaMax.getDestino().getId());

                                    }

                                }

                                //  System.err.println(" saida o que entrou" + get2.getId());
                            } else {
                                double similaridadeRecuperada = arestaAux.getSimilaridade();
                                if (similaridadeRecuperada > limiar && similaridadeRecuperada > maxSimilaridade) {
                                    arestaMax = arestaAux;
                                    maxSimilaridade = similaridadeRecuperada;
                                    //tem que recuperar a similaridade no SI

                                }

                                //se ja tem a similaridades
                            }
                            //  si.getAresta(key, get, key, key)
                            // if (si.get) {

                        }

                    }
                    if (arestaMax != null) {//encontrou uma instancia muito similar. REcuperar cluster ou colocar criar novo cluster
                        int clusterRecuperadoBI2 = bi.getclusterId(arestaMax.getDestino().getId(), key, arestaMax.getDestino().getSourceId());
                        if (clusterRecuperadoBI2 == -1) { //a instancia similar n tem cluster. Entao cria cluster
                            get1.setClusterId(controleClusterId);
                            arestaMax.getDestino().setClusterId(controleClusterId);
                            controleClusterId++;
                            // this.preencheSIeBI(limiar, key, key, key, key, key, key);
                            bi.insertVertice(key, get1);
                            bi.insertVertice(key, arestaMax.getDestino());

                        } else {
                            get1.setClusterId(clusterRecuperadoBI2);
                            bi.insertVertice(key, get1);

                        }  // }

                    } //else { //não tem similaridade acima do limiar com nenhuma outra instancia
                    //  get1.setClusterId(controleClusterId);
                    //controleClusterId++;
                    // bi.insertVertice(key, get1);
                    //}

                }

            }

            //clusterRecuperadoBI = this.bi.getclusterId(get.getId(),key, get.getSourceId());
            //  int
            // AbstractStringMetric similaridade = new Levenshtein();
        }//pra cada bloco
        System.out.println("**************Fim*************");

    }

    public void cluseriza5(BlockIndex bi2) {
        //verifica quais do BI2 estão armazenados no BI oficial e copia cluster
        // System.err.println(" 2 " + bi2.getNumeroElementos());
        //System.err.println(" 1 " + bi.getNumeroElementos());
        for (Map.Entry<String, ArrayList<Vertice>> entrySet : bi2.getMapaNomes().entrySet()) {

            String key = entrySet.getKey();
            int clusterRecuperadoBI;
            ArrayList<Vertice> value = entrySet.getValue();
            for (int i = 0; i < value.size(); i++) {
                Vertice get = value.get(i);
                clusterRecuperadoBI = this.bi.getclusterId(get.getId(), key, get.getSourceId());
                if (clusterRecuperadoBI != -1) {
                    get.setClusterId(clusterRecuperadoBI);

                }

            }

        }
        //clusteriza as instancias de bi2 que estao sem cluster. Acessa SI ou atualiza SI

        for (Map.Entry<String, ArrayList<Vertice>> entrySet : bi2.getMapaNomes().entrySet()) { //pra cada bloco
            String key = entrySet.getKey();
            int clusterRecuperadoBI;

            ArrayList<Vertice> value = entrySet.getValue();
            for (int i = 0; i < value.size(); i++) {//pra o array de cada bloco da entrada
                // System.err.println(" teste  ++++++++++++++" + controleApagar);

                Vertice get1 = value.get(i);
                if (get1.getClusterId() == -1) {
                    double limiar = 0.9; //limiar
                    Aresta arestaMax = null;
                    Aresta arestaAux = null;
                    double maxSimilaridade = 0;

                    for (int j = 0; j < value.size(); j++) {//descobrir por instancia sem bloco a instancia de maior similaridade
                        if (i != j) {
                            Vertice get2 = value.get(j);

                            //se retornar null é pq n existe a aresta em si.N tem similaridade medida previamente
                            arestaAux = this.si.getAresta(key, get1, get2.getId(), get2.getSourceId());
                            if (arestaAux == null) { //mede similaridade
                                AbstractStringMetric metric = new Levenshtein();
                                float result = metric.getSimilarity(get1.getConteudoComparacao(), get2.getConteudoComparacao());
                                if (result > limiar) {
                                    si.verificaSimilaridade(result, key, get1.getId(), get1.getSourceId(), get2.getId(), get2.getSourceId(), key);
                                    if (result > maxSimilaridade) {
                                        maxSimilaridade = result;
                                        //System.err.print(" Teste agraaa +++++  ");
                                        arestaMax = this.si.getAresta(key, get1, get2.getId(), get2.getSourceId());
                                        //System.err.println(" teste continua  " + arestaMax.getDestino().getId());

                                    }

                                }

                                //  System.err.println(" saida o que entrou" + get2.getId());
                            } else {
                                double similaridadeRecuperada = arestaAux.getSimilaridade();
                                if (similaridadeRecuperada > limiar && similaridadeRecuperada > maxSimilaridade) {
                                    arestaMax = arestaAux;
                                    maxSimilaridade = similaridadeRecuperada;
                                    //tem que recuperar a similaridade no SI

                                }

                                //se ja tem a similaridades
                            }
                            //  si.getAresta(key, get, key, key)
                            // if (si.get) {

                        }

                    }
                    if (arestaMax != null) {//encontrou uma instancia muito similar. REcuperar cluster ou colocar criar novo cluster
                        int clusterRecuperadoBI2 = bi.getclusterId(arestaMax.getDestino().getId(), key, arestaMax.getDestino().getSourceId());
                        if (clusterRecuperadoBI2 == -1) { //a instancia similar n tem cluster. Entao cria cluster
                            get1.setClusterId(controleClusterId);
                            arestaMax.getDestino().setClusterId(controleClusterId);
                            controleClusterId++;
                            // this.preencheSIeBI(limiar, key, key, key, key, key, key);
                            bi.insertVertice(key, get1);
                            bi.insertVertice(key, arestaMax.getDestino());

                        } else {
                            get1.setClusterId(clusterRecuperadoBI2);
                            bi.insertVertice(key, get1);

                        }  // }

                    } //else { //não tem similaridade acima do limiar com nenhuma outra instancia
                    //  get1.setClusterId(controleClusterId);
                    //controleClusterId++;
                    // bi.insertVertice(key, get1);
                    //}

                }

            }

            //clusterRecuperadoBI = this.bi.getclusterId(get.getId(),key, get.getSourceId());
            //  int
            // AbstractStringMetric similaridade = new Levenshtein();
        }//pra cada bloco
        System.out.println("**************Fim*************");

    }

    public void cluseriza6(BlockIndex bi2) {
        //verifica quais do BI2 estão armazenados no BI oficial e copia cluster
        // System.err.println(" 2 " + bi2.getNumeroElementos());
        //System.err.println(" 1 " + bi.getNumeroElementos());
        for (Map.Entry<String, ArrayList<Vertice>> entrySet : bi2.getMapaNomes().entrySet()) {

            String key = entrySet.getKey();
            int clusterRecuperadoBI;
            ArrayList<Vertice> value = entrySet.getValue();
            for (int i = 0; i < value.size(); i++) {
                Vertice get = value.get(i);
                clusterRecuperadoBI = this.bi.getclusterId(get.getId(), key, get.getSourceId());
                if (clusterRecuperadoBI != -1) {
                    get.setClusterId(clusterRecuperadoBI);

                }

            }

        }
        //clusteriza as instancias de bi2 que estao sem cluster. Acessa SI ou atualiza SI

        for (Map.Entry<String, ArrayList<Vertice>> entrySet : bi2.getMapaNomes().entrySet()) { //pra cada bloco
            String key = entrySet.getKey();
            int clusterRecuperadoBI;

            ArrayList<Vertice> value = entrySet.getValue();
            for (int i = 0; i < value.size(); i++) {//pra o array de cada bloco da entrada
                // System.err.println(" teste  ++++++++++++++" + controleApagar);

                Vertice get1 = value.get(i);
                if (get1.getClusterId() == -1) {
                    double limiar = 0.9; //limiar
                    Aresta arestaMax = null;
                    Aresta arestaAux = null;
                    double maxSimilaridade = 0;
                    Vertice verticeOrigem = si.getVerticeOrigem(key, get1.getId(), get1.getSourceId());

                    for (int j = 0; j < value.size(); j++) {//descobrir por instancia sem bloco a instancia de maior similaridade
                        if (i != j) {
                            Vertice get2 = value.get(j);

                            Vertice verticeDestino = si.getVerticeOrigem(key, get2.getId(), get2.getSourceId());
                            arestaAux = this.si.getAresta(key, get1, get2.getId(), get2.getSourceId());
                            if (verticeDestino != null && verticeOrigem != null) {
                                if (arestaAux == null) {
                                    continue;
                                }

                            }
                            //se retornar null é pq n existe a aresta em si.N tem similaridade medida previamente

                            if (arestaAux == null) { //mede similaridade
                                AbstractStringMetric metric = new Levenshtein();
                                float result = metric.getSimilarity(get1.getConteudoComparacao(), get2.getConteudoComparacao());
                                if (result > limiar) {
                                    si.verificaSimilaridade(result, key, get1.getId(), get1.getSourceId(), get2.getId(), get2.getSourceId(), key);
                                    if (result > maxSimilaridade) {
                                        maxSimilaridade = result;
                                        //System.err.print(" Teste agraaa +++++  ");
                                        arestaMax = this.si.getAresta(key, get1, get2.getId(), get2.getSourceId());
                                        //System.err.println(" teste continua  " + arestaMax.getDestino().getId());

                                    }

                                }

                                //  System.err.println(" saida o que entrou" + get2.getId());
                            } else {
                                double similaridadeRecuperada = arestaAux.getSimilaridade();
                                if (similaridadeRecuperada > limiar && similaridadeRecuperada > maxSimilaridade) {
                                    arestaMax = arestaAux;
                                    maxSimilaridade = similaridadeRecuperada;
                                    //tem que recuperar a similaridade no SI

                                }

                                //se ja tem a similaridades
                            }
                            //  si.getAresta(key, get, key, key)
                            // if (si.get) {

                        }

                    }
                    if (arestaMax != null) {//encontrou uma instancia muito similar. REcuperar cluster ou colocar criar novo cluster
                        int clusterRecuperadoBI2 = bi.getclusterId(arestaMax.getDestino().getId(), key, arestaMax.getDestino().getSourceId());
                        if (clusterRecuperadoBI2 == -1) { //a instancia similar n tem cluster. Entao cria cluster
                            get1.setClusterId(controleClusterId);
                            arestaMax.getDestino().setClusterId(controleClusterId);
                            controleClusterId++;
                            // this.preencheSIeBI(limiar, key, key, key, key, key, key);
                            bi.insertVertice(key, get1);
                            bi.insertVertice(key, arestaMax.getDestino());

                        } else {
                            get1.setClusterId(clusterRecuperadoBI2);
                            bi.insertVertice(key, get1);

                        }  // }

                    } //else { //não tem similaridade acima do limiar com nenhuma outra instancia
                    //  get1.setClusterId(controleClusterId);
                    //controleClusterId++;
                    // bi.insertVertice(key, get1);
                    //}

                }

            }

            //clusterRecuperadoBI = this.bi.getclusterId(get.getId(),key, get.getSourceId());
            //  int
            // AbstractStringMetric similaridade = new Levenshtein();
        }//pra cada bloco
        System.out.println("**************Fim*************");

    }

    public void reduz(float porcentagem, int tamanho) {
        int numeroTotalElementos = this.bi.getNumeroElementos();
        //int numeroTotalSimilaridades = this.si.getTamanho();
        float teste = numeroTotalElementos * porcentagem;
        int tamanhoControle = 0;

        for (Map.Entry<String, ArrayList<Vertice>> entrySet : bi.getMapaNomes().entrySet()) {
            String key = entrySet.getKey();
            ArrayList<Vertice> value = entrySet.getValue();

            for (int i = 0; i < value.size(); i++) {
                Vertice get = value.get(i);
                if (tamanho > tamanhoControle) {
                    // si.getVerticeOrigem(key, get.getId(), get.getSourceId());
                    value.remove(get);
                    tamanhoControle++;
                }

            }

        }

    }

    public void reduzBI(float porcentagem, int tamanho) {
        int numeroTotalElementos = this.bi.getNumeroElementos();
        //int numeroTotalSimilaridades = this.si.getTamanho();
        float teste = numeroTotalElementos * porcentagem;
        int tamanhoControle = 0;

        for (Map.Entry<String, ArrayList<Vertice>> entrySet : bi.getMapaNomes().entrySet()) {
            String key = entrySet.getKey();
            ArrayList<Vertice> value = entrySet.getValue();

            for (int i = 0; i < value.size(); i++) {
                Vertice get = value.get(i);
                if (tamanho > tamanhoControle) {
                    // si.getVerticeOrigem(key, get.getId(), get.getSourceId());
                    value.remove(get);
                    tamanhoControle++;
                }

            }

        }

    }

    public void reduzSI(float porcentagem, int tamanho) {
        int numeroTotalElementos = this.si.getTamanho();
        //int numeroTotalSimilaridades = this.si.getTamanho();
        float teste = numeroTotalElementos * porcentagem;
        int tamanhoControle = 0;

        for (Map.Entry<String, Map<Vertice, ArrayList<Aresta>>> entrySet : si.getMapaArestas().entrySet()) {
            Map<Vertice, ArrayList<Aresta>> aux = new HashMap<Vertice, ArrayList<Aresta>>();
            // aux = this.getMapaArestas().get(key);
            aux = entrySet.getValue();

            for (Map.Entry<Vertice, ArrayList<Aresta>> entrySet1 : aux.entrySet()) {
                ArrayList<Aresta> aux2 = entrySet1.getValue();
                for (int i = 0; i < aux2.size(); i++) {
                    Aresta a = aux2.get(i);
                    if (tamanho > tamanhoControle) {
                        aux2.remove(a);
                        tamanhoControle++;
                    }
                }

            }

        }
        System.err.println(" tamanho metodo " + tamanhoControle);

    }

//Clusteriza sem consultar indices
    public void cluserizaTradicional(BlockIndex bi2) {
        for (Map.Entry<String, ArrayList<Vertice>> entrySet : bi2.getMapaNomes().entrySet()) { //pra cada bloco
            String key = entrySet.getKey();
            // int clusterRecuperadoBI;

            ArrayList<Vertice> value = entrySet.getValue();
            for (int i = 0; i < value.size(); i++) {//pra o array de cada bloco da entrada
                // System.err.println(" teste  ++++++++++++++" + controleApagar);

                Vertice get1 = value.get(i);

                double limiar = 0.9; //limiar
                Vertice verticeMax = null;

                double maxSimilaridade = 0;
                for (int j = 0; j < value.size(); j++) {//descobrir por instancia sem bloco a instancia de maior similaridade
                    if (i != j) {
                        Vertice get2 = value.get(j);
                        //se retornar null é pq n existe a aresta em si.N tem similaridade medida previamente
                        // arestaAux = this.si.getAresta(key, get1, get2.getId(), get2.getSourceId());
                        //mede similaridade
                        AbstractStringMetric metric = new Levenshtein();
                        //float result = metric.getSimilarity(get1.getConteudoComparacao(), get2.getConteudoComparacao());
                        float result1 = metric.getSimilarity(get1.getConteudoComparacao(), get2.getConteudoComparacao());
                        float result2 = metric.getSimilarity(get1.getConteudoComparacao2(), get2.getConteudoComparacao2());
                        float result = (float) (result1 + result2) / 2;
                        //si.verificaSimilaridade(result, key, get1.getId(), get1.getSourceId(), get2.getId(), get2.getSourceId(), key);

                        if (result > limiar && result > maxSimilaridade) {
                            maxSimilaridade = result;
                            verticeMax = get2;
                            //arestaMax = this.si.getAresta(key, get1, get2.getId(), get2.getSourceId());

                        }

                        //se ja tem a similaridades
                        //  si.getAresta(key, get, key, key)
                        // if (si.get) {
                    }

                }

                if (verticeMax != null) {
                    if (verticeMax.getClusterId() == -1) {
                        verticeMax.setClusterId(controleClusterId);
                        get1.setClusterId(controleClusterId);
                        controleClusterId++;

                    } else {
                        get1.setClusterId(verticeMax.getClusterId());
                    }

                } else {
                    get1.setClusterId(controleClusterId);
                    controleClusterId++;
                }

            }

            //clusterRecuperadoBI = this.bi.getclusterId(get.getId(),key, get.getSourceId());
            //  int
            // AbstractStringMetric similaridade = new Levenshtein();
        }//pra cada bloco
        System.out.println("**************Fim*************");

    }

}
    
