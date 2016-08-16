/*
 * DuDe - The Duplicate Detection Toolkit
 * 
 * Copyright (C) 2010  Hasso-Plattner-Institut f?r Softwaresystemtechnik GmbH,
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
package experimentos.Cora;

import Index.BlockIndex;
import java.io.File;
import java.io.IOException;

import org.xml.sax.SAXException;

import de.hpi.fgis.dude.algorithm.Algorithm;
import de.hpi.fgis.dude.algorithm.duplicatedetection.NaiveDuplicateDetection;
import de.hpi.fgis.dude.algorithm.duplicatedetection.SortedNeighborhoodMethod;
import de.hpi.fgis.dude.datasource.CSVSource;
import de.hpi.fgis.dude.datasource.DataSource;
import de.hpi.fgis.dude.datasource.XMLSource;
import de.hpi.fgis.dude.output.DuDeOutput;
import de.hpi.fgis.dude.output.JsonOutput;
import de.hpi.fgis.dude.output.statisticoutput.SimpleStatisticOutput;
import de.hpi.fgis.dude.output.statisticoutput.StatisticOutput;
import de.hpi.fgis.dude.postprocessor.StatisticComponent;
import de.hpi.fgis.dude.preprocessor.DocumentFrequencyPreprocessor;
import de.hpi.fgis.dude.similarityfunction.contentbased.calculationstrategy.ArrayConversionStrategy;
import de.hpi.fgis.dude.similarityfunction.contentbased.calculationstrategy.AverageArrayArrayStrategy;
import de.hpi.fgis.dude.similarityfunction.contentbased.impl.simmetrics.LevenshteinDistanceFunction;
import de.hpi.fgis.dude.util.GlobalConfig;
import de.hpi.fgis.dude.util.GoldStandard;
import de.hpi.fgis.dude.util.data.DuDeObjectPair;
import de.hpi.fgis.dude.util.sorting.sortingkey.SortingKey;
import de.hpi.fgis.dude.util.sorting.sortingkey.TextBasedSubkey;
import graph.Vertice;
import org.apache.commons.codec.language.DoubleMetaphone;
import query.QueryExperimento;

/**
 * This execution class runs the naive duplicate detection algorithm on the
 * <code>CORA</code> data source. Two records are similar if their titles match
 * based on a relative Levenshtein distance of 0.8.
 *
 * @author Matthias Pohl
 */
public class CoraExec2 {

    /**
     * Executes the naive duplicate detection on the <code>CORA</code> data
     * source. During the process all duplicates will be written onto the
     * console.
     *
     * @param args No arguments will be processed.
     * @throws SAXException If an error would occur during the XML parsing
     * process. This exception should not be thrown since the CORA.xml can be
     * parsed without any problems.
     * @throws IOException If an error occurs while reading from the file.
     */
    public static void main(String[] args) throws IOException, SAXException {
		
        GlobalConfig.getInstance().setInMemoryObjectThreshold(1000);

        // sets the CSV data source
        CSVSource dataSource = new CSVSource("cora", new File("coraCSV.csv"));
        dataSource.enableHeader();
        dataSource.addIdAttributes("id");
        
        
        CSVSource goldstandardSource = new CSVSource("goldstandard", new File("cora_goldCSV.csv"));
        goldstandardSource.enableHeader();
        GoldStandard goldStandard = new GoldStandard(goldstandardSource);
        goldStandard.setFirstElementsObjectIdAttributes("id1");
        goldStandard.setSecondElementsObjectIdAttributes("id2");
        goldStandard.setSourceIdLiteral("cora");
      
         

        


        Algorithm algorithm = new NaiveDuplicateDetection();
        algorithm.enableInMemoryProcessing();      

        algorithm.addDataSource(dataSource);

		// instantiates the similarity function
        // checks the Levenshtein distance of the papers' titles
        LevenshteinDistanceFunction similarityFunction = new LevenshteinDistanceFunction("titulo");
     

        // writes the pairs onto the console by using the Json syntax
        DuDeOutput output = new JsonOutput(System.out);

		// instantiate statistic component to calculate key figures
        // like runtime, number of comparisons, precision and recall
        StatisticComponent statistic = new StatisticComponent(goldStandard, algorithm);


        
        statistic.setStartTime();
       // long tempoI = System.currentTimeMillis();
        for (DuDeObjectPair pair : algorithm) {
            double similarity;
            try {
                similarity = similarityFunction.getSimilarity(pair);
            } catch (IllegalArgumentException e) {
				// ignore invalid values
                //System.out.println("Values will be ignored: " + e.getMessage());
                continue;
            }

            if (similarity > 0.5) {
				// if it is a duplicate - print it and add it to the
                // statistic component as duplicate
             //output.write(pair);
             //System.out.println("");

                //System.out.println();
                statistic.addDuplicate(pair);

              

            } else {
		//output.write(pair);
               // System.out.println("");
                statistic.addNonDuplicate(pair);
            }
        }

        statistic.setEndTime();

        // Write statistics
        StatisticOutput statisticOutput = new SimpleStatisticOutput(System.out, statistic);
        statisticOutput.writeStatistics();
        System.out.println("Experiment finished.");
        dataSource.close();
        goldStandard.close();
    }

}
