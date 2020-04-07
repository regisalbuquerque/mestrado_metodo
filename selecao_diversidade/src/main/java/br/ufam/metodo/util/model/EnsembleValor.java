package br.ufam.metodo.util.model;

public  class EnsembleValor implements Comparable<EnsembleValor>
{
    public Double valor;
    public EnsembleOnLineBagging ensemble;

    public EnsembleValor(Double valor, EnsembleOnLineBagging ensemble)
    {
        this.valor = valor;
        this.ensemble = ensemble;
    }


    @Override
    public int compareTo(EnsembleValor o) {
        return this.valor.compareTo(o.valor);
    }
} 
    
