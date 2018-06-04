package br.ufam.diversidade;

import br.ufam.diversidade.impl.AmbiguidadeCalculoDiversidade;
import br.ufam.diversidade.impl.CorrelationCalculoDiversidade;
import br.ufam.diversidade.impl.DisagreementCalculoDiversidade;
import br.ufam.diversidade.impl.DoubleFaultCalculoDiversidade;
import br.ufam.diversidade.impl.MargemCalculoDiversidade;
import br.ufam.diversidade.impl.QstatisticsCalculoDiversidade;

public class MedidaCalculoFactory {
	public static MedidaCalculo fabrica(String medida)
    {
        if (medida.equals("Ambiguidade"))
            return new AmbiguidadeCalculoDiversidade();
        
        if (medida.equals("Margem"))
            return new MargemCalculoDiversidade();
        
        if (medida.equals("Qstatistics"))
            return new QstatisticsCalculoDiversidade();
        
        if (medida.equals("Correlation"))
            return new CorrelationCalculoDiversidade();
        
        if (medida.equals("Disagreement"))
            return new DisagreementCalculoDiversidade();
        
        if (medida.equals("DoubleFault"))
            return new DoubleFaultCalculoDiversidade();

        return null;
    }
}
