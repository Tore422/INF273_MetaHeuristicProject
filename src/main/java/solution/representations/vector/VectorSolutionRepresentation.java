package solution.representations.vector;

import java.util.ArrayList;
import java.util.List;

public class VectorSolutionRepresentation<T> implements IVectorSolutionRepresentation<T> {

    private final List<T> solution;

    public VectorSolutionRepresentation(int sizeOfRepresentation) {
        this.solution = new ArrayList<>(sizeOfRepresentation);
    }

    /**
     * Construct solution representation from predefined values.
     *
     * @param values
     */
    public VectorSolutionRepresentation(List<T> values) {
        this.solution = values;
    }

    @Override
    public boolean validate() {
        return false;
    }

    @Override
    public void randomize() {

    }

    @Override
    public List<T> getSolutionRepresentation() {
        return solution;
    }

    @Override
    public int solutionSize() {
        return this.solution.size();
    }


    @Override
    public String toString() {
        return "VectorSolutionRepresentation{" +
                "solution=" + solution +
                '}';
    }
}
