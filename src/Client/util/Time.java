package Client.util;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Time {
    public static String getTime() {
        Date date = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        return dateFormat.format(date);
    }
}

