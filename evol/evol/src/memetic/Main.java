package memetic;

import java.util.HashMap;

import jmetal.core.Algorithm;
import jmetal.core.Operator;
import jmetal.core.Problem;
import jmetal.core.SolutionSet;
import jmetal.metaheuristics.singleObjective.evolutionStrategy.ElitistES;
import jmetal.metaheuristics.singleObjective.geneticAlgorithm.gGA;
import jmetal.operators.crossover.CrossoverFactory;
import jmetal.operators.localSearch.HillClimbing;
import jmetal.operators.localSearch.MutationLocalSearch;
import jmetal.operators.mutation.MutationFactory;
import jmetal.operators.selection.SelectionFactory;
import jmetal.problems.singleObjective.Rastrigin;
import jmetal.problems.singleObjective.Sphere;
import jmetal.util.JMException;

public class Main {
	public static void main(String [] args) throws JMException, ClassNotFoundException {
		Problem   problem   ;         // The problem to solve
		Algorithm algorithm ;         // The algorithm to use
		Operator  crossover ;         // Crossover operator
		Operator  mutation  ;         // Mutation operator
		Operator  selection ;         // Selection operator
		Operator improvement; 		  // Operator for improvement

		HashMap<String, Double>  parameters ; // Operator parameters

		problem = new Rastrigin("Real", 100);

		algorithm = new MemeticAlgorithm(problem) ;

	    /* Algorithm parameters*/
		algorithm.setInputParameter("populationSize", 100);
		algorithm.setInputParameter("maxEvaluations", 25000);

		// Mutation and Crossover for Binary codification
		parameters = new HashMap<String, Double>() ;
		parameters.put("probability", 0.9) ;
		parameters.put("distributionIndex", 0.1) ;
		crossover = CrossoverFactory.getCrossoverOperator("SBXCrossover", parameters);

		parameters = new HashMap<String, Double>() ;
		parameters.put("probability", 0.1) ;
		parameters.put("perturbation", 0.1) ;
		mutation = MutationFactory.getMutationOperator("UniformMutation", parameters);

	    /* Selection Operator */
		parameters = null ;
		selection = SelectionFactory.getSelectionOperator("BinaryTournament", parameters) ;

		HashMap params = new HashMap() ;
		params.put("improvementRounds", 5) ;
		params.put("problem", problem) ;
		params.put("mutation", mutation) ;
//	    params.put("max_local_gens", 20) ;
		improvement = new MutationLocalSearch(params);
//	    improvement = new HillClimbing(params);

		// Adding the operators to the algorithm
		algorithm.addOperator("crossover", crossover);
		algorithm.addOperator("improvement", improvement);
	    /* Add the operators to the algorithm*/
		algorithm.addOperator("crossover", crossover);
		algorithm.addOperator("mutation", mutation);
		algorithm.addOperator("selection", selection);
	 
	    /* Execute the Algorithm */
		long initTime = System.currentTimeMillis();
		SolutionSet population = algorithm.execute();
		long estimatedTime = System.currentTimeMillis() - initTime;
		System.out.println("Total execution time: " + estimatedTime);

	    /* Log messages */
		System.out.println("Objectives values have been writen to file FUN");
		population.printObjectivesToFile("FUN");
		System.out.println("Variables values have been writen to file VAR");
		population.printVariablesToFile("VAR");
	} //main
}
