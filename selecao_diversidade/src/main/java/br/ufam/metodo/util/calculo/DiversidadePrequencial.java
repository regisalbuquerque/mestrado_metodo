package br.ufam.metodo.util.calculo;

import com.yahoo.labs.samoa.instances.Instance;

import br.ufam.diversidade.MedidaCalculo;
import moa.classifiers.Classifier;

public class DiversidadePrequencial {

	private double div;
	private Predicoes predicoes;
	private MedidaCalculo medidaCalculo;
	
	private int numClassificadores;
	private Integer slidingWindow;
	private boolean usaSlidingWindow;
	
	public DiversidadePrequencial(int numClassificadores, Integer slidingWindow, MedidaCalculo medidaCalculo) {
		if (slidingWindow == null || slidingWindow == -1 || slidingWindow == 0)
		{
			this.usaSlidingWindow = false;
			this.slidingWindow = null;
		}
		else
		{
			this.usaSlidingWindow = true;
			this.slidingWindow = slidingWindow;
		}
		this.numClassificadores = numClassificadores;
		this.medidaCalculo = medidaCalculo;
		
		reset();
	}
	
	public void reset()
    {
        div = 0.0;
       	predicoes = new Predicoes(this.numClassificadores, this.slidingWindow);
        	
    }
	
	public void calcula(Classifier[] classificadores, Instance instancia) 
	{
		if (usaSlidingWindow)
			predicoes.incluiInstancia(classificadores, instancia);
		else
			predicoes.incluiInstanciaOnline(classificadores, instancia);
		
		
		this.div = medidaCalculo.calcula(predicoes);
	}
	
	
	
	public double getDiv() {
		return div;
	}
	
	
	public static double[] getArray(DiversidadePrequencial diversidades[])
    {
        double array[] = new double[diversidades.length];
        for (int i = 0; i < diversidades.length; i++) {
            array[i] = diversidades[i].getDiv();
        }
        return array;
    }

	public Predicoes getPredicoes() {
		return predicoes;
	}
	
	
	
}
