package br.ufam.metodos.modificados;

import com.yahoo.labs.samoa.instances.Instance;

import br.ufam.metodo.util.model.EnsembleUtil;
import br.ufam.metodo.util.model.IEnsembleClassifiers;
import moa.classifiers.Classifier;
import moa.classifiers.core.driftdetection.ChangeDetector;
import moa.classifiers.meta.OzaBag;
import moa.core.MiscUtils;


public class OzaBagModificado extends OzaBag implements IEnsembleClassifiers {
	
	private static final long serialVersionUID = 1L;

    EnsembleUtil ensembleUtil = new EnsembleUtil();
	
    @Override
    public void resetLearningImpl() 
    {
    	super.ensemble = new Classifier[this.ensembleSizeOption.getValue()];
    	Classifier baseLearner = (Classifier) getPreparedClassOption(this.baseLearnerOption);
        ensembleUtil.prepare(baseLearner, super.ensemble);
    }

    @Override
    public void trainOnInstanceImpl(Instance inst) {
   	
    	ensembleUtil.praparaTreinamento();
    	
        for (int i = 0; i < this.ensemble.length; i++) {
        	
        	ensembleUtil.computaClassificadorDrift(this.ensemble[i], inst, i);
        	
            int k = MiscUtils.poisson(ensembleUtil.getLambda(), classifierRandom);
            Instance weightedInst = (Instance) inst.copy();
            if (k > 0) {
                weightedInst.setWeight(inst.weight() * k);
                this.ensemble[i].trainOnInstance(weightedInst);
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
