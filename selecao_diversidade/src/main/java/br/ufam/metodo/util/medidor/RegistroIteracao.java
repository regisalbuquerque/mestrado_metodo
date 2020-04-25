package br.ufam.metodo.util.medidor;

import java.util.Arrays;
import java.util.List;

import br.ufam.metodo.diversidade.util.Diversidades;


public class RegistroIteracao {
    private final int iteracao;
    private String codigo;
    private final Diversidades diversidades;
    private final double taxaAcerto;
    private final double taxaErro;
    private final boolean acertou;
    private final Double lambdaSelecionado;
    
    private final double acuraciaPrequencial;
    private final double desvioAcuraciaPrequencial;
    private final double erroPrequencial;
    private final double desvioErroPrequencial;
    
    private final List<Double> listaEnsemblesLambdas;
    private final List<Double> listaEnsemblesDiversidades;
    private final List<Double> listaEnsemblesAcuracias;
    

    public RegistroIteracao(int iteracao, 
    		String codigo,
    		Diversidades diversidades, 
    		double taxaAcerto, 
    		double taxaErro, 
    		boolean acertou, 
    		double acuraciaPrequencial, 
    		double desvioAcuraciaPrequencial, 
    		double erroPrequencial, 
    		double desvioErroPrequencial,
    		Double lambdaSelecionado,
    		Double[] arrayEnsemblesLambdas,
    		Double[] arrayEnsemblesDiversidades,
    		Double[] arrayEnsemblesAcuracias) {
    	this.codigo = codigo;
        this.iteracao = iteracao;
        this.diversidades = diversidades;
        this.taxaAcerto = taxaAcerto;
        this.taxaErro = taxaErro;
        this.acertou = acertou;
        this.acuraciaPrequencial = acuraciaPrequencial;
        this.desvioAcuraciaPrequencial = desvioAcuraciaPrequencial;
        this.erroPrequencial = erroPrequencial;
        this.desvioErroPrequencial = desvioErroPrequencial;
        this.lambdaSelecionado = lambdaSelecionado;
        this.listaEnsemblesLambdas = arrayEnsemblesLambdas != null ? Arrays.asList(arrayEnsemblesLambdas): null;
        this.listaEnsemblesDiversidades = arrayEnsemblesDiversidades != null ? Arrays.asList(arrayEnsemblesDiversidades): null;
        this.listaEnsemblesAcuracias = arrayEnsemblesAcuracias != null ? Arrays.asList(arrayEnsemblesAcuracias): null;
    }
    
    public boolean possuiLambdas()
    {
    	return listaEnsemblesLambdas != null;
    }
    
    
    public String getCodigo() {
		return codigo;
	}


	public int getIteracao() {
        return iteracao;
    }

    public Diversidades getDiversidades() {
        return diversidades;
    }

    
    public double getTaxaAcerto() {
        return taxaAcerto;
    }

    public double getTaxaErro() {
        return taxaErro;
    }

    public double getAcuraciaPrequencial() {
        return acuraciaPrequencial;
    }

    public double getDesvioAcuraciaPrequencial() {
        return desvioAcuraciaPrequencial;
    }

    public double getErroPrequencial() {
        return erroPrequencial;
    }

    public double getDesvioErroPrequencial() {
        return desvioErroPrequencial;
    }


    public boolean isAcertou() {
        return acertou;
    }

	public double getLambdaSelecionado() {
		return lambdaSelecionado;
	}



	public List<Double> getListaEnsemblesDiversidades() {
		return listaEnsemblesDiversidades;
	}



	public List<Double> getListaEnsemblesAcuracias() {
		return listaEnsemblesAcuracias;
	}



	public List<Double> getListaEnsemblesLambdas() {
		return listaEnsemblesLambdas;
	}
	
	
	
	
}
