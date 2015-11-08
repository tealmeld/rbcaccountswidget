package rbc.rbcaccountswidget;

/**
 * Created by User on 11/8/2015.
 */
public class Utils {

    public static String FormatCurrency(Long value) {
        String s = value.toString();
        int len = s.length();
        s = s.substring(0, len - 2) + "." + s.substring(len - 2, len);
        return s;
    }
}
