//  ES_main.java
//
//  Author:
//       Antonio J. Nebro <antonio@lcc.uma.es>
//       Juan J. Durillo <durillo@lcc.uma.es>
//
//  Copyright (c) 2011 Antonio J. Nebro, Juan J. Durillo
//
//  This program is free software: you can redistribute it and/or modify
//  it under the terms of the GNU Lesser General Public License as published by
//  the Free Software Foundation, either version 3 of the License, or
//  (at your option) any later version.
//
//  This program is distributed in the hope that it will be useful,
//  but WITHOUT ANY WARRANTY; without even the implied warranty of
//  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
//  GNU Lesser General Public License for more details.
// 
//  You should have received a copy of the GNU Lesser General Public License
//  along with this program.  If not, see <http://www.gnu.org/licenses/>.
package jmetal.metaheuristics.singleObjective.evolutionStrategy;

import jmetal.core.Algorithm;
import jmetal.core.Operator;
import jmetal.core.Problem;
import jmetal.core.SolutionSet;
import jmetal.operators.mutation.MutationFactory;
import jmetal.problems.singleObjective.Rastrigin;
import jmetal.util.JMException;

import java.util.HashMap;

/**
 * This class runs a single-objective Evolution Strategy (ES). The ES can be 
 * a (mu+lambda) ES (class ElitistES) or a (mu,lambda) ES (class NonElitistGA). 
 * The OneMax problem is used to test the algorithms.
 */
public class ES_main {

  public static void main(String [] args) throws JMException, ClassNotFoundException {
    Problem   problem   ;         // The problem to solve
    Algorithm algorithm ;         // The algorithm to use
    Operator  mutation  ;         // Mutation operator
            
    HashMap  parameters ; // Operator parameters

    problem = new Rastrigin("Real", 50);
    
    int mu     ; 
    int lambda ; 
    
    // Requirement: lambda must be divisible by mu
    mu     = 2  ;
    lambda = 4 ;

    //algorithm = new OnePlusOneES(problem, mu, lambda);
    //algorithm = new MuLambda(problem, mu, lambda);
    algorithm = new MuPlusLambda(problem, mu, lambda);

    /* Algorithm params*/
    //algorithm.setInputParameter("maxEvaluations", 20000);
    algorithm.setInputParameter("maxEvaluations", 20000);

    /* Mutation and Crossover for Real codification */
    parameters = new HashMap() ;
    parameters.put("probability", 0.2) ;
    parameters.put("perturbation", 0.8) ;
    mutation = MutationFactory.getMutationOperator("UniformMutation", parameters);
    
    algorithm.addOperator("mutation",mutation);
 
    /* Execute the Algorithm */
    long initTime = System.currentTimeMillis();
    SolutionSet population = algorithm.execute();
    long estimatedTime = System.currentTimeMillis() - initTime;
    System.out.println("Total execution time: "+estimatedTime);

    /* Log messages */
    System.out.println("Objectives values have been writen to file FUN");
    population.printObjectivesToFile("FUN");
    System.out.println("Variables values have been writen to file VAR");
    population.printVariablesToFile("VAR");          
  }//main

} // ES_main
