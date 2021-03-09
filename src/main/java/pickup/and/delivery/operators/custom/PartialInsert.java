package pickup.and.delivery.operators.custom;

import solution.representations.vector.IVectorSolutionRepresentation;
import solution.representations.vector.VectorSolutionRepresentation;

import java.util.Arrays;
import java.util.List;

import static pickup.and.delivery.operators.OperatorUtilities.findStartIndexOfVehiclesWithMoreThanOneCall;
import static pickup.and.delivery.operators.OperatorUtilities.getIndicesOfAllZeroes;

public class PartialInsert {


    public static void main(String[] args) {
        List<Integer> values = Arrays.asList(7, 7, 5, 5, 0, 2, 2, 0, 3, 4, 4, 3, 1, 1, 0, 6, 6);
        IVectorSolutionRepresentation<Integer> sol = new VectorSolutionRepresentation<>(values);
        usePartialSwapOnSolution(sol);
    }


    public static IVectorSolutionRepresentation<Integer> usePartialSwapOnSolution(
            IVectorSolutionRepresentation<Integer> solution) {
        IVectorSolutionRepresentation<Integer> newSolution = new VectorSolutionRepresentation<>(
                solution.getSolutionRepresentation());
        List<Integer> newSolutionRepresentation = newSolution.getSolutionRepresentation();
        List<Integer> zeroIndices = getIndicesOfAllZeroes(newSolutionRepresentation);
        List<int[]> startIndicesOfVehiclesToProcess = findStartIndexOfVehiclesWithMoreThanOneCall(zeroIndices);







      //  int[] startAndStopIndexOfVehicle = new int[2];
        //int startIndexOfCurrentlySelectedVehicle, stopIndexOfCurrentlySelectedVehicle;















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
        return newSolution;
    }



}
