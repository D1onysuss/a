package manager;

// ApiClient.java
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import service.DeepSeekApiService;

import java.util.concurrent.TimeUnit;

public class ApiClient {
    private static Retrofit retrofit = null;
    private static String baseUrl = "https://api.deepseek.com/v1/"; // 默认URL

    // 设置基础URL（从配置文件中读取）
    public static void setBaseUrl(String url) {
        baseUrl = url;
        retrofit = null; // 重置retrofit实例，以便使用新的URL
    }

    public static Retrofit getClient() {
        if (retrofit == null) {
            // 创建日志拦截器
            HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
            logging.setLevel(HttpLoggingInterceptor.Level.BODY);

            // 创建OkHttp客户端
            OkHttpClient client = new OkHttpClient.Builder()
                    .addInterceptor(logging)
                    .connectTimeout(50, TimeUnit.SECONDS)
                    .readTimeout(50, TimeUnit.SECONDS)
                    .writeTimeout(50, TimeUnit.SECONDS)
                    .build();

            retrofit = new Retrofit.Builder()
                    .baseUrl(baseUrl)
                    .client(client)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }

    public static DeepSeekApiService getApiService() {
        return getClient().create(DeepSeekApiService.class);
    }
}
