package br.ufam.metodo.util.dados;

import com.yahoo.labs.samoa.instances.Instance;

import br.ufam.metodo.util.calculo.Matematica;
import br.ufam.metodo.util.model.InstanciaDistancia;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class JanelaDeslizante {
    
    int num; //Se igual a -1 -> Janela sem deslizamento
    
    List<Instance> instancias;

    public JanelaDeslizante(int num) {
        instancias = new ArrayList<>();
        this.num = num;
    }
    
    public void incluir(Instance instancia)
    {
        instancias.add(instancia);
        if (this.num != -1 && instancias.size() >= this.num)
            instancias.remove(0); //Deslizamento
    }
    
    public boolean isEmpty()
    {
    	if (instancias == null) return true;
    	return instancias.isEmpty();
    }
    
    public List<Instance> getSemelhantes(int k, Instance x)
    {
        
        if (this.instancias == null || this.instancias.isEmpty()) return null;
        
        List<InstanciaDistancia> lista = new ArrayList<>();
        
        for (Instance instancia : instancias) {
            lista.add(new InstanciaDistancia(instancia, Matematica.euclidean(x, instancia)));
        }
        
        Collections.sort(lista);
        
        List<Instance> listaSemelhantes = new ArrayList<>();
        
        for (int i = 0; i < k && i < lista.size(); i++) {
            listaSemelhantes.add(lista.get(i).instance);
        }
        
        return listaSemelhantes;
    }
    
    public List<Instance> getInstancias()
    {
        return instancias;
    }
    
}
