package pickup.and.delivery;

import solution.representations.vector.IVectorSolutionRepresentation;
import solution.representations.vector.VectorSolutionRepresentation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class BlindRandomSearch {


    public static IVectorSolutionRepresentation<Integer> blindRandomSearch(IVectorSolutionRepresentation<Integer> solution) {
        IVectorSolutionRepresentation<Integer> bestSolution = solution;
        final int NUMBER_OF_ITERATIONS = 10000;
        for (int i = 0; i < NUMBER_OF_ITERATIONS; i++) {
         //   System.out.println("iteration number:" + i);
            IVectorSolutionRepresentation<Integer> currentSolution = createRandomSolution(bestSolution);
            if (PickupAndDelivery.feasible(currentSolution)
                    && PickupAndDelivery.calculateCost(currentSolution)
                    < PickupAndDelivery.calculateCost(bestSolution)) {
                bestSolution = currentSolution;
            }
        }
        return bestSolution;
    }

    private static IVectorSolutionRepresentation<Integer> createRandomSolution(IVectorSolutionRepresentation<Integer> exampleSolution) {
        List<Integer> randomSolution = new ArrayList<>(exampleSolution.getSolutionRepresentation());
        Collections.shuffle(randomSolution);
        while (true) {
            tryToCreateARandomValidSolution(randomSolution);
            if (PickupAndDelivery.isValidSolution(new VectorSolutionRepresentation<>(randomSolution))) {
                break;
            }
        }
        return new VectorSolutionRepresentation<>(randomSolution);




        /*
        List<Integer> randomSolution = new ArrayList<>(exampleSolution.getSolutionSize());
        List<Integer> elements = new ArrayList<>(exampleSolution.getSolutionRepresentation());
        int indexOfPreviousZero = -1;
        while (!elements.isEmpty()) {
            int randomIndexNumber = (int) (elements.size() * Math.random());
            Integer element = elements.get(randomIndexNumber);
            elements.remove(randomIndexNumber);
           // System.out.println("element = " + element);
            if (element == 0) {
                List<Integer> unfinishedCalls = new ArrayList<>();
           //     System.out.println("got zero");
           //     System.out.println("randomSolution = " + randomSolution);
                for (int i = indexOfPreviousZero + 1; i < randomSolution.size(); i++) {
                    Integer callId = randomSolution.get(i);
                    if (unfinishedCalls.contains(callId)) {
                        unfinishedCalls.remove(callId);
                    } else {
                        unfinishedCalls.add(callId);
                    }
                }
                Collections.shuffle(unfinishedCalls);
            //    System.out.println("unfinishedCalls = " + unfinishedCalls);
                for (Integer callId : unfinishedCalls) {
                    randomSolution.add(callId);
                    elements.remove(callId);
                }
                randomSolution.add(element);
            //    System.out.println("randomSolution = " + randomSolution);
                indexOfPreviousZero = randomSolution.size() - 1;
            } else {
                randomSolution.add(element);
            }
        }
       // System.out.println("randomSolution = " + randomSolution);
        return new VectorSolutionRepresentation<>(randomSolution);*/


      /*  List<Integer> randomSolutionCandidate = new ArrayList<>(exampleSolution.solutionSize());
        List<Integer> elements = new ArrayList<>(exampleSolution.getSolutionRepresentation());
        Collections.shuffle(elements);
        createRandomSolutionCandidate(randomSolutionCandidate, elements);
        makeSolutionCandidateValid(randomSolutionCandidate, elements);
        return new VectorSolutionRepresentation<>(randomSolutionCandidate);*/
    }

    private static void tryToCreateARandomValidSolution(List<Integer> randomSolution) {
        List<Integer> elements = new ArrayList<>(randomSolution.size() / 3);
        List<Integer> unfinishedCalls = new ArrayList<>(randomSolution.size() / 3);
        findUnfinishedCalls(randomSolution, elements, unfinishedCalls);
        randomSolution.removeAll(elements);
        randomlyAddAllUnfinishedCalls(randomSolution, elements);
    }

    private static void randomlyAddAllUnfinishedCalls(List<Integer> randomSolution, List<Integer> elements) {
        Collections.shuffle(elements);
        while (!elements.isEmpty()) {
            Integer element = elements.get(elements.size() - 1);
            elements.remove(elements.size() - 1);
            int randomIndexNumber = (int) (randomSolution.size() * Math.random());
            randomSolution.add(randomIndexNumber, element);
        }
    }

    private static void findUnfinishedCalls(List<Integer> randomSolution, List<Integer> elements, List<Integer> unfinishedCalls) {
        for (Integer element : randomSolution) {
            if (element == 0) {
                elements.addAll(unfinishedCalls);
                unfinishedCalls.clear();
            } else {
                if (unfinishedCalls.contains(element)) {
                    unfinishedCalls.remove(element);
                } else {
                    unfinishedCalls.add(element);
                }
            }
        }
        elements.addAll(unfinishedCalls); // Add the unfinished calls from those outsourced
    }

    private static void makeSolutionCandidateValid(List<Integer> randomSolutionCandidate, List<Integer> elements) {
        List<Integer> unfinishedCalls = getUnfinishedCallsFromSolutionCandidate(randomSolutionCandidate, elements);
        elements.addAll(unfinishedCalls);
        randomSolutionCandidate.removeAll(elements); // First remove illegal calls from the solution,
        randomSolutionCandidate.addAll(elements); // then add them as outsourced calls.
    }

    private static void createRandomSolutionCandidate(List<Integer> randomSolutionCandidate, List<Integer> elements) {
        while (!elements.isEmpty()) {
            int randomIndexNumber = (int) (elements.size() * Math.random());
            Integer element = elements.get(randomIndexNumber);
            randomSolutionCandidate.add(element);
            elements.remove(randomIndexNumber);
        }
    }

    private static List<Integer> getUnfinishedCallsFromSolutionCandidate(List<Integer> randomSolutionCandidate, List<Integer> elements) {
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
