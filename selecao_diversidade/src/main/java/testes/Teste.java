package testes;

import javax.naming.LimitExceededException;

/**
 *
 * @author regis
 */
public class Teste {
    public static void main(String[] args) {
    	
    	double limInferior = 0.001;
    	double limSuperior = 100;
    	int quantidade = 11;
    	
    	double distancia = limSuperior - limInferior;
    	double parte = distancia / (double)quantidade;
    	
    	System.out.println(distancia);
    	System.out.println(parte);
    	

    	double limInferior_aux = limInferior;
    	double limSuperior_aux = limSuperior;
    	for (int i = 0; i < quantidade; i++) {
    		System.out.println(" --- Parte " + i + " --- ");
    		System.out.println(limInferior_aux);
			limSuperior_aux = limInferior_aux + parte;
			System.out.println(limSuperior_aux);
			limInferior_aux = limSuperior_aux;
		}
    }
}
