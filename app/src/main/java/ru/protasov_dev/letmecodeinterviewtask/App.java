package ru.protasov_dev.letmecodeinterviewtask;

import android.app.Application;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class App extends Application{

    private Retrofit retrofit;
    private static MovieReviewsApi movieReviewsApi;

    @Override
    public void onCreate() {
        super.onCreate();

        retrofit = new Retrofit.Builder()
                .baseUrl("https://api.nytimes.com")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        movieReviewsApi = retrofit.create(MovieReviewsApi.class);
    }

    public static MovieReviewsApi getApi(){
        return movieReviewsApi;
    }
}
