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
 * 
 */

package experimentos;

import Index.BlockIndex;
import java.io.File;
import java.io.IOException;



import de.hpi.fgis.dude.algorithm.Algorithm;
import de.hpi.fgis.dude.algorithm.duplicatedetection.NaiveDuplicateDetection;
import de.hpi.fgis.dude.datasource.CSVSource;
import de.hpi.fgis.dude.output.DuDeOutput;
import de.hpi.fgis.dude.output.JsonOutput;
import de.hpi.fgis.dude.output.statisticoutput.SimpleStatisticOutput;
import de.hpi.fgis.dude.output.statisticoutput.StatisticOutput;
import de.hpi.fgis.dude.postprocessor.StatisticComponent;
import de.hpi.fgis.dude.similarityfunction.contentbased.impl.simmetrics.LevenshteinDistanceFunction;
import de.hpi.fgis.dude.util.GlobalConfig;
import de.hpi.fgis.dude.util.GoldStandard;
import de.hpi.fgis.dude.util.data.DuDeObjectPair;
import de.hpi.fgis.dude.util.sorting.sortingkey.TextBasedSubkey;
import graph.Vertice;
import net.sf.javaml.clustering.Clusterer;
import net.sf.javaml.clustering.KMeans;
import net.sf.javaml.clustering.KMedoids;
import net.sf.javaml.core.Dataset;
import net.sf.javaml.core.DefaultDataset;
import net.sf.javaml.core.Instance;
import net.sf.javaml.core.SparseInstance;
import net.sf.javaml.distance.DistanceMeasure;
import net.sf.javaml.distance.EuclideanDistance;
import net.sf.javaml.tools.DatasetTools;
import net.sf.javaml.tools.data.FileHandler;
import net.sf.javaml.tools.weka.WekaClusterer;

import org.apache.commons.codec.language.DoubleMetaphone;
import weka.clusterers.XMeans;

/**
 * This execution class runs the naive duplicate detection algorithm on the <code>CD</code> data source. Two records are similar if their titles match
 * based on the Levenshtein distance.
 * 
 * @author Matthias Pohl, Uwe Draisbach
 */
public class CDBlocagem {

	/**
	 * Executes the naive duplicate detection on the <code>CD</code> data source. During the process all duplicates will be written onto the console.
	 * 
	 * @param args
	 *            No arguments will be processed.
	 * @throws IOException
	 *             If an error occurs while reading from the file.
	 */
	public static void main(String[] args) throws IOException {
            EuclideanDistance ec = new EuclideanDistance();
            
           
            //KMedoids km = new KMedoids(50, 100, ec);
           
		// enables dynamic data-loading for file-based sorting
		//GlobalConfig.getInstance().setInMemoryObjectThreshold(10000);

		// instantiates the CSV data source for reading records
		// "cddb" is the source identifier
		//CSVSource dataSource = new CSVSource("cddb", new File("cd.csv"));
		//dataSource.enableHeader();
                Dataset data = FileHandler.loadDataset(new File("cd1.csv"));
               System.err.println(data.size());
               System.err.println(data.get(5).keySet().first());
                
                System.err.println(" testeeeeeeee  1   ");
                XMeans cl = new XMeans();
                Clusterer cl2 = new WekaClusterer(cl);
               //Clusterer cl = new KMeans(10, 1,ec);
               System.err.println(" testeeeeeeee 2    ");
               Dataset [] dt = cl2.cluster(data);
               System.err.println(" testeeeeeeee     "+ dt.length);
              System.err.println(data);
              FileHandler.exportDataset(data,new File("output.txt"));
                 

		// uses the id attribute for the object id - this call is optional, if no id attribute is set, DuDe will generate its own object ids
		//dataSource.addIdAttributes("pk");
                
               // TextBasedSubkey artistSubkey = new TextBasedSubkey("artist");
		//artistSubkey.setIgnoredCharactersRegEx(TextBasedSubkey.NO_VOWELS_REGEX);
		//artistSubkey.setRange(2);
		

		// instantiates the CSV data source for reading the goldstandard
		// "goldstandard" is the goldstandard identifier
		//CSVSource goldstandardSource = new CSVSource("goldstandard", new File("cd_gold.csv"));
		//goldstandardSource.enableHeader();

		// instantiate the gold standard
		// "cddb" is the source identifier
		//GoldStandard goldStandard = new GoldStandard(goldstandardSource);
		//goldStandard.setFirstElementsObjectIdAttributes("disc1_id");
		//goldStandard.setSecondElementsObjectIdAttributes("disc2_id");
		//goldStandard.setSourceIdLiteral("cddb");

		// instantiates the naive duplicate detection algorithm
		//Algorithm algorithm = new NaiveDuplicateDetection();
		//algorithm.enableInMemoryProcessing();

		// adds the "data" to the algorithm
		//algorithm.addDataSource(dataSource);

		// instantiates the similarity function
		// checks the Levenshtein distance of the CD titles
		//LevenshteinDistanceFunction similarityFunction = new LevenshteinDistanceFunction("title");

		// writes the duplicate pairs onto the console by using the Json syntax
		//DuDeOutput output = new JsonOutput(System.out);

		// instantiate statistic component to calculate key figures
		// like runtime, number of comparisons, precision and recall
		//StatisticComponent statistic = new StatisticComponent(goldStandard, algorithm);

		// the actual computation starts
		// the algorithm returns each generated pair step-by-step
		//long tempoInicioProcesso = System.currentTimeMillis();
               // long tempoASerDecontado = 0;
		//for (DuDeObjectPair pair : algorithm) {
			/**final double similarity = similarityFunction.getSimilarity(pair);
			if (similarity > 0.9) {
				// if it is a duplicate - print it and add it to the
				// statistic component as duplicate
                            long tempoInicio = System.currentTimeMillis();
                           
                            DoubleMetaphone dbm = new DoubleMetaphone();
                   String keyBlock1 = dbm.encode(pair.getFirstElement().getAttributeValues("artist").toString());
                   String keyBlock2 = dbm.encode(pair.getSecondElement().getAttributeValues("artist").toString());
                   String id1 = dbm.encode(pair.getFirstElement().getAttributeValues("pk").toString());
                   String id2 = dbm.encode(pair.getSecondElement().getAttributeValues("pk").toString());

                   Vertice v1 = new Vertice(id1, "Base 1", 0);
                   Vertice v2 = new Vertice(id2, "Base 1", 0);
                   BlockIndex BI = new BlockIndex();
                          BI .insertVertice(keyBlock1, v1);
                          BI .insertVertice(keyBlock2, v1);
                           tempoASerDecontado = tempoASerDecontado + (System.currentTimeMillis()-tempoInicio);
                            
				//output.write(pair);
                            System.err.println(  pair.getSecondElement().getAttributeValues("pk").toString());
                                
				System.out.println();
				statistic.addDuplicate(pair);
			} else {
				// if it is not a duplicate, add it to the statistic
				// component as non-duplicate
				statistic.addNonDuplicate(pair);
			}
		}
                System.err.println(tempoASerDecontado + " Subtrair");
		//statistic.setEndTime();
                System.err.println(" Tempo total processamento " + (System.currentTimeMillis()-tempoInicioProcesso)   );
                
                

		// Write statistics
		StatisticOutput statisticOutput = new SimpleStatisticOutput(System.out, statistic);
		statisticOutput.writeStatistics();
		System.out.println("Experiment finished.");
                
                

		// clean up
		dataSource.cleanUp();
		goldStandard.close();
	}**/
        }

}
