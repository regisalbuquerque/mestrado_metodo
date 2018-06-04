package br.ufam.diversidade.impl;

import java.util.List;

import br.ufam.diversidade.MedidaCalculo;
import br.ufam.metodo.util.calculo.Matematica;
import br.ufam.metodo.util.calculo.Predicoes;

public class MargemCalculoDiversidade implements MedidaCalculo {

	
	public double calcula(Predicoes predicoes)
	{
		List<List<Integer>> listaPredicoes = predicoes.getListaPredicoesIteracoesClassificadores();
		
		double num_mv = 0;
		double num_sv = 0;
		double num_dataset = listaPredicoes.size();
		double num_classificadores = listaPredicoes.get(0).size();
		
		//Percorre o DataSet - x (uma observação do dataset)
		for (List<Integer> votosClassificadores : listaPredicoes) {
			
			Integer mv = Matematica.mostCommon(votosClassificadores);
			Integer second_vote = Matematica.secondMostCommon(votosClassificadores);
			
			for (Integer y_i : votosClassificadores) {
				if (y_i == mv)
					num_mv++;
				else if (y_i == second_vote)
					num_sv++;
			}
		}
		
		return (num_mv-num_sv)/(num_dataset*num_classificadores);
	}
	
	
	
	public boolean isMaximiza() {
		return true;
	}

}
