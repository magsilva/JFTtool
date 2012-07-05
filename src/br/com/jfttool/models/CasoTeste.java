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
 * Classe responsável por gerenciar os casos de teste
 */
public class CasoTeste implements DbModelInterface {
    
    public static final String NAME = "caso_teste";
    
    private Integer id;
    private Integer id_saida;
    private Integer id_dado_teste;
    private Integer valido;
    private Integer id_projeto;

    /**
     * Retorna id do caso de teste
     * @return Integer
     */   
    public Integer getId() {
        return id;
    }
    
    /**
     * Atribui valor para id
     * @param id 
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Retorna id do dado de teste associado ao caso de teste
     * @return Integer
     */
    public Integer getId_Dado_Teste() {
        return id_dado_teste;
    }

    /**
     * Atribui valor para id_dado_teste
     * @param id_dado_teste 
     */
    public void setId_Dado_Teste(Integer id_dado_teste) {
        this.id_dado_teste = id_dado_teste;
    }

    /**
     * Retorna se o caso de teste é para restrição válida ou invalida
     * @return Integer
     */
    public Integer getValido() {
        return valido;
    }

    /**
     * Atribui valor para variável valido
     * @param valido 
     */
    public void setValido(Integer valido) {
        this.valido = valido;
    }

    /**
     * Retorna id_dado_teste
     * @return Integer
     */
    public Integer getId_dado_teste() {
        return id_dado_teste;
    }

    /**
     * Atribui valor para id_dado_teste
     * @param id_dado_teste
     */
    public void setId_dado_teste(Integer id_dado_teste) {
        this.id_dado_teste = id_dado_teste;
    }

    /**
     * Retorna id_saida
     * @return Integer
     */
    public Integer getId_saida() {
        return id_saida;
    }

    /**
     * Atribui valor para id_saida
     * @param id_saida 
     */
    public void setId_saida(Integer id_saida) {
        this.id_saida = id_saida;
    }
    
    /**
     * Retorna valor do id_projeto
     * @return Integer
     */
    public Integer getId_projeto() {
        return id_projeto;
    }

    /**
     * Atribui valor para id_projeto
     * @param id_projeto
     */
    public void setId_projeto(Integer id_projeto) {
        this.id_projeto = id_projeto;
    }

    /**
     * Construtor da classe
     */
    public CasoTeste() {
    }

    /**
     * Construtor que recebe todos os parâmetros de um caso de teste e cria a classe
     * @param id_saida Id do domínio de saída
     * @param  id_dado_teste Id do Dado de teste
     * @param valido Se o dado de teste é válido ou inválido
     * @param id_projeto Id do projeto
     */
    public CasoTeste(Integer id_saida, Integer id_dado_teste, Integer valido, Integer id_projeto) {
        this.id_saida = id_saida;
        this.id_dado_teste = id_dado_teste;
        this.valido = valido;
        this.id_projeto = id_projeto;
    }
    
    /**
     * Cria um dado de teste em memória a partir de um registro do banco (ResultSet).
     * É necessário que o ponteiro do ResultSet esteja no projeto que se deseja instanciar.      
     * @param rs ResultSet com um registro do banco de dados
     * @throws SQLException 
     */
    public CasoTeste(ResultSet rs) throws SQLException {
        this.id = rs.getInt("id");
        this.id_saida = rs.getInt("id_saida");
        this.id_dado_teste = rs.getInt("id_dado_teste");
        this.valido = rs.getInt("valido");
        this.id_projeto = rs.getInt("id_projeto");
    }
    
    /**
     * Busca todos os casos de teste do banco de dados e retorna uma lista com todos eles.
     * @return ArrayList<CasoTeste>
     * @throws SQLException
     * @throws ClassNotFoundException 
     */
    public static ArrayList<CasoTeste> getAll() throws SQLException, ClassNotFoundException {
        Statement stm = new DbConn().getStatement();
        String query = "select * from " + NAME + ";";
        ResultSet result = stm.executeQuery(query);
        ArrayList<CasoTeste> resultList = resultSetToObjects(result);
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
     * @return ArrayList<CasoTeste>
     * @throws ClassNotFoundException
     * @throws SQLException 
     */
    public static ArrayList<CasoTeste> getByColumn(String column, String value) throws ClassNotFoundException, SQLException {
        Statement stm = new DbConn().getStatement();
        String query = "select * from " + NAME + " where " + column + "='" + value + "';";
        
        ResultSet result = stm.executeQuery(query);
        ArrayList<CasoTeste> resultList = resultSetToObjects(result);
        stm.close();
        return resultList;
    }
    
    /**
     * Obtêm todos os dados de teste para o id_projeto e criterio passados como parâmetros
     * e retorna um ArrayList de objetos da classe.
     * @param column
     * @param value
     * @return ArrayList<CasoTeste>
     * @throws ClassNotFoundException
     * @throws SQLException 
     */
    public static ArrayList<CasoTeste> getByCriterio(String id_projeto, String criterio) throws ClassNotFoundException, SQLException {
        Statement stm = new DbConn().getStatement();
        String query = "select c.* from caso_teste AS c, dado_teste AS d where c.id_projeto = " + id_projeto + " and d.id_projeto = " + 
                id_projeto + " and d.criterio = '" + criterio + "' order by c.id ;";
        
        ResultSet result = stm.executeQuery(query);
        ArrayList<CasoTeste> resultList = resultSetToObjects(result);
        stm.close();
        return resultList;
    }
            
    /**
     * Salva a instância atual no banco de dados caso ela ainda não possua id.
     * Caso contrario, atualiza todos os dados do projeto cujo id no banco é igual
     * ao attributo id da classe.
     * Ao salvar um novo registro, o atributo id da instância é atualizada
     * @return int
     * @throws SQLException
     * @throws ClassNotFoundException 
     */
    @Override
    public int save() throws SQLException, ClassNotFoundException {
        Statement stm = new DbConn().getStatement();
        String query = "";
        
        int result = -1;
        if (this.id == null) {
            query += "insert into " + NAME + "('id_saida', 'id_dado_teste', 'valido', id_projeto) " +
                    "values('" + this.id_saida + "', '" + this.id_dado_teste + "', '" + this.valido + 
                    "', '" + this.id_projeto +"')";
            result = stm.executeUpdate(query);
            ResultSet keys = stm.getGeneratedKeys();
            keys.next();
            this.id = keys.getInt(1);
        } else {
            query += "update " + NAME + "',id_saida='" + this.id_saida + "',id_dado_teste='" + 
                    this.id_dado_teste + "',valido='" + this.valido + "',id_projeto='" + this.id_projeto 
                    + "' where id=" + this.id;
            result = stm.executeUpdate(query);
        }
        
        stm.close();
        return result;
    }
    
    /**
     * Transforma o resultado de uma query no banco (ResultSet) em um arraylist
     * de casos de teste, ou seja, para cada registro do banco, instancia uma classe CasoTeste
     * @param rs
     * @return
     * @throws SQLException 
     */
    public static ArrayList<CasoTeste> resultSetToObjects(ResultSet rs) throws SQLException {
        ArrayList<CasoTeste> resultList = new ArrayList<CasoTeste>();

        while(rs.next()) {
            CasoTeste newObj = new CasoTeste(rs);
            resultList.add(newObj);
        }
        
        return resultList;
    }
    
    
}

