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
import util.Arquivo;

/**
 *
 * @author Priscilla
 */

    
    /*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 * Esta Classe cria amostrar aleatórias com o objetivo de mostrar que ao longo
 * do tempo o tempo de processamento incremental cai e fica menor que o em lote.
 * As métricas de qualidade são aproximadas. Precisao incrementa é até melhor
 */


import Index.BlockIndex;
import Index.SimilarityIndex;
import avaliacao.Calculadora;
import classificacao.SingleLink;
import java.io.IOException;
import java.util.ArrayList;
import query.Amostra;
import query.QueryExperimento;
import util.Arquivo;

/**
 *
 * @author Priscilla
 */
public class FebrlSigleLinkComAmostras2 {

    public static void main(String[] args) throws IOException {

        int numeroIteracoes = 1;
        double temposAcumuladosAux[] = new double[numeroIteracoes];
       double temposAcumulados[] = new double[numeroIteracoes];



        for (int j = 0; j < numeroIteracoes; j++) {
            BlockIndex bi = new BlockIndex();
            SimilarityIndex si = new SimilarityIndex();
            SingleLink cluster = new SingleLink(si, bi);

            int numeroConsultas = 1;
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
                amostra.geraPosicao(9000, 130002);
                bi2 = amostra.blocaDadosDaAmostraConsultaFebrl(amostra.carregaDadosFebrl());
                bi2Aux = bi2;

                //Para os dois cados
                valoresEsperados[i] = (long) amostra.calculaDuplicadosDaEntradaConsultaFebrl();
                //para calcular sem reuso
                System.out.println(" Tamanho do Reuso tradicional " + bi2Aux.getNumeroElementos()+" "+ bi2.getNumeroBlocos());

                long tempoInicialAux = System.currentTimeMillis();
                clusterAux.singleLink(bi2Aux);
                temposAux[i] = (System.currentTimeMillis() - tempoInicialAux);
                valoresAcertadosAux[i] = ((long) clusterAux.getTotalCerto());
                valoresErradosAux[i] = ((long) clusterAux.getTotalErrado());
                System.out.println(" CERTO Aux  " + valoresAcertadosAux[i]);
                System.out.println(" ERRADO Aux  " + valoresErradosAux[i]);

                //Para calcular com reuso
                 System.out.println(" Tamanho do Reuso incremental " );


                long tempoInicial = System.currentTimeMillis();
                cluster.singleLink(bi2);
                tempos[i] = (System.currentTimeMillis()- tempoInicial);
                valoresAcertados[i] = ((long) cluster.getTotalCerto());
                valoresErrados[i] = ((long) cluster.getTotalErrado());
                System.out.println(" CERTO  " + valoresAcertados[i]);
                System.out.println(" ERRADO  " + valoresErrados[i]);

            }
            Calculadora calculadora = new Calculadora();

            System.out.println(" Média PrecisaoAux: " + calculadora.calculaPrecisao(valoresAcertadosAux, valoresErradosAux));
            System.out.println(" Média RecallAux: " + calculadora.calculaRecall(valoresAcertadosAux, valoresEsperados));
            System.out.println(" Média TempoAux: " + calculadora.calculaMediaAritmetica(temposAux));
            System.out.println(" Média F-MeasureAux: " + calculadora.calculaFMeasure(valoresAcertadosAux, valoresEsperados, valoresErradosAux));

            System.out.println(" Média Precisao: " + calculadora.calculaPrecisao(valoresAcertados, valoresErrados));
            System.out.println(" Média Recall: " + calculadora.calculaRecall(valoresAcertados, valoresEsperados));
            System.out.println(" Média Tempo: " + calculadora.calculaMediaAritmetica(tempos));
            System.out.println(" Média F-Measure: " + calculadora.calculaFMeasure(valoresAcertados, valoresEsperados, valoresErrados));

            Arquivo saida = new Arquivo("Experimento Tempos " + j);
            saida.insereTemposEmTXT(tempos);
            Arquivo saidaAux = new Arquivo("ExperimentoAux Tempos" + j);
            saidaAux.insereTemposEmTXT(temposAux);   
            Arquivo saidaFmeasure = new Arquivo("Experimento Fmeasure" + j);
            saidaFmeasure.insereTemposEmTXT(calculadora.calculaFMeasureArray(valoresAcertados, valoresEsperados, valoresErrados));
            
            Arquivo saidaFmeasureAux = new Arquivo("Experimento FmeasureAux" + j);
            saidaFmeasureAux.insereTemposEmTXT(calculadora.calculaFMeasureArray(valoresAcertadosAux, valoresEsperados, valoresErradosAux));
            
            
            Arquivo saidaPrecisao = new Arquivo("Experimento Precisao" + j);
            saidaPrecisao.insereTemposEmTXT(calculadora.calculaPrecisaoArray(valoresAcertados, valoresErrados));
            
             Arquivo saidaPrecisaoAux = new Arquivo("Experimento PrecisaoAux" + j);
            saidaPrecisaoAux.insereTemposEmTXT(calculadora.calculaPrecisaoArray(valoresAcertadosAux, valoresErradosAux));
            
            
            temposAcumulados[j]=calculadora.calculaMediaAritmetica(tempos);
            temposAcumuladosAux[j]=calculadora.calculaMediaAritmetica(temposAux);

        }
                    Calculadora calculadora2 = new Calculadora();

        
                    System.out.println(" Média Tempo Acumulado: " + calculadora2.calculaMediaAritmetica(temposAcumulados));
                                        System.out.println(" Média Tempo Acumulado Aux: " + calculadora2.calculaMediaAritmetica(temposAcumuladosAux));


        //   System.out.println("F-Measure " + (2 * ((calculadora.calculaRecall(valoresAcertados, valoresEsperados) * calculadora.calculaPrecisao(valoresAcertados, valoresErrados)) / (calculadora.calculaPrecisao(valoresAcertados, valoresErrados) + calculadora.calculaRecall(valoresAcertados, valoresEsperados)))));
//contar qd cada um ganha. Analisar a diferenca de classificao entre eles
    }

}

    
    
    

