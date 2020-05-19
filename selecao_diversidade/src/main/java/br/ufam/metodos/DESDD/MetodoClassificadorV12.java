package br.ufam.metodos.DESDD;

import com.github.javacliparser.FloatOption;
import com.github.javacliparser.ListOption;
import com.github.javacliparser.Option;

import br.ufam.metodo.util.model.DESDDClassifier;

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
		this.lambdas = new Double[this.poolOfEnsembles.length];
		for (int i = 0; i < this.poolOfEnsembles.length; i++) {
			this.lambdas[i] = ((FloatOption) lambdasArray[i]).getValue();
		}
		
	}
	
}
