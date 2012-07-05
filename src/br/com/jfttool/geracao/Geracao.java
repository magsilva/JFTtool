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
import br.com.jfttool.enums.TipoEntrada;
import br.com.jfttool.enums.TipoRestricao;
import br.com.jfttool.models.DadoEntrada;
import br.com.jfttool.models.Entrada;
import br.com.jfttool.models.Restricao;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author JFTtool
 *Classe responsável pela geração de dados utilizando o critério do particionamento em classes
 */
public class Geracao {

 /**
  * Realiza a geração de dados de teste para o particionamento em classes de equivalência de um projeto
  * @param id_projeto Identificador do projeto que deseja-se realizar o particonamento em classes de equivalência.
  * @throws SQLException
  * @throws ClassNotFoundException
  */
    public static void partEmClasses (int id_projeto) throws SQLException, ClassNotFoundException{
        //D: Faz todo o particionamento em classes do projeto de uma só vez
        ArrayList<ArrayList<DadoEntrada>> retornoValidas = new ArrayList<ArrayList<DadoEntrada>>(); 
        ArrayList<ArrayList<DadoEntrada>> retornoInvalidas = new ArrayList<ArrayList<DadoEntrada>>();
       
        ArrayList<Entrada> resultEntrada = Entrada.getByColumn("id_projeto", Integer.toString(id_projeto));
        for(Entrada e : resultEntrada){
            ArrayList<Restricao> resultRestricao = Restricao.getByColumn("id_entrada", Integer.toString(e.getId()));            
            ArrayList<DadoEntrada> resultDadosValidos = new ArrayList<DadoEntrada>();
            ArrayList<DadoEntrada> resultDadosInvalidos = new ArrayList<DadoEntrada>();
            for(Restricao r : resultRestricao){//for percorre a lista
                if("valida".equals(r.getClasse())){//se for valida
                    if(e.getTipo()==TipoEntrada.INTEIRO){
                        //Inicio da parte que lida com inteiros aqui.
                        if(r.getTipo()==TipoRestricao.IN){                          
                          DadoEntrada t = new DadoEntrada(Integer.toString(RandomInt.geraIn(r.getConteudo())), e.getId(), 1);
                          resultDadosValidos.add(t);                                                      
                        } 
                        else if(r.getTipo()==TipoRestricao.NOT_IN){                            
                              DadoEntrada t = new DadoEntrada(Integer.toString(RandomInt.geraNotIn(r.getConteudo())), e.getId(), 1);
                              resultDadosValidos.add(t);                                                          
                        }
                        else if(r.getTipo()==TipoRestricao.IGUALDADE) {
                              DadoEntrada t = new DadoEntrada(Integer.toString(RandomInt.geraIn(r.getConteudo())), e.getId(), 1);
                              resultDadosValidos.add(t);
                        }
                        else if(r.getTipo()==TipoRestricao.DIFERENCA) {
                              DadoEntrada t = new DadoEntrada(Integer.toString(RandomInt.geraNotIn(r.getConteudo())), e.getId(), 1);
                              resultDadosValidos.add(t);  
                        }
                        else if (r.getTipo()==TipoRestricao.INTERVALO){//verificar geração aqui.
                            DadoEntrada t = new DadoEntrada(Integer.toString(RandomInt.geraIntervalo(r.getConteudo())), e.getId(), 1);
                            resultDadosValidos.add(t);                        
                        }
                        else if (r.getTipo()==TipoRestricao.VALOR_MAXIMO){
                           DadoEntrada t = new DadoEntrada(Integer.toString(RandomInt.geraMaximo(Integer.parseInt(r.getConteudo()))), e.getId(), 1);
                           resultDadosValidos.add(t);                           
                        }
                        else if (r.getTipo()==TipoRestricao.VALOR_MINIMO){                            
                            DadoEntrada t = new DadoEntrada(Integer.toString(RandomInt.geraMinimo(Integer.parseInt(r.getConteudo()))), e.getId(), 1);
                            resultDadosValidos.add(t);                           
                        }
                     //Final dos Inteiros aqui.   
                    }
                    else if(e.getTipo()==TipoEntrada.REAL){
                        if(r.getTipo()==TipoRestricao.IN){
                            DadoEntrada t = new DadoEntrada(Double.toString(RandomFloat.geraIn(r.getConteudo())), e.getId(), 1);
                            resultDadosValidos.add(t);
                        }
                        else if(r.getTipo()==TipoRestricao.NOT_IN){
                            DadoEntrada t = new DadoEntrada(Double.toString(RandomFloat.geraNotIn(r.getConteudo())), e.getId(), 1);
                            resultDadosValidos.add(t);
                        }
                        else if(r.getTipo()==TipoRestricao.IGUALDADE) {
                            DadoEntrada t = new DadoEntrada(Double.toString(RandomFloat.geraIn(r.getConteudo())), e.getId(), 1);
                            resultDadosValidos.add(t);
                        }
                        else if(r.getTipo()==TipoRestricao.DIFERENCA){
                            DadoEntrada t = new DadoEntrada(Double.toString(RandomFloat.geraNotIn(r.getConteudo())), e.getId(), 1);
                            resultDadosValidos.add(t);
                        }
                        else if(r.getTipo()==TipoRestricao.INTERVALO){
                            DadoEntrada t = new DadoEntrada(Double.toString(RandomFloat.geraIntervalo(r.getConteudo())), e.getId(), 1);
                            resultDadosValidos.add(t);
                        }
                        else if(r.getTipo()==TipoRestricao.VALOR_MAXIMO){
                            DadoEntrada t = new DadoEntrada(Double.toString(RandomFloat.geraMaximo(Double.parseDouble(r.getConteudo()))), e.getId(), 1);
                            resultDadosValidos.add(t);
                        }
                        else if(r.getTipo()==TipoRestricao.VALOR_MINIMO){
                            DadoEntrada t = new DadoEntrada(Double.toString(RandomFloat.geraMinimo(Double.parseDouble(r.getConteudo()))), e.getId(), 1);
                            resultDadosValidos.add(t);
                        }
                   }
                    else if(e.getTipo()==TipoEntrada.STRING){
                        //Restrição do tipo é igual a um grupo de strings
                        if(r.getTipo()==TipoRestricao.IN){                            
                            resultDadosValidos.addAll(GeracaoString.geraDadoConjuntoString(r,e));
                        }
                        //Restrição do tipo é diferente de um grupo de strings
                        else if(r.getTipo()==TipoRestricao.NOT_IN){
                            resultDadosValidos.add(GeracaoString.geraStringDiferente(r,e));
                        }
                    }
                } else if("invalida".equals(r.getClasse())){//caso seja invalida outra idéia em gracao
                    //TODO: a geração de inválidas será automática?
                    if(e.getTipo()==TipoEntrada.INTEIRO){
                        if(r.getTipo()==TipoRestricao.IN){
                          DadoEntrada t = new DadoEntrada(Integer.toString(RandomInt.geraIn(r.getConteudo())), e.getId(), 0);
                          resultDadosInvalidos.add(t);
                        } 
                        else if(r.getTipo()==TipoRestricao.NOT_IN){
                            DadoEntrada t = new DadoEntrada(Integer.toString(RandomInt.geraNotIn(r.getConteudo())), e.getId(), 0);
                            resultDadosInvalidos.add(t);
                        } 
                        else if(r.getTipo()==TipoRestricao.IGUALDADE) {
                            DadoEntrada t = new DadoEntrada(Integer.toString(RandomInt.geraIn(r.getConteudo())), e.getId(), 0);
                            resultDadosInvalidos.add(t);
                        }
                        else if(r.getTipo()==TipoRestricao.DIFERENCA) {
                            DadoEntrada t = new DadoEntrada(Integer.toString(RandomInt.geraNotIn(r.getConteudo())), e.getId(), 0);
                            resultDadosInvalidos.add(t);
                        }
                        else if (r.getTipo()==TipoRestricao.INTERVALO){//verificar geração aqui.
                            DadoEntrada t = new DadoEntrada(Integer.toString(RandomInt.geraIntervalo(r.getConteudo())), e.getId(), 0);
                            resultDadosInvalidos.add(t);
                        }
                        else if (r.getTipo()==TipoRestricao.VALOR_MAXIMO){
                            DadoEntrada t = new DadoEntrada(Integer.toString(RandomInt.geraMaximo(Integer.parseInt(r.getConteudo()))), e.getId(), 0);
                            resultDadosInvalidos.add(t);
                        }
                        else if (r.getTipo()==TipoRestricao.VALOR_MINIMO){
                            DadoEntrada t = new DadoEntrada(Integer.toString(RandomInt.geraMinimo(Integer.parseInt(r.getConteudo()))), e.getId(), 0);                                                
                            resultDadosInvalidos.add(t);
                        }
                    }else if (e.getTipo()==TipoEntrada.REAL){
                        if(r.getTipo()==TipoRestricao.IN){
                            DadoEntrada t = new DadoEntrada(Double.toString(RandomFloat.geraIn(r.getConteudo())), e.getId(), 0);
                            resultDadosInvalidos.add(t);
                        }
                        else if(r.getTipo()==TipoRestricao.NOT_IN){
                            DadoEntrada t = new DadoEntrada(Double.toString(RandomFloat.geraNotIn(r.getConteudo())), e.getId(), 0);
                            resultDadosInvalidos.add(t);
                        }
                        else if(r.getTipo()==TipoRestricao.IGUALDADE) {
                            DadoEntrada t = new DadoEntrada(Double.toString(RandomFloat.geraIn(r.getConteudo())), e.getId(), 0);
                            resultDadosInvalidos.add(t);
                        }
                        else if(r.getTipo()==TipoRestricao.DIFERENCA){
                            DadoEntrada t = new DadoEntrada(Double.toString(RandomFloat.geraNotIn(r.getConteudo())), e.getId(), 0);
                            resultDadosInvalidos.add(t);
                        }
                        else if(r.getTipo()==TipoRestricao.INTERVALO){
                            DadoEntrada t = new DadoEntrada(Double.toString(RandomFloat.geraIntervalo(r.getConteudo())), e.getId(), 0);
                            resultDadosInvalidos.add(t);
                        }
                        else if(r.getTipo()==TipoRestricao.VALOR_MAXIMO){
                            DadoEntrada t = new DadoEntrada(Double.toString(RandomFloat.geraMaximo(Double.parseDouble(r.getConteudo()))), e.getId(), 0);
                            resultDadosInvalidos.add(t);
                        }
                        else if(r.getTipo()==TipoRestricao.VALOR_MINIMO){
                            DadoEntrada t = new DadoEntrada(Double.toString(RandomFloat.geraMinimo(Double.parseDouble(r.getConteudo()))), e.getId(), 0);
                            resultDadosInvalidos.add(t);
                        }
                    }else if(e.getTipo()==TipoEntrada.STRING){
                        //Restrição do tipo é igual a um grupo de strings
                        if(r.getTipo()==TipoRestricao.IN){                            
                            resultDadosInvalidos.addAll(GeracaoString.geraDadoConjuntoString(r,e));
                        }
                        //Restrição do tipo é diferente de um grupo de strings
                        else if(r.getTipo()==TipoRestricao.NOT_IN){
                            resultDadosInvalidos.add(GeracaoString.geraStringDiferente(r,e));
                        }
                   }
               }
           }
           retornoValidas.add(resultDadosValidos); 
           retornoInvalidas.add(resultDadosInvalidos); 
        }
        ArrayList[] retorno = new ArrayList[2];
        
        retorno[0] = retornoValidas;
        retorno[1] = retornoInvalidas;
        
        try {
            GeraDadoTeste.geraDadoTeste(retorno, TipoCriterio.PARTICIONAMENTO_CLASSES, id_projeto);
        } catch (Exception ex) {
            Logger.getLogger(Geracao.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
         
}
