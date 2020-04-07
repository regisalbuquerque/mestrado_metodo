package br.ufam.metodo.util.model;

import moa.classifiers.Classifier;

public interface IEnsembleSelection {
	public Classifier[][] getClassifiers(); 
	public int getUltimoEnsembleSelecionadoIndex();
	public double getUltimoEnsembleSelecionadoLambda();
	public Double[] getEnsemblesLambdas();
	public Double[] getUltimasDiversidades();
	public Double[] getUltimasAccs();
}
