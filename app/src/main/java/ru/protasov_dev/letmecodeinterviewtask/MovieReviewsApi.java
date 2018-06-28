package ru.protasov_dev.letmecodeinterviewtask;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;
import ru.protasov_dev.letmecodeinterviewtask.ParseTaskManagers.PostModelCritics.PostModelCritics;
import ru.protasov_dev.letmecodeinterviewtask.ParseTaskManagers.PostModelReviews.PostModelReviews;

public interface MovieReviewsApi {
    @GET("/svc/movies/v2/reviews/search.json")
    Call<List<PostModelReviews>> getAllReviews(@Query("api-key") String apiKey);

    @GET("/svc/movies/v2/reviews/search.json")
    Call<List<PostModelReviews>> getReviewsByKeywords(@Query("api-key") String apiKey,
                                                      @Query("query") String keywords);

    @GET("/svc/movies/v2/reviews/search.json")
    Call<List<PostModelReviews>> getReviewsByDate(@Query("api-key") String apiKey,
                                                  @Query("publication-date") String date);

    @GET("/svc/movies/v2/reviews/search.json")
    Call<List<PostModelReviews>> getReviewsByKeywordsAndDate(@Query("api-key") String apiKey,
                                                             @Query("query") String keywords,
                                                             @Query("publication-date") String date);

    @GET("/svc/movies/v2/critics/all.json")
    Call<List<PostModelCritics>> getAllCritics(@Query("api-key") String apiKey);


    @GET("/svc/movies/v2/critics/{name-critic}.json")
    Call<List<PostModelCritics>> getCriticsByName(@Query("api-key") String apiKey);
}
