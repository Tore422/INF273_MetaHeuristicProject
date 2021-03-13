package pickup.and.delivery.entities;

import java.util.ArrayList;
import java.util.List;

public class Vehicle {

    private int index;
    private int homeNode;
    private Integer startingTime;
    private int capacity;
    private List<Integer> possibleCalls;

    public Vehicle(int index, int homeNode, Integer startingTime, int capacity) {
        this.index = index;
        this.homeNode = homeNode;
        this.startingTime = startingTime;
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

    public Integer getStartingTime() {
        return startingTime;
    }

    public void setStartingTime(Integer startingTime) {
        this.startingTime = startingTime;
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
                ", startingTime=" + startingTime +
                ", capacity=" + capacity +
                ", possibleCalls=" + possibleCalls +
                '}';
    }
}
