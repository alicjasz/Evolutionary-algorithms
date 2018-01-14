package memetic;

import jmetal.core.*;
import jmetal.operators.localSearch.LocalSearch;
import jmetal.util.JMException;
import jmetal.util.comparators.ObjectiveComparator;

import java.util.Comparator;

public class MemeticAlgorithm extends Algorithm{
    /**
     *
     */
    private static final long serialVersionUID = 1L;
//	private int mu_;
//	private int lambda_;

    public MemeticAlgorithm(Problem problem) {
        super(problem);
    }

    /**
     * Execute the Memetic algorithm
     *
     * @throws JMException
     */
    public SolutionSet execute() throws JMException, ClassNotFoundException {
        int populationSize;
        int maxEvaluations;
        int evaluations;

        SolutionSet population;
        SolutionSet offspringPopulation;

        Operator mutationOperator;
        Operator crossoverOperator;
        Operator selectionOperator;
        Operator localSearchOperator;

        Comparator comparator;
        comparator = new ObjectiveComparator(0); // Single objective comparator

        // Read the params
        populationSize = ((Integer) this.getInputParameter("populationSize")).intValue();
        maxEvaluations = ((Integer) this.getInputParameter("maxEvaluations")).intValue();

        // Initialize the variables
        population = new SolutionSet(populationSize);
        offspringPopulation = new SolutionSet(populationSize);

        evaluations = 0;
        // Read the operators
        mutationOperator = this.operators_.get("mutation");
        crossoverOperator = this.operators_.get("crossover");
        selectionOperator = this.operators_.get("selection");
        localSearchOperator = (LocalSearch) operators_.get("improvement");

        // Create the initial population
        Solution newIndividual;
        for (int i = 0; i < populationSize; i++) {
            newIndividual = new Solution(problem_);
            problem_.evaluate(newIndividual);
            evaluations++;
            population.add(newIndividual);
        } //for

        // Sort population
        population.sort(comparator);

        while (evaluations < maxEvaluations) {
            // if ((evaluations % 10) == 0) {
            System.out.println(population.get(0).getObjective(0));
            // } //

            // Copy the best two individuals to the offspring population
            offspringPopulation.add(new Solution(population.get(0)));
//			offspringPopulation.add(new Solution(population.get(1)));

            // Reproductive cycle
            for (int i = 0; i < (populationSize - 1); i++) {
                // Selection
                Solution[] parents = new Solution[2];

                parents[0] = (Solution) selectionOperator.execute(population);
                parents[1] = (Solution) selectionOperator.execute(population);

                // Crossover
                Solution[] offspring = (Solution[]) crossoverOperator.execute(parents);

                // Mutation
                mutationOperator.execute(offspring[0]);

                // Lamarck
                //Solution mutated_solution = (Solution) mutationOperator.execute(offspring[0]);
                //if(offspring[0].getObjective(0) < mutated_solution.getObjective(0))
                //  offspring[0] = mutated_solution;


                // Evaluation of the new individual
                problem_.evaluate(offspring[0]);

                //Baldwin
                Solution local_offspring = (Solution) localSearchOperator.execute(offspring[0]);
                offspring[0].setObjective(0, local_offspring.getObjective(0));
                evaluations += 1;

                // Local Search using HillClimbing
                //offspring[0] = (Solution) localSearchOperator.execute(offspring[0]);


                // Replacement: the two new individuals are inserted in the offspring
                // population
                if(parents[0].getObjective(0) > offspring[0].getObjective(0)) {
                    offspringPopulation.add(offspring[0]);
                }else {
                    offspringPopulation.add(parents[0]);
                }


//				offspringPopulation.add(offspring[1]);
            } // for

            // The offspring population becomes the new current population
            population.clear();
            for (int i = 0; i < populationSize; i++) {
                population.add(offspringPopulation.get(i));
            }
            offspringPopulation.clear();
            population.sort(comparator);
        } // while

        // Return a population with the best individual
        SolutionSet resultPopulation = new SolutionSet(1);
        resultPopulation.add(population.get(0));

        System.out.println("Evaluations: " + evaluations);
        return resultPopulation;
    } // execute
}
