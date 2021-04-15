package pickup.and.delivery.algorithms;

import pickup.and.delivery.PickupAndDelivery;
import pickup.and.delivery.operators.custom.KReinsert;
import pickup.and.delivery.operators.custom.PartialReinsert;
import pickup.and.delivery.operators.custom.SmartOneReinsert;
import pickup.and.delivery.operators.custom.SmartTwoExchange;
import solution.representations.vector.IVectorSolutionRepresentation;
import solution.representations.vector.VectorSolutionRepresentation;

import java.util.*;

import static pickup.and.delivery.operators.OperatorUtilities.RANDOM;
import static pickup.and.delivery.operators.OperatorUtilities.countNumberOfCallsInSolution;

public class GeneralAdaptiveMetaheuristic {

    public static void main(String[] args) {
        //List<Integer> values = Arrays.asList(0, 0, 0, 1, 1, 2, 2, 3, 3, 4, 4, 5, 5, 6, 6, 7, 7);
        List<Integer> values = Arrays.asList(0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 2, 2, 3, 3, 4, 4, 5, 5, 6, 6, 7, 7, 8, 8, 9, 9, 10, 10, 11, 11, 12, 12, 13, 13, 14, 14, 15, 15, 16, 16, 17, 17, 18, 18, 19, 19, 20, 20, 21, 21, 22, 22, 23, 23, 24, 24, 25, 25, 26, 26, 27, 27, 28, 28, 29, 29, 30, 30, 31, 31, 32, 32, 33, 33, 34, 34, 35, 35, 36, 36, 37, 37, 38, 38, 39, 39, 40, 40, 41, 41, 42, 42, 43, 43, 44, 44, 45, 45, 46, 46, 47, 47, 48, 48, 49, 49, 50, 50, 51, 51, 52, 52, 53, 53, 54, 54, 55, 55, 56, 56, 57, 57, 58, 58, 59, 59, 60, 60, 61, 61, 62, 62, 63, 63, 64, 64, 65, 65, 66, 66, 67, 67, 68, 68, 69, 69, 70, 70, 71, 71, 72, 72, 73, 73, 74, 74, 75, 75, 76, 76, 77, 77, 78, 78, 79, 79, 80, 80, 81, 81, 82, 82, 83, 83, 84, 84, 85, 85, 86, 86, 87, 87, 88, 88, 89, 89, 90, 90, 91, 91, 92, 92, 93, 93, 94, 94, 95, 95, 96, 96, 97, 97, 98, 98, 99, 99, 100, 100, 101, 101, 102, 102, 103, 103, 104, 104, 105, 105, 106, 106, 107, 107, 108, 108, 109, 109, 110, 110, 111, 111, 112, 112, 113, 113, 114, 114, 115, 115, 116, 116, 117, 117, 118, 118, 119, 119, 120, 120, 121, 121, 122, 122, 123, 123, 124, 124, 125, 125, 126, 126, 127, 127, 128, 128, 129, 129, 130, 130);

        IVectorSolutionRepresentation<Integer> sol = new VectorSolutionRepresentation<>(values);
        IVectorSolutionRepresentation<Integer> bestSolution = adaptiveMetaheuristicSearch(sol);
        System.out.println("bestSolution = " + bestSolution);
        System.out.println("best cost = " + PickupAndDelivery.calculateCost(bestSolution));
        //printOperatorUsageStatistics();
        printOperatorUsageStatisticsForIteration(0);
        printOperatorUsageStatisticsForIteration(100);
        printOperatorUsageStatisticsForIteration(1000);
        printOperatorUsageStatisticsForIteration(9999);
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
            int i = 1;
            for (Operators operatorName : Operators.values()) {
                if (operatorID == i) {
                    return operatorName;
                }
                i++;
            }
            throw new IllegalArgumentException("Not a valid operator ID");
        }
    }

    private static final int NUMBER_OF_OPERATORS = Operators.values().length;
    private static final double INITIAL_OPERATOR_WEIGHTS = 1.0 / NUMBER_OF_OPERATORS;
    private static final int NUMBER_OF_ITERATIONS = 10000;
    private static final int NUMBER_OF_ESCAPE_ITERATIONS = 20;
    private static final int THRESHOLD_FOR_ESCAPING_LOCAL_OPTIMA = 500;
    private static Map<Integer, Double> operatorWeights;
    private static Map<Integer, Integer> scores;
    private static Map<Integer, Integer> numberOfTimesEachOperatorHasBeenUsedThisSegment;
    private static int selectedOperator;
    private static int numberOfReinsertsForEscapeAlgorithm;
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
     //   System.out.println("INITIAL_OPERATOR_WEIGHTS = " + INITIAL_OPERATOR_WEIGHTS);
        operatorWeights = initializeOperatorWeights();
        scores = initializeScores();
        numberOfTimesEachOperatorHasBeenUsedThisSegment = initializeOperatorUsageCounter();

      //  System.out.println("Operators.getOperators() = " + Operators.getOperatorsWithID());

        List<IVectorSolutionRepresentation<Integer>> discoveredSolutions = new ArrayList<>();
        List<Integer> objectiveCostOfDiscoveredSolutions = new ArrayList<>();
        objectiveValuesBeforeOperatorUse = new ArrayList<>();
        objectiveValuesAfterOperatorUse = new ArrayList<>();
        newSolutionWasFeasible = new ArrayList<>();
        operatorSelected = new ArrayList<>();
        numberOfReinsertsForEscapeAlgorithm = countNumberOfCallsInSolution(initialSolution) / 2;
        int bestObjectiveFoundSoFar = PickupAndDelivery.calculateCost(bestSolution);
        int numberOfIterationsSincePreviousBestWasFound = 0;

        int numberOfTimesSolutionWasInfeasible = 0;

        int objectiveCostOfCurrentSolution = bestObjectiveFoundSoFar;
       // System.out.println("initialSolution = " + initialSolution);
        for (int i = 0; i < NUMBER_OF_ITERATIONS; i++) {
            foundNewBestSolutionThisIteration = false;
        //    System.out.println();
        //    System.out.println("iteration number = " + i);
            if (numberOfIterationsSincePreviousBestWasFound > THRESHOLD_FOR_ESCAPING_LOCAL_OPTIMA) {
          //      System.out.println("Escaping local optima");
                for (int j = 0; j < NUMBER_OF_ESCAPE_ITERATIONS; j++) {
                    currentSolution = useEscapeAlgorithmOnSolution(currentSolution);
        //            System.out.println("currentSolution = " + currentSolution);
                    int objectiveCostOfNewCurrentSolution = PickupAndDelivery.calculateCost(currentSolution);
                    registerOperatorUsageStatistics(objectiveCostOfCurrentSolution, objectiveCostOfNewCurrentSolution,
                            PickupAndDelivery.feasible(currentSolution));
                    objectiveCostOfCurrentSolution = objectiveCostOfNewCurrentSolution;
                    if (objectiveCostOfNewCurrentSolution < bestObjectiveFoundSoFar) {
                        bestSolution = currentSolution;
                        bestObjectiveFoundSoFar = objectiveCostOfNewCurrentSolution;
                        break; // Found new global best, so we have definitely left the local optima
                    }
                }
                numberOfIterationsSincePreviousBestWasFound = 0; // Reset to avoid escaping again immediately
            } else {
                IVectorSolutionRepresentation<Integer> newSolution = selectAndApplyOperatorOnSolution(currentSolution);
                int objectiveCostOfNewSolution = PickupAndDelivery.calculateCost(newSolution);
                boolean newSolutionIsFeasible = PickupAndDelivery.feasible(newSolution);
                registerOperatorUsageStatistics(objectiveCostOfCurrentSolution,
                        objectiveCostOfNewSolution, newSolutionIsFeasible);
             //   System.out.println("selectedOperator = " + selectedOperator);
             //   System.out.println("newSolution = " + newSolution);
                if (!newSolutionIsFeasible) {
                    numberOfTimesSolutionWasInfeasible++;
             //       System.out.println("Error, not a feasible solution");
                }
                if (objectiveCostOfNewSolution < bestObjectiveFoundSoFar
                        && newSolutionIsFeasible) { // Should always be feasible?
           //         System.out.println("iteration Number = " + i);
            //        System.out.println("Found a new best solution");
                    bestSolution = newSolution;
                    bestObjectiveFoundSoFar = objectiveCostOfNewSolution;
                    numberOfIterationsSincePreviousBestWasFound = 0;
                    foundNewBestSolutionThisIteration = true;
                }
                if (accept(newSolution)) {
              //      System.out.println("Solution was acceptable");
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
        System.out.println("numberOfTimesSolutionWasInfeasible = " + numberOfTimesSolutionWasInfeasible);
        return bestSolution;
    }

    private static void registerOperatorUsageStatistics(int objectiveCostOfCurrentSolution,
                                                        int objectiveCostOfNewSolution, boolean newSolutionIsFeasible) {
        objectiveValuesBeforeOperatorUse.add(objectiveCostOfCurrentSolution);
        objectiveValuesAfterOperatorUse.add(objectiveCostOfNewSolution);
        newSolutionWasFeasible.add(newSolutionIsFeasible);
        operatorSelected.add(selectedOperator);
    }

    private static void printOperatorUsageStatistics() {
        int numberOfTimesEscapeAlgorithmWasUsed = 0;
        int lastIterationEscapeAlgorithmWasUsed = 0;
        System.out.println("Operator performance stats");
        for (int i = 0; i < NUMBER_OF_ITERATIONS; i++) {
            System.out.println();
            System.out.println("iteration = " + i);
            int operator = operatorSelected.get(i);
            if (operator == 0) {
                System.out.println("operator = Escape algorithm");
                numberOfTimesEscapeAlgorithmWasUsed++;
                lastIterationEscapeAlgorithmWasUsed = i;
            } else {
                System.out.println("operator = " + Operators.getOperatorNameFromID(operator));
            }
            System.out.println("objectiveValuesBeforeOperatorUse = " + objectiveValuesBeforeOperatorUse.get(i));
            System.out.println("objectiveValuesAfterOperatorUse = " + objectiveValuesAfterOperatorUse.get(i));
            System.out.println("newSolutionWasFeasible = " + newSolutionWasFeasible.get(i));

            System.out.println("numberOfTimesEscapeAlgorithmWasUsed = "
                    + numberOfTimesEscapeAlgorithmWasUsed / NUMBER_OF_ESCAPE_ITERATIONS);
            System.out.println("lastIterationEscapeAlgorithmWasUsed = " + lastIterationEscapeAlgorithmWasUsed);
        }
    }

    private static void printOperatorUsageStatisticsForIteration(int iterationNumber) {
        System.out.println();
        System.out.println("iteration = " + iterationNumber);
        int operator = operatorSelected.get(iterationNumber);
        if (operator == 0) {
            System.out.println("operator = Escape algorithm");
        } else {
            System.out.println("operator = " + Operators.getOperatorNameFromID(operator));
        }
        System.out.println("objectiveValuesBeforeOperatorUse = "
                + objectiveValuesBeforeOperatorUse.get(iterationNumber));
        System.out.println("objectiveValuesAfterOperatorUse = "
                + objectiveValuesAfterOperatorUse.get(iterationNumber));
        System.out.println("newSolutionWasFeasible = " + newSolutionWasFeasible.get(iterationNumber));
    }

    private static final int ZERO = 0;

    private static Map<Integer, Integer> initializeScores() {
        Map<Integer, Integer> initialScores = new HashMap<>();
        for (int i = 1; i <= NUMBER_OF_OPERATORS; i++) {
            initialScores.put(i, ZERO);
        }
    //    System.out.println("initialScores.values = " + initialScores.values());
    //    System.out.println("initialScores.keys = " + initialScores.keySet());
        return initialScores;
    }

    private static Map<Integer, Double> initializeOperatorWeights() {
        Map<Integer, Double> operatorWeights = new HashMap<>();
        for (int i = 1; i <= NUMBER_OF_OPERATORS; i++) {
            operatorWeights.put(i, INITIAL_OPERATOR_WEIGHTS);
        }
    //    System.out.println("operatorWeights.values() = " + operatorWeights.values());
    //    System.out.println("operatorWeights.keySet() = " + operatorWeights.keySet());
        return operatorWeights;
    }

    private static Map<Integer, Integer> initializeOperatorUsageCounter() {
        Map<Integer, Integer> initialOperatorUsage = new HashMap<>();
        for (int i = 1; i <= NUMBER_OF_OPERATORS; i++) {
            initialOperatorUsage.put(i, ZERO);
        }
    //    System.out.println("initialOperatorUsage.values() = " + initialOperatorUsage.values());
    //    System.out.println("initialOperatorUsage.keySet() = " + initialOperatorUsage.keySet());
        return initialOperatorUsage;
    }

    private static final int SEGMENT_SIZE = 100;

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
    private static final double R = 0.8;// Reaction factor [0,1].
                                                             // Determines how quickly the weights should change.
    private static void updateWeights(int iterationNumber) {
        double sum = 0;
        for (int currentOperatorID = 1; currentOperatorID <= NUMBER_OF_OPERATORS; currentOperatorID++) {
            double oldWeight = operatorWeights.get(currentOperatorID);
            double score = (double) scores.get(currentOperatorID);
            double theta = Math.max(1.0, numberOfTimesEachOperatorHasBeenUsedThisSegment.get(currentOperatorID));
            double newWeight = oldWeight * (1.0 - R) + (R * (score / theta));
            operatorWeights.put(currentOperatorID, Math.max(newWeight, MINIMUM_WEIGHT));
       //     System.out.println("theta = " + theta);
       //     System.out.println("oldWeight = " + oldWeight);
            sum += operatorWeights.get(currentOperatorID);
        }
        for (int currentOperatorID = 1; currentOperatorID <= NUMBER_OF_OPERATORS; currentOperatorID++) {
            double normalizedNewWeight = operatorWeights.get(currentOperatorID) / sum;
            operatorWeights.put(currentOperatorID, normalizedNewWeight);
        //    System.out.println("normalizedNewWeight = " + normalizedNewWeight);
        }
        resetScores();
        resetOperatorUsagePerSegment();
    }

    private static void resetScores() {
        for (int i = 1; i <= NUMBER_OF_OPERATORS; i++) {
            scores.put(i, ZERO);
        }
    }

    private static void resetOperatorUsagePerSegment() {
        for (int i = 1; i <= NUMBER_OF_OPERATORS; i++) {
            numberOfTimesEachOperatorHasBeenUsedThisSegment.put(i, ZERO);
        }
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
      //      System.out.println("Adding scores for new best: " + scores.values());
        } else if (objectiveCostOfNewSolution < objectiveCostOfCurrentSolution) {
            scores.put(selectedOperator, oldScore + SCORE_FOR_FINDING_BETTER_NEIGHBOUR_SOLUTION);
      //      System.out.println("Adding scores for better than current solution: " + scores.values());
        } else {
            boolean foundUnexploredSolution = isUnexploredSolution(
                    newSolution, discoveredSolutions, objectiveCostOfDiscoveredSolutions, objectiveCostOfNewSolution);
            if (foundUnexploredSolution) {
                scores.put(selectedOperator, oldScore + SCORE_FOR_FINDING_UNEXPLORED_SOLUTION);
      //          System.out.println("Adding scores for unexplored solution: " + scores.values());
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
             //       System.out.println("Found duplicate solution of solution from iteration: " + i);
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

    private static final int ONE = 1;

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
        int oldNumberOfOperatorUsage = numberOfTimesEachOperatorHasBeenUsedThisSegment.get(selectedOperator);
        numberOfTimesEachOperatorHasBeenUsedThisSegment.put(selectedOperator, oldNumberOfOperatorUsage + ONE);
        return newSolution;
    }

    private static IVectorSolutionRepresentation<Integer> useEscapeAlgorithmOnSolution(
            IVectorSolutionRepresentation<Integer> solution) {
        selectedOperator = 0;
        return KReinsert.useKReinsertOnSolution(solution, numberOfReinsertsForEscapeAlgorithm);
    }
}
