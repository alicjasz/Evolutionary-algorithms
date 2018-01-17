package jmetal.metaheuristics.singleObjective.evolutionStrategy;

import jmetal.core.*;
import jmetal.operators.localSearch.LocalSearch;
import jmetal.util.JMException;
import jmetal.util.comparators.ObjectiveComparator;

import java.util.Comparator;

public class OnePlusOneES extends Algorithm {

    private int     mu_     ;
    private int     lambda_ ;
    /**
     * Constructor
     *
     * @param problem The problem to be solved
     */
    public OnePlusOneES(Problem problem, int mu, int lambda){
        super(problem) ;
        mu_      = mu     ;
        lambda_  = lambda ;
    } // ElitistES

    /**
     * Execute the ElitistES algorithm
     * @throws JMException
     */
    public SolutionSet execute() throws JMException, ClassNotFoundException {
        int maxEvaluations ;
        int evaluations    ;

        SolutionSet population          ;
        SolutionSet offspringPopulation ;

        Solution betterIndividual ;

        Operator mutationOperator ;
        Comparator comparator     ;
        Operator localSearchOperator ;


        comparator = new ObjectiveComparator(0) ; // Single objective comparator

        // Read the params
        maxEvaluations = ((Integer)this.getInputParameter("maxEvaluations")).intValue();

        // Initialize the variables
        population          = new SolutionSet(mu_) ;
        offspringPopulation = new SolutionSet(lambda_) ;

        evaluations  = 0;

        // Read the operators
        mutationOperator  = this.operators_.get("mutation");
        localSearchOperator = (LocalSearch) operators_.get("improvement");

        System.out.println("(" + mu_ + " + " + lambda_+")ES") ;

        // Create 1-parent population of mu solutions
        Solution newIndividual;
        newIndividual = new Solution(problem_);
        problem_.evaluate(newIndividual);
        evaluations++;
        betterIndividual = new Solution(newIndividual);
        population.add(betterIndividual);

        // Main loop
        while (evaluations < maxEvaluations) {
            // STEP 1. Generate the offspring
            Solution offspring = new Solution(population.get(0)) ;
            mutationOperator.execute(offspring);
            /*Solution mutated_solution = (Solution) mutationOperator.execute(offspring);
            if(offspring.getObjective(0) < mutated_solution.getObjective(0))
                offspring = mutated_solution;*/
            problem_.evaluate(offspring);
            Solution local_offspring = (Solution) localSearchOperator.execute(offspring);
            offspring.setObjective(0, local_offspring.getObjective(0));
            offspringPopulation.add(offspring);
            evaluations++;

            if(comparator.compare(betterIndividual, offspringPopulation.get(0)) > 0) {
                betterIndividual = new Solution(offspringPopulation.get(0));
                population.clear();
                // STEP 4. Create the new mu population
                population.add(betterIndividual);
            }

            System.out.println(population.get(0).getObjective(0)) ;

            // STEP 6. Delete the offspring population
            offspringPopulation.clear() ;
        } // while

        // Return a population with the best individual
        SolutionSet resultPopulation = new SolutionSet(1) ;
        resultPopulation.add(population.get(0)) ;

        return resultPopulation ;
    } // execute
} // ElitistES
