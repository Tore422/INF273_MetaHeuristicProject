package pickup.and.delivery.algorithms;

import pickup.and.delivery.PickupAndDelivery;
import pickup.and.delivery.operators.original.ThreeExchange;
import solution.representations.vector.IVectorSolutionRepresentation;
import solution.representations.vector.VectorSolutionRepresentation;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static pickup.and.delivery.operators.OperatorUtilities.RANDOM;

public class GeneralAdaptiveMetaheuristic {

    public static void main(String[] args) {
        List<Integer> values = Arrays.asList(0, 0, 0, 1, 1, 2, 2, 3, 3, 4, 4, 5, 5, 6, 6, 7, 7);
        IVectorSolutionRepresentation<Integer> sol = new VectorSolutionRepresentation<>(values);
        adaptiveMetaheuristicSearch(sol);
    }


    private static final int NUMBER_OF_OPERATORS = 3;
    private static final double INITIAL_OPERATOR_WEIGHTS = 1.0 / NUMBER_OF_OPERATORS;
    private static final int NUMBER_OF_ITERATIONS = 10000;
    private static final int NUMBER_OF_ESCAPE_ITERATIONS = 20;
    private static final int THRESHOLD_FOR_ESCAPING_LOCAL_OPTIMA = 500;

    private GeneralAdaptiveMetaheuristic() {}

    public static IVectorSolutionRepresentation<Integer> adaptiveMetaheuristicSearch(
            IVectorSolutionRepresentation<Integer> initialSolution) {
        IVectorSolutionRepresentation<Integer> bestSolution = initialSolution;
        IVectorSolutionRepresentation<Integer> currentSolution = initialSolution;

        System.out.println("INITIAL_OPERATOR_WEIGHTS = " + INITIAL_OPERATOR_WEIGHTS);
        Map<Integer, Double> operatorWeights = initializeOperatorWeights();
        





/*

        int bestObjectiveFoundSoFar = PickupAndDelivery.calculateCost(bestSolution);
        int numberOfIterationsSincePreviousImprovement = 0;
        for (int i = 0; i < NUMBER_OF_ITERATIONS; i++) {
            if (numberOfIterationsSincePreviousImprovement > THRESHOLD_FOR_ESCAPING_LOCAL_OPTIMA) {
                for (int j = 0; j < NUMBER_OF_ESCAPE_ITERATIONS; j++) {
                    currentSolution = useEscapeAlgorithmOnSolution(currentSolution);
                    int objectiveCostForCurrentSolution = PickupAndDelivery.calculateCost(currentSolution);
                    if (objectiveCostForCurrentSolution < bestObjectiveFoundSoFar) {
                        bestSolution = currentSolution;
                        bestObjectiveFoundSoFar = objectiveCostForCurrentSolution;
                        break; // Found new global best, so we have definitely left the local optima
                    }
                }
                numberOfIterationsSincePreviousImprovement = 0; // Reset to avoid escaping again immediately
            } else {
                IVectorSolutionRepresentation<Integer> newSolution = selectAndApplyOperatorOnSolution(currentSolution);
                if (!PickupAndDelivery.feasible(newSolution)) {
                    System.out.println("Error, not a feasible solution");
                }
                int objectiveCostForNewSolution = PickupAndDelivery.calculateCost(newSolution);
                if (objectiveCostForNewSolution < bestObjectiveFoundSoFar) {
                    bestSolution = newSolution;
                    bestObjectiveFoundSoFar = objectiveCostForNewSolution;
                }
                if (accept(newSolution, currentSolution)) {
                    currentSolution = newSolution;
                    updateOperatorSelectionParameters();
                    numberOfIterationsSincePreviousImprovement = 0;
                } else {
                    numberOfIterationsSincePreviousImprovement++;
                }
            }
        }*/
        return bestSolution;
    }

    private static Map<Integer, Double> initializeOperatorWeights() {
        Map<Integer, Double> map = new HashMap<>();
        for (int i = 1; i <= NUMBER_OF_OPERATORS; i++) {
            map.put(i, INITIAL_OPERATOR_WEIGHTS);
        }
        System.out.println("map.values() = " + map.values());
        System.out.println("map.keySet() = " + map.keySet());
        return map;
    }

    private static void updateOperatorSelectionParameters() {



    }

    private static boolean accept(IVectorSolutionRepresentation<Integer> newSolution,
                                  IVectorSolutionRepresentation<Integer> currentSolution) {
        return PickupAndDelivery.feasible(newSolution);
    }


    private static IVectorSolutionRepresentation<Integer> selectAndApplyOperatorOnSolution(
            IVectorSolutionRepresentation<Integer> currentSolution) {
        IVectorSolutionRepresentation<Integer> newSolution = null;
        double operatorChoice = RANDOM.nextDouble();
       // if (operatorChoice < )





        return newSolution;
    }

    // TODO: Replace with an operator that only provides divers, feasible solutions!
    private static IVectorSolutionRepresentation<Integer> useEscapeAlgorithmOnSolution(
            IVectorSolutionRepresentation<Integer> solution) {
        IVectorSolutionRepresentation<Integer> newSolution;
        do {
            newSolution = ThreeExchange.useThreeExchangeOnSolution(solution);
        } while (!PickupAndDelivery.feasible(newSolution));
        return newSolution;
    }
}
