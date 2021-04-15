package pickup.and.delivery.operators.custom;

import pickup.and.delivery.PickupAndDelivery;
import solution.representations.vector.IVectorSolutionRepresentation;
import solution.representations.vector.VectorSolutionRepresentation;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static pickup.and.delivery.operators.OperatorUtilities.*;

public class ThreeReinsert {

    public static void main(String[] args) {
        List<Integer> values = Arrays.asList(0, 0, 0, 1, 1, 2, 2, 3, 3, 4, 4, 5, 5, 6, 6, 7, 7);
        IVectorSolutionRepresentation<Integer> sol = new VectorSolutionRepresentation<>(values);
        IVectorSolutionRepresentation<Integer> bestSol = useThreeReinsertOnSolution(sol, 3);
        System.out.println("PickupAndDelivery.feasible(bestSol) = " + PickupAndDelivery.feasible(bestSol));
        System.out.println("PickupAndDelivery.calculateCost(sol) = " + PickupAndDelivery.calculateCost(sol));
        System.out.println("bestSol = " + bestSol);
    }
    
    
    

    public static IVectorSolutionRepresentation<Integer> useThreeReinsertOnSolution(
            IVectorSolutionRepresentation<Integer> solution, int numberOfReInsertions) {
        IVectorSolutionRepresentation<Integer> newSolution = new VectorSolutionRepresentation<>(
                solution.getSolutionRepresentation());
        List<Integer> newSolutionRepresentation = newSolution.getSolutionRepresentation();
        List<Integer> zeroIndices = getIndicesOfAllZeroes(newSolutionRepresentation);
        List<Integer> ignoredIndices = new ArrayList<>(zeroIndices);
        int num = 0;
        while (num < numberOfReInsertions && ignoredIndices.size() < newSolutionRepresentation.size()) {
            System.out.println("num = " + num);

            int firstIndexOfCall = findRandomIndexWithinExclusiveBounds(
                    MINUS_ONE, newSolutionRepresentation.size(), ignoredIndices);
            int secondIndexOfCall = getSecondIndexOfCall(newSolutionRepresentation, zeroIndices, firstIndexOfCall);
            int callID = newSolutionRepresentation.get(firstIndexOfCall);
            IVectorSolutionRepresentation<Integer> copyOfNewSolution =
                    new VectorSolutionRepresentation<>(newSolutionRepresentation);
            List<Integer> copyOfNewSolutionRepresentation = copyOfNewSolution.getSolutionRepresentation();
            copyOfNewSolutionRepresentation.remove(firstIndexOfCall);
            if (firstIndexOfCall < secondIndexOfCall) {
                secondIndexOfCall--;
            }
            copyOfNewSolutionRepresentation.remove(secondIndexOfCall);
            List<Integer> zeroIndicesForCopy = getIndicesOfAllZeroes(copyOfNewSolutionRepresentation);
            List<Integer> startIndicesOfVehiclesThatCanTakeTheCall = findStartIndicesOfVehiclesThatCanTakeTheCall(
                    zeroIndicesForCopy, callID);

            if (startIndicesOfVehiclesThatCanTakeTheCall.isEmpty()) {
                continue;
            }
            System.out.println("firstIndexOfCall = " + firstIndexOfCall);
            System.out.println("secondIndexOfCall = " + secondIndexOfCall);
            System.out.println("callID = " + callID);

            System.out.println("startIndicesOfVehiclesThatCanTakeTheCall = " + startIndicesOfVehiclesThatCanTakeTheCall);

            System.out.println("Processing vehicles");
            List<Integer> processedVehicles = new ArrayList<>();
            while (processedVehicles.size() < startIndicesOfVehiclesThatCanTakeTheCall.size()) {
                int randomIndexNotYetSelected = findRandomIndexWithinExclusiveBounds(
                        MINUS_ONE, startIndicesOfVehiclesThatCanTakeTheCall.size(), processedVehicles);
                int startIndexOfSelectedVehicle = startIndicesOfVehiclesThatCanTakeTheCall
                        .get(randomIndexNotYetSelected);
                int stopIndexOfSelectedVehicle = findStopIndex(
                        copyOfNewSolutionRepresentation, zeroIndicesForCopy, startIndexOfSelectedVehicle);
                int[] feasiblePositions;
                if (stopIndexOfSelectedVehicle == copyOfNewSolutionRepresentation.size()) {
                    System.out.println("Adding to end of outsourced");
                    feasiblePositions = insertIntoVehicleForOutsourcedCalls(callID, copyOfNewSolutionRepresentation,
                            startIndexOfSelectedVehicle, stopIndexOfSelectedVehicle);
                } else if ((startIndexOfSelectedVehicle + 1) == stopIndexOfSelectedVehicle) {
                    System.out.println("Adding to empty vehicle");
                    feasiblePositions = insertIntoEmptyVehicle(callID, copyOfNewSolutionRepresentation, stopIndexOfSelectedVehicle);
                } else {
                    System.out.println("Looking for feasible positions");
                    feasiblePositions = lookForFeasiblePositionsInVehicle(callID,
                            copyOfNewSolutionRepresentation, zeroIndicesForCopy,
                            startIndexOfSelectedVehicle, stopIndexOfSelectedVehicle);
                    if (feasiblePositions[0] != -1) { // Need only check one value to see if new positions were found
                        int firstPosition = feasiblePositions[0];
                        int secondPosition = feasiblePositions[1];
                        copyOfNewSolutionRepresentation.add(firstPosition, callID);
                        copyOfNewSolutionRepresentation.add(secondPosition, callID);
                    } else {
                        processedVehicles.add(randomIndexNotYetSelected);// Found no feasible solution for this vehicle.
                        continue; // No changes means copyOfNewSolution is still feasible, so we need to skip the check.
                    }
                }
                if (PickupAndDelivery.feasible(copyOfNewSolution)) {
                    System.out.println("Found feasible solution");
                    newSolution = copyOfNewSolution;
                    updateIgnoredIndices(ignoredIndices, firstIndexOfCall, secondIndexOfCall, feasiblePositions);
                    ignoredIndices.add(feasiblePositions[0]);
                    ignoredIndices.add(feasiblePositions[1]);
                    zeroIndices = getIndicesOfAllZeroes(newSolutionRepresentation);
                    num++;
                    break;
                }
                processedVehicles.add(randomIndexNotYetSelected);
            }
            System.out.println("Processed all vehicles");
            System.out.println("num is now: " + num);
        }
        return newSolution;
    }

    private static void updateIgnoredIndices(List<Integer> ignoredIndices, int firstIndexOfCall,
                                             int secondIndexOfCall, int[] feasiblePositions) {
        int feasiblePosition1 = feasiblePositions[0];
        int feasiblePosition2 = feasiblePositions[1];
        for (int i = 0; i < ignoredIndices.size(); i++) {
            int index = ignoredIndices.get(i);
            if (index > firstIndexOfCall) {
                index--;
            }
            if (index > secondIndexOfCall) {
                index--;
            }
            if (index > feasiblePosition1) {
                index++;
            }
            if (index > feasiblePosition2) {
                index++;
            }
            ignoredIndices.set(i, index);
        }
    }

    private static int[] lookForFeasiblePositionsInVehicle(
            int callID, List<Integer> copyOfNewSolutionRepresentation, List<Integer> zeroIndicesForCopy,
            int startIndexOfSelectedVehicle, int stopIndexOfSelectedVehicle) {
        int vehicleNumber = findVehicleNumberForVehicleContainingIndex(
                (startIndexOfSelectedVehicle + 1), zeroIndicesForCopy);
        int[] feasiblePositions = new int[2];
        feasiblePositions[0] = -1;
        feasiblePositions[1] = -1;
        for (int i = startIndexOfSelectedVehicle + 1; i < (stopIndexOfSelectedVehicle + 1); i++) {
            List<Integer> copyOfCopyNewSolution = new ArrayList<>(copyOfNewSolutionRepresentation);
            copyOfCopyNewSolution.add(i, callID);
            for (int j = i + 1; j < (stopIndexOfSelectedVehicle + 2); j++) {
                copyOfCopyNewSolution.add(j, callID);
                if (constraintsHoldForVehicle(vehicleNumber, copyOfCopyNewSolution)) {
                    feasiblePositions[0] = i;
                    feasiblePositions[1] = j;
                    return feasiblePositions;
                }
                copyOfCopyNewSolution.remove(j);
            }
        }
        return feasiblePositions;
    }

    private static int[] insertIntoEmptyVehicle(int callID, List<Integer> copyOfNewSolution,
                                                int stopIndexOfSelectedVehicle) {
        // Empty vehicle, so we can insert call without caring about the insertion order.
        copyOfNewSolution.add(stopIndexOfSelectedVehicle, callID);
        copyOfNewSolution.add(stopIndexOfSelectedVehicle, callID);
        int[] positions = new int[2];
        positions[0] = stopIndexOfSelectedVehicle;
        positions[1] = stopIndexOfSelectedVehicle + 1;
        return positions;
    }

    private static int[] insertIntoVehicleForOutsourcedCalls(int callID, List<Integer> copyOfNewSolution,
                                                             int startIndexOfSelectedVehicle, int stopIndexOfSelectedVehicle) {
        // Vehicle for outsourced calls
        if (startIndexOfSelectedVehicle == (copyOfNewSolution.size() - 1)) {
            return insertIntoEmptyVehicleForOutsourcedCalls(callID, copyOfNewSolution);
        }
        return insertIntoNotEmptyVehicleForOutsourcedCalls(callID, copyOfNewSolution,
                startIndexOfSelectedVehicle, stopIndexOfSelectedVehicle);
    }

    private static int[] insertIntoNotEmptyVehicleForOutsourcedCalls(int callID, List<Integer> copyOfNewSolution,
                                                                     int startIndexOfSelectedVehicle,
                                                                     int stopIndexOfSelectedVehicle) {
        int[] positions = new int[2];
        int position = findRandomIndexWithinExclusiveBounds(
                startIndexOfSelectedVehicle, stopIndexOfSelectedVehicle + 1, null);
        if (position == stopIndexOfSelectedVehicle) {
            copyOfNewSolution.add(callID); // Add to last position.
        } else {
            copyOfNewSolution.add(position, callID);
        }
        positions[0] = position;
        position = findRandomIndexWithinExclusiveBounds(
                startIndexOfSelectedVehicle, stopIndexOfSelectedVehicle + 2, null);
        if (position == (stopIndexOfSelectedVehicle + 1)) {
            copyOfNewSolution.add(callID);
        } else {
            copyOfNewSolution.add(position, callID);
        }
        if (position <= positions[0]) {
            positions[0] = positions[0] + 1;
        }
        positions[1] = position;
        return positions;
    }

    private static int[] insertIntoEmptyVehicleForOutsourcedCalls(int callID, List<Integer> copyOfNewSolution) {
        // Vehicle for outsourced calls is empty, so we can simply add call to end.
        copyOfNewSolution.add(callID);
        copyOfNewSolution.add(callID);
        int[] positions = new int[2];
        positions[0] = copyOfNewSolution.size() - 2;
        positions[1] = copyOfNewSolution.size() - 1;
        return positions;
    }

}
