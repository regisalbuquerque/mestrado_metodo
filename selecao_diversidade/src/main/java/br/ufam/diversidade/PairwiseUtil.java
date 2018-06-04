package br.ufam.diversidade;

import java.util.List;

public abstract class PairwiseUtil {

	public static double[][] calculaMatriz(List<Integer> y_i, List<Integer> y_j, List<Integer> y_true) {
		double[][] table_scores = new double[2][2];

		for (int i = 0; i < y_true.size(); i++) {
			if (y_true.get(i) == y_i.get(i)) {
				if (y_true.get(i) == y_j.get(i))
					table_scores[1][1] += 1; // Ambos i e j corretos (N11)
				else
					table_scores[1][0] += 1; // i correto e j incorreto (N10)
			} else {
				if (y_true.get(i) == y_j.get(i))
					table_scores[0][1] += 1; // i incorreto e j correto (N01)
				else
					table_scores[0][0] += 1; // Ambos i e j incorretos (N00)

			}
		}
		return table_scores;
	}

	public static double getN11(double[][] table_scores) {
		return table_scores[1][1];
	}

	public static double getN10(double[][] table_scores) {
		return table_scores[1][0];
	}

	public static double getN01(double[][] table_scores) {
		return table_scores[0][1];
	}

	public static double getN00(double[][] table_scores) {
		return table_scores[0][0];
	}
}
