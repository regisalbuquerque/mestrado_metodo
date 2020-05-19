package br.ufam.metodo.util.aprendizado;

import br.ufam.metodo.util.model.IEnsembleClassifiers;
import br.ufam.metodos.modificados.ADOBModificado;

public class MetodoAprendizadoADOB implements MetodoAprendizado{

	@Override
	public IEnsembleClassifiers[] alocaPoolEnsembles(int size) {
		return new ADOBModificado[size];
	}

	@Override
	public IEnsembleClassifiers criaNovoEnsemble() {
		return new ADOBModificado();
	}

}
