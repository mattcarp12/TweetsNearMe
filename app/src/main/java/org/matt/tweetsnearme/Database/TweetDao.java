package org.matt.tweetsnearme.Database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import org.matt.tweetsnearme.Model.Tweet;

import java.util.List;

@Dao
public interface TweetDao {

    @Insert
    void insert(Tweet tweet);

    @Query("DELETE FROM tweets")
    void deleteAll();

    @Query("SELECT * from tweets")
    LiveData<List<Tweet>> getAllTweets();

}
