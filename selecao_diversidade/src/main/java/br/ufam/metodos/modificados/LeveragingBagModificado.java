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
package br.ufam.metodos.modificados;

import com.yahoo.labs.samoa.instances.Instance;

import br.ufam.metodo.util.model.EnsembleUtil;
import br.ufam.metodo.util.model.IEnsembleClassifiers;
import moa.classifiers.Classifier;
import moa.classifiers.core.driftdetection.ADWIN;
import moa.classifiers.core.driftdetection.ChangeDetector;
import moa.classifiers.meta.LeveragingBag;
import moa.core.MiscUtils;

public class LeveragingBagModificado extends LeveragingBag implements IEnsembleClassifiers{

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
        this.ADError = new ADWIN[this.ensemble.length];
        for (int i = 0; i < this.ensemble.length; i++) {
            this.ADError[i] = new ADWIN((double) this.deltaAdwinOption.getValue());
        }
        this.numberOfChangesDetected = 0;
        if (this.outputCodesOption.isSet()) {
            this.initMatrixCodes = true;
        }
        
        ensembleUtil.prepare(baseLearner, this.ensemble);
        
    }

    @Override
    public void trainOnInstanceImpl(Instance inst) {
    	
    	ensembleUtil.praparaTreinamento();
    	
        int numClasses = inst.numClasses();
        //Output Codes
        if (this.initMatrixCodes == true) {
            this.matrixCodes = new int[this.ensemble.length][inst.numClasses()];
            for (int i = 0; i < this.ensemble.length; i++) {
                int numberOnes;
                int numberZeros;

                do { // until we have the same number of zeros and ones
                    numberOnes = 0;
                    numberZeros = 0;
                    for (int j = 0; j < numClasses; j++) {
                        int result = 0;
                        if (j == 1 && numClasses == 2) {
                            result = 1 - this.matrixCodes[i][0];
                        } else {
                            result = (this.classifierRandom.nextBoolean() ? 1 : 0);
                        }
                        this.matrixCodes[i][j] = result;
                        if (result == 1) {
                            numberOnes++;
                        } else {
                            numberZeros++;
                        }
                    }
                } while ((numberOnes - numberZeros) * (numberOnes - numberZeros) > (this.ensemble.length % 2));

            }
            this.initMatrixCodes = false;
        }


        boolean Change = false;
        Instance weightedInst = (Instance) inst.copy();
        double w = ensembleUtil.getLambda();

        //Train ensemble of classifiers
        for (int i = 0; i < this.ensemble.length; i++) {
        	
        	ensembleUtil.computaClassificadorDrift(this.ensemble[i], inst, i);
        	
            double k = 0.0;
            switch (this.leveraginBagAlgorithmOption.getChosenIndex()) {
                case 0: //LeveragingBag
                    k = MiscUtils.poisson(w, this.classifierRandom);
                    break;
                case 1: //LeveragingBagME
                    double error = this.ADError[i].getEstimation();
                    k = !this.ensemble[i].correctlyClassifies(weightedInst) ? 1.0 : (this.classifierRandom.nextDouble() < (error / (1.0 - error)) ? 1.0 : 0.0);
                    break;
                case 2: //LeveragingBagHalf
                    w = 1.0;
                    k = this.classifierRandom.nextBoolean() ? 0.0 : w;
                    break;
                case 3: //LeveragingBagWT
                    w = 1.0;
                    k = 1.0 + MiscUtils.poisson(w, this.classifierRandom);
                    break;
                case 4: //LeveragingSubag
                    w = 1.0;
                    k = MiscUtils.poisson(1, this.classifierRandom);
                    k = (k > 0) ? w : 0;
                    break;
            }
            if (k > 0) {
                if (this.outputCodesOption.isSet()) {
                    weightedInst.setClassValue((double) this.matrixCodes[i][(int) inst.classValue()]);
                }
                weightedInst.setWeight(inst.weight() * k);
                this.ensemble[i].trainOnInstance(weightedInst);
            }
            boolean correctlyClassifies = this.ensemble[i].correctlyClassifies(weightedInst);
            double ErrEstim = this.ADError[i].getEstimation();
            if (this.ADError[i].setInput(correctlyClassifies ? 0 : 1)) {
                if (this.ADError[i].getEstimation() > ErrEstim) {
                    Change = true;
                }
            }
        }
        if (Change) {
            numberOfChangesDetected++;
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

