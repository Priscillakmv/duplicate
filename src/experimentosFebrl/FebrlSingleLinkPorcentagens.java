/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package experimentosFebrl;

import Index.BlockIndex;
import Index.SimilarityIndex;
import avaliacao.Calculadora;
import classificacao.SingleLink;
import java.io.IOException;
import query.Amostra;
import query.QueryExperimento;

/**
 *
 * @author Priscilla
 */
public class FebrlSingleLinkPorcentagens {
       public static void main(String[] args) throws IOException {
    
        int numeroIteracoes = 5;

        int tamanhoAmostras[] = {1000, 5000, 10000, 20000, 30000};     
        double tempos[] = new double[5];  
       // double valoresEsperados[] = new double[9];  
        double precisao[] = new double[5];
        double recall[] = new double[5]; 
        double fmeasure[] = new double[5];


        //double valoresEsperadosAux[] = new double[numeroIteracoes];
        double temposAux[] = new double[5];
        double precisaoAux[] = new double[5];
        double recallAux[] = new double[5]; 
        double fmeasureAux[] = new double[5];
        
               
        for (int j = 0; j < tamanhoAmostras.length; j++) {

            int tamanho = tamanhoAmostras[j];
            double tempo = 0;
            double tempoAux = 0;
            double valoresEsperadosCumulativos[]= new double[5];
            double valoresAcertadosCumilativos[] = new double[5];
            double valoreErradosCumulativos[] = new double[5]; 
            double valoresAcertadosAuxCumulativos[] = new double[5];
            double valoreErradosAuxCumulativos[] = new double[5];            
            
            for (int i = 0; i < numeroIteracoes; i++) {
                 BlockIndex bi = new BlockIndex();
                 SimilarityIndex si = new SimilarityIndex();
                 SingleLink cluster = new SingleLink(si, bi);

               
                BlockIndex bi2 = new BlockIndex();
                Amostra amostra = new Amostra();
                amostra.geraPosicao(tamanho, 130002);
                
                bi2 = amostra.blocaDadosDaAmostraConsultaFebrl(amostra.carregaDadosFebrl());
              
                
                //Para calcular com reuso
               
                cluster.singleLinkFebrl6(bi2);//armazena amostra
                
                BlockIndex bi3 = new BlockIndex();
                QueryExperimento query = new QueryExperimento();
               bi3 = query.blocaConsultaFebrl(query.carregaDadosFebrl());
               // Amostra amostra3 = new Amostra();
               // amostra3.geraPosicao(130001, 130002);
                valoresEsperadosCumulativos[i]= 30000;
               // bi3 = amostra3.blocaDadosDaAmostraConsultaFebrl(amostra3.carregaDadosFebrl());
                
                long tempoInicial = System.currentTimeMillis();
                cluster.singleLinkFebrl6(bi3);//passa a base toda considerando uma porcentagem armazenada.
                
                tempo = tempo+ (System.currentTimeMillis() - tempoInicial);
                
                valoresAcertadosCumilativos[i] = ((double) cluster.getTotalCerto());
                valoreErradosCumulativos[i] = ((double) cluster.getTotalErrado());
                
 //Para Calcular Sem reuso
                
             
                
               /** BlockIndex bi4 = new BlockIndex();
                SimilarityIndex si4 = new SimilarityIndex();                  
                SingleLink cluster2 = new SingleLink(si4, bi4);
                Amostra amostra4 = new Amostra();
                amostra4.geraPosicao(130001, 130002);
                bi4 = amostra4.blocaDadosDaAmostraConsultaFebrl(amostra4.carregaDadosFebrl());
                

                
                long tempoInicialAux = System.currentTimeMillis();
                cluster2.singleLinkFebrl6(bi4);//passa a base toda considerando uma porcentagem armazenada.
                
                tempoAux = tempoAux+ (System.currentTimeMillis() - tempoInicialAux);
                valoresAcertadosAuxCumulativos[i] = ((double) cluster2.getTotalCerto());
                valoreErradosAuxCumulativos[i] = ((double) cluster2.getTotalErrado());**/
            }
          temposAux[j]=tempoAux/numeroIteracoes; 
          tempos[j]=tempo/numeroIteracoes;
          Calculadora calculadora = new Calculadora();
          
          precisaoAux[j]= calculadora.calculaPrecisao(valoresAcertadosCumilativos, valoreErradosAuxCumulativos);
          recallAux[j] =  calculadora.calculaRecall(valoresAcertadosAuxCumulativos, valoresEsperadosCumulativos);  
          fmeasureAux[j]= calculadora.calculaFMeasure(valoresAcertadosAuxCumulativos, valoresEsperadosCumulativos, valoreErradosAuxCumulativos);
          
          precisao[j]= calculadora.calculaPrecisao(valoresAcertadosCumilativos, valoreErradosCumulativos);
          recall[j] = calculadora.calculaRecall(valoresAcertadosCumilativos, valoresEsperadosCumulativos); 
          fmeasure[j]=calculadora.calculaFMeasure(valoresAcertadosCumilativos, valoresEsperadosCumulativos, valoreErradosCumulativos);
          
          
          
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
        System.out.println("");
        
        
     
      
        
        
        
         System.out.println("Pior Caso ");
                 BlockIndex bi5 = new BlockIndex();
                 SimilarityIndex si5 = new SimilarityIndex();
                 SingleLink cluster = new SingleLink(si5, bi5);

               
                BlockIndex bi6 = new BlockIndex();
                Amostra amostra5 = new Amostra();
                amostra5.geraPosicao(9759, 9760);
                
                bi6 = amostra5.blocaDadosDaAmostraConsulta(amostra5.carregaDadosCD());
              
                
                //Para calcular com reuso
               long tempoInicialWorst = System.currentTimeMillis();
                cluster.singleLink(bi6);//armazena amostra
                 double tempo2 = System.currentTimeMillis() - tempoInicialWorst;
                 
                System.out.println(" Tempo pior caso: " + tempo2);
                
                
}
        
                
        
  
    
}
