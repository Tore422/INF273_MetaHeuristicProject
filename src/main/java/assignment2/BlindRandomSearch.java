package assignment2;

import solution.representations.vector.IVectorSolutionRepresentation;
import solution.representations.vector.VectorSolutionRepresentation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class BlindRandomSearch {


    public IVectorSolutionRepresentation<Integer> blindRandomSearch(IVectorSolutionRepresentation<Integer> solution) {
        IVectorSolutionRepresentation<Integer> bestSolution = solution;
        final int NUMBER_OF_ITERATIONS = 10000;
        for (int i = 0; i < NUMBER_OF_ITERATIONS; i++) {
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
}
