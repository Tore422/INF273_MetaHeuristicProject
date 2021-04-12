package pickup.and.delivery.algorithms;

import pickup.and.delivery.PickupAndDelivery;
import pickup.and.delivery.operators.custom.PartialReinsert;
import pickup.and.delivery.operators.custom.SmartOneReinsert;
import pickup.and.delivery.operators.custom.SmartTwoExchange;
import pickup.and.delivery.operators.original.ThreeExchange;
import solution.representations.vector.IVectorSolutionRepresentation;
import solution.representations.vector.VectorSolutionRepresentation;

import java.util.*;

import static pickup.and.delivery.operators.OperatorUtilities.RANDOM;

public class GeneralAdaptiveMetaheuristic {

    public static void main(String[] args) {
        List<Integer> values = Arrays.asList(0, 0, 0, 1, 1, 2, 2, 3, 3, 4, 4, 5, 5, 6, 6, 7, 7);
        IVectorSolutionRepresentation<Integer> sol = new VectorSolutionRepresentation<>(values);
        System.out.println("sol = " + adaptiveMetaheuristicSearch(sol));
    }

    enum Operators {
        SMART_ONE_REINSERT,
        SMART_TWO_EXCHANGE,
        PARTIAL_REINSERT;

        public static Map<Operators, Integer> getOperatorsWithID() {
            Map<Operators, Integer> operatorIndices = new EnumMap<>(Operators.class);
            int i = 1;
            for (Operators operator : Operators.values()) {
                operatorIndices.put(operator, i++);
            }
            return operatorIndices;
        }
    }

    private static final int NUMBER_OF_OPERATORS = Operators.values().length;
    private static final double INITIAL_OPERATOR_WEIGHTS = 1.0 / NUMBER_OF_OPERATORS;
    private static final int NUMBER_OF_ITERATIONS = 10000;
    private static final int NUMBER_OF_ESCAPE_ITERATIONS = 20;
    private static final int THRESHOLD_FOR_ESCAPING_LOCAL_OPTIMA = 500;
    private static Map<Integer, Double> operatorWeights;
    private static Map<Integer, Integer> scores;
    private static int selectedOperator;

    private GeneralAdaptiveMetaheuristic() {}

    public static IVectorSolutionRepresentation<Integer> adaptiveMetaheuristicSearch(
            IVectorSolutionRepresentation<Integer> initialSolution) {
        IVectorSolutionRepresentation<Integer> bestSolution = initialSolution;
        IVectorSolutionRepresentation<Integer> currentSolution = initialSolution;
        System.out.println("INITIAL_OPERATOR_WEIGHTS = " + INITIAL_OPERATOR_WEIGHTS);
        operatorWeights = initializeOperatorWeights();
        scores = initializeScores();

        System.out.println("Operators.getOperators() = " + Operators.getOperatorsWithID());


        /*
        int bestObjectiveFoundSoFar = PickupAndDelivery.calculateCost(bestSolution);
        int numberOfIterationsSincePreviousBestWasFound = 0;
        for (int i = 0; i < NUMBER_OF_ITERATIONS; i++) {
            if (numberOfIterationsSincePreviousBestWasFound > THRESHOLD_FOR_ESCAPING_LOCAL_OPTIMA) {
                for (int j = 0; j < NUMBER_OF_ESCAPE_ITERATIONS; j++) {
                    currentSolution = useEscapeAlgorithmOnSolution(currentSolution);
                    int objectiveCostForCurrentSolution = PickupAndDelivery.calculateCost(currentSolution);
                    if (objectiveCostForCurrentSolution < bestObjectiveFoundSoFar) {
                        bestSolution = currentSolution;
                        bestObjectiveFoundSoFar = objectiveCostForCurrentSolution;
                        break; // Found new global best, so we have definitely left the local optima
                    }
                }
                numberOfIterationsSincePreviousBestWasFound = 0; // Reset to avoid escaping again immediately
            } else {
                IVectorSolutionRepresentation<Integer> newSolution = selectAndApplyOperatorOnSolution(currentSolution);
                if (!PickupAndDelivery.feasible(newSolution)) {
                    System.out.println("Error, not a feasible solution");
                }
                int objectiveCostForNewSolution = PickupAndDelivery.calculateCost(newSolution);
                if (objectiveCostForNewSolution < bestObjectiveFoundSoFar
                        && PickupAndDelivery.feasible(newSolution)) { // Should always be feasible?
                    bestSolution = newSolution;
                    bestObjectiveFoundSoFar = objectiveCostForNewSolution;
                    numberOfIterationsSincePreviousBestWasFound = 0;
                }
                if (accept(newSolution)) {
                    currentSolution = newSolution;
                }
                updateOperatorSelectionParameters(newSolution, currentSolution);
                numberOfIterationsSincePreviousBestWasFound++;
            }
        }//*/
        return bestSolution;
    }

    private static final int ZERO = 0;

    private static Map<Integer, Integer> initializeScores() {
        Map<Integer, Integer> initialScores = new HashMap<>();
        for (int i = 1; i <= NUMBER_OF_OPERATORS; i++) {
            initialScores.put(i, ZERO);
        }
        System.out.println("initialScores.values = " + initialScores.values());
        System.out.println("initialScores.keys = " + initialScores.keySet());
        return initialScores;
    }

    private static Map<Integer, Double> initializeOperatorWeights() {
        Map<Integer, Double> operatorWeights = new HashMap<>();
        for (int i = 1; i <= NUMBER_OF_OPERATORS; i++) {
            operatorWeights.put(i, INITIAL_OPERATOR_WEIGHTS);
        }
        System.out.println("operatorWeights.values() = " + operatorWeights.values());
        System.out.println("operatorWeights.keySet() = " + operatorWeights.keySet());
        return operatorWeights;
    }

    private static final int SCORE_FOR_FINDING_UNEXPLORED_SOLUTION = 1;
    private static final int SCORE_FOR_FINDING_BETTER_NEIGHBOUR_SOLUTION = 2;
    private static final int SCORE_FOR_FINDING_NEW_BEST_SOLUTION = 4;

    private static void updateOperatorSelectionParameters(IVectorSolutionRepresentation<Integer> newSolution,
                                                          IVectorSolutionRepresentation<Integer> currentSolution) {











    }

    private static boolean accept(IVectorSolutionRepresentation<Integer> newSolution) {
        return PickupAndDelivery.feasible(newSolution);
    }


    private static IVectorSolutionRepresentation<Integer> selectAndApplyOperatorOnSolution(
            IVectorSolutionRepresentation<Integer> currentSolution) {
        IVectorSolutionRepresentation<Integer> newSolution;
        final double PROBABILITY_OF_SELECTING_SMART_ONE_REINSERT = operatorWeights.get(1);
        final double PROBABILITY_OF_SELECTING_SMART_TWO_EXCHANGE = operatorWeights.get(1) + operatorWeights.get(2);
        Map<Operators, Integer> operatorsWithID = Operators.getOperatorsWithID();
        double operatorChoice = RANDOM.nextDouble();
        if (operatorChoice < PROBABILITY_OF_SELECTING_SMART_ONE_REINSERT) {
            newSolution = SmartOneReinsert.useSmartOneReinsertOnSolution(currentSolution);
            selectedOperator = operatorsWithID.get(Operators.SMART_ONE_REINSERT);
        } else if (operatorChoice < PROBABILITY_OF_SELECTING_SMART_TWO_EXCHANGE) {
            newSolution = SmartTwoExchange.useSmartTwoExchangeOnSolution(currentSolution);
            selectedOperator = operatorsWithID.get(Operators.SMART_TWO_EXCHANGE);
        } else {
            newSolution = PartialReinsert.usePartialReinsertOnSolution(currentSolution);
            selectedOperator = operatorsWithID.get(Operators.PARTIAL_REINSERT);
        }
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
