package br.ufam.util;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author regis
 */
public class CSVUtil {
    
    BufferedWriter buffWriter;
    
    public CSVUtil(String path_fileName) {
        try {
            buffWriter = new BufferedWriter(new FileWriter(path_fileName));
        } catch (IOException ex) {
            Logger.getLogger(CSVUtil.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public CSVUtil(String path, String fileName) {
        try {
            buffWriter = new BufferedWriter(new FileWriter(path + fileName));
        } catch (IOException ex) {
            Logger.getLogger(CSVUtil.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void cabecalho(String cabecalho) 
    {
        try {
            buffWriter.append(cabecalho + "\n");
        } catch (IOException ex) {
            Logger.getLogger(CSVUtil.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void registro(String registro) 
    {
        try {
            buffWriter.append(registro + "\n");
        } catch (IOException ex) {
            Logger.getLogger(CSVUtil.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void fechar() 
    {
        try {
            buffWriter.close();
        } catch (IOException ex) {
            Logger.getLogger(CSVUtil.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
