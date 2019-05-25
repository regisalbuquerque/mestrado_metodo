package br.ufam.metodo.util.calculo;

import com.yahoo.labs.samoa.instances.Instance;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Map.Entry;

public class Matematica {

    @Deprecated //Pois já existe no MOA
    public static int getPoisson(double lambda) {
        //http://stackoverflow.com/questions/1241555/algorithm-to-generate-poisson-and-binomial-random-numbers
        //http://www.johndcook.com/blog/2010/06/14/generating-poisson-random-values/
        double L = Math.exp(-lambda);
        double p = 1.0;
        int k = 0;

        do {
            k++;
            p *= Math.random();
        } while (p > L);

        return k - 1;
    }
    
    
    public static double gerarNumeroAleatorio(double limInferior, double limSuperior)
	{
		Random r = new Random();
		double randomValue = limInferior + (limSuperior - limInferior) * r.nextDouble();
		
		return randomValue;
	}
    
    public static double[] gerarNumerosAleatorios(double limInferior, double limSuperior, int  quantidade)
	{
    	double[] numeros = new double[quantidade];
    	
    	double distancia = limSuperior - limInferior;
    	double parte = distancia / (double)quantidade;
 	
    	double limInferior_aux = limInferior;
    	double limSuperior_aux = limSuperior;
    	for (int i = 0; i < quantidade; i++) {
			limSuperior_aux = limInferior_aux + parte;
			numeros[i] = gerarNumeroAleatorio(limInferior_aux, limSuperior_aux);
			limInferior_aux = limSuperior_aux;
		}
    	
		return numeros;
	}
    
    public static String gerarNumerosAleatoriosString(double limInferior, double limSuperior, int quantidade)
	{
    	String retorno = "";
    	String sep = ",";
    	for (int i = 0; i < quantidade; i++)
    	{
    		 retorno += Matematica.gerarNumeroAleatorio(limInferior, limSuperior);
    		 if (i < quantidade -1)
    			 retorno += sep;
    	}
    	return retorno;
	}
    

	
    
    
    public static <T> T mostCommon(List<T> list) {
        Map<T, Integer> map = new HashMap<>();

        for (T t : list) {
            Integer val = map.get(t);
            map.put(t, val == null ? 1 : val + 1);
        }

        Entry<T, Integer> max = null;

        for (Entry<T, Integer> e : map.entrySet()) {
            if (max == null || e.getValue() > max.getValue())
                max = e;
        }

        return max.getKey();
    }
    
    public static <T> T secondMostCommon(List<T> list) {
        Map<T, Integer> map = new HashMap<>();

        for (T t : list) {
            Integer val = map.get(t);
            map.put(t, val == null ? 1 : val + 1);
        }

        Entry<T, Integer> max = null;
        
        //Encontra o mais frequente
        for (Entry<T, Integer> e : map.entrySet()) {
            if (max == null || e.getValue() > max.getValue())
            {
                max = e;
            }
        }
        
        //Encontra o segundo mais frequente
        //Remove o mais frequente
        map.remove(max.getKey());
        //Procura o mais frequente 
        Entry<T, Integer> maxSecond = null;
        for (Entry<T, Integer> e : map.entrySet()) {
            if (maxSecond == null || e.getValue() > maxSecond.getValue())
            {
            	maxSecond = e;
            }
        }
        
        if (maxSecond == null) return null; //Não houve segundo lugar

        return maxSecond.getKey();
    }
    
    
    public double mediana(Object[] array)
    {
        double median;
        if (array.length % 2 == 0)
            median = ((double)array[array.length/2] + (double)array[array.length/2 - 1])/2;
        else
            median = (double) array[array.length/2];
        
        return median;
    }
    
    public static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        BigDecimal bd = new BigDecimal(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }
    
    public static double euclidean(double[] x, double[] y){ 
        double sum = 0; 
        for (int i=0; i< Math.min(x.length, y.length); i++){ 
            sum += Math.pow(x[i] - y[i], 2); 
        } 
         
        return Math.sqrt(sum); 
    } 
    
    public static double euclidean(Instance x, Instance z){ 
        
        int numAtributos = x.numInputAttributes();
        
        double sum = 0; 
        for (int i=0; i< numAtributos; i++){ 
            sum += Math.pow(x.value(i) - z.value(i), 2); 
        } 
         
        return Math.sqrt(sum); 
    } 


}
