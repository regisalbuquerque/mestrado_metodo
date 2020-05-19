package br.ufam.metodo.util.aprendizado;

import br.ufam.metodo.util.model.IEnsembleClassifiers;
import br.ufam.metodos.modificados.OzaBoostModificado;

public class MetodoAprendizadoOnLineBoosting implements MetodoAprendizado{

	@Override
	public IEnsembleClassifiers[] alocaPoolEnsembles(int size) {
		return new OzaBoostModificado[size];
	}

	@Override
	public IEnsembleClassifiers criaNovoEnsemble() {
		return new OzaBoostModificado();
	}

}
