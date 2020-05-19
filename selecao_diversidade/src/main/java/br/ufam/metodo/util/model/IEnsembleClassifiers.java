package br.ufam.metodo.util.model;

import moa.classifiers.Classifier;
import moa.classifiers.core.driftdetection.ChangeDetector;

public interface IEnsembleClassifiers extends Classifier{
	
	public void setLambda(double lambda);
	public double getLambda();
	
	public void setSize(int size);
//	public void setNumBaseLearners(int num);
	
	public void setBaseLearner(String baseLearner);
//	public void setBaseLearner2(String baseLearner);
//	public void setBaseLearner3(String baseLearner);
//	public void setBaseLearner4(String baseLearner);
//	public void setBaseLearner5(String baseLearner);
	
	public void setChangeDetector(ChangeDetector changeDetector);
	
    public boolean detectouDrift();
    
    public void estrategiaSimpleReset();

}
