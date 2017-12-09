package memetic;

import java.util.HashMap;

import jmetal.core.Algorithm;
import jmetal.core.Operator;
import jmetal.core.Problem;
import jmetal.core.SolutionSet;
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
	    
	    //int bits ; // Length of bit string in the OneMax problem
	    HashMap<String, Double>  parameters ; // Operator parameters

	    int bits = 512;
	    //problem = new OneMax("Binary", bits);
	 
//	    problem = new Sphere("Real", 100);
	    //problem = new Easom("Real") ;
	    //problem = new Griewank("Real", 10) ;
	    problem = new Rastrigin("Real", 50);
	    
	    algorithm = new gGA(problem) ; // Generational GA
	    //algorithm = new ssGA(problem); // Steady-state GA
	    //algorithm = new scGA(problem) ; // Synchronous cGA
	    //algorithm = new acGA(problem) ;   // Asynchronous cGA
	    
	    /* Algorithm parameters*/
	    algorithm.setInputParameter("populationSize", 1000);
	    algorithm.setInputParameter("maxEvaluations", 25000);
	    /*
	    // Mutation and Crossover for Real codification 
	    parameters = new HashMap() ;
	    parameters.put("probability", 0.9) ;
	    parameters.put("distributionIndex", 20.0) ;
	    crossover = CrossoverFactory.getCrossoverOperator("SBXCrossover", parameters);                   

	    parameters = new HashMap() ;
	    parameters.put("probability", 1.0/problem.getNumberOfVariables()) ;
	    parameters.put("distributionIndex", 20.0) ;
	    mutation = MutationFactory.getMutationOperator("PolynomialMutation", parameters);                    
	    */
	    
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
	    params.put("improvementRounds", 1) ;
	    params.put("problem", problem) ;
	    params.put("mutation", mutation) ;
//	    improvement = new MutationLocalSearch(parameters);
	    improvement = new HillClimbing(params);

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
