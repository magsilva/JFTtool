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
 * Classe responsável pela geração de Dados de Teste para o critério Análise de Valor Limite
 */
public class GeracaoValorLimite {
    
     /**
      * @author JFTtool
      * Método responsável por gerar os dados de teste para cada restrição de cada entrada do projeto
      * @param id_projeto Id do Projeto
      * @throws SQLException
      * @throws ClassNotFoundException 
      */
     public static void analiseValorLimite (int id_projeto) throws SQLException, ClassNotFoundException {
         
        ArrayList<ArrayList<DadoEntrada>> retornoValidas = new ArrayList<ArrayList<DadoEntrada>>();
        ArrayList<ArrayList<DadoEntrada>> retornoInvalidas = new ArrayList<ArrayList<DadoEntrada>>();
        
        ArrayList<Entrada> resultEntrada = Entrada.getByColumn("id_projeto", Integer.toString(id_projeto));
        for(Entrada e : resultEntrada){
            ArrayList<Restricao> resultRestricao = Restricao.getByColumn("id_entrada", Integer.toString(e.getId()));            
            ArrayList<DadoEntrada> resultDadosValidos = new ArrayList<DadoEntrada>();
            ArrayList<DadoEntrada> resultDadosInvalidos = new ArrayList<DadoEntrada>();
            for(Restricao r : resultRestricao){
              if("valida".equals(r.getClasse())){  
                if(e.getTipo() == TipoEntrada.INTEIRO) {
                    if(r.getTipo() == TipoRestricao.INTERVALO) {
                         //Gerar um número aleatoriamente entre o intervalo
                         String [] valores = r.getConteudo().split(",");
                         int valorMin = Integer.parseInt(valores[0]), valorMax=Integer.parseInt(valores[1]);
                         DadoEntrada t = new DadoEntrada(Integer.toString(RandomInt.geraIntervalo(r.getConteudo())), e.getId(), 1);
                         resultDadosValidos.add(t);
                         //Pegar o limite inferior do intervalo
                         t = new DadoEntrada(Integer.toString(valorMin), e.getId(), 1);
                         resultDadosValidos.add(t);
                         //Pegar o limite inferior menos um
                         t = new DadoEntrada(Integer.toString(valorMin-1), e.getId(), 1);
                         resultDadosValidos.add(t);
                         //Pegar o limite superior
                         t = new DadoEntrada(Integer.toString(valorMax), e.getId(), 1);
                         resultDadosValidos.add(t);
                         //Pegar o limite superior mais um
                         t = new DadoEntrada(Integer.toString(valorMax+1), e.getId(), 1);
                         resultDadosValidos.add(t);
                    }
                    else if(r.getTipo() == TipoRestricao.VALOR_MAXIMO) {
                        //Gerar um número aleatoriamente menor que VALOR_MAXIMO
                        DadoEntrada t = new DadoEntrada(Integer.toString(RandomInt.geraMaximo(Integer.parseInt(r.getConteudo()))), e.getId(), 1);
                        resultDadosValidos.add(t);
                        //Pegar VALOR_MAXIMO
                        t = new DadoEntrada(r.getConteudo(), e.getId(), 1);
                        resultDadosValidos.add(t);
                        //Pegar VALOR_MAXIMO mais um
                        t = new DadoEntrada(Integer.toString(Integer.parseInt(r.getConteudo())+1), e.getId(), 1);
                        resultDadosValidos.add(t);
                    }
                    else if(r.getTipo() == TipoRestricao.VALOR_MINIMO) {
                        // Gerar um número aleatoriamente maior que VALOR_MINIMO
                        DadoEntrada t = new DadoEntrada(Integer.toString(RandomInt.geraMinimo(Integer.parseInt(r.getConteudo()))), e.getId(), 1);
                        resultDadosValidos.add(t);
                        // Pegar VALOR_MINIMO
                        t = new DadoEntrada(r.getConteudo(), e.getId(), 1);
                        resultDadosValidos.add(t);
                        // Pegar VALOR_MINIMO menos um
                        t = new DadoEntrada(Integer.toString(Integer.parseInt(r.getConteudo())-1), e.getId(), 1);
                        resultDadosValidos.add(t);
                    }
                    else if(r.getTipo() == TipoRestricao.IN) {
                        // Está igual ao PartClasses
                        DadoEntrada t = new DadoEntrada(Integer.toString(RandomInt.geraIn(r.getConteudo())), e.getId(), 1);
                        resultDadosValidos.add(t);
                    }
                    else if(r.getTipo() == TipoRestricao.NOT_IN) {
                        // Está igual PartClasses
                        DadoEntrada t = new DadoEntrada(Integer.toString(RandomInt.geraNotIn(r.getConteudo())), e.getId(), 1);
                        resultDadosValidos.add(t);
                    }
                    else if(r.getTipo() == TipoRestricao.IGUALDADE) {
                        DadoEntrada t = new DadoEntrada(Integer.toString(RandomInt.geraIn(r.getConteudo())), e.getId(), 1);
                        resultDadosValidos.add(t);
                    }
                    else if(r.getTipo() == TipoRestricao.DIFERENCA) {
                        DadoEntrada t = new DadoEntrada(Integer.toString(RandomInt.geraNotIn(r.getConteudo())), e.getId(), 1);
                        resultDadosValidos.add(t);
                } 
            }
            if(e.getTipo() == TipoEntrada.REAL){
                  if(r.getTipo() == TipoRestricao.INTERVALO) {
                         //Gerar um número aleatoriamente entre o intervalo
                         String [] valores = r.getConteudo().split(",");
                         double valorMin = Double.parseDouble(valores[0]), valorMax=Double.parseDouble(valores[1]);
                         DadoEntrada t = new DadoEntrada(Double.toString(RandomFloat.geraIntervalo(r.getConteudo())), e.getId(), 1);
                         resultDadosValidos.add(t);
                         //Pegar o limite inferior do intervalo
                         t = new DadoEntrada(Double.toString(valorMin), e.getId(), 1);
                         resultDadosValidos.add(t);
                         //Pegar o limite inferior menos um
                         t = new DadoEntrada(Double.toString(valorMin-1), e.getId(), 1);
                         resultDadosValidos.add(t);
                         //Pegar o limite superior
                         t = new DadoEntrada(Double.toString(valorMax), e.getId(), 1);
                         resultDadosValidos.add(t);
                         //Pegar o limite superior mais um
                         t = new DadoEntrada(Double.toString(valorMax+1), e.getId(), 1);
                         resultDadosValidos.add(t);
                    }
                    else if(r.getTipo() == TipoRestricao.VALOR_MAXIMO) {
                        //Gerar um número aleatoriamente menor que VALOR_MAXIMO
                        DadoEntrada t = new DadoEntrada(Double.toString(RandomFloat.geraMaximo(Double.parseDouble(r.getConteudo()))), e.getId(), 1);
                        resultDadosValidos.add(t);
                        //Pegar VALOR_MAXIMO
                        t = new DadoEntrada(r.getConteudo(), e.getId(), 1);
                        resultDadosValidos.add(t);
                        //Pegar VALOR_MAXIMO mais um
                        t = new DadoEntrada(Double.toString(Double.parseDouble(r.getConteudo())+1), e.getId(), 1);
                        resultDadosValidos.add(t);
                    }
                    else if(r.getTipo() == TipoRestricao.VALOR_MINIMO) {
                        // Gerar um número aleatoriamente maior que VALOR_MINIMO
                        DadoEntrada t = new DadoEntrada(Double.toString(RandomFloat.geraMinimo(Double.parseDouble(r.getConteudo()))), e.getId(), 1); 
                        resultDadosValidos.add(t);
                        // Pegar VALOR_MINIMO
                        t = new DadoEntrada(r.getConteudo(), e.getId(), 1);
                        resultDadosValidos.add(t);
                        // Pegar VALOR_MINIMO menos um
                        t = new DadoEntrada(Double.toString(Double.parseDouble(r.getConteudo())-1), e.getId(), 1);
                        resultDadosValidos.add(t);
                    }
                    else if(r.getTipo() == TipoRestricao.IN) {
                        // Está igual ao PartClasses 
                        DadoEntrada t = new DadoEntrada(Double.toString(RandomFloat.geraIn(r.getConteudo())), e.getId(), 1);
                        resultDadosValidos.add(t);
                    }
                    else if(r.getTipo() == TipoRestricao.NOT_IN) {
                         // Está igual PartClasses
                         DadoEntrada t = new DadoEntrada(Double.toString(RandomFloat.geraNotIn(r.getConteudo())), e.getId(), 1);
                         resultDadosValidos.add(t);
                    }         
                    else if(r.getTipo() == TipoRestricao.IGUALDADE) {
                         DadoEntrada t = new DadoEntrada(Double.toString(RandomFloat.geraIn(r.getConteudo())), e.getId(), 1);
                         resultDadosValidos.add(t);
                    }
                    else if(r.getTipo() == TipoRestricao.DIFERENCA) {
                         DadoEntrada t = new DadoEntrada(Double.toString(RandomFloat.geraIn(r.getConteudo())), e.getId(), 1);
                         resultDadosValidos.add(t);
                    }
            }
            if(e.getTipo() == TipoEntrada.STRING){
                //Restrição do tipo é igual a um grupo de strings
                if(r.getTipo()==TipoRestricao.IN){                            
                     resultDadosValidos.addAll(GeracaoString.geraDadoConjuntoString(r,e));
                }
                //Restrição do tipo é diferente de um grupo de strings
                else if(r.getTipo()==TipoRestricao.NOT_IN){
                     resultDadosValidos.add(GeracaoString.geraStringDiferente(r,e));
                }
            }
            
          //Classe inválida
          else if("invalida".equals(r.getClasse())){
             if(e.getTipo() == TipoEntrada.INTEIRO) {
                    if(r.getTipo() == TipoRestricao.INTERVALO) {
                         //Gerar um número aleatoriamente entre o intervalo
                         String [] valores = r.getConteudo().split(",");
                         int valorMin = Integer.parseInt(valores[0]), valorMax=Integer.parseInt(valores[1]);
                         DadoEntrada t = new DadoEntrada(Integer.toString(RandomInt.geraIntervalo(r.getConteudo())), e.getId(), 0);
                         resultDadosInvalidos.add(t);
                         //Pegar o limite inferior do intervalo
                         t = new DadoEntrada(Integer.toString(valorMin), e.getId(), 0);
                         resultDadosInvalidos.add(t);
                         //Pegar o limite inferior menos um
                         t = new DadoEntrada(Integer.toString(valorMin-1), e.getId(), 0);
                         resultDadosInvalidos.add(t);
                         //Pegar o limite superior
                         t = new DadoEntrada(Integer.toString(valorMax), e.getId(), 0);
                         resultDadosInvalidos.add(t);
                         //Pegar o limite superior mais um
                         t = new DadoEntrada(Integer.toString(valorMax+1), e.getId(), 0);
                         resultDadosInvalidos.add(t);
                    }
                    else if(r.getTipo() == TipoRestricao.VALOR_MAXIMO) {
                        //Gerar um número aleatoriamente menor que VALOR_MAXIMO
                        DadoEntrada t = new DadoEntrada(Integer.toString(RandomInt.geraMaximo(Integer.parseInt(r.getConteudo()))), e.getId(),  0);
                        resultDadosInvalidos.add(t);
                        //Pegar VALOR_MAXIMO
                        t = new DadoEntrada(r.getConteudo(), e.getId(), 0);
                        resultDadosInvalidos.add(t);
                        //Pegar VALOR_MAXIMO mais um
                        t = new DadoEntrada(Integer.toString(Integer.parseInt(r.getConteudo())+1), e.getId(), 0);
                        resultDadosInvalidos.add(t);
                    }
                    else if(r.getTipo() == TipoRestricao.VALOR_MINIMO) {
                        // Gerar um número aleatoriamente maior que VALOR_MINIMO
                        DadoEntrada t = new DadoEntrada(Integer.toString(RandomInt.geraMinimo(Integer.parseInt(r.getConteudo()))), e.getId(), 0);
                        resultDadosInvalidos.add(t);
                        // Pegar VALOR_MINIMO
                        t = new DadoEntrada(r.getConteudo(), e.getId(), 0);
                        resultDadosInvalidos.add(t);
                        // Pegar VALOR_MINIMO menos um
                        t = new DadoEntrada(Integer.toString(Integer.parseInt(r.getConteudo())-1), e.getId(), 0);
                        resultDadosInvalidos.add(t);
                    }
                    else if(r.getTipo() == TipoRestricao.IN) {
                        // Está igual ao PartClasses
                        DadoEntrada t = new DadoEntrada(Integer.toString(RandomInt.geraIn(r.getConteudo())), e.getId(), 0);
                        resultDadosInvalidos.add(t);
                    }
                    else if(r.getTipo() == TipoRestricao.NOT_IN) {
                        // Está igual PartClasses
                        DadoEntrada t = new DadoEntrada(Integer.toString(RandomInt.geraNotIn(r.getConteudo())), e.getId(), 0);
                        resultDadosInvalidos.add(t);
                    }
                    else if(r.getTipo() == TipoRestricao.IGUALDADE) {
                        DadoEntrada t = new DadoEntrada(Integer.toString(RandomInt.geraIn(r.getConteudo())), e.getId(), 0);
                        resultDadosInvalidos.add(t);
                    }
                    else if(r.getTipo() == TipoRestricao.DIFERENCA) {
                        DadoEntrada t = new DadoEntrada(Integer.toString(RandomInt.geraNotIn(r.getConteudo())), e.getId(), 0);
                        resultDadosInvalidos.add(t);
                    }
                } 
            }
            if(e.getTipo() == TipoEntrada.REAL){
                  if(r.getTipo() == TipoRestricao.INTERVALO) {
                         //Gerar um número aleatoriamente entre o intervalo
                         String [] valores = r.getConteudo().split(",");
                         double valorMin = Double.parseDouble(valores[0]), valorMax=Double.parseDouble(valores[1]);
                         DadoEntrada t = new DadoEntrada(Double.toString(RandomFloat.geraIntervalo(r.getConteudo())), e.getId(), 0);
                         resultDadosInvalidos.add(t);
                         //Pegar o limite inferior do intervalo
                         t = new DadoEntrada(Double.toString(valorMin), e.getId(), 0);
                         resultDadosInvalidos.add(t);
                         //Pegar o limite inferior menos um
                         t = new DadoEntrada(Double.toString(valorMin-1), e.getId(), 0);
                         resultDadosInvalidos.add(t);
                         //Pegar o limite superior
                         t = new DadoEntrada(Double.toString(valorMax), e.getId(), 0);
                         resultDadosInvalidos.add(t);
                         //Pegar o limite superior mais um
                         t = new DadoEntrada(Double.toString(valorMax+1), e.getId(), 0);
                         resultDadosInvalidos.add(t);
                    }
                    else if(r.getTipo() == TipoRestricao.VALOR_MAXIMO) {
                        //Gerar um número aleatoriamente menor que VALOR_MAXIMO
                        DadoEntrada t = new DadoEntrada(Double.toString(RandomFloat.geraMaximo(Double.parseDouble(r.getConteudo()))), e.getId(), 0);                    
                        resultDadosInvalidos.add(t);
                        //Pegar VALOR_MAXIMO
                        t = new DadoEntrada(r.getConteudo(), e.getId(), 0);
                        resultDadosInvalidos.add(t);
                        //Pegar VALOR_MAXIMO mais um
                        t = new DadoEntrada(Double.toString(Double.parseDouble(r.getConteudo())+1), e.getId(), 0);
                        resultDadosInvalidos.add(t);
                    }
                    else if(r.getTipo() == TipoRestricao.VALOR_MINIMO) {
                        // Gerar um número aleatoriamente maior que VALOR_MINIMO
                        DadoEntrada t = new DadoEntrada(Double.toString(RandomFloat.geraMinimo(Double.parseDouble(r.getConteudo()))), e.getId(), 0); 
                        resultDadosInvalidos.add(t);
                        // Pegar VALOR_MINIMO
                        t = new DadoEntrada(r.getConteudo(), e.getId(), 0);
                        resultDadosInvalidos.add(t);
                        // Pegar VALOR_MINIMO menos um
                        t = new DadoEntrada(Double.toString(Double.parseDouble(r.getConteudo())-1), e.getId(), 0);
                        resultDadosInvalidos.add(t);
                    }
                    else if(r.getTipo() == TipoRestricao.IN) {
                        // Está igual ao PartClasses 
                        DadoEntrada t = new DadoEntrada(Double.toString(RandomFloat.geraIn(r.getConteudo())), e.getId(), 0);
                        resultDadosInvalidos.add(t);
                    }
                    else if(r.getTipo() == TipoRestricao.NOT_IN) {
                        // Está igual PartClasses
                        DadoEntrada t = new DadoEntrada(Double.toString(RandomFloat.geraNotIn(r.getConteudo())), e.getId(), 0);
                        resultDadosInvalidos.add(t);
                    }              
                    else if(r.getTipo() == TipoRestricao.IGUALDADE) {
                        DadoEntrada t = new DadoEntrada(Double.toString(RandomFloat.geraIn(r.getConteudo())), e.getId(), 0);
                        resultDadosInvalidos.add(t);
                    }
                    else if(r.getTipo() == TipoRestricao.DIFERENCA) {
                        DadoEntrada t = new DadoEntrada(Double.toString(RandomFloat.geraNotIn(r.getConteudo())), e.getId(), 0);
                        resultDadosInvalidos.add(t);
                    }
            }
            if(e.getTipo() == TipoEntrada.STRING){
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
        //adiciona no ArrayList 
        retornoValidas.add(resultDadosValidos); 
        retornoInvalidas.add(resultDadosInvalidos);
        }     
       ArrayList[] retorno = new ArrayList[2]; 
       
       retorno[0] = retornoValidas;
       retorno[1] = retornoInvalidas;
       
       try {
            //Gera as combinações dos dados de teste e salva no banco
            GeraDadoTeste.geraDadoTeste(retorno, TipoCriterio.ANALISE_VALOR_LIMITE, id_projeto);
        } catch (Exception ex) {
            Logger.getLogger(Geracao.class.getName()).log(Level.SEVERE, null, ex);
        }
      
    }
}
