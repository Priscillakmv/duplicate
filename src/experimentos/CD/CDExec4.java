/*
 * DuDe - The Duplicate Detection Toolkit
 * 
 * Copyright (C) 2010  Hasso-Plattner-Institut fÃ¼r Softwaresystemtechnik GmbH,
 *                     Potsdam, Germany 
 *
 * 
 */
package experimentos.CD;

import classificacao.ClusterizacaoIncrementalIngenua;
import Index.BlockIndex;
import Index.SimilarityIndex;
import java.io.IOException;
import query.QueryExperimento;

/**
 * Pior Caso não encontra nada no BI e SI. Insere tudo e calcula tudo.
 * Melhor Caso considera que tudo já está armazenado. Apenas acessa e calcula os clusters
 *
 * @author Priscilla Vieira
 */
public class CDExec4 {

      public static void main(String[] args) throws IOException {
        long tempoInicial = System.currentTimeMillis();
        //Pior Caso
        QueryExperimento queryTradicional = new QueryExperimento();
        BlockIndex bi2 = queryTradicional.blocaConsultaCD(queryTradicional.carregaDadosCD());
        BlockIndex bi = new BlockIndex();
        SimilarityIndex si = new SimilarityIndex();
        ClusterizacaoIncrementalIngenua cluster = new ClusterizacaoIncrementalIngenua(bi, si);
       // cluster.cluserizaAcessandoBIeSI(bi2);
        cluster.cluserizaAcessandoBIeSI(bi2);
        System.out.println(" Tempo para processar a abordagem proposta com o pior caso  " + (System.currentTimeMillis() - tempoInicial));
     

        //Melhor Caso
        long tempoInicial2 = System.currentTimeMillis();
        QueryExperimento queryTradicional2 = new QueryExperimento();
        BlockIndex bi3 = queryTradicional2.blocaConsultaCD(queryTradicional2.carregaDadosCD());
        System.err.println(" cora ver " + bi.getNumeroElementos());
        System.err.println(" cora ver " + si.getTamanho());
      //  cluster.cluserizaAcessandoBIeSI(bi3);
        cluster.cluserizaAcessandoBIeSI(bi3);
        System.out.println(" Tempo para processar a abordagem proposta com o melhor caso  " + (System.currentTimeMillis() - tempoInicial2));
     
    }

}
       
