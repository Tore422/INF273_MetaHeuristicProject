package pickup.and.delivery.operators;

import solution.representations.vector.IVectorSolutionRepresentation;
import solution.representations.vector.VectorSolutionRepresentation;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class TwoExchange {

    private static final Random RANDOM = new Random();


    public static void main(String[] args) {
        List<Integer> values = Arrays.asList(7, 7, 5, 5, 0, 0, 0, 6, 6);
        IVectorSolutionRepresentation<Integer> sol = new VectorSolutionRepresentation<>(values);
        useTwoExchangeOnSolution(sol);

        /*

        List<Integer> solution = sol.getSolutionRepresentation();
        System.out.println("initial solution = " + solution);
        List<Integer> zeroes = getIndicesOfAllZeroes(sol.getSolutionRepresentation());
        System.out.println("zeroes = " + zeroes);

        int[] startAndStopIndexOfVehicleA = new int[2];
        int[] startAndStopIndexOfVehicleB = new int[2];
        findStartAndStopIndexOfVehicle(zeroes, 3, startAndStopIndexOfVehicleA);
        findStartAndStopIndexOfVehicle(zeroes, 7, startAndStopIndexOfVehicleB);
        if (startAndStopIndexOfVehicleA[1] == -1) {
            startAndStopIndexOfVehicleA[1] = sol.getSolutionSize();
        }
        if (startAndStopIndexOfVehicleB[1] == -1) {
            startAndStopIndexOfVehicleB[1] = sol.getSolutionSize();
        }
        System.out.println("startAndStopIndexOfVehicleA = " + Arrays.toString(startAndStopIndexOfVehicleA));
        System.out.println("startAndStopIndexOfVehicleB = " + Arrays.toString(startAndStopIndexOfVehicleB));

        int firstIndexOfCallA, secondIndexOfCallA;
        int firstIndexOfCallB, secondIndexOfCallB;

        firstIndexOfCallA = 3;
        secondIndexOfCallA = findIndexOfSecondCallInVehicle(sol.getSolutionRepresentation(),
                startAndStopIndexOfVehicleA[0], firstIndexOfCallA);
        System.out.println("firstIndexOfCallA = " + firstIndexOfCallA);
        System.out.println("secondIndexOfCallA = " + secondIndexOfCallA);

        firstIndexOfCallB = 7;
        secondIndexOfCallB = findIndexOfSecondCallInVehicle(sol.getSolutionRepresentation(),
                startAndStopIndexOfVehicleB[0], firstIndexOfCallB);
        System.out.println("firstIndexOfCallB = " + firstIndexOfCallB);
        System.out.println("secondIndexOfCallB = " + secondIndexOfCallB);

        sol.swapElements(firstIndexOfCallA, firstIndexOfCallB);
        sol.swapElements(secondIndexOfCallA, secondIndexOfCallB);
        System.out.println("sol = " + sol);
*/

// Start of random insert testing
/*
        List<Integer> otherRandomIndex = new ArrayList<>();
        int firstRandomIndexInVehicleA = findRandomIndexWithinVehicle(startAndStopIndexOfVehicleA[0], startAndStopIndexOfVehicleA[1], otherRandomIndex);
        otherRandomIndex.add(firstRandomIndexInVehicleA);
        int secondRandomIndexInVehicleA = findRandomIndexWithinVehicle(startAndStopIndexOfVehicleA[0], startAndStopIndexOfVehicleA[1], otherRandomIndex);

        otherRandomIndex.clear();
        int firstRandomIndexInVehicleB = findRandomIndexWithinVehicle(startAndStopIndexOfVehicleB[0], startAndStopIndexOfVehicleB[1], otherRandomIndex);
        otherRandomIndex.add(firstRandomIndexInVehicleB);
        int secondRandomIndexInVehicleB = findRandomIndexWithinVehicle(startAndStopIndexOfVehicleB[0], startAndStopIndexOfVehicleB[1], otherRandomIndex);
        System.out.println();
        System.out.println("firstRandomIndexInVehicleA = " + firstRandomIndexInVehicleA);
        System.out.println("secondRandomIndexInVehicleA = " + secondRandomIndexInVehicleA);
        System.out.println("firstRandomIndexInVehicleB = " + firstRandomIndexInVehicleB);
        System.out.println("secondRandomIndexInVehicleB = " + secondRandomIndexInVehicleB);


        Integer firstCallFromVehicleA = solution.get(firstIndexOfCallA);
        Integer secondCallFromVehicleA = solution.get(secondIndexOfCallA);
        Integer firstCallFromVehicleB = solution.get(firstIndexOfCallB);
        Integer secondCallFromVehicleB = solution.get(secondIndexOfCallB);

        System.out.println();
        System.out.println("firstCallFromVehicleA = " + firstCallFromVehicleA);
        System.out.println("secondCallFromVehicleA = " + secondCallFromVehicleA);
        System.out.println("firstCallFromVehicleB = " + firstCallFromVehicleB);
        System.out.println("secondCallFromVehicleB = " + secondCallFromVehicleB);
        */

/*
        System.out.println("solution = " + solution);
        System.out.println("Adding " + firstCallFromVehicleB + " to index " + firstRandomIndexInVehicleA);
        if (firstRandomIndexInVehicleA == solution.size() - 1) {
            solution.add(firstCallFromVehicleB); // Append to avoid moving last element one back
        } else {
            solution.add(firstRandomIndexInVehicleA, firstCallFromVehicleB);
        }

        if (firstRandomIndexInVehicleA <= secondRandomIndexInVehicleA) {
            secondRandomIndexInVehicleA++;
        }
        if (firstRandomIndexInVehicleA <= firstRandomIndexInVehicleB) {
            firstRandomIndexInVehicleB++;
        }
        if (firstRandomIndexInVehicleA <= secondRandomIndexInVehicleB) {
            secondRandomIndexInVehicleB++;
        }
        if (firstRandomIndexInVehicleA <= firstIndexOfCallA) {
            firstIndexOfCallA++;
        }
        if (firstRandomIndexInVehicleA <= secondIndexOfCallA) {
            secondIndexOfCallA++;
        }
        if (firstRandomIndexInVehicleA <= firstIndexOfCallB) {
            firstIndexOfCallB++;
        }
        if (firstRandomIndexInVehicleA <= secondIndexOfCallB) {
            secondIndexOfCallB++;
        }

        System.out.println("Removing " + solution.remove(firstIndexOfCallB) + " from index " + firstIndexOfCallB);

        if (firstIndexOfCallB <= firstRandomIndexInVehicleB) {
            firstRandomIndexInVehicleB--;
        }
        if (firstIndexOfCallB <= secondRandomIndexInVehicleB) {
            secondRandomIndexInVehicleB--;
        }
        if (firstIndexOfCallB <= firstIndexOfCallA) {
            firstIndexOfCallA--;
        }
        if (firstIndexOfCallB <= secondIndexOfCallA) {
            secondIndexOfCallA--;
        }
        if (firstIndexOfCallB <= secondIndexOfCallB) {
            secondIndexOfCallB--;
        }
*/



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







        System.out.println();
        System.out.println("solution = " + solution);
        System.out.println("Adding " + secondCallFromVehicleB + " to index " + secondRandomIndexInVehicleA);
        if (secondRandomIndexInVehicleA == solution.size() - 1) {
            solution.add(secondCallFromVehicleB); // Append to avoid moving last element one back
        } else {
            solution.add(secondRandomIndexInVehicleA, secondCallFromVehicleB);
        }

        if (secondRandomIndexInVehicleA <= firstRandomIndexInVehicleB) {
            firstRandomIndexInVehicleB++;
        }
        if (secondRandomIndexInVehicleA <= secondRandomIndexInVehicleB) {
            secondRandomIndexInVehicleB++;
        }
        if (secondRandomIndexInVehicleA <= firstIndexOfCallA) {
            firstIndexOfCallA++;
        }
        if (secondRandomIndexInVehicleA <= secondIndexOfCallA) {
            secondIndexOfCallA++;
        }
        if (secondRandomIndexInVehicleA <= secondIndexOfCallB) {
            secondIndexOfCallB++;
        }

        System.out.println("Removing " + solution.remove(secondIndexOfCallB) + " from index " + secondIndexOfCallB);

        if (secondIndexOfCallB <= firstRandomIndexInVehicleB) {
            firstRandomIndexInVehicleB--;
        }
        if (secondIndexOfCallB <= secondRandomIndexInVehicleB) {
            secondRandomIndexInVehicleB--;
        }
        if (secondIndexOfCallB <= firstIndexOfCallA) {
            firstIndexOfCallA--;
        }
        if (secondIndexOfCallB <= secondIndexOfCallA) {
            secondIndexOfCallA--;
        }


        System.out.println();
        System.out.println("solution = " + solution);
        System.out.println("Adding " + firstCallFromVehicleA + " to index " + firstRandomIndexInVehicleB);
        if (firstRandomIndexInVehicleB == solution.size() - 1) {
            solution.add(firstCallFromVehicleA); // Append to avoid moving last element one back
        } else {
            solution.add(firstRandomIndexInVehicleB, firstCallFromVehicleA);
        }

        if (secondRandomIndexInVehicleA <= secondRandomIndexInVehicleB) {
            secondRandomIndexInVehicleB++;
        }
        if (secondRandomIndexInVehicleA <= firstIndexOfCallA) {
            firstIndexOfCallA++;
        }
        if (secondRandomIndexInVehicleA <= secondIndexOfCallA) {
            secondIndexOfCallA++;
        }

        System.out.println("Removing " + solution.remove(firstIndexOfCallA) + " from index " + firstIndexOfCallA);

        if (secondIndexOfCallB <= secondRandomIndexInVehicleB) {
            secondRandomIndexInVehicleB--;
        }
        if (secondIndexOfCallB <= secondIndexOfCallA) {
            secondIndexOfCallA--;
        }







        System.out.println();
        System.out.println("solution = " + solution);
        System.out.println("Adding " + secondCallFromVehicleA + " to index " + secondRandomIndexInVehicleB);
        if (secondRandomIndexInVehicleB == solution.size() - 1) {
            solution.add(secondCallFromVehicleA); // Append to avoid moving last element one back
        } else {
            solution.add(secondRandomIndexInVehicleB, secondCallFromVehicleA);
        }

        if (secondRandomIndexInVehicleA <= secondIndexOfCallA) {
            secondIndexOfCallA++;
        }

        System.out.println("Removing " + solution.remove(secondIndexOfCallA) + " from index " + secondIndexOfCallA);




        System.out.println("final solution = " + sol.getSolutionRepresentation());
        //        IVectorSolutionRepresentation<Integer> newSol = useTwoExchangeOnSolution(sol);
        //System.out.println("newSol = " + newSol);
 */
    }

    private static int findRandomIndexWithinVehicle(int lowerBound, int upperBound, List<Integer> exceptions) {
        while (true) {
            int randomIndex = RANDOM.nextInt(upperBound);
            if (randomIndex > lowerBound && !exceptions.contains(randomIndex)) {
                return randomIndex;
            }
        }
    }

    public static IVectorSolutionRepresentation<Integer> useTwoExchangeOnSolution(
            IVectorSolutionRepresentation<Integer> solution) {
        VectorSolutionRepresentation<Integer> newSolution = new VectorSolutionRepresentation<>(
                new ArrayList<>(solution.getSolutionRepresentation()));
        List<Integer> newSolutionRepresentation = newSolution.getSolutionRepresentation();
        List<Integer> zeroIndices = getIndicesOfAllZeroes(newSolutionRepresentation);
        int[] startAndStopIndexOfVehicleA = new int[2];
        int[] startAndStopIndexOfVehicleB = new int[2];
        int firstIndexOfCallA = -1, secondIndexOfCallA = -1;
        int firstIndexOfCallB, secondIndexOfCallB;
        boolean foundFirstCallToSwap = false;
     //   System.out.println("newSolution = " + newSolution);
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
     /*   System.out.println("firstIndexOfCallA = " + firstIndexOfCallA);
        System.out.println("secondIndexOfCallA = " + secondIndexOfCallA);
        System.out.println("firstIndexOfCallB = " + firstIndexOfCallB);
        System.out.println("secondIndexOfCallB = " + secondIndexOfCallB);*/

        newSolution.swapElements(firstIndexOfCallA, firstIndexOfCallB);
        newSolution.swapElements(secondIndexOfCallA, secondIndexOfCallB);
      //  System.out.println("newSolution = " + newSolution);
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

    private static int getSecondIndexOfCall(List<Integer> newSolutionRepresentation,
                                            List<Integer> zeroIndices,
                                            int[] startAndStopIndexOfVehicle,
                                            int firstIndexOfCall) {
        int secondIndexOfCall;
        findStartAndStopIndexOfVehicle(zeroIndices, firstIndexOfCall,
                newSolutionRepresentation.size(), startAndStopIndexOfVehicle);
        secondIndexOfCall = findIndexOfSecondCallInVehicle(
                newSolutionRepresentation, startAndStopIndexOfVehicle[0], firstIndexOfCall);
        return secondIndexOfCall;
    }

    private static int findIndexOfSecondCallInVehicle(List<Integer> solution,
                                                      int startIndexOfVehicle,
                                                      int indexOfCallToFindDuplicateOf) {
        Integer callToFindDuplicateOf = solution.get(indexOfCallToFindDuplicateOf);
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

    private static void findStartAndStopIndexOfVehicle(List<Integer> zeroIndices,
                                                       int indexOfCallInVehicleToFind,
                                                       int stopIndexOfOutsourcedCalls,
                                                       int[] startAndStopIndexOfVehicle) {
        if (indexOfCallInVehicleToFind > stopIndexOfOutsourcedCalls || indexOfCallInVehicleToFind < 0) {
            throw new IllegalArgumentException("Index does not exist");
        }
        int startIndexOfOutsourcedCalls = zeroIndices.get(zeroIndices.size() - 1);
        if (indexOfCallInVehicleToFind > startIndexOfOutsourcedCalls) {
            startAndStopIndexOfVehicle[0] = startIndexOfOutsourcedCalls;
            startAndStopIndexOfVehicle[1] = stopIndexOfOutsourcedCalls;
        } else if (indexOfCallInVehicleToFind < zeroIndices.get(0)) {
            startAndStopIndexOfVehicle[0] = -1;
            startAndStopIndexOfVehicle[1] = zeroIndices.get(0);
        } else {
            for (int i = 0; i < zeroIndices.size(); i++) {
                int zeroIndex = zeroIndices.get(i);
                if (zeroIndex > indexOfCallInVehicleToFind) {
                    startAndStopIndexOfVehicle[0] = zeroIndices.get(i - 1);
                    startAndStopIndexOfVehicle[1] = zeroIndex;
                    break;
                }
            }
        }
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
