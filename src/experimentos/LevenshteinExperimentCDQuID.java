/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package experimentos;

import Index.BlockIndex;
import de.hpi.fgis.dude.algorithm.Algorithm;
import de.hpi.fgis.dude.algorithm.duplicatedetection.SortedNeighborhoodMethod;
import de.hpi.fgis.dude.datasource.CSVSource;
import de.hpi.fgis.dude.output.CSVOutput;
import de.hpi.fgis.dude.output.DuDeOutput;

import de.hpi.fgis.dude.preprocessor.DocumentFrequencyPreprocessor;
import de.hpi.fgis.dude.similarityfunction.SimilarityFunction;
import de.hpi.fgis.dude.similarityfunction.contentbased.impl.simmetrics.LevenshteinDistanceFunction;
import de.hpi.fgis.dude.util.GlobalConfig;

import de.hpi.fgis.dude.util.data.DuDeObjectPair;
import de.hpi.fgis.dude.util.sorting.sortingkey.SortingKey;
import de.hpi.fgis.dude.util.sorting.sortingkey.TextBasedSubkey;
import graph.Vertice;
import java.io.File;
import org.apache.commons.codec.language.DoubleMetaphone;
import query.QueryExperimento;

/**
 *
 * @author Priscilla
 */
public class LevenshteinExperimentCDQuID {

    public static void main(String[] args) throws Exception {

        // enables dynamic data-loading for file-based sorting
        GlobalConfig.getInstance().setInMemoryObjectThreshold(10000);

        // sets the CSV data source
        CSVSource dataSource = new CSVSource("cd", new File("cd.csv"));
        dataSource.enableHeader();
        dataSource.addIdAttributes("pk");
        
     //   CSVSource goldstandardSource = new CSVSource("goldstandard", new File("cd_gold.csv"));
	//	goldstandardSource.enableHeader();

		// instantiate the gold standard
		// "cddb" is the source identifier
		//GoldStandard goldStandard = new GoldStandard(goldstandardSource);
		//goldStandard.setFirstElementsObjectIdAttributes("disc1_id");
		//goldStandard.setSecondElementsObjectIdAttributes("disc2_id");
		//goldStandard.setSourceIdLiteral("cddb");
                
        // defines sub-keys that are used to generate the sorting key
        TextBasedSubkey artistSubkey = new TextBasedSubkey("artist");
        artistSubkey.setIgnoredCharactersRegEx(TextBasedSubkey.NO_VOWELS_REGEX);

        DocumentFrequencyPreprocessor dfPreprocessor = new DocumentFrequencyPreprocessor("artist");
        // the key generator uses sub-key selectors to generate a key for each object

        SortingKey sortingKey = new SortingKey();
        sortingKey.addSubkey(artistSubkey);

        Algorithm algorithm = new SortedNeighborhoodMethod(sortingKey, 30);
        algorithm.addPreprocessor(dfPreprocessor);

        // enable in-memory storing
        algorithm.enableInMemoryProcessing();

        // adds the "data" to the algorithm
        algorithm.addDataSource(dataSource);

        // instantiates similarity measure
        //SimilarityFunction similarityFunction = new TFIDFSimilarityFunction(dfPreprocessor, "title");
        SimilarityFunction similarityFunction = new LevenshteinDistanceFunction("artist");
       // DuDeOutput output = new CSVOutput(new File("saida.csv"));

        long start = System.currentTimeMillis();

        // counts the generated object pairs
        int cnt = 0;
       
        int dupCnt = 0;
        int nondupCnt = 0;
       
        //Map<String, ArrayList<String>> mapaSimilares = new HashMap<String, ArrayList<String>>();

        BlockIndex bi = new BlockIndex();
         DoubleMetaphone db = new DoubleMetaphone();
       //  StatisticComponent statistic = new StatisticComponent(goldStandard, algorithm);
        for (DuDeObjectPair pair : algorithm) {
            if (similarityFunction.getSimilarity(pair) > 0.8) {
                ++dupCnt;
                String pk1 = pair.getFirstElement().getAttributeValue("pk").toString();
                String pk2 = pair.getSecondElement().getAttributeValue("pk").toString();

                String title1 = pair.getFirstElement().getAttributeValue("artist").toString();
                String title2 = pair.getSecondElement().getAttributeValue("artist").toString();

               
                String keyBlockpair1 = db.encode(title1);
                String keyBlockpair2 = db.encode(title2);

                //int cluster1 = bi.getclusterId(pk1, keyBlockpair1, "cd");
                //int cluster2 = bi.getclusterId(pk2, keyBlockpair2, "cd");
                 
                 Vertice v1 = new Vertice(pk1, "cd", 0);
                 Vertice v2 = new Vertice(pk2, "cd", 0);

                  bi.insertVertice(keyBlockpair1, v1);
                  bi.insertVertice(keyBlockpair2, v2);
                 // statistic.addDuplicate(pair);

                /**if ((cluster1 != -1) && (cluster2 == -1)) {
                    Vertice v2 = new Vertice(pk2, "cd", cluster1);
                    bi.insertVertice(keyBlockpair2, v2);
                    caso1++;

                }else if ((cluster1 == -1) && (cluster2 != -1)) {
                     Vertice v1 = new Vertice(pk1, "cd", cluster2);
                        bi.insertVertice(keyBlockpair1, v1);
                        caso2++;
                    
                }  else if ((cluster1 == -1) && (cluster2 == -1)) {
                     Vertice v1 = new Vertice(pk1, "cd", cluster);
                        Vertice v2 = new Vertice(pk2, "cd", cluster);

                        bi.insertVertice(keyBlockpair1, v1);
                        bi.insertVertice(keyBlockpair2, v2);
                       cluster++;
                       caso3++;
                }**/
               
                

                // System.err.println(  pair.getFirstElement().getAttributeValue("title").toString());
            } else {
                ++nondupCnt;
              //  statistic.addNonDuplicate(pair);
            }
            ++cnt;
        }
       //bi.printBlockIndex();
       // System.err.println(" numero total de elementor " + bi.getNumeroElementos());
//StatisticOutput statisticOutput = new SimpleStatisticOutput(System.out, statistic);
//		statisticOutput.writeStatistics();
        algorithm.cleanUp();

        // print statistics
      //  System.out.println();
       // System.out.println();

        System.err.println(dupCnt + " duplicates out of " + cnt + " pairs detected in " + (System.currentTimeMillis() - start) + " ms  " + bi.getNumeroElementos() + "  " + bi.getNumeroBlocos());
        //System.err.println(" casos  " + caso1 + " " + caso2 + " " + caso3 + " ");
        QueryExperimento query = new QueryExperimento(bi);
        query.query();

    }

}
