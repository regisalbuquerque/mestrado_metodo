package br.ufam.instancias.selecao;
/**
 * VERSÃO 1.0
 * @author AL18
 *
 */
import org.apache.commons.math3.distribution.NormalDistribution;

public class RandomVariableUncertainty extends Strategy{

	private double s;
	private double delta;
	private double theta;
	private double n;
	private double thetaRand;
	
	public RandomVariableUncertainty(double s, double delta, double theta) {
		this.s = s;
		this.delta = delta;
		this.theta = theta;
	}
	
	public boolean compute(double maximumPosteriori) {
		
		NormalDistribution nd = new NormalDistribution(1, this.delta);
		this.n = nd.sample();
		this.thetaRand = this.theta*this.n;
		if(maximumPosteriori < this.thetaRand) {
			this.theta = this.theta*(1-this.s);
			return true;
		}else {
			this.theta = this.theta*(1+this.s);
			return false;
		}
	}
	
	
	public double getS() {
		return s;
	}
	public void setS(double s) {
		this.s = s;
	}
	public double getDelta() {
		return delta;
	}
	public void setDelta(double delta) {
		this.delta = delta;
	}
	public double getTheta() {
		return theta;
	}
	public void setTheta(double theta) {
		this.theta = theta;
	}

	public double getN() {
		return n;
	}

	public void setN(double n) {
		this.n = n;
	}

	public double getThetaRand() {
		return thetaRand;
	}

	public void setThetaRand(double thetaRand) {
		this.thetaRand = thetaRand;
	}
	
}
