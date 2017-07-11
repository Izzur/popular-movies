package izzur.popularmovies;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

public class DetailActivity extends AppCompatActivity {

    private TextView mTitle, mOverview, mDate, mVote;
    private ImageView mPoster;
    private JSONObject dataMovie;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        mTitle = (TextView) findViewById(R.id.tv_title);
        mOverview = (TextView) findViewById(R.id.tv_overview);
        mDate = (TextView) findViewById(R.id.tv_release_date);
        mVote = (TextView) findViewById(R.id.tv_vote_average);
        mPoster = (ImageView) findViewById(R.id.iv_detail_image);

        Intent i = getIntent();
        String imgLink;
        try {
            dataMovie = new JSONObject(i.getStringExtra("dataJSON"));
            mTitle.setText(dataMovie.getString("title"));
            mOverview.setText(dataMovie.getString("overview"));
            mDate.setText(dataMovie.getString("release_date"));
            mVote.setText(dataMovie.getString("vote_average"));
            imgLink = "http://image.tmdb.org/t/p/w185" + dataMovie.getString("poster_path");
            Picasso.with(this).load(imgLink).into(mPoster);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
