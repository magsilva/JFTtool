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
 * Classe que representa um projeto para ferramenta e permite acesso
 * a tabela projeto no banco de dados
 */
public class Projeto implements DbModelInterface {
    
    public static final String NAME = "projeto";
    
    private Integer id;
    private String nome;
    private ArrayList<Entrada> entradas;
    private ArrayList<Saida> saidas;

        
    public void setId(int id) {
        this.id = id;
    }

    public void setNome(String name) {
        this.nome = name;
    }

    public Integer getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }
    
    public ArrayList<Entrada> getEntradas() {
        return entradas;
    }

    public void setEntradas(ArrayList<Entrada> entradas) {
        this.entradas = entradas;
    }
    
    public Entrada getEntrada(int i) {
        return this.entradas.get(i);
    }

    public void addEntrada(Entrada e){
        this.entradas.add(e);
    }
    
    public ArrayList<Saida> getSaidas() {
        return saidas;
    }

    public void setSaidas(ArrayList<Saida> saidas) {
        this.saidas = saidas;
    }
    
    public Saida getSaida(int i) {
        return this.saidas.get(i);
    }

    public void addSaida(Saida s){
        this.saidas.add(s);
    }

    /**
     * Construtor vazio, cria um projeto que ainda não existe no banco de dados
     * e mantem apenas na memória.
     */
    public Projeto() {
        this.entradas = new ArrayList<Entrada>();
        this.saidas = new ArrayList<Saida>();
    }
    
    /**
     * Cria um projeto já com o atributo nome configura e o mantém apenas 
     * na memória
     * @param nome String Nome do projeto
     */
    public Projeto(String nome) {
        this.nome = nome;
        this.entradas = new ArrayList<Entrada>();
        this.saidas = new ArrayList<Saida>();
    }
    
    /**
     * Cria um projeto com todos os seus atributos. Deve ser utilizado apenas
     * quando o projeto já existe no banco de dados pois o atributo id já deve
     * exisitir para esse projeto.
     * @param id Id do projeto (auto increment do banco de dados)
     * @param nome Nome do projeto
     * @param entradas lista de variáveis entradas para este projeto
     * @param saidas lista de variáveis saidas de saída para este projeto 
     */
    public Projeto(Integer id, String nome, ArrayList<Entrada> entradas, ArrayList<Saida> saidas) {
        this.id = id;
        this.nome = nome;
        this.entradas = entradas;
        this.saidas = saidas;
        this.entradas = new ArrayList<Entrada>();
        this.saidas = new ArrayList<Saida>();
    }
    
    /**
     * Cria um projeto em memória a partir de um registro do banco (ResultSet)
     * É necessário que o ponteiro do ResultSet esteja no projeto que se deseja
     * instanciar.
     * @param rs ResultSet com o ponteiro apontando para o registro do projeto que se deseja instanciar
     * @throws SQLException 
     */
    public Projeto(ResultSet rs) throws SQLException {
        this.id = rs.getInt("id");
        this.nome = rs.getString("nome");
    }
    
    
    /**
     * Recupera um projeto do banco por nome e assume ele
     * CUIDADO: os nomes de projetos não são únicos no banco de dados.
     * Prefira sempre fazer busca por ID
     * @return Boolean Indica sucesso (true) ou falha (false) no processo.
     */
    public Boolean setIdByName() throws SQLException, ClassNotFoundException{
        ArrayList<Projeto> projetos = getAll();
        for(int i=0;i<projetos.size();i++){
            if(projetos.get(i).getNome().equals(this.nome)){
                this.id=projetos.get(i).getId();
                return true;   
            }
        
        }
        return false;
    }
    
    /**
     * Busca todos os projetos no banco de dados e retonar um ArrayList de projetos
     * @return ArrayList<Projeto>
     * @throws SQLException
     * @throws ClassNotFoundException 
     */
    public static ArrayList<Projeto> getAll() throws SQLException, ClassNotFoundException {
        Statement stm = new DbConn().getStatement();
        String query = "select * from " + NAME + ";";
        ResultSet result = stm.executeQuery(query);
        ArrayList<Projeto> resultList = resultSetToObjects(result);
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
     * Obtêm todos os projetos onde a coluna passada no parâmetro column 
     * possui o valor passado no parâmetro value e retorna um ArrayList
     * de instâncias da desse classe.
     * @return ArrayList<Projeto>
     * @throws SQLException
     * @throws ClassNotFoundException 
     */
    public static ArrayList<Projeto> getByColumn(String column, String value) throws ClassNotFoundException, SQLException {
        Statement stm = new DbConn().getStatement();
        String query = "select * from " + NAME + " where " + column + "='" + value + "';";
        
        ResultSet result = stm.executeQuery(query);
        ArrayList<Projeto> resultList = resultSetToObjects(result);
        stm.close();
        return resultList;
    }
    
    /**
     * Salva a instância atual no banco de dados caso ela ainda não possua id.
     * Caso contrario atualiza todos os dados do projeto cujo id no banco é igual
     * ao attributo id da classe
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
            query += "insert into " + NAME + "('nome') values('" + this.nome + "')";
            result = stm.executeUpdate(query);
            ResultSet keys = stm.getGeneratedKeys();
            keys.next();
            this.id = keys.getInt(1);
        } else {
            query += "update " + NAME + " set nome='" + this.nome + "' where id=" + this.id;
            result = stm.executeUpdate(query);
        }
        
        stm.close();
        return result;
    }
    
    /**
     * Transforma o resultado de uma query no banco (ResultSet) em um arraylist
     * de projeto, ou seja, para cada registro do banco, instancia uma classe Projeto
     * @param rs ResultSet resutado de uma query no banco de dados
     * @return ArrayList<Projeto>
     * @throws SQLException 
     */
    public static ArrayList<Projeto> resultSetToObjects(ResultSet rs) throws SQLException {
        ArrayList<Projeto> resultList = new ArrayList<Projeto>();
        
        while(rs.next()) {
            Projeto newObj = new Projeto(rs);
            resultList.add(newObj);
        }
        
        return resultList;
    }

}
