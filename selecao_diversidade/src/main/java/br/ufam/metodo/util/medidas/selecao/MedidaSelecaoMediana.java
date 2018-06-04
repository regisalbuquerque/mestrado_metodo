package br.ufam.metodo.util.medidas.selecao;

import com.yahoo.labs.samoa.instances.Instance;

import br.ufam.metodo.util.model.Ensemble;
import br.ufam.metodo.util.model.EnsembleValor;
import br.ufam.metodo.util.pareto.Solucao;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 *
 * @author regis
 */
public class MedidaSelecaoMediana implements MedidaSelecao{

    @Override
    public int seleciona(double[] diversidades, double[] acc, boolean maximizacaoDiv, boolean maximizacaoAcc) {

        //Calcular as diversidades
        List<Solucao> lista = new ArrayList<>();
        for (int i = 0; i < diversidades.length; i++) {
            lista.add(new Solucao(i, diversidades[i], acc[i]));
        }
        
        Collections.sort(lista, Solucao.comparatorValor1_Crescente()); //Ordena
        
        int median_index;
        if (lista.size() % 2 == 0)
            median_index = lista.size()/2-1; //Se for PAR pega o menor do meio. Ex: 1 2 (3) 4 5 6
        else
            median_index = lista.size()/2;   //Se for IMPAR pega o menor do meio. Ex: 1 2 (3) 4 5
        
        //System.out.println("Median Index:" + median_index);
        
        return lista.get(median_index).getIndex();
    }
    
}
