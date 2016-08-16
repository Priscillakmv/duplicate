/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package experimentos;

import de.hpi.fgis.dude.algorithm.Algorithm;
import de.hpi.fgis.dude.algorithm.duplicatedetection.RSwoosh;
import de.hpi.fgis.dude.algorithm.recordlinkage.NaiveRecordLinkage;
import de.hpi.fgis.dude.datasource.CSVSource;
import de.hpi.fgis.dude.output.statisticoutput.SimpleStatisticOutput;
import de.hpi.fgis.dude.output.statisticoutput.StatisticOutput;
import de.hpi.fgis.dude.postprocessor.StatisticComponent;
import de.hpi.fgis.dude.preprocessor.DocumentFrequencyPreprocessor;
import de.hpi.fgis.dude.similarityfunction.SimilarityFunction;
import de.hpi.fgis.dude.similarityfunction.aggregators.Average;
import de.hpi.fgis.dude.similarityfunction.contentbased.impl.simmetrics.LevenshteinDistanceFunction;
import de.hpi.fgis.dude.util.GlobalConfig;
import de.hpi.fgis.dude.util.GoldStandard;
import de.hpi.fgis.dude.util.data.DuDeObjectPair;
import de.hpi.fgis.dude.util.sorting.sortingkey.SortingKey;
import de.hpi.fgis.dude.util.sorting.sortingkey.TextBasedSubkey;
import java.io.File;
import java.io.IOException;

/**
 *
 * @author Priscilla
 */
public class FebrlExemplo {

    public static void main(String[] args) throws IOException {

      GlobalConfig.getInstance().setInMemoryObjectThreshold(1000000);

        CSVSource dataSource = new CSVSource("febrl", new File("febrl_gold.csv"));
       System.err.println(" Entrada:" + dataSource.getExtractedRecordCount());
        
      //dataSource.setSeparatorCharacter(',');
        dataSource.enableHeader();

       dataSource.addIdAttributes("ID");

        CSVSource goldstandardSource = new CSVSource("goldstandard", new File("gold_base_priscilla_2.csv"));
        goldstandardSource.enableHeader();

        // instantiate the gold standard
        // "cddb" is the source identifier
        GoldStandard goldStandard = new GoldStandard(goldstandardSource);
        goldStandard.setFirstElementsObjectIdAttributes("ID_1");
        goldStandard.setSecondElementsObjectIdAttributes("ID_2");
        goldStandard.setSourceIdLiteral("febrl");

        Algorithm algorithm = new NaiveRecordLinkage();
        algorithm.enableInMemoryProcessing();

        algorithm.addDataSource(dataSource);

        // SimilarityFunction similarityFunction = new Average(new LevenshteinDistanceFunction("GIVEN_NAME"), new LevenshteinDistanceFunction("SURNAME"));
        SimilarityFunction similarityFunction = new LevenshteinDistanceFunction("GIVEN_NAME");
        StatisticComponent statistic = new StatisticComponent(goldStandard, algorithm);

        statistic.setStartTime();
        long start = System.currentTimeMillis();

        for (DuDeObjectPair pair : algorithm) {
            final double similarity = similarityFunction.getSimilarity(pair);
            if (similarity > 0.8) {
                statistic.addDuplicate(pair);
                System.err.println(" Teste "+ pair.getFirstElementObjectData().toString());
            } else {

                statistic.addNonDuplicate(pair);
            }
        }
        statistic.setEndTime();

        // Write statistics
        StatisticOutput statisticOutput = new SimpleStatisticOutput(System.out, statistic);
        statisticOutput.writeStatistics();
        System.out.println("Experiment finished." + (System.currentTimeMillis() - start) + "  ms");

        // clean up
        dataSource.cleanUp();
        //goldStandard.close();
    }

}
