package pickup.and.delivery.algorithms;

import solution.representations.vector.IVectorSolutionRepresentation;
import solution.representations.vector.VectorSolutionRepresentation;

import java.util.ArrayList;
import java.util.List;

public class LocalSearch {

    public static IVectorSolutionRepresentation<Integer> localSearch(IVectorSolutionRepresentation<Integer> initialSolution) {
        List<Integer> solution = new ArrayList<>(initialSolution.getSolutionRepresentation());


        return new VectorSolutionRepresentation<>(solution);
    }


}
