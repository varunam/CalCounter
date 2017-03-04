package util;

import java.text.DecimalFormat;

/**
 * Created by vaam on 28-02-2017.
 */
public class Utils {

    public static String formatNumber(int value)
    {
        DecimalFormat formatter = new DecimalFormat("##,##,###");
        String formatted = formatter.format(value);

        return formatted;
    }
}
