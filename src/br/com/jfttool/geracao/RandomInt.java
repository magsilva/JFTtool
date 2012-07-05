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
 *Classe contém todos os métodos de geração de números (inteiros) aleatórios obedecendo algumas restrições.
 */
public class RandomInt {
    
       /**
        * Gera um dado inteiro dentro do intervalo passado.
        * @param conteudo String com conteúdo, deve conter dois valores separados por "," ex: 5,30 .
        * @return int Inteiro com o valor do número gerado dentro do intervalor passado.
        */
       public static int geraIntervalo(String conteudo){
        String [] valores = conteudo.split(",");
        int vmin=Integer.parseInt(valores[0]),vmax=Integer.parseInt(valores[1]);
        if(vmin>vmax){
            int aux = vmax;
            vmax=vmin;
            vmin=aux;
        }
        
        Random random = new Random();
        int numeroGerado=0;
        if(vmax==0 && vmin==0){
            numeroGerado=0;
        }
        else if ((vmax>=0 && vmin>0)||(vmax>0&&vmin>=0)){
            numeroGerado = vmin + random.nextInt(vmax-vmin);
        }
        else if (vmax<0 && vmin<0){
            int aux = -vmin;
            vmin = -vmax;
            vmax = aux;          
            numeroGerado = vmin + random.nextInt(vmax-vmin);
            numeroGerado = -numeroGerado;
        }
        else if(vmin<0 && vmax>0){
            int numeroGerado1, numerogerado2;
            numeroGerado1 = random.nextInt(vmax);
            numerogerado2 = -random.nextInt(-vmin);
            if(random.nextFloat() > 0.5)
                numeroGerado = numeroGerado1;
            else
                 numeroGerado = numerogerado2;
        }
        
        return numeroGerado;
    }

    /**
     * Método que gera um dado inteiro menor que um dado valor máximo.
     * @param vmax Valor máximo que o número gerado pode assumir.
     * @return int Inteiro gerado que deve ter valor menor que vmax.
     */
    public static int geraMaximo(int vmax){
        Random random = new Random();
        int numeroGerado = random.nextInt();      
        if(vmax<=0){
            while(numeroGerado>vmax){
                numeroGerado=random.nextInt();
            }
        }
        if(vmax>0){
            int numeroGerado1, numeroGerado2;
            numeroGerado1 = random.nextInt(vmax);
            numeroGerado2 = random.nextInt();
            if (numeroGerado2>0)
                numeroGerado2 = -numeroGerado2;
            if(random.nextFloat()>0.5)
                numeroGerado=numeroGerado1;
            else
                numeroGerado=numeroGerado2;
        }           
        return numeroGerado;
    }

    /**
     * Método que gera um dado inteiro maior que um dado valor mínimo.
     * @param vmin Valor mínimo que o número gerado pode assumir.
     * @return int Inteiro gerado que deve ter valor maior que vmin.
     */
    public static int geraMinimo(int vmin){
        Random random = new Random();
        int numeroGerado = random.nextInt();
        if(vmin>=0){
            while(numeroGerado<vmin)
                numeroGerado = random.nextInt();
        }
        if(vmin<0){
            int numeroGerado1, numeroGerado2;
            numeroGerado2 = -random.nextInt(-vmin);
            numeroGerado1 = random.nextInt();
            if (numeroGerado1<0)
                numeroGerado1 = -numeroGerado1;
            if(random.nextFloat()>0.5)
                numeroGerado = numeroGerado1;
            else
                numeroGerado=numeroGerado2;
        }
        return numeroGerado;
    }

    /**
     * Método que retorna um dado inteiro escolhido aleatóriamente dentro de uma lista de números inteiros.
     * @param conteudo Lista de números inteiros, todos os números devem ser passados separados por "," ex: 1,2,3,4
     * @return int Inteiro escolhido aleatóriamente dentro da lista de inteiros passada.
     */
    public static int geraIn(String conteudo){
        String [] valores = conteudo.split(",");
        int indice = (int) (valores.length*Math.random());
        return Integer.parseInt(valores[indice]);
    }

    /**
     * Método que retorna um dado inteiro que é diferente de todos os números inteiros contidos em uma lista recebida (conteudo).
     * @param conteudo Lista de números inteiros separados por ","  ex: 1,3,5,6,7,9,10 .
     * @return int Inteiro diferente de todos os inteiros presentes na lista passada.
     */
    public static int geraNotIn(String conteudo){
        String [] valores = conteudo.split(",");
        int numeroGerado=0;
        Boolean geracaoCorreta=false;
        while(!geracaoCorreta){
            Random random = new Random();
            int numeroGerado1 =(int) ((Integer.MAX_VALUE-1)*random.nextDouble());
            int numeroGerado2 = - (int) ((Integer.MAX_VALUE-1)*random.nextDouble());
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
