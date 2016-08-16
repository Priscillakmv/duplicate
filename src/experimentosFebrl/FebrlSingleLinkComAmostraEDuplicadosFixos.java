/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package experimentosFebrl;

import Index.BlockIndex;
import Index.SimilarityIndex;
import avaliacao.Calculadora;
import avaliacao.Cluster;
import classificacao.SingleLink;
import de.hpi.fgis.dude.datasource.CSVSource;
import java.io.IOException;
import query.Amostra;
import util.Arquivo;

/**
 *
 * @author Priscilla
 */
public class FebrlSingleLinkComAmostraEDuplicadosFixos {
     public static void main(String[] args) throws IOException {

        int numeroIteracoes = 1;
        double temposAcumuladosAux[] = new double[numeroIteracoes];
       double temposAcumulados[] = new double[numeroIteracoes];



        for (int j = 0; j < numeroIteracoes; j++) {
            BlockIndex bi = new BlockIndex();
            SimilarityIndex si = new SimilarityIndex();
            SingleLink cluster = new SingleLink(si, bi);

            int numeroConsultas = 100;
            double valoresEsperados[] = new double[numeroConsultas];
          double tempos[] = new double[numeroConsultas];
            double valoresAcertados[] = new double[numeroConsultas];
            double valoresErrados[] = new double[numeroConsultas];

            //double valoresEsperadosAux[] = new double[numeroIteracoes];
           double temposAux[] = new double[numeroConsultas];
            double valoresAcertadosAux[] = new double[numeroConsultas];
           double valoresErradosAux[] = new double[numeroConsultas];
           Amostra amostra = new Amostra();
               amostra.geraPosicaoApartirEspelho(42757, 42758);
               amostra.insereDuplicadosIdNaAmostra(amostra.carregaDadosFebrlEspelho());

            for (int i = 0; i < numeroConsultas; i++) {
                //Para Calcular Sem reuso
                BlockIndex biAux = new BlockIndex();
                SimilarityIndex siAux = new SimilarityIndex();
                SingleLink clusterAux = new SingleLink(siAux, biAux);
                BlockIndex bi2Aux = new BlockIndex();

                BlockIndex bi2 = new BlockIndex();
                //Amostra amostra = new Amostra();
                //amostra.geraPosicaoApartirEspelho(42757, 42758);
                System.err.println(" Segundo caso 8600");
                
                //inserir os duplicados na amostra        
      
                //aux ja tá com os duplicados atualiza
               // amostra.insereDuplicadosIdNaAmostra(amostra.carregaDadosFebrlEspelho());
                
                //gera posicao resto da base para completar
                //amostra.geraPosicao((1000-amostra.getAux().size()), 130002);
                System.out.println(" Tamanho amostras " +amostra.getAux().size() );
                Amostra amostra2 = new Amostra();
                amostra2.geraPosicao(4200, (amostra.getAux().size()+1));
                bi2 = amostra2.blocaDadosDaAmostraConsultaFebrl(amostra2.carregaDadosFebrl());
                bi2Aux = bi2;

                //Para os dois cados
                 System.out.println(" Tamanho do Reuso tradicional " );

                valoresEsperados[i] = (long) amostra2.calculaDuplicadosDaEntradaConsultaFebrl();
                //para calcular sem reuso
                System.out.println(" Tamanho do Reuso tradicional " );

                long tempoInicialAux = System.currentTimeMillis();
                //alterei olugar do sinew
                clusterAux.setTempoIncremental(0);
                clusterAux.singleLinkFebrl7(bi2Aux);
                temposAux[i] = (System.currentTimeMillis() - tempoInicialAux);
                valoresAcertadosAux[i] = ((long) clusterAux.getTotalCerto());
                valoresErradosAux[i] = ((long) clusterAux.getTotalErrado());
                System.out.println(" CERTO Aux  " + valoresAcertadosAux[i]);
                System.out.println(" ERRADO Aux  " + valoresErradosAux[i]);

                //Para calcular com reuso
                 System.out.println(" Tamanho do Reuso incremental " );


                long tempoInicial = System.currentTimeMillis();
                cluster.singleLinkFebrl7(bi2);
                tempos[i] = ((System.currentTimeMillis()- tempoInicial)-cluster.getTempoParaDiminuir());
                //tempos[i] = ((System.currentTimeMillis()- tempoInicial));
                valoresAcertados[i] = ((long) cluster.getTotalCerto());
                valoresErrados[i] = ((long) cluster.getTotalErrado());
                System.out.println(" CERTO  " + valoresAcertados[i]);
                System.out.println(" ERRADO  " + valoresErrados[i]);
                
               /** amostra.geraPosicao((10000-amostra.getAux().size()), 130002);
                BlockIndex bi3 = amostra.blocaDadosDaAmostraConsultaFebrl(amostra.carregaDadosFebrl());
                SimilarityIndex si3 = new SimilarityIndex();
                BlockIndex bi4 = new BlockIndex();
                BlockIndex b4aux = bi3;
                SingleLink cluster3 = new SingleLink(si, bi4);
                System.err.println(" Tempo sem reuso *************************************: ");
                long tempoInicialAux4 = System.currentTimeMillis();
                cluster3.singleLinkFebrl6(b4aux);
                System.err.println(" tempo: **********************"+ (System.currentTimeMillis()- tempoInicialAux4));
                
                                System.err.println(" Tempo com reuso******************************: ");
                long tempoInicial4 = System.currentTimeMillis();

                cluster.singleLinkFebrl6(bi3);
                
                System.err.println(" tempo: **************************"+ ((System.currentTimeMillis()- tempoInicial4)-cluster.getTempoParaDiminuir()));
                System.err.println(" Tamanho SI e BI" + cluster.getSi().getTamanho() + " " + cluster.getBi().getNumeroElementos());**/

            }
            //System.out.println(" Diminuir " +cluster.getTempoParaDiminuir());
            Calculadora calculadora = new Calculadora();

            System.out.println(" Média PrecisaoAux: " + calculadora.calculaPrecisao(valoresAcertadosAux, valoresErradosAux));
            System.out.println(" Média RecallAux: " + calculadora.calculaRecall(valoresAcertadosAux, valoresEsperados));
            System.out.println(" Média TempoAux: " + calculadora.calculaMediaAritmetica(temposAux));
            System.out.println(" Média F-MeasureAux: " + calculadora.calculaFMeasure(valoresAcertadosAux, valoresEsperados, valoresErradosAux));

            System.out.println(" Média Precisao: " + calculadora.calculaPrecisao(valoresAcertados, valoresErrados));
            System.out.println(" Média Recall: " + calculadora.calculaRecall(valoresAcertados, valoresEsperados));
            System.out.println(" Média Tempo: " + calculadora.calculaMediaAritmetica(tempos));
            System.out.println(" Média F-Measure: " + calculadora.calculaFMeasure(valoresAcertados, valoresEsperados, valoresErrados));

            Arquivo saida = new Arquivo("Experimento Tempos1 " + j);
            saida.insereTemposEmTXT(tempos);
            Arquivo saidaAux = new Arquivo("ExperimentoAux Tempos1" + j);
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
