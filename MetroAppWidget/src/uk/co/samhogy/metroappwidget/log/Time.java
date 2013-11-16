
package uk.co.samhogy.metroappwidget.log;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class Time {

    public static String now() {
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        return sdf.format(cal.getTime());
    }

}
