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
package br.com.jfttool.controle;

import br.com.jfttool.connection.DbConn;
import br.com.jfttool.enums.TipoEntrada;
import br.com.jfttool.enums.TipoRestricao;
import br.com.jfttool.geracao.Geracao;
import br.com.jfttool.geracao.GeracaoValorLimite;
import br.com.jfttool.models.Entrada;
import br.com.jfttool.models.Projeto;
import br.com.jfttool.models.Restricao;
import java.io.IOException;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author JFTtool Team
 * Classe responsável por realizar testes via console
 */
public class TesteGeracao {
     
    /**
     * Método principal da classe responsável por instanciar entradas e gerar dados de teste via console
     * @param args
     * @throws SQLException
     * @throws ClassNotFoundException
     * @throws IOException 
     */
    public static void main(String args[]) throws SQLException, ClassNotFoundException, IOException {
        //Inicia banco de dados
        DbConn conecta = new DbConn(); 
        conecta.initDb();
        
        //Armazenar um Projeto no banco de dados
        Projeto teste = new Projeto("TesteGeracao");
        try {
            teste.save();
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(TesteGeracao.class.getName()).log(Level.SEVERE, null, ex);
        }
        

        //Armazenar Entradas no banco de dados
        //Entrada1
        Entrada ent1 = new Entrada(teste.getId(), "preco", TipoEntrada.REAL, 1);
        ent1.save();
        //Armazenar as restrições para a Entrada no banco de dados
        Restricao r1 = new Restricao(ent1.getId(), "valida", TipoRestricao.INTERVALO, "0.3,9.9");
        r1.save();
        Restricao r2 = new Restricao(ent1.getId(), "invalida", TipoRestricao.VALOR_MAXIMO, "0");
        r2.save();
        //Entrada2
        Entrada ent2 = new Entrada(teste.getId(), "pets", TipoEntrada.INTEIRO, 1);        
        ent2.save();
        Restricao r5 = new Restricao(ent2.getId(), "valida", TipoRestricao.INTERVALO, "1,9");
        r5.save();
        Restricao r4 = new Restricao(ent2.getId(), "invalida", TipoRestricao.VALOR_MAXIMO, "-1");
        r4.save(); 
       
        /*
         * Teste para a Geração Particionamento em Classes
         */
        Geracao.partEmClasses(teste.getId());
        
        /*
         * Teste para a Geração Análise Valor Limite        
         *
         */
        GeracaoValorLimite.analiseValorLimite(teste.getId());
        
     }
     
}
