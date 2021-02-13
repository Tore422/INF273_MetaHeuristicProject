package solution.representations.vector;

import solution.representations.ISolutionRepresentation;

import java.util.List;

public interface IVectorSolutionRepresentation<T> extends ISolutionRepresentation {

    /**
     * Get the solution representation.
     *
     * @return The solution representation as a List.
     */
    List<T> getSolutionRepresentation();

    /**
     * Get the number of elements in the solution representation.
     *
     * @return The size of the solution representation.
     */
    int solutionSize();

}
