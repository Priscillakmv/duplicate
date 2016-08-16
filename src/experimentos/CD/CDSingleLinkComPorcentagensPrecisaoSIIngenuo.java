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
public class CDSingleLinkComPorcentagensPrecisaoSIIngenuo {
    
    
    /*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Priscilla
 */


    public static void main(String[] args) throws IOException {
        
        int numeroIteracoes = 5;

        int tamanhoAmostras[] = {1000, 2000, 3000, 4000, 5000, 6000, 7000, 8000, 9000};     
        double tempos[] = new double[9];  
       // double valoresEsperados[] = new double[9];  
        double precisao[] = new double[9];
        double recall[] = new double[9]; 
        double fmeasure[] = new double[9];


        //double valoresEsperadosAux[] = new double[numeroIteracoes];
        double temposAux[] = new double[9];
        double precisaoAux[] = new double[9];
        double recallAux[] = new double[9]; 
        double fmeasureAux[] = new double[9];
        
               
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
                amostra.geraPosicao(tamanho, 9760);
                
                bi2 = amostra.blocaDadosDaAmostraConsulta(amostra.carregaDadosCD());
              
                
                //Para calcular com reuso
               
                cluster.singleLinkIngenuo(bi2);//armazena amostra
                
                BlockIndex bi3 = new BlockIndex();
                Amostra amostra3 = new Amostra();
                amostra3.geraPosicao(9759, 9760);
                valoresEsperadosCumulativos[i]= 298;
                bi3 = amostra3.blocaDadosDaAmostraConsulta(amostra3.carregaDadosCD());
                
                long tempoInicial = System.currentTimeMillis();
                cluster.singleLinkIngenuo(bi3);//passa a base toda considerando uma porcentagem armazenada.
                
                tempo = tempo+ (System.currentTimeMillis() - tempoInicial);
                
                valoresAcertadosCumilativos[i] = ((double) cluster.getTotalCerto());
                valoreErradosCumulativos[i] = ((double) cluster.getTotalErrado());
                
 //Para Calcular Sem reuso
                
             
                
                BlockIndex bi4 = new BlockIndex();
                SimilarityIndex si4 = new SimilarityIndex();                  
                SingleLink cluster2 = new SingleLink(si4, bi4);
                Amostra amostra4 = new Amostra();
                amostra4.geraPosicao(9759, 9760);
                bi4 = amostra4.blocaDadosDaAmostraConsulta(amostra4.carregaDadosCD());
                

                
                long tempoInicialAux = System.currentTimeMillis();
                cluster2.singleLinkIngenuo2(bi4);//passa a base toda considerando uma porcentagem armazenada.
                
                tempoAux = tempoAux+ (System.currentTimeMillis() - tempoInicialAux);
                valoresAcertadosAuxCumulativos[i] = ((double) cluster2.getTotalCerto());
                valoreErradosAuxCumulativos[i] = ((double) cluster2.getTotalErrado());
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
        
        
     
          System.out.println("");
           System.out.println(" Fmeasure Tradicional " );
        for (int i = 0; i < fmeasureAux.length; i++) {
            System.out.print(" "+fmeasureAux[i] );
           
            
        }
         System.out.println("");

          System.out.println("fmeasure Incremental " );
        for (int i = 0; i < fmeasure.length; i++) {
            System.out.print(" "+fmeasure[i] );
           
            
        }
        
        
        
        
                 //System.out.println("Pior Caso ");
                 BlockIndex bi5 = new BlockIndex();
                 SimilarityIndex si5 = new SimilarityIndex();
                 SingleLink cluster = new SingleLink(si5, bi5);

               
                BlockIndex bi6 = new BlockIndex();
                Amostra amostra5 = new Amostra();
                amostra5.geraPosicao(9759, 9760);
                
                bi6 = amostra5.blocaDadosDaAmostraConsulta(amostra5.carregaDadosCD());
              
                
                //Para calcular com reuso
               long tempoInicialWorst = System.currentTimeMillis();
                cluster.singleLinkIngenuo(bi6);//armazena amostra
                 double tempo2 = System.currentTimeMillis() - tempoInicialWorst;                 
                System.out.println(" Tempo pior caso: " + tempo2);     
                
                BlockIndex bi73 = new BlockIndex();
                Amostra amostra4 = new Amostra();
                amostra4.geraPosicao(9759, 9760);
                bi73 = amostra4.blocaDadosDaAmostraConsulta(amostra4.carregaDadosCD());
                long tempoInicialBest = System.currentTimeMillis();
                cluster.singleLinkIngenuo(bi73);
                double tempo = System.currentTimeMillis() - tempoInicialBest;
                 //System.out.println(" Melhor Caso ");
                System.out.println(" Tempo melhor caso: " + tempo);
                
                System.out.println(" Tempo Tradicional " );              
                BlockIndex bi8 = new BlockIndex();
                Amostra amostra8 = new Amostra();
                amostra8.geraPosicao(9759, 9760);
                SimilarityIndex si8 = new SimilarityIndex();
                SingleLink cluster8 = new SingleLink(si8, bi8);
                bi8 = amostra8.blocaDadosDaAmostraConsulta(amostra8.carregaDadosCD());
                long tempoInicialTradicional = System.currentTimeMillis();
                cluster8.singleLinkIngenuo2(bi8);
                double tempoT = System.currentTimeMillis() - tempoInicialTradicional;
              
                System.out.println(" Tempo Tradicional: " + tempoT);
                
                
                
        
                
        
    }

}

    

