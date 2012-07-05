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

import br.com.jfttool.enums.TipoCriterio;
import br.com.jfttool.models.DadoEntrada;
import br.com.jfttool.models.DadoTeste;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * @author JFTtool
 * Classe responsável por fazer a combinação dos dados de entrada para gerar dados de teste
 */
public class GeraDadoTeste {
    /**
     * Método que realiza a combinação entre os dados válidos e inválidos para a geração dos dados de teste
     * @param dados Lista com os dados de entrada gerados
     * @param tipo Critério selecionado
     * @param id_projeto Id do Projeto
     * @throws SQLException
     * @throws ClassNotFoundException
     * @throws Exception 
     */
    public static void geraDadoTeste(ArrayList[] dados, TipoCriterio tipo, int id_projeto) throws SQLException, ClassNotFoundException, Exception{
       
        //Talvez colocar for para gerar vários dados de teste valido
        DadoTeste dv = new DadoTeste(tipo, id_projeto);
        dv.save();          
      
        for (int i=0;i<dados[0].size();i++){
            ArrayList<DadoEntrada> nome = (ArrayList<DadoEntrada>) dados[0].get(i);
            int indice = (int) (nome.size()*Math.random()); 
            nome.get(indice).setId_Dado_Teste(dv.getId());
            nome.get(indice).save();     
        }
        //final talvez colocar o for
        
        for (int i =0 ; i<dados[1].size();i++){  
          ArrayList <DadoEntrada> nome = (ArrayList <DadoEntrada>) dados[1].get(i);
          for (int j=0; j<nome.size(); j++){
                DadoTeste di = new DadoTeste(tipo, id_projeto);
                di.save(); 
                for (int k=0;k<dados[0].size();k++){
                    
                      ArrayList <DadoEntrada> nomeValidas = (ArrayList <DadoEntrada>) dados[0].get(k);
                      
                      if(k!=i){  
                          int indice = (int) (nomeValidas.size()*Math.random());   
                          DadoEntrada daux = new DadoEntrada(nomeValidas.get(indice).getValor(), nomeValidas.get(indice).getId_entrada(), di.getId(), 1);
                          daux.save();
                      }    
                      else if(k==i){
                          nome.get(j).setId_Dado_Teste(di.getId());
                          nome.get(j).save();
                      }
                }
          }      
        }      
    }
    
}
