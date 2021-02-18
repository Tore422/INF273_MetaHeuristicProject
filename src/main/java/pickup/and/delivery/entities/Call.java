package pickup.and.delivery.entities;

public class Call {
    /**
     * An order to pickup a package, and deliver it to a destination.
     * Pickup and delivery must be performed within the given timeframe.
     * A package has a size, which will occupy capacity during transport,
     * and a cost to be payed if the delivery is not performed within the timeframe.
     */
    private int callIndex;
    private int originNode;
    private int destinationNode;
    private Integer packageSize;
    private Integer costOfNotTransporting;
    private int lowerBoundTimeWindowForPickup;
    private int upperBoundTimeWindowForPickup;
    private int lowerBoundTimeWindowForDelivery;
    private int upperBoundTimeWindowForDelivery;

    public Call(int callIndex, int originNode, int destinationNode,
                Integer packageSize, Integer costOfNotTransporting,
                int lowerBoundTimeWindowForPickup, int upperBoundTimeWindowForPickup,
                int lowerBoundTimeWindowForDelivery, int upperBoundTimeWindowForDelivery) {
        this.callIndex = callIndex;
        this.originNode = originNode;
        this.destinationNode = destinationNode;
        this.packageSize = packageSize;
        this.costOfNotTransporting = costOfNotTransporting;
        this.lowerBoundTimeWindowForPickup = lowerBoundTimeWindowForPickup;
        this.upperBoundTimeWindowForPickup = upperBoundTimeWindowForPickup;
        this.lowerBoundTimeWindowForDelivery = lowerBoundTimeWindowForDelivery;
        this.upperBoundTimeWindowForDelivery = upperBoundTimeWindowForDelivery;
    }

    public int getCallIndex() {
        return callIndex;
    }

    public void setCallIndex(int callIndex) {
        this.callIndex = callIndex;
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

    public Integer getPackageSize() {
        return packageSize;
    }

    public void setPackageSize(Integer packageSize) {
        this.packageSize = packageSize;
    }

    public Integer getCostOfNotTransporting() {
        return costOfNotTransporting;
    }

    public void setCostOfNotTransporting(Integer costOfNotTransporting) {
        this.costOfNotTransporting = costOfNotTransporting;
    }

    public int getLowerBoundTimeWindowForPickup() {
        return lowerBoundTimeWindowForPickup;
    }

    public void setLowerBoundTimeWindowForPickup(int lowerBoundTimeWindowForPickup) {
        this.lowerBoundTimeWindowForPickup = lowerBoundTimeWindowForPickup;
    }

    public int getUpperBoundTimeWindowForPickup() {
        return upperBoundTimeWindowForPickup;
    }

    public void setUpperBoundTimeWindowForPickup(int upperBoundTimeWindowForPickup) {
        this.upperBoundTimeWindowForPickup = upperBoundTimeWindowForPickup;
    }

    public int getLowerBoundTimeWindowForDelivery() {
        return lowerBoundTimeWindowForDelivery;
    }

    public void setLowerBoundTimeWindowForDelivery(int lowerBoundTimeWindowForDelivery) {
        this.lowerBoundTimeWindowForDelivery = lowerBoundTimeWindowForDelivery;
    }

    public int getUpperBoundTimeWindowForDelivery() {
        return upperBoundTimeWindowForDelivery;
    }

    public void setUpperBoundTimeWindowForDelivery(int upperBoundTimeWindowForDelivery) {
        this.upperBoundTimeWindowForDelivery = upperBoundTimeWindowForDelivery;
    }

    @Override
    public String toString() {
        return "Call{" +
                "callIndex=" + callIndex +
                ", originNode=" + originNode +
                ", destinationNode=" + destinationNode +
                ", packageSize=" + packageSize +
                ", costOfNotTransporting=" + costOfNotTransporting +
                ", lowerBoundTimeWindowForPickup=" + lowerBoundTimeWindowForPickup +
                ", upperBoundTimeWindowForPickup=" + upperBoundTimeWindowForPickup +
                ", lowerBoundTimeWindowForDelivery=" + lowerBoundTimeWindowForDelivery +
                ", upperBoundTimeWindowForDelivery=" + upperBoundTimeWindowForDelivery +
                '}';
    }
}
