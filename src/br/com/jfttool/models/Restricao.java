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
import br.com.jfttool.enums.TipoEntrada;
import br.com.jfttool.enums.TipoRestricao;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;



/**
 * @author JFTtool Team
 * Classe que gera dados de teste para restrições de entrada tipo Strin0g.
 */
public class Restricao implements DbModelInterface {
    
    public static final String NAME = "restricao";
    
    private Integer id;
    private Integer id_entrada;
    private Integer id_saida;
    private String classe; //Válida ou Inválida
    private TipoRestricao tipo;
    private String conteudo;

    public String getClasse() {
        return classe;
    }

    public void setClasse(String classe) {
        this.classe = classe;
    }

    public String getConteudo() {
        return conteudo;
    }

    public void setConteudo(String conteudo) {
        this.conteudo = conteudo;
    }

    public Integer getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Integer getId_entrada() {
        return id_entrada;
    }

    public void setId_entrada(int id_entrada) {
        this.id_entrada = id_entrada;
    }

    public Integer getId_saida() {
        return id_saida;
    }

    public void setId_saida(int id_saida) {
        this.id_saida = id_saida;
    }

    public TipoRestricao getTipo() {
        return tipo;
    }

    public void setTipo(TipoRestricao tipo) {
        this.tipo = tipo;
    }

    public Restricao() {
    }
    
    public Restricao(Integer id_entrada, String classe, TipoRestricao tipo, String conteudo) {
        this.id_entrada = id_entrada;
        this.classe = classe;
        this.tipo = tipo;
        this.conteudo = conteudo;
    }
    
    public Restricao(Integer id_entrada, Integer id_saida, String classe, TipoRestricao tipo, String conteudo) {
        this.id_entrada = id_entrada;
        this.id_saida = id_saida;
        this.classe = classe;
        this.tipo = tipo;
        this.conteudo = conteudo;
    }
    
    public Restricao(Integer id_entrada, TipoEntrada tipoE, Integer id_saida, String classe, TipoRestricao tipo, String conteudo) {
        this.id_entrada = id_entrada;
        this.id_saida = id_saida;
        this.classe = classe;
        this.tipo = tipo;
        this.conteudo = conteudo;
        //Geração automática de restrição inválida para string
        /*if((tipoE==TipoEntrada.STRING)&&classe.equals("valida")&&(tipo==TipoRestricao.IN)){
            Restricao r = new Restricao(id_entrada, id_saida, "invalida", TipoRestricao.NOT_IN,conteudo);
            try {
                r.save();
            } catch (SQLException ex) {
                Logger.getLogger(Restricao.class.getName()).log(Level.SEVERE, null, ex);
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(Restricao.class.getName()).log(Level.SEVERE, null, ex);
            }
        }*/
    }

    public Restricao(ResultSet rs) throws SQLException {
        this.id = rs.getInt("id");
        this.id_entrada = rs.getInt("id_entrada");
        this.id_saida = rs.getInt("id_saida");
        this.classe = rs.getString("classe");
        this.tipo = TipoRestricao.valueOf(rs.getString("tipo"));
        this.conteudo = rs.getString("conteudo");
    }
    /**
     * Busca todos os projetos no banco de dados e retonar um ArrayList de Restrições
     * @return
     * @throws SQLException
     * @throws ClassNotFoundException 
     */
    public static ArrayList<Restricao> getAll() throws SQLException, ClassNotFoundException {
        Statement stm = new DbConn().getStatement();
        String query = "select * from " + NAME + ";";
        ResultSet result = stm.executeQuery(query);
        ArrayList<Restricao> resultList = resultSetToObjects(result);
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
     * Obtêm todas as restrições de entrada onde a coluna passada no parâmetro column 
     * possui o valor passado no parâmetro value e retorna um ArrayList de objetos
     * da classe.
     * @param column
     * @param value
     * @return
     * @throws ClassNotFoundException
     * @throws SQLException 
     */
    public static ArrayList<Restricao> getByColumn(String column, String value) throws ClassNotFoundException, SQLException {
        Statement stm = new DbConn().getStatement();
        String query = "select * from " + NAME + " where " + column + "='" + value + "';";
        //System.out.println(query);
        ResultSet result = stm.executeQuery(query);
        ArrayList<Restricao> resultList = resultSetToObjects(result);
        stm.close();
        return resultList;
    }
    
    public static ArrayList<Restricao> getByColumn(int column, String value) throws ClassNotFoundException, SQLException {
        Statement stm = new DbConn().getStatement();
        String query = "select * from " + NAME + " where " + column + "='" + value + "';";
        //System.out.println(query);
        ResultSet result = stm.executeQuery(query);
        ArrayList<Restricao> resultList = resultSetToObjects(result);
        stm.close();
        return resultList;
    }       
    
    public static ArrayList<Restricao> getAllByEntradaId(int idEntrada) throws SQLException, ClassNotFoundException{
        DbConn connection = new DbConn();
        connection.connect(DbConn.DATABASE_FILE);
        Statement stm = connection.getStatement();
        
        String query = "select * from " + NAME + "where id_entrada = " + idEntrada + ";";
        //System.out.println(query);
        ResultSet result = stm.executeQuery(query);
        ArrayList<Restricao> resultList = resultSetToObjects(result);
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
            query += "insert into " + NAME + "('id_entrada', 'id_saida', 'classe', 'tipo', 'conteudo') " +
                    "values('" + this.id_entrada + "', '" + this.id_saida + "', '" + this.classe + "', '" + this.tipo +  "', '" + this.conteudo + "')";
            result = stm.executeUpdate(query);
            ResultSet keys = stm.getGeneratedKeys();
            keys.next();
            this.id = keys.getInt(1);
        } else {
            query += "update " + NAME + " set id_entrada='" + this.id_entrada + "',id_saida='" + this.id_saida + "', classe='" + this.classe + "', tipo='" + this.tipo + "',conteudo='" + this.conteudo + "' where id=" + this.id;
            result = stm.executeUpdate(query);
        }
        
        stm.close();
        return result;
    }
    /**
     * Transforma o resultado de uma query no banco (ResultSet) em um arraylist
     * de projeto, ou seja, para cada registro do banco, instancia uma classe Restricao
     * @param rs ResultSet resutado de uma query no banco de dados
     * @return ArrayList<Projeto>
     * @throws SQLException 
     */
    public static ArrayList<Restricao> resultSetToObjects(ResultSet rs) throws SQLException {
        ArrayList<Restricao> resultList = new ArrayList<Restricao>();
        
        while(rs.next()) {
            Restricao newObj = new Restricao(rs);
            resultList.add(newObj);
        }
        
        return resultList;
    }
    
     public static ArrayList<Restricao> getByTwoColumns(int idEntrada, String classe) throws SQLException, ClassNotFoundException{
        DbConn connection = new DbConn();
        connection.connect(DbConn.DATABASE_FILE);
        Statement stm = connection.getStatement();
        
        String query = "select * from " + NAME + " where id_entrada = " + idEntrada + 
                 " and classe = '" + classe +"' ;";
        //System.out.println(query);
        ResultSet result = stm.executeQuery(query);
        ArrayList<Restricao> resultList = resultSetToObjects(result);
        stm.close();
        return resultList;
    }

}
