package assignment2;

import solution.representations.vector.IVectorSolutionRepresentation;
import solution.representations.vector.VectorSolutionRepresentation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class BlindRandomSearch {


    public IVectorSolutionRepresentation<Integer> blindRandomSearch(IVectorSolutionRepresentation<Integer> solution) {
        IVectorSolutionRepresentation<Integer> bestSolution = solution;
        int numberOfIterations = 10000;
        for (int i = 0; i < numberOfIterations; i++) {
            System.out.println("iteration number:" + i);
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
      /*  List<Integer> randomSolutionCandidate = new ArrayList<>(exampleSolution.solutionSize());
        List<Integer> elements = new ArrayList<>(exampleSolution.getSolutionRepresentation());
        Collections.shuffle(elements);
        List<Integer> copyOfElements = new ArrayList<>();*/
        IVectorSolutionRepresentation<Integer> randomSolutionCandidate =
                new VectorSolutionRepresentation<>(exampleSolution.getSolutionRepresentation());
        Collections.shuffle(exampleSolution.getSolutionRepresentation());

        int solutionSize = exampleSolution.solutionSize();
        do {
            int randomIndexA = (int) (solutionSize * Math.random());
            int randomIndexB = (int) (solutionSize * Math.random());
            exampleSolution.swapElements(randomIndexA, randomIndexB);
        } while (!isValidSolution(exampleSolution.getSolutionRepresentation()));
        return exampleSolution;

            /*
            randomSolutionCandidate.clear();
            copyOfElements.addAll(elements);
            while (!copyOfElements.isEmpty()) {
                int randomIndexNumber = (int) (copyOfElements.size() * Math.random());
                Integer element = copyOfElements.get(randomIndexNumber);
                randomSolutionCandidate.add(element);
                copyOfElements.remove(randomIndexNumber);
            }
        }
        return new VectorSolutionRepresentation<>(randomSolutionCandidate);*/
       // return randomSolutionCandidate;
    }

    private boolean isValidSolution(List<Integer> randomSolutionCandidate) {
        if (randomSolutionCandidate.isEmpty()) {
            return false; // Empty is not a valid solution...
        }
        List<Integer> vehicleCalls = new ArrayList<>();
        for (Integer element : randomSolutionCandidate) {
            if (element == 0) {
                if (!vehicleCalls.isEmpty()) {
                    return false; // No vehicle should start a call without finishing it.
                }
            } else {
                if (vehicleCalls.contains(element)) {
                    vehicleCalls.remove(element);
                } else {
                    vehicleCalls.add(element);
                }
            }
        }
        return true;
    }
}
