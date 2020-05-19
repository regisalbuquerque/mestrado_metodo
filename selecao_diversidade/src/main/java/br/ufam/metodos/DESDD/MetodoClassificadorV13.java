package br.ufam.metodos.DESDD;

import com.github.javacliparser.FloatOption;
import com.github.javacliparser.ListOption;
import com.github.javacliparser.Option;

import br.ufam.metodo.util.calculo.Matematica;
import br.ufam.metodo.util.model.DESDDClassifier;

/**
 *
 * @author regis Versão 13
 */
public class MetodoClassificadorV13 extends DESDDClassifier {

	private static final long serialVersionUID = 1L;

	@Override
	public String getPurposeString() {
		return "Classifier that ...";
	}

	public ListOption lambdasOption = new ListOption("lambdaAttributes", 'y',
			"Lambdas of ensembles. Enter comma seperated list, ", 
			new FloatOption("lambdaAttribute", ' ', "Lambda", 1),
			new FloatOption[] {  },
			',');
	
	public static FloatOption lambdaMinOption = new FloatOption("lambdaInfLimit", 'i',
            "Limite Inferior Lambda", 1);
	
	public static FloatOption lambdaMaxOption = new FloatOption("lambdaSupLimit", 't',
            "Limite Superior Lambda", 1);
	

	
	public static Double[] lambdas_static;
	
	public static Double LAMBDA_MIN = null;
	
	public static Double LAMBDA_MAX = null;
	
	public static Integer LAMBDAS_NUM = null;

	@Override
	public void setupLambdas()
	{
		
		Option[] lambdasArray = lambdasOption.getList();
		
		//Verifica se possui Lambdas 
		if (lambdasArray.length == this.poolOfEnsembles.length)
		{
			this.lambdas = new Double[this.poolOfEnsembles.length];
			for (int i = 0; i < this.poolOfEnsembles.length; i++) {
				this.lambdas[i] = ((FloatOption) lambdasArray[i]).getValue();
			}
		}
		else // Não possui lambdas na lista - GERAR
		{
			if (lambdas_static == null)
			{
				gerarLambdas(this.poolOfEnsembles.length);
			}
			if (lambdas_static == null || lambdas_static.length < this.poolOfEnsembles.length)
				throw new RuntimeException("ERRO: Lambdas não gerados!");
			this.lambdas = new Double[this.poolOfEnsembles.length];
			for (int i = 0; i < this.poolOfEnsembles.length; i++) {
				this.lambdas[i] = lambdas_static[i];
			}
		}
	}
	
	public static void gerarLambdas(int num_Lambdas)
	{
		if (LAMBDAS_NUM == null || LAMBDAS_NUM != num_Lambdas)
		{
			LAMBDAS_NUM = num_Lambdas;
		}
		
		if (LAMBDA_MIN == null || LAMBDA_MIN != lambdaMinOption.getValue())
		{
			LAMBDA_MIN = lambdaMinOption.getValue();
		}
		
		if (LAMBDA_MAX == null || LAMBDA_MAX != lambdaMaxOption.getValue())
		{
			LAMBDA_MAX = lambdaMaxOption.getValue();
		}
		
		double[] numeros = Matematica.gerarNumerosAleatorios(LAMBDA_MIN, LAMBDA_MAX, LAMBDAS_NUM);
		
		lambdas_static = new Double[LAMBDAS_NUM];
		for (int i = 0; i < LAMBDAS_NUM; i++) {
			lambdas_static[i] = numeros[i];
		}
	}

}
