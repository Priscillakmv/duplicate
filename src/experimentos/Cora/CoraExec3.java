/*
 * DuDe - The Duplicate Detection Toolkit
 * 
 * Copyright (C) 2010  Hasso-Plattner-Institut fÃ¼r Softwaresystemtechnik GmbH,
 *                     Potsdam, Germany 
 *
 * 
 */
package experimentos.Cora;

import classificacao.ClusterizacaoIncrementalIngenua;
import Index.BlockIndex;
import classificacao.ClusterizacaoIngenuaLote;
import java.io.IOException;
import query.QueryExperimento;

/**
 * Caso Tradicional
 * This execution class runs the naive duplicate detection algorithm on the
 * <code>CD</code> data source. Two records are similar if their titles match
 * based on the Levenshtein distance.
 *Roda com a base toda e insere tudo no BI. Depoisclusteriza tradicional so com, sem recuperar nada prévio
 * @author Matthias Pohl, Uwe Draisbach
 */
public class CoraExec3 {

    /**
     * Executes the naive duplicate detection on the <code>CD</code> data
     * source. During the process all duplicates will be written onto the
     * console.
     *
     * @param args No arguments will be processed.
     * @throws IOException If an error occurs while reading from the file.
     */
    public static void main(String[] args) throws IOException {
        // enables dynamic data-loading for file-based sorting
       long tempoInicial = System.currentTimeMillis();
        QueryExperimento queryTradicional = new QueryExperimento();
        BlockIndex bi2= queryTradicional.blocaConsultaCora(queryTradicional.carregaDadosCora());
        ClusterizacaoIngenuaLote cluster = new ClusterizacaoIngenuaLote(bi2);
        cluster.cluserizaTradicional_NaoAcessaBIeSI(bi2);
        System.err.println(" tetsdfsdf " + bi2.getNumeroElementos());
        System.out.println(" Tempo para processar a abordagem tradicional com o algoritmo ingenuo " + (System.currentTimeMillis()-tempoInicial));
       // bi2.printBlockIndex();
// bi2.printBlockIndex();
     
        

       
        }
      
}
        // Write statistics
        //StatisticOutput statisticOutput = new SimpleStatisticOutput(System.out, statistic);
       // statisticOutput.writeStatistics();
       // System.out.println("Experiment finished Pior Caso ou melhor caso." + (System.currentTimeMillis() - start) + "  ms");

        
// clean up
       
