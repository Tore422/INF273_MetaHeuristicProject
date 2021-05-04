package pickup.and.delivery.operators.custom;

import pickup.and.delivery.PickupAndDelivery;
import solution.representations.vector.IVectorSolutionRepresentation;
import solution.representations.vector.VectorSolutionRepresentation;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static pickup.and.delivery.operators.OperatorUtilities.*;

public class GreedyKReinsert {

    public static void main(String[] args) {
        List<Integer> values = Arrays.asList(0, 0, 0, 1, 1, 2, 2, 3, 3, 4, 4, 5, 5, 6, 6, 7, 7);
        IVectorSolutionRepresentation<Integer> sol = new VectorSolutionRepresentation<>(values);
        IVectorSolutionRepresentation<Integer> bestSol = useGreedyKReinsertOnSolution(sol, 3);
        System.out.println("PickupAndDelivery.feasible(bestSol) = " + PickupAndDelivery.feasible(bestSol));
        System.out.println("PickupAndDelivery.calculateCost(sol) = " + PickupAndDelivery.calculateCost(sol));
        System.out.println("bestSol = " + bestSol);
    }

    /*
     Buggy operator, do not use until fixed. Gives unfinished calls somehow.
*/

    public static IVectorSolutionRepresentation<Integer> useGreedyKReinsertOnSolution(
            IVectorSolutionRepresentation<Integer> solution, int numberOfReInsertions) {
        IVectorSolutionRepresentation<Integer> newSolution = new VectorSolutionRepresentation<>(
                solution.getSolutionRepresentation());
        List<Integer> newSolutionRepresentation = newSolution.getSolutionRepresentation();
        List<Integer> zeroIndices = getIndicesOfAllZeroes(newSolutionRepresentation);
        List<Integer> ignoredIndices = new ArrayList<>(zeroIndices);
        int num = 0;
        while (num < numberOfReInsertions && ignoredIndices.size() < newSolutionRepresentation.size()) {
        //    System.out.println("num = " + num);
        //    System.out.println("newSolutionRepresentation = " + newSolutionRepresentation);
            int firstIndexOfCall = findRandomIndexWithinExclusiveBounds(
                    MINUS_ONE, newSolutionRepresentation.size(), ignoredIndices);
            int secondIndexOfCall = getSecondIndexOfCall(newSolutionRepresentation, zeroIndices, firstIndexOfCall);
            int callID = newSolutionRepresentation.get(firstIndexOfCall);
            int startingCost = PickupAndDelivery.calculateCost(newSolution);
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
       //     System.out.println("startIndicesOfVehiclesThatCanTakeTheCall = " + startIndicesOfVehiclesThatCanTakeTheCall);
            boolean foundFeasiblePositionToReinsert = processVehiclesForFeasibleInsertions(
                    ignoredIndices, firstIndexOfCall, secondIndexOfCall, callID, copyOfNewSolution,
                    zeroIndicesForCopy, startIndicesOfVehiclesThatCanTakeTheCall, startingCost);
            if (foundFeasiblePositionToReinsert) {
        //        System.out.println("Found feasible positions");
                num++;
        //        System.out.println("num is now: " + num);
                newSolution = copyOfNewSolution;
                newSolutionRepresentation = newSolution.getSolutionRepresentation();
                zeroIndices = getIndicesOfAllZeroes(newSolutionRepresentation);
      //          System.out.println("newSolutionRepresentation = " + newSolutionRepresentation);
            } else {
                ignoredIndices.add(firstIndexOfCall);
                ignoredIndices.add(secondIndexOfCall);
            }
        }
        return newSolution;
    }

    private static boolean processVehiclesForFeasibleInsertions(
            List<Integer> ignoredIndices, int firstIndexOfCall, int secondIndexOfCall, int callID,
            IVectorSolutionRepresentation<Integer> copyOfNewSolution, List<Integer> zeroIndicesForCopy,
            List<Integer> startIndicesOfVehiclesThatCanTakeTheCall, int startingCost) {
    //    System.out.println("Processing vehicles");
        List<Integer> copyOfNewSolutionRepresentation = copyOfNewSolution.getSolutionRepresentation();
        List<Integer> processedVehicles = new ArrayList<>();
     //   System.out.println("startingCost = " + startingCost);
        while (processedVehicles.size() < startIndicesOfVehiclesThatCanTakeTheCall.size()) {
            IVectorSolutionRepresentation<Integer> copyOfCopyNewSolution =
                    new VectorSolutionRepresentation<>(copyOfNewSolutionRepresentation);
            List<Integer> copyOfCopyNewSolutionRepresentation = copyOfCopyNewSolution.getSolutionRepresentation();
            int randomIndexNotYetSelected = findRandomIndexWithinExclusiveBounds(
                    MINUS_ONE, startIndicesOfVehiclesThatCanTakeTheCall.size(), processedVehicles);
            int startIndexOfSelectedVehicle = startIndicesOfVehiclesThatCanTakeTheCall
                    .get(randomIndexNotYetSelected);
      //      System.out.println("startIndexOfSelectedVehicle = " + startIndexOfSelectedVehicle);
            int stopIndexOfSelectedVehicle = findStopIndex(
                    copyOfNewSolutionRepresentation, zeroIndicesForCopy, startIndexOfSelectedVehicle);
            int[] feasiblePositions = new int[2];
            boolean skipFeasibilityCheck = false;
        //    System.out.println("copyOfNewSolutionRepresentation = " + copyOfNewSolutionRepresentation);
       //     System.out.println("startIndexOfSelectedVehicle = " + startIndexOfSelectedVehicle);
       //     System.out.println("stopIndexOfSelectedVehicle = " + stopIndexOfSelectedVehicle);
       //     System.out.println("callID = " + callID);
            if (stopIndexOfSelectedVehicle == copyOfNewSolutionRepresentation.size()) {
       //         System.out.println("Adding to end of outsourced");
                feasiblePositions = insertIntoVehicleForOutsourcedCalls(callID, copyOfCopyNewSolutionRepresentation,
                        startIndexOfSelectedVehicle, stopIndexOfSelectedVehicle);
           //     System.out.println("feasiblePositions = " + Arrays.toString(feasiblePositions));
            } else if ((startIndexOfSelectedVehicle + 1) == stopIndexOfSelectedVehicle) {
                //System.out.println("Adding to empty vehicle");
                feasiblePositions = insertIntoEmptyVehicle(
                        callID, copyOfCopyNewSolutionRepresentation, stopIndexOfSelectedVehicle);
           //     System.out.println("feasiblePositions = " + Arrays.toString(feasiblePositions));
            } else {
           //     System.out.println();
           //     System.out.println("Looking for feasible positions");
                int vehicleNumber = findVehicleNumberForVehicleContainingIndex(
                        startIndexOfSelectedVehicle, zeroIndicesForCopy);
                int[] startAndStopIndicesForVehicle = new int[2];
                startAndStopIndicesForVehicle[0] = startIndexOfSelectedVehicle;
                startAndStopIndicesForVehicle[1] = stopIndexOfSelectedVehicle;
                List<int[]> candidatePositionsForInsertion = findPositionsWithinConstraints(
                        vehicleNumber, callID, startAndStopIndicesForVehicle, copyOfCopyNewSolutionRepresentation);
           /*     for (int[] pos : candidatePositionsForInsertion) {
                    System.out.println("candidate pos = " + Arrays.toString(pos));
                }*/
                if (!candidatePositionsForInsertion.isEmpty()) {
                //    System.out.println("found feasible pos");
                    int[] lowestCostOption = findLowestCostOption(
                            copyOfCopyNewSolutionRepresentation, callID, startIndexOfSelectedVehicle,
                            stopIndexOfSelectedVehicle, vehicleNumber, candidatePositionsForInsertion);
                //    System.out.println("lowestCostOption = " + Arrays.toString(lowestCostOption));
                    int firstPosition = lowestCostOption[0];
                    int secondPosition = lowestCostOption[1];
                    feasiblePositions[0] = firstPosition;
                    feasiblePositions[1] = secondPosition;
                    copyOfCopyNewSolutionRepresentation.add(firstPosition, callID);
                    copyOfCopyNewSolutionRepresentation.add(secondPosition, callID);
                } else {
                    // Found no feasible solution in this vehicle.
                    // No changes means copyOfNewSolution is still feasible, so we need to skip the feasibility check.
                    skipFeasibilityCheck = true;
                //    System.out.println("Skipped, due to no feasible pos");
                }
            }
            if (!PickupAndDelivery.feasible(copyOfCopyNewSolution)) {
                System.out.println("copyOfCopyNewSolutionRepresentation = " + copyOfCopyNewSolutionRepresentation);
                throw new IllegalStateException("!!!!!");
            }
            if (!skipFeasibilityCheck && (PickupAndDelivery.calculateCost(copyOfCopyNewSolution) < startingCost)) {
          //      System.out.println("Found feasible solution");
                // Found an improved solution
                updateIgnoredIndices(ignoredIndices, firstIndexOfCall, secondIndexOfCall, feasiblePositions);
                int firstFeasiblePosition = feasiblePositions[0];
                int secondFeasiblePosition = feasiblePositions[1];
                ignoredIndices.add(firstFeasiblePosition);
                ignoredIndices.add(secondFeasiblePosition);
                if (firstFeasiblePosition >= copyOfNewSolutionRepresentation.size()) {
                    copyOfNewSolutionRepresentation.add(callID);
                } else {
                    copyOfNewSolutionRepresentation.add(firstFeasiblePosition, callID);
                }
                if (secondFeasiblePosition >= copyOfNewSolutionRepresentation.size()) {
                    copyOfNewSolutionRepresentation.add(callID);
                } else {
                    copyOfNewSolutionRepresentation.add(secondFeasiblePosition, callID);
                }
                return true;
            }
            processedVehicles.add(randomIndexNotYetSelected);
        }
    //    System.out.println("Processed all vehicles without finding feasible solution");
        return false;
    }

    private static void updateIgnoredIndices(List<Integer> ignoredIndices, int firstIndexOfCall,
                                             int secondIndexOfCall, int[] feasiblePositions) {
        int feasiblePosition1 = feasiblePositions[0];
        int feasiblePosition2 = feasiblePositions[1];
        for (int i = 0; i < ignoredIndices.size(); i++) {
            int index = ignoredIndices.get(i);
            if (index > firstIndexOfCall) { // Shift index down if we removed an element in a lower position
                index--;
            }
            if (index > secondIndexOfCall) {
                index--;
            }
            if (index >= feasiblePosition1) { // Shift index up if we insert element at same or lower position
                index++;
            }
            if (index >= feasiblePosition2) {
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

    private static int[] insertIntoVehicleForOutsourcedCalls(
            int callID, List<Integer> copyOfNewSolution,
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
        // Vehicle for outsourced calls is empty, so we can simply add call to the end.
        copyOfNewSolution.add(callID);
        copyOfNewSolution.add(callID);
        int[] positions = new int[2];
        positions[0] = copyOfNewSolution.size() - 2;
        positions[1] = copyOfNewSolution.size() - 1;
        return positions;
    }

}
