package pickup.and.delivery.operators;

import solution.representations.vector.IVectorSolutionRepresentation;
import solution.representations.vector.VectorSolutionRepresentation;

import java.util.Arrays;
import java.util.List;

import static pickup.and.delivery.operators.OperatorUtilities.*;

public class TwoExchange {

    public static void main(String[] args) {
        List<Integer> values = Arrays.asList(7, 7, 5, 5, 0, 0, 0, 6, 6);
        IVectorSolutionRepresentation<Integer> sol = new VectorSolutionRepresentation<>(values);
        useTwoExchangeOnSolution(sol);

/*
[7, 7, 5, 5, 0, 0, 0, 6, 6]
[6, 7, 7, 6, 0, 0, 0, 5, 5]
firstRandomIndexInVehicleA = 0
secondRandomIndexInVehicleA = 3
firstRandomIndexInVehicleB = 8
secondRandomIndexInVehicleB = 7

firstIndexOfCallA = 3
secondIndexOfCallA = 2
firstIndexOfCallB = 7
secondIndexOfCallB = 8

firstCallFromVehicleA = 5
secondCallFromVehicleA = 5
firstCallFromVehicleB = 6
secondCallFromVehicleB = 6

[6, 7, 7, 5, 5, 0, 0, 0, 6, 6]
increment all other indexes >= firstRandomIndexInVehicleA

remove element at index of firstCallB
[6, 7, 7, 5, 5, 0, 0, 0, 6]
decrement all other indexes >= firstCallBIndex


[6, 7, 7, 5, 6, 5, 0, 0, 0, 6]
increment all other indexes >= secondRandomIndexInVehicleA

remove element at index of secondCallB
[6, 7, 7, 5, 6, 5, 0, 0, 0]
decrement all other indexes >= secondCallBIndex


[6, 7, 7, 5, 6, 5, 0, 0, 0, 5]
increment all other indexes >= firstRandomIndexInVehicleB

remove element at index of firstCallA
[6, 7, 7, 5, 6, 0, 0, 0, 5]
decrement all other indexes >= firstCallAIndex




[6, 7, 7, 5, 6, 0, 0, 0, 5, 5]
increment all other indexes >= secondRandomIndexInVehicleB

remove element at index of secondCallA
[6, 7, 7, 6, 0, 0, 0, 5, 5]
decrement all other indexes >= secondCallAIndex
 */
    }

    public static IVectorSolutionRepresentation<Integer> useTwoExchangeOnSolution(
            IVectorSolutionRepresentation<Integer> solution) {
        VectorSolutionRepresentation<Integer> newSolution = new VectorSolutionRepresentation<>(
                solution.getSolutionRepresentation());
        List<Integer> newSolutionRepresentation = newSolution.getSolutionRepresentation();
        List<Integer> zeroIndices = getIndicesOfAllZeroes(newSolutionRepresentation);
        int[] startAndStopIndexOfVehicleA = new int[2];
        int[] startAndStopIndexOfVehicleB = new int[2];
        int firstIndexOfCallA = -1, secondIndexOfCallA = -1;
        int firstIndexOfCallB, secondIndexOfCallB;
        boolean foundFirstCallToSwap = false;
        while (true) {
            int randomIndex = RANDOM.nextInt(newSolutionRepresentation.size());
            Integer element = newSolutionRepresentation.get(randomIndex);
            if (element.equals(0)) {
                continue;
            }
            if (!foundFirstCallToSwap) {
                firstIndexOfCallA = randomIndex;
                secondIndexOfCallA = getSecondIndexOfCall(newSolutionRepresentation,
                        zeroIndices, startAndStopIndexOfVehicleA,firstIndexOfCallA);
                foundFirstCallToSwap = true;
            } else if (randomIndex != firstIndexOfCallA
                    && randomIndex != secondIndexOfCallA) { // Swapping the call with itself would be rather pointless
                firstIndexOfCallB = randomIndex;
                secondIndexOfCallB = getSecondIndexOfCall(newSolutionRepresentation,
                        zeroIndices, startAndStopIndexOfVehicleB, firstIndexOfCallB);
                break;
            }
        }
        newSolution.swapElements(firstIndexOfCallA, firstIndexOfCallB);
        newSolution.swapElements(secondIndexOfCallA, secondIndexOfCallB);
        return newSolution;
        /*
        [3, 3, 0, 7, 1, 7, 1, 0, 0, 4, 5, 6, 4, 6, 2, 5, 2]
        firstIndexA = 5
        secondIndexA = 3
        firstIndexB = 9
        secondIndexB = 12
        indexOfZeroForCallA = 2
        indexOfZeroForCallB = 8

        is indexOfZeroForCall > indexForOutsourcedCalls
            max = sol.size(); // next vehicle equivalent would come after the end of the array.
        else
            max = from vehicle start, find index of next zero.

        forCallA max = 7
        forCallB max = 17

        find random int between 2 and 7 exclusive, so 3, 4, 5 or 6
        find random int between 8 and 17 exclusive, so 9, 10, 11, 12, 13, 14, 15 or 16
        (if first vehicle, need to subtract 1, so we get 0 or 1).

        swap calls A and B
        [3, 3, 0, 4, 1, 4, 1, 0, 0, 7, 5, 6, 7, 6, 2, 5, 2] simple swap.

        then swap A internally in the vehicle
        then swap B internally in the vehicle
        [3, 3, 0, 4, 4, 1, 1, 0, 0, 2, 5, 6, 6, 7, 7, 5, 2] random swap within vehicle
         */
    }
}
