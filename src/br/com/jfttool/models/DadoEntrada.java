
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
 * Classe que representa um valor gerado para uma determinada entrada que 
 * compõe um dado de teste.
 */
public class DadoEntrada implements DbModelInterface {
    
    public static final String NAME = "dado_entrada";
    
    private Integer id;
    private String valor;
    private Integer id_entrada;
    private Integer id_dado_teste;
    private Integer valido;

    public Integer getId_entrada() {
        return id_entrada;
    }

    public void setId_entrada(int id_entrada) {
        this.id_entrada = id_entrada;
    }

    public String getValor() {
        return valor;
    }

    public void setValor(String valor) {
        this.valor = valor;
    }

    public Integer getId() {
        return id;
    }
    
    public void setId(int id) {
        this.id = id;
    }

    public Integer getId_Dado_Teste() {
        return id_dado_teste;
    }

    public void setId_Dado_Teste(Integer id_dado_teste) {
        this.id_dado_teste = id_dado_teste;
    }

    public Integer getValido() {
        return valido;
    }

    public void setValido(Integer valido) {
        this.valido = valido;
    }
    /**
     * Construtor da classe
     */
    public DadoEntrada() {
    }
    
    /**
     * Cria um Dado de entrada com seus atributos.
     * @param valor Valor do dado
     * @param id_entrada Id da entrada associada
     * @param id_dado_teste Id do dado de teste associado
     * @param valido Especificação se é um dado de classe válida ou inválida
     */
    public DadoEntrada(String valor, Integer id_entrada, Integer id_dado_teste, Integer valido) {
        this.valor = valor;
        this.id_entrada = id_entrada;
        this.id_dado_teste = id_dado_teste;
        this.valido = valido;
    }
    
    public DadoEntrada(String valor, Integer id_entrada, Integer valido) {
        this.valor = valor;
        this.id_entrada = id_entrada;
        this.valido = valido;
    }
    /**
     * Cria um dado de entrada em memória a partir de um registro do banco (ResultSet).
     * É necessário que o ponteiro do ResultSet esteja no projeto que se deseja instanciar.
     * @param rs
     * @throws SQLException 
     */
    public DadoEntrada(ResultSet rs) throws SQLException {
        this.id = rs.getInt("id");
        this.valor = rs.getString("valor");
        this.id_entrada = rs.getInt("id_entrada");
        this.id_dado_teste = rs.getInt("id_dado_teste");
        this.valido = rs.getInt("valido");
    }
    /**
     * Busca todos os projetos no banco de dados e retonar um ArrayList de dados de entrada
     * @return
     * @throws SQLException
     * @throws ClassNotFoundException 
     */
    public static ArrayList<DadoEntrada> getAll() throws SQLException, ClassNotFoundException {
        Statement stm = new DbConn().getStatement();
        String query = "select * from " + NAME + ";";
        ResultSet result = stm.executeQuery(query);
        ArrayList<DadoEntrada> resultList = resultSetToObjects(result);
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
     * Obtêm todos os dados de entrada onde a coluna passada no parâmetro column 
     * possui o valor passado no parâmetro value e retorna um ArrayList de objetos
     * da classe.
     * @param column
     * @param value
     * @return
     * @throws ClassNotFoundException
     * @throws SQLException 
     */
    public static ArrayList<DadoEntrada> getByColumn(String column, String value) throws ClassNotFoundException, SQLException {
        Statement stm = new DbConn().getStatement();
        String query = "select * from " + NAME + " where " + column + "='" + value + "';";
        
        ResultSet result = stm.executeQuery(query);
        ArrayList<DadoEntrada> resultList = resultSetToObjects(result);
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
            query += "insert into " + NAME + "('valor', 'id_entrada', 'id_dado_teste', 'valido') " +
                    "values('" + this.valor + "', '" + this.id_entrada + "', '" + this.id_dado_teste + "', '" + this.valido + "')";
            result = stm.executeUpdate(query);
            ResultSet keys = stm.getGeneratedKeys();
            keys.next();
            this.id = keys.getInt(1);
        } else {
            query += "update " + NAME + " set valor='" + this.valor + "',id_entrada='" + this.id_entrada + "',id_dado_teste='" + this.id_dado_teste + "',valido='" + this.valido + "' where id=" + this.id;
            result = stm.executeUpdate(query);
        }
        
        stm.close();
        return result;
    }
    /**
     * Transforma o resultado de uma query no banco (ResultSet) em um arraylist
     * de projeto, ou seja, para cada registro do banco, instancia uma classe DadoEntrada
     * @param rs ResultSet resutado de uma query no banco de dados
     * @return ArrayList<Projeto>
     * @throws SQLException 
     */
    public static ArrayList<DadoEntrada> resultSetToObjects(ResultSet rs) throws SQLException {
        ArrayList<DadoEntrada> resultList = new ArrayList<DadoEntrada>();

        while(rs.next()) {
            DadoEntrada newObj = new DadoEntrada(rs);
            resultList.add(newObj);
        }
        
        return resultList;
    }
    
    @Deprecated // Use o getByColumn ao para queries simples como essa.
    public ArrayList<DadoEntrada> getByIdEntrada(int id) throws SQLException, ClassNotFoundException {
        DbConn connection = new DbConn();
        Statement stm = connection.getStatement();
        
        String query = "select * from " + NAME + " where id_entrada = " + id + ";";
        
        ResultSet result = stm.executeQuery(query);
        ArrayList<DadoEntrada> resultList = resultSetToObjects(result);
        stm.close();
        return resultList;
    }
    
}
