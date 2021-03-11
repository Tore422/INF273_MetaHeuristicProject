package pickup.and.delivery.operators;

import pickup.and.delivery.entities.Call;
import pickup.and.delivery.entities.NodeTimesAndCosts;
import pickup.and.delivery.entities.Vehicle;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import static pickup.and.delivery.PickupAndDelivery.*;

public class OperatorUtilities {

    public static final Random RANDOM = new Random();


    public static void findStartIndicesOfVehiclesThatCanTakeTheCall(
            List<Integer> zeroIndices, Integer firstPartOfCallToReinsert,
            List<Integer> startIndexOfVehiclesThatCanTakeTheCall) {
        for (Vehicle vehicle : getVehicles()) {
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
     * @param exceptions list of indexes to be ignored (can be null).
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
            if (randomIndex > lowerBound
                    && (exceptions == null || !exceptions.contains(randomIndex))) {
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

    /**
     * Returns a list with start and stop indices of vehicles
     * handling two or more calls.<br>
     * Does not include vehicle for outsourced calls.
     *
     * @param zeroIndices
     * @return
     */
    public static List<int[]> findVehiclesWithMoreThanOneCall(List<Integer> zeroIndices) {
        List<int[]> startAndStopIndicesOfVehiclesWithMoreThanOneCall = new ArrayList<>(zeroIndices.size());
        System.out.println("zeroIndices = " + zeroIndices);
        if (zeroIndices.isEmpty()) {
            return startAndStopIndicesOfVehiclesWithMoreThanOneCall;
        }
        int startIndex = -1;
        int stopIndex = zeroIndices.get(0);
            for (int i = 0; i < zeroIndices.size(); i++) {
                if (stopIndex - startIndex > 3) {
                    int[] startAndStopIndexOfVehicle = new int[2];
                    startAndStopIndexOfVehicle[0] = startIndex;
                    startAndStopIndexOfVehicle[1] = stopIndex;
                    startAndStopIndicesOfVehiclesWithMoreThanOneCall.add(startAndStopIndexOfVehicle);
                }
                if (i + 1 < zeroIndices.size()) {
                    startIndex = zeroIndices.get(i);
                    stopIndex = zeroIndices.get(i + 1);
                }
            }
        for (int[] element : startAndStopIndicesOfVehiclesWithMoreThanOneCall) {
            System.out.println("element = " + Arrays.toString(element));
        }
        return startAndStopIndicesOfVehiclesWithMoreThanOneCall;
    }

    /* Assumes valid solution */
    public static int computeCostForVehicle(int startIndex, int stopIndex, int vehicleNumber,
                                     List<Integer> solutionRepresentation) {
        int totalCost = 0;
        int currentNode = getVehicles().get(vehicleNumber - 1).getHomeNode();
        List<Call> unfinishedCalls = new ArrayList<>();
        for (int i = startIndex + 1; i < stopIndex; i++) {
            System.out.println("element = " + solutionRepresentation.get(i));
            Call currentCall = getCalls().get(solutionRepresentation.get(i));
            NodeTimesAndCosts nodeTimesAndCosts = getNodeTimesAndCostsForVehicle(
                    vehicleNumber, currentCall.getCallIndex());
            int destinationNode = - 1;
            if (unfinishedCalls.contains(currentCall)) {
                destinationNode = currentCall.getDestinationNode();
                totalCost += nodeTimesAndCosts.getDestinationNodeCosts();
                unfinishedCalls.remove(currentCall);
            } else {
                destinationNode = currentCall.getOriginNode();
                totalCost += nodeTimesAndCosts.getOriginNodeCosts();
                unfinishedCalls.add(currentCall);
            }
            totalCost += getTravelCostForJourney(currentNode, destinationNode, vehicleNumber);
            currentNode = destinationNode;
        }
        return totalCost;
    }

/*  public static boolean computeIfTimeWindowHasBeenViolated() {

        return false;
    }
*/

/*  public static boolean computeIfCapacityHasBeenExceeded() {

    }
*/

    public static int findVehicleNumberForVehicleStartingAtIndex(int startIndex, List<Integer> zeroIndices) {
        int vehicleNumber = 1;
        if (startIndex == -1) {
            return vehicleNumber;
        }
        for (int i = 0; i < zeroIndices.size() - 1; i++) {
            vehicleNumber++;
            int zeroIndex = zeroIndices.get(i);
            if (zeroIndex == startIndex) {
                return vehicleNumber;
            }
        }
        if (zeroIndices.get(zeroIndices.size() - 1).equals(startIndex)) {
            throw new IllegalArgumentException("Start index matches vehicle for outsourced calls");
        } else {
            throw new IllegalArgumentException("Given start index does not match for any vehicle");
        }
    }


}
