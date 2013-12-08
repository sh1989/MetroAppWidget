package uk.co.samhogy.metroappwidget;

public class Log {
    private static final String TAG = "uk.co.samhogy.metroappwidget";
    
    public static void debug(String message) {
        android.util.Log.d(TAG, message);
    }

    public static void error(String message, Exception e) {
        android.util.Log.e(TAG, message, e);
    }
}
