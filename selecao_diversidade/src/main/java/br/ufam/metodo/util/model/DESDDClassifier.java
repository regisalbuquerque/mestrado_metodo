package br.ufam.metodo.util.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.github.javacliparser.FlagOption;
import com.github.javacliparser.FloatOption;
import com.github.javacliparser.IntOption;
import com.github.javacliparser.MultiChoiceOption;
import com.yahoo.labs.samoa.instances.Instance;

import br.ufam.diversidade.MedidaCalculo;
import br.ufam.diversidade.MedidaCalculoFactory;
import br.ufam.metodo.util.aprendizado.MetodoAprendizado;
import br.ufam.metodo.util.aprendizado.MetodoAprendizadoFactory;
import br.ufam.metodo.util.calculo.AcuraciaPrequencial;
import br.ufam.metodo.util.calculo.DiversidadePrequencial;
import br.ufam.metodo.util.dados.BufferInstancias;
import br.ufam.metodo.util.drift.DetectorDrift;
import br.ufam.metodo.util.medidas.selecao.MedidaSelecao;
import br.ufam.metodo.util.medidas.selecao.MedidaSelecaoFactory;
import br.ufam.metodo.util.medidor.Indicadores;
import moa.classifiers.AbstractClassifier;
import moa.classifiers.Classifier;
import moa.classifiers.core.driftdetection.ChangeDetector;
import moa.core.Measurement;
import moa.core.Utils;
import moa.options.ClassOption;

public abstract class DESDDClassifier extends AbstractClassifier implements DetectorDrift, IEnsembleSelection {

	private static final long serialVersionUID = 1L;
	
	public IntOption ensemblesNumberOption = new IntOption("ensemblesNumber", 'n', "The number of ensembles.", 10, 1,
			Integer.MAX_VALUE);

	public IntOption ensembleSizeOption = new IntOption("ensembleSize", 's', "The number of models in the bag.", 10, 1,
			Integer.MAX_VALUE);


	public MultiChoiceOption selectionOptionEstrategiaGeracao = new MultiChoiceOption("SelectionEstrategiaGeracao", 'g',
			"SelectionEstrategiaGeracao.", new String[] { "OnlineBagging", "OnlineBoosting", "LeverageBagging", "Pure", "ADOB", "BOLE"},
			new String[] { "OnlineBagging", "OnlineBoosting", "LeverageBagging", "Pure", "ADOB", "BOLE"}, 0);
	
	public ClassOption driftDetectionMethodOption = new ClassOption("driftDetectionMethod", 'd',
			"Drift detection method to use.", ChangeDetector.class, "DDM");

	public MultiChoiceOption selectionOptionMedidaCalculo = new MultiChoiceOption("SelectionMedidaCalculo", 'c',
			"SelectionMedidaCalculo.",
			new String[] { "Ambiguidade", "Margem", "Qstatistics", "Correlation", "Disagreement", "DoubleFault" },
			new String[] { "Ambiguidade", "Margem", "Qstatistics", "Correlation", "Disagreement", "DoubleFault" }, 0);
	
	public IntOption medidaCalculoJanela = new IntOption("janelaCalculo", 'j',
			"Sliding Window w", -1, -1, Integer.MAX_VALUE);

	public MultiChoiceOption selectionOptionEstrategiaSelecao = new MultiChoiceOption("SelectionEstrategiaSelecao", 'z',
			"SelectionEstrategiaSelecao.", new String[] { "Menor", "Maior", "MaiorAcc", "Media", "Mediana", "Pareto" },
			new String[] { "Menor", "Maior", "MaiorAcc", "Media", "Mediana", "Pareto" }, 0);

	public MultiChoiceOption selectionOptionEstrategiaRecuperacao = new MultiChoiceOption(
			"SelectionEstrategiaRecuperacao", 'r', "SelectionEstrategiaRecuperacao.",
			new String[] { "WorstClassifierAllEnsembles", "SimpleReset", "SimpleResetEnsemble", "SimpleResetSystem", "SimpleResetSystem1Detector", "RetreinaTodosComBufferWarning" },
			new String[] { "Quando encontra drift, reseta o pior classificador de cada ensemble ",
					"Quando encontra drift, reseta o pior classificador (ensembles com Drift)",
					"Quando encontra drift, reseta o ensemble", 
					"Quando encontra drift, reseta o sistema todo",
					"Quando encontra drift, reseta o sistema todo 1 detector",
					"Quando encontra drift, reseta o sistema todo e treina todos com BufferWarning" },
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

	
	//PARA O LB:
	
    // Leveraging Bagging MC: uses this option to use Output Codes
    public FlagOption outputCodesOption = new FlagOption("outputCodes", 'o',
            "Use Output Codes to use binary classifiers.");
    
    public MultiChoiceOption leveraginBagAlgorithmOption = new MultiChoiceOption(
            "leveraginBagAlgorithm", 'm', "Leveraging Bagging to use.", new String[]{
                "LeveragingBag", "LeveragingBagME", "LeveragingBagHalf", "LeveragingBagWT", "LeveragingSubag"},
            new String[]{"Leveraging Bagging for evolving data streams using ADWIN",
                "Leveraging Bagging ME using weight 1 if misclassified, otherwise error/(1-error)",
                "Leveraging Bagging Half using resampling without replacement half of the instances",
                "Leveraging Bagging WT without taking out all instances.",
                "Leveraging Subagging using resampling without replacement."
            }, 0);
    
    
	
	
	
	protected Double[] lambdas;
	
	protected IEnsembleClassifiers[] poolOfEnsembles;
	
	protected int ultimoEnsembleSelecionadoIndex;
	
	protected Double[] ultimosLambdas;
	
	protected Double[] ultimasDiversidades;
	
	protected Double[] ultimasAccs;
			
	protected AcuraciaPrequencial[] ensemble_acc;
	protected DiversidadePrequencial[] ensemble_diversidade;
	protected boolean usaSlidingWindow;

	protected int ultimoDrift = -1;

	protected BufferInstancias BufferWarning;

	protected ChangeDetector driftDetectionMethod;

	protected boolean newBufferReset;

	protected int ddmLevel;

	public static final int DDM_INCONTROL_LEVEL = 0;

	public static final int DDM_WARNING_LEVEL = 1;

	public static final int DDM_OUTCONTROL_LEVEL = 2;
	
	boolean detectDrift = false;

	protected int changeDetected = 0;

	protected int warningDetected = 0;

	protected int iteracao = 0;

	protected MetodoAprendizado metodoAprendizado;
	protected MedidaSelecao medidaSelecao;
	protected MedidaCalculo medidaCalculo;
	
	
	public Indicadores[] indicadores = null;
	//public Resultado[] resultadosEnsembles = null;
	
	
	@Override
	public void resetLearningImpl() {

		metodoAprendizado = MetodoAprendizadoFactory.fabrica(this.selectionOptionEstrategiaGeracao.getChosenLabel());
		medidaSelecao = MedidaSelecaoFactory.fabrica(this.selectionOptionEstrategiaSelecao.getChosenLabel());
		medidaCalculo = MedidaCalculoFactory.fabrica(this.selectionOptionMedidaCalculo.getChosenLabel());

		this.driftDetectionMethod = ((ChangeDetector) getPreparedClassOption(this.driftDetectionMethodOption)).copy();
		this.newBufferReset = false;

		this.poolOfEnsembles = metodoAprendizado.alocaPoolEnsembles(this.ensemblesNumberOption.getValue());
		
		this.BufferWarning = new BufferInstancias();
		
		setupLambdas();
		
		estrategiaInicial();
	}
	
	public abstract void setupLambdas();
	
	public void randomizaLambdasEnsembles() {
		//System.out.println("ATENÇÃO: Randomização dos Ensembles Desligada!");
	}
	
	public void mostrarLambdas()
	{
		System.out.println("LAMBDAS : ");
		for (int i = 0; i < this.lambdas.length; i++) {
			System.out.println(" " + lambdas[i]);
		}
		System.out.println("-----------------------");
	}
	
	protected void estrategiaInicial() {

		this.indicadores = new Indicadores[this.poolOfEnsembles.length];
		//if (resultadosEnsembles == null)
		//	resultadosEnsembles = new Resultado[ensemblesNumberOption.getValue()];
		
		
		this.ensemble_acc = new AcuraciaPrequencial[this.poolOfEnsembles.length];
		this.ensemble_diversidade = new DiversidadePrequencial[this.poolOfEnsembles.length];
		

		for (int i = 0; i < this.poolOfEnsembles.length; i++) {
			inicializa_ensemble(i);
		}

	}
	
	protected void inicializa_ensemble(int i) {
		this.indicadores[i] = new Indicadores();
		//if (resultadosEnsembles[i] == null) //Para não comprometer o que já foi gravado
		//{
		//	resultadosEnsembles[i] = new Resultado();
		//}
		//resultadosEnsembles[i].setCodigo(Double.toString(lambdas[i]));
		
		this.poolOfEnsembles[i] = cria_novo_ensemble(this.lambdas[i]);
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
	
	protected IEnsembleClassifiers cria_novo_ensemble(double lambda) {
		
		IEnsembleClassifiers ensemble = metodoAprendizado.criaNovoEnsemble();
		
		ensemble.setLambda(lambda);
		ensemble.setSize(this.ensembleSizeOption.getValue());
		ensemble.setBaseLearner(this.baseLearner1Option.getValueAsCLIString());
		ensemble.setRandomSeed(this.randomSeed);
		ensemble.setChangeDetector(((ChangeDetector) getPreparedClassOption(this.driftDetectionMethodOption)).copy());
		
        
        //LAMBDA
        //ensemble.weightShrinkOption.setValue(lambda); 
        
        //ensemble.deltaAdwinOption.setValue(this.deltaAdwinOption.getValue());
        //ensemble.outputCodesOption.setValue(this.outputCodesOption.isSet());
        //ensemble.leveraginBagAlgorithmOption.setChosenIndex(this.leveraginBagAlgorithmOption.getChosenIndex());
        //ensemble.setRandomSeed(this.randomSeed);
		
		
		ensemble.prepareForUse();
		ensemble.resetLearning();

		return ensemble;
	}
	
	@Override
	public void trainOnInstanceImpl(Instance inst) {

		this.iteracao++;
		detectDrift = false;

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
				detectDrift = true;
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
			
			//Randomizar Lambdas // Se o método for sobrescrito
			this.randomizaLambdasEnsembles();

			// Treinar o POOL
			for (int i = 0; i < this.poolOfEnsembles.length; i++) {
				treinarEnsemble(inst, i);
			}
		}
		else // NÃO USA DDM
		{
			
	    	List<Integer> ensemblesComDrift = new ArrayList<>(); 
	    	
	    	//Randomizar Lambdas // Se o método for sobrescrito
			this.randomizaLambdasEnsembles();
	    	
	        for (int i = 0; i < this.poolOfEnsembles.length; i++) {
	        	
	        	IEnsembleClassifiers ensemble = treinarEnsemble(inst, i);
	            
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

	protected IEnsembleClassifiers treinarEnsemble(Instance inst, int i) {
		IEnsembleClassifiers ensemble = this.poolOfEnsembles[i];

		//Antes de treinar - calcula ACC[]
		int trueClass = (int) inst.classValue();
		double[] votos = ensemble.getVotesForInstance(inst);
		if (Utils.maxIndex(votos) == trueClass)
		{
		    this.ensemble_acc[i].acertou();
		    this.indicadores[i].acertou();
		}
		else
		{
		    this.ensemble_acc[i].errou();
		    this.indicadores[i].errou();
		}
		
		// Se usa SlidingWindow - calcula a diversidade aqui!
		if (usaSlidingWindow)
		{
			//Calculo
			this.ensemble_diversidade[i].calcula(ensemble.getSubClassifiers(), inst);
		}
		
		ensemble.trainOnInstance(inst);
		return ensemble;
	}

	protected void executaEstrategia(List<Integer> ensemblesComDrift, BufferInstancias bufferWarning) {

		if (selectionOptionEstrategiaRecuperacao.getChosenLabel().equals("WorstClassifierAllEnsembles"))
        {
			for (int i = 0; i < this.poolOfEnsembles.length; i++) {
				this.poolOfEnsembles[i].estrategiaSimpleReset();
			}
        }
		else if (selectionOptionEstrategiaRecuperacao.getChosenLabel().equals("SimpleReset"))
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
		
		}

	}

	@Override
	public double[] getVotesForInstance(Instance inst) {

		// Se não usa SlidingWindow - Calcula a diversidade
        if (!usaSlidingWindow)
        {
        	for (int i = 0; i < this.poolOfEnsembles.length; i++) {
        		IEnsembleClassifiers ensemble = this.poolOfEnsembles[i];
            	//Calculo
            	this.ensemble_diversidade[i].calcula(ensemble.getSubClassifiers(), inst);
            }
		}

        
        this.ultimosLambdas = Arrays.copyOf(this.lambdas, this.lambdas.length);
        this.ultimasDiversidades = DiversidadePrequencial.getArray(this.ensemble_diversidade);
        this.ultimasAccs = AcuraciaPrequencial.getArray(this.ensemble_acc);
        
		// Faz a seleção
		this.ultimoEnsembleSelecionadoIndex = this.medidaSelecao.seleciona( this.ultimasDiversidades,
																			this.ultimasAccs,
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
		return detectDrift;
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
		return this.poolOfEnsembles[this.ultimoEnsembleSelecionadoIndex].getLambda();
	}
	
	@Override
	public Double[] getUltimasDiversidades()
	{
		return this.ultimasDiversidades;
	}
	
	@Override
	public Double[] getUltimasAccs()
	{
		return this.ultimasAccs;
	}
	
	@Override
	public Double[] getEnsemblesLambdas() {
		return this.ultimosLambdas;
	}
	
}
