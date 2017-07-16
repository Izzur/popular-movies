package izzur.popularmovies;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.novoda.merlin.MerlinsBeard;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;

import butterknife.BindView;
import butterknife.ButterKnife;
import izzur.popularmovies.utilities.NetworkUtils;

public class MainActivity extends AppCompatActivity {

    final static String TAG = MainActivity.class.getSimpleName();
    final static String THEMOVIEDB_BASE_URL = "https://api.themoviedb.org/3/movie";
    final private static String API_KEY = BuildConfig.API_KEY;
    @BindView(R.id.rv_grid)
    RecyclerView rViewMain;
    private String sortState = "popular";
    private JSONObject dataMovie = null;
    private JSONArray arrayMovie = null;
    private GridLayoutManager gridLayoutManager;
    private MoviesAdapter moviesAdapter;
    private MerlinsBeard merlinsBeard;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        merlinsBeard = MerlinsBeard.from(this);
        makeQuery(sortState);

        gridLayoutManager = new GridLayoutManager(MainActivity.this, 2);
        rViewMain.setHasFixedSize(true);
        rViewMain.setLayoutManager(gridLayoutManager);

        moviesAdapter = new MoviesAdapter(MainActivity.this, 20, arrayMovie);
        rViewMain.setAdapter(moviesAdapter);
    }


    private void makeQuery(String param) {
        if (merlinsBeard.isConnected()) {
            URL queryUrl = NetworkUtils.buildUrl(THEMOVIEDB_BASE_URL, param, null, API_KEY);
            new QueryTask().execute(queryUrl);
        } else {
            Toast.makeText(this, "No internet connection", Toast.LENGTH_LONG).show();
        }
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
                sortState = "popular";
                makeQuery(sortState);
                return true;
            case R.id.sort_rating:
                sortState = "top_rated";
                makeQuery(sortState);
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
