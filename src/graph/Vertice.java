/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package graph;

import java.util.Objects;

/**
 *
 * @author Priscilla
 */
public class Vertice {
    private String id;
    private String sourceId;
    private int clusterId;
    private double similarity;

    public String getConteudoComparacao2() {
        return conteudoComparacao2;
    }
    public void setSimilarity (double similarity){
        this.similarity = similarity;
    }
    public double getSimilarity (){
        return this.similarity;
    }

    public void setConteudoComparacao2(String conteudoComparacao2) {
        this.conteudoComparacao2 = conteudoComparacao2;
    }
        private String conteudoComparacao;
        private String conteudoComparacao2;

    public String getConteudoComparacao() {
        return conteudoComparacao;
    }

    public void setConteudoComparacao(String conteudoComparacao) {
        this.conteudoComparacao = conteudoComparacao;
    }

   

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Vertice other = (Vertice) obj;
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        if (this.clusterId != other.clusterId) {
            return false;
        }
        return true;
    }

   
    
    public  Vertice (String id, String sourceId, int clusterId){
        this.id = id;
        this.sourceId = sourceId;
        this.clusterId = clusterId;
        
    }
    public  Vertice (String id, String sourceId, int clusterId, String conteudoComparacao, String conteudoComparacao2){
        this.id = id;
        this.sourceId = sourceId;
        this.clusterId = clusterId;
        this.conteudoComparacao = conteudoComparacao;
        this.conteudoComparacao2 = conteudoComparacao2;
        
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSourceId() {
        return sourceId;
    }

    public void setSourceId(String sourceId) {
        this.sourceId = sourceId;
    }

    public int getClusterId() {
        return clusterId;
    }

    public void setClusterId(int clusterId) {
        this.clusterId = clusterId;
    }
    
    
}
