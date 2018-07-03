package ru.protasov_dev.letmecodeinterviewtask;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;
import ru.protasov_dev.letmecodeinterviewtask.ParseTaskManagers.PostModelCritics.PostModelCritics;
import ru.protasov_dev.letmecodeinterviewtask.ParseTaskManagers.PostModelReviews.PostModelReviews;

public interface MovieReviewsApi {
    @GET("/svc/movies/v2/reviews/search.json")
    Call<PostModelReviews> getAllReviews(@Query("api-key") String apiKey,
                                         @Query("query") String query,
                                         @Query("reviewer") String reviewer,
                                         @Query("offset") int offset,
                                         @Query("order") String order);

    @GET("/svc/movies/v2/reviews/search.json")
    Call<PostModelReviews> getCriticPost(@Query("api-key") String apiKey, @Query("reviewer") String reviewer);

    @GET("/svc/movies/v2/critics/{name-critic}.json")
    Call<PostModelCritics> getCritic(@Path("name-critic") String name, @Query("api-key") String apiKey);
}
