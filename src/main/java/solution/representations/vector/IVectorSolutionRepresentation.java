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
    int getSolutionSize();

    /**
     * Swap the position of two elements in the solution representation.
     *
     * @param indexForElementA
     * @param indexForElementB
     */
    void swapElementsAtIndices(int indexForElementA, int indexForElementB);

    /**
     * Swap the position of three elements in the solution representation.<br>
     * Swaps elements so that the solution goes from [A, B, C] to [C, A, B].
     * @param indexForElementA
     * @param indexForElementB
     * @param indexForElementC
     */
    void swapThreeElements(int indexForElementA, int indexForElementB, int indexForElementC);
}
