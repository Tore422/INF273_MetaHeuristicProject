package pickup.and.delivery.operators.custom;

import solution.representations.vector.IVectorSolutionRepresentation;
import solution.representations.vector.VectorSolutionRepresentation;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static pickup.and.delivery.PickupAndDelivery.feasible;
import static pickup.and.delivery.operators.OperatorUtilities.*;

public class SmartOneReinsert {

    public static void main(String[] args) {
        List<Integer> values = Arrays.asList(3, 3, 0, 1, 1, 0, 5, 5, 6, 6, 0, 4, 2, 4, 2);
                //3, 3, 0, 7, 1, 7, 1, 0, 5, 5, 6, 6, 0, 4, 2, 4, 2);
        IVectorSolutionRepresentation<Integer> sol = new VectorSolutionRepresentation<>(values);
        System.out.println("feasible(sol) = " + feasible(sol));
        /*int[] startAndStop = new int[2];
        startAndStop[0] = 2;
        startAndStop[1] = 5;
        List<int[]> candidates = findPositionsWithinConstraints(2, 7,
                startAndStop, sol.getSolutionRepresentation());
        for (int[] pos : candidates) {
            System.out.println("pos = " + Arrays.toString(pos));
        }
        int[] bestPositions = findLowestCostOption(sol.getSolutionRepresentation(), 7,
                startAndStop[0], startAndStop[1], 2, candidates);
        System.out.println("bestPositions = " + Arrays.toString(bestPositions));
        sol.getSolutionRepresentation().add(bestPositions[0], 7);
        sol.getSolutionRepresentation().add(bestPositions[1], 7);
        System.out.println("solutionRepresentation = " + sol.getSolutionRepresentation());
        System.out.println();
        List<Integer> values2 = Arrays.asList(96, 41, 96, 1, 1, 41, 0, 84, 92, 84, 92, 81, 53, 81, 53, 0, 5, 29, 8, 8, 5, 29, 0, 103, 103, 31, 109, 109, 31, 0, 26, 26, 14, 24, 24, 14, 0, 114, 6, 6, 114, 75, 75, 0, 58, 58, 60, 42, 60, 42, 113, 113, 0, 118, 118, 93, 93, 0, 82, 82, 2, 2, 0, 34, 34, 0, 97, 97, 119, 119, 0, 91, 91, 59, 59, 100, 100, 0, 52, 52, 10, 10, 0, 18, 18, 22, 22, 19, 36, 36, 19, 0, 61, 61, 98, 20, 98, 20, 0, 39, 39, 51, 51, 0, 94, 94, 55, 40, 55, 40, 0, 74, 28, 28, 74, 4, 4, 0, 65, 65, 62, 62, 130, 130, 0, 89, 89, 67, 78, 67, 78, 0, 43, 43, 112, 112, 15, 15, 0, 120, 122, 120, 122, 27, 27, 129, 124, 124, 129, 0, 80, 66, 66, 80, 99, 99, 0, 73, 73, 105, 105, 0, 77, 77, 87, 87, 125, 125, 0, 70, 70, 0, 17, 83, 17, 83, 11, 11, 95, 95, 46, 46, 0, 107, 107, 127, 127, 0, 32, 32, 110, 110, 0, 116, 116, 0, 69, 69, 12, 12, 85, 85, 0, 102, 126, 128, 126, 102, 128, 0, 90, 90, 54, 54, 23, 23, 0, 50, 111, 50, 111, 0, 115, 115, 13, 106, 13, 106, 0, 121, 121, 44, 44, 3, 3, 0, 108, 117, 108, 117, 45, 45, 0, 123, 123, 0, 7, 37, 57, 7, 37, 57, 88, 88, 0, 49, 35, 38, 35, 49, 38, 0, 25, 47, 101, 30, 25, 76, 56, 48, 101, 79, 16, 79, 76, 86, 86, 64, 63, 56, 47, 63, 33, 72, 71, 64, 71, 9, 72, 68, 33, 30, 9, 68, 48, 16, 104, 104);
        System.out.println("values = " + values2);
        sol = new VectorSolutionRepresentation<>(values2);
        System.out.println(feasible(sol));
        int callId = 21;
        int vehicle = 1;
        startAndStop[0] = -1;
        startAndStop[1] = 6;
        candidates = findPositionsWithinConstraints(vehicle, callId, startAndStop, sol.getSolutionRepresentation());
        if (candidates.isEmpty()) {
            System.out.println("No candidates were found...");
        } else {
            for (int[] candidate : candidates) {
                System.out.println(Arrays.toString(candidate));
            }
            bestPositions = findLowestCostOption(sol.getSolutionRepresentation(), callId,
                    startAndStop[0], startAndStop[1], vehicle, candidates);
            System.out.println("bestPositions = " + Arrays.toString(bestPositions));
            sol.getSolutionRepresentation().add(bestPositions[0], callId);
            sol.getSolutionRepresentation().add(bestPositions[1], callId);
            System.out.println("solution = " + sol.getSolutionRepresentation());
        }*/
        useSmartOneReinsertOnSolution(sol);
    }

    public static IVectorSolutionRepresentation<Integer> useSmartOneReinsertOnSolution(
            IVectorSolutionRepresentation<Integer> solution) {
        IVectorSolutionRepresentation<Integer> newSolution = new VectorSolutionRepresentation<>(
                solution.getSolutionRepresentation());
        List<Integer> newSolutionRepresentation = newSolution.getSolutionRepresentation();
        List<Integer> zeroIndices = getIndicesOfAllZeroes(newSolutionRepresentation);
        System.out.println("zeroIndices = " + zeroIndices);
        boolean solutionHasOutsourcedCalls =
                zeroIndices.get(zeroIndices.size() - 1) != (newSolutionRepresentation.size() - 1);
        System.out.println("solutionHasOutsourcedCalls = " + solutionHasOutsourcedCalls);
        if (solutionHasOutsourcedCalls) {
            if(processOutsourcedCalls(newSolutionRepresentation, zeroIndices)) {
                return newSolution;
            }
        }
        while (true) {
            int randomIndex = RANDOM.nextInt(zeroIndices.get(zeroIndices.size() - 1));
            int element = newSolutionRepresentation.get(randomIndex);
            if (element != 0) {
                int firstIndexOfCall = randomIndex;
                int secondIndexOfCall = getSecondIndexOfCall(newSolutionRepresentation, zeroIndices, firstIndexOfCall);
                    


            }

        }

        return newSolution;
    }

    private static boolean processOutsourcedCalls(List<Integer> newSolutionRepresentation, List<Integer> zeroIndices) {
        int[] startAndStopIndexOfVehicle = new int[2];
        startAndStopIndexOfVehicle[0] = zeroIndices.get(zeroIndices.size() - 1);
        startAndStopIndexOfVehicle[1] = newSolutionRepresentation.size();
        int numberOfCallsThatCanBeProcessed = findNumberOfDifferentCallsInVehicle(
                startAndStopIndexOfVehicle[0], startAndStopIndexOfVehicle[1]);
        System.out.println("numberOfCallsThatCanBeProcessed = " + numberOfCallsThatCanBeProcessed);
        List<Integer> ignoredIndices = new ArrayList<>();
        do {
            int firstIndexOfOutsourcedCall = findRandomIndexWithinVehicle(
                    startAndStopIndexOfVehicle[0], startAndStopIndexOfVehicle[1], ignoredIndices);
            int secondIndexOfOutsourcedCall = getSecondIndexOfCall(
                    newSolutionRepresentation, zeroIndices, firstIndexOfOutsourcedCall);
            int callId = newSolutionRepresentation.get(firstIndexOfOutsourcedCall);
            List<Integer> startIndicesOfVehiclesThatCanTakeTheCall =
                    findStartIndicesOfVehiclesThatCanTakeTheCall(zeroIndices, callId);
            if (!startIndicesOfVehiclesThatCanTakeTheCall.isEmpty()) {
                List<Integer> vehiclesProcessedSoFar = new ArrayList<>();
                while (vehiclesProcessedSoFar.size() < startIndicesOfVehiclesThatCanTakeTheCall.size()) {
                    int randomVehicleIndex = RANDOM.nextInt(startIndicesOfVehiclesThatCanTakeTheCall.size());
                    if (!vehiclesProcessedSoFar.contains(randomVehicleIndex)) {
                        int startIndex = startIndicesOfVehiclesThatCanTakeTheCall.get(randomVehicleIndex);
                        int stopIndex = findStopIndex(newSolutionRepresentation, startIndex);
                        if (findNumberOfDifferentCallsInVehicle(startIndex, stopIndex) == 0) {
                            // Here we assume that a vehicle that can handle the call is not limited by
                            // constraints when it's not handling any other calls.
                            addCallToEmptyVehicle(newSolutionRepresentation, firstIndexOfOutsourcedCall,
                                    secondIndexOfOutsourcedCall, callId, stopIndex);
                            return true;
                        }
                        int vehicleNumber = findVehicleNumberForVehicleStartingAtIndex(startIndex, zeroIndices);
                        int[] startAndStopIndicesForVehicle = new int[2]; // Needed for method call
                        startAndStopIndicesForVehicle[0] = startIndex;
                        startAndStopIndicesForVehicle[1] = stopIndex;
                        List<int[]> candidatePositionsForInsertion = findPositionsWithinConstraints(
                                vehicleNumber, callId, startAndStopIndicesForVehicle, newSolutionRepresentation);
                        if (!candidatePositionsForInsertion.isEmpty()) {
                            int[] lowestCostOption = findLowestCostOption(newSolutionRepresentation, callId,
                                    startIndex, stopIndex, vehicleNumber, candidatePositionsForInsertion);
                            newSolutionRepresentation.add(lowestCostOption[0], callId);
                            newSolutionRepresentation.add(lowestCostOption[1], callId);
                            return true;
                        }
                        // Found no positions for which the constraints hold, so we try another vehicle
                        vehiclesProcessedSoFar.add(randomVehicleIndex);
                    }
                }
            }
            ignoredIndices.add(firstIndexOfOutsourcedCall); // No vehicle could handle the call,
            ignoredIndices.add(secondIndexOfOutsourcedCall);// so we try another outsourced call.
        } while (ignoredIndices.size() != 2 * numberOfCallsThatCanBeProcessed);
        return false;
    }

    private static void addCallToEmptyVehicle(
            List<Integer> newSolutionRepresentation, int firstIndexOfCall,
            int secondIndexOfCall, int callId, int stopIndex) {
        // If there is a vehicle that can handle the call and is empty,
        // we simply add the call to that vehicle as the order of insertion does not matter.
        newSolutionRepresentation.remove(firstIndexOfCall);
        newSolutionRepresentation.remove(secondIndexOfCall);
        newSolutionRepresentation.add(stopIndex, callId);
        newSolutionRepresentation.add(stopIndex, callId);
    }

/*
get solution
find zero indices
check if there are any outsourced calls

If there are outsourced calls,
try to move one of them to a vehicle that can handle that call.
If a vehicle can handle the call, try to insert it in each

If no vehicle can take the call, try the next call.
If no moves are possible without violating time and capacity constraints,
we instead try selecting a random call from among the other vehicles,
and try inserting it into another vehicle that can handle that call.
Pick a random vehicle among those that can take the call,
and attempt insertion in


find vehicles that contain calls




 */


}
