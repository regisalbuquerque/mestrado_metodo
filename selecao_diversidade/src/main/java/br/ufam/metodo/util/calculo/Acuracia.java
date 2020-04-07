package br.ufam.metodo.util.calculo;

import com.yahoo.labs.samoa.instances.Instance;

import br.ufam.metodo.util.model.EnsembleOnLineBagging;

import java.util.List;

/**
 *
 * @author regis
 */
public class Acuracia {
    public static double calculaAcuracia(List<Instance> instancias, EnsembleOnLineBagging ensemble)
    {
        double acertos = 0;
        if (instancias == null) return 0;
        for (Instance instancia : instancias) {
            if(ensemble.correctlyClassifies(instancia))
                acertos++;
        }
        return acertos/instancias.size();
    }
}
