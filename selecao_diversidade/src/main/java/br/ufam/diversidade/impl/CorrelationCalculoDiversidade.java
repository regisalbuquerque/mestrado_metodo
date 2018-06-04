package br.ufam.diversidade.impl;

import br.ufam.diversidade.PairwiseCalculo;
import br.ufam.diversidade.PairwiseUtil;

public class CorrelationCalculoDiversidade extends PairwiseCalculo {

	@Override
	public double calculo(double[][] table_scores) 
	{
		double n11 = PairwiseUtil.getN11(table_scores);
		double n10 = PairwiseUtil.getN10(table_scores);
		double n01 = PairwiseUtil.getN01(table_scores);		
		double n00 = PairwiseUtil.getN00(table_scores);
		
		double dividendo = n11 * n00 - n01 * n10;
		double divisor = Math.sqrt((n11*n10) + (n01*n00) + (n11*n00) + (n10*n00));

		if (divisor == 0)
			return 0;
		return (dividendo) / (divisor);
	}

	@Override
	public boolean isMaximiza() {
		return true;
	}
	
}
