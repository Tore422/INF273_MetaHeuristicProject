package pickup.and.delivery;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pickup.and.delivery.algorithms.BlindRandomSearch;
import pickup.and.delivery.algorithms.GeneralAdaptiveMetaheuristic;
import pickup.and.delivery.algorithms.LocalSearch;
import pickup.and.delivery.algorithms.SimulatedAnnealing;
import pickup.and.delivery.entities.Call;
import pickup.and.delivery.entities.Journey;
import pickup.and.delivery.entities.NodeTimesAndCosts;
import pickup.and.delivery.entities.Vehicle;
import solution.representations.vector.IVectorSolutionRepresentation;
import solution.representations.vector.VectorSolutionRepresentation;

import java.util.ArrayList;
import java.util.List;

public class PickupAndDelivery {
    private static final Logger log = LoggerFactory.getLogger(PickupAndDelivery.class);

    enum SearchAlgorithm {
        BLIND_RANDOM_SEARCH,
        LOCAL_SEARCH,
        SIMULATED_ANNEALING_SEARCH,
        ADAPTIVE_METAHEURISTIC_SEARCH
    }

    // Files for test data
    private static final String PATH_TO_FILE_1 = "src/main/resources/assignment2.test.data/Call_7_Vehicle_3.txt";
    private static final String PATH_TO_FILE_2 = "src/main/resources/assignment2.test.data/Call_18_Vehicle_5.txt";
    private static final String PATH_TO_FILE_3 = "src/main/resources/assignment2.test.data/Call_035_Vehicle_07.txt";
    private static final String PATH_TO_FILE_4 = "src/main/resources/assignment2.test.data/Call_080_Vehicle_20.txt";
    private static final String PATH_TO_FILE_5 = "src/main/resources/assignment2.test.data/Call_130_Vehicle_40.txt";
/*
    // Files for the exam
    private static final String PATH_TO_FILE_1 = "src/main/resources/exam.test.data/Call_7_Vehicle_3.txt";
    private static final String PATH_TO_FILE_2 = "src/main/resources/exam.test.data/Call_18_Vehicle_5.txt";
    private static final String PATH_TO_FILE_3 = "src/main/resources/exam.test.data/Call_035_Vehicle_07.txt";
    private static final String PATH_TO_FILE_4 = "src/main/resources/exam.test.data/Call_080_Vehicle_20.txt";
    private static final String PATH_TO_FILE_5 = "src/main/resources/exam.test.data/Call_130_Vehicle_40.txt";//*/

    public static void main(String[] args) {
        initialize(PATH_TO_FILE_5);
       // GeneralAdaptiveMetaheuristic.main(null);
        //System.out.println(createWorstSolution());
        //KReinsert.main(null);
      //  SmartOneReinsert.main(null);
        //SmartTwoExchange.main(null);
      //  PartialReinsert.main(null);

        final int NUMBER_OF_ITERATIONS = 10;
        runForNumberOfIterations(SearchAlgorithm.ADAPTIVE_METAHEURISTIC_SEARCH, NUMBER_OF_ITERATIONS);
       // runForNumberOfIterations(SearchAlgorithm.SIMULATED_ANNEALING_SEARCH, NUMBER_OF_ITERATIONS);
      //  runOnceForEachInputFile(SearchAlgorithm.ADAPTIVE_METAHEURISTIC_SEARCH);
    }

    private static void runOnceForEachInputFile(SearchAlgorithm algorithm) {
        final int ONE = 1;
        final String UNDERSCORE = "***********************************";
        long timerStart = System.currentTimeMillis();
        System.out.println("Computing solution for input file 1");
        System.out.println(UNDERSCORE);
        initialize(PATH_TO_FILE_1);
        runForNumberOfIterations(algorithm, ONE);
        System.out.println();
        System.out.println("Computing solution for input file 2");
        System.out.println(UNDERSCORE);
        initialize(PATH_TO_FILE_2);
        runForNumberOfIterations(algorithm, ONE);
        System.out.println();
        System.out.println("Computing solution for input file 3");
        System.out.println(UNDERSCORE);
        initialize(PATH_TO_FILE_3);
        runForNumberOfIterations(algorithm, ONE);
        System.out.println();
        System.out.println("Computing solution for input file 4");
        System.out.println(UNDERSCORE);
        initialize(PATH_TO_FILE_4);
        runForNumberOfIterations(algorithm, ONE);
        System.out.println();
        System.out.println("Computing solution for input file 5");
        System.out.println(UNDERSCORE);
        initialize(PATH_TO_FILE_5);
        runForNumberOfIterations(algorithm, ONE);
        long timerStop = System.currentTimeMillis();
        long result = (timerStop - timerStart);
        System.out.println();
        System.out.println("Total runtime = " + result);
    }

    private static void runForNumberOfIterations(SearchAlgorithm algorithm, int numberOfIterations) {
        int totalCost = 0;
        long totalTime = 0L;
        IVectorSolutionRepresentation<Integer> bestSolution = null;
        List<IVectorSolutionRepresentation<Integer>> solutions = new ArrayList<>();
        for (int i = 0; i < numberOfIterations; i++) {
            long timerStart = System.currentTimeMillis();
            calculateSolution(algorithm);
            long timerStop = System.currentTimeMillis();
            long result = (timerStop - timerStart);
            System.out.println("Time: " + result + " micro-seconds");
            solutions.add(new VectorSolutionRepresentation<>(solutionRepresentation.getSolutionRepresentation()));
            totalCost += calculateCost(solutionRepresentation);
            totalTime += result;
            if (bestSolution == null || calculateCost(solutionRepresentation) < calculateCost(bestSolution)) {
                bestSolution = new VectorSolutionRepresentation<>(solutionRepresentation.getSolutionRepresentation());
            }
        }
        System.out.println();
        System.out.println("SUMMARY");
        System.out.println("------------------------");
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
        System.out.println("bestSolution = " + bestSolution);
        if (worstCost <= 0) {
            log.error("WorstCost was zero or negative");
        } else {
            double improvementInPercent = (100.0 * (worstCost - bestCost) / worstCost);
            System.out.println("improvementInPercent = " + improvementInPercent);
        }
    }

    private static int numberOfNodes;
    private static int numberOfVehicles;
    private static int numberOfCalls;
    private static List<Vehicle> vehicles;
    private static List<Call> calls;
    private static List<Journey> possibleJourneys;
    private static List<NodeTimesAndCosts> nodeTimesAndCosts;
    private static IVectorSolutionRepresentation<Integer> solutionRepresentation;

    private static void initialize(String pathToFile) {
        vehicles = new ArrayList<>();
        calls = new ArrayList<>();
        possibleJourneys = new ArrayList<>();
        nodeTimesAndCosts = new ArrayList<>();
        processLines(getFileContents(pathToFile));
        sortInputVehicles();
        sortInputCalls();
        sortInputNodeTimesAndCosts();
        sortInputPossibleJourneys();
    }

    private static List<String> getFileContents(String pathToFile) {
        ReadFromFile fileReader = new ReadFromFile();
        return fileReader.readFile(pathToFile);
    }

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
            if (counter < 7) {
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
                } else {
                    log.error("What happened here? Got unexpected input data");
                }
            } else {
                if (counter == 7) {
                    extractPossibleJourneys(line);
                } else if (counter == 8) {
                    extractNodeTimesAndCosts(line);
                } else {
                    log.error("What happened here? Got unexpected input data");
                }
            }
        }
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

    // Sorting methods in case of unsorted input data
    private static void sortInputVehicles() {
        ArrayList<Vehicle> sortedVehicles = new ArrayList<>(vehicles);
        for (Vehicle vehicle : vehicles) {
            int index = vehicle.getIndex() - 1;
            sortedVehicles.set(index, vehicle);
        }
        vehicles = sortedVehicles;
    }

    private static void sortInputCalls() {
        ArrayList<Call> sortedCalls = new ArrayList<>(calls);
        for (Call call : calls) {
            int index = call.getCallIndex() - 1;
            sortedCalls.set(index, call);
        }
        calls = sortedCalls;
    }

    private static void sortInputPossibleJourneys() {
        ArrayList<Journey> sortedPossibleJourneys = new ArrayList<>(possibleJourneys);
        for (Journey journey : possibleJourneys) {
            int originNode = journey.getOriginNode();
            int destinationNode = journey.getDestinationNode();
            int vehicleNumber = journey.getVehicleIndex();
            int index = (numberOfVehicles * numberOfNodes * (originNode - 1))
                    + (numberOfVehicles * destinationNode) - (numberOfVehicles - vehicleNumber) - 1;
            sortedPossibleJourneys.set(index, journey);
        }
        possibleJourneys = sortedPossibleJourneys;
    }

    private static void sortInputNodeTimesAndCosts() {
        ArrayList<NodeTimesAndCosts> sortedNodeTimesAndCosts = new ArrayList<>(nodeTimesAndCosts);
        for (NodeTimesAndCosts nodeTimesAndCosts : nodeTimesAndCosts) {
            int vehicleNumber = nodeTimesAndCosts.getVehicleIndex();
            int callID = nodeTimesAndCosts.getCall().getCallIndex();
            int index = ((vehicleNumber - 1) * numberOfCalls) + callID - 1;
            sortedNodeTimesAndCosts.set(index, nodeTimesAndCosts);
        }
        nodeTimesAndCosts = sortedNodeTimesAndCosts;
    }

    private static void calculateSolution(SearchAlgorithm algorithm) {
        // [0, 0, 0, 1, 1, 2, 2, 3, 3, 4, 4, 5, 5, 6, 6, 7, 7] = 3286422
        solutionRepresentation = createWorstSolution();

        switch (algorithm) {
            case BLIND_RANDOM_SEARCH ->
                    useBlindRandomSearchOnSolution();
            case LOCAL_SEARCH ->
                    useLocalSearchOnSolution();
            case SIMULATED_ANNEALING_SEARCH ->
                    useSimulatedAnnealingOnSolution();
            case ADAPTIVE_METAHEURISTIC_SEARCH ->
                    useAdaptiveMetaheuristicOnSolution();
            default -> log.error("Did not recognize the selected algorithm");
        }
        /*if (algorithm != SearchAlgorithm.LOCAL_SEARCH) {
            // System.out.println("cost before = " + calculateCost(solutionRepresentation));
             useLocalSearchOnSolution(); //TODO: uncomment before final delivery. Improves solution quite a bit.
        }//*/
        System.out.println("SolutionRepresentation = " + solutionRepresentation);
        System.out.println("Feasible = " + feasible(solutionRepresentation));
        System.out.println("Solution cost = " + calculateCost(solutionRepresentation));


        // [7, 7, 5, 5, 0, 0, 0, 6, 6] = 901763
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

    private static void useBlindRandomSearchOnSolution() {
        System.out.println("Blind Random search");
        solutionRepresentation = BlindRandomSearch.blindRandomSearch(solutionRepresentation);
    }

    private static void useLocalSearchOnSolution() {
        System.out.println("\nLocal search");
        solutionRepresentation = LocalSearch.localSearch(solutionRepresentation);
    }

    private static void useSimulatedAnnealingOnSolution() {
        System.out.println("\nSimulated annealing search");
        solutionRepresentation = SimulatedAnnealing.simulatedAnnealingSearch(solutionRepresentation);
    }

    private static void useAdaptiveMetaheuristicOnSolution() {
        System.out.println("\nAdaptive Metaheuristic search");
        solutionRepresentation = GeneralAdaptiveMetaheuristic.adaptiveMetaheuristicSearch(solutionRepresentation);
    }

    /* Assumes that the given solution is valid */
    public static boolean feasible(IVectorSolutionRepresentation<Integer> solution) {
        int currentLoad = 0;
        int currentMaxLoad = vehicles.get(0).getCapacity();
        int currentTime = vehicles.get(0).getStartingTime();
        int previousNode = vehicles.get(0).getHomeNode();
        int vehicleNumber = 1;
        List<Call> unfinishedCalls = new ArrayList<>();
        for (Integer element : solution.getSolutionRepresentation()) {
            if (element == 0) {
                vehicleNumber++;
                currentLoad = 0;
                if (!unfinishedCalls.isEmpty()) {
                    log.error("Solution has unfinished calls and is therefore not valid");
                    return false;
                }
                if (vehicleNumber <= numberOfVehicles) {
                    currentTime = vehicles.get(vehicleNumber - 1).getStartingTime();
                    previousNode = vehicles.get(vehicleNumber - 1).getHomeNode();
                    currentMaxLoad = vehicles.get(vehicleNumber - 1).getCapacity();
                }
            } else if (vehicleNumber == numberOfVehicles + 1) {
                break; // Outsourced jobs are always feasible. Becomes somebody else's problem anyway...
            } else {
                Call call = calls.get(element - 1);
                NodeTimesAndCosts nodeTimesAndCosts = getNodeTimesAndCostsForVehicle(
                        vehicleNumber, call.getCallIndex());
                int destinationNode;
                if (unfinishedCalls.contains(call)) { // Deliver package
                    destinationNode = call.getDestinationNode();
                    int travelTime = getTravelTime(previousNode, destinationNode, vehicleNumber);
                    int lowerBoundTimeWindowForDelivery = call.getLowerBoundTimeWindowForDelivery();
                    int upperBoundTimeWindowForDelivery = call.getUpperBoundTimeWindowForDelivery();
                    currentTime = Math.max(currentTime + travelTime, lowerBoundTimeWindowForDelivery);
                    if (currentTime > upperBoundTimeWindowForDelivery) {
                        //log.error("Time window exceeded for delivery");
                        return false;
                    }
                    currentLoad -= call.getPackageSize(); // Unload package
                    unfinishedCalls.remove(call); // Call has been finished
                    currentTime += nodeTimesAndCosts.getDestinationNodeTime(); // Add time cost for delivering package
                } else { // Pickup package
                    if (!vehicles.get(vehicleNumber - 1).getPossibleCalls().contains(element)) {
                        //log.error("Incompatible vessel and cargo");
                        return false;
                    }
                    destinationNode = call.getOriginNode();
                    int travelTime = getTravelTime(previousNode, destinationNode, vehicleNumber);
                    int lowerBoundTimeWindowForPickup = call.getLowerBoundTimeWindowForPickup();
                    int upperBoundTimeWindowForPickup = call.getUpperBoundTimeWindowForPickup();
                    currentTime = Math.max(currentTime + travelTime, lowerBoundTimeWindowForPickup);
                    if (currentTime > upperBoundTimeWindowForPickup) {
                        //log.error("Time window exceeded for pickup");
                        return false;
                    }
                    currentLoad += call.getPackageSize();
                    if (currentLoad > currentMaxLoad) {
                        //log.error("Capacity exceeded");
                        return false;
                    }
                    unfinishedCalls.add(call);
                    currentTime += nodeTimesAndCosts.getOriginNodeTime(); // Add time cost for picking up package
                }
                previousNode = destinationNode;
            }
        }
        // System.out.println("Feasible");
        return true;
    }


   // private static long timeSpentLookingWithLoop1 = 0;
  //  private static long timeSpentLookingWithIndex1 = 0;

    public static int getTravelTime(int previousNode, int destinationNode, int vehicleNumber) {
  //      Long timerStart = System.currentTimeMillis();
        int indexOfJourney = (numberOfVehicles * numberOfNodes * (previousNode - 1))
                + (numberOfVehicles * destinationNode) - (numberOfVehicles - vehicleNumber) - 1;
        return possibleJourneys.get(indexOfJourney).getTravelTime();
        //    System.out.println(possibleJourneys.get(indexOfJourney));
  //      Long timerStop = System.currentTimeMillis();
  //      timeSpentLookingWithIndex1 += (timerStop - timerStart);
/*
        timerStart = System.currentTimeMillis();
        for (Journey journey : possibleJourneys) {
            if (journey.getOriginNode() == previousNode
                    && journey.getDestinationNode() == destinationNode
                    && journey.getVehicleIndex() == vehicleNumber) {
                //     System.out.println("journey = " + journey);
                timerStop = System.currentTimeMillis();
                timeSpentLookingWithLoop1 += (timerStop - timerStart);
                currentTime += journey.getTravelTime();
            }
        }
        return currentTime;
        */
    }

    public static NodeTimesAndCosts getNodeTimesAndCostsForVehicle(int vehicleNumber, Integer callID) {
        int nodeTimesAndCostsIndex = ((vehicleNumber - 1) * numberOfCalls) + callID - 1;
        return nodeTimesAndCosts.get(nodeTimesAndCostsIndex);

 /*       for (NodeTimesAndCosts nodeTimesAndCosts : nodeTimesAndCosts) {
            if (nodeTimesAndCosts.getVehicleIndex() == vehicleNumber
                    && nodeTimesAndCosts.getCall().getCallIndex() == callID) {
                return nodeTimesAndCosts;
            }
        }
        throw new IllegalStateException("No node times and costs entry for given vehicle");*/
    }

    /* Assumes that the given solution is valid */
    public static Integer calculateCost(IVectorSolutionRepresentation<Integer> solution) {
        int totalCost = 0;
        int previousNode = vehicles.get(0).getHomeNode();
        int vehicleNumber = 1;
        List<Call> unfinishedCalls = new ArrayList<>();
        for (Integer element : solution.getSolutionRepresentation()) {
            if (element == 0) {
                vehicleNumber++;
                if (vehicleNumber <= numberOfVehicles) {
                    previousNode = vehicles.get(vehicleNumber - 1).getHomeNode();
                }
            } else if (vehicleNumber == numberOfVehicles + 1) {
                totalCost = processOutsourcedCall(totalCost, unfinishedCalls, calls.get(element - 1));
            } else {
                Call call = calls.get(element - 1);
                int destinationNode;
                if (unfinishedCalls.contains(call)) {
                    destinationNode = call.getDestinationNode();
                    totalCost = processDeliveryCosts(totalCost, previousNode, vehicleNumber, element, destinationNode);
                    unfinishedCalls.remove(call); // Call has been finished
                } else {
                    destinationNode = call.getOriginNode();
                    totalCost = processPickupCosts(totalCost, previousNode, vehicleNumber, element, destinationNode);
                    unfinishedCalls.add(call);
                }
                previousNode = destinationNode;
            }
        }
        return totalCost;
    }

    private static int processDeliveryCosts(int totalCost, int previousNode, int vehicleNumber,
                                            int callId, int destinationNode) {
        totalCost += getTravelCostForJourney(previousNode, destinationNode, vehicleNumber);
        totalCost += getNodeTimesAndCostsForVehicle(vehicleNumber, callId)
                .getDestinationNodeCosts(); // Cost of delivering package
        return totalCost;
    }

    private static int processPickupCosts(int totalCost, int previousNode, int vehicleNumber,
                                          int callId, int destinationNode) {
        totalCost += getTravelCostForJourney(previousNode, destinationNode, vehicleNumber);
        totalCost += getNodeTimesAndCostsForVehicle(vehicleNumber, callId)
                .getOriginNodeCosts(); // Cost of picking up package
        return totalCost;
    }

    private static int processOutsourcedCall(int totalCost, List<Call> unfinishedCalls, Call element) {
        if (unfinishedCalls.contains(element)) {
            totalCost += element.getCostOfNotTransporting();
        } else {
            unfinishedCalls.add(element);
        }
        return totalCost;
    }

  //  private static long timeLookingWithLoop2 = 0;
  //  private static long timeLookingWithIndex2 = 0;

    public static int getTravelCostForJourney(int originNode, int destinationNode, int vehicleIndex) {
   //     Long timerStart = System.currentTimeMillis();
        int indexOfJourney = (numberOfVehicles * numberOfNodes * (originNode - 1))
                + (numberOfVehicles * destinationNode) - (numberOfVehicles - vehicleIndex) - 1;
        return possibleJourneys.get(indexOfJourney).getTravelCost();
        /*
    //    System.out.println(possibleJourneys.get(indexOfJourney));
        Long timerStop = System.currentTimeMillis();
        timeLookingWithIndex2 += (timerStop - timerStart);

        timerStart = System.currentTimeMillis();
        for (Journey journey : possibleJourneys) {
            if (journey.getOriginNode() == originNode
                    && journey.getDestinationNode() == destinationNode
                    && journey.getVehicleIndex() == vehicleIndex) {
           //     System.out.println("journey = " + journey);
                timerStop = System.currentTimeMillis();
                timeLookingWithLoop2 += (timerStop - timerStart);
                return journey.getTravelCost();
            }
        }
        throw new IllegalStateException("No journey between " + originNode + " and "
                + destinationNode + " for vehicle " + vehicleIndex + " exists.");*/
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

    public static List<Vehicle> getVehicles() {
        return vehicles;
    }

    public static List<Call> getCalls() {
        return calls;
    }
}
