package izzur.popularmovies;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;

import izzur.popularmovies.utilities.NetworkUtils;

public class MainActivity extends AppCompatActivity {

    final static String TAG = MainActivity.class.getSimpleName();
    final static String THEMOVIEDB_BASE_URL = "https://api.themoviedb.org/3/movie";
    final static String IMAGE_BASE_URL = "http://image.tmdb.org/t/p";
    final private static String API_KEY = BuildConfig.API_KEY;
    private JSONObject dataMovie = null;
    private JSONArray arrayMovie = null;
    private RecyclerView rViewMain;
    private GridLayoutManager gridLayoutManager;
    private MoviesAdapter moviesAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        rViewMain = (RecyclerView) findViewById(R.id.rv_grid);

        makeQuery("popular");

        gridLayoutManager = new GridLayoutManager(MainActivity.this, 2);
        rViewMain.setHasFixedSize(true);
        rViewMain.setLayoutManager(gridLayoutManager);

        moviesAdapter = new MoviesAdapter(MainActivity.this, 20, arrayMovie);
        rViewMain.setAdapter(moviesAdapter);
    }

    private void makeQuery(String param) {
        URL queryUrl = NetworkUtils.buildUrl(THEMOVIEDB_BASE_URL, param, null, API_KEY);
        new QueryTask().execute(queryUrl);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();

        switch (itemId) {
            case R.id.sort_popular:
                makeQuery("popular");
                return true;
            case R.id.sort_rating:
                makeQuery("top_rated");
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private class QueryTask extends AsyncTask<URL, Void, String> {
        @Override
        protected String doInBackground(URL... params) {
            URL queryUrl = params[0];
            Log.d(TAG, "URL is " + queryUrl.toString());
            String queryResult = null;
            try {
                queryResult = NetworkUtils.getResponseFromHttpUrl(queryUrl);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return queryResult;
        }

        @Override
        protected void onPostExecute(String queryResult) {
            if (queryResult != null && !queryResult.equals("")) {
                try {
                    dataMovie = new JSONObject(queryResult);
                    arrayMovie = new JSONArray(dataMovie.getString("results"));
                    moviesAdapter.updateData(arrayMovie);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                Log.d(TAG, "No http response");
            }
            Log.d(TAG, arrayMovie.toString());
        }
    }
}
