package rbc.rbcaccountswidget;

/**
 * Created by User on 11/8/2015.
 */
public class Utils {

    public static String FormatCurrency(Long value) {
        String s = value.toString();
        int len = s.length();
        if (len >= 3) {
            s = s.substring(0, len - 2) + "." + s.substring(len - 2, len);
            len = len - 2;
            for (int i = len; i > 3; i--) {
                if ((len - i) % 3 == 0) {
                    s = s.substring(0, i-3) + " " + s.substring(i-3, s.length());
                }
            }
        } else if (len == 2) {
            s = "0." + s;
        } else { // len == 1
            s = "0.0" + s;
        }

        return "$ " + s;
    }
}
