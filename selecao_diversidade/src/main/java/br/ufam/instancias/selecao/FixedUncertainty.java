package br.ufam.instancias.selecao;
/**
 * VERSÃO 1.0
 * @author AL18
 *
 */
public class FixedUncertainty extends Strategy{

	private double theta;
	
	public FixedUncertainty(double theta) {
		this.theta = theta;
	}
	
	
	public boolean compute(double maximumPosteriori) {
		if(maximumPosteriori < this.theta) {
			return true;
		}else {
			return false;
		}
	}


	public double getTheta() {
		return theta;
	}


	public void setTheta(double theta) {
		this.theta = theta;
	}
}
