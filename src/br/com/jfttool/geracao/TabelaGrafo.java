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

import br.com.jfttool.enums.TipoRelacionamento;
import br.com.jfttool.models.Causa;
import br.com.jfttool.models.CausaComposta;
import br.com.jfttool.models.CausaEfeito;
import br.com.jfttool.models.Efeito;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Classe que é responsável por calcular a tabela de decisão do grafo causa-efeito
 * AVISO: Esta classe é preliminar e não calcula todas as possibilidades que deveria.
 * É necessário melhorá-la
 */
public class TabelaGrafo {

    private ArrayList<Efeito> efeitos = null;
    private ArrayList<Causa> causas = null;
    private ArrayList<CausaComposta> causasCompostas  = null;
    private ArrayList<CausaEfeito> causasEfeitos  = null;
    
    private Hashtable<Integer, Boolean> valorCausas = new Hashtable<Integer, Boolean>();
    private Hashtable<Integer, Boolean> valorEfeitos = new Hashtable<Integer, Boolean>();
    
    private Hashtable<Integer, CausaComposta> causaCompostaPorId = new Hashtable<Integer, CausaComposta>();
    private Hashtable<Integer, CausaEfeito> causaEfeitoPorCompostaId = new Hashtable<Integer, CausaEfeito>();
    private Hashtable<Integer, CausaEfeito> causaEfeitoPorCausaId = new Hashtable<Integer, CausaEfeito>();
    
    private ArrayList<ArrayList<String>> causasTabela = new ArrayList<ArrayList<String>>();
    private ArrayList<ArrayList<String>> efeitosTabela = new ArrayList<ArrayList<String>>();
    
    public TabelaGrafo() {}

    public ArrayList<ArrayList<String>> getCausasTabela() {
        return causasTabela;
    }

    public ArrayList<ArrayList<String>> getEfeitosTabela() {
        return efeitosTabela;
    }

    public ArrayList<Causa> getCausas() {
        return causas;
    }

    public ArrayList<Efeito> getEfeitos() {
        return efeitos;
    }
    
    /**
     * A partir do id de um projeto, gera a tabela de decisão em dois atributos
     * arraylist da classe: CausasTabela e EfeitosTabela
     * @param idProjeto 
     */
    public void geraTabela(Integer idProjeto) {
        try {
            
            //Pega todos os dados no banco
            efeitos = Efeito.getByColumn("id_projeto", idProjeto.toString());
            causas = Causa.getByColumn("id_projeto", idProjeto.toString());
            causasCompostas = CausaComposta.getByColumn("id_projeto", idProjeto.toString());
            causasEfeitos = CausaEfeito.getByColumn("id_projeto", idProjeto.toString());
            
            //Organiza as causas compostas por id em um hashtable
            for (int i = 0 ; i < causasCompostas.size() ; i++) {
                CausaComposta cc = causasCompostas.get(i);
                causaCompostaPorId.put(cc.getId(), cc);
            }
            
            // Organiza as causaEfeitos por id da causa ou por id da causa composta
            for (int i = 0 ; i < causasEfeitos.size() ; i++) {
                CausaEfeito ce = causasEfeitos.get(i);
                if (ce.getId_causa() != 0) {
                    causaEfeitoPorCausaId.put(ce.getId_causa(), ce);
                } else if (ce.getId_causa_composta() != 0) {
                    causaEfeitoPorCompostaId.put(ce.getId_causa_composta(), ce);
                }
            }
            
            //Comeca a processar a tabela a partir dos efeitos
            for (int i = 0 ; i < efeitos.size() ; i++) {
                Efeito e = efeitos.get(i);
                valorCausas = new Hashtable<Integer, Boolean>();
                valorEfeitos = new Hashtable<Integer, Boolean>();
                
                //pega as causaEfeitos que se relacionam com esse efeito
                ArrayList<CausaEfeito> ces = CausaEfeito.getByColumn("id_efeito", e.getId().toString());
                
                valorEfeitos.put(e.getId(), true);
                
                // Itera nas causa-efeitos
                for (int j = 0 ; j < ces.size() ; j++) {
                    
                    CausaEfeito ce = ces.get(j);
                    
                    //Se ela liga o efeito a uma causa. Fim do processando desse efeito
                    if (ce.getId_causa() != 0) {
                        if (ce.getTipo_relacionamento() == TipoRelacionamento.IDENTIDADE) {
                            valorCausas.put(ce.getId_causa(), Boolean.TRUE);
                            verificaEfeito(Boolean.TRUE, ce.getId_causa());
                        } else if (ce.getTipo_relacionamento() == TipoRelacionamento.NOT) {
                            valorCausas.put(ce.getId_causa(), Boolean.FALSE);
                            verificaEfeito(Boolean.FALSE, ce.getId_causa());
                        }
                    //Se liga o efeito a uma causa composta, deve continuar através da função recursiva processaCaminho
                    } else if (ce.getId_causa_composta() != 0) {
                        if (ce.getTipo_relacionamento() == TipoRelacionamento.IDENTIDADE) {
                            processaCaminho(Boolean.TRUE, causaCompostaPorId.get(ce.getId_causa_composta()));
                        } else if (ce.getTipo_relacionamento() == TipoRelacionamento.NOT) {
                            processaCaminho(Boolean.FALSE, causaCompostaPorId.get(ce.getId_causa_composta()));
                        }
                    }
                }
                
                
                salvaColuna(valorCausas, valorEfeitos);
                
            }
            
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(TabelaGrafo.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(TabelaGrafo.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        
    }
    
    /**
     * Converte as informações de uma coluna da tabela de decisão de hashtable para um arraylist
     * Além disso converte os valores booleanos para zeros e uns e um "-" quando é nulo.
     * @param causaHash Hashtable com valores das causas
     * @param efeitoHash Hashtable com valores dos efeitos
     */
    private void salvaColuna(Hashtable<Integer, Boolean> causaHash, Hashtable<Integer, Boolean> efeitoHash) {
        ArrayList<String> colunaTabelaCausa = new ArrayList<String>(); 
        for (int i=0 ; i< causas.size() ; i++) {
            Causa c = causas.get(i);
            if (causaHash.containsKey(c.getId())) {
                if (causaHash.get(c.getId())) {
                    colunaTabelaCausa.add("1");
                } else {
                    colunaTabelaCausa.add("0");
                }
            } else {
                colunaTabelaCausa.add("-");
            }
        }
        
        causasTabela.add(colunaTabelaCausa);
        
        ArrayList<String> colunaTabelaEfeito = new ArrayList<String>(); 
        for (int i=0 ; i< efeitos.size() ; i++) {
            Efeito e = efeitos.get(i);
            if (efeitoHash.containsKey(e.getId())) {
                if (efeitoHash.get(e.getId())) {
                    colunaTabelaEfeito.add("1");
                } else {
                    colunaTabelaEfeito.add("0");
                }
            } else {
                colunaTabelaEfeito.add("-");
            }
        }
        
        efeitosTabela.add(colunaTabelaEfeito);
    }
    
    
    /**
     * Função recursiva que processa os caminhos do grafo causa-efeito, partindo
     * de uma causa composta e um valor de saida para essa causa composta.
     * A recursão para sempre que a causa composta encontra uma causa e continua
     * se uma causa_composta é encontrada.
     * @param valorSaida Boolean que representa a saída da causa composta true para 1 e false para 0
     * @param cc CausaComposta a partir da qual o grafo será percorrido
     */
    private void processaCaminho(Boolean valorSaida, CausaComposta cc){
    
        if (cc.getTipo_relacionamento() == TipoRelacionamento.AND) {
            
            //lado 1
            //Procesa causa
            if (cc.getCausa1_id() != 0) {
                if (cc.getRelacao_c1() == TipoRelacionamento.IDENTIDADE) {
                    valorCausas.put(cc.getCausa1_id(), valorSaida);
                    verificaEfeito(valorSaida, cc.getCausa1_id());
                } else if (cc.getRelacao_c1() == TipoRelacionamento.NOT) {
                    valorCausas.put(cc.getCausa1_id(), !valorSaida);
                    verificaEfeito(!valorSaida, cc.getCausa1_id());
                }
            } else if (cc.getCausa_composta1_id() != 0) { //Processa causa composta
                if (cc.getRelacao_c1() == TipoRelacionamento.IDENTIDADE) {
                    if (valorSaida) {
                        processaCaminho(valorSaida, causaCompostaPorId.get(cc.getCausa_composta1_id()));
                    } else {
                        processaCaminho(true, causaCompostaPorId.get(cc.getCausa_composta1_id()));
                    }
                } else if (cc.getRelacao_c1() == TipoRelacionamento.NOT) {
                    if (valorSaida) {
                        processaCaminho(!valorSaida, causaCompostaPorId.get(cc.getCausa_composta1_id()));
                    } else {
                        processaCaminho(false, causaCompostaPorId.get(cc.getCausa_composta1_id()));
                    }
                }
            } else {
                System.err.println("ERRO: Causa 1 ou Causa Composta 1 não setados");
            }
            
            //lado 2
            //Processa causa
            if (cc.getCausa2_id() != 0) {
                if (cc.getRelacao_c2() == TipoRelacionamento.IDENTIDADE) {
                    valorCausas.put(cc.getCausa2_id(), valorSaida);
                    verificaEfeito(valorSaida, cc.getCausa2_id());
                } else if (cc.getRelacao_c2() == TipoRelacionamento.NOT) {
                    valorCausas.put(cc.getCausa2_id(), !valorSaida);
                    verificaEfeito(!valorSaida, cc.getCausa2_id());
                }
            } else if (cc.getCausa_composta2_id() != 0) { //Processa causa composta
                if (cc.getRelacao_c2() == TipoRelacionamento.IDENTIDADE) {
                    processaCaminho(valorSaida, causaCompostaPorId.get(cc.getCausa_composta2_id()));
                } else if (cc.getRelacao_c2() == TipoRelacionamento.NOT) {
                    processaCaminho(!valorSaida, causaCompostaPorId.get(cc.getCausa_composta2_id()));
                }
            } else {
                System.err.println("ERRO: Causa 2 ou Causa Composta 2 não setados");
            }

        } else if (cc.getTipo_relacionamento() == TipoRelacionamento.OR) {
            
            //lado 1
            //Processa causa
            if (cc.getCausa1_id() != 0) {
                if (cc.getRelacao_c1() == TipoRelacionamento.IDENTIDADE) {
                    valorCausas.put(cc.getCausa1_id(), valorSaida);
                    verificaEfeito(valorSaida, cc.getCausa1_id());
                } else if (cc.getRelacao_c1() == TipoRelacionamento.NOT) {
                    valorCausas.put(cc.getCausa1_id(), !valorSaida);
                    verificaEfeito(!valorSaida, cc.getCausa1_id());
                }
            } else if (cc.getCausa_composta1_id() != 0) { //Processa causa composta
                if (cc.getRelacao_c1() == TipoRelacionamento.IDENTIDADE) {
                    if (valorSaida) {
                        processaCaminho(valorSaida, causaCompostaPorId.get(cc.getCausa_composta1_id()));
                    } else {
                        processaCaminho(false, causaCompostaPorId.get(cc.getCausa_composta1_id()));
                    }
                } else if (cc.getRelacao_c1() == TipoRelacionamento.NOT) {
                    processaCaminho(true, causaCompostaPorId.get(cc.getCausa_composta1_id()));
                }
            } else {
                System.err.println("ERRO: Causa 1 ou Causa Composta 1 não setados");
            }
            
            //lado 2
            //Processa causa
            if (cc.getCausa2_id() != 0) {
                if (cc.getRelacao_c2() == TipoRelacionamento.IDENTIDADE) {
                    valorCausas.put(cc.getCausa2_id(), valorSaida);
                    verificaEfeito(valorSaida, cc.getCausa2_id());
                } else if (cc.getRelacao_c2() == TipoRelacionamento.NOT) {
                    valorCausas.put(cc.getCausa2_id(), !valorSaida);
                    verificaEfeito(!valorSaida, cc.getCausa2_id());
                }
            } else if (cc.getCausa_composta2_id() != 0) { //Process causa composta
                if (cc.getRelacao_c2() == TipoRelacionamento.IDENTIDADE) {
                    processaCaminho(valorSaida, causaCompostaPorId.get(cc.getCausa_composta2_id()));
                } else if (cc.getRelacao_c2() == TipoRelacionamento.NOT) {
                    processaCaminho(!valorSaida, causaCompostaPorId.get(cc.getCausa_composta2_id()));
                }
            } else {
                System.err.println("ERRO: Causa 2 ou Causa Composta 2 não setados");
            }
            
        }
        
        verificaEfeito(valorSaida, cc);
        
        return;
    
    }
    
    /**
     * Método que verifica se a causa composta atual é ligada a algum efeito
     * se for, marca o valor que esse efeito assume 
     * @param valorSaida Valor da saída da causa composta
     * @param cc Causa composta
     */
    private void verificaEfeito(Boolean valorSaida, CausaComposta cc){
        if (causaEfeitoPorCompostaId.containsKey(cc.getId())) {
            CausaEfeito ce = causaEfeitoPorCompostaId.get(cc.getId());
            if (ce.getTipo_relacionamento() == TipoRelacionamento.IDENTIDADE) {
                valorEfeitos.put(ce.getId_efeito(), valorSaida);
            } else if (ce.getTipo_relacionamento() == TipoRelacionamento.NOT) {
                valorEfeitos.put(ce.getId_efeito(), !valorSaida);
            }
        }
    }
    
    /**
     * Método que verifica se a causa atual é ligada a algum efeito
     * se for, marca o valor que esse efeito assume 
     * @param valorSaida Valor da saída da causa
     * @param idCausa id da causa
     */
    private void verificaEfeito(Boolean valorSaida, Integer idCausa){
        if (causaEfeitoPorCausaId.containsKey(idCausa)) {
            CausaEfeito ce = causaEfeitoPorCausaId.get(idCausa);
            if (ce.getTipo_relacionamento() == TipoRelacionamento.IDENTIDADE) {
                valorEfeitos.put(ce.getId_efeito(), valorSaida);
            } else if (ce.getTipo_relacionamento() == TipoRelacionamento.NOT) {
                valorEfeitos.put(ce.getId_efeito(), !valorSaida);
            }
        }
    }
    
}
