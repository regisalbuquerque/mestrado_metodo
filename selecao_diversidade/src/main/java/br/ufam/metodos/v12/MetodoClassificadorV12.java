package br.ufam.metodos.v12;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.github.javacliparser.FloatOption;
import com.github.javacliparser.IntOption;
import com.github.javacliparser.ListOption;
import com.github.javacliparser.MultiChoiceOption;
import com.github.javacliparser.Option;
import com.yahoo.labs.samoa.instances.Instance;

import br.ufam.diversidade.MedidaCalculo;
import br.ufam.diversidade.MedidaCalculoFactory;
import br.ufam.metodo.util.calculo.Acuracia;
import br.ufam.metodo.util.calculo.AcuraciaPrequencial;
import br.ufam.metodo.util.calculo.DiversidadePrequencial;
import br.ufam.metodo.util.dados.BufferInstancias;
import br.ufam.metodo.util.drift.DetectorDrift;
import br.ufam.metodo.util.medidas.selecao.MedidaSelecao;
import br.ufam.metodo.util.medidas.selecao.MedidaSelecaoFactory;
import br.ufam.metodo.util.medidor.Indicadores;
import br.ufam.metodo.util.medidor.Resultado;
import br.ufam.metodo.util.model.DESDDClassifier;
import br.ufam.metodo.util.model.Ensemble;
import br.ufam.metodo.util.model.EnsembleValor;
import br.ufam.metodo.util.model.IEnsembleSelection;
import br.ufam.metodo.util.model.IEnsemblesResultados;
import moa.classifiers.AbstractClassifier;
import moa.classifiers.Classifier;
import moa.classifiers.core.driftdetection.ChangeDetector;
import moa.core.Measurement;
import moa.core.Utils;
import moa.options.ClassOption;

/**
 *
 * @author regis Versão 12
 */
public class MetodoClassificadorV12 extends DESDDClassifier {

	private static final long serialVersionUID = 1L;

	@Override
	public String getPurposeString() {
		return "Classifier that ...";
	}
	
	public ListOption lambdasOption = new ListOption("lambdaAttributes", 'y',
			"Lambdas of ensembles. Enter comma seperated list, ", new FloatOption("lambdaAttribute", ' ', "Lambda", 1),
			new FloatOption[] { new FloatOption("", ' ', "", 1.0), new FloatOption("", ' ', "", 1.0),
					new FloatOption("", ' ', "", 1.0), new FloatOption("", ' ', "", 1.0),
					new FloatOption("", ' ', "", 1.0), new FloatOption("", ' ', "", 1.0),
					new FloatOption("", ' ', "", 1.0), new FloatOption("", ' ', "", 1.0),
					new FloatOption("", ' ', "", 1.0), new FloatOption("", ' ', "", 1.0) },
			',');

	@Override
	public void setupLambdas() {
		Option[] lambdasArray = lambdasOption.getList();
		if (lambdasArray.length < this.poolOfEnsembles.length)
			throw new RuntimeException("ERRO encontrado no parâmetro LAMBDA(y)");
		this.lambdas = new double[this.poolOfEnsembles.length];
		for (int i = 0; i < this.poolOfEnsembles.length; i++) {
			this.lambdas[i] = ((FloatOption) lambdasArray[i]).getValue();
		}
		
	}
	
}
