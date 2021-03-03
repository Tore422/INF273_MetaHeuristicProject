package pickup.and.delivery.operators;

import solution.representations.vector.IVectorSolutionRepresentation;
import solution.representations.vector.VectorSolutionRepresentation;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class OneReinsert {

    private static final Random RANDOM = new Random();

    public static void main(String[] args) {
        List<Integer> values = Arrays.asList(7, 7, 5, 5, 0, 0, 0, 6, 6);
      //  List<Integer> values = Arrays.asList(7, 7, 5, 5, 0, 2, 2, 0, 3, 4, 4, 3, 1, 1, 0, 6, 6);
        IVectorSolutionRepresentation<Integer> sol = new VectorSolutionRepresentation<>(values);
        useOneReinsertOnSolution(sol);
    }


    public static IVectorSolutionRepresentation<Integer> useOneReinsertOnSolution(IVectorSolutionRepresentation<Integer> solution) {
        if (solution == null || solution.getSolutionRepresentation().isEmpty()) {
            throw new IllegalArgumentException("Solution was empty/nonexistent.");
        }
        VectorSolutionRepresentation<Integer> newSolution = new VectorSolutionRepresentation<>(
                new ArrayList<>(solution.getSolutionRepresentation()));
        List<Integer> newSolutionRepresentation = newSolution.getSolutionRepresentation();
        List<Integer> zeroIndices = getIndicesOfAllZeroes(newSolutionRepresentation);
        int[] startAndStopIndexOfVehicle = new int[2];
        int firstIndexOfCall = -1, secondIndexOfCall = -1;
        Integer firstPartOfCallToReinsert = -1, secondPartOfCallToReinsert = -1;
     //   System.out.println("newSolutionRepresentation = " + newSolutionRepresentation);
        while (true) {
            int randomIndex = RANDOM.nextInt(newSolutionRepresentation.size());
            Integer element = newSolutionRepresentation.get(randomIndex);
            if (element.equals(0)) {
                continue;
            }
            firstIndexOfCall = randomIndex;
            secondIndexOfCall = getSecondIndexOfCall(
                    newSolutionRepresentation, zeroIndices, startAndStopIndexOfVehicle, firstIndexOfCall);
       //     System.out.println("firstIndexOfCall = " + firstIndexOfCall);
        //    System.out.println("secondIndexOfCall = " + secondIndexOfCall);
            firstPartOfCallToReinsert = newSolutionRepresentation.remove(firstIndexOfCall);
            if (secondIndexOfCall > firstIndexOfCall) {
                secondIndexOfCall--;
            }
            secondPartOfCallToReinsert = newSolutionRepresentation.remove(secondIndexOfCall);
            break;
        }
       /* System.out.println("solution = " + solution);
        System.out.println("zeroIndices old = " + zeroIndices);
        zeroIndices = getIndicesOfAllZeroes(newSolutionRepresentation);
        System.out.println("zeroIndices new = " + zeroIndices);
        System.out.println("firstPartOfCallToReinsert = " + firstPartOfCallToReinsert);
        List<Integer> vehiclesThatCanTakeTheCall = new ArrayList<>();
        for (Vehicle vehicle : PickupAndDelivery.getVehicles()) {
            for (Integer possibleCall : vehicle.getPossibleCalls()) {
                if (possibleCall.equals(firstPartOfCallToReinsert)) {
                    vehiclesThatCanTakeTheCall.add(vehicle.getIndex());
                }
            }
        }
        System.out.println("vehiclesThatCanTakeTheCall = " + vehiclesThatCanTakeTheCall);
        List<Integer> startIndexOfVehiclesThatCanTakeTheCall = new ArrayList<>();
        for (Integer vehicle : vehiclesThatCanTakeTheCall) {
            startIndexOfVehiclesThatCanTakeTheCall.add(zeroIndices.get(vehicle - 1));
        }
        System.out.println("startIndexOfVehiclesThatCanTakeTheCall = " + startIndexOfVehiclesThatCanTakeTheCall);
*/





      //  System.out.println("zeroIndices old = " + zeroIndices);
        zeroIndices = getIndicesOfAllZeroes(newSolutionRepresentation);
        int randomVehicle = RANDOM.nextInt(zeroIndices.size());
     //   System.out.println("zeroIndices new = " + zeroIndices);
     //   System.out.println("randomVehicle = " + randomVehicle);
     //   System.out.println("newSolutionRepresentation = " + newSolutionRepresentation);
        int startIndexOfVehicle, stopIndexOfVehicle;
        if (randomVehicle == 0) {
            startIndexOfVehicle = -1;
            stopIndexOfVehicle = zeroIndices.get(0);
        } else {
            startIndexOfVehicle = zeroIndices.get(randomVehicle);
            if (startIndexOfVehicle == newSolutionRepresentation.size() - 1) {
                // Since there are no other outsourced calls, and the order of the
                // insertion is irrelevant, we can simply append the call to the end of the solution.
                newSolutionRepresentation.add(firstPartOfCallToReinsert);
                newSolutionRepresentation.add(secondPartOfCallToReinsert);
       //         System.out.println("startIndexOfVehicle = " + startIndexOfVehicle);
        //        System.out.println("newSolutionRepresentation add to end = " + newSolutionRepresentation);
                return newSolution;
            } else if (randomVehicle == zeroIndices.size() - 1) {
                stopIndexOfVehicle = newSolutionRepresentation.size();
        //        System.out.println("startIndexOfVehicle = " + startIndexOfVehicle);
        //        System.out.println("stopIndexOfVehicle = " + stopIndexOfVehicle);
                List<Integer> excludedIndexes = new ArrayList<>();
                int randomIndexOne = findRandomIndexWithinVehicle(
                        startIndexOfVehicle, stopIndexOfVehicle, excludedIndexes);
                if (randomIndexOne > newSolutionRepresentation.size()) {
                    newSolutionRepresentation.add(firstPartOfCallToReinsert);
                } else {
                    newSolutionRepresentation.add(randomIndexOne, firstPartOfCallToReinsert);
                }
        //        System.out.println("newSolutionRepresentation = " + newSolutionRepresentation);
                int randomIndexTwo = findRandomIndexWithinVehicle(
                        startIndexOfVehicle, stopIndexOfVehicle + 1, excludedIndexes);
                if (randomIndexTwo > newSolutionRepresentation.size()) {
                    newSolutionRepresentation.add(secondPartOfCallToReinsert);
                } else {
                    newSolutionRepresentation.add(randomIndexTwo, secondPartOfCallToReinsert);
                }
       //         System.out.println("newSolutionRepresentation = " + newSolutionRepresentation);
                return newSolution;
            } else {
                stopIndexOfVehicle = zeroIndices.get(randomVehicle + 1);
            }
        }
        if (stopIndexOfVehicle == startIndexOfVehicle + 1) {
            // Since the vehicle is empty, and the order of insertion is irrelevant,
            // we can simply add the call to the vehicle.
        //    System.out.println("startIndexOfVehicle = " + startIndexOfVehicle);
        //    System.out.println("stopIndexOfVehicle = " + stopIndexOfVehicle);
        //    System.out.println("vehicle was empty");
            newSolutionRepresentation.add(stopIndexOfVehicle, firstPartOfCallToReinsert);
            newSolutionRepresentation.add(stopIndexOfVehicle, secondPartOfCallToReinsert);
       //     System.out.println("newSolutionRepresentation = " + newSolutionRepresentation);
            return newSolution;
        }
     //   System.out.println("startIndexOfVehicle = " + startIndexOfVehicle);
      //  System.out.println("stopIndexOfVehicle = " + stopIndexOfVehicle);
      //  System.out.println("newSolutionRepresentation = " + newSolutionRepresentation);
        List<Integer> otherIndex = new ArrayList<>();
        int randomIndexOne = findRandomIndexWithinVehicle(startIndexOfVehicle, stopIndexOfVehicle + 1, otherIndex);
     //   System.out.println("randomIndexOne = " + randomIndexOne);
        newSolutionRepresentation.add(randomIndexOne, firstPartOfCallToReinsert);
     //   System.out.println("newSolutionRepresentation = " + newSolutionRepresentation);
        int randomIndexTwo = findRandomIndexWithinVehicle(startIndexOfVehicle, stopIndexOfVehicle + 2, otherIndex);
     //   System.out.println("randomIndexTwo = " + randomIndexTwo);
        newSolutionRepresentation.add(randomIndexTwo, secondPartOfCallToReinsert);
     //   System.out.println("newSolutionRepresentation = " + newSolutionRepresentation);
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
    private static int findRandomIndexWithinVehicle(int lowerBound, int upperBound, List<Integer> exceptions) {
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
