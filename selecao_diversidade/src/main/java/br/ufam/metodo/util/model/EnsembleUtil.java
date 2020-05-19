package br.ufam.metodo.util.model;

import com.github.javacliparser.FloatOption;
import com.yahoo.labs.samoa.instances.Instance;

import moa.classifiers.Classifier;
import moa.classifiers.core.driftdetection.ADWIN;
import moa.classifiers.core.driftdetection.ChangeDetector;

public class EnsembleUtil{
	
	public FloatOption deltaAdwinOption = new FloatOption("deltaAdwin", 'a',
            "Delta of Adwin change detection", 0.002, 0.0, 1.0);
	
	private ChangeDetector changeDetector;
	
	private Classifier baseLearner;
	private ADWIN[] ADError; //Para cada classificador
	private ChangeDetector[] DETECTOR;
	private boolean drift;
	private double lambda;
	
	public void prepare(Classifier baseLearner, Classifier[] ensemble) {
        this.baseLearner = baseLearner;
        this.baseLearner.resetLearning();
        
        for (int i=0; i < ensemble.length; i++) {
            ensemble[i] = baseLearner.copy(); 
        }
        
        //Cria os ADWIN(Estimador de erro) e um DETECTOR para cada Classificador
        this.ADError = new ADWIN[ensemble.length];
        this.DETECTOR = new ChangeDetector[ensemble.length];
        for (int i = 0; i < ensemble.length; i++) {
            this.ADError[i] = new ADWIN((double) this.deltaAdwinOption.getValue());
            this.DETECTOR[i] = changeDetector.copy();
        }
	
	}
	
	public void praparaTreinamento()
	{
		this.setDrift(false);  // A priori DRIFT igual a FALSE
	}
	
	public void computaClassificadorDrift(Classifier classificador, Instance inst, int i) {
		boolean correctlyClassifies = classificador.correctlyClassifies(inst);
		
		this.ADError[i].setInput(correctlyClassifies ? 0 : 1); //Para poder encontrar o PIOR
		
		this.DETECTOR[i].input(correctlyClassifies ? 0 : 1);
		if (this.DETECTOR[i].getChange())
		{
			this.setDrift(true);
		}
	}
	
	public void estrategiaSimpleReset(Classifier[] ensemble) {
    	double max = 0.0;
        int imax = -1;
        for (int i = 0; i < ensemble.length; i++) {
            if (max < this.ADError[i].getEstimation()) {
                max = this.ADError[i].getEstimation();
                imax = i;
            }
        }
        if (imax != -1) {
            ensemble[imax].resetLearning();
            this.ADError[imax] = new ADWIN((double) this.deltaAdwinOption.getValue());
            this.DETECTOR[imax] = changeDetector.copy(); 
        }
	}
	
	
	// Getters & Setters ...

	public void setChangeDetector(ChangeDetector changeDetector) {
		this.changeDetector = changeDetector;
	}
	public boolean isDrift() {
		return drift;
	}
	private void setDrift(boolean drift) {
		this.drift = drift;
	}
	public double getLambda() {
		return lambda;
	}
	public void setLambda(double lambda) {
		this.lambda = lambda;
	}

}
