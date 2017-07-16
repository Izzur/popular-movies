package izzur.popularmovies;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Adapter for Movies
 */

public class MoviesAdapter extends RecyclerView.Adapter<MoviesAdapter.MoviesViewHolder> {

    private static final String TAG = MoviesAdapter.class.getSimpleName();
    private static int viewHolderCount;
    private Context mContext;
    //private final ListItemClickListener mOnClickListener;
    private int mNumberItems;
    private JSONArray data;

    public MoviesAdapter(Context context, int numberOfItems, JSONArray jsonArray) {
        mContext = context;
        mNumberItems = numberOfItems;
        //mOnClickListener = listener;
        data = jsonArray;
        viewHolderCount = 0;
    }

    public void updateData(JSONArray jsonArray) {
        data = jsonArray;
        this.notifyDataSetChanged();
    }

    @Override
    public MoviesViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View v = inflater.inflate(R.layout.movie_list_item, parent, false);
        return new MoviesViewHolder(v);
    }

    @Override
    public void onBindViewHolder(MoviesViewHolder holder, final int position) {
        String imgLink = null;
        if (data != null)
            try {
                imgLink = "http://image.tmdb.org/t/p/w185" + data.getJSONObject(position).getString("poster_path");
                Picasso.with(mContext).load(imgLink).into(holder.posterImage);
                holder.posterImage
                        .setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                //Toast.makeText(mContext, "Image clicked", Toast.LENGTH_SHORT).show();
                                Intent i = new Intent(mContext.getApplicationContext(), DetailActivity.class);
                                try {
                                    i.putExtra("dataJSON", data.getJSONObject(position).toString());
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                mContext.startActivity(i);
                            }
                        });
            } catch (JSONException e) {
                e.printStackTrace();
            }
    }

    @Override
    public int getItemCount() {
        return mNumberItems;
    }

    public interface ListItemClickListener {
        void onListItemClick(int clickedItemIndex);
    }

    class MoviesViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.iv_poster_image)
        ImageView posterImage;

        public MoviesViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            ButterKnife.bind(this, itemView);
        }

        @Override
        public void onClick(View v) {
            //int clickedPosition = getAdapterPosition();
        }
    }
}
