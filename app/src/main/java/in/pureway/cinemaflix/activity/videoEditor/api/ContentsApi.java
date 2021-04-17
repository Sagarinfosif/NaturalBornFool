package in.pureway.cinemaflix.activity.videoEditor.api;

import retrofit2.Call;
import retrofit2.http.GET;

public interface ContentsApi {

//    @GET("/api/v3/{api_key}?dev=true")
//    Call<ContentsResponse> getContents(@Path("api_key") String apiKey);

    @GET("/api/v3/fb032156dcc0745d350314dc\n?dev=true")
    Call<ModelContentsResponse> getContents1();
}