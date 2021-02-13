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
        //    System.out.println("iteration number:" + i);
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
        List<Integer> randomSolutionCandidate = new ArrayList<>(exampleSolution.solutionSize());
        List<Integer> elements = new ArrayList<>(exampleSolution.getSolutionRepresentation());
        Collections.shuffle(elements);
        createRandomSolutionCandidate(randomSolutionCandidate, elements);
        makeSolutionCandidateValid(randomSolutionCandidate, elements);
        return new VectorSolutionRepresentation<>(randomSolutionCandidate);
    }

    private void makeSolutionCandidateValid(List<Integer> randomSolutionCandidate, List<Integer> elements) {
        List<Integer> unfinishedCalls = getUnfinishedCallsFromSolutionCandidate(randomSolutionCandidate, elements);
        elements.addAll(unfinishedCalls);
        randomSolutionCandidate.removeAll(elements); // First remove illegal calls from the solution,
        randomSolutionCandidate.addAll(elements); // then add them as outsourced calls.
    }

    private void createRandomSolutionCandidate(List<Integer> randomSolutionCandidate, List<Integer> elements) {
        while (!elements.isEmpty()) {
            int randomIndexNumber = (int) (elements.size() * Math.random());
            Integer element = elements.get(randomIndexNumber);
            randomSolutionCandidate.add(element);
            elements.remove(randomIndexNumber);
        }
    }

    private List<Integer> getUnfinishedCallsFromSolutionCandidate(List<Integer> randomSolutionCandidate, List<Integer> elements) {
        List<Integer> unfinishedCalls = new ArrayList<>(randomSolutionCandidate.size());
        for (Integer element : randomSolutionCandidate) {
            if (element == 0) {
                if (!unfinishedCalls.isEmpty()) {
                    elements.addAll(unfinishedCalls);
                    unfinishedCalls.clear();
                }
            } else {
                if (unfinishedCalls.contains(element)) {
                    unfinishedCalls.remove(element);
                } else {
                    unfinishedCalls.add(element);
                }
            }
        }
        return unfinishedCalls;
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
