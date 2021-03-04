package pickup.and.delivery.operators;

import solution.representations.vector.IVectorSolutionRepresentation;
import solution.representations.vector.VectorSolutionRepresentation;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class ThreeExchange {

    private static final Random RANDOM = new Random();

    public static void main(String[] args) {
        List<Integer> values = Arrays.asList(7, 7, 5, 5, 0, 0, 0, 6, 6);
        IVectorSolutionRepresentation<Integer> sol = new VectorSolutionRepresentation<>(values);
        useThreeExchangeOnSolution(sol);
    }

    public static IVectorSolutionRepresentation<Integer> useThreeExchangeOnSolution(
            IVectorSolutionRepresentation<Integer> solution) {
        VectorSolutionRepresentation<Integer> newSolution = new VectorSolutionRepresentation<>(
                new ArrayList<>(solution.getSolutionRepresentation()));
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
    //    System.out.println("newSolution = " + newSolution);
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
    /*    System.out.println("firstIndexOfCallA = " + firstIndexOfCallA);
        System.out.println("secondIndexOfCallA = " + secondIndexOfCallA);
        System.out.println("firstIndexOfCallB = " + firstIndexOfCallB);
        System.out.println("secondIndexOfCallB = " + secondIndexOfCallB);
        System.out.println("firstIndexOfCallC = " + firstIndexOfCallC);
        System.out.println("secondIndexOfCallC = " + secondIndexOfCallC);*/

        newSolution.swapThreeElements(firstIndexOfCallA, firstIndexOfCallB, firstIndexOfCallC);
        newSolution.swapThreeElements(secondIndexOfCallA, secondIndexOfCallB, secondIndexOfCallC);
    //    System.out.println("newSolution = " + newSolution);
        return newSolution;
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
