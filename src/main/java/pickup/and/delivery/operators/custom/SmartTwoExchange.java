package pickup.and.delivery.operators.custom;

import solution.representations.vector.IVectorSolutionRepresentation;
import solution.representations.vector.VectorSolutionRepresentation;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static pickup.and.delivery.PickupAndDelivery.calculateCost;
import static pickup.and.delivery.PickupAndDelivery.feasible;
import static pickup.and.delivery.operators.OperatorUtilities.*;

public class SmartTwoExchange {

    public static void main(String[] args) {
        List<Integer> values = Arrays.asList(3, 3, 0, 1, 1, 0, 5, 5, 6, 6, 0, 4, 2, 4, 2, 7, 7);
        //3, 3, 0, 7, 1, 7, 1, 0, 5, 5, 6, 6, 0, 4, 2, 4, 2);
        IVectorSolutionRepresentation<Integer> sol = new VectorSolutionRepresentation<>(values);
        System.out.println("feasible(sol) = " + feasible(sol));
        System.out.println(calculateCost(sol));
        System.out.println(useSmartTwoExchangeOnSolution(sol));
        System.out.println(calculateCost(sol));
    }

    public static IVectorSolutionRepresentation<Integer> useSmartTwoExchangeOnSolution(
            IVectorSolutionRepresentation<Integer> solution) {
        IVectorSolutionRepresentation<Integer> newSolution = new VectorSolutionRepresentation<>(
                solution.getSolutionRepresentation());
        List<Integer> newSolutionRepresentation = newSolution.getSolutionRepresentation();
        List<Integer> zeroIndices = getIndicesOfAllZeroes(newSolutionRepresentation);
        List<Integer> startIndicesOfEmptyVehicles = findEmptyVehicles(newSolutionRepresentation, zeroIndices);
        List<Integer> ignoredIndices = new ArrayList<>(zeroIndices);
        while (ignoredIndices.size() < newSolutionRepresentation.size()) {
            int firstIndexOfRandomCall = findRandomIndexWithinExclusiveBounds(
                    MINUS_ONE, newSolutionRepresentation.size(), ignoredIndices);
            int secondIndexOfRandomCall = getSecondIndexOfCall(
                    newSolutionRepresentation, zeroIndices, firstIndexOfRandomCall);
            int callId = newSolutionRepresentation.get(firstIndexOfRandomCall);
            int startIndexOfRandomCallsVehicle = findStartIndex(newSolutionRepresentation, firstIndexOfRandomCall);
            List<Integer> startIndicesOfVehiclesThatCanTakeTheCall =
                    findStartIndicesOfVehiclesThatCanTakeTheCall(zeroIndices, callId);
            startIndicesOfVehiclesThatCanTakeTheCall.removeAll(startIndicesOfEmptyVehicles);
            boolean foundFeasibleSwap = processVehiclesThatCanTakeTheCall(
                    newSolution, newSolutionRepresentation, zeroIndices, firstIndexOfRandomCall,
                    secondIndexOfRandomCall, startIndexOfRandomCallsVehicle, startIndicesOfVehiclesThatCanTakeTheCall);
            if (foundFeasibleSwap) {
                return newSolution;
            }
            ignoredIndices.add(firstIndexOfRandomCall); // Found no feasible swaps for this call,
            ignoredIndices.add(secondIndexOfRandomCall);// so we move on to the next and try again.
        }
        // No feasible swaps were found in solution space N1, so we make a random swap.
        makeRandomSwap(newSolution, newSolutionRepresentation, zeroIndices);
      //  System.out.println("Made a random swap");
        return newSolution;
    }

    private static void makeRandomSwap(IVectorSolutionRepresentation<Integer> newSolution,
                                       List<Integer> newSolutionRepresentation, List<Integer> zeroIndices) {
        List<Integer> ignoredIndices = new ArrayList<>(zeroIndices);
        int firstIndex = findRandomIndexWithinExclusiveBounds(
                MINUS_ONE, newSolutionRepresentation.size(), ignoredIndices);
        int secondIndex = getSecondIndexOfCall(newSolutionRepresentation, zeroIndices, firstIndex);
        ignoredIndices.add(firstIndex);
        ignoredIndices.add(secondIndex);
        int otherFirstIndex = findRandomIndexWithinExclusiveBounds(
                MINUS_ONE, newSolutionRepresentation.size(), ignoredIndices);
        int otherSecondIndex = getSecondIndexOfCall(newSolutionRepresentation, zeroIndices, otherFirstIndex);
        newSolution.swapElementsAtIndices(firstIndex, otherFirstIndex);
        newSolution.swapElementsAtIndices(secondIndex, otherSecondIndex);
    }

    private static boolean processVehiclesThatCanTakeTheCall(
            IVectorSolutionRepresentation<Integer> newSolution, List<Integer> newSolutionRepresentation,
            List<Integer> zeroIndices, int firstIndexOfRandomCall, int secondIndexOfRandomCall,
            int startIndexOfRandomCallsVehicle, List<Integer> startIndicesOfVehiclesThatCanTakeTheCall) {
        List<Integer> processedVehicles = new ArrayList<>();
        while (processedVehicles.size() < startIndicesOfVehiclesThatCanTakeTheCall.size()) {
            int indexOfRandomVehicleToProcess = findRandomIndexWithinExclusiveBounds(
                    MINUS_ONE, startIndicesOfVehiclesThatCanTakeTheCall.size(), processedVehicles);
            int startIndexOfSelectedVehicle = startIndicesOfVehiclesThatCanTakeTheCall
                    .get(indexOfRandomVehicleToProcess);
            int stopIndexOfSelectedVehicle = findStopIndex(newSolutionRepresentation, startIndexOfSelectedVehicle);
            int numberOfCallsInVehicle = findNumberOfDifferentCallsInVehicle(
                    startIndexOfSelectedVehicle, stopIndexOfSelectedVehicle);
            List<Integer> processedCalls = new ArrayList<>();
            if (startIndexOfSelectedVehicle == startIndexOfRandomCallsVehicle) {
                processedCalls.add(firstIndexOfRandomCall); // We want to avoid swapping the call with itself
                processedCalls.add(secondIndexOfRandomCall);
            }
            boolean foundFeasibleSwap = processCallsInSelectedVehicle(
                    newSolution, newSolutionRepresentation, zeroIndices, firstIndexOfRandomCall,
                    secondIndexOfRandomCall, startIndexOfSelectedVehicle, stopIndexOfSelectedVehicle,
                    numberOfCallsInVehicle, processedCalls);
            if (foundFeasibleSwap) {
                return true;
            }
            processedVehicles.add(indexOfRandomVehicleToProcess);
        }
        return false;
    }

    private static boolean processCallsInSelectedVehicle(
            IVectorSolutionRepresentation<Integer> newSolution, List<Integer> newSolutionRepresentation,
            List<Integer> zeroIndices, int firstIndexOfRandomCall, int secondIndexOfRandomCall,
            int startIndexOfSelectedVehicle, int stopIndexOfSelectedVehicle,
            int numberOfCallsInVehicle, List<Integer> processedCalls) {
        while (processedCalls.size() < (2 * numberOfCallsInVehicle)) {
            int firstRandomIndexOfCallInSelectedVehicle = findRandomIndexWithinExclusiveBounds(
                    startIndexOfSelectedVehicle, stopIndexOfSelectedVehicle, processedCalls);
            int secondRandomIndexOfCallInSelectedVehicle = getSecondIndexOfCall(
                    newSolutionRepresentation, zeroIndices, firstRandomIndexOfCallInSelectedVehicle);
            newSolution.swapElementsAtIndices(firstIndexOfRandomCall, firstRandomIndexOfCallInSelectedVehicle);
            newSolution.swapElementsAtIndices(secondIndexOfRandomCall, secondRandomIndexOfCallInSelectedVehicle);
            if (feasible(newSolution)) {
                return true;
            }
            // Swap back if solution is not feasible
            newSolution.swapElementsAtIndices(firstRandomIndexOfCallInSelectedVehicle, firstIndexOfRandomCall);
            newSolution.swapElementsAtIndices(secondRandomIndexOfCallInSelectedVehicle, secondIndexOfRandomCall);
            processedCalls.add(firstRandomIndexOfCallInSelectedVehicle);
            processedCalls.add(secondRandomIndexOfCallInSelectedVehicle);
        }
        return false;
    }



/*
From the given solution
select a random call.
find all vehicles that can handle that call and contains at least one call.
Pick a random vehicle, and try swapping with a random call there.
if new solution is feasible, we return,
otherwise we pick a different call in that vehicle,
if no calls work, we select a different vehicle.
if no vehicle works, we select a different random call.
if no swaps result in a feasible solution, select one at random and swap.


Optionally (K-Exchange?)
if swap is not feasible, find the vehicle that violates the constraints
try swapping a different call in that vehicle with another.
if feasible solution is obtained, we return.
otherwise, swap again, up to a maximum of five swaps.
if not feasible after five swaps, we try a different first swap.

 */





}
