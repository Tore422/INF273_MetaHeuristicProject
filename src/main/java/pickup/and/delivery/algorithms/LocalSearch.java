package pickup.and.delivery.algorithms;

import pickup.and.delivery.PickupAndDelivery;
import pickup.and.delivery.operators.OneReinsert;
import pickup.and.delivery.operators.ThreeExchange;
import pickup.and.delivery.operators.TwoExchange;
import solution.representations.vector.IVectorSolutionRepresentation;

import java.util.Random;

public class LocalSearch {

    private static final Random RANDOM = new Random();
    private static final int NUMBER_OF_ITERATIONS = 10000;
    private static final double PROBABILITY_OF_USING_TWO_EXCHANGE = 0.15;
    private static final double PROBABILITY_OF_USING_THREE_EXCHANGE = 0.35;
   // private static final double PROBABILITY_OF_USING_ONE_REINSERT = 1 -
   //         PROBABILITY_OF_USING_TWO_EXCHANGE - PROBABILITY_OF_USING_THREE_EXCHANGE;

    public static IVectorSolutionRepresentation<Integer> localSearch(IVectorSolutionRepresentation<Integer> initialSolution) {
        IVectorSolutionRepresentation<Integer> bestSolution = initialSolution;
        IVectorSolutionRepresentation<Integer> currentSolution;
        for (int i = 0; i < NUMBER_OF_ITERATIONS; i++) {
            currentSolution = selectAndApplyOperatorOnSolution(bestSolution);
            if (PickupAndDelivery.feasible(currentSolution)
                    && (PickupAndDelivery.calculateCost(currentSolution)
                    < PickupAndDelivery.calculateCost(bestSolution))) {
                bestSolution = currentSolution;
            }
        }
        return bestSolution;
    }

    private static IVectorSolutionRepresentation<Integer> selectAndApplyOperatorOnSolution(
            IVectorSolutionRepresentation<Integer> solution) {
        IVectorSolutionRepresentation<Integer> currentSolution;
        double operatorChoice = RANDOM.nextDouble();
        if (operatorChoice < PROBABILITY_OF_USING_TWO_EXCHANGE) {
            currentSolution = TwoExchange.useTwoExchangeOnSolution(solution);
        } else if (operatorChoice < (PROBABILITY_OF_USING_TWO_EXCHANGE + PROBABILITY_OF_USING_THREE_EXCHANGE)) {
            currentSolution = ThreeExchange.useThreeExchangeOnSolution(solution);
        } else {
            currentSolution = OneReinsert.useOneReinsertOnSolution(solution);
        }
        return currentSolution;
    }
}
