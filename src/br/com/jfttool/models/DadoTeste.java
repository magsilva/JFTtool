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

package br.com.jfttool.models;

import br.com.jfttool.connection.DbConn;
import br.com.jfttool.connection.DbModelInterface;
import br.com.jfttool.enums.TipoCriterio;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;


/**
 * @author JFTtool Team
 * Classe responsável por gerenciar os dados de teste e acessar no banco de dados
 */
public class DadoTeste implements DbModelInterface {
    
    public static final String NAME = "dado_teste";
    
    private Integer id;
    private TipoCriterio criterio;
    private Integer id_projeto;
    private ArrayList<DadoEntrada> dados_entrada;
    
    public void setId(int id) {
        this.id = id;
    }
    
    public Integer getId() {
        return id;
    }

    public TipoCriterio getCriterio() {
        return criterio;
    }

    public void setCriterio(TipoCriterio criterio) {
        this.criterio = criterio;
    }
    
    public void setProjeto(int projeto) {
        this.id_projeto = projeto;
    }
    
    public Integer getProjeto() {
        return id_projeto;
    }
    
    public void setDadosEntrada(ArrayList<DadoEntrada> dados) {
        this.dados_entrada = dados;
    }
    
    public ArrayList<DadoEntrada> getDadosEntrada() {
        return dados_entrada;
    }
    
    public DadoEntrada getDadoEntrada(int i) {
        return this.dados_entrada.get(i);
    }

    public void addDadoEntrada(DadoEntrada d){
        this.dados_entrada.add(d);
    }


    /**
     * Construtor da classe
     */
    public DadoTeste() {
    }
    
    /**
     * Construtor da classe que recebe todos os parâmetros e instancia a classe
     * @param criterio
     * @param id_projeto 
     */
    public DadoTeste(TipoCriterio criterio, Integer id_projeto) {
        this.criterio = criterio;
        this.id_projeto = id_projeto;
    }
    /**
     * Cria um dado de teste em memória a partir de um registro do banco (ResultSet).
     * É necessário que o ponteiro do ResultSet esteja no projeto que se deseja instanciar.      
     * @param rs
     * @throws SQLException 
     */
    public DadoTeste(ResultSet rs) throws SQLException {
        this.id = rs.getInt("id");
        this.criterio = TipoCriterio.valueOf(rs.getString("criterio"));
        this.id_projeto = rs.getInt("id_projeto");
    }
    
    /**
     * Busca todos os projetos no banco de dados e retonar um ArrayList de dados de teste. 
     * @return
     * @throws SQLException
     * @throws ClassNotFoundException 
     */
    public static ArrayList<DadoTeste> getAll() throws SQLException, ClassNotFoundException {
        Statement stm = new DbConn().getStatement();
        String query = "select * from " + NAME + ";";
        ResultSet result = stm.executeQuery(query);
        ArrayList<DadoTeste> resultList = resultSetToObjects(result);
        stm.close();
        return resultList;
    }
    /**
     * Obtêm os metadados da tabela no banco de dados
     * @return ResultSetMetaData
     * @throws SQLException
     * @throws ClassNotFoundException 
     */
    public static ResultSetMetaData getMetaData() throws SQLException, ClassNotFoundException {
        Statement stm = new DbConn().getStatement();
        String query = "select * from " + NAME + ";";
        ResultSet rs = stm.executeQuery(query);
        return rs.getMetaData();
    }
    /**
     * Obtêm todos os dados de teste onde a coluna passada no parâmetro column 
     * possui o valor passado no parâmetro value e retorna um ArrayList de objetos
     * da classe.
     * @param column
     * @param value
     * @return
     * @throws ClassNotFoundException
     * @throws SQLException 
     */
    public static ArrayList<DadoTeste> getByColumn(String column, String value) throws ClassNotFoundException, SQLException {
        Statement stm = new DbConn().getStatement();
        String query = "select * from " + NAME + " where " + column + "='" + value + "';";
        
        ResultSet result = stm.executeQuery(query);
        ArrayList<DadoTeste> resultList = resultSetToObjects(result);
        stm.close();
        return resultList;
    }
    
    /**
     * Busca todos os dados de teste com o id e criterio passados no parâmetro
     * @param id
     * @param criterio
     * @return ArrayList<DadoTeste>
     * @throws ClassNotFoundException
     * @throws SQLException 
     */
    public static ArrayList<DadoTeste> getByCriterio(String id, String criterio) throws ClassNotFoundException, SQLException {
        Statement stm = new DbConn().getStatement();
        String query = "select * from " + NAME + " where id_projeto = " + id + " and criterio = '" 
                + criterio + "';";
        ResultSet result = stm.executeQuery(query);
        ArrayList<DadoTeste> resultList = resultSetToObjects(result);
        stm.close();
        return resultList;
    }
    
    /**
     * Salva a instância atual no banco de dados caso ela ainda não possua id.
     * Caso contrario, atualiza todos os dados do projeto cujo id no banco é igual
     * ao attributo id da classe.
     * Ao salvar um novo registro, o atributo id da instância é atualizada
     * @return int resultado da query que salva ou atualiza a tabela do banco de dados
     * @throws SQLException
     * @throws ClassNotFoundException 
     */
    @Override
    public int save() throws SQLException, ClassNotFoundException {
        Statement stm = new DbConn().getStatement();
        String query = "";
        
        int result = -1;
        if (this.id == null) {
            query += "insert into " + NAME + "('criterio', 'id_projeto') values('" + this.getCriterio() + "', '" + this.id_projeto + "')";
            result = stm.executeUpdate(query);
            ResultSet keys = stm.getGeneratedKeys();
            keys.next();
            this.id = keys.getInt(1);
        } else {
            query += "update " + NAME + " set criterio='" + this.getCriterio() + "',id_projeto='" + this.getProjeto() + "' where id=" + this.id;
            result = stm.executeUpdate(query);
        }
        
        stm.close();
        return result;
    }
    
    /**
     * Transforma o resultado de uma query no banco (ResultSet) em um arraylist
     * de dados de teste, ou seja, para cada registro do banco, instancia uma classe DadoTeste
     * @param rs
     * @return ArrayList<DadoTeste>
     * @throws SQLException 
     */
    public static ArrayList<DadoTeste> resultSetToObjects(ResultSet rs) throws SQLException {
        ArrayList<DadoTeste> resultList = new ArrayList<DadoTeste>();
        
        while(rs.next()) {
            DadoTeste newObj = new DadoTeste(rs);
            resultList.add(newObj);
        }
        
        return resultList;
    }

}
