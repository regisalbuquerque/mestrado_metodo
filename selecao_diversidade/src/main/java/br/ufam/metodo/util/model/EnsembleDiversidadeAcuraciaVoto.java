package br.ufam.metodo.util.model;

import java.util.Comparator;

@Deprecated
public  class EnsembleDiversidadeAcuraciaVoto
{
    
    public EnsembleOnLineBagging ensemble;
    public Double diversidade;
    public Double acuracia;
    public Integer voto;

    public EnsembleDiversidadeAcuraciaVoto(EnsembleOnLineBagging ensemble, Double diversidade, Double acuracia, Integer voto) {
        this.ensemble = ensemble;
        this.diversidade = diversidade;
        this.acuracia = acuracia;
        this.voto = voto;
    }

    
    public static Comparator<EnsembleDiversidadeAcuraciaVoto> comparatorDiversidade(){
        Comparator<EnsembleDiversidadeAcuraciaVoto> comparator = new Comparator<EnsembleDiversidadeAcuraciaVoto>() {
            @Override
            public int compare(EnsembleDiversidadeAcuraciaVoto o1, EnsembleDiversidadeAcuraciaVoto o2) {
                return o1.diversidade.compareTo(o2.diversidade);
            }
        };
        return comparator;
    }
    
    public static Comparator<EnsembleDiversidadeAcuraciaVoto> comparatorDiversidadeAcuracia(){
        Comparator<EnsembleDiversidadeAcuraciaVoto> comparator = new Comparator<EnsembleDiversidadeAcuraciaVoto>() {
            @Override
            public int compare(EnsembleDiversidadeAcuraciaVoto o1, EnsembleDiversidadeAcuraciaVoto o2) {
                if (o1.diversidade.compareTo(o2.diversidade)==0){
                    return o2.acuracia.compareTo(o1.acuracia);
                }
                else
                    return o1.diversidade.compareTo(o2.diversidade);
            }
        };
        return comparator;
    }
    
    public static Comparator<EnsembleDiversidadeAcuraciaVoto> comparatorAcuracia(){
        Comparator<EnsembleDiversidadeAcuraciaVoto> comparator = new Comparator<EnsembleDiversidadeAcuraciaVoto>() {
            @Override
            public int compare(EnsembleDiversidadeAcuraciaVoto o1, EnsembleDiversidadeAcuraciaVoto o2) {
                return o2.acuracia.compareTo(o1.acuracia);
            }
        };
        return comparator;
    }
    
    public static Comparator<EnsembleDiversidadeAcuraciaVoto> comparatorAcuraciaDiversidade(){
        Comparator<EnsembleDiversidadeAcuraciaVoto> comparator = new Comparator<EnsembleDiversidadeAcuraciaVoto>() {
            @Override
            public int compare(EnsembleDiversidadeAcuraciaVoto o1, EnsembleDiversidadeAcuraciaVoto o2) {
                if (o2.acuracia.compareTo(o1.acuracia)==0){
                    return o1.diversidade.compareTo(o2.diversidade);
                }
                else
                    return o2.acuracia.compareTo(o1.acuracia);
            }
        };
        return comparator;
    }
    
    public static Comparator<EnsembleDiversidadeAcuraciaVoto> comparatorVoto(){
        Comparator<EnsembleDiversidadeAcuraciaVoto> comparator = new Comparator<EnsembleDiversidadeAcuraciaVoto>() {
            @Override
            public int compare(EnsembleDiversidadeAcuraciaVoto o1, EnsembleDiversidadeAcuraciaVoto o2) {
                return o1.voto.compareTo(o2.voto);
            }
        };
        return comparator;
    }

} 
    
