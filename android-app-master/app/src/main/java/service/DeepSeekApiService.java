package service;

// DeepSeekApiService.java
// DeepSeekApiService.java
import model.ApiRequest;

import model.ApiResponse;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface DeepSeekApiService {
    @POST("chat/completions")
    Call<ApiResponse> createChatCompletion(
            @Header("Authorization") String authorization,
            @Header("Content-Type") String contentType,
            @Body ApiRequest request
    );
}

