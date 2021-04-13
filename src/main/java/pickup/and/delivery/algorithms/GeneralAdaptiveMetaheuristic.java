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
        IVectorSolutionRepresentation<Integer> bestSolution = adaptiveMetaheuristicSearch(sol);
        System.out.println("bestSolution = " + bestSolution);
        System.out.println("best cost = " + PickupAndDelivery.calculateCost(bestSolution));

        System.out.println("Operator performance stats");
        for (int i = 0; i < NUMBER_OF_ITERATIONS; i++) {
            System.out.println();
            System.out.println("iteration = " + i);
            int operator = operatorSelected.get(i);
            System.out.println("operator = " + Operators.getOperatorNameFromID(operator));
            System.out.println("objectiveValuesBeforeOperatorUse = " + objectiveValuesBeforeOperatorUse.get(i));
            System.out.println("objectiveValuesAfterOperatorUse = " + objectiveValuesAfterOperatorUse.get(i));
            System.out.println("newSolutionWasFeasible = " + newSolutionWasFeasible.get(i));
        }



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

        public static Operators getOperatorNameFromID(int operatorID) {
            if (operatorID == 1) {
                return SMART_ONE_REINSERT;
            } else if (operatorID == 2) {
                return SMART_TWO_EXCHANGE;
            } else if (operatorID == 3) {
                return PARTIAL_REINSERT;
            } else {
                throw new IllegalArgumentException("Not a valid operator ID");
            }
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
    private static boolean foundNewBestSolutionThisIteration;

    private static List<Integer> objectiveValuesBeforeOperatorUse;
    private static List<Integer> objectiveValuesAfterOperatorUse;
    private static List<Boolean> newSolutionWasFeasible;
    private static List<Integer> operatorSelected;


    private GeneralAdaptiveMetaheuristic() {}

    public static IVectorSolutionRepresentation<Integer> adaptiveMetaheuristicSearch(
            IVectorSolutionRepresentation<Integer> initialSolution) {
        IVectorSolutionRepresentation<Integer> bestSolution = initialSolution;
        IVectorSolutionRepresentation<Integer> currentSolution = initialSolution;
        System.out.println("INITIAL_OPERATOR_WEIGHTS = " + INITIAL_OPERATOR_WEIGHTS);
        operatorWeights = initializeOperatorWeights();
        scores = initializeScores();

        System.out.println("Operators.getOperators() = " + Operators.getOperatorsWithID());

        List<IVectorSolutionRepresentation<Integer>> discoveredSolutions = new ArrayList<>();
        List<Integer> objectiveCostOfDiscoveredSolutions = new ArrayList<>();
        objectiveValuesBeforeOperatorUse = new ArrayList<>();
        objectiveValuesAfterOperatorUse = new ArrayList<>();
        newSolutionWasFeasible = new ArrayList<>();
        operatorSelected = new ArrayList<>();
        int bestObjectiveFoundSoFar = PickupAndDelivery.calculateCost(bestSolution);
        int numberOfIterationsSincePreviousBestWasFound = 0;

        int objectiveCostOfCurrentSolution = bestObjectiveFoundSoFar;
        System.out.println("initialSolution = " + initialSolution);
        for (int i = 0; i < NUMBER_OF_ITERATIONS; i++) {
            foundNewBestSolutionThisIteration = false;
            System.out.println();
            System.out.println("iteration number = " + i);
            if (numberOfIterationsSincePreviousBestWasFound > THRESHOLD_FOR_ESCAPING_LOCAL_OPTIMA) {
                System.out.println("Escaping local optima");
                for (int j = 0; j < NUMBER_OF_ESCAPE_ITERATIONS; j++) {
                    currentSolution = useEscapeAlgorithmOnSolution(currentSolution);
                    System.out.println("currentSolution = " + currentSolution);
                    int objectiveCostForCurrentSolution = PickupAndDelivery.calculateCost(currentSolution);
                    if (objectiveCostForCurrentSolution < bestObjectiveFoundSoFar) {
                        bestSolution = currentSolution;
                        bestObjectiveFoundSoFar = objectiveCostForCurrentSolution;
                        break; // Found new global best, so we have definitely left the local optima
                    }
                }
                numberOfIterationsSincePreviousBestWasFound = 0; // Reset to avoid escaping again immediately
            } else {
                objectiveValuesBeforeOperatorUse.add(objectiveCostOfCurrentSolution);
                IVectorSolutionRepresentation<Integer> newSolution = selectAndApplyOperatorOnSolution(currentSolution);
                int objectiveCostOfNewSolution = PickupAndDelivery.calculateCost(newSolution);
                objectiveValuesAfterOperatorUse.add(objectiveCostOfNewSolution);
                newSolutionWasFeasible.add(PickupAndDelivery.feasible(newSolution));
                operatorSelected.add(selectedOperator);
                System.out.println("selectedOperator = " + selectedOperator);
                System.out.println("newSolution = " + newSolution);
                if (!PickupAndDelivery.feasible(newSolution)) {
                    System.out.println("Error, not a feasible solution");
                }

                if (objectiveCostOfNewSolution < bestObjectiveFoundSoFar
                        && PickupAndDelivery.feasible(newSolution)) { // Should always be feasible?
                    System.out.println("Found a new best solution");
                    bestSolution = newSolution;
                    bestObjectiveFoundSoFar = objectiveCostOfNewSolution;
                    numberOfIterationsSincePreviousBestWasFound = 0;
                    foundNewBestSolutionThisIteration = true;
                }
                if (accept(newSolution)) {
                    System.out.println("Solution was acceptable");
                    currentSolution = newSolution;
                    objectiveCostOfCurrentSolution = objectiveCostOfNewSolution;
                }
                updateOperatorSelectionParameters(newSolution, objectiveCostOfCurrentSolution,
                        objectiveCostOfNewSolution, (i + 1), discoveredSolutions, objectiveCostOfDiscoveredSolutions);
                // Update selection parameters before registering solution as discovered,
                // to avoid always finding duplicate of new solution.
                discoveredSolutions.add(newSolution);
                objectiveCostOfDiscoveredSolutions.add(objectiveCostOfNewSolution);
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

    private static final int SEGMENT_SIZE = 10;

    private static void updateOperatorSelectionParameters(
            IVectorSolutionRepresentation<Integer> newSolution,
            int objectiveCostOfCurrentSolution, int objectiveCostForNewSolution, int iterationNumber,
            List<IVectorSolutionRepresentation<Integer>> discoveredSolutions,
            List<Integer> objectiveCostOfDiscoveredSolutions) {
        updateScores(newSolution, objectiveCostOfCurrentSolution, objectiveCostForNewSolution,
                discoveredSolutions, objectiveCostOfDiscoveredSolutions);
        if (iterationNumber % SEGMENT_SIZE == 0) {
            updateWeights(iterationNumber);
        }
    }

    private static final double MINIMUM_WEIGHT = 0.05;
    private static final double R = INITIAL_OPERATOR_WEIGHTS; // What value should this have?

    private static void updateWeights(int iterationNumber) {
        double sum = 0;
        for (int i = 1; i <= NUMBER_OF_OPERATORS; i++) {
            double oldWeight = operatorWeights.get(i);
            int score = scores.get(i);
            double theta = (double) SEGMENT_SIZE;
            double newWeight = oldWeight * (1.0 - R) + (R * (score / theta));
            operatorWeights.put(i, Math.max(newWeight, MINIMUM_WEIGHT));
            System.out.println("theta = " + theta);
            System.out.println("oldWeight = " + oldWeight);
            System.out.println("newWeight = " + newWeight);
            sum += operatorWeights.get(i);
        }
        System.out.println("sum = " + sum);
        // How to update weights?
    }

    private static final int SCORE_FOR_FINDING_UNEXPLORED_SOLUTION = 1;
    private static final int SCORE_FOR_FINDING_BETTER_NEIGHBOUR_SOLUTION = 2;
    private static final int SCORE_FOR_FINDING_NEW_BEST_SOLUTION = 4;

    private static void updateScores(IVectorSolutionRepresentation<Integer> newSolution,
                                     int objectiveCostOfCurrentSolution, int objectiveCostOfNewSolution,
                                     List<IVectorSolutionRepresentation<Integer>> discoveredSolutions,
                                     List<Integer> objectiveCostOfDiscoveredSolutions) {
        int oldScore = scores.get(selectedOperator);
        if (foundNewBestSolutionThisIteration) {
            scores.put(selectedOperator, oldScore + SCORE_FOR_FINDING_NEW_BEST_SOLUTION);
            System.out.println("Adding scores for new best: " + scores.values());
        } else if (objectiveCostOfNewSolution < objectiveCostOfCurrentSolution) {
            scores.put(selectedOperator, oldScore + SCORE_FOR_FINDING_BETTER_NEIGHBOUR_SOLUTION);
            System.out.println("Adding scores for better than current solution: " + scores.values());
        } else {
            boolean foundUnexploredSolution = isUnexploredSolution(
                    newSolution, discoveredSolutions, objectiveCostOfDiscoveredSolutions, objectiveCostOfNewSolution);
            if (foundUnexploredSolution) {
                scores.put(selectedOperator, oldScore + SCORE_FOR_FINDING_UNEXPLORED_SOLUTION);
                System.out.println("Adding scores for unexplored solution: " + scores.values());
            }
        } // Are scores assigned by only the highest amount, or all that apply to the solution?
    }

    private static boolean isUnexploredSolution(IVectorSolutionRepresentation<Integer> newSolution,
                                                List<IVectorSolutionRepresentation<Integer>> discoveredSolutions,
                                                List<Integer> objectiveCostOfDiscoveredSolutions,
                                                int objectiveCostOfNewSolution) {
        boolean foundUnexploredSolution = true;
        int i = 0;
        for (Integer objectiveCost : objectiveCostOfDiscoveredSolutions) {
            if (objectiveCost.equals(objectiveCostOfNewSolution)) {
                IVectorSolutionRepresentation<Integer> possibleDuplicateSolution = discoveredSolutions.get(i);
                if (newSolution.equals(possibleDuplicateSolution)) {
                    foundUnexploredSolution = false;
                    System.out.println("Found duplicate solution of solution from iteration: " + i);
                    break;
                }
            }
            i++;
        }
        return foundUnexploredSolution;
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
