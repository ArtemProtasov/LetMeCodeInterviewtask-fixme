package ru.protasov_dev.letmecodeinterviewtask;

import android.app.Application;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class App extends Application{

    private Retrofit retrofit;
    private static MovieReviewsApi movieReviewsApi;

    @Override
    public void onCreate() {
        super.onCreate();

        Gson gson = new GsonBuilder()
                .setDateFormat("yyyy/MM/dd")
                .create();

        retrofit = new Retrofit.Builder()
                .baseUrl("https://api.nytimes.com")
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        movieReviewsApi = retrofit.create(MovieReviewsApi.class);
    }

    public static MovieReviewsApi getApi(){
        return movieReviewsApi;
    }
}
