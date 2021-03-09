package pickup.and.delivery.operators;

import pickup.and.delivery.PickupAndDelivery;
import pickup.and.delivery.entities.Vehicle;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class OperatorUtilities {

    public static final Random RANDOM = new Random();


    public static void findStartIndicesOfVehiclesThatCanTakeTheCall(
            List<Integer> zeroIndices, Integer firstPartOfCallToReinsert,
            List<Integer> startIndexOfVehiclesThatCanTakeTheCall) {
        for (Vehicle vehicle : PickupAndDelivery.getVehicles()) {
            for (Integer possibleCall : vehicle.getPossibleCalls()) {
                if (possibleCall.equals(firstPartOfCallToReinsert)) {
                    if (vehicle.getIndex() == 1) {
                        startIndexOfVehiclesThatCanTakeTheCall.add(-1);
                    } else {
                        startIndexOfVehiclesThatCanTakeTheCall.add(zeroIndices.get(vehicle.getIndex() - 2));
                    }
                }
            }
        }
        Integer startIndexOfOutsourcedCalls = zeroIndices.get(zeroIndices.size() - 1);
        startIndexOfVehiclesThatCanTakeTheCall.add(startIndexOfOutsourcedCalls);
    }

    public static int getSecondIndexOfCall(List<Integer> newSolutionRepresentation,
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

    public static int findIndexOfSecondCallInVehicle(List<Integer> solution,
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

    public static void findStartAndStopIndexOfVehicle(List<Integer> zeroIndices,
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

    /**
     * Find a random index within the provided lower and upper bounds,
     * which is not in the given list of exceptions.
     * @param lowerBound lower bound (exclusive)
     * @param upperBound upper bound (exclusive)
     * @param exceptions list of indexes to be ignored.
     * @return A random number between the given lower and upper bounds.<br>
     *    Example call:
     * findRandomIndexWithinVehicle(0, 10, [2,4]) => (1,3,5,6,7,8 or 9)
     */
    public static int findRandomIndexWithinVehicle(int lowerBound, int upperBound, List<Integer> exceptions) {
        if (upperBound <= lowerBound + 1) {
            throw new IllegalArgumentException("Given bound does allow values in between");
        }
        int attempts = 0;
        final int MAX_ATTEMPTS = 100000;
        while (attempts++ < MAX_ATTEMPTS) { // Should be able to find a value before MAX_ATTEMPTS, if it is possible
            int randomIndex = RANDOM.nextInt(upperBound);
            if (randomIndex > lowerBound && !exceptions.contains(randomIndex)) {
                return randomIndex;
            }
        }
        throw new IllegalArgumentException("List of exceptions did not permit a value to be found");
    }

    public static List<Integer> getIndicesOfAllZeroes(List<Integer> solutionRepresentation) {
        List<Integer> zeroIndices = new ArrayList<>();
        int index = 0;
        for (Integer element : solutionRepresentation) {
            if (element.equals(0)) {
                zeroIndices.add(index);
            }
            index++;
        }
        return zeroIndices;
    }


    public static List<int[]> findStartIndexOfVehiclesWithMoreThanOneCall(List<Integer> zeroIndices) {
        List<int[]> startIndexOfVehiclesWithMoreThanOneCall = new ArrayList<>(zeroIndices.size());
        System.out.println("zeroIndices = " + zeroIndices);
        if (zeroIndices.isEmpty()) {
            return startIndexOfVehiclesWithMoreThanOneCall;
        }
        int startIndex = 0;
        int stopIndex = zeroIndices.get(0);
            for (int i = 0; i < zeroIndices.size(); i++) {
                if (stopIndex - startIndex > 3) {
                    int[] startAndStopIndexOfVehicle = new int[2];
                    startAndStopIndexOfVehicle[0] = startIndex;
                    startAndStopIndexOfVehicle[1] = stopIndex;
                    startIndexOfVehiclesWithMoreThanOneCall.add(startAndStopIndexOfVehicle);
                }
                if (i + 1 < zeroIndices.size()) {
                    startIndex = zeroIndices.get(i);
                    stopIndex = zeroIndices.get(i + 1);
                }
            }
        for (int[] element : startIndexOfVehiclesWithMoreThanOneCall) {
            System.out.println("element = " + Arrays.toString(element));
        }
        return startIndexOfVehiclesWithMoreThanOneCall;
    }
}
