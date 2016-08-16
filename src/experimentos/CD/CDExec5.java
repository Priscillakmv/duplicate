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
import classificacao.ClusterizacaoIngenuaLote;
import java.io.IOException;
import query.QueryExperimento;

/**
 * Caso Médio
 *
 * @author Priscilla Vieira
 */
public class CDExec5 {

    public static void main(String[] args) throws IOException {
        
        
        
        long tempoInicial = System.currentTimeMillis();
        QueryExperimento queryTradicional4 = new QueryExperimento();
        BlockIndex bi4 = queryTradicional4.blocaConsultaCD(queryTradicional4.carregaDadosCD());
        ClusterizacaoIngenuaLote cluster4 = new ClusterizacaoIngenuaLote(bi4);
        cluster4.cluserizaTradicional_NaoAcessaBIeSI2(bi4);
        System.err.println(" tetsdfsdf " + bi4.getNumeroElementos());
        System.out.println(" Tempo para processar a abordagem tradicional com o algoritmo ingenuo " + (System.currentTimeMillis() - tempoInicial));

        
        QueryExperimento queryTradicional = new QueryExperimento();
        //o parametro de tamanho deve variar para alterar a porcentagem de dados excluídos.
        BlockIndex bi2 = queryTradicional.blocaConsultaReduzida(queryTradicional.carregaDadosCD(), 5000);
        BlockIndex bi = new BlockIndex();
        SimilarityIndex si = new SimilarityIndex();
        
        

        ClusterizacaoIncrementalIngenua cluster = new ClusterizacaoIncrementalIngenua(bi, si); //vazios
       // long tempoInicial = System.currentTimeMillis();

       // cluster.cluserizaAcessandoBIeSI(bi2); //passo só parte dos dados
         cluster.cluseriza4(bi2);
       // System.err.println(" Tamanho " + bi.getNumeroElementos());
        //System.out.println(" Tempo para processar a abordagem proposta com o pior caso  " + (System.currentTimeMillis() - tempoInicial));

        //melhor tempo a seguir
        long tempoInicial2 = System.currentTimeMillis();
        QueryExperimento queryTradicional2 = new QueryExperimento();
        BlockIndex bi3 = queryTradicional2.blocaConsultaCD(queryTradicional2.carregaDadosCD());

       // long tempoExcluir = System.currentTimeMillis() - tempoInicial2;
        //long tempoExcluir2 = System.currentTimeMillis();
        //cluster.cluserizaAcessandoBIeSI(bi3);
         cluster.cluseriza4(bi3);
        //long tempoFinal = (System.currentTimeMillis() - tempoExcluir2) + tempoExcluir;
         //bi3.printBlockIndex();
        System.err.println("Tempo para processar a abordagem proposta com o caso medio: " + (System.currentTimeMillis()-tempoInicial2));
        System.err.println("bgege"  + si.getTamanho());
       //System.out.println(" Tempo para processar a abordagem proposta com o caso medio  " + tempoFinal);
    }

}
