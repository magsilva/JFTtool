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
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

/**
 * @author JFTtool Team
 * Classe que representa uma causa (do grafo causa-efeito) para ferramenta e 
 * permite acesso a tabela causa no banco de dados
 */
public class Causa implements DbModelInterface {
    
    public static final String NAME = "causa";
    
    private Integer id;
    private Integer id_entrada;
    private String nome;
    private String valor;
    private Integer id_projeto;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getId_entrada() {
        return id_entrada;
    }

    public void setId_entrada(Integer id_entrada) {
        this.id_entrada = id_entrada;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getValor() {
        return valor;
    }

    public void setValor(String valor) {
        this.valor = valor;
    }

    public Integer getId_projeto() {
        return id_projeto;
    }

    public void setId_projeto(Integer id_projeto) {
        this.id_projeto = id_projeto;
    }

    public Causa() {
    }

    /**
     * Construtor que recebe todos os parâmetros da uma causa e instancia a classe
     * @param id_entrada
     * @param nome
     * @param valor
     * @param id_projeto 
     */
    public Causa(Integer id_entrada, String nome, String valor, Integer id_projeto) {
        this.id_entrada = id_entrada;
        this.nome = nome;
        this.valor = valor;
        this.id_projeto = id_projeto;
    }

    /**
     * Construtor que recebe uma Causa do banco (ResultSet) para preencher
     * a instância
     * @param row ResultSet
     * @throws SQLException 
     */
    public Causa(ResultSet row) throws SQLException {
        this.id = row.getInt("id");
        this.id_entrada = row.getInt("id_entrada");
        this.nome = row.getString("nome");
        this.valor = row.getString("valor");
        this.id_projeto = row.getInt("id_projeto");
    }
    
    /**
     * Busca todas os registros de causas no banco de dados, instancia cada uma
     * e as retornar em um ArrayList
     * @return ArrayList<Causa>
     * @throws SQLException
     * @throws ClassNotFoundException 
     */
    public static ArrayList<Causa> getAll() throws SQLException, ClassNotFoundException {
        Statement stm = new DbConn().getStatement();
        String query = "select * from " + NAME + ";";
        
        ResultSet result = stm.executeQuery(query);
        ArrayList<Causa> resultList = resultSetToObjects(result);
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
        ResultSetMetaData metadata = rs.getMetaData();
        return metadata;
    }
    
    /**
     * Obtêm todos as causas onde a coluna passada no parâmetro column 
     * possui o valor passado no parâmetro value e as retorna como um 
     * ArrayList de instâncias dessa classe.
     * @return ArrayList<Causa>
     * @throws SQLException
     * @throws ClassNotFoundException 
     */
    public static ArrayList<Causa> getByColumn(String column, String value) throws ClassNotFoundException, SQLException {
        Statement stm = new DbConn().getStatement();
        String query = "select * from " + NAME + " where " + column + "='" + value + "';";
        
        ResultSet result = stm.executeQuery(query);
        ArrayList<Causa> resultList = resultSetToObjects(result);
        stm.close();
        return resultList;
    }

     /**
     * Salva a instância atual da causa no banco de dados caso ela ainda não possua id.
     * Caso contrario atualiza todos os dados na causa em que o id no banco é igual
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
            query += "insert into " + NAME + "('id_entrada', 'nome', 'valor', 'id_projeto') values('" + this.id_entrada + "', '" + this.nome + "', '"+ this.valor +"', '"  + this.id_projeto + "')";
            result = stm.executeUpdate(query);
            ResultSet keys = stm.getGeneratedKeys();
            keys.next();
            this.id = keys.getInt(1);
        } else {
            query += "update " + NAME + " set id_entrada='" + this.id_entrada + "', nome='" + this.nome + "', valor='" + this.valor + "', id_projeto='" + this.id_projeto + "' where id=" + this.id;
            result = stm.executeUpdate(query);
        }
        
        stm.close();
        return result;
    }

    /**
     * Transforma o resultado de uma query no banco (ResultSet) em um arraylist
     * de causas, ou seja, para cada registro do banco, instancia uma classe Causa
     * @param rs ResultSet resutado de uma query no banco de dados
     * @return ArrayList<Causa>
     * @throws SQLException 
     */
    public static ArrayList<Causa> resultSetToObjects(ResultSet rs) throws SQLException {
        ArrayList<Causa> resultList = new ArrayList<Causa>();

        while(rs.next()) {
            Causa newObj = new Causa(rs);
            resultList.add(newObj);
        }
        
        return resultList;
    }

    
}
