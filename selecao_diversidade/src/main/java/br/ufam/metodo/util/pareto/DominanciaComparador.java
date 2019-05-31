package br.ufam.metodo.util.pareto;

/**
 * Interface for comparing two solutions using a dominance relation.  A
 * dominance relation may impose a partial or total ordering on a set of 
 * solutions.
 * <p>
 * Implementations which also implement {@link Comparator} impose a 
 * total ordering on the set of solutions.  However, it is typically the case
 * that {@code (compare(x, y)==0) == (x.equals(y))} does not hold, and the
 * comparator may impose orderings that are inconsistent with equals.
 */


	/**
	 * Compares the two solutions using a dominance relation, returning
	 * {@code -1} if {@code solution1} dominates {@code solution2}, {@code 1} if
	 * {@code solution2} dominates {@code solution1}, and {@code 0} if the
	 * solutions are non-dominated.
	 * 
	 * @param solution1 the first solution
	 * @param solution2 the second solution
	 * @return {@code -1} if {@code solution1} dominates {@code solution2},
	 *         {@code 1} if {@code solution2} dominates {@code solution1}, and
	 *         {@code 0} if the solutions are non-dominated
	 */

//BASEADO EM: org.moeaframework.core.comparator.ParetoObjectiveComparator
public class DominanciaComparador {
	
	public static int compare(Solucao solution1, Solucao solution2) {
		boolean dominate1 = false;
		boolean dominate2 = false;

		//Objective 1
		if (solution1.getValor1() < solution2.getValor1()) {
			dominate1 = true;

			if (dominate2) {
				return 0;
			}
		} else if (solution1.getValor1() > solution2.getValor1()) {
			dominate2 = true;

			if (dominate1) {
				return 0;
			}
		}
		
		//Objective 2
		if (solution1.getValor2() < solution2.getValor2()) {
			dominate1 = true;

			if (dominate2) {
				return 0;
			}
		} else if (solution1.getValor2() > solution2.getValor2()) {
			dominate2 = true;

			if (dominate1) {
				return 0;
			}
		}

		if (dominate1 == dominate2) {
			return 0;
		} else if (dominate1) {
			return -1;
		} else {
			return 1;
		}
	}

}
