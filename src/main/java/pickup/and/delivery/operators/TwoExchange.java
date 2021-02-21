package pickup.and.delivery.operators;

import solution.representations.vector.IVectorSolutionRepresentation;
import solution.representations.vector.VectorSolutionRepresentation;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class TwoExchange {

    public static IVectorSolutionRepresentation<Integer> useTwoExchangeOnSolution(IVectorSolutionRepresentation<Integer> solution) {
        List<Integer> sol = new ArrayList<>(solution.getSolutionRepresentation());
        List<Integer> zeroIndices = getIndicesOfAllZeroes(sol);
        int startIndexOfOutsourcedCalls = zeroIndices.get(zeroIndices.size() - 1);
        int firstIndexOfCallA, secondIndexOfCallA;
        int firstIndexOfCallB, secondIndexOfCallB;
        int startIndexOfVehicleA, startIndexOfVehicleB;
        boolean foundFirstCallToSwap = false;
        Random random = new Random();
        while (true) {
            int randomIndex = random.nextInt(sol.size());
            Integer element = sol.get(randomIndex);
            if (element.equals(0)) {
                continue;
            }
            if (!foundFirstCallToSwap) {
                firstIndexOfCallA = randomIndex;
                startIndexOfVehicleA = findStartIndexOfVehicle(zeroIndices,
                        startIndexOfOutsourcedCalls, firstIndexOfCallA);
                secondIndexOfCallA = findIndexOfSecondCallInVehicle(sol, startIndexOfVehicleA, firstIndexOfCallA);
                foundFirstCallToSwap = true;
            } else {
                firstIndexOfCallB = randomIndex;
                startIndexOfVehicleB = findStartIndexOfVehicle(zeroIndices,
                        startIndexOfOutsourcedCalls, firstIndexOfCallB);
                secondIndexOfCallB = findIndexOfSecondCallInVehicle(sol, startIndexOfVehicleB, firstIndexOfCallB);
                break;
            }
        }
        VectorSolutionRepresentation<Integer> newSolution = new VectorSolutionRepresentation<>(sol);
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
       // newSolution.swapElements(firstIndexOfCallA, firstIndexOfCallB);
       // newSolution.swapElements(secondIndexOfCallA, secondIndexOfCallB);




        return newSolution;
    }

    private static int findIndexOfSecondCallInVehicle(List<Integer> solution,
                                                      int startIndexOfVehicle, int indexOfCallToFindDuplicateOf) {
        Integer callToFindDuplicateOf = solution.get(indexOfCallToFindDuplicateOf);
        if (startIndexOfVehicle == 0) { // If the call is handled by the first vehicle,
            startIndexOfVehicle--; // we adjust the value to fit with the following for loop.
        }
        for (int i = startIndexOfVehicle + 1; i < solution.size(); i++) {
            Integer call = solution.get(i);
            if (call.equals(0)) {
                break;
            }
            if (call.equals(callToFindDuplicateOf) && i != indexOfCallToFindDuplicateOf) {
                return i;
            }
        }
        throw new IllegalArgumentException("Solution contains unfinished calls." +
                "\nGiven solution is not valid.");
    }

    private static int findStartIndexOfVehicle(List<Integer> zeroIndices, int startIndexOfOutsourcedCalls, int indexOfCallToSwap) {
        int startIndexOfVehicle = -1;
        if (indexOfCallToSwap > startIndexOfOutsourcedCalls) {
            startIndexOfVehicle = startIndexOfOutsourcedCalls;
        } else if (indexOfCallToSwap < zeroIndices.get(0)) {
            startIndexOfVehicle = 0;
        } else {
            for (int i = 0; i < zeroIndices.size(); i++) {
                int zeroIndex = zeroIndices.get(i);
                if (zeroIndex > indexOfCallToSwap) {
                    startIndexOfVehicle = zeroIndices.get(i - 1);
                    break;
                }
            }
        }
        return startIndexOfVehicle;
        /*
        element is in index 16
        zeroes are in indices [2, 10, 14, 15, 20]
        is 16 < 2
        is 16 < 10
        is 16 < 14
        is 16 < 15
        is 16 < 20
            startIndexOfVehicle = 15;
         */
    }

    private static List<Integer> getIndicesOfAllZeroes(List<Integer> sol) {
        List<Integer> zeroIndices = new ArrayList<>();
        int index = 0;
        for (Integer element : sol) {
            if (element.equals(0)) {
                zeroIndices.add(index);
            }
            index++;
        }
        return zeroIndices;
    }
}
