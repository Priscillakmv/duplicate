/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package avaliacao;

import java.util.ArrayList;

/**
 *
 * @author Priscilla
 */
public class Calculadora {

    public Calculadora() {
    }
    
    public double calculaSoma(double valores[]) {
       double soma = 0;
        for (int i = 0; i < valores.length; i++) {
           double get = valores[i];

            soma = soma + get;

        }
        return soma;

    }
    
     public double[] calculaFMeasureArray(double acertos[], double resultadosEsperados[], double erros[]) {
        double[] fMeasureAlcancada = new double[acertos.length];
        double[]recallAux = calculaRecallArray(acertos, resultadosEsperados);
        double[]precisaoAux = calculaPrecisaoArray(acertos, erros);

          for (int i = 0; i < recallAux.length; i++) {
               if ((recallAux[i]==0)&&(precisaoAux[i]==0)) {
                   fMeasureAlcancada[i]=0;
                 
            }else{
                 fMeasureAlcancada[i]= (2 * recallAux[i] * precisaoAux[i] / (recallAux[i] + precisaoAux[i]));  
               }
              
          }
    return fMeasureAlcancada;
     }
      public double calculaFMeasure(double acertos[], double resultadosEsperados[], double erros[]) {
        double[] fMeasureAlcancada = new double[acertos.length];
        double[]recallAux = calculaRecallArray(acertos, resultadosEsperados);
        double[]precisaoAux = calculaPrecisaoArray(acertos, erros);

          for (int i = 0; i < recallAux.length; i++) {
               if ((recallAux[i]==0)&&(precisaoAux[i]==0)) {
                   fMeasureAlcancada[i]=0;
                 
            }else{
                 fMeasureAlcancada[i]= (2 * recallAux[i] * precisaoAux[i] / (recallAux[i] + precisaoAux[i]));  
               }
              
          }
    return calculaMediaAritmetica(fMeasureAlcancada);

        
        
       // for (int i = 0; i < acertos.length; i++) {
         //  double get = acertos[i];
         //   double get2 = resultadosEsperados[i];
            
           // if ((calculaRecall(acertos, resultadosEsperados)==0)&&(calculaPrecisao(acertos, erros)==0)) {
              //  return 0; 
                // continue
          //  }
           // else{
             //   return  2 * calculaRecall(acertos, resultadosEsperados) * calculaPrecisao(acertos, erros) / (calculaRecall(acertos, resultadosEsperados) + calculaPrecisao(acertos, erros));

                
          //  }
          
            

    }
      
      public double calculaRecall(double acertos[], double resultadosEsperados[]) {
        double recallAlcancada[] = new double[acertos.length];
        for (int i = 0; i < acertos.length; i++) {
            double get = acertos[i];
            double get2 = resultadosEsperados[i];
            
            if (get2==0) {
                  recallAlcancada[i]= 1; 
                  continue;
            }
            if(get==0){
                recallAlcancada[i] = 0;
                  continue;
            }
          
              double aux = (get / get2);//divisao Por Zero ver como faz. Revisar os arrays de precisao
            recallAlcancada[i]= aux;  
                      

        }
        return calculaMediaAritmetica(recallAlcancada);

    }
    
    public double[] calculaRecallArray(double acertos[], double resultadosEsperados[]) {
       double recallAlcancada[] = new double[acertos.length];
        for (int i = 0; i < acertos.length; i++) {
            double get = acertos[i];
           double get2 = resultadosEsperados[i];
            
            if (get2==0) {
                  recallAlcancada[i]= 1; 
                  continue;
            }
            if(get==0){
                recallAlcancada[i] = 0;
                  continue;
            }
          
             double aux = (get / get2);//divisao Por Zero ver como faz. Revisar os arrays de precisao
            recallAlcancada[i]= aux;  
                      

        }
        return recallAlcancada;

    }
    public double calculaPrecisao(double acertos[],double erros[]) {
        double precisaoAlcancada[] = new double[acertos.length];
        for (int i = 0; i < acertos.length; i++) {
           double get = acertos[i];
           double get2 = erros [i];
            
            if (get==0 && get2==0) {
               precisaoAlcancada [i]= 1;
               continue;

                
            }if (get==0) {
                 precisaoAlcancada[i]=0;
               continue;
            }
            
         double aux = (get / (get2 + get));
            precisaoAlcancada[i]=aux;
            
        //    System.out.println(" *&*&*&*&*&*&* " + " " + get + " " + get2 + " " + aux );
           
       

        }
        return calculaMediaAritmetica(precisaoAlcancada);

    }


    public double[] calculaPrecisaoArray(double acertos[],double erros[]) {
       double precisaoAlcancada[] = new double[acertos.length];
        for (int i = 0; i < acertos.length; i++) {
            double get = acertos[i];
           double get2 = erros [i];
            
            if (get==0 && get2==0) {
               precisaoAlcancada [i]= 1;
               continue;

                
            }if (get==0) {
                 precisaoAlcancada[i]=0;
               continue;
            }
            
          double aux = (get / (get2 + get));
            precisaoAlcancada[i]=aux;
            
        //    System.out.println(" *&*&*&*&*&*&* " + " " + get + " " + get2 + " " + aux );
           
       

        }
        return precisaoAlcancada;

    }

   
    public double calculaMediaAritmetica(double valores[]) {
        double media = 0;
        media = calculaSoma(valores) / valores.length;
        return media;

    }
    
    
}
