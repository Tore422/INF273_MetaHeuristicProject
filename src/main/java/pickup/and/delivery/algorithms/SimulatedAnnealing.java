package pickup.and.delivery.algorithms;

import pickup.and.delivery.PickupAndDelivery;
import pickup.and.delivery.operators.OneReinsert;
import pickup.and.delivery.operators.ThreeExchange;
import pickup.and.delivery.operators.TwoExchange;
import solution.representations.vector.IVectorSolutionRepresentation;

import java.util.Random;

public class SimulatedAnnealing {

    private static final Random RANDOM = new Random();

    public static IVectorSolutionRepresentation<Integer> simulatedAnnealingSearch(IVectorSolutionRepresentation<Integer> initialSolution) {
        double probabilityOfUsingTwoExchange = 0.33;
        double probabilityOfUsingThreeExchange = 0.33;
        double probabilityOfUsingOneReinsert = 1 - probabilityOfUsingTwoExchange - probabilityOfUsingThreeExchange;
        double initialTemperature = 100;
        double coolingFactor = 0.1;
        double temperature = initialTemperature;
        IVectorSolutionRepresentation<Integer> bestSolution = initialSolution;
        IVectorSolutionRepresentation<Integer> currentlyAcceptedSolution = initialSolution;
        IVectorSolutionRepresentation<Integer> newSolution;
     //   Double min = Double.MAX_VALUE, max = Double.MIN_VALUE;
        final int NUMBER_OF_ITERATIONS = 10000;
        for (int i = 0; i < NUMBER_OF_ITERATIONS; i++) {
            double operatorChoice = RANDOM.nextDouble();
            newSolution = applySelectedOperatorOnSolution(
                    probabilityOfUsingTwoExchange, probabilityOfUsingThreeExchange,
                    currentlyAcceptedSolution, operatorChoice);
            double deltaE = PickupAndDelivery.calculateCost(newSolution)
                    - PickupAndDelivery.calculateCost(currentlyAcceptedSolution);
            boolean newSolutionIsFeasible = PickupAndDelivery.feasible(newSolution);
          /*  if (deltaE > 0 && deltaE < min) {
                min = deltaE;
            }
            if (deltaE > 0 && deltaE > max) {
                max = deltaE;
            }*/
            if (newSolutionIsFeasible && deltaE < 0) {
                currentlyAcceptedSolution = newSolution;
                if (PickupAndDelivery.calculateCost(currentlyAcceptedSolution)
                        < PickupAndDelivery.calculateCost(bestSolution)) {
        //            System.out.println("operatorChoice = " + operatorChoice);
                    bestSolution = currentlyAcceptedSolution;
                }
            } else if (newSolutionIsFeasible
                    && (RANDOM.nextDouble() < Math.exp(-deltaE / temperature))) {
                currentlyAcceptedSolution = newSolution;
            }
            temperature = coolingFactor * temperature;
       //     System.out.println("temperature = " + temperature);
        }
     /*   System.out.println("min = " + min);
        System.out.println("max = " + max);*/
        return bestSolution;
    }

    private static IVectorSolutionRepresentation<Integer> applySelectedOperatorOnSolution(
            double probabilityOfUsingTwoExchange, double probabilityOfUsingThreeExchange,
            IVectorSolutionRepresentation<Integer> currentlyAcceptedSolution, double operatorChoice) {
        IVectorSolutionRepresentation<Integer> newSolution;
        if (operatorChoice < probabilityOfUsingTwoExchange) {
            newSolution = TwoExchange.useTwoExchangeOnSolution(currentlyAcceptedSolution);
        } else if (operatorChoice < (probabilityOfUsingTwoExchange + probabilityOfUsingThreeExchange)) {
            newSolution = ThreeExchange.useThreeExchangeOnSolution(currentlyAcceptedSolution);
        } else {
            newSolution = OneReinsert.useOneReinsertOnSolution(currentlyAcceptedSolution);
        }
        return newSolution;
    }
}
