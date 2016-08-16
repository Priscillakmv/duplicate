/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package graph;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 *
 * @author Priscilla
 */
public class Aresta {

    private double similaridade;
    private Vertice origem;
    private Vertice destino;
    private String keyDestino;

    public Aresta(Vertice elemOrigem, Vertice elementoDestino, double similaridade, String keyDestino) {

        this.similaridade = similaridade;
        this.origem = elemOrigem;
        this.destino = elementoDestino;
               // this.keyDestino = keyDestino;

    }

    public String getKeyDestino() {
        return keyDestino;
    }

    public void setKeyDestino(String keyDestino) {
        this.keyDestino = keyDestino;
    }

    public double getSimilaridade() {
        return similaridade;
    }

    public void setSimilaridade(float similaridade) {
        this.similaridade = similaridade;
    }

    public Vertice getOrigem() {
        return origem;
    }

    public void setOrigem(Vertice origem) {
        this.origem = origem;
    }

    public Vertice getDestino() {
        return destino;
    }

    public void setDestino(Vertice destino) {
        this.destino = destino;
    }

}
