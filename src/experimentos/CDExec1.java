/*
 * DuDe - The Duplicate Detection Toolkit
 * 
 * Copyright (C) 2010  Hasso-Plattner-Institut fÃ¼r Softwaresystemtechnik GmbH,
 *                     Potsdam, Germany 
 *
 * This file is part of DuDe.
* 
 * DuDe is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * DuDe is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with DuDe.  If not, see <http://www.gnu.org/licenses/>.
 * PRECISO CLUSTERIZAR A SAIDA
 */
package experimentos;

import java.io.File;
import java.io.IOException;

import de.hpi.fgis.dude.algorithm.Algorithm;
import de.hpi.fgis.dude.algorithm.duplicatedetection.NaiveDuplicateDetection;
import de.hpi.fgis.dude.algorithm.duplicatedetection.RSwoosh;
import de.hpi.fgis.dude.algorithm.duplicatedetection.SortedNeighborhoodMethod;
import de.hpi.fgis.dude.datasource.CSVSource;
import de.hpi.fgis.dude.output.DuDeOutput;
import de.hpi.fgis.dude.output.JsonOutput;
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

/**
 * This execution class runs the naive duplicate detection algorithm on the
 * <code>CD</code> data source. Two records are similar if their titles match
 * based on the Levenshtein distance.
 *
 * @author Matthias Pohl, Uwe Draisbach
 */
public class CDExec1 {

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
        GlobalConfig.getInstance().setInMemoryObjectThreshold(1000000);

		// instantiates the CSV data source for reading records
        // "cddb" is the source identifier
        CSVSource dataSource = new CSVSource("febrl", new File("febrl.csv"));
                System.err.println(" Entrada:" + dataSource.getExtractedRecordCount());

        dataSource.enableHeader();

        // uses the id attribute for the object id - this call is optional, if no id attribute is set, DuDe will generate its own object ids
        dataSource.addIdAttributes("id");

		//instantiates the CSV data source for reading the goldstandard
        // "goldstandard" is the goldstandard identifier
        CSVSource goldstandardSource = new CSVSource("goldstandard", new File("gold_febrl.csv"));
        goldstandardSource.enableHeader();

		// instantiate the gold standard
        // "cddb" is the source identifier
        GoldStandard goldStandard = new GoldStandard(goldstandardSource);
        goldStandard.setFirstElementsObjectIdAttributes("febrl1_id");
        goldStandard.setSecondElementsObjectIdAttributes("febrl2_id");
        goldStandard.setSourceIdLiteral("febrl");
        

        
Algorithm algorithm = new NaiveDuplicateDetection();
        //Algorithm algorithm = new SortedNeighborhoodMethod(sortingKey, 20);
        

		// instantiates the naive duplicate detection algorithm
        //Algorithm algorithm = new NaiveDuplicateDetection();
        //algorithm.enableInMemoryProcessing();
        // adds the "data" to the algorithm
        algorithm.addDataSource(dataSource);

		// instantiates the similarity function
        // checks the Levenshtein distance of the CD titles
        SimilarityFunction similarityFunction = new LevenshteinDistanceFunction("rec2_id");
        //LevenshteinDistanceFunction similarityFunction = new   LevenshteinDistanceFunction("artist");

		// writes the duplicate pairs onto the console by using the Json syntax
        //DuDeOutput output = new JsonOutput(System.out);
		// instantiate statistic component to calculate key figures
        // like runtime, number of comparisons, precision and recall
        StatisticComponent statistic = new StatisticComponent(goldStandard, algorithm);

		// the actual computation starts
        // the algorithm returns each generated pair step-by-step
        statistic.setStartTime();
        long start = System.currentTimeMillis();

        for (DuDeObjectPair pair : algorithm) {
            final double similarity = similarityFunction.getSimilarity(pair);
            if (similarity ==1) {
				// if it is a duplicate - print it and add it to the
                // statistic component as duplicate
                //output.write(pair);
                //System.out.println();
                System.err.println(" SAida ++++++++++++++++++++++++++++++=" + pair.getFirstElementObjectData().toString());
                statistic.addDuplicate(pair);
            } else {
				// if it is not a duplicate, add it to the statistic
                // component as non-duplicate
                               // System.out.println(" &&&&&&");

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
