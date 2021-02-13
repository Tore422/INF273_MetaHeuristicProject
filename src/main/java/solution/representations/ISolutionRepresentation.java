package solution.representations;

public interface ISolutionRepresentation {

    /**
     * Checks if the contained solution is valid?
     *
     * @return true if valid, false otherwise
     */
    boolean validate();

    /**
     * Shuffles the contents of the representation
     * to create a new permutation.
     */
    void randomize();
}
