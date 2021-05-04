package pickup.and.delivery.operators;

import pickup.and.delivery.PickupAndDelivery;
import pickup.and.delivery.entities.Call;
import pickup.and.delivery.entities.NodeTimesAndCosts;
import pickup.and.delivery.entities.Vehicle;
import solution.representations.vector.IVectorSolutionRepresentation;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static pickup.and.delivery.PickupAndDelivery.*;

public class OperatorUtilities {

    public static final int MINUS_ONE = -1;
    public static final Random RANDOM = new Random();

    private OperatorUtilities() {}

    public static List<Integer> findStartIndicesOfVehiclesThatCanTakeTheCall(
            List<Integer> zeroIndices, Integer callID) {
        List<Integer> startIndicesOfVehiclesThatCanTakeTheCall = new ArrayList<>();
        for (Vehicle vehicle : getVehicles()) {
            for (Integer possibleCall : vehicle.getPossibleCalls()) {
                if (possibleCall.equals(callID)) {
                    if (vehicle.getIndex() == 1) {
                        startIndicesOfVehiclesThatCanTakeTheCall.add(-1);
                    } else {
                        startIndicesOfVehiclesThatCanTakeTheCall.add(zeroIndices.get(vehicle.getIndex() - 2));
                    }
                }
            }
        }
        Integer startIndexOfOutsourcedCalls = zeroIndices.get(zeroIndices.size() - 1);
        startIndicesOfVehiclesThatCanTakeTheCall.add(startIndexOfOutsourcedCalls);
        return startIndicesOfVehiclesThatCanTakeTheCall;
    }

    public static int getSecondIndexOfCall(List<Integer> newSolutionRepresentation,
                                            List<Integer> zeroIndices,
                                            int firstIndexOfCall) {
        int secondIndexOfCall;
        int[] startAndStopIndexOfVehicle = findStartAndStopIndexOfVehicle(zeroIndices, firstIndexOfCall,
                newSolutionRepresentation.size());
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

    public static int[] findStartAndStopIndexOfVehicle(List<Integer> zeroIndices,
                                                       int indexOfCallInVehicleToFind,
                                                       int stopIndexOfOutsourcedCalls) {
        if (indexOfCallInVehicleToFind > stopIndexOfOutsourcedCalls || indexOfCallInVehicleToFind < 0) {
            throw new IllegalArgumentException("Index does not exist");
        }
        int[] startAndStopIndexOfVehicle = new int[2];
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
                if (zeroIndex >= indexOfCallInVehicleToFind) {
                    startAndStopIndexOfVehicle[0] = zeroIndices.get(i - 1);
                    startAndStopIndexOfVehicle[1] = zeroIndex;
                    break;
                }
            }
        }
        return startAndStopIndexOfVehicle;
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
    public static int findRandomIndexWithinExclusiveBounds(int lowerBound, int upperBound, List<Integer> exceptions) {
        if (upperBound <= lowerBound + 1) {
            throw new IllegalArgumentException("Given bound does not allow values in-between");
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
        return startAndStopIndicesOfVehiclesWithMoreThanOneCall;
    }

    /* Assumes valid solution */
    public static int computeCostForVehicle(int startIndex, int stopIndex, int vehicleNumber,
                                     List<Integer> solutionRepresentation) {
        int totalCost = 0;
        int currentNode = getVehicles().get(vehicleNumber - 1).getHomeNode();
        List<Call> unfinishedCalls = new ArrayList<>();
        for (int i = startIndex + 1; i < stopIndex; i++) {
            Call currentCall = getCalls().get(solutionRepresentation.get(i) - 1);
            NodeTimesAndCosts nodeTimesAndCosts = getNodeTimesAndCostsForVehicle(
                    vehicleNumber, currentCall.getCallIndex());
            int destinationNode;
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

    /**
     * Assumes that the solution has all calls in vehicles that can handle them.
     * Test for that might be added later...
     *
     * @param vehicleNumber
     * @param solutionRepresentation
     * @return true, if both time windows and capacity constraints
     * hold for the selected vehicle.
     */
    public static boolean constraintsHoldForVehicle(int vehicleNumber, List<Integer> solutionRepresentation) {
        List<Integer> zeroIndices = getIndicesOfAllZeroes(solutionRepresentation);
        if (vehicleNumber > zeroIndices.size()) {
            throw new IllegalArgumentException("That many vehicles does not exist");
        }
        int stopIndex = zeroIndices.get(vehicleNumber - 1);
        int startIndex = findStartIndex(solutionRepresentation, zeroIndices, stopIndex);
        boolean timeWindowConstraintHolds = timeWindowConstraintHoldsFor(
                startIndex, stopIndex, vehicleNumber, solutionRepresentation);
        boolean capacityConstraintHolds = vehicleCapacityConstraintHoldsFor(
                startIndex, stopIndex, vehicleNumber, solutionRepresentation);
        boolean compatibilityConstraintHolds = compatibilityConstraintHoldsFor(
                startIndex, stopIndex, vehicleNumber, solutionRepresentation);
        return timeWindowConstraintHolds && capacityConstraintHolds && compatibilityConstraintHolds;
    }

    public static boolean compatibilityConstraintHoldsFor(int startIndex, int stopIndex, int vehicleNumber,
                                                          List<Integer> solutionRepresentation) {
        Vehicle vehicle = PickupAndDelivery.getVehicles().get(vehicleNumber - 1);
        List<Integer> callsThatAreCompatibleWithTheVehicle = vehicle.getPossibleCalls();
        for (int i = (startIndex + 1); i < stopIndex; i++) {
            int element = solutionRepresentation.get(i);
            if (element != 0 && !callsThatAreCompatibleWithTheVehicle.contains(element)) {
                return false;
            }
        }
        return true;
    }

  public static boolean timeWindowConstraintHoldsFor(int startIndex, int stopIndex, int vehicleNumber,
                                                     List<Integer> solutionRepresentation) {
    /*  System.out.println("Checking time window constraint for vehicle: " + vehicleNumber);
      System.out.println("startIndex = " + startIndex);
      System.out.println("stopIndex = " + stopIndex);
      System.out.println("solutionRepresentation = " + solutionRepresentation);//*/
      Vehicle vehicle = getVehicles().get(vehicleNumber - 1);
      int currentTime = vehicle.getStartingTime();
      int currentNode = vehicle.getHomeNode();
      List<Call> unfinishedCalls = new ArrayList<>();
      for (int i = startIndex + 1; i < stopIndex; i++) {
          Call currentCall = getCalls().get(solutionRepresentation.get(i) - 1);
          NodeTimesAndCosts nodeTimesAndCosts = getNodeTimesAndCostsForVehicle(
                  vehicleNumber, currentCall.getCallIndex());
          int destinationNode = -1;
          if (unfinishedCalls.contains(currentCall)) {
              destinationNode = currentCall.getDestinationNode();
              currentTime = Math.max(currentTime + getTravelTime(currentNode, destinationNode, vehicleNumber),
                      currentCall.getLowerBoundTimeWindowForDelivery());
              if (currentTime > currentCall.getUpperBoundTimeWindowForDelivery()) {
            //      System.out.println("Time window exceeded for delivery");
                  return false;
              }
              currentTime += nodeTimesAndCosts.getDestinationNodeTime();
              unfinishedCalls.remove(currentCall);
          } else {
              destinationNode = currentCall.getOriginNode();
              currentTime = Math.max(currentTime + getTravelTime(currentNode, destinationNode, vehicleNumber),
                      currentCall.getLowerBoundTimeWindowForPickup());
              if (currentTime > currentCall.getUpperBoundTimeWindowForPickup()) {
              //    System.out.println("Time window exceeded for pickup");
                  return false;
              }
              currentTime += nodeTimesAndCosts.getOriginNodeTime();
              unfinishedCalls.add(currentCall);
          }
          currentNode = destinationNode;
      }
      return true;
    }


  public static boolean vehicleCapacityConstraintHoldsFor(int startIndex, int stopIndex, int vehicleNumber,
                                                          List<Integer> solutionRepresentation) {
      Vehicle vehicle = getVehicles().get(vehicleNumber - 1);
      int maxCapacity = vehicle.getCapacity();
      int currentLoad = 0;
      List<Call> unfinishedCalls = new ArrayList<>();
      for (int i = startIndex + 1; i < stopIndex; i++) {
          Call currentCall = getCalls().get(solutionRepresentation.get(i) - 1);
          if (unfinishedCalls.contains(currentCall)) {
              currentLoad -= currentCall.getPackageSize();
              unfinishedCalls.remove(currentCall);
          } else {
              currentLoad += currentCall.getPackageSize();
              if (currentLoad > maxCapacity) {
              //    System.out.println("Capacity exceeded");
                  return false;
              }
              unfinishedCalls.add(currentCall);
          }
      }
      return true;
    }


    public static int findVehicleNumberForVehicleContainingIndex(int elementIndex, List<Integer> zeroIndices) {
        int vehicleNumber = 1;
        for (int zeroIndex : zeroIndices) {
            if (zeroIndex > elementIndex) {
                return vehicleNumber;
            }
            vehicleNumber++;
        }
        if (zeroIndices.get(zeroIndices.size() - 1).equals(elementIndex)) {
            throw new IllegalArgumentException("Start index matches vehicle for outsourced calls");
        } else {
            throw new IllegalArgumentException("Given start index does not match for any vehicle");
        }
    }

    public static int findNumberOfDifferentCallsInVehicle(int startIndex, int stopIndex) {
        int numberOfDifferentCallsInVehicle = 0;
        if (startIndex == -1) {
            numberOfDifferentCallsInVehicle = stopIndex / 2;
        } else {
            numberOfDifferentCallsInVehicle = ((stopIndex - startIndex) - 1) / 2;
        }
        return numberOfDifferentCallsInVehicle;
    }

    /**
     * Expects the start index of a vehicle or an index in the vehicle,
     * and returns whether or not the index is part of an outsourced vehicle.
     *
     * @param solutionRepresentation
     * @param zeroIndices
     * @param index
     * @return true if the start index of the vehicle is equal to the
     * start index of the outsourced calls vehicle.
     */
    public static boolean isOutsourcedVehicle(List<Integer> solutionRepresentation, List<Integer> zeroIndices,
                                              int index) {
        int startIndexOfOutsourcedCalls = zeroIndices.get(zeroIndices.size() - 1);
        int startIndexOfSelectedVehicle = findStartIndex(solutionRepresentation, zeroIndices, index);
        return startIndexOfSelectedVehicle == startIndexOfOutsourcedCalls;
    }

    public static List<int[]> findPositionsWithinConstraints(
            int vehicleNumber, int callID, int[] startAndStopIndicesForVehicle, List<Integer> solutionRepresentation) {
        Vehicle vehicle = getVehicles().get(vehicleNumber - 1);
        NodeTimesAndCosts nodeTimesAndCostsForGivenCall = getNodeTimesAndCostsForVehicle(vehicleNumber, callID);
        Call givenCall = getCalls().get(callID - 1);
        int maxCapacity = vehicle.getCapacity();
        int currentTime = vehicle.getStartingTime();
        int currentLoad = 0;
        int currentNode = vehicle.getHomeNode();
        int startIndex = startAndStopIndicesForVehicle[0];
        int stopIndex = startAndStopIndicesForVehicle[1];
        List<Integer> validStartPositions = new ArrayList<>();
        List<Call> unfinishedCalls = new ArrayList<>();
     //   System.out.println("givenCall origin node = " + givenCall.getOriginNode());
     //   System.out.println("solutionRepresentation = " + solutionRepresentation);
        for (int i = startIndex + 1; i < stopIndex; i++) {
        //    System.out.println("i = " + i);
            Call currentCall = getCalls().get(solutionRepresentation.get(i) - 1);
            NodeTimesAndCosts nodeTimesAndCosts = getNodeTimesAndCostsForVehicle(
                    vehicleNumber, currentCall.getCallIndex());
            int destinationNode;
            if (unfinishedCalls.contains(currentCall)) {
            //    System.out.println("Delivery");
                destinationNode = currentCall.getDestinationNode();
                checkIfPossibleToVisitGivenCallPickupNodeBeforeCurrentDeliveryNode(
                        givenCall, nodeTimesAndCostsForGivenCall, validStartPositions, currentCall,
                        vehicleNumber, maxCapacity, currentTime, currentLoad,
                        currentNode, i, destinationNode);
                currentTime = Math.max(currentTime + getTravelTime(currentNode, destinationNode, vehicleNumber),
                        currentCall.getLowerBoundTimeWindowForDelivery());
                currentLoad -= givenCall.getPackageSize();
                currentTime += nodeTimesAndCosts.getDestinationNodeTime();
                unfinishedCalls.remove(currentCall);
            } else {
           //     System.out.println("Pickup");
                destinationNode = currentCall.getOriginNode();
                checkIfPossibleToVisitGivenCallPickupNodeBeforeCurrentPickupNode(
                        givenCall, nodeTimesAndCostsForGivenCall, validStartPositions, currentCall,
                        vehicleNumber, maxCapacity, currentTime, currentLoad, currentNode, i, destinationNode);
                currentTime = Math.max(currentTime + getTravelTime(currentNode, destinationNode, vehicleNumber),
                        currentCall.getLowerBoundTimeWindowForPickup());
                currentLoad += givenCall.getPackageSize();
                currentTime += nodeTimesAndCosts.getOriginNodeTime();
                unfinishedCalls.add(currentCall);
            }
          //  System.out.println("currentNode = " + currentNode);
          //  System.out.println("destinationNode = " + destinationNode);
            currentNode = destinationNode;
        }
        checkIfPossibleToVisitGivenCallAfterVisitingAllOtherCalls(givenCall, validStartPositions, stopIndex,
                vehicleNumber, maxCapacity, currentTime, currentLoad, currentNode);
        return findStartAndStopPositionsFulfillingConstraints(
                vehicleNumber, callID, solutionRepresentation, startIndex, stopIndex, validStartPositions);
    }

    private static List<int[]> findStartAndStopPositionsFulfillingConstraints(
            int vehicleNumber, int callID, List<Integer> solutionRepresentation,
            int startIndex, int stopIndex, List<Integer> validStartPositions) {
        List<int[]> validStartAndStopPositions = new ArrayList<>();
        stopIndex++; // Increase by one as we insert a start element in the vehicle
        for (int startPosition : validStartPositions) {
            List<Integer> copyOfSolutionRepresentation = new ArrayList<>(solutionRepresentation);
            copyOfSolutionRepresentation.add(startPosition, callID);
            for (int i = startPosition + 1; i <= stopIndex; i++) {
                copyOfSolutionRepresentation.add(i, callID);
                boolean timeWindowConstraintHolds = timeWindowConstraintHoldsFor(
                        startIndex, (stopIndex + 1), vehicleNumber, copyOfSolutionRepresentation);
                boolean vehicleCapacityConstraintHolds = vehicleCapacityConstraintHoldsFor(
                        startIndex, (stopIndex + 1), vehicleNumber, copyOfSolutionRepresentation);
                if (timeWindowConstraintHolds && vehicleCapacityConstraintHolds) {
                    int[] startAndStopIndices = new int[2];
                    startAndStopIndices[0] = startPosition;
                    startAndStopIndices[1] = i;
                    validStartAndStopPositions.add(startAndStopIndices);
                }
                copyOfSolutionRepresentation.remove(i);
            }
        }
        return validStartAndStopPositions;
    }

    private static void checkIfPossibleToVisitGivenCallAfterVisitingAllOtherCalls(
            Call givenCall, List<Integer> validStartPositions, int currentIndex,
            int vehicleNumber, int maxCapacity, int currentTime, int currentLoad, int currentNode) {
        int sizeOfPackage = givenCall.getPackageSize();
        int originNodeForGivenCall = givenCall.getOriginNode();
        int givenCallLowerBoundForPickup = givenCall.getLowerBoundTimeWindowForPickup();
        int givenCallUpperBoundForPickup = givenCall.getUpperBoundTimeWindowForPickup();
        int travelTimeFromPreviousNodeToGivenCall = getTravelTime(
                currentNode, originNodeForGivenCall, vehicleNumber);
        int alternativeCurrentTime = Math
                .max((currentTime + travelTimeFromPreviousNodeToGivenCall), givenCallLowerBoundForPickup);
        if (alternativeCurrentTime < givenCallUpperBoundForPickup
                && (currentLoad + sizeOfPackage) < maxCapacity) {
            validStartPositions.add(currentIndex); // Can visit given call before the destination call
        }
    }

    private static void checkIfPossibleToVisitGivenCallPickupNodeBeforeCurrentPickupNode(
            Call givenCall, NodeTimesAndCosts nodeTimesAndCostsForGivenCall,
            List<Integer> validStartPositions, Call currentCall, int vehicleNumber, int maxCapacity,
            int currentTime, int currentLoad, int currentNode, int currentIndex,  int destinationNode) {
        int sizeOfPackage = givenCall.getPackageSize();
        int originNodeForGivenCall = givenCall.getOriginNode();
        int givenCallLowerBoundForPickup = givenCall.getLowerBoundTimeWindowForPickup();
        int givenCallUpperBoundForPickup = givenCall.getUpperBoundTimeWindowForPickup();
        int pickupTimeForGivenCall = nodeTimesAndCostsForGivenCall.getOriginNodeTime();
        int travelTimeFromPreviousNodeToGivenCall = getTravelTime(
                currentNode, originNodeForGivenCall, vehicleNumber);
        int alternativeCurrentTime = Math
                .max((currentTime + travelTimeFromPreviousNodeToGivenCall), givenCallLowerBoundForPickup);
        if (alternativeCurrentTime < givenCallUpperBoundForPickup) {
            alternativeCurrentTime += pickupTimeForGivenCall;
            int travelTimeFromGivenCallOriginNodeToCurrentDestinationNode = getTravelTime(
                    originNodeForGivenCall, destinationNode, vehicleNumber);
            alternativeCurrentTime = Math
                    .max((alternativeCurrentTime + travelTimeFromGivenCallOriginNodeToCurrentDestinationNode),
                            currentCall.getLowerBoundTimeWindowForPickup());
            if (alternativeCurrentTime < currentCall.getUpperBoundTimeWindowForPickup()
                    && (currentLoad + sizeOfPackage) < maxCapacity) {
                validStartPositions.add(currentIndex); // Can visit given call before the destination call
            //    System.out.println("Could visit given call between nodes " + currentNode + " and " + destinationNode);
            }
        }
    }

    private static void checkIfPossibleToVisitGivenCallPickupNodeBeforeCurrentDeliveryNode(
            Call givenCall, NodeTimesAndCosts nodeTimesAndCostsForGivenCall,
            List<Integer> validStartPositions, Call currentCall, int vehicleNumber, int maxCapacity,
            int currentTime, int currentLoad, int currentNode, int currentIndex,  int destinationNode) {
        int sizeOfPackage = givenCall.getPackageSize();
        int originNodeForGivenCall = givenCall.getOriginNode();
        int givenCallLowerBoundForPickup = givenCall.getLowerBoundTimeWindowForPickup();
        int givenCallUpperBoundForPickup = givenCall.getUpperBoundTimeWindowForPickup();
        int pickupTimeForGivenCall = nodeTimesAndCostsForGivenCall.getOriginNodeTime();
        int travelTimeFromPreviousNodeToGivenCall = getTravelTime(
                currentNode, originNodeForGivenCall, vehicleNumber);
        int alternativeCurrentTime = Math
                .max((currentTime + travelTimeFromPreviousNodeToGivenCall), givenCallLowerBoundForPickup);
        if (alternativeCurrentTime < givenCallUpperBoundForPickup) {
            alternativeCurrentTime += pickupTimeForGivenCall;
            int travelTimeFromGivenCallOriginNodeToCurrentDestinationNode = getTravelTime(
                    originNodeForGivenCall, destinationNode, vehicleNumber);
            alternativeCurrentTime = Math
                    .max((alternativeCurrentTime + travelTimeFromGivenCallOriginNodeToCurrentDestinationNode),
                            currentCall.getLowerBoundTimeWindowForDelivery());
            if (alternativeCurrentTime < currentCall.getUpperBoundTimeWindowForDelivery()
                    && (currentLoad + sizeOfPackage) < maxCapacity) {
                validStartPositions.add(currentIndex); // Can visit given call before the destination call
            //    System.out.println("Could visit given call between nodes " + currentNode + " and " + destinationNode);
            }
        }
    }


    public static int[] findLowestCostOption(List<Integer> newSolutionRepresentation, int callId,
                                             int startIndex, int stopIndex, int vehicleNumber,
                                             List<int[]> candidatePositionsForInsertion) {
        int[] firstCandidatePositions = candidatePositionsForInsertion.get(0);
        int lowestCostSoFar = computeInitialCost(
                newSolutionRepresentation, firstCandidatePositions,
                startIndex, stopIndex, callId, vehicleNumber);
        int chosenPositionForPickup = firstCandidatePositions[0];
        int chosenPositionForDelivery = firstCandidatePositions[1];
      //  System.out.println("lowestCostSoFar = " + lowestCostSoFar);
        for (int[] positions : candidatePositionsForInsertion) {
            int firstPosition = positions[0];
            int secondPosition = positions[1];
            List<Integer> copyOfSolutionRepresentation = new ArrayList<>(newSolutionRepresentation);
            copyOfSolutionRepresentation.add(firstPosition, callId);
            copyOfSolutionRepresentation.add(secondPosition, callId);
            int cost = computeCostForVehicle(startIndex, stopIndex + 2,
                    vehicleNumber, copyOfSolutionRepresentation);
            if (cost < lowestCostSoFar) {
                chosenPositionForPickup = firstPosition;
                chosenPositionForDelivery = secondPosition;
                lowestCostSoFar = cost;
            }
        }
      /*  System.out.println("cost improvement = " + (a - lowestCostSoFar));
        System.out.println("chosenPositionForPickup = " + chosenPositionForPickup);
        System.out.println("chosenPositionForDelivery = " + chosenPositionForDelivery);*/
        int[] bestPositions = new int[2];
        bestPositions[0] = chosenPositionForPickup;
        bestPositions[1] = chosenPositionForDelivery;
     /*   System.out.println("chosenPositionForPickup = " + chosenPositionForPickup);
        System.out.println("chosenPositionForDelivery = " + chosenPositionForDelivery);
        System.out.println("cost = " + lowestCostSoFar);
        System.out.println("startIndex = " + startIndex);
        System.out.println("stopIndex = " + stopIndex);*/
        return bestPositions;
    }

    public static int computeInitialCost(List<Integer> newSolutionRepresentation, int[] candidatePositions,
                                         int startIndex, int stopIndex, int callId, int vehicleNumber) {
        List<Integer> copyOfSolutionRepresentation = new ArrayList<>(newSolutionRepresentation);
        copyOfSolutionRepresentation.add(candidatePositions[0], callId);
        copyOfSolutionRepresentation.add(candidatePositions[1], callId);
        return computeCostForVehicle(startIndex, stopIndex + 2,
                vehicleNumber, copyOfSolutionRepresentation);
    }

    public static int findStopIndex(List<Integer> solutionRepresentation, List<Integer> zeroIndices, int startIndex) {
        if (startIndex < -1 || startIndex >= solutionRepresentation.size()) {
            throw new IllegalArgumentException("Start index is outside of the solution");
        }
        int stopIndex = solutionRepresentation.size();
        for (int zeroIndex : zeroIndices) {
            if (zeroIndex > startIndex) {
                stopIndex = zeroIndex;
                break;
            }
        }
        return stopIndex;
    }

    public static int findStartIndex(List<Integer> solutionRepresentation, List<Integer> zeroIndices, int stopIndex) {
        if (stopIndex < 0 || stopIndex > solutionRepresentation.size()) {
            throw new IllegalArgumentException("Stop index is outside of the solution");
        }
        int startIndex = -1;
        for (int i = (zeroIndices.size() - 1); i >= 0; i--) {
            int zeroIndex = zeroIndices.get(i);
            if (zeroIndex < stopIndex) {
                startIndex = zeroIndex;
                break;
            }
        }
        return startIndex;
    }

    public static List<Integer> findEmptyVehicles(List<Integer> newSolutionRepresentation, List<Integer> zeroIndices) {
        List<Integer> startIndicesOfVehiclesWithoutAnyCalls = new ArrayList<>();
        if (zeroIndices.isEmpty()) {
            return startIndicesOfVehiclesWithoutAnyCalls;
        }
        // special case for first vehicle
        int firstZeroIndex = zeroIndices.get(0);
        if (firstZeroIndex == 0) {
            startIndicesOfVehiclesWithoutAnyCalls.add(-1);
        }
        for (int i = 0; i < zeroIndices.size() - 1; i++) {
            int startIndex = zeroIndices.get(i);
            int stopIndex = zeroIndices.get(i + 1);
            if ((stopIndex - startIndex) == 1) {
                startIndicesOfVehiclesWithoutAnyCalls.add(startIndex);
            }
        }
        // special case for outsourced calls
        int startIndexOfOutsourcedCalls = zeroIndices.get(zeroIndices.size() - 1);
        if (startIndexOfOutsourcedCalls == newSolutionRepresentation.size() - 1) {
            startIndicesOfVehiclesWithoutAnyCalls.add(startIndexOfOutsourcedCalls);
        }
        return startIndicesOfVehiclesWithoutAnyCalls;
    }

    /* Assumes that there are calls in vehicles, besides those outsourced */
    public static int[] findPositionsOfMostExpensiveCall(
            List<Integer> solutionRepresentation, List<Integer> zeroIndices) {
        int[] positionsOfCallWithLargestCost = new int[2];
        int largestCostDifference = -1;
        List<Integer> indicesToIgnore = new ArrayList<>(zeroIndices);
        int startIndexOfOutsourcedCalls = zeroIndices.get(zeroIndices.size() - 1);
        for (int i = 0; i < startIndexOfOutsourcedCalls; i++) {
            if (!indicesToIgnore.contains(i)) {
                List<Integer> copyOfSolutionRepresentation = new ArrayList<>(solutionRepresentation);
                int secondIndexOfCall = getSecondIndexOfCall(copyOfSolutionRepresentation, zeroIndices, i);
                int[] startAndStopIndexOfVehicle = findStartAndStopIndexOfVehicle(
                        zeroIndices, i, copyOfSolutionRepresentation.size());
                int vehicleNumber = findVehicleNumberForVehicleContainingIndex(
                        startAndStopIndexOfVehicle[0], zeroIndices);
                int oldCostForVehicle = computeCostForVehicle(
                        startAndStopIndexOfVehicle[0], startAndStopIndexOfVehicle[1], vehicleNumber,
                        copyOfSolutionRepresentation);
                copyOfSolutionRepresentation.remove(i);
                copyOfSolutionRepresentation.remove(secondIndexOfCall);
                int newCostForVehicle = computeCostForVehicle(
                        startAndStopIndexOfVehicle[0], (startAndStopIndexOfVehicle[1] - 2), vehicleNumber,
                        copyOfSolutionRepresentation);
                int costDifference = oldCostForVehicle - newCostForVehicle;
                if (costDifference > largestCostDifference) {
                    positionsOfCallWithLargestCost[0] = i;
                    positionsOfCallWithLargestCost[1] = secondIndexOfCall;
                    largestCostDifference = costDifference;
                }
                indicesToIgnore.add(i);
                indicesToIgnore.add(secondIndexOfCall);
            }
        }
        return positionsOfCallWithLargestCost;
    }

    private static final double PROBABILITY_OF_PICKING_MOVE_MOST_EXPENSIVE_CALL_APPROACH = 0.30;

    public static boolean decideRemovalOperator() {
        double selectedRemoveApproach = RANDOM.nextDouble();
        return selectedRemoveApproach < PROBABILITY_OF_PICKING_MOVE_MOST_EXPENSIVE_CALL_APPROACH;
    }

    public static int countNumberOfCallsInSolution(IVectorSolutionRepresentation<Integer> solution) {
        int numberOfCalls = 0;
        List<Integer> visitedCalls = new ArrayList<>();
        for (Integer element : solution.getSolutionRepresentation()) {
            if (element != 0 && !visitedCalls.contains(element)) {
                numberOfCalls++;
                visitedCalls.add(element);
            }
        }
        return numberOfCalls;
    }
}
