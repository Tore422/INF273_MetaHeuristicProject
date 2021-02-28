package pickup.and.delivery.algorithms;

import pickup.and.delivery.PickupAndDelivery;
import solution.representations.vector.IVectorSolutionRepresentation;
import solution.representations.vector.VectorSolutionRepresentation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class BlindRandomSearch {

    private static final Random RANDOM = new Random();
    private static final int NUMBER_OF_ITERATIONS = 10000;

    public static IVectorSolutionRepresentation<Integer> blindRandomSearch(IVectorSolutionRepresentation<Integer> initialSolution) {
        IVectorSolutionRepresentation<Integer> bestSolution = initialSolution;
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

    /**
     * Create a valid, random solution representation based on
     * a given example solution.
     *
     * @param exampleSolution The solution to create a permutation from.
     * @return A valid permutation of the given solution.
     */
    public static IVectorSolutionRepresentation<Integer> createRandomSolution(IVectorSolutionRepresentation<Integer> exampleSolution) {
        List<Integer> randomSolution = new ArrayList<>(exampleSolution.getSolutionRepresentation());
        Collections.shuffle(randomSolution);
        do {
            tryToCreateARandomValidSolution(randomSolution);
        } while (!PickupAndDelivery.isValidSolution(new VectorSolutionRepresentation<>(randomSolution)));
        return new VectorSolutionRepresentation<>(randomSolution);
    }

    private static void tryToCreateARandomValidSolution(List<Integer> randomSolution) {
        List<Integer> elements = new ArrayList<>(randomSolution.size() / 3);
        findUnfinishedCalls(randomSolution, elements);
        randomSolution.removeAll(elements);
        randomlyAddAllUnfinishedCalls(randomSolution, elements);
    }

    private static void findUnfinishedCalls(List<Integer> randomSolution, List<Integer> elements) {
        List<Integer> unfinishedCalls = new ArrayList<>(randomSolution.size() / 3);
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

    private static void randomlyAddAllUnfinishedCalls(List<Integer> randomSolution, List<Integer> elements) {
        Collections.shuffle(elements);
        while (!elements.isEmpty()) {
            Integer element = elements.get(elements.size() - 1);
            elements.remove(elements.size() - 1);
            int randomIndexNumber = RANDOM.nextInt(randomSolution.size());
            randomSolution.add(randomIndexNumber, element);
        }
    }
}
