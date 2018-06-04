package br.ufam.metodo.util.medidas.selecao;

/**
 *
 * @author regis
 */
public class MedidaSelecaoMaior implements MedidaSelecao{

    @Override
    public int seleciona(double[] diversidades, double[] acc, boolean maximizacaoDiv, boolean maximizacaoAcc) {
        int indice_maior = 0;
        for (int i=1; i < diversidades.length; i++) {
            if (diversidades[i] > diversidades[indice_maior])
                indice_maior = i;
        }
        
        return indice_maior;
    }
    
}
