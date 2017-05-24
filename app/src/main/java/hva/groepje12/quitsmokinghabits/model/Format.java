package hva.groepje12.quitsmokinghabits.model;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;

public class Format {
    public static String formatDoubleToPrice(double price) {
        NumberFormat formatter = NumberFormat.getCurrencyInstance();

        DecimalFormatSymbols dfs = new DecimalFormatSymbols();
        dfs.setCurrencySymbol("€");
        dfs.setGroupingSeparator('.');
        dfs.setMonetaryDecimalSeparator(',');

        ((DecimalFormat) formatter).setDecimalFormatSymbols(dfs);
        return formatter.format(price);
    }
}
