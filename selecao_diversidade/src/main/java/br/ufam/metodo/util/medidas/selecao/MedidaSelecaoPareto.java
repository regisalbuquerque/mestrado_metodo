package br.ufam.metodo.util.medidas.selecao;

import java.util.Collections;
import java.util.List;

import br.ufam.metodo.util.pareto.FronteiraDePareto;
import br.ufam.metodo.util.pareto.Solucao;

public class MedidaSelecaoPareto implements MedidaSelecao{
    
    @Override
    public int seleciona(Double diversidades[], Double acc[], boolean maximizacaoDiv, boolean maximizacaoAcc)
    {
        
        Solucao[] solucoes = new Solucao[diversidades.length];
        for (int i = 0; i < diversidades.length; i++) {
            solucoes[i] = new Solucao(i, diversidades[i], acc[i]);
        }
        
        FronteiraDePareto paretoFront = new FronteiraDePareto(solucoes, maximizacaoDiv, maximizacaoAcc); 
        
        
        //Lista pareto front
        
        List<Solucao> listaSolucoesPareto = paretoFront.getParetoSoluctions();
        
        if (listaSolucoesPareto == null || listaSolucoesPareto.isEmpty()) return 0;
        
        Collections.sort(listaSolucoesPareto, Solucao.comparatorValor2_Decrescente());
        
        return listaSolucoesPareto.get(0).getIndex();
                
        /*
            10 ensembles: E1, E2,…E10
            D1 e A1, D2 e A2… D10 e A10
            D1 < D2 e A1 > A2
            E1 é melhor do que E2
        */

    }
}
