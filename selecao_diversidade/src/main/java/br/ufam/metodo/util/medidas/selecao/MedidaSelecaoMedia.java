package br.ufam.metodo.util.medidas.selecao;

import java.util.Map;
import java.util.TreeMap;

/**
 *
 * @author regis
 */
public class MedidaSelecaoMedia implements MedidaSelecao{

    @Override
    public int seleciona(Double[] diversidades, Double[] acc, boolean maximizacaoDiv, boolean maximizacaoAcc) {
        Integer indexMedia = -1;
        Double ensembleMediaAmbiguidadeLocal = 0.0;
        Double soma = 0.0;
        
        //Calcular as ambiguidades - TreeMap (Ordem Crescente)
        TreeMap<Double, Integer> map = new TreeMap<>();
        for (int i=0; i < diversidades.length; i++) {
            map.put(diversidades[i], i);
            soma += diversidades[i];
        }
        
        Double media = soma / (double)diversidades.length;
        
        //Procurar o ensemble que mais se aproxima da média - ALGORITMO PODE SER MELHORADO
        for (Map.Entry<Double, Integer> entry : map.entrySet()) {
            Double key = entry.getKey();
            Integer value = entry.getValue();
            
            if (indexMedia == -1 || Math.abs(key-media) < Math.abs(ensembleMediaAmbiguidadeLocal-media))
            {
                indexMedia = value;
                ensembleMediaAmbiguidadeLocal = key;
            }
        }

        return indexMedia;
    }
    
}
