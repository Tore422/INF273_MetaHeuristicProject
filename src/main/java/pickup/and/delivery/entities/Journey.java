package pickup.and.delivery.entities;

public class Journey {

    private int vehicleIndex;
    private int originNode;
    private int destinationNode;
    private int travelTime;
    private Integer travelCost;

    public Journey(int vehicleIndex, int originNode, int destinationNode, int travelTime, Integer travelCost) {
        this.vehicleIndex = vehicleIndex;
        this.originNode = originNode;
        this.destinationNode = destinationNode;
        this.travelTime = travelTime;
        this.travelCost = travelCost;
    }

    public int getVehicleIndex() {
        return vehicleIndex;
    }

    public void setVehicleIndex(int vehicleIndex) {
        this.vehicleIndex = vehicleIndex;
    }

    public int getOriginNode() {
        return originNode;
    }

    public void setOriginNode(int originNode) {
        this.originNode = originNode;
    }

    public int getDestinationNode() {
        return destinationNode;
    }

    public void setDestinationNode(int destinationNode) {
        this.destinationNode = destinationNode;
    }

    public int getTravelTime() {
        return travelTime;
    }

    public void setTravelTime(int travelTime) {
        this.travelTime = travelTime;
    }

    public Integer getTravelCost() {
        return travelCost;
    }

    public void setTravelCost(Integer travelCost) {
        this.travelCost = travelCost;
    }

    @Override
    public String toString() {
        return "Journey{" +
                "vehicleIndex=" + vehicleIndex +
                ", originNode=" + originNode +
                ", destinationNode=" + destinationNode +
                ", travelTime=" + travelTime +
                ", travelCost=" + travelCost +
                '}';
    }
}
