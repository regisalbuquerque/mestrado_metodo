package br.ufam.metodo.util.medidas.selecao;

/**
 *
 * @author regis
 */
public class MedidaSelecaoFactory {
    public static MedidaSelecao fabrica(String medida)
    {
        if (medida.equals("Menor"))
            return new MedidaSelecaoMenor();
        
        if (medida.equals("Maior"))
            return new MedidaSelecaoMaior();
        
        if (medida.equals("MaiorAcc"))
            return new MedidaSelecaoMaiorAcc();
        
        if (medida.equals("Media"))
            return new MedidaSelecaoMedia();
        
        if (medida.equals("Mediana"))
            return new MedidaSelecaoMediana();
        
        if (medida.equals("Pareto"))
            return new MedidaSelecaoPareto();
        return null;
    }
}
