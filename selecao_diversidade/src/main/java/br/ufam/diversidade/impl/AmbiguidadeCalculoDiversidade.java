package br.ufam.diversidade.impl;

import java.util.List;

import br.ufam.diversidade.MedidaCalculo;
import br.ufam.metodo.util.calculo.Matematica;
import br.ufam.metodo.util.calculo.Predicoes;

public class AmbiguidadeCalculoDiversidade implements MedidaCalculo {

	
	public double calcula(Predicoes predicoes)
	{
		List<List<Integer>> listaPredicoes = predicoes.getListaPredicoesIteracoesClassificadores();
		
		double soma = 0;
		double num_dataset = listaPredicoes.size();
		double num_classificadores = listaPredicoes.get(0).size();
		
		//Percorre o DataSet - x (uma observação do dataset)
		for (List<Integer> votosClassificadores : listaPredicoes) {
			
			Integer w_k = Matematica.mostCommon(votosClassificadores);
			
			for (Integer y_i : votosClassificadores) {
				if (y_i != w_k)
					soma += 1;
			}
		}
		
		return soma/(num_dataset*num_classificadores);
	}
	
	
	
	public boolean isMaximiza() {
		return false;
	}

}
