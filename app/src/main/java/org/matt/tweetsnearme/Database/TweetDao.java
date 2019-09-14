package org.matt.tweetsnearme.Database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import org.matt.tweetsnearme.Model.Tweet;

import java.util.List;

import static androidx.room.OnConflictStrategy.IGNORE;

@Dao
public interface TweetDao {

    @Insert(onConflict = IGNORE)
    void insert(Tweet tweet);

    @Query("DELETE FROM Tweet")
    void deleteAll();

    @Query("SELECT * from Tweet")
    LiveData<List<Tweet>> getAllTweets();

}
