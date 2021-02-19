package pickup.and.delivery.operators;

import solution.representations.vector.IVectorSolutionRepresentation;
import solution.representations.vector.VectorSolutionRepresentation;

import java.util.List;

public class ThreeExchange {

    public static IVectorSolutionRepresentation<Integer> useThreeExchangeOnSolution(IVectorSolutionRepresentation<Integer> solution) {
        List<Integer> sol = solution.getSolutionRepresentation();

        return new VectorSolutionRepresentation<>(sol);
    }
}
