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
import br.com.jfttool.enums.TipoRelacionamento;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

/**
 * @author JFTtool
 * Classe que representa uma causa composta (do grafo causa-efeito) para ferramenta e 
 * permite acesso a tabela causa_composta no banco de dados
 */
public class CausaComposta implements DbModelInterface {

    public static final String NAME = "causa_composta";
    private Integer id;
    private Integer causa1_id;
    private Integer causa2_id;
    private Integer causa_composta1_id;
    private Integer causa_composta2_id;
    private TipoRelacionamento tipo_relacionamento;
    private TipoRelacionamento relacao_c1;
    private TipoRelacionamento relacao_c2;
    private Integer id_projeto;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getCausa1_id() {
        return causa1_id;
    }

    public void setCausa1_id(Integer causa1_id) {
        this.causa1_id = causa1_id;
    }

    public Integer getCausa2_id() {
        return causa2_id;
    }

    public void setCausa2_id(Integer causa2_id) {
        this.causa2_id = causa2_id;
    }

    public TipoRelacionamento getRelacao_c1() {
        return relacao_c1;
    }

    public void setRelacao_c1(TipoRelacionamento relacao_c1) {
        this.relacao_c1 = relacao_c1;
    }

    public TipoRelacionamento getRelacao_c2() {
        return relacao_c2;
    }

    public void setRelacao_c2(TipoRelacionamento relacao_c2) {
        this.relacao_c2 = relacao_c2;
    }

    public TipoRelacionamento getTipo_relacionamento() {
        return tipo_relacionamento;
    }

    public void setTipo_relacionamento(TipoRelacionamento tipo_relacionamento) {
        this.tipo_relacionamento = tipo_relacionamento;
    }

    public Integer getId_projeto() {
        return id_projeto;
    }

    public void setId_projeto(Integer id_projeto) {
        this.id_projeto = id_projeto;
    }

    public Integer getCausa_composta1_id() {
        return causa_composta1_id;
    }

    public void setCausa_composta1_id(Integer causa_composta1_id) {
        this.causa_composta1_id = causa_composta1_id;
    }

    public Integer getCausa_composta2_id() {
        return causa_composta2_id;
    }

    public void setCausa_composta2_id(Integer causa_composta2_id) {
        this.causa_composta2_id = causa_composta2_id;
    }

    /**
     * Construtor da classe
     */
    public CausaComposta() {
    }
    
    /**
     * Construtor que recebe todos os parâmetros de uma causaComposta e instancia a classe
     * @param causa1_id
     * @param causa2_id
     * @param causa_composta1_id
     * @param causa_composta2_id
     * @param tipo_relacionamento
     * @param relacao_c1
     * @param relacao_c2
     * @param id_projeto 
     */
    public CausaComposta(Integer causa1_id, Integer causa2_id, Integer causa_composta1_id, Integer causa_composta2_id, String tipo_relacionamento, String relacao_c1, String relacao_c2, Integer id_projeto) {
        this.causa1_id = causa1_id;
        this.causa2_id = causa2_id;
        this.causa_composta1_id = causa_composta1_id;
        this.causa_composta2_id = causa_composta2_id;
        this.tipo_relacionamento = TipoRelacionamento.valueOf(tipo_relacionamento);
        this.relacao_c1 = TipoRelacionamento.valueOf(relacao_c1);
        this.relacao_c2 = TipoRelacionamento.valueOf(relacao_c2);
        this.id_projeto = id_projeto;
    }

    /**
     * Construtor que recebe uma causa composta do banco (ResultSet) para preencher
     * a instância
     * @param rs
     * @throws SQLException 
     */
    public CausaComposta(ResultSet rs) throws SQLException {
        this.id = rs.getInt("id");
        this.causa1_id = rs.getInt("causa1_id");
        this.causa2_id = rs.getInt("causa2_id");
        this.causa_composta1_id = rs.getInt("causa_composta1_id");
        this.causa_composta2_id = rs.getInt("causa_composta2_id");
        this.tipo_relacionamento = TipoRelacionamento.valueOf(rs.getString("tipo_relacionamento"));
        this.relacao_c1 = TipoRelacionamento.valueOf(rs.getString("relacao_c1"));
        this.relacao_c2 = TipoRelacionamento.valueOf(rs.getString("relacao_c2"));
        this.id_projeto = rs.getInt("id_projeto");
    }

    /**
     * Busca todas os registros de causa compostass no banco de dados, instancia cada uma
     * e as retornar em um ArrayList
     * @return ArrayList<CausaComposta>
     * @throws SQLException
     * @throws ClassNotFoundException 
     */
    public static ArrayList<CausaComposta> getAll() throws SQLException, ClassNotFoundException {
        Statement stm = new DbConn().getStatement();
        String query = "select * from " + NAME + ";";
        ResultSet result = stm.executeQuery(query);
        ArrayList<CausaComposta> resultList = resultSetToObjects(result);
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
     * Obtêm todos as causas compostas onde a coluna passada no parâmetro column 
     * possui o valor passado no parâmetro value e as retorna como um 
     * ArrayList de instâncias dessa classe.
     * @param column String com o nome da coluna
     * @param value String com o valor que a coluna deve possuir
     * @return ArrayList<CausaComposta>
     * @throws SQLException
     * @throws ClassNotFoundException 
     */
    public static ArrayList<CausaComposta> getByColumn(String column, String value) throws ClassNotFoundException, SQLException {
        Statement stm = new DbConn().getStatement();
        String query = "select * from " + NAME + " where " + column + "='" + value + "';";

        ResultSet result = stm.executeQuery(query);
        ArrayList<CausaComposta> resultList = resultSetToObjects(result);
        stm.close();
        return resultList;
    }

    /**
     * Salva a instância atual da causa composta no banco de dados caso ela ainda não possua id.
     * Caso contrario atualiza todos os dados no causa composta em que o id no banco é igual
     * ao atributo id da classe.
     * Ao salvar um novo registro, o atributo id da instância é atualizada
     * @return int resultado da query que salva ou atualiza a tabela do banco de
     * dados
     * @throws SQLException
     * @throws ClassNotFoundException 
     */
    @Override
    public int save() throws SQLException, ClassNotFoundException {
        Statement stm = new DbConn().getStatement();
        String query = "";

        int result = -1;
        if (this.id == null) {
            query += "insert into " + NAME ;
            query +=" ('causa1_id', 'causa2_id', 'causa_composta1_id', 'causa_composta2_id', 'tipo_relacionamento', 'relacao_c1', 'relacao_c2', 'id_projeto') values('" ;
            query += this.causa1_id + "', '" + this.causa2_id + "', '" +this.causa_composta1_id + "', '" + this.causa_composta2_id + "', '" + this.tipo_relacionamento + "', '" + this.relacao_c1 + "', '" + this.relacao_c2 + "', '" + this.id_projeto + "')";
            
            
            result = stm.executeUpdate(query);
            ResultSet keys = stm.getGeneratedKeys();
            keys.next();
            this.id = keys.getInt(1);
        } else {
            query += "update " + NAME + " set causa1_id='" + this.causa1_id + "',causa2_id='" + this.causa2_id + "', causa_composta1_id='" + this.causa_composta1_id + "',causa_composta2_id='" + this.causa_composta2_id + "', tipo_relacionamento='" + this.tipo_relacionamento + "', relacao_c1='" + this.relacao_c1 + "', relacao_c2='" + this.relacao_c2 + "', id_projeto='" + this.id_projeto + "' where id=" + this.id;
            result = stm.executeUpdate(query);
        }

        stm.close();
        return result;
    }

    /**
     * Transforma o resultado de uma query no banco (ResultSet) em um arraylist
     * de causas compostas, ou seja, para cada registro do banco, instancia uma classe CausaComposta
     * @param rs ResultSet resutado de uma query no banco de dados
     * @return ArrayList<CausaComposta>
     * @throws SQLException 
     */
    public static ArrayList<CausaComposta> resultSetToObjects(ResultSet rs) throws SQLException {
        ArrayList<CausaComposta> resultList = new ArrayList<CausaComposta>();

        while (rs.next()) {
            CausaComposta newObj = new CausaComposta(rs);
            resultList.add(newObj);
        }

        return resultList;
    }
}
