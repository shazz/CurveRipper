/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package curveripper;

/**
 *
 * @author admin
 */
import java.util.Calendar;
import java.text.SimpleDateFormat;

public class DateUtils
{

    public static final String DATE_FORMAT_NOW = "HH:mm:ss.S";

    public static String now()
    {
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT_NOW);
        return sdf.format(cal.getTime());

    }
}
