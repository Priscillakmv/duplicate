/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package query;

import Index.BlockIndex;
import avaliacao.Precisao;
import de.hpi.fgis.dude.datasource.CSVSource;
import de.hpi.fgis.dude.postprocessor.StatisticComponent;
import de.hpi.fgis.dude.similarityfunction.contentbased.util.SoundEx;
import de.hpi.fgis.dude.util.GlobalConfig;
import de.hpi.fgis.dude.util.GoldStandard;
import de.hpi.fgis.dude.util.data.DuDeObject;
import de.hpi.fgis.dude.util.data.DuDeObjectPair;
import graph.Aresta;
import graph.Vertice;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;
import org.apache.commons.codec.language.DoubleMetaphone;

/**
 *
 * @author Priscilla
 */
public class Amostra {

    private Random gerador;
    private HashSet<Integer> aux;
     private HashSet<Integer> auxEspelho;
    GoldStandard goldStandard;
    StatisticComponent st;
    int certo = 0;
    int errado = 0;
   private   ArrayList<Vertice> dadosEntrada ;

    public HashSet<Integer> getAux() {
        return aux;
    }

    public void setAux(HashSet<Integer> aux) {
        this.aux = aux;
    }

    public HashSet<Integer> getAuxEspelho() {
        return auxEspelho;
    }

    public void setAuxEspelho(HashSet<Integer> auxEspelho) {
        this.auxEspelho = auxEspelho;
    }


    public Amostra() {
        this.gerador = new Random();
        this.aux = new HashSet<Integer>();
        this.dadosEntrada =  new ArrayList<Vertice>();
         this.auxEspelho = new HashSet<Integer>();
      //  this.st = new StatisticComponent

    }

    public CSVSource carregaDadosCD() throws FileNotFoundException {
        GlobalConfig.getInstance().setInMemoryObjectThreshold(1000);

        // sets the CSV data source
        CSVSource dataSource = new CSVSource("cd", new File("cdcd.csv"));
        dataSource.enableHeader();
        dataSource.addIdAttributes("pk");

        return dataSource;

    }
     public CSVSource carregaDadosCora() throws FileNotFoundException {
        GlobalConfig.getInstance().setInMemoryObjectThreshold(1000);

        // sets the CSV data source
         CSVSource dataSource = new CSVSource("cora", new File("coraCSV.csv"));
        dataSource.enableHeader();
        dataSource.addIdAttributes("id");

        return dataSource;

    }
    public CSVSource carregaDadosFebrl() throws FileNotFoundException {
        GlobalConfig.getInstance().setInMemoryObjectThreshold(1000);

        // sets the CSV data source
        CSVSource dataSource = new CSVSource("febrl", new File("febrl.csv"));
        dataSource.enableHeader();
        dataSource.addIdAttributes("id");

        return dataSource;

    }
    
    
    public CSVSource carregaDadosFebrlEspelho() throws FileNotFoundException {
        GlobalConfig.getInstance().setInMemoryObjectThreshold(1000);

        // sets the CSV data source
        CSVSource dataSource = new CSVSource("teste", new File("gold_febrl_teste.csv"));
        dataSource.enableHeader();
        dataSource.addIdAttributes("id");

        return dataSource;

    }
    
    /**
     * 
     * @param tamanhoAmostra total de pares (porcental de duplicados / 2)
     * @param limiteIntevalo tamanho do espelho + 1
     * @return 
     */
        public HashSet<Integer> geraPosicaoApartirEspelho(int tamanhoAmostra, int limiteIntevalo) {

        boolean tamanhoNotOK = true;
        int count = 0;
        while (tamanhoNotOK) {
            int posicao = this.gerador.nextInt(limiteIntevalo);
            //  System.out.println("  SAida " + posicao);
            auxEspelho.add(posicao);
            count++;
            if (auxEspelho.size() >= tamanhoAmostra) {
                tamanhoNotOK = false;
            }

        }

        return auxEspelho;
    }

          public HashSet<Integer> geraPosicaoCora(int tamanhoAmostra, int limiteIntevalo) {

        boolean tamanhoNotOK = true;
       
        while (tamanhoNotOK) {
            int posicao = this.gerador.nextInt(limiteIntevalo);
            if (posicao>0){
                 aux.add(posicao);
           
            if (aux.size() >= tamanhoAmostra) {
                tamanhoNotOK = false;
            }
            }
            //  System.out.println("  SAida " + posicao);
           

        }

      //  System.out.println(" Count  " + count);
        //Iterator<Integer> iterator = aux.iterator();
        //while (iterator.hasNext()) { 
        //  System.out.print(iterator.next() + " ");
        // }

        return aux;
    }
        
    public HashSet<Integer> geraPosicao(int tamanhoAmostra, int limiteIntevalo) {

        boolean tamanhoNotOK = true;
        int count = 0;
        while (tamanhoNotOK) {
            int posicao = this.gerador.nextInt(limiteIntevalo);
            //  System.out.println("  SAida " + posicao);
            aux.add(posicao);
            count++;
            if (aux.size() >= tamanhoAmostra) {
                tamanhoNotOK = false;
            }

        }

      //  System.out.println(" Count  " + count);
        //Iterator<Integer> iterator = aux.iterator();
        //while (iterator.hasNext()) { 
        //  System.out.print(iterator.next() + " ");
        // }

        return aux;
    }

    public boolean encontraKeySelecionada(String key) {
        boolean encontrou = false;
        Iterator<Integer> iterator = aux.iterator();
        while (iterator.hasNext()) {
            if (key.equalsIgnoreCase(iterator.next().toString())) {
                encontrou = true;
            }
        }
        return encontrou;
    }
      /**
       * espelho
       * @param key
       * @return
       * @throws FileNotFoundException 
       */
    public boolean encontraKeySelecionada2(String key)  {
          
        boolean encontrou = false;
        Iterator<Integer> iterator = auxEspelho.iterator();
        while (iterator.hasNext()) {
            if (key.equalsIgnoreCase(iterator.next().toString())) {
                encontrou = true;
            }
        }
        return encontrou;
    }

    public int calculaDuplicadosDaEntradaConsulta() throws IOException {

        Precisao precisao = new Precisao();
        precisao.InicializaGoldStandardCD();
      
        int totalDuplicados = 0;
        for (int i = 0; i < this.dadosEntrada.size(); i++) {
            Vertice get = this.dadosEntrada.get(i);
            for (int j = i + 1; j < this.dadosEntrada.size(); j++) {
                Vertice get1 = this.dadosEntrada.get(j);
                DuDeObject dude1 = new DuDeObject(get.getSourceId(), get.getId());
                DuDeObject dude2 = new DuDeObject(get1.getSourceId(), get1.getId());
                DuDeObjectPair dudePair = new DuDeObjectPair(dude1, dude2);
                if (precisao.getSt().isDuplicate(dudePair)) {
                    totalDuplicados++;
                    
                }
               
            }

        }
        System.out.println(" Total Duplicados  " + totalDuplicados);
        return totalDuplicados;

    }
        public int calculaDuplicadosDaEntradaConsultaCora() throws IOException {

        Precisao precisao = new Precisao();
        precisao.InicializaGoldStandardCora();
      
        int totalDuplicados = 0;
        for (int i = 0; i < this.dadosEntrada.size(); i++) {
            Vertice get = this.dadosEntrada.get(i);
            for (int j = i + 1; j < this.dadosEntrada.size(); j++) {
                Vertice get1 = this.dadosEntrada.get(j);
                DuDeObject dude1 = new DuDeObject(get.getSourceId(), get.getId());
                DuDeObject dude2 = new DuDeObject(get1.getSourceId(), get1.getId());
                DuDeObjectPair dudePair = new DuDeObjectPair(dude1, dude2);
                if (precisao.getSt().isDuplicate(dudePair)) {
                    totalDuplicados++;
                    
                }
               
            }

        }
         if (totalDuplicados>0) {
                    System.err.println(" Total Duplicados  " + totalDuplicados);
 
         }
      //  System.out.println(" Total Duplicados  " + totalDuplicados);
        return totalDuplicados;

    }
    
     public int calculaDuplicadosDaEntradaConsultaFebrl() throws IOException {

        Precisao precisao = new Precisao();
        precisao.InicializaGoldStandardFebrl();
      
        int totalDuplicados = 0;
        for (int i = 0; i < this.dadosEntrada.size(); i++) {
            Vertice get = this.dadosEntrada.get(i);
            for (int j = i + 1; j < this.dadosEntrada.size(); j++) {
                Vertice get1 = this.dadosEntrada.get(j);
                DuDeObject dude1 = new DuDeObject(get.getSourceId(), get.getId());
                DuDeObject dude2 = new DuDeObject(get1.getSourceId(), get1.getId());
                DuDeObjectPair dudePair = new DuDeObjectPair(dude1, dude2);
                if (precisao.getSt().isDuplicate(dudePair)) {
                    totalDuplicados++;
                    
                }
               
            }

        }
         if (totalDuplicados>0) {
                    System.err.println(" Total Duplicados  " + totalDuplicados);
 
         }
      //  System.out.println(" Total Duplicados  " + totalDuplicados);
        return totalDuplicados;

    }
    
         public BlockIndex blocaDadosDaAmostraConsultaCora(CSVSource dataSource) {

        BlockIndex bi2 = new BlockIndex();
        //SoundEx db = new SoundEx();

       DoubleMetaphone db = new DoubleMetaphone();
        for (Iterator<DuDeObject> iterator = dataSource.iterator(); iterator.hasNext();) {
            DuDeObject next = iterator.next();
            if (encontraKeySelecionada(next.getAttributeValue("id").toString())) {
                String pk = next.getAttributeValue("id").toString();
                String block = next.getAttributeValue("titulo").toString();
                String block2 = next.getAttributeValue("titulo").toString();
                String block3 = next.getAttributeValue("autor").toString();

                //String keyBlock = block;
                String keyBlock = db.doubleMetaphone(block2);
                Vertice v1 = new Vertice(pk, "cora", -1, block3, block2);
                bi2.insertVertice(keyBlock, v1);
                this.dadosEntrada.add(v1);
                //   System.out.println(" Pegou Id   " + next.getAttributeValue("title").toString() );
            }
        }

        return bi2;

    }
     
    public BlockIndex blocaDadosDaAmostraConsultaFebrl(CSVSource dataSource) {

        BlockIndex bi2 = new BlockIndex();
        //SoundEx db = new SoundEx();

       // DoubleMetaphone db = new DoubleMetaphone();
        for (Iterator<DuDeObject> iterator = dataSource.iterator(); iterator.hasNext();) {
            DuDeObject next = iterator.next();
            if (encontraKeySelecionada(next.getAttributeValue("id").toString())) {
                String pk = next.getAttributeValue("id").toString();
                String block = next.getAttributeValue("blocking").toString();
                String block2 = next.getAttributeValue("surname").toString();
                String block3 = next.getAttributeValue("given_name").toString();

                String keyBlock = block;
                //String keyBlock = db.getSoundEx(block2);
                Vertice v1 = new Vertice(pk, "febrl", -1, block3, block2);
                bi2.insertVertice(keyBlock, v1);
                this.dadosEntrada.add(v1);
                //   System.out.println(" Pegou Id   " + next.getAttributeValue("title").toString() );
            }
        }

        return bi2;

    }
   /**
    * inserindo os duplicados do espelho
    * @param dataSourceEspelho duplicados do espel
    * @return 
    */
    public void insereDuplicadosIdNaAmostra( CSVSource dataSourceEspelho) {

        BlockIndex bi2 = new BlockIndex();
        //SoundEx db = new SoundEx();

       // DoubleMetaphone db = new DoubleMetaphone();
        for (Iterator<DuDeObject> iterator = dataSourceEspelho.iterator(); iterator.hasNext();) {
            DuDeObject next = iterator.next();
            if (encontraKeySelecionada2(next.getAttributeValue("id").toString())) {
                //String id = next.getAttributeValue("id").toString();
                String id1 = next.getAttributeValue("febrl1_id").toString();
                String id2 = next.getAttributeValue("febrl2_id").toString();
                aux.add(Integer.valueOf(id1));
                aux.add(Integer.valueOf(id2));
               
            }
        }

       

    }

    public BlockIndex blocaDadosDaAmostraConsulta(CSVSource dataSource) {

        BlockIndex bi2 = new BlockIndex();
        //SoundEx db = new SoundEx();

        DoubleMetaphone db = new DoubleMetaphone();
        for (Iterator<DuDeObject> iterator = dataSource.iterator(); iterator.hasNext();) {
            DuDeObject next = iterator.next();
            if (encontraKeySelecionada(next.getAttributeValue("key").toString())) {
                String pk = next.getAttributeValue("pk").toString();
                String block = next.getAttributeValue("title").toString();
                String block2 = next.getAttributeValue("artist").toString();

                String keyBlock = db.encode(block);
                //String keyBlock = db.getSoundEx(block2);
                Vertice v1 = new Vertice(pk, "cd", -1, block, block2);
                bi2.insertVertice(keyBlock, v1);
                this.dadosEntrada.add(v1);
                //   System.out.println(" Pegou Id   " + next.getAttributeValue("title").toString() );
            }
        }

        return bi2;

    }

   
      public static void main(String[] args) throws IOException {
     
     
     // Amostra a = new Amostra(); a.geraPosicao(1000, 9759); 
     // BlockIndex bi = a.blocaDadosDaAmostraConsulta(a.carregaDadosCD()); System.out.println("Tamanho " + bi.getNumeroBlocos());
          Map<Vertice,  ArrayList<Aresta>> mapaArestas = new HashMap<Vertice, ArrayList<Aresta>>();
           Map<Integer,  Map <Vertice, ArrayList<Aresta>>> mapaArestas2 = new HashMap<Integer,  Map <Vertice, ArrayList<Aresta>>>();
          ArrayList<Aresta> a = new ArrayList<Aresta>();
             ArrayList<Aresta> b = new ArrayList<Aresta>();
             Vertice v1 =  new Vertice("1", "teste", -1);
             Vertice v2 =  new Vertice("3", "teste", -1);
             Vertice v3 =  new Vertice("5", "teste", -1);
             Vertice v4 =  new Vertice("0", "teste", -1);
             Aresta a1 = new Aresta(v1, v2, 2, "teste");
             Aresta a2 = new Aresta(v4, v3, 5, "teste");
             Aresta a3 = new Aresta(v3, v4, 8, "teste");
              Aresta b1 = new Aresta(v1, v2, 4, "teste");
             Aresta b2 = new Aresta(v1, v3, 6, "teste");
             Aresta b3 = new Aresta(v1, v4, 0, "teste");
          a.add(a1);
          a.add(a2);
          a.add(a3);
            b.add(b1);
        b.add(b2);
          b.add(b3);
          mapaArestas.put(v2, a);
          mapaArestas.put(v1, b);
          mapaArestas2.put(0, mapaArestas);
          mapaArestas2.put(1, mapaArestas);
        
        /**  for (Map.Entry<String, ArrayList<Aresta>> entrySet : mapaArestas.entrySet()) {
              String key = entrySet.getKey();
                Collections.sort (mapaArestas.get(key), new Comparator() {
            public int compare(Object o1, Object o2) {
                Aresta p1 = (Aresta) o1;
                Aresta p2 = (Aresta) o2;
                return Integer.valueOf(p1.getOrigem().getId())< Integer.valueOf(p2.getOrigem().getId()) ? -1 : (Integer.valueOf(p1.getOrigem().getId()) > Integer.valueOf(p2.getOrigem().getId()) ? +1 : 0);
            }
        });
             
              
          }**/
          
             //Set <Integer> key = (HashSet <Integer>) (Set <Integer>) mapaArestas.keySet();
              ArrayList <Vertice> values = new ArrayList<Vertice>(mapaArestas2.get(0).keySet());
              //Map<Integer, Aresta> treeMap = new TreeMap<Integer, Aresta>(mapaArestas);

              
                Collections.sort (values, new Comparator() {
            public int compare(Object o1, Object o2) {
                Vertice p1 = (Vertice) o1;
                Vertice p2 = (Vertice) o2;
                return Integer.valueOf(p1.getId())< Integer.valueOf(p2.getId()) ? -1 : Integer.valueOf(p1.getId()) > Integer.valueOf(p2.getId()) ? +1 : 0;
            }
        });
                  for (int i = 0; i < mapaArestas2.get(0).keySet().size(); i++) {
              //Aresta get = a.get(i);
              System.out.println(" a " + mapaArestas.keySet().toString());
              
          }
             
        /**  double busca = 1;
           ArrayList <Integer> values2 = new ArrayList<Integer>(mapaArestas.keySet());
          int saida = Collections.binarySearch(values2, 0, new Comparator(){
              @Override
              public int compare(Object o1, Object o2) {
                Integer p1 = (Integer) o1;
                Integer p2 = (Integer) o2;
                return Integer.compare( p1, p2);
                       
      
                
              } 
                  
                
              
              
              
          });  **/ 
          //System.out.println(" saida : " + saida);
          //mapaArestas.keySet().remove(b);
          
           Iterator itr =  mapaArestas2.get(0).keySet().iterator();
           int pos=0;
        while(itr.hasNext()) {
            Object element = itr.next();
            if (pos==0) {
                itr.remove();
                break;
            }
            pos++;
           
           
        }
        /** for (Map.Entry<Integer, ArrayList<Aresta>> entrySet : mapaArestas.entrySet()) {
              Integer key = entrySet.getKey();
              ArrayList<Aresta> value = entrySet.getValue();
              System.out.println(" saida fical " + key );
              
          }
          System.out.println(" teste " + mapaArestas2.get(0).toString());
          
       /** double busca = 2;
          int saida = Collections.binarySearch(mapaArestas.get("a"), b1, new Comparator(){

                  

              @Override
              public int compare(Object o1, Object o2) {
                Aresta p1 = (Aresta) o1;
                Aresta p2 = (Aresta) o2;
                return Double.compare( (Double)p1.getSimilaridade(), (Double)p2.getSimilaridade());
                       
      
                
              } 
                  
                
              
              
              
          });**/
          
        //  System.out.println(" SAida: " +  saida);
          //System.out.println(mapaArestas.get("a").get(saida).getSimilaridade()); 
        
                  
          
        
         /** for (int i = 0; i < mapaArestas.get("a").size(); i++) {
              Aresta get = a.get(i);
                System.out.println(" a " + get.getSimilaridade());
              
          }
         
          System.out.println(mapaArestas.get("a").get(1).getSimilaridade());
          	
          	

   for (int i = 0; i < b.size(); i++) {
              Aresta get = b.get(i);
              System.out.println(" b " + get.getSimilaridade());
              
          }
          for (int i = 0; i < mapaArestas.get("b").size(); i++) {
              Aresta get = b.get(i);
                System.out.println(" b " + get.getSimilaridade());
              
          }
         
          System.out.println(mapaArestas.get("b").get(0).getSimilaridade());
         ArrayList<Aresta> f = mapaArestas.get("b");
         f.remove(0);
         
          System.out.println(" ****************");
          for (int i = 0; i < mapaArestas.get("b").size(); i++) {
              Aresta get = b.get(i);
                System.out.println(" b " + get.getSimilaridade());
              
          }
          mapaArestas.get("a").remove(a1);
           mapaArestas.get("a").remove(a2);
            mapaArestas.get("a").remove(a3);
          System.out.println(" tamanho excluido: " + mapaArestas.get("a").size());
                        System.out.println(" tamanho excluido 2: " + mapaArestas.get("a").size());
                        System.out.println(" tamanho excluido 2: " + mapaArestas.size());

         
         
      
         // System.out.println(" Saida: " + saida);

             
      
      }  **/
}
}
