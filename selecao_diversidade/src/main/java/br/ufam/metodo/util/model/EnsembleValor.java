package br.ufam.metodo.util.model;

public  class EnsembleValor implements Comparable<EnsembleValor>
{
    public Double valor;
    public Ensemble ensemble;

    public EnsembleValor(Double valor, Ensemble ensemble)
    {
        this.valor = valor;
        this.ensemble = ensemble;
    }


    @Override
    public int compareTo(EnsembleValor o) {
        return this.valor.compareTo(o.valor);
    }
} 
    
