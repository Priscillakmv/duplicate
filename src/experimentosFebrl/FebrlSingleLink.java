/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package experimentosFebrl;

import Index.BlockIndex;
import Index.SimilarityIndex;
import classificacao.SingleLink;
import java.io.IOException;
import query.QueryExperimento;

/**
 *
 * @author Priscilla
 */
public class FebrlSingleLink {
    public static void main(String[] args) throws IOException {
        //Tradicional. clusteriza toda a entrada

        System.out.println(" Resutado em lote");

        QueryExperimento queryTradicional1 = new QueryExperimento();
        //o parametro de tamanho deve variar para alterar a porcentagem de dados excluídos.
        //BlockIndex bi2 = queryTradicional1.blocaConsultaReduzidaFixa(queryTradicional1.carregaDadosCD(), 200);
        BlockIndex bi2 = queryTradicional1.blocaConsultaFebrl(queryTradicional1.carregaDadosFebrl());
      
        BlockIndex bi1 = new BlockIndex();
        SimilarityIndex si1 = new SimilarityIndex();
        SingleLink cluster = new SingleLink(si1, bi1);
        //System.out.println(" BI tamanho " + bi2.getNumeroElementos());
        //ClusterizacaoIncrementalOtimizada cluster = new ClusterizacaoIncrementalOtimizada(bi1, si1); //vazios
        // long tempoInicial = System.currentTimeMillis();

        // cluster.cluserizaAcessandoBIeSI(bi2); //passo só parte dos dados
        long tempoInicial = System.currentTimeMillis();
        cluster.singleLinkFebrl(bi2);
        System.out.println(" Tempo para processar a abordagem proposta com o pior caso  " + (System.currentTimeMillis() - tempoInicial));

        //Inicializa com parte dos dados
       /** System.out.println(" Segunda parte do experimento  ");
        QueryExperimento queryTradicional2 = new QueryExperimento();
        BlockIndex bi3 = queryTradicional2.blocaConsultaReduzidaFixa(queryTradicional2.carregaDadosCD(), 9000);
        BlockIndex bi4 = new BlockIndex();
        SimilarityIndex si4 = new SimilarityIndex();
       // ClusterizacaoIncrementalOtimizada cluster2 = new ClusterizacaoIncrementalOtimizada(bi4, si4);
        // SingleLink cluster2 = new SingleLink(si4, bi4);
        cluster.setBi(bi4);
        cluster.setSi(si4);

        cluster.singleLink(bi3);

        //Pega tudo considerando parte dos dados armazenados
        //  System.out.println(" Tempo para processar a abordagem proposta com o pior caso  " + (System.currentTimeMillis() - tempoInicial2));
        QueryExperimento queryTradicional3 = new QueryExperimento();
        //o parametro de tamanho deve variar para alterar a porcentagem de dados excluídos.
        //BlockIndex bi2 = queryTradicional1.blocaConsultaReduzidaFixa(queryTradicional1.carregaDadosCD(), 200);
        BlockIndex bi5 = queryTradicional3.blocaConsultaCD(queryTradicional1.carregaDadosCD());
        long tempoInicial2 = System.currentTimeMillis();
        cluster.singleLink(bi5);
        System.out.println(" Tempo para processar a abordagem proposta com o pior caso  " + (System.currentTimeMillis() - tempoInicial2));**/
        
    }

    
}
