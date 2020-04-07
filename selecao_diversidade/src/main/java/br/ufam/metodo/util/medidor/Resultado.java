package br.ufam.metodo.util.medidor;

import java.util.ArrayList;
import java.util.List;

import br.ufam.metodo.diversidade.util.Diversidades;

public class Resultado{ 
    //private String nome;
    private String codigo;
    private List<RegistroIteracao> listaRegistrosIteracoes;
    private List<Integer> logDrifts;
    private double tempo;
    private double ramHours;
    private Double acuraciaMedia;
    private Double acuraciaPrequencialMedia;

    public Resultado() {
        listaRegistrosIteracoes = new ArrayList<>();
        logDrifts = new ArrayList<>();
    }
    
    public void registraTempo(double tempo)
    {
        this.tempo = tempo;
    }
    
    public void registraRamHours(double ramHours)
    {
        this.ramHours = ramHours;
    }
    
    public void registraDrift(int iteracao)
    {
        logDrifts.add(iteracao);
    }
    
    public void registra(int iteracao, String codigo, Diversidades diversidades, Indicadores indicadores, boolean acertou, Double lambdaSelecionado, Double[] listaEnsemblesLambdas, Double[] listaEnsemblesDiversidades, Double[] listaEnsemblesAcuracias)
    {
        listaRegistrosIteracoes.add(new RegistroIteracao(iteracao, codigo, diversidades, indicadores.getTaxaAcertoAtual(), 
        		indicadores.getTaxaErroAtual(), acertou, indicadores.getAcuraciaPrequencial(), 
        		indicadores.getDesvioAcuraciaPrequencial(), indicadores.getErroPrequencial(), indicadores.getDesvioErroPrequencial(),
        		lambdaSelecionado, listaEnsemblesLambdas, listaEnsemblesDiversidades, listaEnsemblesAcuracias));
    }
    
    public void registra(int iteracao, String codigo, Diversidades diversidades, double taxaAcerto, double taxaErro, boolean acertou,
    		double acuraciaPrequencial, double desvioAcuraciaPrequencial, double erroPrequencial, double desvioErroPrequencial, Double lambdaSelecionado, Double[] listaEnsemblesLambdas, Double[] listaEnsemblesDiversidades, Double[] listaEnsemblesAcuracias)
    {
        listaRegistrosIteracoes.add(new RegistroIteracao(iteracao, codigo, diversidades, taxaAcerto, taxaErro, 
        		acertou, acuraciaPrequencial, desvioAcuraciaPrequencial, erroPrequencial, desvioErroPrequencial, lambdaSelecionado, listaEnsemblesLambdas, listaEnsemblesDiversidades, listaEnsemblesAcuracias));
    }

    public List<Integer> getLogDrifts() {
        return logDrifts;
    }
    
    
    
    public RegistroIteracao getRegistro(int iteracao)
    {
        RegistroIteracao registroIteracao = listaRegistrosIteracoes.get(iteracao-1);
        if (registroIteracao.getIteracao() != iteracao)
            throw new RuntimeException("ERRO: ITERACAO INVALIDA!!!");
        
        return registroIteracao;
    }
    
    private void calcular()
    {
    	int i = 0;
    	double soma_taxa = 0;
    	double soma_acc = 0;
        for (RegistroIteracao registro : getListaRegistrosIteracoes()) {
            i++;
            soma_taxa += registro.getTaxaAcerto();
            soma_acc += registro.getAcuraciaPrequencial();

        }

        double tam = i;
        
        double media_taxa = soma_taxa / tam;
        double media_acc = soma_acc / tam;
        
        this.acuraciaMedia = media_taxa;
        this.acuraciaPrequencialMedia = media_acc;
    }
    
    

    public void setAcuraciaMedia(Double acuraciaMedia) {
		this.acuraciaMedia = acuraciaMedia;
	}

	public void setAcuraciaPrequencialMedia(Double acuraciaPrequencialMedia) {
		this.acuraciaPrequencialMedia = acuraciaPrequencialMedia;
	}

	public Double getAcuraciaMedia() {
    	if (acuraciaMedia == null) calcular();
		return acuraciaMedia;
	}

	public Double getAcuraciaPrequencialMedia() {
		if (acuraciaPrequencialMedia == null) calcular();
		return acuraciaPrequencialMedia;
	}

	public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public List<RegistroIteracao> getListaRegistrosIteracoes() {
        return listaRegistrosIteracoes;
    }

    public double getTempo() {
        return tempo;
    }

    public double getRamHours() {
        return ramHours;
    }

}
