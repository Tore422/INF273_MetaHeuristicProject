package assignment2.entities;

public class NodeTimesAndCosts {

    private int vehicleIndex;
    private Call call;
    private int originNodeTime;
    private int originNodeCosts;
    private int destinationNodeTime;
    private int destinationNodeCosts;

    public NodeTimesAndCosts(int vehicleIndex, Call call, int originNodeTime, int originNodeCosts, int destinationNodeTime, int destinationNodeCosts) {
        this.vehicleIndex = vehicleIndex;
        this.call = call;
        this.originNodeTime = originNodeTime;
        this.originNodeCosts = originNodeCosts;
        this.destinationNodeTime = destinationNodeTime;
        this.destinationNodeCosts = destinationNodeCosts;
    }

    public int getVehicleIndex() {
        return vehicleIndex;
    }

    public void setVehicleIndex(int vehicleIndex) {
        this.vehicleIndex = vehicleIndex;
    }

    public Call getCall() {
        return call;
    }

    public void setCall(Call call) {
        this.call = call;
    }

    public int getOriginNodeTime() {
        return originNodeTime;
    }

    public void setOriginNodeTime(int originNodeTime) {
        this.originNodeTime = originNodeTime;
    }

    public int getOriginNodeCosts() {
        return originNodeCosts;
    }

    public void setOriginNodeCosts(int originNodeCosts) {
        this.originNodeCosts = originNodeCosts;
    }

    public int getDestinationNodeTime() {
        return destinationNodeTime;
    }

    public void setDestinationNodeTime(int destinationNodeTime) {
        this.destinationNodeTime = destinationNodeTime;
    }

    public int getDestinationNodeCosts() {
        return destinationNodeCosts;
    }

    public void setDestinationNodeCosts(int destinationNodeCosts) {
        this.destinationNodeCosts = destinationNodeCosts;
    }

    @Override
    public String toString() {
        return "NodeTimesAndCosts{" +
                "vehicleIndex=" + vehicleIndex +
                ", call=" + call +
                ", originNodeTime=" + originNodeTime +
                ", originNodeCosts=" + originNodeCosts +
                ", destinationNodeTime=" + destinationNodeTime +
                ", destinationNodeCosts=" + destinationNodeCosts +
                '}';
    }
}
