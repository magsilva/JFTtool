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
package br.com.jfttool.connection;

import java.io.IOException;
import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Classe responsável por gerenciar a conexão com o banco de dados SQLite
 */
public class DbConn {
    
    public static final String DATABASE_FILE = "database.sqlite";
    
    private static final String DATABASE_SCHEMA = 
"DROP TABLE IF EXISTS caso_teste;" + 
"DROP TABLE IF EXISTS dado_entrada;" + 
"DROP TABLE IF EXISTS dado_teste;" + 
"DROP TABLE IF EXISTS causa_composta;" + 
"DROP TABLE IF EXISTS causa_efeito;" + 
"DROP TABLE IF EXISTS causa_restricao;" + 
"DROP TABLE IF EXISTS efeito_restricao;" + 
"DROP TABLE IF EXISTS grupo_restricao;" + 
"DROP TABLE IF EXISTS efeito;" + 
"DROP TABLE IF EXISTS causa;" + 
"DROP TABLE IF EXISTS restricao;" + 
"DROP TABLE IF EXISTS saida;" + 
"DROP TABLE IF EXISTS entrada;" + 
"DROP TABLE IF EXISTS projeto;" + 
"create table projeto (" + 
"	id INTEGER PRIMARY KEY AUTOINCREMENT," + 
"	nome varchar(100) not null" + 
");" + 
"create table entrada (" + 
"	id INTEGER PRIMARY KEY AUTOINCREMENT," + 
"	id_projeto INTEGER not null," + 
"	nome varchar(100)," + 
"	tipo varchar(50) not null," + 
"	obrigatorio smallint not null default 0," + 
"	FOREIGN KEY(id_projeto) REFERENCES projeto(id)" + 
");" + 
"create table dado_teste (" + 
"	id INTEGER PRIMARY KEY AUTOINCREMENT," + 
"	criterio varchar(100) not null," + 
"        id_projeto INTEGER" + 
");" + 
"create table dado_entrada (" + 
"	id INTEGER PRIMARY KEY AUTOINCREMENT," + 
"	valor varchar(500) not null," + 
"	id_entrada INTEGER not null," + 
"	id_dado_teste INTEGER not null," + 
"	valido smallint not null default 1," + 
"	FOREIGN KEY(id_entrada) REFERENCES entrada(id)," + 
"	FOREIGN KEY(id_dado_teste) REFERENCES dado_teste(id)" + 
");" + 
"create table caso_teste (" + 
"	id INTEGER PRIMARY KEY AUTOINCREMENT," + 
"	id_dado_teste INTEGER not null," + 
"	id_saida INTEGER not null," + 
"       id_projeto INTEGER," + 
"	valido smallint not null default 1," + 
"	FOREIGN KEY(id_saida) REFERENCES saida(id)," + 
"	FOREIGN KEY(id_dado_teste) REFERENCES dado_teste(id)" + 
"     " + 
");" + 
"create table saida (" + 
"	id INTEGER PRIMARY KEY AUTOINCREMENT," + 
"	id_projeto INTEGER not null," + 
"	nome varchar(100)," + 
"	conteudo varchar(500) not null," + 
"	valida varchar(100)," + 
"	FOREIGN KEY(id_projeto) REFERENCES projeto(id)" + 
");" + 
"create table restricao (" + 
"	id INTEGER PRIMARY KEY AUTOINCREMENT," + 
"	id_entrada INTEGER," + 
"	id_saida INTEGER," + 
"	classe varchar(50) not null," + 
"	tipo varchar(50) not null," + 
"	conteudo varchar(1024)," + 
"	FOREIGN KEY(id_entrada) REFERENCES entrada(id)," + 
"	FOREIGN KEY(id_saida) REFERENCES saida(id)" + 
");" + 
"create table causa (" + 
"	id INTEGER PRIMARY KEY AUTOINCREMENT," + 
"	id_entrada INTEGER not null," + 
"	nome varchar(100) not null," + 
"	valor varchar(500)," + 
"	id_projeto INTEGER not null," + 
"	FOREIGN KEY(id_projeto) REFERENCES projeto(id)," + 
"	FOREIGN KEY(id_entrada) REFERENCES entrada(id)" + 
");" + 
"create table efeito (" + 
"	id INTEGER PRIMARY KEY AUTOINCREMENT," + 
"	id_saida INTEGER not null," + 
"	nome varchar(100) not null," + 
"	id_projeto INTEGER not null," + 
"	FOREIGN KEY(id_projeto) REFERENCES projeto(id)," + 
"	FOREIGN KEY(id_saida) REFERENCES saida(id)" + 
");" + 
"create table causa_composta (" + 
"	id INTEGER PRIMARY KEY AUTOINCREMENT," + 
"	causa1_id INTEGER," + 
"	causa2_id INTEGER," + 
"       causa_composta1_id INTEGER," + 
"	causa_composta2_id INTEGER," + 
"	tipo_relacionamento varchar(50) not null," + 
"	relacao_c1 varchar(50) not null default 'IDENTIDADE'," + 
"	relacao_c2 varchar(50) not null default 'IDENTIDADE'," + 
"	id_projeto INTEGER not null," + 
"	FOREIGN KEY(id_projeto) REFERENCES projeto(id)," + 
"	FOREIGN KEY(causa1_id) REFERENCES causa(id)," + 
"	FOREIGN KEY(causa2_id) REFERENCES causa(id)," + 
"       FOREIGN KEY(causa_composta1_id) REFERENCES causa(id)," + 
"	FOREIGN KEY(causa_composta2_id) REFERENCES causa(id)" + 
");" + 
"create table causa_efeito (" + 
"	id INTEGER PRIMARY KEY AUTOINCREMENT," + 
"	id_causa INTEGER," + 
"	id_causa_composta INTEGER," + 
"	id_efeito INTEGER not null," + 
"	tipo_relacionamento varchar(50) not null," + 
"	id_projeto INTEGER not null," + 
"	FOREIGN KEY(id_projeto) REFERENCES projeto(id)," + 
"	FOREIGN KEY(id_causa) REFERENCES causa(id)," + 
"	FOREIGN KEY(id_causa_composta) REFERENCES causa_composta(id)," + 
"	FOREIGN KEY(id_efeito) REFERENCES efeito(id)" + 
");" + 
"create table grupo_restricao (" + 
"	id INTEGER PRIMARY KEY AUTOINCREMENT," + 
"	nome varchar(100) null" + 
");" + 
"create table efeito_restricao (" + 
"	id INTEGER PRIMARY KEY AUTOINCREMENT," + 
"	id_efeito INTEGER not null," + 
"	id_restricao varchar(5) not null," + 
"       grupo INTEGER not null," + 
"       consequencia INTEGER not null default 0," + 
"       id_projeto INTEGER not null," + 
"	FOREIGN KEY(id_projeto) REFERENCES projeto(id)," + 
"	FOREIGN KEY(id_efeito) REFERENCES efeito(id)" + 
");" + 
"create table causa_restricao (" + 
"	id INTEGER PRIMARY KEY AUTOINCREMENT," + 
"	id_causa INTEGER not null," + 
"	id_restricao varchar(5) not null," + 
"       grupo INTEGER not null," + 
"       consequencia INTEGER not null default 0," + 
"       id_projeto INTEGER not null," + 
"	FOREIGN KEY(id_projeto) REFERENCES projeto(id)," + 
"	FOREIGN KEY(id_causa) REFERENCES causa(id)" + 
");";
    
    private static Connection conn = null;
    private static Statement stm = null;

    /**
     * Construtor da classe que já inicia uma conexão com o banco.
     * @throws SQLException
     * @throws ClassNotFoundException 
     */
    public DbConn() throws SQLException, ClassNotFoundException{
        //Utiliza sempre o mesmo nome da constante.
        this.connect(DATABASE_FILE);
    }
    
    /**
     * Método que se conecta ao banco de dados e armaeza a conexão no atributo
     * conn da classe
     * @param file Nome do arquivo do banco de dados
     * @throws SQLException
     * @throws ClassNotFoundException 
     */
    public void connect(String file) throws SQLException, ClassNotFoundException {
        Class.forName("org.sqlite.JDBC");
         
        if (this.conn == null) {
            this.conn = DriverManager.getConnection("jdbc:sqlite:" + file);
            this.conn.setAutoCommit(true);
        }
    }
    
    /**
     * Inicializa o banco de dados criando a estrutura de tabelas inicial
     * AVISO: este método irá apagar todos os dados existentes atualmente
     * no banco.
     * @throws IOException 
     */
    public void initDb() throws IOException {
        try {
            
            String[] strLines = DATABASE_SCHEMA.split(";");

            for (int i = 0 ; i< strLines.length ; i++) {
                this.conn.createStatement().execute(strLines[i]);
            }
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    /**
     * Verifica se o banco de dados possui o número correto
     * de tabelas, caso contrário retorna false
     * @return boolean
     */
    public boolean databaseExists() {
        DatabaseMetaData meta;
        try {
            meta = this.conn.getMetaData();
            ResultSet res = meta.getTables(null, null, null, 
            new String[] {"TABLE"});
            int count = -1; //Comeca com -1 para ignorar uma tabela de controle do SQLITE
            while (res.next()) {
                count++;
            }
            if (count < 14) {
                return false;
            } else {
                return true;
            }
        } catch (SQLException ex) {
            Logger.getLogger(DbConn.class.getName()).log(Level.SEVERE, null, ex);
        }
        return true;
    }
    
    /**
     * Retorna o statement que é utilizado para executar queries no banco 
     * de dados
     * @return Statement
     * @throws SQLException 
     * 
     * 
     */
    public Statement getStatement() throws SQLException {
        try {
            if (this.conn == null) {
                connect(DATABASE_FILE);
            }
            
            if (this.stm == null) {
                this.stm = this.conn.createStatement();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        return this.stm;
    }
    
}
