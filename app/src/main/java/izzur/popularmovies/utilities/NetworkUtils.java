package izzur.popularmovies.utilities;

import android.net.Uri;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * These utilities will be used to communicate with the network.
 */

public class NetworkUtils {

    public static URL buildUrl(String baseUrl, String endpoints, String image, String apiKey) {
        Uri builtUri = Uri.parse(baseUrl).buildUpon()
                .appendPath(endpoints)
                .build();

        if (image != null) {
            builtUri = builtUri.buildUpon()
                    .appendPath(image)
                    .build();
        } else {
            builtUri = builtUri.buildUpon()
                    .appendQueryParameter("api_key", apiKey)
                    .build();
        }

        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return url;
    }

    public static String getResponseFromHttpUrl(URL url) throws IOException {
        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url(url)
                .build();

        Response response = client.newCall(request).execute();
        return response.body().string();
    }
}
