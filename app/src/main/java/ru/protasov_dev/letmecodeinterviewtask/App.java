package ru.protasov_dev.letmecodeinterviewtask;

import android.app.Application;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class App extends Application{

    private static MovieReviewsApi movieReviewsApi;
    private static final String BASE_URL = "https://api.nytimes.com";

    @Override
    public void onCreate() {
        super.onCreate();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        movieReviewsApi = retrofit.create(MovieReviewsApi.class);
    }

    public static MovieReviewsApi getApi(){
        return movieReviewsApi;
    }
}
