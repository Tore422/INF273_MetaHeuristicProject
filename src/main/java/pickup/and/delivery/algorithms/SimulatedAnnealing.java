package pickup.and.delivery.algorithms;

import pickup.and.delivery.PickupAndDelivery;
import pickup.and.delivery.operators.OneReinsert;
import pickup.and.delivery.operators.ThreeExchange;
import pickup.and.delivery.operators.TwoExchange;
import solution.representations.vector.IVectorSolutionRepresentation;

public class SimulatedAnnealing {

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
        final int NUMBER_OF_ITERATIONS = 10000;
        for (int i = 0; i < NUMBER_OF_ITERATIONS; i++) {
            double operatorChoice = (Math.random());
            System.out.println("operatorChoice = " + operatorChoice);
            if (operatorChoice < probabilityOfUsingTwoExchange) {
                newSolution = TwoExchange.useTwoExchangeOnSolution(currentlyAcceptedSolution);
            } else if (operatorChoice < (probabilityOfUsingTwoExchange + probabilityOfUsingThreeExchange)) {
                newSolution = ThreeExchange.useThreeExchangeOnSolution(currentlyAcceptedSolution);
            } else {
                newSolution = OneReinsert.useOneReinsertOnSolution(currentlyAcceptedSolution);
            }
            double deltaE = PickupAndDelivery.calculateCost(newSolution)
                    - PickupAndDelivery.calculateCost(currentlyAcceptedSolution);
            boolean newSolutionIsFeasible = PickupAndDelivery.feasible(newSolution);
            if (newSolutionIsFeasible && deltaE < 0) {
                currentlyAcceptedSolution = newSolution;
                if (PickupAndDelivery.calculateCost(currentlyAcceptedSolution)
                        < PickupAndDelivery.calculateCost(bestSolution)) {
                    bestSolution = currentlyAcceptedSolution;
                }
            } else if (newSolutionIsFeasible
                    && (Math.random() < Math.exp(-deltaE / temperature))) {
                currentlyAcceptedSolution = newSolution;
            }
            temperature = coolingFactor * temperature;
        }
        return bestSolution;
    }
}
