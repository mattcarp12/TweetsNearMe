package org.matt.tweetsnearme.Utilities;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;
import com.twitter.sdk.android.core.models.Tweet;

import org.matt.tweetsnearme.R;

import java.util.List;


public class TweetListAdapter extends RecyclerView.Adapter<TweetListAdapter.MyViewHolder> {
    private List<Tweet> mDataset;
    private Context context;

    // Provide a suitable constructor (depends on the kind of dataset)
    public TweetListAdapter(List<Tweet> myDataset, Context context) {
        mDataset = myDataset;
        this.context = context;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public TweetListAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent,
                                                     int viewType) {
        // create a new view
        View myLayout = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.tweet_view, parent, false);
        return new MyViewHolder(myLayout);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        //holder.textView.setText(mDataset.get(position).text);
        Tweet currTweet = mDataset.get(position);
        holder.tweetUserName.setText(currTweet.user.name);
        holder.tweetText.setText(currTweet.text);
        holder.tweetDistance.setText("5");
        Picasso.with(context).load(currTweet.user.profileImageUrlHttps).into(holder.tweetUserImage);

    }

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class MyViewHolder extends RecyclerView.ViewHolder {
        public ImageView tweetUserImage;
        public TextView tweetUserName;
        public TextView tweetDistance;
        public TextView tweetText;

        public MyViewHolder(View tweetLayout) {
            super(tweetLayout);
            tweetUserImage = tweetLayout.findViewById(R.id.tweet_author_image);
            tweetUserName = tweetLayout.findViewById(R.id.tweet_username);
            tweetDistance = tweetLayout.findViewById(R.id.tweet_distance);
            tweetText = tweetLayout.findViewById(R.id.tweet_text);
        }
    }


    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.size();
    }
}