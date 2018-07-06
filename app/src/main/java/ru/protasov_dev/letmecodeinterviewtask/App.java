package ru.protasov_dev.letmecodeinterviewtask;

import android.app.Application;
import android.content.Context;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class App extends Application {

    private static MovieApi movieApi;
    private static final String BASE_URL = "https://api.nytimes.com";

    @Override
    public void onCreate() {
        super.onCreate();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        movieApi = retrofit.create(MovieApi.class);
    }

    public static MovieApi getApi() {
        return movieApi;
    }
}
