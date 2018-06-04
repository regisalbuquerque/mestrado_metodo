package br.ufam.metodo.util.dados;

import com.yahoo.labs.samoa.instances.Instance;
import com.yahoo.labs.samoa.instances.InstancesHeader;
import moa.options.AbstractOptionHandler;
import moa.streams.ArffFileStream;
import moa.streams.InstanceStream;

public class Dados {
    
    //Deve servir para arff, geradores, etc
    
    AbstractOptionHandler stream ;
    
    
    public Dados(String arffFIle, int classIndex) {
        stream = new ArffFileStream(arffFIle, classIndex);
    }
    
    public Dados(AbstractOptionHandler handler) {
        stream = handler;
    }
    
    public void prepareForUse()
    {
        stream.prepareForUse();
    }
            
    public InstancesHeader getDataHeader()
    {
        return ((InstanceStream)stream).getHeader();
    }
    
    public Instance getProximaInstancia()
    {
        return ((InstanceStream)stream).nextInstance().getData();
    }
    
    public boolean hasMoreInstances()
    {
        return ((InstanceStream)stream).hasMoreInstances();
    }
    
    
}
