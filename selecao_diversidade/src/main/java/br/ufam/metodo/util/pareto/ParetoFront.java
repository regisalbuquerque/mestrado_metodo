package br.ufam.metodo.util.pareto;

import java.util.ArrayList;
import java.util.List;
import org.moeaframework.Executor;
import org.moeaframework.core.NondominatedPopulation;
import org.moeaframework.core.Solution;


/**
 *
 * @author regis
 */
public class ParetoFront {
    
    boolean maximizacaoF1;
    boolean maximizacaoF2;
    Solucao solucoes[];
    

    public ParetoFront(Solucao solucoes[], boolean maximizacaoF1, boolean maximizacaoF2) {
        this.maximizacaoF1 = maximizacaoF1;
        this.maximizacaoF2 = maximizacaoF2;
        this.solucoes = solucoes;
    }
    
    public List<Solucao> getParetoSoluctions()
    {
        NondominatedPopulation result = new Executor().
                withProblemClass(ParetoProblem.class, (Object) this.solucoes, (Object) this.maximizacaoF1, (Object) this.maximizacaoF2)
                .withAlgorithm("NSGAII")
                .withMaxEvaluations(1)
                .run();
        
        List<Solucao> listaPareto = new ArrayList<>();
        
        for (Solution solution : result) {
            double f1 = solution.getObjective(0);
            double f2 = solution.getObjective(1);
            
            if (this.maximizacaoF1) f1 = -f1;
            if (this.maximizacaoF2) f2 = -f2;

            listaPareto.add(new Solucao(-1, f1, f2));
        }
        
        //Preparar a lista de retorno
        List<Solucao> listaRetorno = new ArrayList<>();
        
        for (int i = 0; i < this.solucoes.length; i++) {
            if (listaPareto.contains(this.solucoes[i]))
                listaRetorno.add(this.solucoes[i]); //Possui indice
        }
        
        return listaRetorno;
    }
 
}
