package co.techmagic.hr.data.store.client;

import java.util.concurrent.TimeUnit;

import co.techmagic.hr.BuildConfig;
import co.techmagic.hr.data.store.IEmployeeApi;
import co.techmagic.hr.data.store.IUserApi;
import co.techmagic.hr.presentation.util.SharedPreferencesUtil;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;


public class ApiClient {

    private static ApiClient apiClient;
    private Retrofit retrofit;


    public static synchronized ApiClient getApiClient() {
        if (apiClient == null) {
            apiClient = new ApiClient();
        }
        return apiClient;
    }


    private ApiClient() {
        OkHttpClient client = buildClient();
        retrofit = new Retrofit.Builder()
                .baseUrl(BuildConfig.HOST_URL)
                .client(client)
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }


    public OkHttpClient buildClient() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.readTimeout(30, TimeUnit.SECONDS);
        builder.writeTimeout(30, TimeUnit.SECONDS);
        builder.connectTimeout(30, TimeUnit.SECONDS);
        builder.addInterceptor(chain -> {
            String accessToken = SharedPreferencesUtil.getAccessToken();
            Request.Builder request = chain.request().newBuilder();
            if (accessToken != null) {
                request.header("Content-Type", "application/json");
                request.addHeader("Authorization", accessToken);
            }
            return chain.proceed(request.build());
        });

        if (BuildConfig.DEBUG) {
            HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
            interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            builder.addInterceptor(interceptor);
        }
        return builder.build();
    }


    public IUserApi getUserApiClient() {
        return retrofit.create(IUserApi.class);
    }


    public IEmployeeApi getEmployeeClient() {
        return retrofit.create(IEmployeeApi.class);
    }
}
