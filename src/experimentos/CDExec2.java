/*
 * DuDe - The Duplicate Detection Toolkit
 * 
 * Copyright (C) 2010  Hasso-Plattner-Institut fÃ¼r Softwaresystemtechnik GmbH,
 *                     Potsdam, Germany 
 *
 *Rora com a base toda e insere tudo no BI e SI. Depois chama com a base toda apenas o BI
 * 
 */
package experimentos;

import classificacao.ClusterizacaoIncrementalIngenua;
import Index.BlockIndex;
import Index.SimilarityIndex;
import java.io.File;
import java.io.IOException;
import de.hpi.fgis.dude.algorithm.Algorithm;
import de.hpi.fgis.dude.algorithm.duplicatedetection.SortedNeighborhoodMethod;
import de.hpi.fgis.dude.datasource.CSVSource;
import de.hpi.fgis.dude.postprocessor.StatisticComponent;
import de.hpi.fgis.dude.preprocessor.DocumentFrequencyPreprocessor;
import de.hpi.fgis.dude.similarityfunction.contentbased.impl.simmetrics.LevenshteinDistanceFunction;
import de.hpi.fgis.dude.util.GlobalConfig;
import de.hpi.fgis.dude.util.GoldStandard;
import de.hpi.fgis.dude.util.data.DuDeObjectPair;
import de.hpi.fgis.dude.util.sorting.sortingkey.SortingKey;
import de.hpi.fgis.dude.util.sorting.sortingkey.TextBasedSubkey;
import org.apache.commons.codec.language.DoubleMetaphone;
import query.QueryExperimento;

/**
 * This execution class runs the naive duplicate detection algorithm on the
 * <code>CD</code> data source. Two records are similar if their titles match
 * based on the Levenshtein distance.
 *Melhor Caso e pior Caso
 * @author Matthias Pohl, Uwe Draisbach
 */
public class CDExec2 {

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
        GlobalConfig.getInstance().setInMemoryObjectThreshold(10000);

        // instantiates the CSV data source for reading records
        // "cddb" is the source identifier
        CSVSource dataSource = new CSVSource("cddb", new File("saida.csv"));
        dataSource.enableHeader();

        // uses the id attribute for the object id - this call is optional, if no id attribute is set, DuDe will generate its own object ids
        dataSource.addIdAttributes("id");

        //instantiates the CSV data source for reading the goldstandard
        // "goldstandard" is the goldstandard identifier
        CSVSource goldstandardSource = new CSVSource("goldstandard", new File("cd_gold.csv"));
        goldstandardSource.enableHeader();

        // instantiate the gold standard
        // "cddb" is the source identifier
        GoldStandard goldStandard = new GoldStandard(goldstandardSource);
        goldStandard.setFirstElementsObjectIdAttributes("disc1_id");
        goldStandard.setSecondElementsObjectIdAttributes("disc2_id");
        goldStandard.setSourceIdLiteral("cddb");

        TextBasedSubkey artistSubkey = new TextBasedSubkey("artist");
        artistSubkey.setIgnoredCharactersRegEx(TextBasedSubkey.NO_VOWELS_REGEX);

        DocumentFrequencyPreprocessor dfPreprocessor = new DocumentFrequencyPreprocessor("blocking");
        // the key generator uses sub-key selectors to generate a key for each object
        SortingKey sortingKey = new SortingKey();
        sortingKey.addSubkey(artistSubkey);

        Algorithm algorithm = new SortedNeighborhoodMethod(sortingKey, 20);
        algorithm.enableInMemoryProcessing();
        algorithm.addPreprocessor(dfPreprocessor);

        // instantiates the naive duplicate detection algorithm
        //Algorithm algorithm = new NaiveDuplicateDetection();
        //algorithm.enableInMemoryProcessing();
        // adds the "data" to the algorithm
        algorithm.addDataSource(dataSource);

        // instantiates the similarity function
        // checks the Levenshtein distance of the CD titles
        LevenshteinDistanceFunction similarityFunction = new LevenshteinDistanceFunction("id");

		// writes the duplicate pairs onto the console by using the Json syntax
        //DuDeOutput output = new JsonOutput(System.out);
        // instantiate statistic component to calculate key figures
        // like runtime, number of comparisons, precision and recall
        StatisticComponent statistic = new StatisticComponent(goldStandard, algorithm);

        // the actual computation starts
        // the algorithm returns each generated pair step-by-step
        statistic.setStartTime();
        BlockIndex bi = new BlockIndex();
        SimilarityIndex si = new SimilarityIndex();
        DoubleMetaphone db = new DoubleMetaphone();
        long start = System.currentTimeMillis();
        int controle=0;
        
        ClusterizacaoIncrementalIngenua cl = new ClusterizacaoIncrementalIngenua(bi,si);

        for (DuDeObjectPair pair : algorithm) {
            controle ++;
            final double similarity = similarityFunction.getSimilarity(pair);
             String pk1 = pair.getFirstElement().toString();
                String pk2 = pair.getSecondElement().toString();

                
            if (similarity > 0.9) {
                // if it is a duplicate - print it and add it to the
                // statistic component as duplicate
                //output.write(pair);
                //System.out.println();
                System.out.println(" TESTE");
                statistic.addDuplicate(pair);
               

                //int cluster1 = bi.getclusterId(pk1, keyBlockpair1, "cd");
                //int cluster2 = bi.getclusterId(pk2, keyBlockpair2, "cd");
                //rever cluster
              
                
               
               // Vertice v1 = new Vertice(pk1, "cd", 0);
                //Vertice v2 = new Vertice(pk2, "cd", 0);

                //bi.insertVertice(keyBlockpair1, v1);
               // bi.insertVertice(keyBlockpair2, v2);
                
               
            } else {
                // if it is not a duplicate, add it to the statistic
                // component as non-duplicate
                statistic.addNonDuplicate(pair);
            }
        }
        System.out.println(" Tamanho Salvo " +  si.getTamanho() + "  controle" + controle);
        System.out.println(" Tamanho Salvo BI " +  bi.getNumeroElementos());
        //statistic.setEndTime();

        // Write statistics
        //StatisticOutput statisticOutput = new SimpleStatisticOutput(System.out, statistic);
       // statisticOutput.writeStatistics();
       // System.out.println("Experiment finished Pior Caso ou melhor caso." + (System.currentTimeMillis() - start) + "  ms");

        
// clean up
        dataSource.cleanUp();
        QueryExperimento query = new QueryExperimento(cl.getBi(), cl.getSi());
        //Consulta do melhor caso. Acessei tudo o que eu consultei
        query.queryBuscaTudo(cl);
        //goldStandard.close();
    }

}
