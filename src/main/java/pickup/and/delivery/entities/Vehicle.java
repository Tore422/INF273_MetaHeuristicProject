package pickup.and.delivery.entities;

import java.util.ArrayList;
import java.util.List;

public class Vehicle {

    private int index;
    private int homeNode;
    private Integer startingTimeInHours;
    private int capacity;
    private List<Integer> possibleCalls;

    public Vehicle(int index, int homeNode, Integer startingTimeInHours, int capacity) {
        this.index = index;
        this.homeNode = homeNode;
        this.startingTimeInHours = startingTimeInHours;
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

    public Integer getStartingTimeInHours() {
        return startingTimeInHours;
    }

    public void setStartingTimeInHours(Integer startingTimeInHours) {
        this.startingTimeInHours = startingTimeInHours;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public List<Integer> getPossibleCalls() {
        return possibleCalls;
    }

    @Override
    public String toString() {
        return "Vehicle{" +
                "index=" + index +
                ", homeNode=" + homeNode +
                ", startingTimeInHours=" + startingTimeInHours +
                ", capacity=" + capacity +
                ", possibleCalls=" + possibleCalls +
                '}';
    }
}
