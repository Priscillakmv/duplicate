/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package experimentos.CD;

import Index.BlockIndex;
import Index.SimilarityIndex;
import classificacao.AverageLink;
import java.io.IOException;
import query.Amostra;

/**
 *
 * @author Priscilla
 */
public class CDAverageLink {
     public static void main(String[] args) throws IOException {

       
    
         
                 BlockIndex bi = new BlockIndex();
                 SimilarityIndex si = new SimilarityIndex();
                // SingleLink cluster = new SingleLink(si, bi);
                 AverageLink cluster = new AverageLink();
                 

               
                BlockIndex bi2 = new BlockIndex();
                Amostra amostra = new Amostra();
                amostra.geraPosicao(9759, 9760);
                bi2 = amostra.blocaDadosDaAmostraConsulta(amostra.carregaDadosCD());
                cluster.averageLinkIncremental(bi2);
               System.out.println(" TEste   "+cluster.getBi().getNumeroElementos());
                
                //Para calcular com reuso
               
               
 //Para Calcular Sem reuso
                
             
            
            
           

        
    }

}
