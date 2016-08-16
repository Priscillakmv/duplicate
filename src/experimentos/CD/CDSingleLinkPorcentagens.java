/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package experimentos.CD;

import Index.BlockIndex;
import Index.SimilarityIndex;
import avaliacao.Calculadora;
import classificacao.SingleLink;
import java.io.IOException;
import query.Amostra;
import util.Arquivo;

/**
 *
 * @author Priscilla
 */
public class CDSingleLinkPorcentagens {

    public static void main(String[] args) throws IOException {

       
        int numeroIteracoes = 5;

        int tamanhoAmostras[] = {1000, 2000, 3000, 4000, 5000, 6000, 7000, 8000, 9000};     
        double tempos[] = new double[9];  
        double valoresEsperados[] = new double[9];  
        double valoresAcertados[] = new double[9];
        double valoreErrados[] = new double[9]; 


        //double valoresEsperadosAux[] = new double[numeroIteracoes];
        double temposAux[] = new double[9];
        double valoresAcertadosAux[] = new double[9];
        double valoreErradosAux[] = new double[9];
       
        for (int j = 0; j < tamanhoAmostras.length; j++) {

            int tamanho = tamanhoAmostras[j];
            double tempo = 0;
            double tempoAux = 0;
            for (int i = 0; i < numeroIteracoes; i++) {
                 BlockIndex bi = new BlockIndex();
                 SimilarityIndex si = new SimilarityIndex();
                 SingleLink cluster = new SingleLink(si, bi);

               
                BlockIndex bi2 = new BlockIndex();
                Amostra amostra = new Amostra();
                amostra.geraPosicao(tamanho, 9760);
                bi2 = amostra.blocaDadosDaAmostraConsulta(amostra.carregaDadosCD());
              
                
                //Para calcular com reuso
               
                cluster.singleLink(bi2);//armazena amostra
                
                BlockIndex bi3 = new BlockIndex();
                Amostra amostra3 = new Amostra();
                amostra3.geraPosicao(9759, 9760);
                bi3 = amostra3.blocaDadosDaAmostraConsulta(amostra3.carregaDadosCD());
                
                long tempoInicial = System.currentTimeMillis();
                cluster.singleLink(bi3);//passa a base toda considerando uma porcentagem armazenada.
                
                tempo = tempo+ (System.currentTimeMillis() - tempoInicial);
                
 //Para Calcular Sem reuso
                
             
                
                BlockIndex bi4 = new BlockIndex();
                SimilarityIndex si4 = new SimilarityIndex();                  
                SingleLink cluster2 = new SingleLink(si4, bi4);
                Amostra amostra4 = new Amostra();
                amostra4.geraPosicao(9759, 9760);
                bi4 = amostra4.blocaDadosDaAmostraConsulta(amostra4.carregaDadosCD());
                

                
                long tempoInicialAux = System.currentTimeMillis();
                cluster2.singleLink(bi4);//passa a base toda considerando uma porcentagem armazenada.
                
                tempoAux = tempoAux+ (System.currentTimeMillis() - tempoInicialAux);
            }
          temposAux[j]=tempoAux/numeroIteracoes; 
          tempos[j]=tempo/numeroIteracoes;
            
            
            
           

        }
         System.out.println(" Tempos Tradicional " );
         System.out.println("");
        for (int i = 0; i < temposAux.length; i++) {
            System.out.print(" " + temposAux[i] );
           
            
        }
        System.out.println("");
        
        System.out.println(" Tempos Com Reuso " );
        for (int i = 0; i < tempos.length; i++) {
            System.out.print(" "+tempos[i] );
           
            
        }

        
    }

}
