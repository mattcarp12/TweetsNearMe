package org.matt.tweetsnearme.DI;

import android.app.Application;

import org.matt.tweetsnearme.Network.TwitterApi;
import org.matt.tweetsnearme.Network.TwitterService;
import org.matt.tweetsnearme.Repository.TweetRepository;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

@Module
public class AppModule {

    @Singleton
    @Provides
    static OkHttpClient provideOkHttpClient() {
        return new OkHttpClient.Builder()
                .addInterceptor(new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
                .build();

        /* Legacy implementation of authorization

            .addInterceptor(new Interceptor() {
                    @Override
                    public okhttp3.Response intercept(Chain chain) throws IOException {
                        Request originalRequest = chain.request();
                        Request.Builder builder = originalRequest.newBuilder().header("Authorization",
                                token != null ? token.getAuthorization() : credentials);
                        Request newRequest = builder.build();
                        return chain.proceed(newRequest);
                    }
                })
         */
    }

    @Singleton
    @Provides
    static Retrofit provideRetrofit(OkHttpClient okHttpClient) {
        return new Retrofit.Builder()
                .baseUrl(TwitterApi.BASE_URL)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
    }

    @Singleton
    @Provides
    static TwitterApi provideTwitterApi(Retrofit retrofit) {
        return retrofit.create(TwitterApi.class);
    }

    @Singleton
    @Provides
    static TwitterService provideTwitterService(TwitterApi twitterApi) {
        return new TwitterService(twitterApi);
    }

    @Singleton
    @Provides
    static TweetRepository provideTweetRepository(Application application, TwitterService twitterService) {
        return new TweetRepository(application, twitterService);
    }

}
