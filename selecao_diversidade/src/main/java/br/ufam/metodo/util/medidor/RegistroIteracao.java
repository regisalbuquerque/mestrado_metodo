package br.ufam.metodo.util.medidor;

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
    		Double lambdaSelecionado) {
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
}
