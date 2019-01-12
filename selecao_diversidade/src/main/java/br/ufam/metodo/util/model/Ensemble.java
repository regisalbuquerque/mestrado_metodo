package br.ufam.metodo.util.model;

import com.yahoo.labs.samoa.instances.Instance;

import moa.classifiers.Classifier;
import moa.classifiers.core.driftdetection.ADWIN;
import moa.classifiers.core.driftdetection.DDM;
import moa.core.DoubleVector;
import moa.core.Measurement;
import moa.core.MiscUtils;

public class Ensemble extends AbstractEnsemble{
    

    protected Classifier[] baseLearners;
    protected Classifier[] classificadores;
    protected ADWIN[] ADError; //Para cada classificador
    protected DDM[] DDM;
    
    protected boolean drift;
    
    public double lambda;
    
    @Override
    public void resetLearningImpl(){
        this.classificadores = new Classifier[ensembleSizeOption.getValue()];
        
        this.baseLearners = new Classifier[5];
        this.baseLearners[0] = ((Classifier) getPreparedClassOption(this.baseLearner1Option));
        this.baseLearners[1] = ((Classifier) getPreparedClassOption(this.baseLearner2Option));
        this.baseLearners[2] = ((Classifier) getPreparedClassOption(this.baseLearner3Option));
        this.baseLearners[3] = ((Classifier) getPreparedClassOption(this.baseLearner4Option));
        this.baseLearners[4] = ((Classifier) getPreparedClassOption(this.baseLearner5Option));
        
        for (int i = 0; i < this.baseLearners.length; i++) {
            this.baseLearners[i].resetLearning();
        }
        

        int j = 0;
        int max = numBaseLeanersOption.getValue(); //Distribui os baseLearners de acordo com o parametro numBaseLeanersOption
        for (int i=0; i < this.classificadores.length; i++) {
            if (j >= max) j = 0;
            this.classificadores[i] = this.baseLearners[j++].copy(); 
        }
        
        //Cria os ADWIN e DDM para cada Classificador
        this.ADError = new ADWIN[this.classificadores.length];
        this.DDM = new DDM[this.classificadores.length];
        for (int i = 0; i < this.classificadores.length; i++) {
            this.ADError[i] = new ADWIN((double) this.deltaAdwinOption.getValue());
            this.DDM[i] = new DDM();
        }


        this.lambda = lambdaOption.getValue();
        
    }
    
    

    @Override
    public void trainOnInstanceImpl(Instance inst) {

    	boolean Change = false;
        this.drift = false;  // A priori DRIFT igual a FALSE
    	
        for (int i = 0; i < this.classificadores.length; i++) {
        	Instance weightedInst = (Instance) inst.copy();
            int k = MiscUtils.poisson(this.lambda, classifierRandom);
            if (k > 0) {
                weightedInst.setWeight(inst.weight() * k);
                this.classificadores[i].trainOnInstance(weightedInst);
            }
            
            
            boolean correctlyClassifies = this.classificadores[i].correctlyClassifies(weightedInst);
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
    
    public void estrategiaSimpleReset()
    {
    	double max = 0.0;
        int imax = -1;
        for (int i = 0; i < this.classificadores.length; i++) {
            if (max < this.ADError[i].getEstimation()) {
                max = this.ADError[i].getEstimation();
                imax = i;
            }
        }
        if (imax != -1) {
            this.classificadores[imax].resetLearning();
            //this.ensemble[imax].trainOnInstance(inst);
            this.ADError[imax] = new ADWIN((double) this.deltaAdwinOption.getValue());
            //this.DDM[imax] = new DDM();  
        }
    }
    
    

    @Override
    public double[] getVotesForInstance(Instance inst) {
        DoubleVector combinedVote = new DoubleVector();
        for (int i = 0; i < this.classificadores.length; i++) {
            DoubleVector vote = new DoubleVector(this.classificadores[i].getVotesForInstance(inst));
            if (vote.sumOfValues() > 0.0) {
                vote.normalize();
                combinedVote.addValues(vote);
            }
        }
        
  
        return combinedVote.getArrayRef();
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
    public Measurement[] getModelMeasurementsImpl() {
        return new Measurement[]{new Measurement("ensemble size",
                    this.classificadores != null ? this.classificadores.length : 0)};
    }

    @Override
    public Classifier[] getSubClassifiers() {
        return this.classificadores.clone();
    }
    
    public boolean detectouDrift()
    {
    	return this.drift;
    }
    
}
