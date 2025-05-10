package invest_wise;

/**
 * Represents a financial asset in the InvestWise application.
 * This class stores information about different types of assets including their type, name, and value.
 */
public class Asset {
    /** The type of the asset (e.g., "Stock", "Real Estate", "Cash") */
    String type;
    /** The name or identifier of the asset */
    String name;
    /** The monetary value of the asset */
    double value;

    /**
     * Constructs a new Asset with the specified type, name, and value.
     *
     * @param type The type of the asset
     * @param name The name of the asset
     * @param value The monetary value of the asset
     */
    public Asset(String type, String name, double value) {
        this.type = type;
        this.name = name;
        this.value = value;
    }

    /**
     * Converts the asset information to a CSV string format.
     *
     * @return A string representation of the asset in CSV format
     */
    public String toCSV() {
        return type + "," + name + "," + value;
    }

    /**
     * Creates an Asset object from a CSV string.
     *
     * @param csv The CSV string containing asset information
     * @return A new Asset object if the CSV is valid, null otherwise
     */
    public static Asset fromCSV(String csv) {
        try {
            String[] parts = csv.split(",");
            if (parts.length == 3) {
                String type = parts[0];
                String name = parts[1];
                double value = Double.parseDouble(parts[2]);
                return new Asset(type, name, value);
            }
        } catch (Exception e) {
            return null;
        }
        return null;
    }

    /**
     * Returns a formatted string representation of the asset.
     *
     * @return A formatted string showing the asset's type, name, and value
     */
    @Override
    public String toString() {
        return String.format("⇛ %-12s %-20s $%,.2f", type + ":", name, value);
    }
}
