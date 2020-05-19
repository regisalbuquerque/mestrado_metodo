package testes;

import com.yahoo.labs.samoa.instances.Instance;

import br.ufam.metodo.util.medidor.Indicadores;
import br.ufam.metodos.originais.BOLE;
import moa.classifiers.Classifier;
import moa.streams.generators.LEDGenerator;

public class TestClassifier {

	public static void main(String[] args) {
		System.out.println("Teste de Classificador!");
		
		LEDGenerator base = new LEDGenerator();
		base.prepareForUse();
        
        Classifier classificadorEmTeste = new BOLE();
        classificadorEmTeste.setModelContext(base.getHeader());
        classificadorEmTeste.prepareForUse();
        
		Indicadores indicadores = new Indicadores();
        
        for (int iteracao = 0; iteracao < 100; iteracao++) {
            
            Instance instanciaAtual = base.nextInstance().getData();

            if (classificadorEmTeste.correctlyClassifies(instanciaAtual)) {
                indicadores.acertou();
            } else {
                indicadores.errou();
            }

            classificadorEmTeste.trainOnInstance(instanciaAtual);
        }
        System.out.println("Resultado: " + indicadores.getMediaTaxaAcerto());

	}

}
