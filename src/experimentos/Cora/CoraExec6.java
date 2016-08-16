/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package experimentos.Cora;

import Index.BlockIndex;
import Index.SimilarityIndex;
import classificacao.ClusterizacaoIncrementalOtimizada;
import java.io.IOException;
import query.QueryExperimento;

/**
 *
 * @author Priscilla
 */
public class CoraExec6 {
     public static void main(String[] args) throws IOException {
        //Tradicional. clusteriza toda a entrada
        
        System.out.println(" Resutado em lote");
        QueryExperimento queryTradicional1 = new QueryExperimento();
        //o parametro de tamanho deve variar para alterar a porcentagem de dados excluídos.
       //BlockIndex bi2 = queryTradicional1.blocaConsultaReduzidaFixa(queryTradicional1.carregaDadosCD(), 200);
        BlockIndex bi2 = queryTradicional1.blocaConsultaCora(queryTradicional1.carregaDadosCora());
        
       
        BlockIndex bi1 = new BlockIndex();
        SimilarityIndex si1 = new SimilarityIndex(); 
        ClusterizacaoIncrementalOtimizada cluster = new ClusterizacaoIncrementalOtimizada(bi1, si1); //vazios
       // long tempoInicial = System.currentTimeMillis();

       // cluster.cluserizaAcessandoBIeSI(bi2); //passo só parte dos dados
         cluster.cluserizaOtimizado4Cora(bi2);
       //System.out.println(" Tamanho " + bi2.getNumeroElementos());
      //  System.out.println(" Tamanho " + bi2.getNumeroBlocos());
       // System.out.println(" SI " + si.getTamanho());
        //System.out.println(" Tempo para processar a abordagem proposta com o pior caso  " + (System.currentTimeMillis() - tempoInicial));

        //melhor tempo a seguir
       // long tempoInicial2 = System.currentTimeMillis();
        
      /**  
        System.out.println(" Resutado em lote com parte dos dados");
        //Inicializa com parte dos dados
      QueryExperimento queryTradicional2 = new QueryExperimento();
      BlockIndex bi3 = queryTradicional2.blocaConsultaReduzida(queryTradicional2.carregaDadosCD(), 5000);
      BlockIndex bi4 = new BlockIndex();
      SimilarityIndex si4 = new SimilarityIndex();
      ClusterizacaoIncrementalOtimizada cluster2 = new ClusterizacaoIncrementalOtimizada(bi4, si4);
      cluster2.cluserizaOtimizado2(bi3);
     
      
      //Carrega todos os dados, considerando que parte dos dados possuem informações prévias
      
        System.out.println(" Resutado incremental");
      
      QueryExperimento queryTradicional3 = new QueryExperimento();
     BlockIndex bi5 = queryTradicional3.blocaConsultaCD(queryTradicional3.carregaDadosCD());
     cluster2.cluserizaOtimizado3(bi5);
      
      

       // long tempoExcluir = System.currentTimeMillis() - tempoInicial2;
        //long tempoExcluir2 = System.currentTimeMillis();
        //cluster.cluserizaAcessandoBIeSI(bi3);
       //  cluster.cluserizaOtimizado2(bi3);
        //long tempoFinal = (System.currentTimeMillis() - tempoExcluir2) + tempoExcluir;
         //bi3.printBlockIndex();
        //System.err.println("Tempo para processar a abordagem proposta com o caso medio: " + (System.currentTimeMillis()-tempoInicial2));
       // System.err.println("bgege"  + bi3.getNumeroElementos());
       //System.out.println(" Tempo para processar a abordagem proposta com o caso medio  " + tempoFinal);
    }**/
    
}
}
