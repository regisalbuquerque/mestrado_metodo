package br.ufam.util;

import java.math.BigDecimal;

public class Registro {

	String sep = ";";
	StringBuilder registro;

	public Registro() {
		registro = new StringBuilder();
	}

	public void adiciona(Object o, Integer tam) {
		if (o == null)
			registro.append("" + sep);
		else {
			String str = "";
			if (o.getClass() == java.lang.Integer.class)
				str = ((Integer) o).toString();

			else if (o.getClass() == java.math.BigDecimal.class)
				str = ((BigDecimal) o).toString();

			else if (o.getClass() == java.lang.Double.class)
				str = ((Double) o).toString();

			else
				str = (String) o;

			if (tam != null)
				str = StringUtil.limita(str, tam);

			registro.append(str + sep);
		}
	}

	public void adiciona(Object o) {
		adiciona(o, null);
	}

	public void adiciona() {
		adiciona(null, null);
	}

	public String toString() {
		return registro.toString();
	}

	public boolean SoTemNumeros(String str) {
		for (int i = 0; i < str.length(); i++) {
			if (!Character.isDigit(str.charAt(i))) {
				return false;
			}
		}
		return true;
	}
}
