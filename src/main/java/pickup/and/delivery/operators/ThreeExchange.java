package pickup.and.delivery.operators;

import solution.representations.vector.IVectorSolutionRepresentation;
import solution.representations.vector.VectorSolutionRepresentation;

import java.util.Arrays;
import java.util.List;

import static pickup.and.delivery.operators.OperatorUtilities.*;

public class ThreeExchange {

    public static void main(String[] args) {
        List<Integer> values = Arrays.asList(7, 7, 5, 5, 0, 0, 0, 6, 6);
        IVectorSolutionRepresentation<Integer> sol = new VectorSolutionRepresentation<>(values);
        useThreeExchangeOnSolution(sol);
    }

    public static IVectorSolutionRepresentation<Integer> useThreeExchangeOnSolution(
            IVectorSolutionRepresentation<Integer> solution) {
        VectorSolutionRepresentation<Integer> newSolution = new VectorSolutionRepresentation<>(
                solution.getSolutionRepresentation());
        List<Integer> newSolutionRepresentation = newSolution.getSolutionRepresentation();
        List<Integer> zeroIndices = getIndicesOfAllZeroes(newSolutionRepresentation);
        int[] startAndStopIndexOfVehicleA = new int[2];
        int[] startAndStopIndexOfVehicleB = new int[2];
        int[] startAndStopIndexOfVehicleC = new int[2];
        int firstIndexOfCallA = -1, secondIndexOfCallA = -1;
        int firstIndexOfCallB = -1, secondIndexOfCallB = -1;
        int firstIndexOfCallC, secondIndexOfCallC;
        boolean foundFirstCallToSwap = false;
        boolean foundSecondCallToSwap = false;
        while (true) {
            int randomIndex = RANDOM.nextInt(newSolutionRepresentation.size());
            Integer element = newSolutionRepresentation.get(randomIndex);
            if (element.equals(0)) {
                continue;
            }
            if (!foundFirstCallToSwap) {
                firstIndexOfCallA = randomIndex;
                secondIndexOfCallA = getSecondIndexOfCall(newSolutionRepresentation,
                        zeroIndices, startAndStopIndexOfVehicleA, firstIndexOfCallA);
                foundFirstCallToSwap = true;
            } else if (!foundSecondCallToSwap
                    && randomIndex != firstIndexOfCallA
                    && randomIndex != secondIndexOfCallA) { // Swapping the call with itself would be rather pointless
                firstIndexOfCallB = randomIndex;
                secondIndexOfCallB = getSecondIndexOfCall(newSolutionRepresentation,
                        zeroIndices, startAndStopIndexOfVehicleB, firstIndexOfCallB);
                foundSecondCallToSwap = true;
            } else if (randomIndex != firstIndexOfCallA
                    && randomIndex != secondIndexOfCallA
                    && randomIndex != firstIndexOfCallB
                    && randomIndex != secondIndexOfCallB) { // Swapping the call with itself would be rather pointless
                firstIndexOfCallC = randomIndex;
                secondIndexOfCallC = getSecondIndexOfCall(newSolutionRepresentation,
                        zeroIndices, startAndStopIndexOfVehicleC, firstIndexOfCallC);
                break;
            }
        }

        newSolution.swapThreeElements(firstIndexOfCallA, firstIndexOfCallB, firstIndexOfCallC);
        newSolution.swapThreeElements(secondIndexOfCallA, secondIndexOfCallB, secondIndexOfCallC);
        return newSolution;
    }

}
