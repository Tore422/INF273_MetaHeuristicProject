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
    public static IVectorSolutionRepresentation<Integer> simulatedAnnealingSearch(
            IVectorSolutionRepresentation<Integer> initialSolution) {
        double temperature = INITIAL_TEMPERATURE;
        IVectorSolutionRepresentation<Integer> bestSolution = initialSolution;
        IVectorSolutionRepresentation<Integer> currentlyAcceptedSolution = initialSolution;
        IVectorSolutionRepresentation<Integer> newSolution;
     //   Double min = Double.MAX_VALUE, max = Double.MIN_VALUE;
    //    int numberOfTimesWorseSolutionWasAccepted = 0;
        int numberOfTimesSolutionWasFeasible = 0;
        PartialReinsert.numberOfTimesSolutionIsFeasible = 0;
        SmartOneReinsert.numberOfTimesSolutionIsFeasible = 0;
        SmartTwoExchange.numberOfTimesSolutionIsFeasible = 0;

        SmartTwoExchange.counterA = 0;
        SmartTwoExchange.counterB = 0;

        SmartOneReinsert.infeasibleRandom = 0;
        SmartOneReinsert.infeasibleNormal = 0;
        SmartOneReinsert.infeasibleOutsourced = 0;
        
        SmartOneReinsert.infeasibleCountForNotEmptyVehicles = 0;
        SmartOneReinsert.infeasibleBefore = 0;

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
        System.out.println("PartialReinsert.numberOfTimesSolutionIsFeasible = " + PartialReinsert.numberOfTimesSolutionIsFeasible);
        System.out.println("SmartOneReinsert.numberOfTimesSolutionIsFeasible = " + SmartOneReinsert.numberOfTimesSolutionIsFeasible);
        System.out.println("SmartTwoExchange.numberOfTimesSolutionIsFeasible = " + SmartTwoExchange.numberOfTimesSolutionIsFeasible);
        System.out.println("total: = " + (PartialReinsert.numberOfTimesSolutionIsFeasible
                + SmartOneReinsert.numberOfTimesSolutionIsFeasible + SmartTwoExchange.numberOfTimesSolutionIsFeasible));
     //*/
        System.out.println("SmartTwoExchange.counterA = " + SmartTwoExchange.counterA);
        System.out.println("SmartTwoExchange.counterB = " + SmartTwoExchange.counterB);
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
