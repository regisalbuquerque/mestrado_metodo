package br.ufam.metodos.modificados;

import com.github.javacliparser.FloatOption;
import com.yahoo.labs.samoa.instances.Instance;

import br.ufam.metodo.util.model.IEnsembleClassifiers;
import moa.classifiers.Classifier;
import moa.classifiers.core.driftdetection.ADWIN;
import moa.classifiers.core.driftdetection.DDM;
import moa.classifiers.meta.OzaBag;
import moa.core.MiscUtils;
import moa.options.ClassOption;


public class OzaBagModificado extends OzaBag implements IEnsembleClassifiers {
	
	private static final long serialVersionUID = 1L;

	public ClassOption baseLearner1Option = new ClassOption("baseLearner1", '1',
            "Classifier to train.", Classifier.class, "trees.HoeffdingTree");
    
    public ClassOption baseLearner2Option = new ClassOption("baseLearner2", '2',
            "Classifier to train.", Classifier.class, "lazy.kNN");
    
    public ClassOption baseLearner3Option = new ClassOption("baseLearner3", '3',
            "Classifier to train.", Classifier.class, "trees.RandomHoeffdingTree");
    
    public ClassOption baseLearner4Option = new ClassOption("baseLearner4", '4',
            "Classifier to train.", Classifier.class, "functions.Perceptron");
    
    public ClassOption baseLearner5Option = new ClassOption("baseLearner5", '5',
            "Classifier to train.", Classifier.class, "bayes.NaiveBayes");
    
	public FloatOption deltaAdwinOption = new FloatOption("deltaAdwin", 'a',
            "Delta of Adwin change detection", 0.002, 0.0, 1.0);
	
    protected int numBaseLearners;
	protected Classifier[] baseLearners = new Classifier[5];
    protected ADWIN[] ADError; //Para cada classificador
    protected DDM[] DDM;
    protected boolean drift;
    public double lambda;
	
    @Override
    public void resetLearningImpl() {

    	super.ensemble = new Classifier[this.ensembleSizeOption.getValue()];
        
        this.baseLearners[0] = ((Classifier) getPreparedClassOption(this.baseLearner1Option));
        this.baseLearners[1] = ((Classifier) getPreparedClassOption(this.baseLearner2Option));
        this.baseLearners[2] = ((Classifier) getPreparedClassOption(this.baseLearner3Option));
        this.baseLearners[3] = ((Classifier) getPreparedClassOption(this.baseLearner4Option));
        this.baseLearners[4] = ((Classifier) getPreparedClassOption(this.baseLearner5Option));
        
        for (int i = 0; i < this.baseLearners.length; i++) {
            this.baseLearners[i].resetLearning();
        }
        
        int j = 0;
        int max = numBaseLearners; //Distribui os baseLearners de acordo com o parametro numBaseLeanersOption
        for (int i=0; i < this.ensemble.length; i++) {
            if (j >= max) j = 0;
            this.ensemble[i] = this.baseLearners[j++].copy(); 
        }
        
        //Cria os ADWIN e DDM para cada Classificador
        this.ADError = new ADWIN[this.ensemble.length];
        this.DDM = new DDM[this.ensemble.length];
        for (int i = 0; i < this.ensemble.length; i++) {
            this.ADError[i] = new ADWIN((double) this.deltaAdwinOption.getValue());
            this.DDM[i] = new DDM();
        }

    }
    
    @Override
    public void trainOnInstanceImpl(Instance inst) {

    	boolean Change = false;
        this.drift = false;  // A priori DRIFT igual a FALSE
    	
        for (int i = 0; i < this.ensemble.length; i++) {
        	
            int k = MiscUtils.poisson(this.lambda, classifierRandom);
            Instance weightedInst = (Instance) inst.copy();
            if (k > 0) {
                weightedInst.setWeight(inst.weight() * k);
                this.ensemble[i].trainOnInstance(weightedInst);
            }
            
            
            boolean correctlyClassifies = this.ensemble[i].correctlyClassifies(inst);
//            double ErrEstim = this.ADError[i].getEstimation();
//            if (this.ADError[i].setInput(correctlyClassifies ? 0 : 1)) {
//                if (this.ADError[i].getEstimation() > ErrEstim) {
//                    Change = true;
//                }
//            }
            
            this.ADError[i].setInput(correctlyClassifies ? 0 : 1); //Para poder encontrar o PIOR
            
            this.DDM[i].input(correctlyClassifies ? 0 : 1);
            if (this.DDM[i].getChange())
            {
            	Change = true;
            }
            
        }
        
        if (Change) {
            this.drift = true;       //DRIFT igual a TRUE
        }
        
    }
	
	
	@Override
	public boolean detectouDrift() {
		return this.drift;
	}

    public void estrategiaSimpleReset()
    {
    	double max = 0.0;
        int imax = -1;
        for (int i = 0; i < this.ensemble.length; i++) {
            if (max < this.ADError[i].getEstimation()) {
                max = this.ADError[i].getEstimation();
                imax = i;
            }
        }
        if (imax != -1) {
            this.ensemble[imax].resetLearning();
            //this.ensemble[imax].trainOnInstance(inst);
            this.ADError[imax] = new ADWIN((double) this.deltaAdwinOption.getValue());
            //this.DDM[imax] = new DDM();  
        }
    }

	@Override
	public void setLambda(double lambda) {
		this.lambda = lambda;
	}

	@Override
	public double getLambda() {
		return this.lambda;
	}

	@Override
	public void setSize(int size) {
		super.ensembleSizeOption.setValue(size);
	}

	@Override
	public void setNumBaseLearners(int num) {
		this.numBaseLearners = num;
	}

	@Override
	public void setBaseLearner1(String baseLearner) {
		this.baseLearnerOption.setValueViaCLIString(baseLearner);
		this.baseLearner1Option.setValueViaCLIString(baseLearner);
	}

	@Override
	public void setBaseLearner2(String baseLearner) {
		this.baseLearner2Option.setValueViaCLIString(baseLearner);
	}

	@Override
	public void setBaseLearner3(String baseLearner) {
		this.baseLearner3Option.setValueViaCLIString(baseLearner);
	}

	@Override
	public void setBaseLearner4(String baseLearner) {
		this.baseLearner4Option.setValueViaCLIString(baseLearner);
	}

	@Override
	public void setBaseLearner5(String baseLearner) {
		this.baseLearner5Option.setValueViaCLIString(baseLearner);
	}

}
