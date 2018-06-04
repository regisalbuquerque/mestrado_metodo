package br.ufam.metodo.util.model;

import com.yahoo.labs.samoa.instances.Instance;

public  class InstanciaDistancia implements Comparable<InstanciaDistancia>
{
    public Instance instance;
    public Double distancia;

    public InstanciaDistancia(Instance instance, Double distancia) {
        this.instance = instance;
        this.distancia = distancia;
    }


    @Override
    public int compareTo(InstanciaDistancia o) {
        return this.distancia.compareTo(o.distancia);
    }
} 
    
