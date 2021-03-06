package pickup.and.delivery.operators.original;

import solution.representations.vector.IVectorSolutionRepresentation;
import solution.representations.vector.VectorSolutionRepresentation;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static pickup.and.delivery.operators.OperatorUtilities.*;

public class OneReinsert {

    private OneReinsert() {
        throw new IllegalStateException("Utility class");
    }

    public static void main(String[] args) {
       // List<Integer> values = Arrays.asList(7, 7, 5, 5, 0, 0, 0, 6, 6);
        List<Integer> values = Arrays.asList(7, 7, 5, 5, 0, 2, 2, 0, 3, 4, 4, 3, 1, 1, 0, 6, 6);
        IVectorSolutionRepresentation<Integer> sol = new VectorSolutionRepresentation<>(values);
        useOneReinsertOnSolution(sol);
    }

    public static IVectorSolutionRepresentation<Integer> useOneReinsertOnSolution(IVectorSolutionRepresentation<Integer> solution) {
        if (solution == null || solution.getSolutionRepresentation().isEmpty()) {
            throw new IllegalArgumentException("Solution was empty/nonexistent.");
        }
        IVectorSolutionRepresentation<Integer> newSolution = new VectorSolutionRepresentation<>(
                solution.getSolutionRepresentation());
        List<Integer> newSolutionRepresentation = newSolution.getSolutionRepresentation();
        List<Integer> zeroIndices = getIndicesOfAllZeroes(newSolutionRepresentation);
        int firstIndexOfCall = findRandomIndexWithinExclusiveBounds(
                MINUS_ONE, newSolutionRepresentation.size(), zeroIndices);
        int secondIndexOfCall = getSecondIndexOfCall(
                newSolutionRepresentation, zeroIndices, firstIndexOfCall);
        int firstPartOfCallToReinsert = newSolutionRepresentation.remove(firstIndexOfCall);
        if (secondIndexOfCall > firstIndexOfCall) {
            secondIndexOfCall--;
        }
        int secondPartOfCallToReinsert = newSolutionRepresentation.remove(secondIndexOfCall);
        //System.out.println("solution = " + newSolution);
        //System.out.println("zeroIndices old = " + zeroIndices);
        zeroIndices = getIndicesOfAllZeroes(newSolutionRepresentation); // Updating after having removed the call
        //System.out.println("zeroIndices new = " + zeroIndices);
        //System.out.println("firstPartOfCallToReinsert = " + firstPartOfCallToReinsert);
        //System.out.println("secondPartOfCallToReinsert = " + secondPartOfCallToReinsert);
        List<Integer> startIndicesOfVehiclesThatCanTakeTheCall = findStartIndicesOfVehiclesThatCanTakeTheCall(
                zeroIndices, firstPartOfCallToReinsert);
        int randomStartIndex = RANDOM.nextInt(startIndicesOfVehiclesThatCanTakeTheCall.size());
        int startIndexOfVehicle = startIndicesOfVehiclesThatCanTakeTheCall.get(randomStartIndex);
        SolutionWithElementsToInsert solutionWithElementsToInsert = new SolutionWithElementsToInsert(
                newSolution, firstPartOfCallToReinsert, secondPartOfCallToReinsert);
        int stopIndexOfVehicle = findStopIndex(newSolutionRepresentation, zeroIndices, startIndexOfVehicle);
        if (startIndexOfVehicle == newSolutionRepresentation.size() - 1) {
            return getSolutionWhenStartingAtEmptyOutsourcedCallsVehicle(solutionWithElementsToInsert);
        } else if (randomStartIndex == startIndicesOfVehiclesThatCanTakeTheCall.size() - 1) {
            return solutionWhenMovingOutsourcedCalls(solutionWithElementsToInsert, startIndexOfVehicle);
        } else if (stopIndexOfVehicle == startIndexOfVehicle + 1) {
            return getSolutionWhenInsertingIntoEmptyVehicle(
                    solutionWithElementsToInsert, stopIndexOfVehicle);
        } else {
            return getGeneralSolutionWhenInsertingIntoAVehicle(
                    solutionWithElementsToInsert, startIndexOfVehicle, stopIndexOfVehicle);
        }
/*
    [7, 7, 5, 5, 0, 2, 2, 0, 3, 4, 4, 3, 1, 1, 0, 6, 6]
    pick randomly call 1
    [7, 7, 5, 5, 0, 2, 2, 0, 3, 4, 4, 3, 0, 6, 6]
    zeroes: [4, 7, 12]
    vehicles that can handle call 1: [1, 2, 3]
    contains vehicle 1, so adding -1 to new list.
    adding start index of outsourced calls (12) to new list.
    [-1, 4, 7, 12]
    pick random value 2 from array size
    random value is not array size - 1, so
    vehicle start at 7, end at 12.
    pick random index 9, between 8 and 12


    pick random value 0 from array size
    random value is not array size - 1, so
    vehicle start at -1, end at 4.


    pick random value 3 from array size
    random value is equal to the array size - 1, so
    vehicle start at 12, end at solution representation size.


 */
    }

    private static IVectorSolutionRepresentation<Integer> getGeneralSolutionWhenInsertingIntoAVehicle(
            SolutionWithElementsToInsert solutionWithElementsToInsert,
            int startIndexOfVehicle, int stopIndexOfVehicle) {
        IVectorSolutionRepresentation<Integer> newSolution = solutionWithElementsToInsert.solution;
        List<Integer> newSolutionRepresentation = newSolution.getSolutionRepresentation();
        Integer firstPartOfCallToReinsert = solutionWithElementsToInsert.firstPartOfCallToReinsert;
        Integer secondPartOfCallToReinsert = solutionWithElementsToInsert.secondPartOfCallToReinsert;


        int firstRandomIndexWithinVehicle = findRandomIndexWithinExclusiveBounds(
                startIndexOfVehicle, stopIndexOfVehicle + 1, null);
        //     System.out.println("randomIndexOne = " + firstRandomIndexWithinVehicle);
        newSolutionRepresentation.add(firstRandomIndexWithinVehicle, firstPartOfCallToReinsert);
        //     System.out.println("newSolutionRepresentation = " + newSolutionRepresentation);
        int secondRandomIndexWithinVehicle = findRandomIndexWithinExclusiveBounds(
                startIndexOfVehicle, stopIndexOfVehicle + 2, null);
        //     System.out.println("randomIndexTwo = " + secondRandomIndexWithinVehicle);
        newSolutionRepresentation.add(secondRandomIndexWithinVehicle, secondPartOfCallToReinsert);
        //     System.out.println("newSolutionRepresentation = " + newSolutionRepresentation);
        return newSolution;
    }

    private static IVectorSolutionRepresentation<Integer> getSolutionWhenInsertingIntoEmptyVehicle(
            SolutionWithElementsToInsert solutionWithElementsToInsert, int stopIndexOfVehicle) {
        IVectorSolutionRepresentation<Integer> newSolution = solutionWithElementsToInsert.solution;
        List<Integer> newSolutionRepresentation = newSolution.getSolutionRepresentation();
        Integer firstPartOfCallToReinsert = solutionWithElementsToInsert.firstPartOfCallToReinsert;
        Integer secondPartOfCallToReinsert = solutionWithElementsToInsert.secondPartOfCallToReinsert;


        // Since the vehicle is empty, and the order of insertion is irrelevant,
        // we can simply add the call to the vehicle.
        //         System.out.println("vehicle was empty");
        newSolutionRepresentation.add(stopIndexOfVehicle, firstPartOfCallToReinsert);
        newSolutionRepresentation.add(stopIndexOfVehicle, secondPartOfCallToReinsert);
        //        System.out.println("newSolutionRepresentation = " + newSolutionRepresentation);
        return newSolution;
    }

    private static IVectorSolutionRepresentation<Integer> getSolutionWhenStartingAtEmptyOutsourcedCallsVehicle(
            SolutionWithElementsToInsert solutionWithElementsToInsert) {
        IVectorSolutionRepresentation<Integer> newSolution = solutionWithElementsToInsert.solution;
        List<Integer> newSolutionRepresentation = newSolution.getSolutionRepresentation();
        Integer firstPartOfCallToReinsert = solutionWithElementsToInsert.firstPartOfCallToReinsert;
        Integer secondPartOfCallToReinsert = solutionWithElementsToInsert.secondPartOfCallToReinsert;

        // Since there are no other outsourced calls, and the order of the
        // insertion is irrelevant, we can simply append the call to the end of the solution.
        newSolutionRepresentation.add(firstPartOfCallToReinsert);
        newSolutionRepresentation.add(secondPartOfCallToReinsert);
        //    System.out.println("startIndexOfVehicle = " + startIndexOfVehicle);
        //    System.out.println("newSolutionRepresentation add to end = " + newSolutionRepresentation);
        return newSolution;
    }

    private static IVectorSolutionRepresentation<Integer> solutionWhenMovingOutsourcedCalls(
            SolutionWithElementsToInsert solutionWithElementsToInsert, int startIndexOfVehicle) {
        IVectorSolutionRepresentation<Integer> newSolution = solutionWithElementsToInsert.solution;
        List<Integer> newSolutionRepresentation = newSolution.getSolutionRepresentation();
        Integer firstPartOfCallToReinsert = solutionWithElementsToInsert.firstPartOfCallToReinsert;
        Integer secondPartOfCallToReinsert = solutionWithElementsToInsert.secondPartOfCallToReinsert;
        int stopIndexOfVehicle;
        stopIndexOfVehicle = newSolutionRepresentation.size();
        //    System.out.println("startIndexOfVehicle = " + startIndexOfVehicle);
        //    System.out.println("stopIndexOfVehicle = " + stopIndexOfVehicle);
        List<Integer> excludedIndexes = new ArrayList<>();
        int randomIndexOne = findRandomIndexWithinExclusiveBounds(
                startIndexOfVehicle, stopIndexOfVehicle + 1, excludedIndexes);
        if (randomIndexOne >= newSolutionRepresentation.size()) {
            newSolutionRepresentation.add(firstPartOfCallToReinsert);
        } else {
            newSolutionRepresentation.add(randomIndexOne, firstPartOfCallToReinsert);
        }
        //      System.out.println("newSolutionRepresentation = " + newSolutionRepresentation);
        int randomIndexTwo = findRandomIndexWithinExclusiveBounds(
                startIndexOfVehicle, stopIndexOfVehicle + 2, excludedIndexes);
        if (randomIndexTwo >= newSolutionRepresentation.size()) {
            newSolutionRepresentation.add(secondPartOfCallToReinsert);
        } else {
            newSolutionRepresentation.add(randomIndexTwo, secondPartOfCallToReinsert);
        }
        //       System.out.println("newSolutionRepresentation = " + newSolutionRepresentation);
        return newSolution;
    }

/*
      //  System.out.println("zeroIndices old = " + zeroIndices);
        zeroIndices = getIndicesOfAllZeroes(newSolutionRepresentation);
        int randomVehicle = RANDOM.nextInt(zeroIndices.size());
     //   System.out.println("zeroIndices new = " + zeroIndices);
     //   System.out.println("randomVehicle = " + randomVehicle);
     //   System.out.println("newSolutionRepresentation = " + newSolutionRepresentation);
        int startIndexOfVehicle, stopIndexOfVehicle;
        if (randomVehicle == 0) {
            startIndexOfVehicle = -1;
            stopIndexOfVehicle = zeroIndices.get(0);
        } else {
            startIndexOfVehicle = zeroIndices.get(randomVehicle);
            if (startIndexOfVehicle == newSolutionRepresentation.size() - 1) {
                // Since there are no other outsourced calls, and the order of the
                // insertion is irrelevant, we can simply append the call to the end of the solution.
                newSolutionRepresentation.add(firstPartOfCallToReinsert);
                newSolutionRepresentation.add(secondPartOfCallToReinsert);
       //         System.out.println("startIndexOfVehicle = " + startIndexOfVehicle);
        //        System.out.println("newSolutionRepresentation add to end = " + newSolutionRepresentation);
                return newSolution;
            } else if (randomVehicle == zeroIndices.size() - 1) {
                stopIndexOfVehicle = newSolutionRepresentation.size();
        //        System.out.println("startIndexOfVehicle = " + startIndexOfVehicle);
        //        System.out.println("stopIndexOfVehicle = " + stopIndexOfVehicle);
                List<Integer> excludedIndexes = new ArrayList<>();
                int randomIndexOne = findRandomIndexWithinVehicle(
                        startIndexOfVehicle, stopIndexOfVehicle + 1, excludedIndexes);
                if (randomIndexOne > newSolutionRepresentation.size()) {
                    newSolutionRepresentation.add(firstPartOfCallToReinsert);
                } else {
                    newSolutionRepresentation.add(randomIndexOne, firstPartOfCallToReinsert);
                }
        //        System.out.println("newSolutionRepresentation = " + newSolutionRepresentation);
                int randomIndexTwo = findRandomIndexWithinVehicle(
                        startIndexOfVehicle, stopIndexOfVehicle + 1, excludedIndexes);
                if (randomIndexTwo > newSolutionRepresentation.size()) {
                    newSolutionRepresentation.add(secondPartOfCallToReinsert);
                } else {
                    newSolutionRepresentation.add(randomIndexTwo, secondPartOfCallToReinsert);
                }
       //         System.out.println("newSolutionRepresentation = " + newSolutionRepresentation);
                return newSolution;
            } else {
                stopIndexOfVehicle = zeroIndices.get(randomVehicle + 1);
            }
        }
        if (stopIndexOfVehicle == startIndexOfVehicle + 1) {
            // Since the vehicle is empty, and the order of insertion is irrelevant,
            // we can simply add the call to the vehicle.
        //    System.out.println("startIndexOfVehicle = " + startIndexOfVehicle);
        //    System.out.println("stopIndexOfVehicle = " + stopIndexOfVehicle);
        //    System.out.println("vehicle was empty");
            newSolutionRepresentation.add(stopIndexOfVehicle, firstPartOfCallToReinsert);
            newSolutionRepresentation.add(stopIndexOfVehicle, secondPartOfCallToReinsert);
       //     System.out.println("newSolutionRepresentation = " + newSolutionRepresentation);
            return newSolution;
        }
     //   System.out.println("startIndexOfVehicle = " + startIndexOfVehicle);
      //  System.out.println("stopIndexOfVehicle = " + stopIndexOfVehicle);
      //  System.out.println("newSolutionRepresentation = " + newSolutionRepresentation);
        List<Integer> otherIndex = new ArrayList<>();
        int randomIndexOne = findRandomIndexWithinVehicle(startIndexOfVehicle, stopIndexOfVehicle + 1, otherIndex);
     //   System.out.println("randomIndexOne = " + randomIndexOne);
        newSolutionRepresentation.add(randomIndexOne, firstPartOfCallToReinsert);
     //   System.out.println("newSolutionRepresentation = " + newSolutionRepresentation);
        int randomIndexTwo = findRandomIndexWithinVehicle(startIndexOfVehicle, stopIndexOfVehicle + 2, otherIndex);
     //   System.out.println("randomIndexTwo = " + randomIndexTwo);
        newSolutionRepresentation.add(randomIndexTwo, secondPartOfCallToReinsert);
     //   System.out.println("newSolutionRepresentation = " + newSolutionRepresentation);
        return newSolution;//
    }//*/

    private static class SolutionWithElementsToInsert {
        private final IVectorSolutionRepresentation<Integer> solution;
        private final Integer firstPartOfCallToReinsert;
        private final Integer secondPartOfCallToReinsert;

        public SolutionWithElementsToInsert(IVectorSolutionRepresentation<Integer> sol,
                                            Integer firstPartOfCallToReinsert,
                                            Integer secondPartOfCallToReinsert) {
            this.solution = sol;
            this.firstPartOfCallToReinsert = firstPartOfCallToReinsert;
            this.secondPartOfCallToReinsert = secondPartOfCallToReinsert;
        }
    }
}
