package br.ufam.metodo.util.aprendizado;

public class MetodoAprendizadoFactory {
	
	public static MetodoAprendizado fabrica(String metodoAprendizado)
    {
        if (metodoAprendizado.equals("OnlineBagging"))
            return new MetodoAprendizadoOnLineBagging();
        
        if (metodoAprendizado.equals("OnlineBoosting"))
        	return new MetodoAprendizadoOnLineBoosting();
        
        if (metodoAprendizado.equals("BOLE"))
            return new MetodoAprendizadoBOLE();
        
        if (metodoAprendizado.equals("ADOB"))
        	return new MetodoAprendizadoADOB();
        
        if (metodoAprendizado.equals("LeverageBagging"))
        	throw new UnsupportedOperationException("ERROR! Não implementado ainda! " + metodoAprendizado);
        
        if (metodoAprendizado.equals("Pure"))
        	throw new UnsupportedOperationException("ERROR! Não implementado ainda! " + metodoAprendizado);
        
        throw new UnsupportedOperationException("ERROR! Não implementado ainda! " + metodoAprendizado);
        
    }

}
