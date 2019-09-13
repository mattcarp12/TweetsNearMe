package org.matt.tweetsnearme.Database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import org.matt.tweetsnearme.Model.Coordinates;
import org.matt.tweetsnearme.Model.Tweet;
import org.matt.tweetsnearme.Model.User;

@Database(entities = {Tweet.class, User.class, Coordinates.class}, version = 1)
public abstract class TweetDatabase extends RoomDatabase {

    private static volatile TweetDatabase INSTANCE;

    public abstract TweetDao tweetDao();

    public static TweetDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (TweetDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            TweetDatabase.class, "tweet_database")
                            .build();
                }
            }
        }
        return INSTANCE;
    }

}
