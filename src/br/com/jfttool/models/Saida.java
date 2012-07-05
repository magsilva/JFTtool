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
 * Classe que representa uma saída do domínio de saída para um projeto de teste
 * a tabela projeto no banco de dados.
 */
public class Saida implements DbModelInterface {
    
    public static final String NAME = "saida";
    private Integer id;
    private Integer id_projeto;
    private String nome, conteudo;
    private String valida;

    public String getConteudo() {
        return conteudo;
    }

    public void setConteudo(String conteudo) {
        this.conteudo = conteudo;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getId_projeto() {
        return id_projeto;
    }

    public void setId_projeto(Integer id_projeto) {
        this.id_projeto = id_projeto;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getValida() {
        return valida;
    }

    public void setValida(String valida) {
        this.valida = valida;
    }
    /**
     * Construtor vazio.
     */
    public Saida() {
    }
    /**
     * Construtor padrao de saída.
     * @param id_projeto Id do projeto de teste
     * @param nome Um nome para a saída
     * @param conteudo A descrição da saída
     * @param valida Verificador se a saída é válida ou inválida 
     */
    public Saida(Integer id_projeto, String nome, String conteudo, String valida) {
        this.id_projeto = id_projeto;
        this.nome = nome;
        this.conteudo = conteudo;
        this.valida = valida;
    }
    public Saida(String nome, String conteudo, String valida) {
        this.nome = nome;
        this.conteudo = conteudo;
        this.valida = valida;
    }
    /**
     * Cria uma saida em memória a partir de um registro do banco (ResultSet).
     * É necessário que o ponteiro do ResultSet esteja no projeto que se deseja instanciar.
     * @param rs
     * @throws SQLException 
     */
    public Saida(ResultSet rs) throws SQLException {
        this.id = rs.getInt("id");
        this.id_projeto = rs.getInt("id_projeto");
        this.nome = rs.getString("nome");
        this.conteudo = rs.getString("conteudo");
        this.valida = rs.getString("valida");
    }
    
    /**
     * Busca todos os projetos no banco de dados e retonar um ArrayList de saídas
     * @return
     * @throws SQLException
     * @throws ClassNotFoundException 
     */
    public static ArrayList<Saida> getAll() throws SQLException, ClassNotFoundException {
        Statement stm = new DbConn().getStatement();
        String query = "select * from " + NAME + ";";
        ResultSet result = stm.executeQuery(query);
        ArrayList<Saida> resultList = resultSetToObjects(result);
        stm.close();
        return resultList;
    }
    
    /**
     * Obtêm os metadados da tabela no banco de dados
     * @return
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
     * Obtêm todas as saídas onde a coluna passada no parâmetro column 
     * possui o valor passado no parâmetro value e retorna um ArrayList de objetos
     * da classe.
     * @param column
     * @param value
     * @return
     * @throws ClassNotFoundException
     * @throws SQLException 
     */
    public static ArrayList<Saida> getByColumn(String column, String value) throws ClassNotFoundException, SQLException {
        Statement stm = new DbConn().getStatement();
        String query = "select * from " + NAME + " where " + column + "='" + value + "';";
        
        ResultSet result = stm.executeQuery(query);
        ArrayList<Saida> resultList = resultSetToObjects(result);
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
            query += "insert into " + NAME + "('id_projeto', 'nome', 'conteudo', 'valida') " +
                    "values('" + this.id_projeto + "', '" + this.nome + "', '" + this.conteudo + "', '" + this.valida + "')";
            result = stm.executeUpdate(query);
            ResultSet keys = stm.getGeneratedKeys();
            keys.next();
            this.id = keys.getInt(1);
        } else {
            query += "update " + NAME + " set id_projeto='" + this.id_projeto + "',nome='" + this.nome + "',conteudo='" + 
                    this.conteudo + "',valida='" + this.valida + "' where id=" + this.id;
            result = stm.executeUpdate(query);
        }
        
        stm.close();
        return result;
    }
    /**
     * Transforma o resultado de uma query no banco (ResultSet) em um arraylist
     * de projeto, ou seja, para cada registro do banco, instancia uma classe Saida
     * @param rs ResultSet resutado de uma query no banco de dados
     * @return ArrayList<Projeto>
     * @throws SQLException 
     */
    public static ArrayList<Saida> resultSetToObjects(ResultSet rs) throws SQLException {
        ArrayList<Saida> resultList = new ArrayList<Saida>();
        
        while(rs.next()) {
            Saida newObj = new Saida(rs);
            resultList.add(newObj);
        }
        
        return resultList;
    }


}
