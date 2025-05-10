package invest_wise;

public class Asset {
    String type;
    String name;
    double value;

    public Asset(String type, String name, double value) {
        this.type = type;
        this.name = name;
        this.value = value;
    }

    public String toCSV() {
        return type + "," + name + "," + value;
    }

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

    @Override
    public String toString() {
        return String.format("⇛ %-12s %-20s $%,.2f", type + ":", name, value);
    }
}
