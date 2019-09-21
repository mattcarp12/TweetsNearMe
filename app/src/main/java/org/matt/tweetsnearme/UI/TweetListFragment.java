package org.matt.tweetsnearme.UI;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.matt.tweetsnearme.Adapters.TweetListAdapter;
import org.matt.tweetsnearme.R;
import org.matt.tweetsnearme.ViewModel.TweetViewModel;

public class TweetListFragment extends Fragment {


    private OnFragmentInteractionListener mListener;
    private RecyclerView recyclerView;
    private TweetListAdapter mAdapter;
    private TweetViewModel mViewModel;


    public TweetListFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        /*
         *   Initialize essential components that should be retained
         *   when the fragment is paused, stopped, resumed.
         * */
        super.onCreate(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(TweetViewModel.class);
        mAdapter = new TweetListAdapter(getContext());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        /*
         *   Where the view is created. Initialize the recyclerview here.
         * */
        View rootView = inflater.inflate(R.layout.fragment_recyclerview, container, false);
        recyclerView = rootView.findViewById(R.id.rv_tweet_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(mAdapter);
        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        /*
         *   After view is created, subscribe to the list of tweets (livedata object)
         *   that is located in the view model.
         * */
        mViewModel.getTweetList().observe(this, tweets ->
                mAdapter.setTweetList(tweets)
        );
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
