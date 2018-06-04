package br.ufam.metodo.util.calculo;

import java.util.ArrayList;
import java.util.List;

import com.yahoo.labs.samoa.instances.Instance;

import moa.classifiers.Classifier;
import moa.core.Utils;

public class Predicoes {
	
	List<List<Integer>> listaPredicoesIteracoesClassificadores;
	List<List<Integer>> listaPredicoesClassificadoresIteracoes;
	List<Integer> listaYTrue;
	
	
	int numClassificadores;
	Integer slidingWindow;
	boolean usaSlidingWindow;
	
	public Predicoes(int numClassificadores, Integer slidindWindow)
	{
		if (slidindWindow == null)
		{
			this.usaSlidingWindow = false;
			this.slidingWindow = null;
		}
		else
		{
			this.usaSlidingWindow = true;
			this.slidingWindow = slidindWindow;
		}
		
		init(numClassificadores);
	}
	

	public void init(int numClassificadores)
	{
		this.numClassificadores = numClassificadores;
		
		listaYTrue = new ArrayList<>();
		
		listaPredicoesIteracoesClassificadores = new ArrayList<>();
		listaPredicoesClassificadoresIteracoes = new ArrayList<>();
		
		for (int c = 0; c < numClassificadores; c++)
		{
			listaPredicoesClassificadoresIteracoes.add(new ArrayList<>());
		}
	}
	
	public void incluiInstanciaOnline(Classifier[] classificadores, Instance instancia) {
		
		init(this.numClassificadores); //Zera as predições
		
		incluiInstancia(classificadores, instancia);
	}
	
	public void incluiInstancia(Classifier[] classificadores, Instance instancia) {
		
		List<Integer> listaVotos = new ArrayList<>();
		int trueClass = (int) instancia.classValue();
		
        //Teste
        // c => classificador
        for (int c = 0; c < classificadores.length; c++) 
        {
            Classifier membroClassificador = classificadores[c];
            
            double[] votos = membroClassificador.getVotesForInstance(instancia);
            int prediction_classificador = Utils.maxIndex(votos);
            
            listaVotos.add(prediction_classificador);
        }
        
        addVotos(listaVotos, trueClass);
	}
	
	
	
	public void addVotos(List<Integer> votos, Integer yTrue)
	{
		listaYTrue.add(yTrue);
		
		listaPredicoesIteracoesClassificadores.add(votos); //Adiciona na lista 1
		
		for (int c = 0; c < numClassificadores; c++)       //Adiciona na lista 2
		{
			listaPredicoesClassificadoresIteracoes.get(c).add(votos.get(c)); 
		}
		
		//SLIDING WINDOW
		if (this.usaSlidingWindow && listaPredicoesIteracoesClassificadores.size() > this.slidingWindow)
		{
			listaYTrue.remove(0);
			
			listaPredicoesIteracoesClassificadores.remove(0); //Remove elemento mais antigo na lista 1
			
			for (int c = 0; c < numClassificadores; c++)      //Remove elemento mais antigo na lista 2
			{
				listaPredicoesClassificadoresIteracoes.get(c).remove(0); 
			}
		}
	}

	public List<List<Integer>> getListaPredicoesIteracoesClassificadores() {
		return listaPredicoesIteracoesClassificadores;
	}

	public List<List<Integer>> getListaPredicoesClassificadoresIteracoes() {
		return listaPredicoesClassificadoresIteracoes;
	}

	public List<Integer> getListaYTrue() {
		return listaYTrue;
	}
	
	
	
}
