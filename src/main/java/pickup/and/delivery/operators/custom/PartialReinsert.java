package pickup.and.delivery.operators.custom;

import solution.representations.vector.IVectorSolutionRepresentation;
import solution.representations.vector.VectorSolutionRepresentation;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static pickup.and.delivery.PickupAndDelivery.feasible;
import static pickup.and.delivery.operators.OperatorUtilities.*;

public class PartialReinsert {

    private PartialReinsert() {
        throw new IllegalStateException("Utility class");
    }

    public static void main(String[] args) {
        List<Integer> values = Arrays.asList(3, 3, 0, 7, 1, 7, 1, 0, 5, 5, 6, 6, 0, 4, 2, 4, 2);
        //List<Integer> values = Arrays.asList(96, 21, 21, 41, 96, 1, 1, 41, 0, 84, 92, 84, 92, 81, 53, 81, 53, 0, 5, 29, 8, 8, 5, 29, 0, 103, 103, 31, 109, 109, 31, 0, 26, 26, 14, 24, 24, 14, 0, 114, 6, 6, 114, 75, 75, 0, 58, 58, 60, 42, 60, 42, 113, 113, 0, 118, 118, 93, 93, 0, 82, 82, 2, 2, 0, 34, 34, 0, 97, 97, 119, 119, 0, 91, 91, 59, 59, 100, 100, 0, 52, 52, 10, 10, 0, 18, 18, 22, 22, 19, 36, 36, 19, 0, 61, 61, 98, 20, 98, 20, 0, 39, 39, 51, 51, 0, 94, 94, 55, 40, 55, 40, 0, 74, 28, 28, 74, 4, 4, 0, 65, 65, 62, 62, 130, 130, 0, 89, 89, 67, 78, 67, 78, 0, 43, 43, 112, 112, 15, 15, 0, 120, 122, 120, 122, 27, 27, 129, 124, 124, 129, 0, 80, 66, 66, 80, 99, 99, 0, 73, 73, 105, 105, 0, 77, 77, 87, 87, 125, 125, 0, 70, 70, 0, 17, 83, 17, 83, 11, 11, 95, 95, 46, 46, 0, 107, 107, 127, 127, 0, 32, 32, 110, 110, 0, 116, 116, 0, 69, 69, 12, 12, 85, 85, 0, 102, 126, 128, 126, 102, 128, 0, 90, 90, 54, 54, 23, 23, 0, 50, 111, 50, 111, 0, 115, 115, 13, 106, 13, 106, 0, 121, 121, 44, 44, 3, 3, 0, 108, 117, 108, 117, 45, 45, 0, 123, 123, 0, 7, 37, 57, 7, 37, 57, 88, 88, 0, 49, 35, 38, 35, 49, 38, 0, 25, 47, 101, 30, 25, 76, 56, 48, 101, 79, 16, 79, 76, 86, 86, 64, 63, 56, 47, 63, 33, 72, 71, 64, 71, 9, 72, 68, 33, 30, 9, 68, 48, 16, 104, 104);
        System.out.println("values = " + values);
        List<Integer> a = new ArrayList<>(values.subList(0, 9));
        System.out.println("values = " + a);

        IVectorSolutionRepresentation<Integer> sol = new VectorSolutionRepresentation<>(values);
        System.out.println("feasible(sol) = " + feasible(sol));
        System.out.println(usePartialReinsertOnSolution(sol));
    }

    public static IVectorSolutionRepresentation<Integer> usePartialReinsertOnSolution(
            IVectorSolutionRepresentation<Integer> solution) {
        IVectorSolutionRepresentation<Integer> newSolution = new VectorSolutionRepresentation<>(
                solution.getSolutionRepresentation());
        List<Integer> newSolutionRepresentation = newSolution.getSolutionRepresentation();
        List<Integer> zeroIndices = getIndicesOfAllZeroes(newSolutionRepresentation);
        List<int[]> indicesOfVehiclesToProcess = findVehiclesWithMoreThanOneCall(zeroIndices);
        if (indicesOfVehiclesToProcess.isEmpty()) {
            return randomlyReinsertPartOfAnOutsourcedCall(newSolution, zeroIndices);
        }
        List<Integer> vehiclesProcessedSoFar = new ArrayList<>(indicesOfVehiclesToProcess.size());
        while (vehiclesProcessedSoFar.size() < indicesOfVehiclesToProcess.size()) {
            int indexOfRandomVehicleToProcess = findRandomIndexWithinExclusiveBounds(
                    MINUS_ONE, indicesOfVehiclesToProcess.size(), vehiclesProcessedSoFar);
            int[] startAndStopIndexOfVehicle = indicesOfVehiclesToProcess.get(indexOfRandomVehicleToProcess);
            int vehicleNumber = findVehicleNumberForVehicleStartingAtIndex(
                    startAndStopIndexOfVehicle[0], zeroIndices);
            int costOfInitialSolution = computeCostForVehicle(
                    startAndStopIndexOfVehicle[0], startAndStopIndexOfVehicle[1],
                    vehicleNumber, newSolutionRepresentation);
            boolean foundImprovement = processVehicleLookingForImprovement(
                    newSolution, startAndStopIndexOfVehicle, vehicleNumber, costOfInitialSolution);
            if (foundImprovement) {
                return newSolution;
            }
            vehiclesProcessedSoFar.add(indexOfRandomVehicleToProcess);
        }
        int startIndexOfOutsourcedCalls = zeroIndices.get(zeroIndices.size() - 1);
        if (findNumberOfDifferentCallsInVehicle(startIndexOfOutsourcedCalls, newSolutionRepresentation.size()) != 0) {
            //Tried all possibilities without finding an improvement,
            //so we select a random element from outsourced calls,
            //and insert that element into a random position there.
            return randomlyReinsertPartOfAnOutsourcedCall(newSolution, zeroIndices);
        } else {
            //No outsourced calls to move, so we select a random vehicle, and perform a random move there
            int indexOfCallToMove = findRandomIndexWithinExclusiveBounds(
                    MINUS_ONE, startIndexOfOutsourcedCalls, zeroIndices);
            int[] startAndStopIndices = findStartAndStopIndexOfVehicle(
                    zeroIndices, indexOfCallToMove, newSolutionRepresentation.size());
            int callId = newSolutionRepresentation.remove(indexOfCallToMove);
            int randomIndexToInsertCallInVehicle = findRandomIndexWithinExclusiveBounds(
                    startAndStopIndices[0], startAndStopIndices[1], null);
            newSolutionRepresentation.add(randomIndexToInsertCallInVehicle, callId);
            return newSolution;
        }
/*
    from solution
    find zeroIndices
    Then find each vehicle with at least 2 different calls
    (excluding outsourced)
    and add start index to list.

    Pick a random vehicle, not outsourced, from that list.

    Calculate cost of current setup.
    For each index within that vehicle,
    try inserting that partial call in each position within the vehicle,
    and calculate the new cost.
    If capacity and time limitations are upheld,
    and the new cost is lower than previous best,
    set this index as the best found position so far.
    Repeat for each index (n^2 runtime)

    When finished, insert the partial call into the best found position.
    If no improvement was found, try selecting a different vehicle,
    (blacklisting previously tested vehicles)
    and repeat the process within the new vehicle.

    If still no improvement is possible,
    randomly select a partial call within outsourced
    and insert into a different random position in outsourced.
    [May not matter that much, but simulated annealing having a chance of
    accepting a worse solution makes it possible to
    generate more of the possible solutions this way]

*/
    }

    private static boolean processVehicleLookingForImprovement(
            IVectorSolutionRepresentation<Integer> newSolution, int[] startAndStopIndexOfVehicle,
            int vehicleNumber, int costOfInitialSolution) {
        List<Integer> newSolutionRepresentation = newSolution.getSolutionRepresentation();
        int bestCostSoFarForCurrentVehicle = costOfInitialSolution;
        int indexOfCallToMove = -1, indexToInsertCall = -1;
        int startIndex = startAndStopIndexOfVehicle[0];
        int stopIndex = startAndStopIndexOfVehicle[1];
        for (int i = startIndex + 1; i < stopIndex; i++) {
            List<Integer> copyOfSolutionRepresentation = new ArrayList<>(
                    newSolutionRepresentation.subList(0, stopIndex + 1));
            int currentCall = copyOfSolutionRepresentation.remove(i);
            for (int j = startIndex + 1; j < stopIndex; j++) {
                copyOfSolutionRepresentation.add(j, currentCall);
                boolean timeWindowConstraintHolds = timeWindowConstraintHoldsFor(
                        startIndex, stopIndex, vehicleNumber, copyOfSolutionRepresentation);
                boolean vehicleCapacityConstraintHolds = vehicleCapacityConstraintHoldsFor(
                        startIndex, stopIndex, vehicleNumber, copyOfSolutionRepresentation);
                if (timeWindowConstraintHolds && vehicleCapacityConstraintHolds) {
                    int costOfSolutionCandidate = computeCostForVehicle(
                            startIndex, stopIndex, vehicleNumber, copyOfSolutionRepresentation);
                    if (costOfSolutionCandidate < bestCostSoFarForCurrentVehicle) {
                        indexOfCallToMove = i;
                        indexToInsertCall = j;
                        bestCostSoFarForCurrentVehicle = costOfSolutionCandidate;
                    }
                }
                copyOfSolutionRepresentation.remove(j);
            }
        }
        if (bestCostSoFarForCurrentVehicle < costOfInitialSolution) {
          //  System.out.println("Improved solution by: " + (costOfInitialSolution - bestCostSoFarForCurrentVehicle));
            int partOfCallToReinsert = newSolutionRepresentation.remove(indexOfCallToMove);
            newSolutionRepresentation.add(indexToInsertCall, partOfCallToReinsert);
            return true;
        }
        return false;
    }

    private static IVectorSolutionRepresentation<Integer> randomlyReinsertPartOfAnOutsourcedCall(
            IVectorSolutionRepresentation<Integer> newSolution, List<Integer> zeroIndices) {
        // Select random element from outsourced calls,
        // and insert element into a random position there.
        List<Integer> newSolutionRepresentation = newSolution.getSolutionRepresentation();
        int startIndex = zeroIndices.get(zeroIndices.size() - 1);
        int stopIndex = newSolutionRepresentation.size();
        int randomIndex = findRandomIndexWithinExclusiveBounds(startIndex, stopIndex, null);
        int randomlySelectedCall = newSolutionRepresentation.remove(randomIndex);
        randomIndex = findRandomIndexWithinExclusiveBounds(startIndex, stopIndex - 1, null);
        newSolutionRepresentation.add(randomIndex, randomlySelectedCall);
        return newSolution;
    }
}
