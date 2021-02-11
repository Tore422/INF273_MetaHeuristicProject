package assignment2.entities;

import java.util.ArrayList;
import java.util.List;

public class Vehicle {

    private int index;
    private int homeNode;
    private Integer startingTimeInSeconds;
    private int capacity;
    private List<Integer> possibleCalls;

    public Vehicle(int index, int homeNode, Integer startingTimeInSeconds, int capacity) {
        this.index = index;
        this.homeNode = homeNode;
        this.startingTimeInSeconds = startingTimeInSeconds;
        this.capacity = capacity;
        this.possibleCalls = new ArrayList<>();
    }

    public void addPossibleCall(Integer callIndex) {
        if (callIndex != null) {
            possibleCalls.add(callIndex);
        }
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public int getHomeNode() {
        return homeNode;
    }

    public void setHomeNode(int homeNode) {
        this.homeNode = homeNode;
    }

    public Integer getStartingTimeInSeconds() {
        return startingTimeInSeconds;
    }

    public void setStartingTimeInSeconds(Integer startingTimeInSeconds) {
        this.startingTimeInSeconds = startingTimeInSeconds;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    @Override
    public String toString() {
        return "Vehicle{" +
                "index=" + index +
                ", homeNode=" + homeNode +
                ", startingTimeInSeconds=" + startingTimeInSeconds +
                ", capacity=" + capacity +
                ", possibleCalls=" + possibleCalls +
                '}';
    }
}
