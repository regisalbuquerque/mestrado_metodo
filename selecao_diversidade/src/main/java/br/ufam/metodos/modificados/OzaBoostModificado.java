package br.ufam.metodos.modificados;

import com.yahoo.labs.samoa.instances.Instance;

import br.ufam.metodo.util.model.EnsembleUtil;
import br.ufam.metodo.util.model.IEnsembleClassifiers;
import moa.classifiers.Classifier;
import moa.classifiers.core.driftdetection.ChangeDetector;
import moa.classifiers.meta.OzaBoost;
import moa.core.MiscUtils;

public class OzaBoostModificado extends OzaBoost implements IEnsembleClassifiers{

	private static final long serialVersionUID = 1L;

    EnsembleUtil ensembleUtil = new EnsembleUtil();
	
	@Override
    public void resetLearningImpl() {
        this.ensemble = new Classifier[this.ensembleSizeOption.getValue()];
        Classifier baseLearner = (Classifier) getPreparedClassOption(this.baseLearnerOption);
        baseLearner.resetLearning();
        for (int i = 0; i < this.ensemble.length; i++) {
            this.ensemble[i] = baseLearner.copy();
        }
        this.scms = new double[this.ensemble.length];
        this.swms = new double[this.ensemble.length];
        
        ensembleUtil.prepare(baseLearner, super.ensemble);
    }

    @Override
    public void trainOnInstanceImpl(Instance inst) {
    	
    	ensembleUtil.praparaTreinamento();
    	
        double lambda_d = ensembleUtil.getLambda();
        for (int i = 0; i < this.ensemble.length; i++) {
        	
        	ensembleUtil.computaClassificadorDrift(this.ensemble[i], inst, i);
        	
            double k = this.pureBoostOption.isSet() ? lambda_d : MiscUtils.poisson(lambda_d, this.classifierRandom);
            if (k > 0.0) {
                Instance weightedInst = (Instance) inst.copy();
                weightedInst.setWeight(inst.weight() * k);
                this.ensemble[i].trainOnInstance(weightedInst);
            }
            if (this.ensemble[i].correctlyClassifies(inst)) {
                this.scms[i] += lambda_d;
                lambda_d *= this.trainingWeightSeenByModel / (2 * this.scms[i]);
            } else {
                this.swms[i] += lambda_d;
                lambda_d *= this.trainingWeightSeenByModel / (2 * this.swms[i]);
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
		super.baseLearnerOption.setValueViaCLIString(baseLearner);
	}
	
}
