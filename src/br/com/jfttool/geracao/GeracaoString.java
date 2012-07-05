/*
 * JFTtool - Java Functional Testing tool
 * Created: 03/06/2012
 * 
 * Created by:
 *      Alexandre Ponce de Oliveira - alexandreponce at icmc.usp.br
 *      Daniela Oliveira Francisco - doliveira@sc.usp.br
 *      Edilson José Davoglio Candido - edilson at icmc.usp.br
 *      Filipe Del Nero Grillo - grillo at icmc.usp.br
 *      José Ricardo Furlan Ronqui - jose.ronqui at usp.br
 *      Marcelo Benites Gonçalves - marcelob at icmc.usp.br
 *      Sofia Larissa Costa - sofialc at icmc.usp.br
 * 
 * Copyright (c) 2012
 * All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 * 
 * Redistributions of source code must retain the above copyright notice,
 * this list of conditions and the following disclaimer.
 * 
 * Redistributions in binary form must reproduce the above copyright
 * notice, this list of conditions and the following disclaimer in the
 * documentation and/or other materials provided with the distribution.
 * 
 * Neither the name of the project's authors nor the names of its
 * contributors may be used to endorse or promote products derived from
 * this software without specific prior written permission.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
 * LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS
 * FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT
 * HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED
 * TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 *
 */
package br.com.jfttool.geracao;

import br.com.jfttool.enums.TipoRestricao;
import br.com.jfttool.models.DadoEntrada;
import br.com.jfttool.models.Entrada;
import br.com.jfttool.models.Restricao;
import java.util.*;

/**
 * @author JFTtool Team
 * Classe que gera dados de teste para restrições de entrada tipo Strin0g.
 */
public class GeracaoString {
    /**
     * @author JFTtool Team
     * Gera um conjunto de dados de teste, um para cada string do conjunto (cobre classes válidas e inválidas) 
     * @return ArrayList<DadoEntrada> Model contendo as restrições válidas ou inválidas
     * @param r Restrição para uma entrada
     * @param e Entrada associada à restrição
     */
    public static ArrayList<DadoEntrada> geraDadoConjuntoString(Restricao r, Entrada e){
        ArrayList<DadoEntrada> resultDados = new ArrayList<DadoEntrada>();
        String aux, valores = r.getConteudo();
        int i=0;
            
        while(i!=-1){
            i=valores.indexOf("|");
            if(i!=-1){
                aux=valores.substring(0, i);
                valores=valores.substring(i+1);
                DadoEntrada t = new DadoEntrada(aux, e.getId(), 1);
                resultDados.add(t);              
            }else if(i==-1){
                aux=valores;
                DadoEntrada t = new DadoEntrada(aux, e.getId(), 1);
                resultDados.add(t);      
                valores="";
            }                   
        }        
        return resultDados;
    }
    /**
     * @author JFTtool Team
     * Retorna um dado de teste (String) diferente do grupo de strings (cobre classes válidas e inválidas) 
     * @return ArrayList<DadoEntrada> Model contendo as restrições válidas ou inválidas
     * @param r Restrição para uma entrada
     * @param e Entrada associada à restrição
     */
    public static DadoEntrada geraStringDiferente(Restricao r, Entrada e){
        
        DadoEntrada resultDado = null;
        Boolean test;
        String nova;
        do{
            test=true;
            int k = r.getConteudo().indexOf("|");
            if(k<1)
                k=r.getConteudo().length();
            nova =  GeracaoString.geraStringAleatoria(k);
            String aux="";
            int i=0,j=0;
            String valores = r.getConteudo();
            while(i!=-1){
                i=valores.indexOf("|");
                //recebe uma string do conjunto
                if(i!=-1){
                    aux=valores.substring(0, i);
                    valores=valores.substring(i+1);             
                }
                else {
                    aux=valores;     
                    valores="";
                }   
                
                if(!aux.equals(nova)){
                    test=false;
                    
                }
             } 
        }while(test);
                       
        resultDado=new DadoEntrada(nova, e.getId(), 0);        
        return resultDado;
    }
    
    /**
     * @author JFTtool Team
     * Gera uma string aleatória de tamanho "tam"  
     * @param tam Tamanho que a string aleatória deve conter
     */
    public static String geraStringAleatoria (int tam){
        String retorno="";
        UUID uuid = UUID.randomUUID();  
        String myRandom = uuid.toString();  
        retorno=(myRandom.substring(0,tam)); 
        return retorno;
    }

}
