package assignment2;

import assignment2.entities.Call;
import assignment2.entities.Journey;
import assignment2.entities.NodeTimesAndCosts;
import assignment2.entities.Vehicle;

import java.util.ArrayList;
import java.util.List;

public class PickupAndDelivery {


    public static void main(String[] args) {
        start();
    }

    private static void start() {
        String pathToFile1 = "src/main/resources/assignment2.test.data/Call_7_Vehicle_3.txt";
        String pathToFile2 = "src/main/resources/assignment2.test.data/Call_18_Vehicle_5.txt";
        String pathToFile3 = "src/main/resources/assignment2.test.data/Call_035_Vehicle_07.txt";
        String pathToFile4 = "src/main/resources/assignment2.test.data/Call_080_Vehicle_20.txt";
        String pathToFile5 = "src/main/resources/assignment2.test.data/Call_130_Vehicle_40.txt";
        processLines(getFileContents(pathToFile2));
        //calculateSolution();
    }

    private static List<String> getFileContents(String pathToFile1) {
        ReadFromFile fileReader = new ReadFromFile();
        List<String> lines = fileReader.readFile(pathToFile1);
        for (String line : lines) {
            System.out.println(line);
        }
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
}
