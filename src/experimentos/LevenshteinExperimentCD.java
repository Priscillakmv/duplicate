/*
 * DuDe - The Duplicate Detection Toolkit
 * 
 * Copyright (C) 2010  Hasso-Plattner-Institut für Softwaresystemtechnik GmbH,
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
 * 
 */
package experimentos;

import java.io.File;

import de.hpi.fgis.dude.algorithm.Algorithm;
import de.hpi.fgis.dude.algorithm.duplicatedetection.SortedNeighborhoodMethod;
import de.hpi.fgis.dude.datasource.CSVSource;

import de.hpi.fgis.dude.preprocessor.DocumentFrequencyPreprocessor;
import de.hpi.fgis.dude.similarityfunction.SimilarityFunction;
import de.hpi.fgis.dude.similarityfunction.contentbased.impl.simmetrics.LevenshteinDistanceFunction;
import de.hpi.fgis.dude.util.GlobalConfig;

import de.hpi.fgis.dude.util.data.DuDeObjectPair;
import de.hpi.fgis.dude.util.sorting.sortingkey.SortingKey;
import de.hpi.fgis.dude.util.sorting.sortingkey.TextBasedSubkey;

/**
 * Ëxecuta o algoritmo sem clusterizar. Apenas indicando pares
 *
 * @author Ziawasch Abedjan
 */
public class LevenshteinExperimentCD {

    /**
     * Runs the {@link SortedNeighborhoodMethod} on a huge data set.
     *
     * @param args Won't be considered.
     * @throws Exception If any exception occurs.
     */
    public static void main(String[] args) throws Exception {

        // enables dynamic data-loading for file-based sorting
        
        GlobalConfig.getInstance().setInMemoryObjectThreshold(10000);

        // sets the CSV data source
        CSVSource dataSource = new CSVSource("cd", new File("cd.csv"));
        dataSource.enableHeader();
                // instantiates the CSV data source for reading the goldstandard
        // "goldstandard" is the goldstandard identifier
        CSVSource goldstandardSource = new CSVSource("goldstandard", new File("cd_gold.csv"));
        goldstandardSource.enableHeader();
        dataSource.addIdAttributes("pk");
        

		// instantiate the gold standard
        // "cddb" is the source identifier
       /** GoldStandard goldStandard = new GoldStandard(goldstandardSource);
        goldStandard.setFirstElementsObjectIdAttributes("disc1_id");
        goldStandard.setSecondElementsObjectIdAttributes("disc2_id");
        goldStandard.setSourceIdLiteral("cddb");**/

        // defines sub-keys that are used to generate the sorting key
        TextBasedSubkey artistSubkey = new TextBasedSubkey("artist");
        artistSubkey.setIgnoredCharactersRegEx(TextBasedSubkey.NO_VOWELS_REGEX);

        DocumentFrequencyPreprocessor dfPreprocessor = new DocumentFrequencyPreprocessor("artist");
        // the key generator uses sub-key selectors to generate a key for each object
        SortingKey sortingKey = new SortingKey();
        sortingKey.addSubkey(artistSubkey);

        Algorithm algorithm = new SortedNeighborhoodMethod(sortingKey, 30);
        algorithm.enableInMemoryProcessing();
        algorithm.addPreprocessor(dfPreprocessor);

        // enable in-memory storing
        algorithm.enableInMemoryProcessing();

        // adds the "data" to the algorithm
        algorithm.addDataSource(dataSource);

		// instantiates similarity measure
        //SimilarityFunction similarityFunction = new TFIDFSimilarityFunction(dfPreprocessor, "title");
        SimilarityFunction similarityFunction = new LevenshteinDistanceFunction("artist");
      //  StatisticComponent statistic = new StatisticComponent(goldStandard, algorithm);

        long start = System.currentTimeMillis();

        // counts the generated object pairs
        int cnt = 0;
        int dupCnt = 0;
        int nondupCnt = 0;
        //int cluster = 0;

        for (DuDeObjectPair pair : algorithm) {
            if (similarityFunction.getSimilarity(pair) > 0.8) {
                ++dupCnt;
                //statistic.addDuplicate(pair);

            } else {
                ++nondupCnt;
               // statistic.addNonDuplicate(pair);

            }
            ++cnt;
        }
// Write statistics
       // StatisticOutput statisticOutput = new SimpleStatisticOutput(System.out, statistic);
//        statisticOutput.writeStatistics();
        System.out.println("Experiment finished.");
        algorithm.cleanUp();

        // print statistics
        System.out.println();
        System.out.println();
        System.out.println(dupCnt + " duplicates out of " + cnt + " pairs detected in " + (System.currentTimeMillis() - start) + " ms");

    }

}
