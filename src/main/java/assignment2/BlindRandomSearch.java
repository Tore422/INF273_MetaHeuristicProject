package assignment2;

import solution.representations.vector.IVectorSolutionRepresentation;
import solution.representations.vector.VectorSolutionRepresentation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class BlindRandomSearch {


    public IVectorSolutionRepresentation<Integer> blindRandomSearch(IVectorSolutionRepresentation<Integer> solution) {
        IVectorSolutionRepresentation<Integer> bestSolution = solution;
        int solutionSize = solution.solutionSize();
        int numberOfIterations = 10000;
        for (int i = 0; i < numberOfIterations; i++) {
            IVectorSolutionRepresentation<Integer> currentSolution = createRandomSolution(bestSolution);
            if (PickupAndDelivery.feasible(currentSolution)
                    && PickupAndDelivery.calculateCost(currentSolution)
                    < PickupAndDelivery.calculateCost(bestSolution)) {
                bestSolution = currentSolution;
            }
        }
        return bestSolution;
    }

    private IVectorSolutionRepresentation<Integer> createRandomSolution(IVectorSolutionRepresentation<Integer> exampleSolution) {
        IVectorSolutionRepresentation<Integer> randomSolution =
                new VectorSolutionRepresentation<>(exampleSolution.solutionSize());
        List<Integer> elements = new ArrayList<>(exampleSolution.getSolutionRepresentation());
        Collections.shuffle(elements);
        int startOfCurrentVehicleIndex = 0;
        int currentIndex = 0;
        List<Integer> vehicleCalls = new ArrayList<>();
        while (!elements.isEmpty()) {
            int randomIndexNumber = (int) (elements.size() * Math.random());
            Integer element = elements.get(randomIndexNumber);
            if (element == 0) {
                for (int i = startOfCurrentVehicleIndex; i < currentIndex; i++) {
                    Integer call = randomSolution.getSolutionRepresentation().get(i);
                    if (call == 0) {
                        continue;
                    } else if (!vehicleCalls.contains(call)) {
                        vehicleCalls.add(call);
                    } else {
                        vehicleCalls.remove((Integer) call);
                    }
                }
                if (vehicleCalls.isEmpty()) {
                    randomSolution.getSolutionRepresentation().add(element);
                    elements.remove(randomIndexNumber);
                    startOfCurrentVehicleIndex = currentIndex++;
                } else {
                    vehicleCalls.clear();
                }
            } else {
                randomSolution.getSolutionRepresentation().add(element);
                elements.remove(randomIndexNumber);
                currentIndex++;
            }
        }
        return randomSolution;
    }


}
