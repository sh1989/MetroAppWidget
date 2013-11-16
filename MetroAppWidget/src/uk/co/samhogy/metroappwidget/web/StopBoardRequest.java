
package uk.co.samhogy.metroappwidget.web;

import android.content.Context;
import android.util.Log;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import uk.co.samhogy.metroappwidget.log.Logger;
import uk.co.samhogy.metroappwidget.log.Time;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;

public class StopBoardRequest {

    public static String getTimesForStation(Context c, String url) {
        Log.d("MetroAppWidget", "making web request: " + url);
        Logger logger = new Logger();
        logger.appendLog(c, "Accessing URL: " + url);

        BufferedReader in = null;
        try {
            HttpClient client = new DefaultHttpClient();
            HttpGet request = new HttpGet();
            request.setURI(URI.create(url));
            HttpResponse response = client.execute(request);
            in = new BufferedReader
                    (new InputStreamReader(response.getEntity().getContent()));
            StringBuffer sb = new StringBuffer("");
            String line = "";
            while ((line = in.readLine()) != null) {
                sb.append(line);
            }
            in.close();

            logger.appendLog(c, Time.now() + " Success! " + url);
            return sb.toString();
        } catch (Exception e) {
            Log.e("MetroAppWidget", "Error retrieving data from web", e);
            logger.appendLog(c, "Error retrieving " + url + ":" + "\n" + e.getMessage());
            return "";
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    Log.e("MetroAppWidget", "Failed to close input stream.", e);
                }
            }
        }
    }
}
