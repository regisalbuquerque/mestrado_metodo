package br.ufam.util;

import java.util.regex.Matcher;

import java.util.regex.Pattern;

public class StringUtil {
	public static String procuraUltimoNumero(String texto)
	{
		if (texto == null) return null;
		Pattern p = Pattern.compile("-?\\d+");
		Matcher m = p.matcher(texto);
		String retorno = null;
		while (m.find()) {
			retorno = m.group();
		}
		return retorno;
	}
	
	public static String limita(Object o, int tam) {
		if (o == null) return null;
		return limita(o.toString(), tam);
	}
	
	public static String limita(String s, int tam) {
		if (s == null) return null;
		String retorno = s.substring(0, Math.min(s.length(), tam));
		return retorno;
	}
	
	public static String limitaFinal(String s, int tam) {
		if (s == null) return null;
		if (s.length()> tam) {
			return s.substring(s.length()-tam,s.length());
		}
		return s;
	}
}
