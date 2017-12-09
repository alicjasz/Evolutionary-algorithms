package jmetal.operators.localSearch;

import java.util.Comparator;
import java.util.HashMap;

import jmetal.core.Operator;
import jmetal.core.Problem;
import jmetal.core.Solution;
import jmetal.core.SolutionSet;
import jmetal.operators.mutation.Mutation;
import jmetal.util.JMException;
import jmetal.util.comparators.DominanceComparator;
import jmetal.util.comparators.OverallConstraintViolationComparator;

public class HillClimbing extends LocalSearch {

	/**
	 * Stores the problem to solve
	 */
	private Problem problem_;

	/**
	 * Stores a reference to the archive in which the non-dominated solutions are
	 * inserted
	 */
	private SolutionSet archive_;

	private int improvementRounds_;

	/**
	 * Stores comparators for dealing with constraints and dominance checking,
	 * respectively.
	 */
	private Comparator constraintComparator_;
	private Comparator dominanceComparator_;

	/**
	 * Stores the mutation operator
	 */
	private Operator mutationOperator_;

	/**
	 * Stores the number of evaluations_ carried out
	 */
	int evaluations_;
	private static final long serialVersionUID = 1L;

	public HillClimbing(HashMap<String, Object> parameters) {
		super(parameters);
		// TODO Auto-generated constructor stub
		if (parameters.get("problem") != null)
			problem_ = (Problem) parameters.get("problem");
		if (parameters.get("improvementRounds") != null)
			improvementRounds_ = (Integer) parameters.get("improvementRounds");
		if (parameters.get("mutation") != null)
			mutationOperator_ = (Mutation) parameters.get("mutation");

		evaluations_ = 0;
		archive_ = null;
		dominanceComparator_ = new DominanceComparator();
		constraintComparator_ = new OverallConstraintViolationComparator();
	}

	@Override
	public int getEvaluations() {
		// TODO Auto-generated method stub
		return evaluations_;
	}

	@Override
	public Object execute(Object object) throws JMException {
		// TODO Auto-generated method stub
		int i = 0;
		int best = 0;
		evaluations_ = 0;
		Solution solution = (Solution) object;

		int rounds = improvementRounds_;
		archive_ = (SolutionSet) getParameter("archive");

		if (rounds <= 0)
			return new Solution(solution);
		while (i < rounds) {
			i++;
			Solution mutatedSolution = new Solution(solution);
			Object a = mutationOperator_.execute(mutatedSolution);
			System.out.println(a.toString());
		}
		return null;
	}

}
