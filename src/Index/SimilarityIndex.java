/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Index;

import graph.Aresta;
import graph.Vertice;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Priscilla
 */
public class SimilarityIndex {

    private Map<String, Map<Vertice, ArrayList<Aresta>>> mapaArestas;

    public SimilarityIndex() {
        this.mapaArestas = new HashMap<String, Map<Vertice, ArrayList<Aresta>>>();

    }

    public Map<String, Map<Vertice, ArrayList<Aresta>>> getMapaArestas() {
        return mapaArestas;
    }

    public Map<Vertice, ArrayList<Aresta>> getMapaPorBloco(String key) {

        return this.mapaArestas.get(key);

    }

    public ArrayList<Aresta> getArraySimilaridadePorVertice(String vertice, String key, String sourceId) {

        Vertice v = this.getVerticeOrigem(key, vertice, sourceId);
        return this.mapaArestas.get(key).get(v);

    }
    
    public int getTamanho() {
              
       int numeElementos=0;
        for (Map.Entry<String, Map<Vertice, ArrayList<Aresta>>> entrySet : this.mapaArestas.entrySet()) {
             Map<Vertice, ArrayList<Aresta>> aux = new HashMap<Vertice, ArrayList<Aresta>>();
       // aux = this.getMapaArestas().get(key);
             aux= entrySet.getValue();
             
             for (Map.Entry<Vertice, ArrayList<Aresta>> entrySet1 : aux.entrySet()) {
                
                 numeElementos = numeElementos +entrySet1.getValue().size();
                         
                
            }
            

        }
        
       
        
        
        return numeElementos;
    }


    public void setMapaArestas(Map<String, Map<Vertice, ArrayList<Aresta>>> mapaArestas) {
        this.mapaArestas = mapaArestas;
    }

    public Vertice getVerticeOrigem(String keyOrigem, String verticeOrigemId, String sourceIdOrigem) {

        ArrayList<Vertice> aux = new ArrayList<Vertice>();

        aux.addAll(this.mapaArestas.get(keyOrigem).keySet());

        for (int i = 0; i < aux.size(); i++) {
            if (aux.get(i).getSourceId().equalsIgnoreCase(sourceIdOrigem) && aux.get(i).getId().equalsIgnoreCase(verticeOrigemId)) {
                return aux.get(i);

            }

        }
        return null;

    }

    public boolean verificaBloco(String key) {
        return this.mapaArestas.containsKey(key);

    }

    public Aresta getAresta(String keyOrigem, Vertice vertice, String verticeDestinoId, String sourceIdDestino) {
        ArrayList<Aresta> aux = new ArrayList<Aresta>();
        aux = (ArrayList<Aresta>) this.mapaArestas.get(keyOrigem).get(vertice);
        for (int i = 0; i < aux.size(); i++) {
            if (aux.get(i).getDestino().getId().equalsIgnoreCase(verticeDestinoId) && aux.get(i).getDestino().getSourceId().equalsIgnoreCase(sourceIdDestino)) {
                return aux.get(i);

            }

        }
        return null;
    }

    public int insereSimilaridadeDestino(String keyOrigem, String verticeOrigemId, String sourceIdOrigem, String verticeDestinoId, String sourceIdDestino, String keyDestino, double similaridade) {

        Aresta aresta = criaSimilaridade(keyOrigem, verticeOrigemId, sourceIdOrigem, sourceIdDestino, verticeDestinoId, keyDestino, similaridade);

        //System.out.println(aresta.getDestino().getId() + " Destino");
        // System.out.println(verificaBloco(keyDestino));
        if (verificaBloco(keyOrigem)) {
            Vertice vertice = (Vertice) getVerticeOrigem(keyOrigem, verticeOrigemId, sourceIdOrigem);
            if (vertice != null) {
                insereSimilaridadeVerticeExistente(keyOrigem, aresta);
                return 0;

            } else {
                insereSimilaridadeVerticeInexistente(keyOrigem, aresta);
                return -2;

            }

        } else {
            insereSimilaridadeKeyInexistente(aresta, keyOrigem);
            return -2;

        }

    }

    public int verificaSimilaridade(double similaridade, String keyOrigem, String verticeOrigemId, String sourceIdOrigem, String verticeDestinoId, String sourceIdDestino, String keyDestino) {
        // float similaridade;
        Aresta arestaAux;
        if (this.mapaArestas.containsKey(keyOrigem)) {

            Vertice vertice = this.getVerticeOrigem(keyOrigem, verticeOrigemId, sourceIdOrigem);

            if (vertice != null) {
                Aresta aresta = getAresta(keyOrigem, vertice, verticeDestinoId, sourceIdDestino);
                if (aresta != null) {
                    // return aresta.getSimilaridade();
                    return 0; //ja possui o vertice origem e o destino

                } else {
                    // similaridade = -1; // falta passar os dados
                    arestaAux = criaSimilaridade(keyOrigem, verticeOrigemId, sourceIdOrigem, sourceIdDestino, verticeDestinoId, keyDestino, similaridade);
                    insereSimilaridadeVerticeExistente(keyOrigem, arestaAux);

                    // System.out.println(keyDestino + " " + keyDestino);
                    insereSimilaridadeDestino(keyDestino, verticeDestinoId, sourceIdDestino, verticeOrigemId, sourceIdOrigem, keyOrigem, similaridade);
                    //return similaridade;
                    return -2; //tem o vertice origem, mas n tem o destino, tem a chave de bloqueio
                }

            } else {
                // similaridade = -2;
                arestaAux = criaSimilaridade(keyOrigem, verticeOrigemId, sourceIdOrigem, sourceIdDestino, verticeDestinoId, keyDestino, similaridade);
                insereSimilaridadeVerticeInexistente(keyOrigem, arestaAux);
                int saida = -1 + insereSimilaridadeDestino(keyDestino, verticeDestinoId, sourceIdDestino, verticeOrigemId, sourceIdOrigem, keyOrigem, similaridade);
               // return similaridade;

                return saida; //se a saida for -1, n tem o primeiro, mas tem o segundo. Se a saída for -3, n tem os dois

                //vertice nao esta no indice. Insere no indice calcula similaridade com destino e armazena. FAz o mesmo com o detino
            }

        } else {
            //calcula similaridade
            // similaridade = -3;
            arestaAux = criaSimilaridade(keyOrigem, verticeOrigemId, sourceIdOrigem, sourceIdDestino, verticeDestinoId, keyDestino, similaridade);
            insereSimilaridadeKeyInexistente(arestaAux, keyOrigem);
            int saida = -1 + insereSimilaridadeDestino(keyDestino, verticeDestinoId, sourceIdDestino, verticeOrigemId, sourceIdOrigem, keyOrigem, similaridade);
            return saida; //se a saida for -1, n tem o primeiro, mas tem o segundo. Se a saída for -3, n tem os dois
            // return similaridade;
            //insere similaridade no destino
            //verificaSimilaridade(keyDestino, verticeDestinoId, sourceIdDestino, verticeOrigemId, sourceIdOrigem, keyDestino);

        }

    }

    public Aresta criaSimilaridade(String keyOrigem, String arestaOrigemId, String sourceIdOrigem, String sourceIdDestino, String arestaIdDestino, String keyDestino, double similaridade) {
        Vertice verticeOrigem = new Vertice(arestaOrigemId, sourceIdOrigem, -1);
        Vertice verticeDestino = new Vertice(arestaIdDestino, sourceIdDestino, -1);

        Aresta newAresta = new Aresta(verticeOrigem, verticeDestino, similaridade, keyDestino);
        return newAresta;

    }

    public void insereSimilaridadeVerticeExistente(String keyOrigem, Aresta aresta) {

        ArrayList<Vertice> aux = new ArrayList<Vertice>();
        aux.addAll(this.mapaArestas.get(keyOrigem).keySet());
        for (int i = 0; i < aux.size(); i++) {
            if (aux.get(i).getId() == aresta.getOrigem().getId() && aux.get(i).getSourceId() == aresta.getOrigem().getSourceId()) {
                this.mapaArestas.get(keyOrigem).get(aux.get(i)).add(aresta);
            }

        }

    }

    public void insereSimilaridadeVerticeInexistente(String keyOrigem, Aresta aresta) {
        ArrayList<Aresta> arestaAux = new ArrayList<Aresta>();
        arestaAux.add(aresta);
        Map<Vertice, ArrayList<Aresta>> mapaArestasAux = new HashMap<Vertice, ArrayList<Aresta>>();
        mapaArestasAux.put(aresta.getOrigem(), arestaAux);
        this.mapaArestas.get(keyOrigem).put(aresta.getOrigem(), arestaAux);

    }

    public void insereSimilaridadeKeyInexistente(Aresta aresta, String key) {
        ArrayList<Aresta> arestaAux = new ArrayList<Aresta>();
        arestaAux.add(aresta);

        Map<Vertice, ArrayList<Aresta>> mapaArestasAux = new HashMap<Vertice, ArrayList<Aresta>>();
        mapaArestasAux.put(aresta.getOrigem(), arestaAux);

        this.mapaArestas.put(key, mapaArestasAux);

    }

    public void printArestasPorKey(String key) {
        Map<Vertice, ArrayList<Aresta>> aux = new HashMap<Vertice, ArrayList<Aresta>>();
        aux = this.getMapaArestas().get(key);
        for (Map.Entry<Vertice, ArrayList<Aresta>> entrySet : aux.entrySet()) {
            Vertice key1 = entrySet.getKey();
            ArrayList<Aresta> value = entrySet.getValue();

            for (int i = 0; i < value.size(); i++) {
                Aresta get = value.get(i);
                System.out.println("Vertice Origem " + key1.getId() + get.getSimilaridade() + "  Similaridade " + get.getDestino().getId() + "  Vertice Destino");

            }

        }

    }

    public static void main(String[] args) {
        SimilarityIndex si = new SimilarityIndex();
        System.out.println(si.verificaSimilaridade(1, "peter", "1", "11", "2", "22", "peter") + " similaridade");
        System.out.println(si.verificaSimilaridade(1, "peter", "1", "11", "3", "33", "korn") + " similaridade");
        System.out.println(si.verificaSimilaridade(1, "korn", "4", "44", "5", "55", "korn") + " similaridade");
        System.out.println(si.verificaSimilaridade(1, "korn", "4", "44", "5", "55", "korn") + " similaridade");

        Map<Vertice, ArrayList<Aresta>> aux = new HashMap<Vertice, ArrayList<Aresta>>();
        aux = si.getMapaArestas().get("korn");
        for (Map.Entry<Vertice, ArrayList<Aresta>> entrySet : aux.entrySet()) {
            Vertice key = entrySet.getKey();
            ArrayList<Aresta> value = entrySet.getValue();
            System.out.print(key.getId() + "  vertice origem  ");

            for (int i = 0; i < value.size(); i++) {
                Aresta get = value.get(i);
                System.out.println(get.getDestino().getId() + "  Vertice DEstino");

            }

        }

        /**
         * Aresta aresta = SI.criaSimilaridade("peter", 1, 1, 2, 2, "peter", 1);
         * SI.insereSimilaridadeKeyInexistente(aresta, "peter"); //
         * SI.getMapaArestas().get("peter"); ArrayList<Aresta> aux =
         * SI.getMapaArestas().get("peter").get(aresta.getOrigem());
         * //System.out.println( aux.get(0).getSimilaridade()); Map<String,
         * ArrayList<Integer>> mapaNomes = new HashMap<String,
         * ArrayList<Integer>>(); ArrayList<Integer> aux2 = new
         * ArrayList<Integer>(); aux2.add(2);
         *
         * mapaNomes.putIfAbsent("peter", aux2); mapaNomes.get("peter").add(3);
         *
         * System.out.println(mapaNomes.get("peter"));
         *
         * System.out.println(mapaNomes.get("peter").contains(0));*
         */
    }

}
