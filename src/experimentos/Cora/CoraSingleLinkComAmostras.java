/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package experimentos.Cora;

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
public class CoraSingleLinkComAmostras {
    
     public static void main(String[] args) throws IOException {
        /** System.out.println(" Com toda a base: *******************");
          Amostra amostra2 = new Amostra();
         amostra2.geraPosicaoCora(1879, 1880);
         BlockIndex bi3 = new BlockIndex();                
               
                bi3 = amostra2.blocaDadosDaAmostraConsultaCora(amostra2.carregaDadosCora());
                SimilarityIndex si3 = new SimilarityIndex();
                SingleLink clusterAux3 = new SingleLink(si3, bi3);
                clusterAux3.singleLinkCora6(bi3);**/
               
         

        int numeroIteracoes = 1;
        double temposAcumuladosAux[] = new double[numeroIteracoes];
       double temposAcumulados[] = new double[numeroIteracoes];
       double temposParaDiminuir []=new double [numeroIteracoes];



        for (int j = 0; j < numeroIteracoes; j++) {
            BlockIndex bi = new BlockIndex();
            SimilarityIndex si = new SimilarityIndex();
            SingleLink cluster = new SingleLink(si, bi);

            int numeroConsultas = 60;
            double valoresEsperados[] = new double[numeroConsultas];
          double tempos[] = new double[numeroConsultas];
            double valoresAcertados[] = new double[numeroConsultas];
            double valoresErrados[] = new double[numeroConsultas];

            //double valoresEsperadosAux[] = new double[numeroIteracoes];
           double temposAux[] = new double[numeroConsultas];
            double valoresAcertadosAux[] = new double[numeroConsultas];
           double valoresErradosAux[] = new double[numeroConsultas];

            for (int i = 0; i < numeroConsultas; i++) {
                //Para Calcular Sem reuso
                BlockIndex biAux = new BlockIndex();
                SimilarityIndex siAux = new SimilarityIndex();
                SingleLink clusterAux = new SingleLink(siAux, biAux);
                BlockIndex bi2Aux = new BlockIndex();

                BlockIndex bi2 = new BlockIndex();
                Amostra amostra = new Amostra();
                amostra.geraPosicaoCora(200, 1880);
                
               
                bi2 = amostra.blocaDadosDaAmostraConsultaCora(amostra.carregaDadosCora());
                bi2Aux = bi2;

                //Para os dois cados
                System.out.println(" Tamanho do Reuso tradicional " );

                valoresEsperados[i] = (long) amostra.calculaDuplicadosDaEntradaConsultaCora();
                //para calcular sem reuso
                System.out.println(" Tamanho do Reuso tradicional " );

                long tempoInicialAux = System.currentTimeMillis();
                //alterei olugar do sinew
                clusterAux.singleLinkCora6(bi2Aux);
                temposAux[i] = (System.currentTimeMillis() - tempoInicialAux);
                valoresAcertadosAux[i] = ((long) clusterAux.getTotalCerto());
                valoresErradosAux[i] = ((long) clusterAux.getTotalErrado());
                System.out.println(" CERTO Aux  " + valoresAcertadosAux[i]);
                System.out.println(" ERRADO Aux  " + valoresErradosAux[i]);

                //Para calcular com reuso
                 System.out.println(" Tamanho do Reuso incremental " );


                long tempoInicial = System.currentTimeMillis();
                cluster.singleLinkCora6(bi2);
                tempos[i] = ((System.currentTimeMillis()- tempoInicial)-cluster.getTempoParaDiminuir());
                valoresAcertados[i] = ((long) cluster.getTotalCerto());
                valoresErrados[i] = ((long) cluster.getTotalErrado());
                System.out.println(" CERTO  " + valoresAcertados[i]);
                System.out.println(" ERRADO  " + valoresErrados[i]);

            }
            temposParaDiminuir[j]=  cluster.getTempoParaDiminuir()/100;
            System.out.println(" Diminuir " +cluster.getTempoParaDiminuir());
            Calculadora calculadora = new Calculadora();

            System.out.println(" Média PrecisaoAux: " + calculadora.calculaPrecisao(valoresAcertadosAux, valoresErradosAux));
            System.out.println(" Média RecallAux: " + calculadora.calculaRecall(valoresAcertadosAux, valoresEsperados));
            System.out.println(" Média TempoAux: " + calculadora.calculaMediaAritmetica(temposAux));
            System.out.println(" Média F-MeasureAux: " + calculadora.calculaFMeasure(valoresAcertadosAux, valoresEsperados, valoresErradosAux));

            System.out.println(" Média Precisao: " + calculadora.calculaPrecisao(valoresAcertados, valoresErrados));
            System.out.println(" Média Recall: " + calculadora.calculaRecall(valoresAcertados, valoresEsperados));
            System.out.println(" Média Tempo: " + calculadora.calculaMediaAritmetica(tempos));
            System.out.println(" Média F-Measure: " + calculadora.calculaFMeasure(valoresAcertados, valoresEsperados, valoresErrados));

            Arquivo saida = new Arquivo("Experimento TemposCora " + j);
            saida.insereTemposEmTXT(tempos);
            Arquivo saidaAux = new Arquivo("ExperimentoAux TemposCora" + j);
            saidaAux.insereTemposEmTXT(temposAux);   
            Arquivo saidaFmeasure = new Arquivo("Experimento FmeasureCora" + j);
            saidaFmeasure.insereTemposEmTXT(calculadora.calculaFMeasureArray(valoresAcertados, valoresEsperados, valoresErrados));
            
            Arquivo saidaFmeasureAux = new Arquivo("Experimento FmeasureAuxCora" + j);
            saidaFmeasureAux.insereTemposEmTXT(calculadora.calculaFMeasureArray(valoresAcertadosAux, valoresEsperados, valoresErradosAux));
            
            
            Arquivo saidaPrecisao = new Arquivo("Experimento PrecisaoCora" + j);
            saidaPrecisao.insereTemposEmTXT(calculadora.calculaPrecisaoArray(valoresAcertados, valoresErrados));
            
             Arquivo saidaPrecisaoAux = new Arquivo("Experimento PrecisaoAuxCora" + j);
            saidaPrecisaoAux.insereTemposEmTXT(calculadora.calculaPrecisaoArray(valoresAcertadosAux, valoresErradosAux));
            
            
            temposAcumulados[j]=calculadora.calculaMediaAritmetica(tempos);
            temposAcumuladosAux[j]=calculadora.calculaMediaAritmetica(temposAux);
            
            

        }
                    Calculadora calculadora2 = new Calculadora();

         for (int i = 0; i < temposParaDiminuir.length; i++) {
             System.out.println(" TEmpos para siminuir: " + temposParaDiminuir[i]);
             
         }
                    System.out.println(" Média Tempo Acumulado: " + calculadora2.calculaMediaAritmetica(temposAcumulados));
                                        System.out.println(" Média Tempo Acumulado Aux: " + calculadora2.calculaMediaAritmetica(temposAcumuladosAux));


        //   System.out.println("F-Measure " + (2 * ((calculadora.calculaRecall(valoresAcertados, valoresEsperados) * calculadora.calculaPrecisao(valoresAcertados, valoresErrados)) / (calculadora.calculaPrecisao(valoresAcertados, valoresErrados) + calculadora.calculaRecall(valoresAcertados, valoresEsperados)))));
//contar qd cada um ganha. Analisar a diferenca de classificao entre eles
    }
    
    
}
