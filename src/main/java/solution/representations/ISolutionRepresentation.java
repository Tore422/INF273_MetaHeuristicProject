package solution.representations;

public interface ISolutionRepresentation {

    /**
     * Checks if the contained solution is valid.
     *
     * @param validationMethod The name of the validation method to use.
     * @return true if valid according to selected validation method,
     * false otherwise
     */
    boolean validate(String validationMethod);

    /**
     * Shuffles the contents of the representation
     * to create a new permutation.
     */
    void randomize();
}
