package br.ufam.metodo.util.pareto;

import java.util.ArrayList;
import java.util.List;

public class FronteiraDePareto {

    boolean maximizacaoF1;
    boolean maximizacaoF2;
    Solucao solucoes[];
    

    public FronteiraDePareto(Solucao solucoes[], boolean maximizacaoF1, boolean maximizacaoF2) {
        this.maximizacaoF1 = maximizacaoF1;
        this.maximizacaoF2 = maximizacaoF2;
        this.solucoes = solucoes;
    }
    
    //org.moeaframework.core.FastNondominatedSorting
    public List<Solucao> getParetoSoluctions()
    {
    	
    	int N = solucoes.length;
		
		// precompute the dominance relations
		int[][] dominanceChecks = new int[N][N];
		
		for (int i = 0; i < N; i++) {
			Solucao si = solucoes[i];
			
			for (int j = i+1; j < N; j++) {
				if (i != j) {
					Solucao sj = solucoes[i];
					
					dominanceChecks[i][j] = DominanciaComparador.compare(si, sj);
					dominanceChecks[j][i] = -dominanceChecks[i][j];
				}
			}
		}
		
		// compute for each solution s_i the solutions s_j that it dominates
		// and the number of times it is dominated
		int[] dominatedCounts = new int[N];
		List<List<Integer>> dominatesList = new ArrayList<List<Integer>>();
		List<Integer> currentFront = new ArrayList<Integer>();
		
		
		for (int i = 0; i < N; i++) {
			List<Integer> dominates = new ArrayList<Integer>();
			int dominatedCount = 0;
			
			for (int j = 0; j < N; j++) {
				if (i != j) {
					if (dominanceChecks[i][j] < 0) {
						dominates.add(j);
					} else if (dominanceChecks[j][i] < 0) {
						dominatedCount += 1;
					}
				}
			}
			
			if (dominatedCount == 0) {
				currentFront.add(i);
			}
			
			dominatesList.add(dominates);
			dominatedCounts[i] = dominatedCount;
		}
		
		
		List<Solucao> listaPareto = new ArrayList<>();
		for (Integer item : currentFront) {
			listaPareto.add(solucoes[item]);
		}
       
        return listaPareto;
    }

}
