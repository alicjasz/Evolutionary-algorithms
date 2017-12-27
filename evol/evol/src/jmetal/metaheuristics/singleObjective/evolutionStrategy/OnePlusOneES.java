package jmetal.metaheuristics.singleObjective.evolutionStrategy;

import jmetal.core.*;
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
        Comparator comparator       ;

        comparator = new ObjectiveComparator(0) ; // Single objective comparator

        // Read the params
        maxEvaluations = ((Integer)this.getInputParameter("maxEvaluations")).intValue();

        // Initialize the variables
        population          = new SolutionSet(mu_) ;
        offspringPopulation = new SolutionSet(lambda_) ;

        evaluations  = 0;

        // Read the operators
        mutationOperator  = this.operators_.get("mutation");

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
            Solution mutated = (Solution) mutationOperator.execute(offspring);
            // Lamarck
            if(comparator.compare(mutated, offspring) > 0)
                offspring = mutated;

            problem_.evaluate(offspring);
            offspringPopulation.add(offspring);
            evaluations++;

            // STEP 2. Add the mu (parent) individual to the offspring population
            //offspringPopulation.add(population.get(0)) ;

            // STEP 3. Sort the mu+lambda population
            //offspringPopulation.sort(comparator) ;

            if(comparator.compare(betterIndividual, offspringPopulation.get(0)) > 0) {
                betterIndividual = new Solution(offspringPopulation.get(0));
                population.clear();
                // STEP 4. Create the new mu population
                population.add(betterIndividual);
            }

            System.out.println(/*evaluations + " " +*/
                    population.get(0).getObjective(0)) ;

            // STEP 6. Delete the offspring population
            offspringPopulation.clear() ;
        } // while

        // Return a population with the best individual
        SolutionSet resultPopulation = new SolutionSet(1) ;
        resultPopulation.add(population.get(0)) ;

        return resultPopulation ;
    } // execute
} // ElitistES
