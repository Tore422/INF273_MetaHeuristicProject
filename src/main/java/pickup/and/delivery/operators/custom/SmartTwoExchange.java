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

    public static int numberOfTimesSolutionIsInfeasible = 0;
    public static int numberOfTimesSolutionIsInfeasibleOnArrival = 0;
    public static int numberOfTimesSolutionIsInfeasibleAfterRegularSwap = 0;
    public static int numberOfTimesSolutionIsInfeasibleAfterRandomSwap = 0;

    private SmartTwoExchange() {
        throw new IllegalStateException("Utility class");
    }

    public static void main(String[] args) {
        List<Integer> values = Arrays.asList(3, 3, 0, 1, 1, 0, 5, 5, 6, 6, 0, 4, 2, 4, 2, 7, 7);
        //3, 3, 0, 7, 1, 7, 1, 0, 5, 5, 6, 6, 0, 4, 2, 4, 2);
        IVectorSolutionRepresentation<Integer> sol = new VectorSolutionRepresentation<>(values);
        System.out.println("feasible(sol) = " + feasible(sol));
        System.out.println(calculateCost(sol));
       // System.out.println(useSmartTwoExchangeOnSolution(sol));
       // System.out.println(calculateCost(sol));
    }

    public static IVectorSolutionRepresentation<Integer> useSmartTwoExchangeOnSolution(
            IVectorSolutionRepresentation<Integer> solution) {
        IVectorSolutionRepresentation<Integer> newSolution = new VectorSolutionRepresentation<>(
                solution.getSolutionRepresentation());
        List<Integer> newSolutionRepresentation = newSolution.getSolutionRepresentation();
        List<Integer> zeroIndices = getIndicesOfAllZeroes(newSolutionRepresentation);
        List<Integer> startIndicesOfEmptyVehicles = findEmptyVehicles(newSolutionRepresentation, zeroIndices);
        List<Integer> positionsToIgnore = new ArrayList<>();
        if (!feasible(solution)) {
            numberOfTimesSolutionIsInfeasibleOnArrival++;
        }
        boolean foundFeasibleSwap = processSolutionSpace(
                newSolution, newSolutionRepresentation, zeroIndices, startIndicesOfEmptyVehicles, positionsToIgnore);
        if (!foundFeasibleSwap) { // No feasible swaps were found in the solution space, so we make a random swap.
            makeRandomSwap(newSolution, zeroIndices);
            if (!feasible(newSolution)) {
                numberOfTimesSolutionIsInfeasibleAfterRandomSwap++;
            }
        }
        if (!feasible(newSolution)) {
            numberOfTimesSolutionIsInfeasible++;
        }
        return newSolution;
    }

    private static boolean processSolutionSpace(
            IVectorSolutionRepresentation<Integer> newSolution, List<Integer> newSolutionRepresentation,
            List<Integer> zeroIndices, List<Integer> startIndicesOfEmptyVehicles, List<Integer> positionsToIgnore) {
        List<Integer> ignoredIndices = new ArrayList<>(zeroIndices);
        boolean tryMovingMostExpensiveCall = decideRemovalOperator();
        if (findEmptyVehicles(newSolutionRepresentation, zeroIndices).size() == zeroIndices.size()) {
            tryMovingMostExpensiveCall = false; // Only outsourced vehicles, so makes no sense to call
        }
        while (ignoredIndices.size() < newSolutionRepresentation.size()) {
            int firstIndexOfRandomCall;
            int secondIndexOfRandomCall;
            if (tryMovingMostExpensiveCall) {
                int[] positionsOfMostExpensiveCall = findPositionsOfMostExpensiveCall(
                        newSolutionRepresentation, zeroIndices);
                firstIndexOfRandomCall = positionsOfMostExpensiveCall[0];
                secondIndexOfRandomCall = positionsOfMostExpensiveCall[1];
                tryMovingMostExpensiveCall = false;
            } else {
                firstIndexOfRandomCall = findRandomIndexWithinExclusiveBounds(
                        MINUS_ONE, newSolutionRepresentation.size(), ignoredIndices);
                secondIndexOfRandomCall = getSecondIndexOfCall(
                        newSolutionRepresentation, zeroIndices, firstIndexOfRandomCall);
            }
            int callId = newSolutionRepresentation.get(firstIndexOfRandomCall);
            int startIndexOfRandomCallsVehicle = findStartIndex(
                    newSolutionRepresentation, zeroIndices, firstIndexOfRandomCall);
            List<Integer> startIndicesOfVehiclesThatCanTakeTheCall =
                    findStartIndicesOfVehiclesThatCanTakeTheCall(zeroIndices, callId);
            startIndicesOfVehiclesThatCanTakeTheCall.removeAll(startIndicesOfEmptyVehicles);
            boolean foundFeasibleSwap = processVehiclesThatCanTakeTheCall(
                    newSolution, newSolutionRepresentation, zeroIndices, firstIndexOfRandomCall,
                    secondIndexOfRandomCall, startIndexOfRandomCallsVehicle, startIndicesOfVehiclesThatCanTakeTheCall,
                    positionsToIgnore);
            if (foundFeasibleSwap) {
                return true;
            }
            ignoredIndices.add(firstIndexOfRandomCall); // Found no feasible swaps for this call,
            ignoredIndices.add(secondIndexOfRandomCall);// so we move on to the next and try again.
        }
        return false;
    }

    private static void makeRandomSwap(IVectorSolutionRepresentation<Integer> newSolution, List<Integer> zeroIndices) {
        List<Integer> newSolutionRepresentation = newSolution.getSolutionRepresentation();
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
            int startIndexOfRandomCallsVehicle, List<Integer> startIndicesOfVehiclesThatCanTakeTheCall,
            List<Integer> positionsToIgnore) {
        List<Integer> processedVehicles = new ArrayList<>();
        while (processedVehicles.size() < startIndicesOfVehiclesThatCanTakeTheCall.size()) {
            int indexOfRandomVehicleToProcess = findRandomIndexWithinExclusiveBounds(
                    MINUS_ONE, startIndicesOfVehiclesThatCanTakeTheCall.size(), processedVehicles);
            int startIndexOfSelectedVehicle = startIndicesOfVehiclesThatCanTakeTheCall
                    .get(indexOfRandomVehicleToProcess);
            int stopIndexOfSelectedVehicle = findStopIndex(
                    newSolutionRepresentation, zeroIndices, startIndexOfSelectedVehicle);
            int numberOfCallsInVehicle = findNumberOfDifferentCallsInVehicle(
                    startIndexOfSelectedVehicle, stopIndexOfSelectedVehicle);
            List<Integer> indicesOfProcessedCalls = indicesPreviouslyProcessed(
                    firstIndexOfRandomCall, secondIndexOfRandomCall,
                    startIndexOfRandomCallsVehicle, startIndexOfSelectedVehicle);
            boolean foundFeasibleSwap = processCallsInSelectedVehicle(
                    newSolution, newSolutionRepresentation, zeroIndices, firstIndexOfRandomCall,
                    secondIndexOfRandomCall, startIndexOfSelectedVehicle, stopIndexOfSelectedVehicle,
                    numberOfCallsInVehicle, indicesOfProcessedCalls);
            if (foundFeasibleSwap) {
                return true;
            }
            processedVehicles.add(indexOfRandomVehicleToProcess);
        }
        return false;
    }

    private static List<Integer> indicesPreviouslyProcessed(
            int firstIndexOfRandomCall, int secondIndexOfRandomCall,
            int startIndexOfRandomCallsVehicle, int startIndexOfSelectedVehicle) {
        List<Integer> indicesOfProcessedCalls = new ArrayList<>();
        if (startIndexOfSelectedVehicle == startIndexOfRandomCallsVehicle) {
            indicesOfProcessedCalls.add(firstIndexOfRandomCall); // We want to avoid swapping the call with itself
            indicesOfProcessedCalls.add(secondIndexOfRandomCall);
        }
        return indicesOfProcessedCalls;
    }

    public static int counterA = 0;
    public static int counterB = 0;

    private static boolean processCallsInSelectedVehicle(
            IVectorSolutionRepresentation<Integer> newSolution, List<Integer> newSolutionRepresentation,
            List<Integer> zeroIndices, int firstIndexOfRandomCall, int secondIndexOfRandomCall,
            int startIndexOfSelectedVehicle, int stopIndexOfSelectedVehicle,
            int numberOfCallsInVehicle, List<Integer> indicesOfProcessedCalls) {
        boolean firstVehicleIsOutsourced = isOutsourcedVehicle(newSolutionRepresentation, zeroIndices,
                firstIndexOfRandomCall);
        boolean secondVehicleIsOutsourced = isOutsourcedVehicle(newSolutionRepresentation, zeroIndices,
                stopIndexOfSelectedVehicle);
        int[] vehicleNumbers = getVehicleNumbers(zeroIndices, firstIndexOfRandomCall, (startIndexOfSelectedVehicle + 1),
                firstVehicleIsOutsourced, secondVehicleIsOutsourced);
        while (indicesOfProcessedCalls.size() < (2 * numberOfCallsInVehicle)) {
            int firstRandomIndexOfCallInSelectedVehicle = findRandomIndexWithinExclusiveBounds(
                    startIndexOfSelectedVehicle, stopIndexOfSelectedVehicle, indicesOfProcessedCalls);
            int secondRandomIndexOfCallInSelectedVehicle = getSecondIndexOfCall(
                    newSolutionRepresentation, zeroIndices, firstRandomIndexOfCallInSelectedVehicle);
            newSolution.swapElementsAtIndices(firstIndexOfRandomCall, firstRandomIndexOfCallInSelectedVehicle);
            newSolution.swapElementsAtIndices(secondIndexOfRandomCall, secondRandomIndexOfCallInSelectedVehicle);
            if (isSolutionFeasibleAfterSwappingTwoCalls(
                    newSolutionRepresentation, firstVehicleIsOutsourced, secondVehicleIsOutsourced, vehicleNumbers)) {
                counterA++;
                if (!feasible(newSolution)) {
                    counterB++;
                    numberOfTimesSolutionIsInfeasibleAfterRegularSwap++;
//                    return true;
                }
                return true;
            }
            // Swap back if solution is not feasible
            revertSwap(newSolution, firstIndexOfRandomCall, secondIndexOfRandomCall, indicesOfProcessedCalls,
                    firstRandomIndexOfCallInSelectedVehicle, secondRandomIndexOfCallInSelectedVehicle);
        }
        return false;
    }

    private static void revertSwap(IVectorSolutionRepresentation<Integer> newSolution, int firstIndexOfRandomCall,
                                   int secondIndexOfRandomCall, List<Integer> indicesOfProcessedCalls,
                                   int firstRandomIndexOfCallInSelectedVehicle,
                                   int secondRandomIndexOfCallInSelectedVehicle) {
        newSolution.swapElementsAtIndices(firstRandomIndexOfCallInSelectedVehicle, firstIndexOfRandomCall);
        newSolution.swapElementsAtIndices(secondRandomIndexOfCallInSelectedVehicle, secondIndexOfRandomCall);
        indicesOfProcessedCalls.add(firstRandomIndexOfCallInSelectedVehicle);
        indicesOfProcessedCalls.add(secondRandomIndexOfCallInSelectedVehicle);
    }

    private static int[] getVehicleNumbers(
            List<Integer> zeroIndices, int firstIndex, int secondIndex,
            boolean firstVehicleIsOutsourced, boolean secondVehicleIsOutsourced) {
        int[] vehicleNumbers = new int[2];
        if (!firstVehicleIsOutsourced) {
            vehicleNumbers[0] = findVehicleNumberForVehicleContainingIndex(firstIndex, zeroIndices);
        }
        if (!secondVehicleIsOutsourced) {
            vehicleNumbers[1] = findVehicleNumberForVehicleContainingIndex(secondIndex, zeroIndices);
        }
        return vehicleNumbers;
    }

    private static boolean isSolutionFeasibleAfterSwappingTwoCalls(
            List<Integer> newSolutionRepresentation, boolean firstVehicleIsOutsourced,
            boolean secondVehicleIsOutsourced, int[] vehicleNumbers) {
        boolean firstVehicleIsFeasible = true; // Outsourced vehicle is always feasible
        boolean secondVehicleIsFeasible = true;
        if (!firstVehicleIsOutsourced) {
            firstVehicleIsFeasible = constraintsHoldForVehicle(vehicleNumbers[0], newSolutionRepresentation);
        }
        if (!secondVehicleIsOutsourced) {
            secondVehicleIsFeasible = constraintsHoldForVehicle(vehicleNumbers[1], newSolutionRepresentation);
        }
        return firstVehicleIsFeasible && secondVehicleIsFeasible;
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
