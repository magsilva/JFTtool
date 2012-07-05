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
package br.com.jfttool.models;

import br.com.jfttool.connection.DbConn;
import br.com.jfttool.connection.DbModelInterface;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

/**
 * @author JFTtool Team
 * Classe que representa um efeito (do grafo causa-efeito) para ferramenta e 
 * permite acesso a tabela efeito no banco de dados
 */
public class Efeito implements DbModelInterface {
    
    public static final String NAME = "efeito";
    
    private Integer id;
    private Integer id_saida;
    private String nome;
    private Integer id_projeto;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getId_saida() {
        return id_saida;
    }

    public void setId_saida(Integer id_saida) {
        this.id_saida = id_saida;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
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
    public Efeito() {
    }

    /**
     * Construtor que recebe todos os parâmetros de um efeito e instancia a classe
     * @param id_entrada
     * @param nome
     * @param id_projeto 
     */
    public Efeito(Integer id_entrada, String nome, Integer id_projeto) {
        this.id_saida = id_entrada;
        this.nome = nome;
        this.id_projeto = id_projeto;
    }
    
    /**
     * Construtor que recebe uma Causa do banco (ResultSet) para preencher
     * a instância
     * @param row
     * @throws SQLException 
     */
    public Efeito(ResultSet row) throws SQLException {
        this.id = row.getInt("id");
        this.id_saida = row.getInt("id_saida");
        this.nome = row.getString("nome");
        this.id_projeto = row.getInt("id_projeto");
    }
       
    /**
     * Busca todas os registros de efeitos no banco de dados, instancia cada uma
     * e as retornar em um ArrayList
     * @return ArrayList<Efeito>
     * @throws SQLException
     * @throws ClassNotFoundException 
     */
    public static ArrayList<Efeito> getAll() throws SQLException, ClassNotFoundException {
        Statement stm = new DbConn().getStatement();
        String query = "select * from " + NAME + ";";
        ResultSet result = stm.executeQuery(query);
        ArrayList<Efeito> resultList = resultSetToObjects(result);
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
     * Obtêm todos os efeitos onde a coluna passada no parâmetro column 
     * possui o valor passado no parâmetro value e as retorna como um 
     * ArrayList de instâncias dessa classe.
     * @param column String com o nome da coluna
     * @param value String com o valor que a coluna deve possuir
     * @return ArrayList<Efeito>
     * @throws SQLException
     * @throws ClassNotFoundException 
     */
    public static ArrayList<Efeito> getByColumn(String column, String value) throws ClassNotFoundException, SQLException {
        Statement stm = new DbConn().getStatement();
        String query = "select * from " + NAME + " where " + column + "='" + value + "';";
        
        ResultSet result = stm.executeQuery(query);
        ArrayList<Efeito> resultList = resultSetToObjects(result);
        stm.close();
        return resultList;
    }

    /**
     * Salva a instância atual do efeito no banco de dados caso ele ainda não possua id.
     * Caso contrario atualiza todos os dados no efeito em que o id no banco é igual
     * ao attributo id da classe.
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
            query += "insert into " + NAME + "('id_saida', 'nome', 'id_projeto') values('" + this.id_saida + "', '" + this.nome + "', '" + this.id_projeto + "')";
            result = stm.executeUpdate(query);
            ResultSet keys = stm.getGeneratedKeys();
            keys.next();
            this.id = keys.getInt(1);
        } else {
            query += "update " + NAME + " set id_saida='" + this.id_saida + "', nome='" + this.nome + "', id_projeto='" + this.id_projeto + "' where id=" + this.id;
            result = stm.executeUpdate(query);
        }
        
        stm.close();
        return result;
    }

    /**
     * Transforma o resultado de uma query no banco (ResultSet) em um arraylist
     * de efeitos, ou seja, para cada registro do banco, instancia uma classe Efeito
     * @param rs ResultSet resultado de uma query no banco de dados
     * @return ArrayList<Efeito>
     * @throws SQLException 
     */
    public static ArrayList<Efeito> resultSetToObjects(ResultSet rs) throws SQLException {
        ArrayList<Efeito> resultList = new ArrayList<Efeito>();

        while(rs.next()) {
            Efeito newObj = new Efeito(rs);
            resultList.add(newObj);
        }
        
        return resultList;
    }

    
}
