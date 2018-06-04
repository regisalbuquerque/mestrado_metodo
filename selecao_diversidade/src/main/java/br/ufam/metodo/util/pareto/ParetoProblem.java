package br.ufam.metodo.util.pareto;

import org.moeaframework.core.Solution;
import org.moeaframework.core.variable.EncodingUtils;
import org.moeaframework.problem.AbstractProblem;

/**
 *
 * @author regis
 */
public class ParetoProblem extends AbstractProblem {
    
    boolean maximizacaoF1;
    boolean maximizacaoF2;
    Solucao[] solucoes;

    public ParetoProblem(Solucao[] solucoes, boolean maximizacaoF1, boolean maximizacaoF2) {
        super(1,2); // 1 Variável(x) e 2 Funções Objetivos (F1 e F2)
        this.solucoes = solucoes;
        this.maximizacaoF1 = maximizacaoF1;
        this.maximizacaoF2 = maximizacaoF2;
    }
    
    @Override
    public Solution newSolution() {
        Solution solution = new Solution(getNumberOfVariables(), getNumberOfObjectives());
 
        for (int i = 0; i < getNumberOfVariables(); i++) {
          solution.setVariable(i, EncodingUtils.newInt(1, this.solucoes.length)); //Variável discreta de 1 até total de soluções
        }

        return solution;
    }
    
    @Override
    public void evaluate(Solution solution) {
    //double[] x = CoreUtils.castVariablesToDoubleArray(solution);
    int x = EncodingUtils.getInt(solution.getVariable(0));
    double[] f = new double[numberOfObjectives];
 
        f[0] = this.solucoes[x-1].getValor1();
        f[1] = this.solucoes[x-1].getValor2();
        
        //solution.setObjectives(f);
        if (this.maximizacaoF1) solution.setObjective(0, -f[0]);
        else solution.setObjective(0, f[0]);
        
        if (this.maximizacaoF2) solution.setObjective(1, -f[1]);
        else solution.setObjective(1, f[1]);
    }
}
