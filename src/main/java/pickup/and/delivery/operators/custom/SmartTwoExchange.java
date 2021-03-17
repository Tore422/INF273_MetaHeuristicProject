package pickup.and.delivery.operators.custom;

import solution.representations.vector.IVectorSolutionRepresentation;
import solution.representations.vector.VectorSolutionRepresentation;

import java.util.List;

import static pickup.and.delivery.operators.OperatorUtilities.getIndicesOfAllZeroes;

public class SmartTwoExchange {

    public static void main(String[] args) {

    }

    public static IVectorSolutionRepresentation<Integer> useSmartTwoExchangeOnSolution(
            IVectorSolutionRepresentation<Integer> solution) {
        IVectorSolutionRepresentation<Integer> newSolution = new VectorSolutionRepresentation<>(
                solution.getSolutionRepresentation());
        List<Integer> newSolutionRepresentation = newSolution.getSolutionRepresentation();
        List<Integer> zeroIndices = getIndicesOfAllZeroes(newSolutionRepresentation);
        





    }







}
