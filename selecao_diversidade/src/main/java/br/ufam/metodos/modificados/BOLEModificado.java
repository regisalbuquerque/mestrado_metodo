package br.ufam.metodos.modificados;

import com.yahoo.labs.samoa.instances.Instance;

import br.ufam.metodo.util.model.EnsembleUtil;
import br.ufam.metodo.util.model.IEnsembleClassifiers;
import br.ufam.metodos.originais.BOLE;
import moa.classifiers.Classifier;
import moa.classifiers.core.driftdetection.ChangeDetector;
import moa.core.MiscUtils;

public class BOLEModificado extends BOLE implements IEnsembleClassifiers{

    private static final long serialVersionUID = 1L;

    EnsembleUtil ensembleUtil = new EnsembleUtil();
    

    @Override
    public void resetLearningImpl() {
        this.ensemble = new Classifier[this.ensembleSizeOption.getValue()];
        this.orderPosition = new int[this.ensemble.length];
        Classifier baseLearner = (Classifier) getPreparedClassOption(this.baseLearnerOption);
        baseLearner.resetLearning();
        for (i = 0; i < this.ensemble.length; i++) {
            this.ensemble[i] = baseLearner.copy();
            this.orderPosition[i] = i;
        }
        this.scms = new double[this.ensemble.length];
        this.swms = new double[this.ensemble.length];
        
        ensembleUtil.prepare(baseLearner, this.ensemble);
    }

    @Override
    public void trainOnInstanceImpl(Instance inst) {
    	
    	ensembleUtil.praparaTreinamento();
    	
	// Calculates current accuracy of experts
        double[] acc = new double[this.ensemble.length];
        for (i = 0; i < this.ensemble.length; i++) {
            acc[i] = this.scms[this.orderPosition[i]] + this.swms[this.orderPosition[i]];
            if (acc[i] != 0.0) {
                acc[i] = this.scms[this.orderPosition[i]] / acc[i];
            }
        }
        
	// Sort by accuracy in ascending order
        for (i = 1; i < this.ensemble.length; i++) {
            key_position = this.orderPosition[i];
            key_acc = acc[i];
            j = i - 1;
            while ( (j >=0) && (acc[j] < key_acc) ) {
                this.orderPosition[j+1] = this.orderPosition[j];
                acc[j+1] = acc[j];
                j--;
            }
            this.orderPosition[j+1] = key_position;
            acc[j+1] = key_acc;
        }
        
        correct = false; 
        maxAcc = 0; 
        minAcc = this.ensemble.length - 1; 
        lambda_d = ensembleUtil.getLambda(); 
        for (i = 0; i < this.ensemble.length; i++) {
        	
        	ensembleUtil.computaClassificadorDrift(this.ensemble[i], inst, i);
        	
            if (correct) {
                pos = this.orderPosition[maxAcc];
                maxAcc++;
            } else {
                pos = this.orderPosition[minAcc];
                minAcc--;
            }
            
            if (this.pureBoostOption.isSet())
                k = lambda_d;
            else
                k = MiscUtils.poisson(lambda_d, this.classifierRandom);
            
            if (k > 0.0) {
                Instance weightedInst = (Instance) inst.copy();
                weightedInst.setWeight(inst.weight() * k);
                this.ensemble[pos].trainOnInstance(weightedInst);
            }

	    // Increases or decreases lambda based on the prediction of instance
            if (this.ensemble[pos].correctlyClassifies(inst)) {
                this.scms[pos] += lambda_d;
                lambda_d *= (this.trainingWeightSeenByModel / (2 * this.scms[pos]));
                correct = true;
            } else {
                this.swms[pos] += lambda_d; 
                lambda_d *= (this.trainingWeightSeenByModel / (2 * this.swms[pos]));
                correct = false;
            }
        }
    }
    
	@Override
	public void setChangeDetector(ChangeDetector changeDetector) {
		ensembleUtil.setChangeDetector(changeDetector);
	}

	@Override
	public boolean detectouDrift() {
		return ensembleUtil.isDrift();
	}

    public void estrategiaSimpleReset()
    {
    	ensembleUtil.estrategiaSimpleReset(super.ensemble);
    }

	@Override
	public void setLambda(double lambda) {
		ensembleUtil.setLambda(lambda);
	}

	@Override
	public double getLambda() {
		return ensembleUtil.getLambda();
	}

	@Override
	public void setSize(int size) {
		super.ensembleSizeOption.setValue(size);
	}

	@Override
	public void setBaseLearner(String baseLearner) {
		// TODO Auto-generated method stub
		
	}

}
