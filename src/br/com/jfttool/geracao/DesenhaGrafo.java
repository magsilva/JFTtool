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
package br.com.jfttool.geracao;

import br.com.jfttool.enums.TipoRelacionamento;
import br.com.jfttool.enums.TipoRestricaoGrafo;
import br.com.jfttool.models.*;
import com.mxgraph.model.mxCell;
import com.mxgraph.model.mxGeometry;
import com.mxgraph.util.mxConstants;
import com.mxgraph.util.mxPoint;
import com.mxgraph.view.mxGraph;
import com.mxgraph.view.mxStylesheet;
import java.sql.SQLException;
import java.util.*;

/**
 * Classe responsável por desenhar o grafo causa-efeito na tela utilizando a 
 * biblioteca JgraphX.
 */
public class DesenhaGrafo {
    
    /**
     * Método que monta o diagrama do grafo e o retornar em formato da Jxgraph
     * @param id_projeto Id do projeto para o qual se deseja desenhar o grafo
     * @return mxGraph componente com o grafo desenhado para ser exibido na tela
     * @throws ClassNotFoundException
     * @throws SQLException 
     */
    public mxGraph desenhaGrafo(Integer id_projeto) throws ClassNotFoundException, SQLException {
        
        //Obtem dados necessários
        ArrayList<Causa> causas = Causa.getByColumn("id_projeto", id_projeto.toString());
        ArrayList<Efeito> efeitos = Efeito.getByColumn("id_projeto", id_projeto.toString());
        ArrayList<CausaComposta> causasCompostas = CausaComposta.getByColumn("id_projeto", id_projeto.toString());
        ArrayList<CausaEfeito> causasEfeitos = CausaEfeito.getByColumn("id_projeto", id_projeto.toString());
        ArrayList<CausaRestricao> restrCausas = CausaRestricao.getByColumn("id_projeto", id_projeto.toString(), "ORDER BY grupo ASC");
        ArrayList<EfeitoRestricao> restrEfeitos = EfeitoRestricao.getByColumn("id_projeto", id_projeto.toString(), "ORDER BY grupo ASC");
        
        //Desenha Grafo
        mxGraph graph = new mxGraph();
        Object parent = graph.getDefaultParent();
        
        //Define estilhos para os elementos do grafo
        //Estilo dos nós intermediários
        mxStylesheet stylesheet = graph.getStylesheet();
        Hashtable<String, Object> style_vertex = new Hashtable<String, Object>();
        style_vertex.put(mxConstants.STYLE_SHAPE, mxConstants.SHAPE_ELLIPSE);
        style_vertex.put(mxConstants.STYLE_FONTCOLOR, "#000000");
        style_vertex.put(mxConstants.STYLE_STROKECOLOR, "#000000");
        style_vertex.put(mxConstants.STYLE_FILLCOLOR, "#FFFFFF");
        stylesheet.putCellStyle("ROUNDED", style_vertex);
        
        //Estilo das causas
        Hashtable<String, Object> style_causa = new Hashtable<String, Object>();
        style_causa.put(mxConstants.STYLE_SHAPE, mxConstants.SHAPE_ELLIPSE);
        style_causa.put(mxConstants.STYLE_FONTCOLOR, "#000000");
        style_causa.put(mxConstants.STYLE_STROKECOLOR, "#000000");
        style_causa.put(mxConstants.STYLE_FILLCOLOR, "#99FFCC");
        stylesheet.putCellStyle("CAUSE", style_causa);
        
        //Estilho dos efeitos
        Hashtable<String, Object> style_efeito = new Hashtable<String, Object>();
        style_efeito.put(mxConstants.STYLE_SHAPE, mxConstants.SHAPE_ELLIPSE);
        style_efeito.put(mxConstants.STYLE_FONTCOLOR, "#000000");
        style_efeito.put(mxConstants.STYLE_STROKECOLOR, "#000000");
        style_efeito.put(mxConstants.STYLE_FILLCOLOR, "#FFCC99");
        stylesheet.putCellStyle("EFFECT", style_efeito);
        
        //Estilo invisível utilizado para restrições de causa ou efeito E, I e O
        Hashtable<String, Object> style_invisivel = new Hashtable<String, Object>();
        style_invisivel.put(mxConstants.STYLE_SHAPE, mxConstants.SHAPE_ELLIPSE);
        style_invisivel.put(mxConstants.STYLE_FONTCOLOR, "#000000");
        style_invisivel.put(mxConstants.STYLE_STROKECOLOR, mxConstants.NONE);
        style_invisivel.put(mxConstants.STYLE_FILLCOLOR, mxConstants.NONE);
        stylesheet.putCellStyle("INVISIVEL", style_invisivel);
        
        //Estilo das conexões entre causas, efeitos e causas compostas
        Hashtable<String, Object> style_edge = new Hashtable<String, Object>();
        style_edge.put(mxConstants.STYLE_FONTCOLOR, "#000000");
        style_edge.put(mxConstants.STYLE_STROKECOLOR, "#000000");
        style_edge.put(mxConstants.STYLE_ENDARROW, mxConstants.NONE);
        stylesheet.putCellStyle("EDGE", style_edge);
        
        //Estilo utilizado para arestas das restrições com seta (M,R)
        Hashtable<String, Object> style_restricao = new Hashtable<String, Object>();
        style_restricao.put(mxConstants.STYLE_FONTCOLOR, "#000000");
        style_restricao.put(mxConstants.STYLE_STROKECOLOR, "#000000");
        stylesheet.putCellStyle("RESTRICAO", style_restricao);
        
        //Estilo utilizado para arestas das restrições sem set (E,I,O)
        Hashtable<String, Object> style_restricao_no_arrow = new Hashtable<String, Object>();
        style_restricao_no_arrow.put(mxConstants.STYLE_FONTCOLOR, "#000000");
        style_restricao_no_arrow.put(mxConstants.STYLE_STROKECOLOR, "#000000");
        style_restricao_no_arrow.put(mxConstants.STYLE_ENDARROW, mxConstants.NONE);
        stylesheet.putCellStyle("RESTRICAO_SEM_SETA", style_restricao_no_arrow);
        
        graph.getModel().beginUpdate();
        try
        {   
            //Desenha a legenda no topo
            graph.insertVertex(parent, null, "Causas", 150, 20, 40, 40, "CAUSE");
            graph.insertVertex(parent, null, "Efeitos", 650, 20, 40, 40, "EFFECT");
            
            
            //Calcula o melhor espaçamento vertical das causas e efeitos
            double causa_span = 0;
            double efeito_span = 0;
            if (causas.size() > 1 && efeitos.size() > 1) {
                if (causas.size() > efeitos.size()) {
                    causa_span = 80;
                    efeito_span = ((causas.size()-1)*80) / (efeitos.size()-1);
                } else {
                    causa_span = ((efeitos.size()-1)*80) / (causas.size()-1);
                    efeito_span = 80;
                }
            } else {
                causa_span = 80;
                efeito_span = 80;
            }
            
            // Desenha todas as causas do grafo
            Iterator cit = causas.listIterator();
            HashMap<Integer, mxCell> causasGrafo = new HashMap();
            int causaCount = 0;
            while(cit.hasNext()) {
                Causa c = (Causa)cit.next();
                causasGrafo.put(c.getId(), (mxCell)graph.insertVertex(parent, null, c.getId(), 150, ((causaCount*causa_span) + 100), 40, 40, "CAUSE"));
                causaCount++;
            }
            
            // Desenha todos os efeitos do grafo
            Iterator eit = efeitos.listIterator();
            HashMap<Integer, mxCell> efeitosGrafo = new HashMap();
            int efeitoCount = 0;
            while(eit.hasNext()) {
                Efeito e = (Efeito)eit.next();
                efeitosGrafo.put(e.getId(), (mxCell)graph.insertVertex(parent, null, e.getId(), 650, ((efeitoCount*efeito_span) + 100), 40, 40, "EFFECT"));
                efeitoCount++;
            }
            
            // Desenha as causas compostas
            Iterator ccit = causasCompostas.listIterator();
            HashMap<Integer, mxCell> ccGrafo = new HashMap();
            while(ccit.hasNext()) {
                
                int nr_causas = 0;
                int nr_causas_compostas = 0;
                
                //Primeira conexão da causa composta (pode ser uma coisa ou outra causa composta)
                CausaComposta cc = (CausaComposta)ccit.next();
                ccGrafo.put(cc.getId(), (mxCell)graph.insertVertex(parent, null, cc.getId() + "\n" + this.imprimeRelacao(cc.getTipo_relacionamento()), 316, 150, 40, 40, "ROUNDED"));
                if (cc.getCausa1_id() != 0) {
                    graph.insertEdge(parent, null, this.imprimeRelacao(cc.getRelacao_c1()), causasGrafo.get(cc.getCausa1_id()), ccGrafo.get(cc.getId()), "EDGE");
                    nr_causas++;
                } else if (cc.getCausa_composta1_id() != 0) {
                    graph.insertEdge(parent, null, this.imprimeRelacao(cc.getRelacao_c1()), ccGrafo.get(cc.getCausa_composta1_id()), ccGrafo.get(cc.getId()), "EDGE");
                    nr_causas_compostas++;
                } else {
                    System.err.println("ERRO: Causa 1 ou Causa Composta 1 não setados");
                }
                
                
                //Segunda conexão ad causa composta (pode ser uma coisa ou outra causa composta)
                if (cc.getCausa2_id() != 0) {
                    graph.insertEdge(parent, null, this.imprimeRelacao(cc.getRelacao_c2()), causasGrafo.get(cc.getCausa2_id()), ccGrafo.get(cc.getId()), "EDGE");
                    nr_causas++;
                } else if (cc.getCausa_composta2_id() != 0) {
                    graph.insertEdge(parent, null, this.imprimeRelacao(cc.getRelacao_c2()), ccGrafo.get(cc.getCausa_composta2_id()), ccGrafo.get(cc.getId()), "EDGE");
                    nr_causas_compostas++;
                } else {
                    System.err.println("ERRO: Causa 2 ou Causa Composta 2 não setados");
                }
                
                // Define a posição da causa composta, A posição Y é sempre o meio entre as causas/causas compostas
                // Se ela é ligada apenas a causas, posiciona na coluna mais a esquerda
                if (nr_causas == 2) {
                    double c1y = causasGrafo.get(cc.getCausa1_id()).getGeometry().getY();
                    double c2y = causasGrafo.get(cc.getCausa2_id()).getGeometry().getY();
                    ccGrafo.get(cc.getId()).getGeometry().setY( ((c1y + c2y) / 2) );
                } else { // Se é ligada a pelo menos uma causa composta, posiciona na coluna mais a direita
                    double c1 = 0;
                    if (cc.getCausa1_id() != 0) {
                        c1 = causasGrafo.get(cc.getCausa1_id()).getGeometry().getY();
                    } else {
                        c1 = ccGrafo.get(cc.getCausa_composta1_id()).getGeometry().getY();
                    }
                    
                    double c2 = 0;
                    if (cc.getCausa2_id() != 0) {
                        c2 = causasGrafo.get(cc.getCausa2_id()).getGeometry().getY();
                    } else {
                        c2 = ccGrafo.get(cc.getCausa_composta2_id()).getGeometry().getY();
                    }
                    
                    ccGrafo.get(cc.getId()).getGeometry().setY( ((c1 + c2) / 2) );
                    ccGrafo.get(cc.getId()).getGeometry().setX( 483 );
                }
            }
            
            // Desenha todas as causa-efeitos
            Iterator ceit = causasEfeitos.listIterator();
            while(ceit.hasNext()) {
                CausaEfeito ce = (CausaEfeito)ceit.next();
                
                if((ce.getId_causa() != 0) && (ce.getId_causa_composta() != 0)) { // Este primeiro caso não acontece no modelo de funcionamento atual
                    graph.insertEdge(parent, null, this.imprimeRelacao(ce.getTipo_relacionamento()), causasGrafo.get(ce.getId_causa()), efeitosGrafo.get(ce.getId_efeito()), "EDGE");
                    graph.insertEdge(parent, null, this.imprimeRelacao(ce.getTipo_relacionamento()), ccGrafo.get(ce.getId_causa_composta()), efeitosGrafo.get(ce.getId_efeito()), "EDGE");
                } else if(ce.getId_causa() != 0) { //A causa-efeito é ligada a uma causa
                    graph.insertEdge(parent, null, this.imprimeRelacao(ce.getTipo_relacionamento()), causasGrafo.get(ce.getId_causa()), efeitosGrafo.get(ce.getId_efeito()), "EDGE");
                } else if (ce.getId_causa_composta() != 0) { // A causa-efeito é ligada a uma causa composta
                    graph.insertEdge(parent, null, this.imprimeRelacao(ce.getTipo_relacionamento()), ccGrafo.get(ce.getId_causa_composta()), efeitosGrafo.get(ce.getId_efeito()), "EDGE");
                } else {
                    System.err.println("ERRO: Causa efeito sem causa ou causa composta");
                }
            }
            
            //Código para reposicionar as causas compostas se for detectado colisão (uma em cima da outra)
            for(int k = 0 ; k < 5 ; k++) { //Executar mais de uma vez conserta colisões causadas por evitar a colisão de outros 2 nós
                for(int i = 0 ; i < (causasCompostas.size()) ; i++) { //Itera nas causas_compostas comparando com todas as outras causas compostas
                    for(int j = 0 ; j < (causasCompostas.size()) ; j++) {
                        if (i != j) {
                            boolean colisao = detectaColisao(ccGrafo.get(causasCompostas.get(i).getId()), ccGrafo.get(causasCompostas.get(j).getId()));

                            if (colisao) {
                                mxGeometry geo1 = ccGrafo.get(causasCompostas.get(i).getId()).getGeometry();
                                mxGeometry geo2 = ccGrafo.get(causasCompostas.get(j).getId()).getGeometry();

                                double avg = (geo1.getY() + geo2.getY()) / 2;

                                geo1.setY(avg - (30));
                                geo2.setY(avg + (30));
                            }
                        }
                    }
                }
            }
            
            //Desenha as restrições de causas
            Iterator restrCausasit = restrCausas.listIterator();
            CausaRestricao rcLast = null;
            ArrayList<CausaRestricao> restrList = new ArrayList<CausaRestricao>();
            while (restrCausasit.hasNext()) {
                CausaRestricao rc = (CausaRestricao)restrCausasit.next();
                if (rcLast == null) {
                    rcLast = rc;
                    restrList.add(rc);
                } else if (!restrCausasit.hasNext()) {
                    restrList.add(rc);
                    if (rcLast.getId_restricao() == TipoRestricaoGrafo.R) {
                        addRestricaoR(graph, parent, causasGrafo, restrList);
                    } else {
                        addRestricaoCausaMultipla(graph, parent, causasGrafo, restrList, rcLast.getId_restricao().toString());
                    }
                    
                } else if (rcLast.getGrupo() != rc.getGrupo()) {
                    if (rcLast.getId_restricao() == TipoRestricaoGrafo.R) {
                        addRestricaoR(graph, parent, causasGrafo, restrList);
                    } else {
                        addRestricaoCausaMultipla(graph, parent, causasGrafo, restrList, rcLast.getId_restricao().toString());
                    }
                    restrList = new ArrayList<CausaRestricao>();
                    restrList.add(rc);
                    rcLast = rc;
                } else if (rcLast.getGrupo() == rc.getGrupo()) {
                    restrList.add(rc);
                }
            }
            
            // Desenha todas as restrições de efeitos
            Iterator restrEfeitosit = restrEfeitos.listIterator();
            EfeitoRestricao reLast = null;
            ArrayList<EfeitoRestricao> restrEList = new ArrayList<EfeitoRestricao>();
            while (restrEfeitosit.hasNext()) {
                EfeitoRestricao rc = (EfeitoRestricao)restrEfeitosit.next();
                if (reLast == null) {
                    reLast = rc;
                    restrEList.add(rc);
                } else if (!restrEfeitosit.hasNext()) {
                    restrEList.add(rc);
                    addRestricaoM(graph, parent, efeitosGrafo, restrEList);
                } else if (reLast.getGrupo() != rc.getGrupo()) {
                    addRestricaoM(graph, parent, efeitosGrafo, restrEList);
                    restrEList = new ArrayList<EfeitoRestricao>();
                    restrEList.add(rc);
                    reLast = rc;
                } else if (reLast.getGrupo() == rc.getGrupo()) {
                    restrEList.add(rc);
                }
            }
            
        }
        finally
        {
            //Finaliza a atualização o grafo
            graph.getModel().endUpdate();
        }
        
        return graph;
    }

    /**
     * Método que desenha uma restrição do tipo M para 2 efeitos
     * @param graph desenho do grafo
     * @param parent
     * @param efeitosGrafo Efeitos desenhados no grafo desenhar a restrição entre eles
     * @param efeitos Modelos dos efeitos que fazem parte da restrição
     */
    private void addRestricaoM(mxGraph graph, Object parent, HashMap<Integer, mxCell> efeitosGrafo, ArrayList<EfeitoRestricao> efeitos) {
        mxCell restricao;
        EfeitoRestricao er1 = efeitos.get(0);
        EfeitoRestricao er2 = efeitos.get(1);
        //Quando consequência é 1 a ponta da aresta tem uma seta desenhada
        if (er1.getConsequencia() == 1) {
            restricao = (mxCell)graph.insertEdge(parent, null, "      M", efeitosGrafo.get(er2.getId_efeito()), efeitosGrafo.get(er1.getId_efeito()), "RESTRICAO;dashed=1;rounded=1");
        } else {
            restricao = (mxCell)graph.insertEdge(parent, null, "      M", efeitosGrafo.get(er1.getId_efeito()), efeitosGrafo.get(er2.getId_efeito()), "RESTRICAO;dashed=1;rounded=1");
        }
        
        //Posiciona um ponto intermediário na aresta de restrição para fazer a curva
        double y = (restricao.getSource().getGeometry().getCenterY() + restricao.getTarget().getGeometry().getCenterY()) / 2;
        double x = (restricao.getSource().getGeometry().getCenterX() + 35);
        mxPoint p = new mxPoint(x, y);
        List<mxPoint> points = new ArrayList<mxPoint>();
        points.add(p);
        restricao.getGeometry().setPoints(points);
    }

    /**
     * Método que desenha uma restrição do tipo R para 2 causas
     * @param graph desenho do grafo
     * @param parent
     * @param causasGrafo Causas desenhadas no grafo desenhar a restrição entre elas
     * @param causas Modelos das causas que fazem parte da restrição
     */
    private void addRestricaoR(mxGraph graph, Object parent, HashMap<Integer, mxCell> causasGrafo, ArrayList<CausaRestricao> causas) {
        mxCell restricao4;
        CausaRestricao cr1 = causas.get(0);
        CausaRestricao cr2 = causas.get(1);
        
        //Quando consequência é 1 a ponta da aresta tem uma seta desenhada
        if (cr1.getConsequencia() == 1) {
            restricao4 = (mxCell)graph.insertEdge(parent, null, "R     ", causasGrafo.get(cr2.getId_causa()), causasGrafo.get(cr1.getId_causa()), "RESTRICAO;dashed=1;rounded=1");
        } else {
            restricao4 = (mxCell)graph.insertEdge(parent, null, "R     ", causasGrafo.get(cr1.getId_causa()), causasGrafo.get(cr2.getId_causa()), "RESTRICAO;dashed=1;rounded=1");
        }
        
        //Posiciona um ponto intermediário na aresta de restrição para fazer a curva
        double y4 = (restricao4.getSource().getGeometry().getCenterY() + restricao4.getTarget().getGeometry().getCenterY()) / 2;
        double x4 = (restricao4.getSource().getGeometry().getCenterX() - 35);
        List<mxPoint> points4 = new ArrayList<mxPoint>();
        points4.add(new mxPoint(x4, y4));
        restricao4.getGeometry().setPoints(points4);
    }

    /**
     * Desenha as restrições do tipo E, I e O entre duas causas
     * @param graph desenho do grafo
     * @param parent
     * @param causasGrafo Causas desenhadas no grafo
     * @param causas Modelo das causas que farão parte da restrição
     * @param label Label da restrição, string E, I ou O
     */
    private void addRestricaoCausaMultipla(mxGraph graph, Object parent, HashMap<Integer, mxCell> causasGrafo, ArrayList<CausaRestricao> causas, String label) {
        
        Iterator<CausaRestricao> it = causas.iterator();
        double y = 0;
        double x = 0;
        
        //Calcula a posição o nó da restrição
        while(it.hasNext()) {
            mxGeometry geo = causasGrafo.get(it.next().getId_causa()).getGeometry();
            y += geo.getCenterY();
            x = geo.getCenterX();
        }
        y = y/causas.size();
        x = x = 75;
        // Insere o nó da restrição com estilo invisível para não possuir fundo nem linhas, apenas o label
        mxCell restNo = (mxCell)graph.insertVertex(parent, null, label, x, y, 15, 15, "INVISIVEL");
        
        // Insere todas as arestas ligando a restrição as causas
        it = causas.iterator();
        while(it.hasNext()) {
            CausaRestricao c = it.next();
            graph.insertEdge(parent, null, "", restNo, causasGrafo.get(c.getId_causa()), "RESTRICAO_SEM_SETA;dashed=1");
        }
    }
    
    /**
     * Método que a partir do Enum do TipoRelação retorna uma string para ser
     * impressa no grafo. Evita que a relação identidade seja impressa e também
     * pula uma linha na relação NOT para distanciar ela da aresta, tornando
     * mais legível
     * @param relacao
     * @return String
     */
    public static String imprimeRelacao(TipoRelacionamento relacao) {
        if (relacao == TipoRelacionamento.IDENTIDADE) {
            return "";
        } else if (relacao == TipoRelacionamento.NOT) {
            return relacao.toString() + "\n ";
        } else {
            return relacao.toString();
        }
    }
    
    /**
     * Método que detecta se dois elementos do grafo estão sobrepostos
     * retorna true caso estejam sobrepostos e false caso contrário
     * @param no1
     * @param no2
     * @return boolean
     */
    private boolean detectaColisao(mxCell no1, mxCell no2) {
        
        mxGeometry geo1 = no1.getGeometry();
        mxGeometry geoTmp = new mxGeometry(geo1.getX(), geo1.getY(), geo1.getWidth(), geo1.getHeight());
        
        mxGeometry geo2 = no2.getGeometry();
        
        // Faz o nó crescer o equivalente a seu raio para cada um dos seus lados
        // Isto é usado para detectar colisões que não chegam a envolver o centro 
        // dos elementos
        geo1.grow(20);
        
        boolean result = geo1.contains(geo2.getCenterX(), geo2.getCenterY());
        
        // Desfaz o grow utilizado para voltaro nó ao tamanho normal
        no1.setGeometry(geoTmp);
        
        return result;
    }
    
}
