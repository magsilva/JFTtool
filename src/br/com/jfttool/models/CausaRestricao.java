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
import br.com.jfttool.enums.TipoRestricaoGrafo;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

/**
 * @author JFTtool
 * Classe que representa uma restrição de causa (do grafo causa-efeito) para ferramenta e 
 * permite acesso a tabela causa_restricao no banco de dados
 */
public class CausaRestricao implements DbModelInterface {
    
    public static final String NAME = "causa_restricao";
    
    private Integer id;
    private Integer id_causa;
    private TipoRestricaoGrafo id_restricao;
    private Integer grupo;
    private Integer consequencia;
    private Integer id_projeto;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getId_causa() {
        return id_causa;
    }

    public void setId_causa(Integer id_causa) {
        this.id_causa = id_causa;
    }

    public TipoRestricaoGrafo getId_restricao() {
        return this.id_restricao;
    }

    public void setId_restricao(String id_restricao) {
        this.id_restricao = TipoRestricaoGrafo.valueOf(id_restricao);
    }

    public Integer getConsequencia() {
        return consequencia;
    }

    public void setConsequencia(Integer consequencia) {
        this.consequencia = consequencia;
    }

    public Integer getGrupo() {
        return grupo;
    }

    public void setGrupo(Integer grupo) {
        this.grupo = grupo;
    }

    public Integer getId_projeto() {
        return id_projeto;
    }

    public void setId_projeto(Integer id_projeto) {
        this.id_projeto = id_projeto;
    }

    /**
     * Construtor da classe
     */
    public CausaRestricao() {
    }

    /**
     * Construtor que recebe todos os parâmetros de uma causaRestricao e instancia a classe
     * @param id_efeito
     * @param id_restricao
     * @param grupo
     * @param consequencia
     * @param id_projeto 
     */
    public CausaRestricao(Integer id_causa, String id_restricao, Integer grupo, Integer consequencia, Integer id_projeto) {
        this.id_causa = id_causa;
        this.id_restricao = TipoRestricaoGrafo.valueOf(id_restricao);
        this.grupo = grupo;
        this.consequencia = consequencia;
        this.id_projeto = id_projeto;
    }
    
    /**
     * Construtor que recebe uma restrição de causa do banco (ResultSet) para preencher
     * a instância
     * @param rs
     * @throws SQLException 
     */
    public CausaRestricao(ResultSet rs) throws SQLException {
        this.id = rs.getInt("id");
        this.id_causa = rs.getInt("id_causa");
        this.id_restricao = TipoRestricaoGrafo.valueOf(rs.getString("id_restricao"));
        this.grupo = rs.getInt("grupo");
        this.consequencia = rs.getInt("consequencia");
        this.id_projeto = rs.getInt("id_projeto");
    }
    
    /**
     * Busca todas os registros de restrições de efeitos no banco de dados, instancia cada uma
     * e as retornar em um ArrayList
     * @return ArrayList<CausaRestricao>
     * @throws SQLException
     * @throws ClassNotFoundException 
     */
    public static ArrayList<CausaRestricao> getAll() throws SQLException, ClassNotFoundException {
        Statement stm = new DbConn().getStatement();
        String query = "select * from " + NAME + ";";
        ResultSet result = stm.executeQuery(query);
        ArrayList<CausaRestricao> resultList = resultSetToObjects(result);
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
     * Obtêm todos as restrições de causa onde a coluna passada no parâmetro column 
     * possui o valor passado no parâmetro value e as retorna como um 
     * ArrayList de instâncias dessa classe.
     * @param column String com o nome da coluna
     * @param value String com o valor que a coluna deve possuir
     * @return ArrayList<CausaRestricao>
     * @throws SQLException
     * @throws ClassNotFoundException 
     */
    public static ArrayList<CausaRestricao> getByColumn(String column, String value) throws ClassNotFoundException, SQLException {
        return getByColumn(column, value, "");
    }
    
    /**
     * Obtêm todos as restrições de causa onde a coluna passada no parâmetro column 
     * possui o valor passado no parâmetro value e as retorna como um 
     * ArrayList de instâncias dessa classe.
     * @param column String com o nome da coluna
     * @param value String com o valor que a coluna deve possuir
     * @param options String com sintaxe SQL para ser adicionada ao final da query como um "order by colum" por exemplo
     * @return ArrayList<EfeitoRestricao>
     * @throws SQLException
     * @throws ClassNotFoundException 
     */
    public static ArrayList<CausaRestricao> getByColumn(String column, String value, String options) throws ClassNotFoundException, SQLException {
        Statement stm = new DbConn().getStatement();
        String query = "select * from " + NAME + " where " + column + "='" + value + "' " + options + ";";
        
        ResultSet result = stm.executeQuery(query);
        ArrayList<CausaRestricao> resultList = resultSetToObjects(result);
        stm.close();
        return resultList;
    }
    
    /**
     * Salva a instância atual da causaRestricao no banco de dados caso ela ainda não possua id.
     * Caso contrario atualiza todos os dados no causaRestrica em que o id no banco é igual
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
            query += "insert into " + NAME + "('id_causa', 'id_restricao', 'grupo', 'consequencia', 'id_projeto') values('" + this.id_causa + "', '" + this.id_restricao + "', '" + this.grupo + "', '" + this.consequencia + "', '" + this.id_projeto + "')";
            result = stm.executeUpdate(query);
            ResultSet keys = stm.getGeneratedKeys();
            keys.next();
            this.id = keys.getInt(1);
        } else {
            query += "update " + NAME + " set id_causa='" + this.id_causa + "',id_restricao='" + this.id_restricao + "',grupo='" + this.grupo + "',consequencia='" + this.consequencia + "',id_projeto='" + this.id_projeto + "' where id=" + this.id;
            result = stm.executeUpdate(query);
        }
        
        stm.close();
        return result;
    }
    
    /**
     * Transforma o resultado de uma query no banco (ResultSet) em um arraylist
     * de efeitoRestricao, ou seja, para cada registro do banco, instancia uma classe CausaRestricao
     * @param rs ResultSet resutado de uma query no banco de dados
     * @return ArrayList<CausaRestricao>
     * @throws SQLException 
     */
    public static ArrayList<CausaRestricao> resultSetToObjects(ResultSet rs) throws SQLException {
        ArrayList<CausaRestricao> resultList = new ArrayList<CausaRestricao>();
        
        while(rs.next()) {
            CausaRestricao newObj = new CausaRestricao(rs);
            resultList.add(newObj);
        }
        
        return resultList;
    }

}
