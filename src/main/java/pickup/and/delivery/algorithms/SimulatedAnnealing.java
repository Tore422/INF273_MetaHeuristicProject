package pickup.and.delivery.algorithms;

import pickup.and.delivery.PickupAndDelivery;
import pickup.and.delivery.operators.original.OneReinsert;
import pickup.and.delivery.operators.original.ThreeExchange;
import pickup.and.delivery.operators.original.TwoExchange;
import pickup.and.delivery.operators.custom.PartialReinsert;
import pickup.and.delivery.operators.custom.SmartOneReinsert;
import pickup.and.delivery.operators.custom.SmartTwoExchange;
import solution.representations.vector.IVectorSolutionRepresentation;

import java.util.Random;

public class SimulatedAnnealing {

    private SimulatedAnnealing() {
        throw new IllegalStateException("Utility class");
    }

    private static final Random RANDOM = new Random();
    private static final int NUMBER_OF_ITERATIONS = 10000;
    private static final double INITIAL_TEMPERATURE = 115000000.0;
    private static final double COOLING_FACTOR = 0.9679850447;
    // private static final double FINAL_TEMPERATURE = 0.2;
    private static final double PROBABILITY_OF_USING_TWO_EXCHANGE = 0.15;
    private static final double PROBABILITY_OF_USING_THREE_EXCHANGE = 0.35;
   // private static final double PROBABILITY_OF_USING_ONE_REINSERT = 1 -
   //         PROBABILITY_OF_USING_TWO_EXCHANGE - PROBABILITY_OF_USING_THREE_EXCHANGE;
    private static final double PROBABILITY_OF_USING_SMART_TWO_EXCHANGE = 0.70; // Avg. best solution ~ 21,6M
    private static final double PROBABILITY_OF_USING_PARTIAL_REINSERT = 0.05;
//    private static final double PROBABILITY_OF_USING_SMART_ONE_REINSERT = 1 -
//            PROBABILITY_OF_USING_SMART_TWO_EXCHANGE - PROBABILITY_OF_USING_PARTIAL_REINSERT;

// Best cost 18121675?
// Best cost 18064611? [80, 80, 109, 1, 1, 109, 0, 26, 26, 66, 24, 66, 24, 0, 47, 47, 68, 68, 100, 100, 0, 71, 83, 71, 83, 126, 126, 10, 10, 0, 120, 120, 52, 52, 128, 130, 128, 130, 0, 50, 50, 9, 9, 40, 40, 0, 104, 104, 35, 127, 127, 35, 0, 65, 65, 117, 117, 20, 20, 0, 94, 94, 51, 51, 0, 58, 58, 114, 28, 28, 6, 6, 114, 0, 21, 111, 21, 54, 111, 54, 27, 27, 23, 23, 70, 70, 0, 64, 64, 0, 39, 39, 19, 8, 8, 19, 0, 48, 48, 0, 91, 91, 105, 105, 4, 4, 0, 33, 33, 61, 38, 61, 38, 0, 103, 103, 0, 7, 93, 93, 7, 11, 5, 5, 11, 113, 113, 0, 89, 89, 59, 112, 59, 112, 36, 124, 124, 36, 0, 17, 17, 30, 30, 0, 84, 97, 97, 84, 92, 44, 44, 92, 46, 46, 0, 32, 32, 15, 15, 0, 118, 118, 69, 69, 95, 95, 0, 73, 108, 108, 73, 0, 82, 82, 63, 63, 110, 110, 0, 74, 96, 74, 96, 67, 78, 12, 12, 67, 78, 0, 107, 90, 90, 107, 42, 129, 42, 79, 129, 79, 0, 43, 2, 2, 43, 125, 125, 88, 88, 0, 116, 60, 72, 72, 60, 116, 3, 3, 0, 106, 53, 53, 106, 0, 34, 18, 102, 102, 34, 18, 0, 77, 77, 101, 101, 119, 13, 13, 119, 0, 123, 123, 98, 75, 98, 75, 45, 45, 0, 49, 31, 31, 49, 0, 16, 16, 56, 81, 56, 81, 99, 99, 0, 76, 76, 62, 29, 29, 62, 86, 86, 85, 85, 0, 25, 41, 25, 41, 0, 121, 121, 0, 57, 37, 122, 57, 37, 122, 87, 14, 14, 87, 0, 115, 115, 22, 55, 55, 22, 0]}
// Best cost 17948145? [102, 116, 102, 60, 60, 116, 0, 61, 38, 38, 61, 0, 107, 107, 39, 39, 66, 13, 13, 66, 0, 7, 122, 7, 122, 128, 130, 128, 130, 0, 120, 120, 80, 80, 12, 27, 12, 27, 23, 23, 0, 71, 83, 83, 71, 0, 96, 21, 21, 41, 96, 41, 0, 118, 118, 2, 67, 2, 67, 0, 94, 94, 62, 62, 119, 119, 0, 73, 73, 20, 20, 36, 8, 8, 36, 0, 33, 33, 93, 93, 0, 50, 50, 117, 117, 5, 81, 124, 5, 81, 124, 113, 113, 0, 74, 35, 74, 35, 105, 105, 11, 29, 29, 11, 0, 47, 47, 100, 100, 0, 64, 64, 98, 75, 98, 75, 0, 58, 58, 68, 68, 9, 9, 40, 40, 0, 103, 103, 28, 28, 52, 52, 51, 51, 0, 108, 114, 108, 114, 0, 65, 72, 65, 72, 0, 17, 17, 89, 89, 59, 112, 112, 59, 0, 48, 48, 0, 32, 32, 69, 69, 14, 4, 14, 4, 46, 46, 0, 30, 6, 30, 6, 106, 53, 53, 106, 10, 70, 10, 70, 0, 25, 25, 109, 24, 24, 109, 0, 104, 126, 126, 104, 0, 101, 101, 127, 129, 127, 129, 86, 86, 45, 45, 0, 84, 26, 26, 84, 0, 37, 123, 37, 123, 0, 16, 16, 87, 87, 3, 3, 85, 85, 0, 56, 56, 95, 95, 88, 88, 0, 82, 82, 63, 63, 110, 1, 1, 110, 0, 49, 31, 31, 49, 0, 115, 115, 15, 15, 0, 34, 97, 97, 34, 0, 90, 111, 54, 90, 111, 42, 54, 19, 42, 79, 79, 19, 0, 76, 76, 0, 77, 77, 22, 55, 22, 55, 125, 125, 99, 99, 0, 57, 91, 57, 91, 0, 43, 78, 43, 78, 0, 121, 18, 18, 92, 121, 44, 92, 44, 0]}
    public static IVectorSolutionRepresentation<Integer> simulatedAnnealingSearch(
            IVectorSolutionRepresentation<Integer> initialSolution) {
        double temperature = INITIAL_TEMPERATURE;
        IVectorSolutionRepresentation<Integer> bestSolution = initialSolution;
        IVectorSolutionRepresentation<Integer> currentlyAcceptedSolution = initialSolution;
        IVectorSolutionRepresentation<Integer> newSolution;
     //   Double min = Double.MAX_VALUE, max = Double.MIN_VALUE;
    //    int numberOfTimesWorseSolutionWasAccepted = 0;
        int numberOfTimesSolutionWasFeasible = 0;
        PartialReinsert.numberOfTimesSolutionIsInfeasibleAfterRandomMove = 0;
        SmartOneReinsert.numberOfTimesSolutionIsFeasible = 0;
     //   SmartTwoExchange.numberOfTimesSolutionIsInfeasible = 0;

        SmartOneReinsert.infeasibleRandom = 0;
        SmartOneReinsert.infeasibleNormal = 0;
        SmartOneReinsert.infeasibleOutsourced = 0;
        
        SmartOneReinsert.infeasibleCountForNotEmptyVehicles = 0;

        for (int i = 0; i < NUMBER_OF_ITERATIONS; i++) {
           // System.out.println("Iteration number = " + i);
            newSolution = selectAndApplyCustomOperatorOnSolution(currentlyAcceptedSolution);
        //selectAndApplyOperatorOnSolution(currentlyAcceptedSolution);
          /*  if (deltaE > 0 && deltaE < min) {
                min = deltaE;
            }
            if (deltaE > 0 && deltaE > max) {
                max = deltaE;
            }*/
            if (PickupAndDelivery.feasible(newSolution)) {
                numberOfTimesSolutionWasFeasible++;
                double deltaE = (double) PickupAndDelivery.calculateCost(newSolution)
                        - (double) PickupAndDelivery.calculateCost(currentlyAcceptedSolution);
                if (deltaE < 0) {
                    currentlyAcceptedSolution = newSolution;
                    if (PickupAndDelivery.calculateCost(currentlyAcceptedSolution)
                            < PickupAndDelivery.calculateCost(bestSolution)) {
                        bestSolution = currentlyAcceptedSolution;
                    }
                } else if (RANDOM.nextDouble() < Math.exp(-deltaE / temperature)) {
                  /*  System.out.println("Accepted worse solution with " + Math.exp(-deltaE / temperature));
                    System.out.println("temperature = " + temperature);
                    System.out.println("deltaE = " + deltaE);*/
       //             numberOfTimesWorseSolutionWasAccepted++;
                    currentlyAcceptedSolution = newSolution;
                }
            }
            temperature = COOLING_FACTOR * temperature;
        }
     /*   System.out.println("min = " + min);
        System.out.println("max = " + max);*/
 //       System.out.println("numberOfTimesWorseSolutionWasAccepted = " + numberOfTimesWorseSolutionWasAccepted);
        System.out.println("numberOfTimesSolutionWasFeasible = " + numberOfTimesSolutionWasFeasible);
        System.out.println("PartialReinsert.numberOfTimesSolutionIsFeasible = " + PartialReinsert.numberOfTimesSolutionIsInfeasibleAfterRandomMove);
        System.out.println("SmartOneReinsert.numberOfTimesSolutionIsFeasible = " + SmartOneReinsert.numberOfTimesSolutionIsFeasible);
     //   System.out.println("SmartTwoExchange.numberOfTimesSolutionIsFeasible = " + SmartTwoExchange.numberOfTimesSolutionIsFeasible);
     //   System.out.println("total: = " + (PartialReinsert.numberOfTimesSolutionIsInfeasibleAfterRandomMove
     //           + SmartOneReinsert.numberOfTimesSolutionIsFeasible + SmartTwoExchange.numberOfTimesSolutionIsInfeasible));
     //*/
        System.out.println();
      /*  System.out.println("SmartOneReinsert.infeasibleNormal = " + SmartOneReinsert.infeasibleNormal);
        System.out.println("SmartOneReinsert.infeasibleOutsourced = " + SmartOneReinsert.infeasibleOutsourced);
        System.out.println("SmartOneReinsert.infeasibleRandom = " + SmartOneReinsert.infeasibleRandom);
        System.out.println("SmartOneReinsert.infeasibleCountForNotEmptyVehicles = " + SmartOneReinsert.infeasibleCountForNotEmptyVehicles);
        System.out.println("SmartOneReinsert.infeasibleBefore = " + SmartOneReinsert.infeasibleBefore);//*/
        
        return bestSolution;
    }

    private static IVectorSolutionRepresentation<Integer> selectAndApplyOperatorOnSolution(
            IVectorSolutionRepresentation<Integer> currentlyAcceptedSolution) {
        IVectorSolutionRepresentation<Integer> newSolution;
        double operatorChoice = RANDOM.nextDouble();
        if (operatorChoice < PROBABILITY_OF_USING_TWO_EXCHANGE) {
            newSolution = TwoExchange.useTwoExchangeOnSolution(currentlyAcceptedSolution);
        } else if (operatorChoice < (PROBABILITY_OF_USING_TWO_EXCHANGE + PROBABILITY_OF_USING_THREE_EXCHANGE)) {
            newSolution = ThreeExchange.useThreeExchangeOnSolution(currentlyAcceptedSolution);
        } else {
            newSolution = OneReinsert.useOneReinsertOnSolution(currentlyAcceptedSolution);
        }
        return newSolution;
    }

    private static IVectorSolutionRepresentation<Integer> selectAndApplyCustomOperatorOnSolution(
            IVectorSolutionRepresentation<Integer> currentlyAcceptedSolution) {
        IVectorSolutionRepresentation<Integer> newSolution;
        double operatorChoice = RANDOM.nextDouble();
        if (operatorChoice < PROBABILITY_OF_USING_SMART_TWO_EXCHANGE) {
            newSolution = SmartTwoExchange.useSmartTwoExchangeOnSolution(currentlyAcceptedSolution);
        } else if (operatorChoice < (PROBABILITY_OF_USING_SMART_TWO_EXCHANGE + PROBABILITY_OF_USING_PARTIAL_REINSERT)) {
            newSolution = PartialReinsert.usePartialReinsertOnSolution(currentlyAcceptedSolution);
        } else {
            newSolution = SmartOneReinsert.useSmartOneReinsertOnSolution(currentlyAcceptedSolution);
        }
        return newSolution;
    }
}
