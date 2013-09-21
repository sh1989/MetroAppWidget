
package uk.co.samhogy.metroappwidget.web;

import android.util.Log;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;

public class StopBoardRequest {

    public static String getTimesForStation(String url) {
        Log.d("MetroAppWidget", "making web request: " + url);

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

            return sb.toString();
        } catch (Exception e) {
            Log.e("MetroAppWidget", "Error retrieving data from web", e);
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
