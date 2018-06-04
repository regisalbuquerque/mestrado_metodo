/*
 *    LeveragingBag.java
 *    Copyright (C) 2010 University of Waikato, Hamilton, New Zealand
 *    @author Albert Bifet (abifet at cs dot waikato dot ac dot nz)
 *
 *    This program is free software; you can redistribute it and/or modify
 *    it under the terms of the GNU General Public License as published by
 *    the Free Software Foundation; either version 3 of the License, or
 *    (at your option) any later version.
 *
 *    This program is distributed in the hope that it will be useful,
 *    but WITHOUT ANY WARRANTY; without even the implied warranty of
 *    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *    GNU General Public License for more details.
 *
 *    You should have received a copy of the GNU General Public License
 *    along with this program. If not, see <http://www.gnu.org/licenses/>.
 *    
 */
package br.ufam.metodos.leveraging.v2;

import java.util.ArrayList;
import java.util.List;

import com.github.javacliparser.FlagOption;
import com.github.javacliparser.FloatOption;
import com.github.javacliparser.IntOption;
import com.github.javacliparser.ListOption;
import com.github.javacliparser.MultiChoiceOption;
import com.github.javacliparser.Option;
import com.yahoo.labs.samoa.instances.Instance;

import br.ufam.diversidade.MedidaCalculo;
import br.ufam.diversidade.MedidaCalculoFactory;
import br.ufam.metodo.util.calculo.AcuraciaPrequencial;
import br.ufam.metodo.util.calculo.DiversidadePrequencial;
import br.ufam.metodo.util.medidas.selecao.MedidaSelecao;
import br.ufam.metodo.util.medidas.selecao.MedidaSelecaoFactory;
import moa.classifiers.AbstractClassifier;
import moa.classifiers.Classifier;
import moa.core.Measurement;
import moa.core.Utils;
import moa.options.ClassOption;

/**
 * Leveraging Bagging for evolving data streams using ADWIN. Leveraging Bagging
 * and Leveraging Bagging MC using Random Output Codes ( -o option).
 *
 * <p>See details in:<br /> Albert Bifet, Geoffrey Holmes, Bernhard Pfahringer.
 * Leveraging Bagging for Evolving Data Streams Machine Learning and Knowledge
 * Discovery in Databases, European Conference, ECML PKDD}, 2010.</p>
 *
 * @author Albert Bifet (abifet at cs dot waikato dot ac dot nz)
 * @version $Revision: 7 $
 */
public class LeveragingBagSelection extends AbstractClassifier {

    private static final long serialVersionUID = 1L;

    @Override
    public String getPurposeString() {
        return "Leveraging Bagging for evolving data streams using ADWIN.";
    }
    
    //Adicionado por Regis
    public IntOption ensemblesNumberOption = new IntOption("ensemblesNumber", 'n',
            "The number of ensembles.", 1, 1, Integer.MAX_VALUE);
    
    //Adicionado por Regis - Lambdas -> w
    public ListOption weightShrinkOptionList = new ListOption("weightShrinks", 'w',
            "The number to use to compute the weight of new instances. Enter comma seperated list, ",
            new FloatOption("weightShrink", ' ', "WeightShrink", 6),
            new FloatOption[]{
                new FloatOption("weightShrink", ' ', "WeightShrink", 6)
            },
            ',');
    
    

    public ClassOption baseLearnerOption = new ClassOption("baseLearner", 'l',
            "Classifier to train.", Classifier.class, "trees.HoeffdingTree");

    public IntOption ensembleSizeOption = new IntOption("ensembleSize", 's',
            "The number of models in the bag.", 10, 1, Integer.MAX_VALUE);

    //Desabilitado por Régis
    //public FloatOption weightShrinkOption = new FloatOption("weightShrink", 'w',
    //        "The number to use to compute the weight of new instances.", 6, 0.0, Float.MAX_VALUE); 

    
    //Adicionado por Regis
    public MultiChoiceOption selectionOptionMedidaCalculo = new MultiChoiceOption(
            "SelectionMedidaCalculo", 'c', "SelectionMedidaCalculo.", new String[]{
                "Ambiguidade", "Margem", "Qstatistics", "Correlation", "Disagreement", "DoubleFault"}, new String[]{
                "Ambiguidade",
                "Margem",
                "Qstatistics",
                "Correlation",
                "Disagreement",
                "DoubleFault"}, 0);
    
    //Adicionado por Regis
    public IntOption medidaCalculoJanela = new IntOption("janelaCalculo", 'j',
			"Sliding Window j", -1, -1, Integer.MAX_VALUE);
    
    //Adicionado por Regis
    public MultiChoiceOption selectionOptionEstrategiaSelecao = new MultiChoiceOption(
            "SelectionEstrategiaSelecao", 'e', "SelectionEstrategiaSelecao.", new String[]{
                "Menor", "Maior", "Media", "Mediana", "Pareto"}, new String[]{
                "Menor",
                "Maior",
                "Media",
                "Mediana",
                "Pareto"}, 0);
    
    //Adicionado por Regis
    public MultiChoiceOption selectionOptionEstrategiaRecuperacao = new MultiChoiceOption(
            "SelectionEstrategiaRecuperacao", 'r', "SelectionEstrategiaRecuperacao.", new String[]{
                "SimpleReset", "SimpleResetEnsemble", "SimpleResetSystem"}, new String[]{
                "Quando encontra drift, reseta o pior classificador",
                "Quando encontra drift, reseta o ensemble",
                "Quando encontra drift, reseta o sistema todo"
                }, 0);
    
    public FloatOption deltaAdwinOption = new FloatOption("deltaAdwin", 'a',
            "Delta of Adwin change detection", 0.002, 0.0, 1.0);

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

    protected LeveragingBagModificado[] poolOfEnsembles;
    protected int ultimoEnsembleSelecionadoIndex;
    
    double w[]; //lambdas
    
    AcuraciaPrequencial ensemble_acc[]; //Acurácias prequenciais
    DiversidadePrequencial[] ensemble_diversidade;
	boolean usaSlidingWindow;

    private int iteracao = 0;
    
    protected MedidaSelecao medidaSelecao;
    
    protected MedidaCalculo medidaCalculo;
    
    
    @Override
    public void resetLearningImpl() {
        
        medidaSelecao = MedidaSelecaoFactory.fabrica(this.selectionOptionEstrategiaSelecao.getChosenLabel());
        medidaCalculo = MedidaCalculoFactory.fabrica(this.selectionOptionMedidaCalculo.getChosenLabel());
         
        
        //Cria o pool de ensembles
        
        estrategiaInicial();

    }
    
    private LeveragingBagModificado cria_novo_ensemble(double w) {
        LeveragingBagModificado ensemble = new LeveragingBagModificado();
        
        //Enviar os parâmetros
        ensemble.baseLearnerOption.setValueViaCLIString(this.baseLearnerOption.getValueAsCLIString());
        ensemble.ensembleSizeOption.setValue(this.ensembleSizeOption.getValue());
        ensemble.weightShrinkOption.setValue(w);
        ensemble.deltaAdwinOption.setValue(this.deltaAdwinOption.getValue());
        ensemble.outputCodesOption.setValue(this.outputCodesOption.isSet());
        ensemble.leveraginBagAlgorithmOption.setChosenIndex(this.leveraginBagAlgorithmOption.getChosenIndex());
        ensemble.setRandomSeed(this.randomSeed);

        //fazer o reset
        ensemble.prepareForUse();
        ensemble.resetLearning();
        
        return ensemble;
    	
    }
    
    private void estrategiaInicial()
    {
    	this.poolOfEnsembles = new LeveragingBagModificado[this.ensemblesNumberOption.getValue()];
        
        
        Option[] weightShrinkArray = this.weightShrinkOptionList.getList();
        if (weightShrinkArray.length < this.ensemblesNumberOption.getValue()) {
            throw new RuntimeException("ERROR! weightShrink parameter invalid!");
        }
        
        this.w = new double[weightShrinkArray.length];
        for (int n = 0; n < weightShrinkArray.length; n++) {
            this.w[n] = ((FloatOption) weightShrinkArray[n]).getValue();
        }
        
        this.ensemble_acc = new AcuraciaPrequencial[this.poolOfEnsembles.length];
        this.ensemble_diversidade = new DiversidadePrequencial[this.poolOfEnsembles.length];
        
        for (int i = 0; i < this.poolOfEnsembles.length; i++) {
            inicializa_ensemble(i);
            
        }
    }

	private void inicializa_ensemble(int i) {
		this.poolOfEnsembles[i] = cria_novo_ensemble(this.w[i]);
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
    

    @Override
    public void trainOnInstanceImpl(Instance inst) {
    	this.iteracao++;
        
    	boolean detectDrift = false;
    	List<Integer> ensemblesComDrift = new ArrayList<>(); 
        for (int i = 0; i < this.poolOfEnsembles.length; i++) {
        	
            LeveragingBagModificado ensemble = treinarEnsemble(inst, i);
            
            if (ensemble.detectouDrift())
            {
            	detectDrift = true;
            	ensemblesComDrift.add(i);
            }
        }
        
        if (detectDrift)
        {
        	//Executa estratégia ... ... inclusive a estratégia deve zera o acc e a diversidade (estrategiaInicial())
        	
        	executaEstrategia(ensemblesComDrift);
        	
        }
        
        
        
    }

	private LeveragingBagModificado treinarEnsemble(Instance inst, int i) {
		LeveragingBagModificado ensemble = this.poolOfEnsembles[i];
		
		
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

    private void executaEstrategia(List<Integer> ensemblesComDrift) {
    	
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
        else if (selectionOptionEstrategiaRecuperacao.getChosenLabel().equals("SimpleResetSystem"))
        {
        	estrategiaInicial(); //Inicia todo mundo sem retreino
        }
		
	}

	@Override
    public double[] getVotesForInstance(Instance inst) {
        
        // Se não usa SlidingWindow - Calcula a diversidade
        if (!usaSlidingWindow)
        {
        	for (int i = 0; i < this.poolOfEnsembles.length; i++) {
        		LeveragingBagModificado ensemble = this.poolOfEnsembles[i];
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
        // TODO Auto-generated method stub
    }

    @Override
    protected Measurement[] getModelMeasurementsImpl() {

        return null;
    }

    @Override
    public Classifier[] getSubClassifiers() {
        return this.poolOfEnsembles[this.ultimoEnsembleSelecionadoIndex].getSubClassifiers();
    }
}

