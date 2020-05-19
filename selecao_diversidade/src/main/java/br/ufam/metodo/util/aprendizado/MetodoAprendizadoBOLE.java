package br.ufam.metodo.util.aprendizado;

import br.ufam.metodo.util.model.IEnsembleClassifiers;
import br.ufam.metodos.modificados.BOLEModificado;

public class MetodoAprendizadoBOLE implements MetodoAprendizado{

	@Override
	public IEnsembleClassifiers[] alocaPoolEnsembles(int size) {
		return new BOLEModificado[size];
	}

	@Override
	public IEnsembleClassifiers criaNovoEnsemble() {
		return new BOLEModificado();
	}

}
