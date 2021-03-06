package ru.protasovdev.letmecodeinterviewtask;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;
import ru.protasovdev.letmecodeinterviewtask.parsetaskmanagers.PostModelCritics.PostModelCritics;
import ru.protasovdev.letmecodeinterviewtask.parsetaskmanagers.PostModelReviews.PostModelReviews;

public interface MovieApi {
    @GET("/svc/movies/v2/reviews/search.json")
    Call<PostModelReviews> getAllReviews(@Query("api-key") String apiKey,
                                         @Query("query") String query,
                                         @Query("reviewer") String reviewer,
                                         @Query("offset") int offset,
                                         @Query("order") String order);

    @GET("/svc/movies/v2/reviews/search.json")
    Call<PostModelReviews> getCriticPost(@Query("api-key") String apiKey, @Query("reviewer") String reviewer, @Query("offset") int offset);

    @GET("/svc/movies/v2/critics/{name-critic}.json")
    Call<PostModelCritics> getCritic(@Path("name-critic") String name, @Query("api-key") String apiKey);
}
