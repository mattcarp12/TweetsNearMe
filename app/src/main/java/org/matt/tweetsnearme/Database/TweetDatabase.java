package org.matt.tweetsnearme.Database;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import org.matt.tweetsnearme.Model.Coordinates;
import org.matt.tweetsnearme.Model.Tweet;
import org.matt.tweetsnearme.Model.User;

@Database(entities = {Tweet.class, User.class, Coordinates.class}, version = 1)
public abstract class TweetDatabase extends RoomDatabase {

    private static volatile TweetDatabase INSTANCE;

    public abstract TweetDao tweetDao();

}
