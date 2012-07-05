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

import java.util.Random;

/**
 * @author JFTtool
 * Classe contém todos os métodos de geração de números (double) aleatórios obedecendo algumas restrições.
 */
public class RandomFloat {
    /**
     * Gera um dado double dentro do intervalo passado.
     * @param conteudo String com conteúdo, deve conter dois valores (double) separados por "," ex: 5.345,30.0982 .
     * @return double Double com o valor do número gerado dentro do intervalor passado.
     */
    public static double geraIntervalo(String conteudo){
        String [] valores = conteudo.split(",");
        double vmin=Double.parseDouble(valores[0]),vmax=Double.parseDouble(valores[1]);
        if(vmin>vmax){
           double aux=vmax;
           vmax = vmin;
           vmin=aux;
        }
        Random random = new Random();
        double numeroGerado=0;
        if (vmax>=0 && vmin>=0){
            //numeroGerado = vmin + random.nextInt(vmax-vmin);
            numeroGerado = (vmax-vmin) * random.nextDouble() + vmin;
        }
        else if (vmax<0 && vmin<0){
            double aux = -vmin;
            vmin = -vmax;
            vmax = aux;          
            numeroGerado = (vmax-vmin) * random.nextDouble() + vmin;
            //numeroGerado = vmin + random.nextInt(vmax-vmin);
            numeroGerado = -numeroGerado;
        }
        else if(vmin<0 && vmax>0){
            double numeroGerado1, numerogerado2;
            numeroGerado1 = vmax * random.nextDouble();
            numerogerado2 = vmin * random.nextDouble();
            if(random.nextFloat() > 0.5)
                numeroGerado = numeroGerado1;
            else
                 numeroGerado = numerogerado2;
        }
        
        return numeroGerado;
    }

    /**
     * Método que gera um dado double menor que um dado valor máximo.
     * @param vmax Valor máximo que o número gerado pode assumir.
     * @return double Double gerado que deve ter valor menor que vmax.
     */
   public static double geraMaximo(double vmax){
        Random random = new Random();
        double numeroGerado = random.nextDouble();      
        if(vmax<=0){
            while(numeroGerado>vmax){
                numeroGerado=(Double.MAX_VALUE-1)*random.nextDouble();
                if(numeroGerado>0)//coloquei a mais
                    numeroGerado=-numeroGerado;
            }
        }
        if(vmax>0){
            double numeroGerado1, numeroGerado2;
            numeroGerado1 = vmax*random.nextDouble();
            numeroGerado2 = -(Double.MAX_VALUE-1)*random.nextDouble();
            //if (numeroGerado2>0)//acho que pode ser retirado
                //numeroGerado2 = -numeroGerado2;//esse não sai junto ou coloco menos na geracao
            if(random.nextFloat()>0.5)
                numeroGerado=numeroGerado1;
            else
                numeroGerado=numeroGerado2;
        }           
        return numeroGerado;
    }
    
    
    /**
     * Método que gera um dado double maior que um dado valor mínimo.
     * @param vmin Valor mínimo que o número gerado pode assumir.
     * @return double Double gerado que deve ter valor maior que vmin.
     */
    public static double geraMinimo(double vmin){
        Random random = new Random();
        double numeroGerado = random.nextDouble();
        if(vmin>=0){
            while(numeroGerado<vmin)
                numeroGerado = (Double.MAX_VALUE-1)*random.nextDouble();
        }
        if(vmin<0){
            double numeroGerado1, numeroGerado2;
            numeroGerado2 = vmin*random.nextDouble();
            numeroGerado1 = (Double.MAX_VALUE-1)*random.nextDouble();
            //if (numeroGerado1<0)//acho que pode ser retirado
                //numeroGerado1 = -numeroGerado1;//sai junto
            if(random.nextFloat()>0.5)
                numeroGerado = numeroGerado1;
            else
                numeroGerado=numeroGerado2;
        }
        return numeroGerado;
    } 

    /**
     * Método que retorna um dado double escolhido aleatóriamente dentro de uma lista de números inteiros.
     * @param conteudo Lista de números double, todos os números devem ser passados separados por "," ex: 1.71,2.3456,3.87209,4.578
     * @return double Double escolhido aleatóriamente dentro da lista de inteiros passada.
     */
    public static double geraIn(String conteudo) {
        String [] valores = conteudo.split(",");
        int indice = (int) ((valores.length)*Math.random());
        return Double.parseDouble(valores[indice]);
    }

    /**
     * Método que retorna um dado double que é diferente de todos os números (double) contidos em uma lista recebida (conteudo).
     * @param conteudo Lista de números inteiros separados por ","  ex: 1.11,3.23,5.765,6.098,7.457,9.192,10.3465 .
     * @return double Double diferente de todos os números presentes na lista passada.
     */
    public static double geraNotIn(String conteudo) {
        String [] valores = conteudo.split(",");
        Boolean geracaoCorreta = false;
        double numeroGerado=0;
        while(!geracaoCorreta){
            Random random = new Random();
            double numeroGerado1 = (Double.MAX_VALUE-1)*random.nextDouble();
            double numeroGerado2 = -(Double.MAX_VALUE-1)*random.nextDouble();
            if(random.nextFloat()>0.5)
                numeroGerado=numeroGerado1;
            else
                numeroGerado=numeroGerado2;
                Boolean diferenteDeTodos = true;
                for(int i=0;i<valores.length;i++){
                      if(numeroGerado==Double.parseDouble(valores[i]))
                             diferenteDeTodos=false;
                }
                if (diferenteDeTodos==true)
                      geracaoCorreta=true;
        }  
        return numeroGerado;
    }
}
