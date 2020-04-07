package br.ufam.metodo.util.calculo;

/**
 *
 * @author regis
 */
public final class AcuraciaPrequencial {
    
    private double acc_ex;
    private double acc;
    private double iteracoes;
    
    public AcuraciaPrequencial()
    {
        reset();
    }
    
    public void reset()
    {
        acc_ex = 0.0;
        acc = 0.0;
        iteracoes = 0.0;
    }
    
    public void acertou()
    {
        double predictionAcc = 1;
        
        iteracoes++;
        acc = acc_ex + ( (predictionAcc - acc_ex) / iteracoes );
        acc_ex = acc;
    }
    
    public void errou()
    {
        double predictionAcc = 0;
        
        iteracoes++;
        acc = acc_ex + ( (predictionAcc - acc_ex) / iteracoes );
        acc_ex = acc;
    }

    public double getAcc() {
        return acc;
    }
    
    public static Double[] getArray(AcuraciaPrequencial acuracias[])
    {
        Double array[] = new Double[acuracias.length];
        for (int i = 0; i < acuracias.length; i++) {
            array[i] = acuracias[i].getAcc();
        }
        return array;
    }
    
}
