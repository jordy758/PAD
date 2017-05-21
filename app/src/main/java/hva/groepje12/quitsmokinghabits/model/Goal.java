package hva.groepje12.quitsmokinghabits.model;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;

public class Goal {
    private String goal;
    private double price;

    public Goal() {
    }

    public Goal(String goal, double price) {
        this.goal = goal;
        this.price = price;
    }

    public String getGoal() {
        return goal;
    }

    public void setGoal(String goal) {
        this.goal = goal;
    }

    public String getFormattedPrice() {
        NumberFormat formatter = NumberFormat.getCurrencyInstance();

        DecimalFormatSymbols dfs = new DecimalFormatSymbols();
        dfs.setCurrencySymbol("€");
        dfs.setGroupingSeparator('.');
        dfs.setMonetaryDecimalSeparator(',');

        ((DecimalFormat) formatter).setDecimalFormatSymbols(dfs);
        return formatter.format(price);
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
}
