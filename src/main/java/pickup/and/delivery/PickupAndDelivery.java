package pickup.and.delivery;

import pickup.and.delivery.entities.Call;
import pickup.and.delivery.entities.Journey;
import pickup.and.delivery.entities.NodeTimesAndCosts;
import pickup.and.delivery.entities.Vehicle;
import solution.representations.vector.IVectorSolutionRepresentation;
import solution.representations.vector.VectorSolutionRepresentation;

import java.util.ArrayList;
import java.util.List;

public class PickupAndDelivery {


    public static void main(String[] args) {
        //start();
        runForNumberOfIterations(10);
    }

    private static void runForNumberOfIterations(int numberOfIterations) {
        int totalCost = 0;
        long totalTime = 0L;
        List<IVectorSolutionRepresentation<Integer>> solutions = new ArrayList<>();
        for (int i = 0; i < numberOfIterations; i++) {
            Long timerStart = System.currentTimeMillis();
            start();
            Long timerStop = System.currentTimeMillis();
            long result = (timerStop - timerStart);
            System.out.println("(timerStop - timerStart) = " + result);
            solutions.add(new VectorSolutionRepresentation<>(solutionRepresentation.getSolutionRepresentation()));
            totalCost += calculateCost(solutionRepresentation);
            totalTime += result;
        }
        System.out.println("totalCost = " + totalCost);
        System.out.println("totalTime = " + totalTime);
        totalCost = totalCost / numberOfIterations;
        totalTime = totalTime / numberOfIterations;
        System.out.println("totalCost avg = " + totalCost);
        System.out.println("totalTime avg = " + totalTime);
        System.out.println("solutions = " + solutions);
        int worstCost = calculateCost(createWorstSolution());
        System.out.println("worstCost = " + worstCost);
        int bestCost = worstCost;
        for (IVectorSolutionRepresentation<Integer> solution : solutions) {
            int cost = calculateCost(solution);
            if (cost < bestCost) {
                bestCost = cost;
            }
        }
        System.out.println("bestCost = " + bestCost);
        double improvementInPercent = (100.0 * (worstCost - bestCost) / worstCost);
        System.out.println("improvementInPercent = " + improvementInPercent);
    }

    private static IVectorSolutionRepresentation<Integer> solutionRepresentation;

    private static void start() {
        String pathToFile1 = "src/main/resources/assignment2.test.data/Call_7_Vehicle_3.txt";
        String pathToFile2 = "src/main/resources/assignment2.test.data/Call_18_Vehicle_5.txt";
        String pathToFile3 = "src/main/resources/assignment2.test.data/Call_035_Vehicle_07.txt";
        String pathToFile4 = "src/main/resources/assignment2.test.data/Call_080_Vehicle_20.txt";
        String pathToFile5 = "src/main/resources/assignment2.test.data/Call_130_Vehicle_40.txt";
        processLines(getFileContents(pathToFile5));
        int solutionSize = (2 * numberOfNodes) + numberOfVehicles;
        solutionRepresentation = new VectorSolutionRepresentation<>(solutionSize);
        calculateSolution();
    }

    private static List<String> getFileContents(String pathToFile1) {
        ReadFromFile fileReader = new ReadFromFile();
        List<String> lines = fileReader.readFile(pathToFile1);
     /*   for (String line : lines) {
            System.out.println(line);
        }*/
        return lines;
    }

    private static int numberOfNodes;
    private static int numberOfVehicles;
    private static int numberOfCalls;
    private static ArrayList<Vehicle> vehicles = new ArrayList<>();
    private static ArrayList<Call> calls = new ArrayList<>();
    private static ArrayList<Journey> possibleJourneys = new ArrayList<>();
    private static ArrayList<NodeTimesAndCosts> nodeTimesAndCosts = new ArrayList<>();

    private static void processLines(List<String> lines) {
        if (lines.isEmpty()) {
            System.out.println("Input had nothing to process");
        }
        int counter = 0;
        for (String line : lines) {
            if (line.startsWith("%")) {
                counter++;
                continue;
            }
            if (counter == 1) {
                numberOfNodes = Integer.parseInt(line);
            } else if (counter == 2) {
                numberOfVehicles = Integer.parseInt(line);
            } else if (counter == 3) {
                extractVehicle(line);
            } else if (counter == 4) {
                numberOfCalls = Integer.parseInt(line);
            } else if (counter == 5) {
                extractPossibleCalls(line);
            } else if (counter == 6) {
                extractCalls(line);
            } else if (counter == 7) {
                extractPossibleJourneys(line);
            } else if (counter == 8) {
                extractNodeTimesAndCosts(line);
            } else {
                System.out.println("What happened here?");
            }
        }
/*
        System.out.println("numberOfNodes = " + numberOfNodes);
        System.out.println("numberOfVehicles = " + numberOfVehicles);
        System.out.println("numberOfCalls = " + numberOfCalls);
       for (Vehicle vehicle : vehicles) {
            System.out.println("vehicle = " + vehicle);
        }
        for (Call call : calls) {
      //      System.out.println("call = " + call);
        }
        for (Journey journey : possibleJourneys) {
     //       System.out.println("journey = " + journey);
        }
        for (NodeTimesAndCosts nodeTimesAndCosts : nodeTimesAndCosts) {
     //       System.out.println("nodeTimesAndCosts = " + nodeTimesAndCosts);
        }*/
    }

    private static void extractNodeTimesAndCosts(String line) {
        String[] data = line.split(",");
        int vehicleIndex = Integer.parseInt(data[0]);
        int callIndex = Integer.parseInt(data[1]);
        int originNodeTime = Integer.parseInt(data[2]);
        int originNodeCost = Integer.parseInt(data[3]);
        int destinationNodeTime = Integer.parseInt(data[4]);
        int destinationNodeCost = Integer.parseInt(data[5]);
        nodeTimesAndCosts.add(new NodeTimesAndCosts(vehicleIndex, calls.get(callIndex - 1),
                originNodeTime, originNodeCost,
                destinationNodeTime, destinationNodeCost));
    }

    private static void extractPossibleJourneys(String line) {
        String[] data = line.split(",");
        int vehicleIndex = Integer.parseInt(data[0]);
        int origin = Integer.parseInt(data[1]);
        int destination = Integer.parseInt(data[2]);
        int travelTime = Integer.parseInt(data[3]);
        int travelCost = Integer.parseInt(data[4]);
        possibleJourneys.add(new Journey(vehicleIndex, origin, destination, travelTime, travelCost));
    }

    private static void extractPossibleCalls(String line) {
        String[] data = line.split(",");
        int vehicleIndex = Integer.parseInt(data[0]);
        Vehicle vehicle = vehicles.get(vehicleIndex - 1);
        for (int i = 1; i < data.length; i++) {
            int callIndex = Integer.parseInt(data[i]);
            vehicle.addPossibleCall(callIndex);
        }
    }

    private static void extractCalls(String line) {
        String[] data = line.split(",");
        int callIndex = Integer.parseInt(data[0]);
        int origin = Integer.parseInt(data[1]);
        int destination = Integer.parseInt(data[2]);
        int packageSize = Integer.parseInt(data[3]);
        int costOfFailure = Integer.parseInt(data[4]);
        int lowerTimePickup = Integer.parseInt(data[5]);
        int upperTimePickup = Integer.parseInt(data[6]);
        int lowerTimeDelivery = Integer.parseInt(data[7]);
        int upperTimeDelivery = Integer.parseInt(data[8]);
        calls.add(new Call(callIndex, origin, destination,
                packageSize, costOfFailure, lowerTimePickup,
                upperTimePickup, lowerTimeDelivery, upperTimeDelivery));
    }

    private static void extractVehicle(String line) {
        String[] data = line.split(",");
        int index = Integer.parseInt(data[0]);
        int homeNode = Integer.parseInt(data[1]);
        int startTime = Integer.parseInt(data[2]);
        int capacity = Integer.parseInt(data[3]);
        vehicles.add(new Vehicle(index, homeNode, startTime, capacity));
    }

    private static void calculateSolution() {
        // [0, 0, 0, 1, 1, 2, 2, 3, 3, 4, 4, 5, 5, 6, 6, 7, 7] = 3286422
        solutionRepresentation = createWorstSolution();
        System.out.println("worst solution cost: " + calculateCost(solutionRepresentation));

        solutionRepresentation = BlindRandomSearch.blindRandomSearch(solutionRepresentation);
        System.out.println("solutionRepresentation = " + solutionRepresentation);
        System.out.println(feasible(solutionRepresentation));
        System.out.println(calculateCost(solutionRepresentation));



        // [7, 7, 5, 5, 0, 0, 0, 6, 6] = 901763
  //      List<Integer> values = Arrays.asList(7, 7, 5, 5, 0, 0, 0, 6, 6);
    //    IVectorSolutionRepresentation<Integer> sol2 = new VectorSolutionRepresentation<>(values);
   //     System.out.println(calculateCost(sol2));

        // [1, 1, 0, 0, 0] = 253136 v
        // [2, 2, 0, 0, 0] = 123912 v
        // [3, 3, 0, 0, 0] = 78456 v
        // [4, 4, 0, 0, 0] = 33847 v
        // [5, 5, 0, 0, 0] = 239956 v
        // [6, 6, 0, 0, 0] = 158919 v
        // [7, 7, 0, 0, 0] = 87914 v
        // [0, 2, 2, 0, 0] = 91265 v
        // [0, 0, 2, 2, 0] = 114594 v
        // [0, 0, 0, 2, 2] = 430790 v
        // [1, 1, 0, 2, 2, 0, 3, 3, 0, 4, 4] = 738471 v
        // [1, 2, 1, 2, 0, 0, 0] = 309704 v false
        // [1, 1, 2, 2, 0, 0, 0] = 383431 v false
        // [1, 1, 2, 2, 3, 3, 0, 0, 0] = 580306 v false
        // [1, 1, 2, 2, 3, 3, 4, 4, 0, 0, 0] = 634306 v false
        // [1, 1, 2, 2, 3, 3, 4, 4, 5, 5, 0, 0, 0] = 872235 v false
        // [1, 1, 2, 2, 3, 3, 4, 4, 5, 5, 6, 6, 0, 0, 0] = 953849 v false
        // [1, 1, 2, 2, 3, 3, 4, 4, 5, 5, 6, 6, 7, 7, 0, 0, 0] = 1164367 v false
        // [7, 7, 5, 5, 0, 2, 2, 0, 3, 4, 4, 3, 1, 1, 0, 6, 6] = 1439159 v false
    /*    values = Arrays.asList(1, 7, 0, 1, 0, 6, 6, 0, 7, 3, 2, 5, 2, 4, 3, 4, 5);
        IVectorSolutionRepresentation<Integer> sol3 = new VectorSolutionRepresentation<>(values);
        System.out.println(feasible(sol3));
        System.out.println(calculateCost(sol3));
*/
    }

    /* Assumes that the given solution is valid */
    public static boolean feasible(IVectorSolutionRepresentation<Integer> solution) {
      //  System.out.println("Calculating feasibility for solution " + solution.toString());
        int currentLoad = 0;
        int currentMaxLoad = vehicles.get(0).getCapacity();
        int currentTime = vehicles.get(0).getStartingTimeInHours();
        int previousNode = vehicles.get(0).getHomeNode();
        int vehicleNumber = 1;
        List<Integer> unfinishedCalls = new ArrayList<>();
        List<Integer> visitedCalls = new ArrayList<>();
        for (Integer element : solution.getSolutionRepresentation()) {
            if (element == 0) {
                vehicleNumber++;
                currentLoad = 0;
                unfinishedCalls.clear();
                if (vehicleNumber <= numberOfVehicles) {
                    currentTime = vehicles.get(vehicleNumber - 1).getStartingTimeInHours();
                    previousNode = vehicles.get(vehicleNumber - 1).getHomeNode();
                }
            } else if (vehicleNumber == numberOfVehicles + 1) {
                break; // Outsourced jobs are always feasible. Becomes somebody else's problem anyway...
            } else {
                if (!visitedCalls.contains(element)) {
                    currentTime = getFirstVisitTimes(currentTime, vehicleNumber, element);
                    visitedCalls.add(element);
                }
                int destinationNode = calls.get(element - 1).getDestinationNode();
                if (unfinishedCalls.contains(calls.get(element - 1).getCallIndex())) {
                    currentTime = getTravelTime(currentTime, previousNode, vehicleNumber, destinationNode);
                    int lowerBoundTimeWindowForDelivery = calls.get(element - 1).getLowerBoundTimeWindowForDelivery();
                    int upperBoundTimeWindowForDelivery = calls.get(element - 1).getUpperBoundTimeWindowForDelivery();
                    if (currentTime > upperBoundTimeWindowForDelivery) {
                //        System.out.println("Time window exceeded");
                        return false;
                    }
                    if (currentTime < lowerBoundTimeWindowForDelivery) { // If too early, wait for delivery
                        currentTime += (lowerBoundTimeWindowForDelivery - currentTime);
                    }
                    currentLoad -= calls.get(element - 1).getPackageSize(); // Unload package
                    unfinishedCalls.remove((Integer) calls.get(element - 1).getCallIndex()); // Call has been finished
                } else {
                    if (!vehicles.get(vehicleNumber - 1).getPossibleCalls().contains(element)) {
                //        System.out.println("Incompatible vessel and cargo");
                        return false;
                    }
                    destinationNode = calls.get(element - 1).getOriginNode();
                    currentTime = getTravelTime(currentTime, previousNode, vehicleNumber, destinationNode);
                    int lowerBoundTimeWindowForPickup = calls.get(element - 1).getLowerBoundTimeWindowForPickup();
                    int upperBoundTimeWindowForPickup = calls.get(element - 1).getUpperBoundTimeWindowForPickup();
                    if (currentTime > upperBoundTimeWindowForPickup) {
                //        System.out.println("Time window exceeded");
                        return false;
                    }
                    if (currentTime < lowerBoundTimeWindowForPickup) { // If too early, wait for pickup
                        currentTime += (lowerBoundTimeWindowForPickup - currentTime);
                    }
                    currentLoad += calls.get(element - 1).getPackageSize();
                    if (currentLoad > currentMaxLoad) {
               //         System.out.println("Capacity exceeded");
                        return false;
                    }
                    unfinishedCalls.add(element);
                }
                previousNode = destinationNode;
            }
        }
       // System.out.println("Feasible");
        return true;
    }

    private static int getTravelTime(int currentTime, int previousNode, int vehicleNumber, int destinationNode) {
        for (Journey journey : possibleJourneys) {
            if (journey.getOriginNode() == previousNode
                    && journey.getDestinationNode() == destinationNode
                    && journey.getVehicleIndex() == vehicleNumber) {
                currentTime += journey.getTravelTime();
            }
        }
        return currentTime;
    }

    private static int getFirstVisitTimes(int currentTime, int vehicleNumber, Integer element) {
        for (NodeTimesAndCosts nodeTimesAndCosts : nodeTimesAndCosts) {
            if (nodeTimesAndCosts.getVehicleIndex() == vehicleNumber
                    && nodeTimesAndCosts.getCall().getCallIndex() == element) {
                currentTime += nodeTimesAndCosts.getOriginNodeTime();
                currentTime += nodeTimesAndCosts.getDestinationNodeTime();
                break;
            }
        }
        return currentTime;
    }

    /* Assumes that the given solution is valid */
    public static Integer calculateCost(IVectorSolutionRepresentation<Integer> solution) {
      //  System.out.println("Calculating cost for solution " + solution.toString());
        int totalCost = 0;
        int previousNode = vehicles.get(0).getHomeNode();
        int vehicleNumber = 1;
        List<Integer> unfinishedCalls = new ArrayList<>();
        List<Integer> visitedCalls = new ArrayList<>();
        for (Integer element : solution.getSolutionRepresentation()) {
            if (element == 0) {
                vehicleNumber++;
                unfinishedCalls.clear();
                if (vehicleNumber <= numberOfVehicles) {
                    previousNode = vehicles.get(vehicleNumber - 1).getHomeNode();
                }
            } else if (vehicleNumber == numberOfVehicles + 1) {
                totalCost = processOutsourcedCall(totalCost, unfinishedCalls, element);
            } else {
                if (!visitedCalls.contains(element)) {
                    totalCost = getFirstVisitCosts(totalCost, vehicleNumber, element);
                    visitedCalls.add(element);
                }
                int destinationNode = calls.get(element - 1).getDestinationNode();
                if (unfinishedCalls.contains(calls.get(element - 1).getCallIndex())) {
                    totalCost = getTravelCost(totalCost, previousNode, destinationNode, vehicleNumber);
                    unfinishedCalls.remove((Integer) calls.get(element - 1).getCallIndex()); // Call has been finished
                } else {
                    destinationNode = calls.get(element - 1).getOriginNode();
                    totalCost = getTravelCost(totalCost, previousNode, destinationNode, vehicleNumber);
                    unfinishedCalls.add(element);
                }
                previousNode = destinationNode;
            }
        }
        return totalCost;
    }

    private static int getFirstVisitCosts(int totalCost, int vehicleNumber, Integer element) {
        for (NodeTimesAndCosts nodeTimesAndCosts : nodeTimesAndCosts) {
            if (nodeTimesAndCosts.getVehicleIndex() == vehicleNumber
                    && nodeTimesAndCosts.getCall().getCallIndex() == element) {
                totalCost += nodeTimesAndCosts.getOriginNodeCosts();
                totalCost += nodeTimesAndCosts.getDestinationNodeCosts();
                break;
            }
        }
        return totalCost;
    }

    private static int getTravelCost(int totalCost, int previousNode, int destinationNode, int vehicleNumber) {
        int travelCost = getTravelCostForJourney(previousNode, destinationNode, vehicleNumber);
        if (travelCost == -1) {
            System.out.println("Something is not right here...");
        } else {
            totalCost += travelCost;
        }
        return totalCost;
    }

    private static int processOutsourcedCall(int totalCost, List<Integer> unfinishedCalls, Integer element) {
        if (unfinishedCalls.contains(element)) {
            totalCost += calls.get(element - 1).getCostOfNotTransporting();
        } else {
            unfinishedCalls.add(element);
        }
        return totalCost;
    }

    private static int getTravelCostForJourney(int originNode, int destinationNode, int vehicleIndex) {
        for (Journey journey : possibleJourneys) {
            if (journey.getOriginNode() == originNode
                    && journey.getDestinationNode() == destinationNode
                    && journey.getVehicleIndex() == vehicleIndex) {
                return journey.getTravelCost();
            }
        }
        System.out.println("No journey between " + originNode + " and "
                + destinationNode + " for vehicle " + vehicleIndex + " exists.");
        return -1;
    }

    private static IVectorSolutionRepresentation<Integer> createWorstSolution() {
        int solutionSize = numberOfVehicles + (2 * numberOfNodes);
        IVectorSolutionRepresentation<Integer> worstSolution =
                new VectorSolutionRepresentation<>(solutionSize);
        for (int i = 0; i < numberOfVehicles; i++) {
            worstSolution.getSolutionRepresentation().add(0);
        }
        for (int i = 1; i <= numberOfCalls; i++) { // Outsource all the calls
            worstSolution.getSolutionRepresentation().add(i);
            worstSolution.getSolutionRepresentation().add(i);
        }
        return worstSolution;
    }

    public static boolean isValidSolution(IVectorSolutionRepresentation<Integer> solution) {
        List<Integer> solutionRepresentation = solution.getSolutionRepresentation();
        if (solutionRepresentation.isEmpty()) {
            return false; // Empty is not a valid solution...
        }
        List<Integer> vehicleCalls = new ArrayList<>();
        for (Integer element : solutionRepresentation) {
            if (element == 0) {
                if (!vehicleCalls.isEmpty()) {
                    return false; // No vehicle should start a call without finishing it.
                }
            } else {
                if (vehicleCalls.contains(element)) {
                    vehicleCalls.remove(element);
                } else {
                    vehicleCalls.add(element);
                }
            }
        }
        return true;
    }
}
