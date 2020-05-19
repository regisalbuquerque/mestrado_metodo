package br.ufam.metodo.util.aprendizado;

import br.ufam.metodo.util.model.IEnsembleClassifiers;

public interface MetodoAprendizado {

	IEnsembleClassifiers[] alocaPoolEnsembles(int value);

	IEnsembleClassifiers criaNovoEnsemble();

}
