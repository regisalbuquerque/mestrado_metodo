package br.ufam.instancias.selecao;
/**
 * VERSÃO 1.0
 * @author AL18
 *
 */
public class VariableUncertainty extends Strategy {

	private double theta;
	private double s;
	
	public VariableUncertainty(double theta, double s) {
		this.theta = theta;
		this.s = s;
	}
	
	public boolean compute(double maximumPosteriori) {
		if(maximumPosteriori < this.theta) {
			this.theta = this.theta*(1-this.s);
			return true;
		}else{
			this.theta = this.theta*(1+s);
			return false;
		}
	}

	public double getTheta() {
		return theta;
	}

	public void setTheta(double theta) {
		this.theta = theta;
	}

	public double getS() {
		return s;
	}

	public void setS(double s) {
		this.s = s;
	}
	
	
}
