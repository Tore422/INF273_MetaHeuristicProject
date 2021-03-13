package pickup.and.delivery.operators.custom;

import pickup.and.delivery.operators.OperatorUtilities;
import solution.representations.vector.IVectorSolutionRepresentation;
import solution.representations.vector.VectorSolutionRepresentation;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static pickup.and.delivery.PickupAndDelivery.feasible;

public class SmartOneReinsert {

    public static void main(String[] args) {
        List<Integer> values = Arrays.asList(3, 3, 0, 7, 1, 7, 1, 0, 5, 5, 6, 6, 0, 4, 2, 4, 2);
        IVectorSolutionRepresentation<Integer> sol = new VectorSolutionRepresentation<>(values);
        System.out.println("feasible(sol) = " + feasible(sol));
        useSmartOneReinsertOnSolution(sol);
    }

    public static IVectorSolutionRepresentation<Integer> useSmartOneReinsertOnSolution(
            IVectorSolutionRepresentation<Integer> solution) {
        IVectorSolutionRepresentation<Integer> newSolution = new VectorSolutionRepresentation<>(
                solution.getSolutionRepresentation());
        List<Integer> newSolutionRepresentation = newSolution.getSolutionRepresentation();
        List<Integer> zeroIndices = OperatorUtilities.getIndicesOfAllZeroes(newSolutionRepresentation);
        System.out.println("zeroIndices = " + zeroIndices);
        boolean solutionHasOutsourcedCalls =
                zeroIndices.get(zeroIndices.size() - 1) != (newSolutionRepresentation.size() - 1);
        System.out.println("solutionHasOutsourcedCalls = " + solutionHasOutsourcedCalls);
        if (solutionHasOutsourcedCalls) {
            int[] startAndStopIndexOfVehicle = new int[2];
            startAndStopIndexOfVehicle[0] = zeroIndices.get(zeroIndices.size() - 1);
            startAndStopIndexOfVehicle[1] = newSolutionRepresentation.size();
            int numberOfCallsThatCanBeProcessed = OperatorUtilities.findNumberOfDifferentCallsInVehicle(
                    startAndStopIndexOfVehicle[0], startAndStopIndexOfVehicle[1]);
            System.out.println("numberOfCallsThatCanBeProcessed = " + numberOfCallsThatCanBeProcessed);
            List<Integer> ignoredIndices = new ArrayList<>();
            do {
                int firstIndexOfCall = OperatorUtilities.findRandomIndexWithinVehicle(
                        startAndStopIndexOfVehicle[0], startAndStopIndexOfVehicle[1], ignoredIndices);
                int secondIndexOfCall = OperatorUtilities.getSecondIndexOfCall(
                        newSolutionRepresentation, zeroIndices, startAndStopIndexOfVehicle, firstIndexOfCall);
                int firstPartOfCall = newSolutionRepresentation.get(firstIndexOfCall);
                int secondPartOfCall = newSolutionRepresentation.get(secondIndexOfCall);
                List<Integer> startIndicesOfVehiclesThatCanTakeTheCall = OperatorUtilities
                        .findStartIndicesOfVehiclesThatCanTakeTheCall(zeroIndices, firstPartOfCall);
                if (!startIndicesOfVehiclesThatCanTakeTheCall.isEmpty()) {
                    List<Integer> vehiclesProcessedSoFar = new ArrayList<>();
                    while (true) {
                        int randomVehicleIndex = OperatorUtilities.RANDOM.nextInt(startIndicesOfVehiclesThatCanTakeTheCall.size());
                        if (!vehiclesProcessedSoFar.contains(randomVehicleIndex)) {


                            vehiclesProcessedSoFar.add(randomVehicleIndex);
                        }

                        List<Integer> copyOfNewSolutionRepresentation = new ArrayList<>(newSolutionRepresentation);
                    }


                }


                ignoredIndices.add(firstIndexOfCall);
                ignoredIndices.add(secondIndexOfCall);
            } while (ignoredIndices.size() != 2 * numberOfCallsThatCanBeProcessed);


        }


        return newSolution;
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
