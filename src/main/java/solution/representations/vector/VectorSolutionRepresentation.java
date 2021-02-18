package solution.representations.vector;

import pickup.and.delivery.PickupAndDelivery;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

public class VectorSolutionRepresentation<T> implements IVectorSolutionRepresentation<T> {

    private final List<T> solution;

    public VectorSolutionRepresentation(int sizeOfRepresentation) {
        this.solution = new ArrayList<>(sizeOfRepresentation);
    }

    /**
     * Construct solution representation from predefined values.
     *
     * @param values The list of elements to add to
     *                  the constructed solution representation.
     */
    public VectorSolutionRepresentation(List<T> values) {
        this.solution = values;
    }

    @Override
    public boolean validate(String validationMethod) {
        boolean result = false;
        try {
            if (validationMethod.toUpperCase(Locale.ROOT).equals("PICKUP_AND_DELIVERY")) {
                result = PickupAndDelivery.isValidSolution((IVectorSolutionRepresentation<Integer>) this);
            } else {
                System.out.println("Did not recognize the validation method");
            }
        } catch (Exception e) {
            System.out.println("Failed to validate solution representation with given method");
        }
        return result;
    }

    @Override
    public void randomize() {
        Collections.shuffle(solution);
    }

    @Override
    public List<T> getSolutionRepresentation() {
        return solution;
    }

    @Override
    public int getSolutionSize() {
        return this.solution.size();
    }

    @Override
    public void swapElements(int indexForElementA, int indexForElementB) {
        if (indexForElementA < 0 || indexForElementA >= solution.size()
                || indexForElementB < 0 || indexForElementB >= solution.size()) {
            System.out.println("An index was pointing outside " +
                    "the solution representation. Did not swap the elements");
        } else {
            T elementA = solution.get(indexForElementA);
            T elementB = solution.get(indexForElementB);
            solution.remove(indexForElementA);
            solution.add(indexForElementA, elementB);
            solution.remove(indexForElementB);
            solution.add(indexForElementB, elementA);
        }
    }

    @Override
    public void swapThreeElements(int indexForElementA, int indexForElementB, int indexForElementC) {
        if (indexForElementA < 0 || indexForElementA >= solution.size()
                || indexForElementB < 0 || indexForElementB >= solution.size()
                || indexForElementC < 0 || indexForElementC >= solution.size()) {
            System.out.println("An index was pointing outside " +
                    "the solution representation. Did not swap the elements");
        } else {
            swapElements(indexForElementA, indexForElementB);
            swapElements(indexForElementA, indexForElementC);
        }
    }

    @Override
    public String toString() {
        return "VectorSolutionRepresentation{" +
                "solution=" + solution +
                '}';
    }
}
