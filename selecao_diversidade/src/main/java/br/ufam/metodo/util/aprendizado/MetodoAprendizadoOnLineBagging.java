package br.ufam.metodo.util.aprendizado;

import br.ufam.metodo.util.model.IEnsembleClassifiers;
import br.ufam.metodos.modificados.OzaBagModificado;

public class MetodoAprendizadoOnLineBagging implements MetodoAprendizado{

	@Override
	public IEnsembleClassifiers[] alocaPoolEnsembles(int size) {
		return new OzaBagModificado[size];
	}

	@Override
	public IEnsembleClassifiers criaNovoEnsemble() {
		return new OzaBagModificado();
	}

}
