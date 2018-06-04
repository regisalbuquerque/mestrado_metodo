package br.ufam.diversidade;

import java.util.List;

import br.ufam.metodo.util.calculo.Predicoes;

public abstract class PairwiseCalculo implements MedidaCalculo
{
	@Override
	public double calcula(Predicoes predicoes) {
		
		List<List<Integer>> listaPredicoes = predicoes.getListaPredicoesClassificadoresIteracoes();
		List<Integer> yTrue = predicoes.getListaYTrue();
		
		double soma = 0;
		double numPairs = 0;
		
		//Compara os classificadores dois a dois
		for (int c1 = 0; c1 < listaPredicoes.size()-1; c1++) //Num classificadores - 1
		{
			for (int c2 = c1 + 1; c2 < listaPredicoes.size(); c2++)
			{
				double[][] table_scores = PairwiseUtil.calculaMatriz(listaPredicoes.get(c1), listaPredicoes.get(c2), yTrue);
				soma += calculo(table_scores);
				
				numPairs++;
			}
		}
		return soma/numPairs;
	}
	
	public abstract double calculo(double[][] table_scores);
}
