package br.ufam.metodos.v13;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.github.javacliparser.FloatOption;
import com.github.javacliparser.IntOption;
import com.github.javacliparser.MultiChoiceOption;
import com.yahoo.labs.samoa.instances.Instance;

import br.ufam.diversidade.MedidaCalculo;
import br.ufam.diversidade.MedidaCalculoFactory;
import br.ufam.metodo.util.calculo.Acuracia;
import br.ufam.metodo.util.calculo.AcuraciaPrequencial;
import br.ufam.metodo.util.calculo.DiversidadePrequencial;
import br.ufam.metodo.util.calculo.Matematica;
import br.ufam.metodo.util.dados.BufferInstancias;
import br.ufam.metodo.util.drift.DetectorDrift;
import br.ufam.metodo.util.medidas.selecao.MedidaSelecao;
import br.ufam.metodo.util.medidas.selecao.MedidaSelecaoFactory;
import br.ufam.metodo.util.model.Ensemble;
import br.ufam.metodo.util.model.EnsembleValor;
import br.ufam.metodo.util.model.IEnsembleSelection;
import moa.classifiers.AbstractClassifier;
import moa.classifiers.Classifier;
import moa.classifiers.core.driftdetection.ChangeDetector;
import moa.core.Measurement;
import moa.core.Utils;
import moa.options.ClassOption;

/**
 *
 * @author regis Versão 13
 */
public class MetodoClassificadorV13 extends AbstractClassifier implements DetectorDrift, IEnsembleSelection {

	private static final long serialVersionUID = 1L;

	@Override
	public String getPurposeString() {
		return "Classifier that ...";
	}

	public static IntOption ensemblesNumberOption = new IntOption("ensemblesNumber", 'n', "The number of ensembles.", 10, 1,
			Integer.MAX_VALUE);

	public IntOption ensembleSizeOption = new IntOption("ensembleSize", 's', "The number of models in the bag.", 10, 1,
			Integer.MAX_VALUE);

//	public ListOption lambdasOption = new ListOption("lambdaAttributes", 'y',
//			"Lambdas of ensembles. Enter comma seperated list, ", new FloatOption("lambdaAttribute", ' ', "Lambda", 1),
//			new FloatOption[] { new FloatOption("", ' ', "", 1.0), new FloatOption("", ' ', "", 1.0),
//					new FloatOption("", ' ', "", 1.0), new FloatOption("", ' ', "", 1.0),
//					new FloatOption("", ' ', "", 1.0), new FloatOption("", ' ', "", 1.0),
//					new FloatOption("", ' ', "", 1.0), new FloatOption("", ' ', "", 1.0),
//					new FloatOption("", ' ', "", 1.0), new FloatOption("", ' ', "", 1.0) },
//			',');
	
	public static FloatOption lambdaMinOption = new FloatOption("lambdaInfLimit", 'i',
            "Limite Inferior Lambda", 1);
	
	public static FloatOption lambdaMaxOption = new FloatOption("lambdaSupLimit", 'm',
            "Limite Superior Lambda", 1);
	
	public ClassOption driftDetectionMethodOption = new ClassOption("driftDetectionMethod", 'd',
			"Drift detection method to use.", ChangeDetector.class, "DDM");

	public MultiChoiceOption selectionOptionMedidaCalculo = new MultiChoiceOption("SelectionMedidaCalculo", 'c',
			"SelectionMedidaCalculo.",
			new String[] { "Ambiguidade", "Margem", "Qstatistics", "Correlation", "Disagreement", "DoubleFault" },
			new String[] { "Ambiguidade", "Margem", "Qstatistics", "Correlation", "Disagreement", "DoubleFault" }, 0);
	
	public IntOption medidaCalculoJanela = new IntOption("janelaCalculo", 'j',
			"Sliding Window w", -1, -1, Integer.MAX_VALUE);

	public MultiChoiceOption selectionOptionEstrategiaSelecao = new MultiChoiceOption("SelectionEstrategiaSelecao", 'o',
			"SelectionEstrategiaSelecao.", new String[] { "Menor", "Maior", "Media", "Mediana", "Pareto" },
			new String[] { "Menor", "Maior", "Media", "Mediana", "Pareto" }, 0);

	public MultiChoiceOption selectionOptionEstrategiaRecuperacao = new MultiChoiceOption(
			"SelectionEstrategiaRecuperacao", 'r', "SelectionEstrategiaRecuperacao.",
			new String[] { "SimpleReset", "SimpleResetEnsemble", "SimpleResetSystem", "SimpleResetSystem1Detector", "RetreinaTodosComBufferWarning",
					"RemoveEnsembleMenosAcurados" },
			new String[] { "Quando encontra drift, reseta o pior classificador",
					"Quando encontra drift, reseta o ensemble", 
					"Quando encontra drift, reseta o sistema todo",
					"Quando encontra drift, reseta o sistema todo 1 detector",
					"Quando encontra drift, reseta o sistema todo e treina todos com BufferWarning",
					"Quando encontra drift, reseta ensembles menos acurados" },
			0);

	public IntOption ensemblesNumRemoverRecuperacaoOption = new IntOption("ensembleNumRemoverRecuperacao", 'q',
			"The number of essembles to recriate after drift.", -1, -1, Integer.MAX_VALUE);

	public IntOption numBaseLeanersOption = new IntOption("numBaseLeaners", 'x', "The number of baseLeaners.", 1, 1, 5);

	public FloatOption deltaAdwinOption = new FloatOption("deltaAdwin", 'a',
            "Delta of Adwin change detection", 0.002, 0.0, 1.0);
	
	public ClassOption baseLearner1Option = new ClassOption("baseLearner1", '1', "Classifier to train.",
			Classifier.class, "trees.HoeffdingTree");

	public ClassOption baseLearner2Option = new ClassOption("baseLearner2", '2', "Classifier to train.",
			Classifier.class, "trees.HoeffdingTree");

	public ClassOption baseLearner3Option = new ClassOption("baseLearner3", '3', "Classifier to train.",
			Classifier.class, "trees.HoeffdingTree");

	public ClassOption baseLearner4Option = new ClassOption("baseLearner4", '4', "Classifier to train.",
			Classifier.class, "trees.HoeffdingTree");

	public ClassOption baseLearner5Option = new ClassOption("baseLearner5", '5', "Classifier to train.",
			Classifier.class, "trees.HoeffdingTree");

	// protected Ensembles ensembles;
	protected Ensemble[] poolOfEnsembles;
	protected int ultimoEnsembleSelecionadoIndex;

	AcuraciaPrequencial[] ensemble_acc;
	DiversidadePrequencial[] ensemble_diversidade;
	boolean usaSlidingWindow;

	private int ultimoDrift = -1;

	protected BufferInstancias BufferWarning;

	protected ChangeDetector driftDetectionMethod;

	protected boolean newBufferReset;

	protected int ddmLevel;

	public static final int DDM_INCONTROL_LEVEL = 0;

	public static final int DDM_WARNING_LEVEL = 1;

	public static final int DDM_OUTCONTROL_LEVEL = 2;
	
	public static double[] lambdas;
	
	public static Double LAMBDA_MIN = null;
	
	public static Double LAMBDA_MAX = null;
	
	public static String LAMBDAS_csv = "";
	
	public static Integer LAMBDAS_NUM = null;

	protected int changeDetected = 0;

	protected int warningDetected = 0;

	private int iteracao = 0;

	protected MedidaSelecao medidaSelecao;

	protected MedidaCalculo medidaCalculo;
	
	public static void setupLambdas()
	{
		if (LAMBDAS_NUM == null || LAMBDAS_NUM != ensemblesNumberOption.getValue() )
		{
			LAMBDAS_NUM = ensemblesNumberOption.getValue();
		}
		
		if (LAMBDA_MIN == null || LAMBDA_MIN != lambdaMinOption.getValue())
		{
			LAMBDA_MIN = lambdaMinOption.getValue();
		}
		
		if (LAMBDA_MAX == null || LAMBDA_MAX != lambdaMaxOption.getValue())
		{
			LAMBDA_MAX = lambdaMaxOption.getValue();
		}
	}
	
	public static void gerarLambdas()
	{
		setupLambdas();
		
		System.out.println("Gerando LAMBDAS ************* ");
		lambdas = new double[LAMBDAS_NUM];
		for (int i = 0; i < LAMBDAS_NUM; i++) {
			lambdas[i] = Matematica.gerarNumeroAleatorio(LAMBDA_MIN, LAMBDA_MAX);
		}
	}
	
	public static void mostrarLambdas()
	{
		System.out.println("LAMBDAS : ");
		for (int i = 0; i < LAMBDAS_NUM; i++) {
			System.out.println(" " + lambdas[i]);
		}
		System.out.println("-----------------------");
	}
	
	@Override
	public void resetLearningImpl() {
		
		medidaSelecao = MedidaSelecaoFactory.fabrica(this.selectionOptionEstrategiaSelecao.getChosenLabel());
		medidaCalculo = MedidaCalculoFactory.fabrica(this.selectionOptionMedidaCalculo.getChosenLabel());

		this.driftDetectionMethod = ((ChangeDetector) getPreparedClassOption(this.driftDetectionMethodOption)).copy();
		this.newBufferReset = false;

		estrategiaInicial();

		this.BufferWarning = new BufferInstancias();
	}

	private Ensemble cria_novo_ensemble(double lambda) {
		Ensemble ensemble = new Ensemble();
		ensemble.lambdaOption.setValue(lambda);
		ensemble.ensembleSizeOption.setValue(this.ensembleSizeOption.getValue());
		ensemble.numBaseLeanersOption.setValue(this.numBaseLeanersOption.getValue());
		ensemble.deltaAdwinOption.setValue(this.deltaAdwinOption.getValue());
		ensemble.baseLearner1Option.setValueViaCLIString(this.baseLearner1Option.getValueAsCLIString());
		ensemble.baseLearner2Option.setValueViaCLIString(this.baseLearner2Option.getValueAsCLIString());
		ensemble.baseLearner3Option.setValueViaCLIString(this.baseLearner3Option.getValueAsCLIString());
		ensemble.baseLearner4Option.setValueViaCLIString(this.baseLearner4Option.getValueAsCLIString());
		ensemble.baseLearner5Option.setValueViaCLIString(this.baseLearner5Option.getValueAsCLIString());
		ensemble.setRandomSeed(this.randomSeed);
		
		
		ensemble.prepareForUse();
		ensemble.resetLearning();

		return ensemble;
	}

	private void estrategiaInicial() {

		this.poolOfEnsembles = new Ensemble[this.ensemblesNumberOption.getValue()];
		

		setupLambdas();

		mostrarLambdas();

		
		this.ensemble_acc = new AcuraciaPrequencial[this.poolOfEnsembles.length];
		this.ensemble_diversidade = new DiversidadePrequencial[this.poolOfEnsembles.length];
		
		for (int i = 0; i < this.poolOfEnsembles.length; i++) {
			inicializa_ensemble(i);
		}

	}

	private void inicializa_ensemble(int i) {
		this.poolOfEnsembles[i] = cria_novo_ensemble(lambdas[i]);
		this.ensemble_acc[i] = new AcuraciaPrequencial();
		if (medidaCalculoJanela.getValue() == -1)
		{
			//Sem Janela
			this.usaSlidingWindow = false;
			this.ensemble_diversidade[i] = new DiversidadePrequencial(this.ensembleSizeOption.getValue(), null, medidaCalculo);
		}
		else
		{
			//Com Janela
			this.usaSlidingWindow = true;
			this.ensemble_diversidade[i] = new DiversidadePrequencial(this.ensembleSizeOption.getValue(), medidaCalculoJanela.getValue(), medidaCalculo);
		}
		
	}

	private void estrategiaRemoveMenosAcurados(List<Instance> listaInstancias, int numEnsemblesRemover) {

		// Acurácia medida através do BufferWarning
		List<EnsembleValor> listaTemporaria = new ArrayList<>();
		for (Ensemble ensembleItem : this.poolOfEnsembles) {
			listaTemporaria
					.add(new EnsembleValor(Acuracia.calculaAcuracia(listaInstancias, ensembleItem), ensembleItem));
		}

		Collections.sort(listaTemporaria); // Ordena do menor pro maior

		Ensemble[] ensemblesTemporario = new Ensemble[this.poolOfEnsembles.length];

		for (int i = 0; i < this.poolOfEnsembles.length; i++) {

			if (i < numEnsemblesRemover) { // Cria um novo
				ensemblesTemporario[i] = cria_novo_ensemble(listaTemporaria.get(i).ensemble.lambda); // Usa o lambda que
																										// está sendo
																										// removido(listaTemporaria)
																										// para poder
																										// manter a
																										// mesma
																										// configuração
																										// de lambdas
																										// inicial
				this.ensemble_acc[i] = new AcuraciaPrequencial();
			} else { // Mantem o Ensemble
				ensemblesTemporario[i] = listaTemporaria.get(i).ensemble;
			}
		}

		// Ordenar o vetor de acordo com a ordem do lambda inicial - Isso faz diferença
		for (int i = 0; i < this.lambdas.length; i++) {
			double lambdaValor = this.lambdas[i];
			Ensemble ensembleEncontrado = null;

			for (int j = 0; j < ensemblesTemporario.length; j++) {
				if (ensemblesTemporario[j] != null && lambdaValor == ensemblesTemporario[j].lambda) {
					ensembleEncontrado = ensemblesTemporario[j];
					ensemblesTemporario[j] = null; // Para não ser encontrado novamente em caso de lambdas repetido
					break;
				}
			}
			if (ensembleEncontrado == null)
				throw new RuntimeException("ERRO! Ensemble não preenchido após o DRIFT. NULL");
			this.poolOfEnsembles[i] = ensembleEncontrado;
		}

	}

	@Override
	public void trainOnInstanceImpl(Instance inst) {

		this.iteracao++;

		//CASO Recuperação = ComBufferWarning -> USA DDM
		if (selectionOptionEstrategiaRecuperacao.getChosenLabel().equals("RetreinaTodosComBufferWarning") || selectionOptionEstrategiaRecuperacao.getChosenLabel().equals("SimpleResetSystem1Detector")) {

			int trueClass = (int) inst.classValue();
			boolean prediction;
			if (Utils.maxIndex(getVotesForInstance(inst)) == trueClass) {
				prediction = true;
			} else {
				prediction = false;
			}
			this.driftDetectionMethod.input(prediction ? 0.0 : 1.0);
			this.ddmLevel = DDM_INCONTROL_LEVEL;
			if (this.driftDetectionMethod.getChange()) {
				this.ddmLevel = DDM_OUTCONTROL_LEVEL;
			}
			if (this.driftDetectionMethod.getWarningZone()) {
				this.ddmLevel = DDM_WARNING_LEVEL;
			}
			switch (this.ddmLevel) {
			case DDM_WARNING_LEVEL:
				if (newBufferReset == true) {
					this.warningDetected++;
					this.BufferWarning = new BufferInstancias();
					newBufferReset = false;
				}
				this.BufferWarning.incluir(inst);
				break;

			case DDM_OUTCONTROL_LEVEL:
				this.changeDetected++;
				this.ultimoDrift = iteracao;
				executaEstrategia(null, BufferWarning);
				// return; << Testar com return para não executar o treinamento abaixo;
				// Verificar se faz diferença!
				break;

			case DDM_INCONTROL_LEVEL:
				newBufferReset = true;
				break;
			default:

			}

			// Treinar o POOL
			for (int i = 0; i < this.poolOfEnsembles.length; i++) {
				treinarEnsemble(inst, i);
			}
		}
		else // NÃO USA DDM
		{
			
			boolean detectDrift = false;
	    	List<Integer> ensemblesComDrift = new ArrayList<>(); 
	        for (int i = 0; i < this.poolOfEnsembles.length; i++) {
	        	
	        	Ensemble ensemble = treinarEnsemble(inst, i);
	            
	            if (ensemble.detectouDrift())
	            {
	            	detectDrift = true;
	            	ensemblesComDrift.add(i);
	            }
	        }
	        
	        if (detectDrift)
	        {
	        	//Executa estratégia ... ... inclusive a estratégia deve zera o acc e a diversidade (estrategiaInicial())
	        	
	        	executaEstrategia(ensemblesComDrift, null);
	        	
	        }
			
			
		}

	}

	private Ensemble treinarEnsemble(Instance inst, int i) {
		Ensemble ensemble = this.poolOfEnsembles[i];

		//Antes de treinar - calcula ACC[]
		int trueClass = (int) inst.classValue();
		double[] votos = ensemble.getVotesForInstance(inst);
		if (Utils.maxIndex(votos) == trueClass)
		    this.ensemble_acc[i].acertou();
		else
		    this.ensemble_acc[i].errou();
		
		// Se usa SlidingWindow - calcula a diversidade aqui!
		if (usaSlidingWindow)
		{
			//Calculo
			this.ensemble_diversidade[i].calcula(ensemble.getSubClassifiers(), inst);
		}
		
		ensemble.trainOnInstance(inst);
		return ensemble;
	}

	private void executaEstrategia(List<Integer> ensemblesComDrift, BufferInstancias bufferWarning) {

		if (selectionOptionEstrategiaRecuperacao.getChosenLabel().equals("SimpleReset"))
        {
            if (!ensemblesComDrift.isEmpty()) {
            	for (Integer i: ensemblesComDrift)
            	{
            		this.poolOfEnsembles[i].estrategiaSimpleReset();
            	}
            }
        }
        else if (selectionOptionEstrategiaRecuperacao.getChosenLabel().equals("SimpleResetEnsemble"))
        {
        	if (!ensemblesComDrift.isEmpty()) {
            	for (Integer i: ensemblesComDrift)
            	{
            		inicializa_ensemble(i);
            	}
            }
        }
        else if (selectionOptionEstrategiaRecuperacao.getChosenLabel().equals("SimpleResetSystem") || selectionOptionEstrategiaRecuperacao.getChosenLabel().equals("SimpleResetSystem1Detector"))
        {
        	estrategiaInicial(); //Inicia todo mundo sem retreino
        }
        else if (selectionOptionEstrategiaRecuperacao.getChosenLabel().equals("RetreinaTodosComBufferWarning")) {
			estrategiaInicial();

			// Treino com Buffer WARNING
			while (bufferWarning.hasElementos()) {
				Instance instance = bufferWarning.getProximaInstancia();

				// Treinar o POOL
				for (int i = 0; i < this.poolOfEnsembles.length; i++) {
					this.poolOfEnsembles[i].trainOnInstance(instance);
				}

			}
		
		} else if (selectionOptionEstrategiaRecuperacao.getChosenLabel().equals("RemoveEnsembleMenosAcurados")) {
			int numEnsemblesRemover = this.poolOfEnsembles.length / 2;
			if (ensemblesNumRemoverRecuperacaoOption.getValue() != -1)
				numEnsemblesRemover = ensemblesNumRemoverRecuperacaoOption.getValue();

			estrategiaRemoveMenosAcurados(bufferWarning.getInstancias(), numEnsemblesRemover);

			// Treino com Buffer WARNING
			while (bufferWarning.hasElementos()) {
				Instance instance = bufferWarning.getProximaInstancia();
				trainOnInstanceImpl(instance);
			}
		}

	}

	@Override
	public double[] getVotesForInstance(Instance inst) {

		// Se não usa SlidingWindow - Calcula a diversidade
        if (!usaSlidingWindow)
        {
        	for (int i = 0; i < this.poolOfEnsembles.length; i++) {
        		Ensemble ensemble = this.poolOfEnsembles[i];
            	//Calculo
            	this.ensemble_diversidade[i].calcula(ensemble.getSubClassifiers(), inst);
            }
		}

		// Faz a seleção
		this.ultimoEnsembleSelecionadoIndex = this.medidaSelecao.seleciona( DiversidadePrequencial.getArray(this.ensemble_diversidade),
																			AcuraciaPrequencial.getArray(this.ensemble_acc),
																			medidaCalculo.isMaximiza(),
																			true);

		return this.poolOfEnsembles[this.ultimoEnsembleSelecionadoIndex].getVotesForInstance(inst);

	}

	@Override
	public boolean isRandomizable() {
		return true;
	}

	@Override
	public void getModelDescription(StringBuilder out, int indent) {

	}

	@Override
	protected Measurement[] getModelMeasurementsImpl() {

		return null;
	}

	@Override
	public boolean detectouDrift() {
		if (this.ddmLevel == DDM_OUTCONTROL_LEVEL)
			return true;
		return false;
	}

	@Override
	public Classifier[] getSubClassifiers() {
		return this.poolOfEnsembles[this.ultimoEnsembleSelecionadoIndex].getSubClassifiers();
	}

	@Override
	public Classifier[][] getClassifiers() {
		Classifier[][] ensembles = new Classifier[this.poolOfEnsembles.length][];   
		
		for (int i=0; i < this.poolOfEnsembles.length; i++)
		{
			ensembles[i] = this.poolOfEnsembles[i].getSubClassifiers();
		}
		
		return ensembles;
	}

	@Override
	public int getUltimoEnsembleSelecionadoIndex() {
		return this.ultimoEnsembleSelecionadoIndex;
	}
	
	@Override
	public double getUltimoEnsembleSelecionadoLambda() {
		return this.poolOfEnsembles[this.ultimoEnsembleSelecionadoIndex].lambdaOption.getValue();
	}
	
	
	

	// @Override
	// public double[] getEnsembleDiversidade() {
	// double[] ensembleDiversidade = new double[this.ensemblesNum];
	// for (int i = 0; i < ensembleDiversidade.length ; i++) {
	// ensembleDiversidade[i] = this.ensemble[i].ultimaMedidaCalculo;
	// }
	// return ensembleDiversidade;
	// }
	//
	// @Override
	// public int[] getEnsembleClass() {
	// int[] ensembleClass = new int[this.ensemblesNum];
	// for (int i = 0; i < ensembleClass.length ; i++) {
	// ensembleClass[i] =
	// Utils.maxIndex(this.ensemble[i].medidaCalculo.votos_acumulados);
	// }
	// return ensembleClass;
	// }

}
