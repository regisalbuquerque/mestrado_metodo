package br.ufam.metodo.util.medidas.selecao;

import com.yahoo.labs.samoa.instances.Instance;

import br.ufam.metodo.util.model.Ensemble;
import br.ufam.metodo.util.model.EnsembleDiversidadeAcuraciaVoto;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


@Deprecated
public class AnaliseDiversidadeAcuracia {

    
//    List<EnsembleDiversidadeAcuraciaVoto> lista;
//    boolean diversidades_todas_zero = true;
//    boolean votam_mesmo_rotulo = true;
//
//    public AnaliseDiversidadeAcuracia(Instance inst, Ensemble[] ensemblesList) {
//        
//        lista = new ArrayList<>();
//        
//        Ensemble ensemblePrimeiro = ensemblesList[0];
//        int voto_primeiro = ensemblePrimeiro.medidaCalculo.getVotoMV();
//        for (Ensemble ensemble : ensemblesList) {
//
//            //Diversidade
//            Double diversidade = ensemble.calculaDiversidade(inst);
//            if (diversidade != 0) this.diversidades_todas_zero = false;
//            
//            //Voto
//            int voto = ensemble.medidaCalculo.getVotoMV();
//            if (voto_primeiro != voto)
//                votam_mesmo_rotulo = false;
//
//            
//            lista.add(new EnsembleDiversidadeAcuraciaVoto(ensemble, diversidade, ensemble.acc, voto));
//        }
//    }
//    
//    public void calculaAcuracia(List<Instance> baseValidacao)
//    {
//        for (EnsembleDiversidadeAcuraciaVoto ensembleDiversidadeAcuraciaVoto : lista) {
//            ensembleDiversidadeAcuraciaVoto.acuracia = ensembleDiversidadeAcuraciaVoto.ensemble.calculaAcuracia(baseValidacao);
//        }
//    }
//    
//    public Ensemble ensembleMenorDiversidade()
//    {
//        Collections.sort(lista, EnsembleDiversidadeAcuraciaVoto.comparatorDiversidade());
//        return lista.get(0).ensemble;
//    }
//    
//    public Ensemble ensembleMenorDiversidadeMaiorAcuracia()
//    {
//        Collections.sort(lista, EnsembleDiversidadeAcuraciaVoto.comparatorDiversidadeAcuracia());
//        return lista.get(0).ensemble;
//    }
//    
//    public Ensemble ensembleMaiorAcuraciaMenorDiversidade()
//    {
//        Collections.sort(lista, EnsembleDiversidadeAcuraciaVoto.comparatorAcuraciaDiversidade());
//        return lista.get(0).ensemble;
//    }
//    
//    public Ensemble ensembleAnaliseParetoMaiorAcc()
//    {
//        AnalisePareto analisePareto = new AnalisePareto();
//        
//        for (EnsembleDiversidadeAcuraciaVoto valor : lista) {
//            analisePareto.addSolucao(new ValorValor(valor.diversidade, valor.acuracia));
//        }
//        analisePareto.analisa();
//        
//        //Lista pareto front
//        List<EnsembleDiversidadeAcuraciaVoto> listaPareto = new ArrayList<>();
//        
//        for (EnsembleDiversidadeAcuraciaVoto valor : lista) {
//            if (analisePareto.isParetoFrontMember(new ValorValor(valor.diversidade, valor.acuracia)))
//                listaPareto.add(valor);
//        }
//        
//        Collections.sort(listaPareto, EnsembleDiversidadeAcuraciaVoto.comparatorAcuracia());
//        
//        return listaPareto.get(0).ensemble;
//                
//        /*
//               10 ensembles: E1, E2,…E10
//               D1 e A1, D2 e A2… D10 e A10
//               D1 < D2 e A1 > A2
//               E1 é melhor do que E2
//               */
//    }
//    
//    
//    public boolean isTodasDiversidadesZero()
//    {
//        return diversidades_todas_zero;
//    }
//    
//    public boolean isVotamNoMesmoRotulo()
//    {
//        return votam_mesmo_rotulo;
//    }
//    

    
}
