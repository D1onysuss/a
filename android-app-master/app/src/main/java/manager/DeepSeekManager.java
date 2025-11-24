package manager;

// DeepSeekManager.java
import android.content.Context;

import model.ApiRequest;
import model.ApiResponse;
import model.Message;
import service.ChatCallback;
import service.DeepSeekApiService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import java.util.ArrayList;
import java.util.List;

public class DeepSeekManager {
    private DeepSeekApiService apiService;
    private String apiKey;

    public DeepSeekManager(Context context) {
        // 从配置文件读取API密钥和基础URL
        ConfigManager configManager = new ConfigManager(context);
        this.apiKey = configManager.getApiKey();

        // 设置基础URL
        String baseUrl = configManager.getBaseUrl();
        if (baseUrl != null && !baseUrl.isEmpty()) {
            ApiClient.setBaseUrl(baseUrl);
        }

        // 正确获取API服务实例
        apiService = ApiClient.getApiService();
    }

    public void sendMessage(String userMessage, ChatCallback callback) {
        // 构建消息列表
        List<Message> messages = new ArrayList<>();
        messages.add(new Message("user", userMessage));

        // 创建API请求
        ApiRequest request = new ApiRequest(
                "deepseek-chat",  // 模型名称
                messages,
                0.7,             // temperature
                2048             // max_tokens
        );

        // 执行API调用
        Call<ApiResponse> call = apiService.createChatCompletion(
                "Bearer " + this.apiKey,
                "application/json",
                request
        );

        call.enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    ApiResponse apiResponse = response.body();
                    if (apiResponse.getChoices() != null && !apiResponse.getChoices().isEmpty()) {
                        String assistantResponse = apiResponse.getChoices().get(0).getMessage().getContent();
                        callback.onSuccess(assistantResponse);
                    } else {
                        callback.onError("No response from API");
                    }
                } else {
                    String error = "API Error: " + response.code() + " - " + response.message();
                    if (response.errorBody() != null) {
                        try {
                            error += " - " + response.errorBody().string();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    callback.onError(error);
                }
            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {
                callback.onError("Network error: " + t.getMessage());
            }
        });
    }
}