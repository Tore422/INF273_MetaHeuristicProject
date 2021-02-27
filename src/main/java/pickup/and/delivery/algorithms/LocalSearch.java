package pickup.and.delivery.algorithms;

import pickup.and.delivery.PickupAndDelivery;
import pickup.and.delivery.operators.OneReinsert;
import pickup.and.delivery.operators.ThreeExchange;
import pickup.and.delivery.operators.TwoExchange;
import solution.representations.vector.IVectorSolutionRepresentation;

public class LocalSearch {

    public static IVectorSolutionRepresentation<Integer> localSearch(IVectorSolutionRepresentation<Integer> initialSolution) {
        double probabilityOfUsingTwoExchange = 0.33;
        double probabilityOfUsingThreeExchange = 0.33;
        double probabilityOfUsingOneReinsert = 1 - probabilityOfUsingTwoExchange - probabilityOfUsingThreeExchange;
        IVectorSolutionRepresentation<Integer> bestSolution = initialSolution;
        IVectorSolutionRepresentation<Integer> currentSolution;
        int NUMBER_OF_ITERATIONS = 10000;
        for (int i = 0; i < NUMBER_OF_ITERATIONS; i++) {
            double operatorChoice = Math.random();
        //    System.out.println("operatorChoice = " + operatorChoice);
            if (operatorChoice < probabilityOfUsingTwoExchange) {
        //        System.out.println("picked two exchange");
                currentSolution = TwoExchange.useTwoExchangeOnSolution(bestSolution);
            } else if (operatorChoice < (probabilityOfUsingTwoExchange + probabilityOfUsingThreeExchange)) {
        //        System.out.println("picked three exchange");
                currentSolution = ThreeExchange.useThreeExchangeOnSolution(bestSolution);
            } else {
        //        System.out.println("picked 1-reinsert");
                currentSolution = OneReinsert.useOneReinsertOnSolution(bestSolution);
            }
            if (PickupAndDelivery.feasible(currentSolution)
                    && (PickupAndDelivery.calculateCost(currentSolution)
                    < PickupAndDelivery.calculateCost(bestSolution))) {
        //        System.out.println("solution was improved");
                bestSolution = currentSolution;
            }
        }
        return bestSolution;
    }
}
