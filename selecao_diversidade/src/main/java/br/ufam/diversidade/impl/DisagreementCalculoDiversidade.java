package br.ufam.diversidade.impl;

import br.ufam.diversidade.PairwiseCalculo;
import br.ufam.diversidade.PairwiseUtil;

public class DisagreementCalculoDiversidade extends PairwiseCalculo {

	@Override
	public double calculo(double[][] table_scores) 
	{
		double n11 = PairwiseUtil.getN11(table_scores);
		double n10 = PairwiseUtil.getN10(table_scores);
		double n01 = PairwiseUtil.getN01(table_scores);		
		double n00 = PairwiseUtil.getN00(table_scores);
		
		double dividendo = n01 + n10;
		double divisor = n11 + n10 + n01 + n00;

		if (divisor == 0)
			return 0;
		return (dividendo) / (divisor);
	}

	@Override
	public boolean isMaximiza() {
		return false;
	}
	
}
