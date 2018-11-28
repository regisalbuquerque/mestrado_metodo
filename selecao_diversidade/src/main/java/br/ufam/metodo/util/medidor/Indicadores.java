package br.ufam.metodo.util.medidor;

public final class Indicadores {

    private double acertos;
    private double erros;
    private double iteracoes;

    private double acuraciaPrequencial;
    private double acuraciaPrequencialAnterior;
    private double desvioAcuraciaPrequencial;
    private double erroPrequencial;
    private double erroPrequencialAnterior;
    private double desvioErroPrequencial;
    private double iteracoesPrequencial;


    public Indicadores() {
        reset();
        resetPrequencial();
    }

    private void reset() {
        acertos = 0.0;
        erros = 0.0;
        iteracoes = 0.0;
    }

    public void resetPrequencial() {
        erroPrequencial = 1.0;
        erroPrequencialAnterior = 1.0;
        acuraciaPrequencial = 0.0;
        acuraciaPrequencialAnterior = 0.0;
        iteracoesPrequencial = 0.0;
    }

    
    public void acertou() {
        acertos++;

        iteracoes++;
        iteracoesPrequencial++;
        
        calculaPrequenciais(true);
    }

    public void errou() {
        erros++;

        iteracoes++;
        iteracoesPrequencial++;
        
        calculaPrequenciais(false);
    }
    
    private void calculaPrequenciais(boolean acertou)
    {
        double predictionAcc = 0;
        double predictionErro = 1;
        if (acertou)
        {
            predictionAcc = 1;
            predictionErro = 0;
        }
        this.acuraciaPrequencial = this.acuraciaPrequencialAnterior + ( (predictionAcc - this.acuraciaPrequencialAnterior) / this.iteracoesPrequencial );
        this.desvioAcuraciaPrequencial = Math.sqrt( this.acuraciaPrequencial * (1 - this.acuraciaPrequencial) / this.iteracoesPrequencial );
        this.acuraciaPrequencialAnterior = this.acuraciaPrequencial;
        
        
        this.erroPrequencial = this.erroPrequencialAnterior + ( (predictionErro - this.erroPrequencialAnterior) / this.iteracoesPrequencial );
        this.desvioErroPrequencial = Math.sqrt( this.erroPrequencial * (1 - this.erroPrequencial) / this.iteracoesPrequencial );
        this.erroPrequencialAnterior = this.erroPrequencial;
    }

    public double getTaxaAcertoAtual() {
        return this.acertos / (this.acertos + this.erros) * 100.0;
    }

    public double getTaxaErroAtual() {
        return this.erros / (this.acertos + this.erros) * 100.0;
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

    public double getIteracoes() {
        return this.iteracoes;
    }

}
