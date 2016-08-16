/*
 * Teste para o incremental otimizado
 */
package experimentos.CD;

import Index.BlockIndex;
import Index.SimilarityIndex;
import classificacao.ClusterizacaoIncrementalIngenua;
import classificacao.ClusterizacaoIncrementalOtimizada;
import classificacao.ClusterizacaoIngenuaLote;
import graph.Vertice;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;
import query.QueryExperimento;

/**
 *
 * @author Priscilla
 */
public class CDHillClimbing {
    
    public static void main(String[] args) throws IOException {
        //Tradicional. clusteriza toda a entrada
        
   System.out.println(" Resultado em lote");
       
   QueryExperimento queryTradicional7 = new QueryExperimento();
       BlockIndex bi7 = queryTradicional7.blocaConsultaCD(queryTradicional7.carregaDadosCD());
       BlockIndex bi8 = new BlockIndex();
       SimilarityIndex si7 = new SimilarityIndex();
       ClusterizacaoIncrementalOtimizada cluster7 = new ClusterizacaoIncrementalOtimizada(bi8, si7); //vazios
        long tempoInicial7 = System.currentTimeMillis();

       cluster7.cluserizaOtimizado3(bi7);
       System.out.println(" Tempo para processar Pior Caso  " + (System.currentTimeMillis() - tempoInicial7));

   
        QueryExperimento queryTradicional1 = new QueryExperimento();
        //o parametro de tamanho deve variar para alterar a porcentagem de dados excluídos.
       //BlockIndex bi2 = queryTradicional1.blocaConsultaReduzidaFixa(queryTradicional1.carregaDadosCD(), 200);
        BlockIndex bi2 = queryTradicional1.blocaConsultaCD(queryTradicional1.carregaDadosCD());
        
       
        BlockIndex bi1 = new BlockIndex();
        SimilarityIndex si1 = new SimilarityIndex(); 
        ClusterizacaoIncrementalOtimizada cluster = new ClusterizacaoIncrementalOtimizada(bi1, si1); //vazios
  

       // cluster.cluserizaAcessandoBIeSI(bi2); //passo só parte dos dados
         long tempoInicial = System.currentTimeMillis();
         cluster.cluserizaOtimizado4(bi2);
         System.out.println(" Tempo para processar a abordagem tradicional  " + (System.currentTimeMillis() - tempoInicial));
         
         System.out.println(" Melhor Caso");
         
       QueryExperimento queryTradicional5 = new QueryExperimento();
       BlockIndex bi5 = queryTradicional5.blocaConsultaCD(queryTradicional5.carregaDadosCD());
       long tempoInicial5 = System.currentTimeMillis();
       cluster.cluserizaOtimizado3(bi5);
       System.out.println(" Tempo para processar Melhor Caso  " + (System.currentTimeMillis() - tempoInicial5));
       
       

        
        
        System.out.println(" Resultado em lote com parte dos dados");      
     
        //Inicializa com parte dos dados
      QueryExperimento queryTradicional2 = new QueryExperimento();
      BlockIndex bi3 = queryTradicional2.blocaConsultaReduzidaFixa(queryTradicional2.carregaDadosCD(), 9000);
      BlockIndex bi4 = new BlockIndex();
      SimilarityIndex si4 = new SimilarityIndex();
      ClusterizacaoIncrementalOtimizada cluster2 = new ClusterizacaoIncrementalOtimizada(bi4, si4);
     
      cluster2.cluserizaOtimizado4(bi3);    
      
      //Carrega todos os dados, considerando que parte dos dados possuem informações prévias    
      QueryExperimento queryTradicional3 = new QueryExperimento();
     BlockIndex bi6 = queryTradicional3.blocaConsultaCD(queryTradicional3.carregaDadosCD());
     // ClusterizacaoIncrementalOtimizada cluster3 = new ClusterizacaoIncrementalOtimizada(cluster2.getBi(), cluster2.getSi());
     //cluster2.cluserizaOtimizado3(bi5);
      long tempoInicial2 = System.currentTimeMillis();
     cluster2.cluserizaOtimizado3(bi6);         
     System.out.println(" Tempo para processar a abordagem proposta com o caso medio  " + (System.currentTimeMillis()- tempoInicial2));
     
      
    }
    

}
