package org.matt.tweetsnearme.Database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import org.matt.tweetsnearme.Model.Tweet;

import java.util.List;

import io.reactivex.Completable;

import static androidx.room.OnConflictStrategy.IGNORE;

@Dao
public interface TweetDao {

    @Insert(onConflict = IGNORE)
    Completable insertAll(List<Tweet> tweets);

    @Query("DELETE FROM Tweet")
    Completable deleteAll();

    @Query("SELECT * from Tweet")
    LiveData<List<Tweet>> getTweets();

}
