package pickup.and.delivery.operators.custom;

import pickup.and.delivery.operators.OperatorUtilities;
import solution.representations.vector.IVectorSolutionRepresentation;
import solution.representations.vector.VectorSolutionRepresentation;

import java.util.List;

public class SmartOneReinsert {

    public static void main(String[] args) {

    }

    public static IVectorSolutionRepresentation<Integer> useSmartOneReinsertOnSolution(
            IVectorSolutionRepresentation<Integer> solution) {
        IVectorSolutionRepresentation<Integer> newSolution = new VectorSolutionRepresentation<>(
                solution.getSolutionRepresentation());
        List<Integer> newSolutionRepresentation = newSolution.getSolutionRepresentation();
        List<Integer> zeroIndices = OperatorUtilities.getIndicesOfAllZeroes(newSolutionRepresentation);
        boolean solutionHasOutsourcedCalls =
                zeroIndices.get(zeroIndices.size() - 1) == newSolutionRepresentation.size();
        if (solutionHasOutsourcedCalls) {
            
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
