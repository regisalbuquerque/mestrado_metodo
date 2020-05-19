package br.ufam.metodo.util.aprendizado;

public class MetodoAprendizadoFactory {
	
	public static MetodoAprendizado fabrica(String metodoAprendizado)
    {
//		if (this.selectionOptionEstrategiaGeracao.getChosenLabel().equals("OnlineBagging"))
//		{
//			this.poolOfEnsembles = new EnsembleOnLineBagging[this.ensemblesNumberOption.getValue()];
//		}
//		else if(this.selectionOptionEstrategiaGeracao.getChosenLabel().equals("OnlineBoosting"))
//		{
//			this.poolOfEnsembles = new EnsembleOnLineBoosting[this.ensemblesNumberOption.getValue()];
//		}
//		else if(this.selectionOptionEstrategiaGeracao.getChosenLabel().equals("LeverageBagging"))
//		{
//			this.poolOfEnsembles = new LeveragingBagModificado[this.ensemblesNumberOption.getValue()];
//		}
//		else if(this.selectionOptionEstrategiaGeracao.getChosenLabel().equals("Pure"))
//		{
//			this.poolOfEnsembles = new EnsembleOnLinePure[this.ensemblesNumberOption.getValue()];
//		}
//		else
//		{
//			throw new RuntimeException("ERROR! Estrategia de geracao INVALIDA!");
//		}
//		
		
		
        if (metodoAprendizado.equals("OnlineBagging"))
            return new MetodoAprendizadoOnLineBagging();
        
        if (metodoAprendizado.equals("OnlineBoosting"))
        	throw new UnsupportedOperationException("ERROR! Não implementado ainda! " + metodoAprendizado);
        
        if (metodoAprendizado.equals("LeverageBagging"))
        	throw new UnsupportedOperationException("ERROR! Não implementado ainda! " + metodoAprendizado);
        
        if (metodoAprendizado.equals("Pure"))
        	throw new UnsupportedOperationException("ERROR! Não implementado ainda! " + metodoAprendizado);
        
        throw new UnsupportedOperationException("ERROR! Não implementado ainda! " + metodoAprendizado);
        
    }

}
