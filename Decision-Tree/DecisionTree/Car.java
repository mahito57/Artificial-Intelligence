package DecisionTree;

public class Car {
    private String buyingPrice;
    private String maintenancePrice;
    private String numberOfDoors;
    private String numberOfPersons;
    private String luggageBoot;
    private String safety;
    private String classification;

    public Car(String buyingPrice, String maintenancePrice, String numberOfDoors, String numberOfPersons, String luggageBoot, String safety, String classification) {
        this.buyingPrice = buyingPrice;
        this.maintenancePrice = maintenancePrice;
        this.numberOfDoors = numberOfDoors;
        this.numberOfPersons = numberOfPersons;
        this.luggageBoot = luggageBoot;
        this.safety = safety;
        this.classification = classification;
    }

    // Getter and setter methods for all attributes
    public String getBuyingPrice() {
        return buyingPrice;
    }

    public void setBuyingPrice(String buyingPrice) {
        this.buyingPrice = buyingPrice;
    }

    public String getMaintenancePrice() {
        return maintenancePrice;
    }

    public void setMaintenancePrice(String maintenancePrice) {
        this.maintenancePrice = maintenancePrice;
    }

    public String getNumberOfDoors() {
        return numberOfDoors;
    }

    public void setNumberOfDoors(String numberOfDoors) {
        this.numberOfDoors = numberOfDoors;
    }

    public String getNumberOfPersons() {
        return numberOfPersons;
    }

    public void setNumberOfPersons(String numberOfPersons) {
        this.numberOfPersons = numberOfPersons;
    }

    public String getLuggageBoot() {
        return luggageBoot;
    }

    public void setLuggageBoot(String luggageBoot) {
        this.luggageBoot = luggageBoot;
    }

    public String getSafety() {
        return safety;
    }

    public void setSafety(String safety) {
        this.safety = safety;
    }

    public String getClassification() {
        return classification;
    }

    public void setClassification(String classification) {
        this.classification = classification;
    }

    public String getAttributeValue(String attributeName) {
        switch (attributeName) {
            case "buyingPrice":
                return this.buyingPrice;
            case "maintenancePrice":
                return this.maintenancePrice;
            case "numberOfDoors":
                return this.numberOfDoors;
            case "numberOfPersons":
                return this.numberOfPersons;
            case "luggageBoot":
                return this.luggageBoot;
            case "safety":
                return this.safety;
            case "classification":
                return this.classification;
            default:
                throw new IllegalArgumentException("Invalid attribute name: " + attributeName);
        }
    }

    @Override
    public String toString() {
        return "Car{" +
                "buyingPrice='" + buyingPrice + '\'' +
                ", maintenancePrice='" + maintenancePrice + '\'' +
                ", numberOfDoors='" + numberOfDoors + '\'' +
                ", numberOfPersons='" + numberOfPersons + '\'' +
                ", luggageBoot='" + luggageBoot + '\'' +
                ", safety='" + safety + '\'' +
                ", classification='" + classification + '\'' +
                '}';
    }

    public static void main(String[] args) {
        // Example usage:
        Car car = new Car("med", "low", "4", "4", "big", "high", "good");
        System.out.println(car);
    }
}

