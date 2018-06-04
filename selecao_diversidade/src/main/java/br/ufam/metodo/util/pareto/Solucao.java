package br.ufam.metodo.util.pareto;

import java.util.Comparator;
import java.util.Objects;

/**
 *
 * @author regis
 */
public class Solucao {
    private int index;
    private Double valor1;
    private Double valor2;

    public Solucao(int index, Double valor1, Double valor2) {
        this.index = index;
        this.valor1 = valor1;
        this.valor2 = valor2;
    }
    
    // -- Comparators ------------------------
    
    public static Comparator<Solucao> comparatorValor1_Crescente(){
        Comparator<Solucao> comparator = new Comparator<Solucao>() {
            @Override
            public int compare(Solucao o1, Solucao o2) {
                return o1.getValor1().compareTo(o2.getValor1());
            }
        };
        return comparator;
    }
    
    public static Comparator<Solucao> comparatorValor1_Decrescente(){
        Comparator<Solucao> comparator = new Comparator<Solucao>() {
            @Override
            public int compare(Solucao o1, Solucao o2) {
                return o2.getValor1().compareTo(o1.getValor1());
            }
        };
        return comparator;
    }
    
    public static Comparator<Solucao> comparatorValor2_Crescente(){
        Comparator<Solucao> comparator = new Comparator<Solucao>() {
            @Override
            public int compare(Solucao o1, Solucao o2) {
                return o1.getValor2().compareTo(o2.getValor2());
            }
        };
        return comparator;
    }
    
    public static Comparator<Solucao> comparatorValor2_Decrescente(){
        Comparator<Solucao> comparator = new Comparator<Solucao>() {
            @Override
            public int compare(Solucao o1, Solucao o2) {
                return o2.getValor2().compareTo(o1.getValor2());
            }
        };
        return comparator;
    }
    

    // -- Equals ----------------------------
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Solucao other = (Solucao) obj;
        if (!Objects.equals(this.valor1, other.valor1)) {
            return false;
        }
        if (!Objects.equals(this.valor2, other.valor2)) {
            return false;
        }
        return true;
    }
    
    
    
    
    
    // -- Getters & Setters ---------------------

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public Double getValor1() {
        return valor1;
    }

    public void setValor1(Double valor1) {
        this.valor1 = valor1;
    }

    public Double getValor2() {
        return valor2;
    }

    public void setValor2(Double valor2) {
        this.valor2 = valor2;
    }
    
    
}
