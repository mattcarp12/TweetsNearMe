package org.matt.tweetsnearme.Network;

//import com.twitter.sdk.android.core.models.Search;

import org.matt.tweetsnearme.Model.OAuthToken;
import org.matt.tweetsnearme.Model.Search;

import io.reactivex.Observable;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface TwitterApi {

    String BASE_URL = "https://api.twitter.com/";

    @FormUrlEncoded
    @POST("/oauth2/token")
    Observable<OAuthToken> postCredentials(@Field("grant_type") String grantType,
                                           @Header("Authorization") String credentials);

    @GET("/1.1/search/tweets.json")
    Observable<Search> getTweets(@Query("geocode") String geocode,
                                 @Query("count") int count,
                                 @Query("since_id") int sinceId,
                                 @Query("result_type") String resultType,
                                 @Header("Authorization") String authorization);

}
