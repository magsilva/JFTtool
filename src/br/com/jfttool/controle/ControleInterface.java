/*
 * JFTtool - Java Functional Testing tool
 * Created: 03/06/2012
 * 
 * Created by:
 *      Alexandre Ponce de Oliveira - alexandreponce at icmc.usp.br
 *      Daniela Oliveira Francisco - doliveira at sc.usp.br
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
package br.com.jfttool.controle;

import br.com.jfttool.enums.TipoCriterio;
import br.com.jfttool.geracao.Geracao;
import br.com.jfttool.geracao.GeracaoValorLimite;
import br.com.jfttool.geracao.TabelaGrafo;
import br.com.jfttool.models.*;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JTable;
   
/**
 * @author JFTtool
 * Classe responsável pela comunicação entre Modelo e Visão dos da interface
 */
public class ControleInterface {
    private Projeto projeto_aberto;
    private ArrayList<DadoTeste> dados_teste_part;
    private ArrayList<DadoTeste> dados_teste_analise;
    private ArrayList<CasoTeste> casos_teste;

    /**
     * Construtor da classe
     */
    public ControleInterface() {
       this.projeto_aberto = null;
       this.dados_teste_part = new ArrayList<DadoTeste>();
       this.dados_teste_analise = new ArrayList<DadoTeste>();
    }
    
    /**
     * Método que recupera um projeto de teste do banco de dados
     * @param nome_projeto Nome do Projeto
     * @throws SQLException
     * @throws ClassNotFoundException
     * @return Boolean 
     */
    public Boolean recuperaProjetoBanco(String nome_projeto) throws SQLException, ClassNotFoundException{
        setProjeto_aberto(new Projeto(nome_projeto));
        obterDadosTesteClasses();
        obterDadosTesteValorLimite();
        return getProjeto_aberto().setIdByName();
    }
    
    /**
     * Adiciona uma entrada no banco e associa ao projeto
     * @param e Domínio de Entrada
     * @throws SQLException
     * @throws ClassNotFoundException
     * @return Integer 
     */
    public Integer addEntrada(Entrada e) throws SQLException, ClassNotFoundException{
        e.setId_projeto(this.getProjeto_aberto().getId());
        //salva a novaEntrada no banco
        e.save();
        //adicionar nova entrada no array de entradas
        getProjeto_aberto().addEntrada(e);
        //coloca o id da Entrada nas restrições
        for(int i=0; i < e.getRestricoes().size(); i++) {
            e.getRestricao(i).setId_entrada(e.getId());
        //salva no  banco
            e.getRestricao(i).save();
        }
        return e.getId();
    }
    
    /**
     * Adiciona uma restrição ao projeto de teste
     * @param id_variavel Id da entrada
     * @param r Restricao
     * @throws SQLException
     * @ClassNotFoundException
     */
    public void addRestricao(Integer id_variavel, Restricao r) throws SQLException, ClassNotFoundException{
        Restricao novaRest = new Restricao(id_variavel, r.getClasse(), r.getTipo(), r.getConteudo());
        novaRest.setId(novaRest.save());
            System.out.println(r.getTipo());
            System.out.println(r.getConteudo());        
    }
    
    /** 
     * Solicita a geração de dados de teste com o critério Particionamento em Classes 
     * @throws ClassNotFoundException
     * @see br.com.grupo1.geracao.Geracao
     */
    public void gerarDadosTesteClasses() throws ClassNotFoundException{
        try {
            Geracao.partEmClasses(getProjeto_aberto().getId());
            obterDadosTesteClasses();
        } catch (SQLException ex) {
            Logger.getLogger(ControleInterface.class.getName()).log(Level.SEVERE, null, ex);
        }    
    }
    
    /**
     * Obtém dados de teste do banco para Particionamento em Classes
     * @throws ClassNotFoundException
     * @throws SQLException 
     */
    public void obterDadosTesteClasses() throws ClassNotFoundException, SQLException {
        //recuperar do banco os dados de teste
        dados_teste_part = DadoTeste.getByCriterio(getProjeto_aberto().getId().toString(), TipoCriterio.PARTICIONAMENTO_CLASSES.toString());
        for(int i=0; i< dados_teste_part.size(); i++) {
            dados_teste_part.get(i).setDadosEntrada( DadoEntrada.getByColumn("id_dado_teste", dados_teste_part.get(i).getId().toString()) );
        }
    }
    
    /** 
     * Solicita a geração de dados de teste com o critério Análise do Valor Limite
     * @throws ClassNotFoundException
     * @see br.com.grupo1.geracao.GeracaoValorLimite
     */
    public void gerarDadosTesteValorLimite() throws ClassNotFoundException {
        try {
            GeracaoValorLimite.analiseValorLimite(getProjeto_aberto().getId());
            obterDadosTesteValorLimite();
        } catch(SQLException ex) {
            Logger.getLogger(ControleInterface.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    /**
     * Obtém dados de teste do banco de dados para Análise do Valor Limite
     * @throws ClassNotFoundException
     * @throws SQLException 
     */
    public void obterDadosTesteValorLimite() throws ClassNotFoundException, SQLException {
        //recuperar do banco os dados de teste
        dados_teste_analise = DadoTeste.getByCriterio(getProjeto_aberto().getId().toString(), TipoCriterio.ANALISE_VALOR_LIMITE.toString());
            for(int i=0; i< dados_teste_analise.size(); i++) {
                dados_teste_analise.get(i).setDadosEntrada( DadoEntrada.getByColumn("id_dado_teste", dados_teste_analise.get(i).getId().toString()) );
            }
    }
    
    /** 
     * Retorna os casos de teste do critério Análise do Valor Limite
     * @throws ClassNotFoundException
     * @throws SQLException
     * @see br.com.database.models.CasoTeste
     */
    public void obterCasosTeste(String criterio) throws ClassNotFoundException, SQLException{
        casos_teste = CasoTeste.getByCriterio(getProjeto_aberto().getId().toString(), criterio);
    }
    
    /** 
     * Retorna o projeto aberto
     * @return Projeto
     */
    public Projeto getProjeto_aberto() {
        return projeto_aberto;
    }

    /**
     * Atribui valor para projeto_aberto
     * @param projeto_aberto Projeto aberto
     */
    public void setProjeto_aberto(Projeto projeto_aberto) {
        this.projeto_aberto = projeto_aberto;
    }
    
    /**
     * Cria um novo projeto no banco
     * @param nome Nome do projeto
     * @see br.com.database.models.Projeto
     */
    public void newProjeto_aberto(String nome) {
        this.projeto_aberto = new Projeto(nome);
        try {
            this.projeto_aberto.save();
        } catch (SQLException ex) {
            Logger.getLogger(ControleInterface.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(ControleInterface.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Atribui valor para a lista de dados de teste
     * @param dados Lista de dados de teste
     */
    public void setDadosTesteClasses(ArrayList<DadoTeste> dados) {
        this.dados_teste_part = dados;
    }
    
    /**
     * Retorna os dados de teste para o Particionamento em Classes de Equivalência
     * @return ArrayList<DadoTeste>
     */
    public ArrayList<DadoTeste> getDadosTesteClasses(){
        return this.dados_teste_part;
    }
    
    /**
     * Atribui valor para os dados de teste da Análise Valor Limite
     * @param dados Lista de Dados de Teste da Análise Valor Limite
     */
    public void setDadosTesteAnalise(ArrayList<DadoTeste> dados) {
        this.dados_teste_analise = dados;
    }
    
    /**
     * Retorna a lista de dados de teste para Análiva Valor Limite
     * @return ArrayList<DadoTeste>
     */
    public ArrayList<DadoTeste> getDadosTesteAnalise(){
        return this.dados_teste_analise;
    }
    
    /**
     * Atribui valor para a lista de casos de teste para Particionamento em Classes
     * @param dados Lista de casos de teste para Particion. Classes
     */
    public void setCasosTeste(ArrayList<CasoTeste> dados) {
        this.casos_teste = dados;
    }
    
    /**
     * Retorna a lista de casos de teste para Particionamento em Classes
     * @return ArrayList<CasoTeste> 
     */
    public ArrayList<CasoTeste> getCasosTeste(){
        return this.casos_teste;
    }
        
    /**
     * Atualiza os dados da tabela de decisão na interface
     * @param causas Causas do Grafo
     * @param efeitos Efeitos do Grafo
     */
    public void atualizaTabelaDecisao(JTable causas, JTable efeitos) {
        TabelaGrafo tg = new TabelaGrafo();
        tg.geraTabela(getProjeto_aberto().getId());
        
        String[] colunas = new String[tg.getCausasTabela().size() + 1];
        
        for (int i = 0 ; i <= tg.getCausasTabela().size() ; i++) { 
            if (i == 0) {
                colunas[i] = "Causa";
            } else {
                colunas[i] = Integer.toString(i);
            }
        }
        String c[][] = new String[tg.getCausasTabela().get(0).size()][tg.getCausasTabela().size()+1];
        ArrayList<Causa> cs = tg.getCausas();
        for (int i=0 ; i<cs.size() ; i++) {
            c[i][0] = cs.get(i).getId().toString();
        }
        for (int i = 1 ; i < tg.getCausasTabela().size()+1 ; i++) { //colunas
            for (int j = 0 ; j < tg.getCausasTabela().get(0).size() ; j++) { //linhas
                c[j][i] = tg.getCausasTabela().get(i-1).get(j);
            }
        }
        javax.swing.table.DefaultTableModel modeloCausa = new javax.swing.table.DefaultTableModel(c, colunas);
        causas.setModel(modeloCausa);
        
        colunas[0] = "Efeito";
        
        c = new String[tg.getEfeitosTabela().get(0).size()][tg.getEfeitosTabela().size()+1];
        ArrayList<Efeito> es = tg.getEfeitos();
        for (int i=0 ; i<es.size() ; i++) {
            c[i][0] = es.get(i).getId().toString();
        }
        for (int i = 1 ; i < tg.getEfeitosTabela().size()+1 ; i++) { //colunas
            for (int j = 0 ; j < tg.getEfeitosTabela().get(0).size() ; j++) { //linhas
                c[j][i] = tg.getEfeitosTabela().get(i-1).get(j);
            }
        }
        
        javax.swing.table.DefaultTableModel modeloEfeito = new javax.swing.table.DefaultTableModel(c, colunas);
        efeitos.setModel(modeloEfeito);
    }

           
}

/*
 * Select para CASOS TESTE
 *  select * from caso_teste, dado_teste where caso_teste.id_projeto = 3 and dado_teste.id_projeto = 3 
    and dado_teste.criterio = 'ANALISE_VALOR_LIMITE' order by caso_teste.id
 */