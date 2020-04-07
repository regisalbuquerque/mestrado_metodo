package br.ufam.metodo.util.medidas.selecao;


/**
 *
 * @author regis
 */
public class MedidaSelecaoMenor implements MedidaSelecao{

    @Override
    public int seleciona(Double[] diversidades, Double[] acc, boolean maximizacaoDiv, boolean maximizacaoAcc) {
        int indice_menor = 0;
        for (int i=1; i < diversidades.length; i++) {
            if (diversidades[i] < diversidades[indice_menor])
                indice_menor = i;
        }
        
        return indice_menor;
    }
    
}
