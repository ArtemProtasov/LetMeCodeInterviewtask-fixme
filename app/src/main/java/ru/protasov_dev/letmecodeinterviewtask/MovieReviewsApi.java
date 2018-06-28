package ru.protasov_dev.letmecodeinterviewtask;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;
import ru.protasov_dev.letmecodeinterviewtask.ParseTaskManagers.PostModelReviews.PostModel;

public interface MovieReviewsApi {
    @GET("/svc/movies/v2/reviews/search.json")
    Call<List<PostModel>> getAllReviews(@Query("api-key") String apiKey);

    @GET("/svc/movies/v2/reviews/search.json")
    Call<List<PostModel>> getReviewsByKeywords(@Query("api-key") String apiKey,
                                               @Query("query") String keywords);

    @GET("/svc/movies/v2/reviews/search.json")
    Call<List<PostModel>> getReviewsByDate(@Query("api-key") String apiKey,
                                           @Query("publication-date") String date);

    @GET("/svc/movies/v2/reviews/search.json")
    Call<List<PostModel>> getReviewsByKeywordsAndDate(@Query("api-key") String apiKey,
                                                      @Query("query") String keywords,
                                                      @Query("publication-date") String date);
}
