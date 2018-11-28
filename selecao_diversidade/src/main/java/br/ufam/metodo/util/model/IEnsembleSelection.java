package br.ufam.metodo.util.model;

import moa.classifiers.Classifier;

public interface IEnsembleSelection {
	public Classifier[][] getClassifiers(); 
	public int getUltimoEnsembleSelecionadoIndex();
	public double getUltimoEnsembleSelecionadoLambda();
}
