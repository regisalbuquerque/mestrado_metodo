package br.ufam.metodo.util.medidas.selecao;

/**
 *
 * @author regis
 */
public class MedidaSelecaoMaiorAcc implements MedidaSelecao{

    @Override
    public int seleciona(Double[] diversidades, Double[] acc, boolean maximizacaoDiv, boolean maximizacaoAcc) {
        int indice_maior = 0;
        for (int i=1; i < acc.length; i++) {
            if (acc[i] > acc[indice_maior])
                indice_maior = i;
        }
        
        return indice_maior;
    }
    
}
