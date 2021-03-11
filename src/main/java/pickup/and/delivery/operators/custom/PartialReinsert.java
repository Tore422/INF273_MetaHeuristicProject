package pickup.and.delivery.operators.custom;

import solution.representations.vector.IVectorSolutionRepresentation;
import solution.representations.vector.VectorSolutionRepresentation;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static pickup.and.delivery.operators.OperatorUtilities.*;

public class PartialReinsert {


    public static void main(String[] args) {
        List<Integer> values = Arrays.asList(7, 7, 5, 5, 0, 2, 2, 0, 3, 4, 4, 3, 1, 1, 0, 6, 6);
        IVectorSolutionRepresentation<Integer> sol = new VectorSolutionRepresentation<>(values);
        usePartialReinsertOnSolution(sol);
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

       // while (true) {
            int randomIndex = RANDOM.nextInt(indicesOfVehiclesToProcess.size());
            if (!vehiclesProcessedSoFar.contains(randomIndex)) {
                vehiclesProcessedSoFar.add(randomIndex);
                int[] startAndStopIndexOfVehicle = indicesOfVehiclesToProcess.get(randomIndex);
                System.out.println("startAndStopIndexOfVehicle = " + Arrays.toString(startAndStopIndexOfVehicle));
                int vehicleNumber = findVehicleNumberForVehicleStartingAtIndex(
                        startAndStopIndexOfVehicle[0], zeroIndices);
                System.out.println("vehicleNumber = " + vehicleNumber);
                int bestCostSoFarForCurrentVehicle = computeCostForVehicle(
                        startAndStopIndexOfVehicle[0], startAndStopIndexOfVehicle[1],
                        vehicleNumber, newSolutionRepresentation);
                System.out.println("bestCostSoFarForCurrentVehicle = " + bestCostSoFarForCurrentVehicle);



                // Process vehicle
           //     bestCostSoFarForCurrentVehicle = OperatorUtilities.findCostOfIndividualVehicle(startAndStopIndexOfVehicle);


               // if () {
                    //if finding improvement, we return

              //      return newSolution;
              //  }
                // otherwise we try a different vehicle.
            }
            if (vehiclesProcessedSoFar.size() >= indicesOfVehiclesToProcess.size()) {
                //Tried all possibilities without finding an improvement,
                //so we select random element from outsourced calls,
                //and insert that element into a random position there.


                return newSolution;
            }
       // }
    return newSolution;





/*
    from solution
    find zeroIndices
    Then find each vehicle with at least 2 different calls
    (excluding outsourced)
    and add start index to list.

    Pick a random vehicle, not outsourced, from that list.
    Calculate capacity, travel time and cost

    Calculate cost of current setup.
    Pick a random index within that vehicle,
    and try inserting that partial call in each position within the vehicle,
    and calculate the new cost.
    If capacity and time limitations are upheld,
    and the new cost is lower than previous best,
    set this index as the best found position so far.

    When finished, insert the partial call into the best found position,
    if no improvement was found, try selecting a different random index
    (blacklist previously tested indices)
    and repeat the process within the vehicle.

    If no improvement is possible, try selecting a different vehicle,
    (blacklisting previously tested vehicles)
    and repeat the process.

    If still no improvement is possible,
    randomly select a partial call within outsourced
    and insert into a different random position in outsourced.
    [May not matter that much, but simulated annealing having a chance of
    accepting a worse solution makes it possible to
    generate more of the possible solutions this way]

*/
    }

    private static IVectorSolutionRepresentation<Integer> randomlyReinsertPartOfAnOutsourcedCall(
            IVectorSolutionRepresentation<Integer> newSolution, List<Integer> zeroIndices) {
        // Select random element from outsourced calls,
        // and insert element into a random position there.
        List<Integer> newSolutionRepresentation = newSolution.getSolutionRepresentation();
        int startIndex = zeroIndices.get(zeroIndices.size() - 1);
        int stopIndex = newSolutionRepresentation.size();
        int randomIndex = findRandomIndexWithinVehicle(startIndex, stopIndex, null);
        System.out.println("randomIndex = " + randomIndex);
        int randomlySelectedCall = newSolutionRepresentation.remove(randomIndex);
        System.out.println("randomlySelectedCall = " + randomlySelectedCall);
        randomIndex = findRandomIndexWithinVehicle(startIndex, stopIndex - 1, null);
        System.out.println("newRandomIndex = " + randomIndex);
        newSolutionRepresentation.add(randomIndex, randomlySelectedCall);
        System.out.println("newSolutionRepresentation = " + newSolutionRepresentation);
        return newSolution;
    }
}
