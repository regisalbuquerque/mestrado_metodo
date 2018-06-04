package br.ufam.metodo.util.dados;

import com.yahoo.labs.samoa.instances.Instance;
import java.util.ArrayList;
import java.util.List;

public class BufferInstancias {
    
    List<Instance> instancias;

    public BufferInstancias() {
        instancias = new ArrayList<>();
    }
    
    public void incluir(Instance instancia)
    {
        instancias.add(instancia);
    }
    
    public Instance getProximaInstancia()
    {
        if (instancias.size() > 0) 
            return instancias.remove(instancias.size()-1);
        return null;
    }
    
    public boolean hasElementos()
    {
        if (instancias.size() > 0) return true;
        return false;
    }
    
    public int getNumInstancias()
    {
        return instancias.size();
    }
    
    public List<Instance> getInstancias()
    {
        return instancias;
    }
    
}
