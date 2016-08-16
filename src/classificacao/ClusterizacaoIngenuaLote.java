/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package classificacao;

import Index.BlockIndex;
import avaliacao.Precisao;
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
public class ClusterizacaoIngenuaLote {
    BlockIndex bi;
    int controleClusterId = 0;
    public ClusterizacaoIngenuaLote(BlockIndex bi){
        this.bi = bi;

        controleClusterId = 0;
        
    }
    public void cluserizaTradicional_NaoAcessaBIeSI(BlockIndex bi2) throws IOException {
        Precisao precisao = new Precisao();
        precisao.InicializaGoldStandardCD();
        //System.out.println(" Tamanho  " + bi2.getNumeroElementos());
        for (Map.Entry<String, ArrayList<Vertice>> entrySet : bi2.getMapaNomes().entrySet()) { //pra cada bloco
            String key = entrySet.getKey();
            ArrayList<Vertice> value = entrySet.getValue();
            for (int i = 0; i < value.size(); i++) {//pra o array de cada bloco da entrada
                Vertice get1 = value.get(i);
                double limiar = 0.9; //limiar
                Vertice verticeMax = null;
                double maxSimilaridade = 0;
                for (int j = i + 1; j < value.size(); j++) {//descobrir por instancia sem bloco a instancia de maior similaridade
                    if (i != j) {
                        Vertice get2 = value.get(j);
                        AbstractStringMetric metric = new Levenshtein();
                        float result = metric.getSimilarity(get1.getConteudoComparacao(), get2.getConteudoComparacao());
                        if (result > limiar && result > maxSimilaridade) {
                            maxSimilaridade = result;
                            verticeMax = get2;
                        }

                    }
                }

                if (verticeMax != null) {
                    if (verticeMax.getClusterId() == -1) {
                        verticeMax.setClusterId(controleClusterId);
                        get1.setClusterId(controleClusterId);
                        controleClusterId++;
                        //Calcular precisao
                        precisao.inserePairDuplicado("cddb", get1.getId(), "cddb", verticeMax.getId());

                    } else {
                        get1.setClusterId(verticeMax.getClusterId());
                        precisao.inserePairDuplicado("cddb", get1.getId(), "cddb", verticeMax.getId());

                    }

                } else {
                    get1.setClusterId(controleClusterId);
                    controleClusterId++;
                }
            }
        }//pra cada bloco
        System.out.println(" Precisao Clusters  " + precisao.getPrecisao());
        System.out.println(" Cobertura Clusters  " + precisao.getCobertura());
    }

    /**
     * Clusteriza de forma ingenua por Bloco. Mas so junta astuplas com maior
     * similaridade
     *
     * @param bi2
     *
     */
    public void cluserizaTradicional_NaoAcessaBIeSI2(BlockIndex bi2) throws IOException {
        Precisao precisao = new Precisao();
        precisao.InicializaGoldStandardCD();
        //System.out.println(" Tamanho  " + bi2.getNumeroElementos());
        for (Map.Entry<String, ArrayList<Vertice>> entrySet : bi2.getMapaNomes().entrySet()) { //pra cada bloco
            String key = entrySet.getKey();
            ArrayList<Vertice> value = entrySet.getValue();
            for (int i = 0; i < value.size(); i++) {//pra o array de cada bloco da entrada
                Vertice get1 = value.get(i);
                double limiar = 0.9; //limiar
                Vertice verticeMax = null;

                for (int j = i+1; j < value.size(); j++) {//descobrir por instancia sem bloco a instancia de maior similaridade
                    if (i != j) {
                        Vertice get2 = value.get(j);
                        AbstractStringMetric metric = new Levenshtein();
                        float result = metric.getSimilarity(get1.getConteudoComparacao(), get2.getConteudoComparacao());
                        if (result > limiar) {
                            verticeMax = get2;
                            if (verticeMax.getClusterId() == -1) {
                                verticeMax.setClusterId(controleClusterId);
                                get1.setClusterId(controleClusterId);
                                controleClusterId++;
                                //Calcular precisao
                                precisao.inserePairDuplicado("cddb", get1.getId(), "cddb", verticeMax.getId());

                            } else {
                                get1.setClusterId(verticeMax.getClusterId());
                                precisao.inserePairDuplicado("cddb", get1.getId(), "cddb", verticeMax.getId());

                            }
                        }

                    }
                }

                 if (verticeMax == null) {
             
                get1.setClusterId(controleClusterId);
                controleClusterId++;
                }
            }
        }//pra cada bloco
        System.out.println(" Precisao Clusters  " + precisao.getPrecisao());
        System.out.println(" Cobertura Clusters  " + precisao.getCobertura());
    }
}
    

