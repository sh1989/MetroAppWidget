
package uk.co.samhogy.metroappwidget.log;

import android.content.Context;
import android.os.Environment;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class Logger {

    private final String FILENAME = "metroappwidget_log.txt";

    public void appendLog(Context c, String text)
    {
        if (!canWrite()) {
            return;
        }
        String toWrite = Time.now() + "\n" + text + "\n\n";

        File f = new File(c.getExternalFilesDir(null), FILENAME);

        FileOutputStream writer = null;
        try {
            writer = new FileOutputStream(f, true);
            writer.write(toWrite.getBytes());
            writer.flush();

            writer.close();
        }
        // this should never happen!
        catch (FileNotFoundException e) {
        } catch (IOException e) {
        } finally {
            if (writer != null) {
                try {
                    writer.close();
                } catch (IOException e) {
                }
            }
        }
    }

    private boolean canWrite() {
        String state = Environment.getExternalStorageState();

        if (Environment.MEDIA_MOUNTED.equals(state)) {
            return true;
        }

        return false;
    }
}
