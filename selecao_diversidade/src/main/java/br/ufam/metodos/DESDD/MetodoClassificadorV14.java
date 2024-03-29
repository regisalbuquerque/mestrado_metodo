package br.ufam.metodos.DESDD;

import com.github.javacliparser.FloatOption;

import br.ufam.metodo.util.calculo.Matematica;
import br.ufam.metodo.util.model.DESDDClassifier;

/**
 *
 * @author regis Versão 14
 */
public class MetodoClassificadorV14 extends DESDDClassifier {

	private static final long serialVersionUID = 1L;

	@Override
	public String getPurposeString() {
		return "Classifier that ...";
	}

	public FloatOption lambdaMinOption = new FloatOption("lambdaInfLimit", 'i',
            "Limite Inferior Lambda", 1);
	
	public FloatOption lambdaMaxOption = new FloatOption("lambdaSupLimit", 't',
            "Limite Superior Lambda", 1);

	
	public Double LAMBDA_MIN = null;
	
	public Double LAMBDA_MAX = null;
	
	public Integer LAMBDAS_NUM = null;

	@Override
	public void setupLambdas()
	{
		LAMBDAS_NUM = this.ensemblesNumberOption.getValue();
		LAMBDA_MIN = this.lambdaMinOption.getValue();
		LAMBDA_MAX = this.lambdaMaxOption.getValue();
		
		this.lambdas = new Double[this.poolOfEnsembles.length];
		gerarLambdas();
	}

	@Override
	public void randomizaLambdasEnsembles()
	{
		this.gerarLambdas();
		for (int i = 0; i < this.poolOfEnsembles.length; i++) {
			this.poolOfEnsembles[i].setLambda(this.lambdas[i]);
		}
	}
	
	public void gerarLambdas()
	{
		
		double[] numeros = Matematica.gerarNumerosAleatorios(LAMBDA_MIN, LAMBDA_MAX, LAMBDAS_NUM);
		
		this.lambdas = new Double[LAMBDAS_NUM];
		for (int i = 0; i < LAMBDAS_NUM; i++) {
			this.lambdas[i] = numeros[i];
		}
	}


	
}
