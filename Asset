package invest_wise;

import java.text.NumberFormat;
import java.util.Locale;

public class Asset {
    public String type;
    public String name;
    public double value;

    public Asset(String type, String name, double value) {
        this.type = type;
        this.name = name;
        this.value = value;
    }

    @Override
    public String toString() {
        NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(Locale.US);
        return String.format("%-12s %-20s %10s",
                type + ":",
                name,
                currencyFormat.format(value));
    }
}
