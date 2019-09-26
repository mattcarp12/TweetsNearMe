package org.matt.tweetsnearme.Database;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.room.Room;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.runner.AndroidJUnit4;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.matt.tweetsnearme.Model.Tweet;
import org.matt.tweetsnearme.TestUtil;

import java.io.IOException;
import java.util.List;

@RunWith(AndroidJUnit4.class)
public class DatabaseTest {
    private TweetDao tweetDao;
    private TweetDatabase db;

    @Before
    public void createDb() {
        Context context = ApplicationProvider.getApplicationContext();
        db = Room.inMemoryDatabaseBuilder(context, TweetDatabase.class).build();
        tweetDao = db.tweetDao();
    }

    @After
    public void closeDb() throws IOException {
        db.close();
    }

    @Test
    public void writeUserAndReadInList() throws Exception {
        // TODO: Create tweets and read into list
        /*
            User user = TestUtil.createUser(3);
            user.setName("george");
            userDao.insert(user);
            List<User> byName = userDao.findUsersByName("george");
            assertThat(byName.get(0), equalTo(user));
         */
        List<Tweet> tweetList = TestUtil.getTweetList();
        tweetDao.insertAll(tweetList);
        LiveData<List<Tweet>> tweetListQuery = tweetDao.getTweets();

    }
}

