package br.ufam.metodo.diversidade.util;

import br.ufam.diversidade.MedidaCalculo;
import br.ufam.diversidade.impl.AmbiguidadeCalculoDiversidade;
import br.ufam.diversidade.impl.CorrelationCalculoDiversidade;
import br.ufam.diversidade.impl.DisagreementCalculoDiversidade;
import br.ufam.diversidade.impl.DoubleFaultCalculoDiversidade;
import br.ufam.diversidade.impl.MargemCalculoDiversidade;
import br.ufam.diversidade.impl.QstatisticsCalculoDiversidade;
import br.ufam.metodo.util.calculo.Predicoes;

/**
 *
 * @author regis
 */
public class DiversidadeUtil {

    public static Diversidades calculaDiversidades(Predicoes predicoes)
    {

        Diversidades diversidades = new Diversidades();
        
    	MedidaCalculo ambiguidade = new AmbiguidadeCalculoDiversidade();
    	//MedidaCalculo margem = new MargemCalculoDiversidade();
    	
    	//MedidaCalculo qstatistics = new QstatisticsCalculoDiversidade();
    	//MedidaCalculo correlation = new CorrelationCalculoDiversidade();
    	//MedidaCalculo disagreement = new DisagreementCalculoDiversidade();
    	//MedidaCalculo doublefault = new DoubleFaultCalculoDiversidade();
    
        
        
        diversidades.setAmbiguidade(ambiguidade.calcula(predicoes));
        //diversidades.setMargem(margem.calcula(predicoes));
        
        //diversidades.setCorrelation(correlation.calcula(predicoes));
        //diversidades.setDisagreement(disagreement.calcula(predicoes));
        //diversidades.setDoubleFault(doublefault.calcula(predicoes));
        //diversidades.setQstatistics(qstatistics.calcula(predicoes));

        return diversidades;
    }
    
    
}
