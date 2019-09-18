package org.matt.tweetsnearme.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import org.matt.tweetsnearme.Model.Tweet;
import org.matt.tweetsnearme.R;

import java.util.List;


public class TweetListAdapter extends RecyclerView.Adapter<TweetListAdapter.MyViewHolder> {
    private List<Tweet> tweetList;
    private Context context;

    public TweetListAdapter(Context context) {
        this.context = context;
    }

    public void setTweetList(List<Tweet> tweetList) {
        this.tweetList = tweetList;
        notifyDataSetChanged();
    }

    @Override
    public TweetListAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View myLayout = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.tweet_view, parent, false);
        return new MyViewHolder(myLayout);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Tweet currTweet = tweetList.get(position);
        holder.tweetUserName.setText(currTweet.getUser().getName());
        holder.tweetText.setText(currTweet.getText());
        holder.tweetDistance.setText("5");
        Picasso.with(context).load(currTweet.getUser().getProfileImageUrl()).into(holder.tweetUserImage);
    }

    @Override
    public int getItemCount() {
        return tweetList == null ? 0 : tweetList.size();
    }

    static class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView tweetUserImage;
        TextView tweetUserName;
        TextView tweetDistance;
        TextView tweetText;

        MyViewHolder(View tweetLayout) {
            super(tweetLayout);
            tweetUserImage = tweetLayout.findViewById(R.id.tweet_author_image);
            tweetUserName = tweetLayout.findViewById(R.id.tweet_username);
            tweetDistance = tweetLayout.findViewById(R.id.tweet_distance);
            tweetText = tweetLayout.findViewById(R.id.tweet_text);
        }
    }

}